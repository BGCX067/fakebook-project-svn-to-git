package foo.crawler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import foo.dao.MessageBaseDao;
import foo.dao.PersonDao;
import foo.entity.MessageBase;
import foo.entity.Person;
import foo.entity.Person.Gender;
import foo.util.Native2AsciiUtils;

/**
 * Plurk Crawler 負責抓取 Plurk 上公開的資訊，並且 parse 文字存到資料庫中
 * @author phil
 */
@Component
public class PlurkCrawler {

	//抓取的人數
	private static final int FANS_SIZE = 80;
	private static final int WAIT_FOR_ALL_THREAD = 36000;
	private static final Logger log = Logger.getLogger(PlurkCrawler.class);
	private static final String IMG_URL = "http://avatars.plurk.com/%s-big%s.jpg";
	private static final String USER_URL = "http://www.plurk.com/Users/fetchUserInfo?user_id=%s";
	private static final Pattern USER = Pattern
			.compile("\"uid\": (\\d+).*?\"nick_name\": \"(.*?)\".*?\"has_profile_image\": 1.*?\"avatar\": (.*?), \"full_name\": \"(.+?)\".*?\"gender\": (\\d?)");
	private static final String UID_LINK = "http://www.plurk.com/Friends/showFansBasic?user_id=201312&offset=%s";
	private static final Pattern UID = Pattern
			.compile("http://avatars.plurk.com/(\\d+)-medium.*?");
	private static final Pattern MESSAGE = Pattern
			.compile("content_raw\\\\\": \\\\\"(.*?)\\\\\",");

	@Autowired
	private PersonDao personDao;
	@Autowired
	private MessageBaseDao messageBaseDao;
	private DefaultHttpClient client;

	public PlurkCrawler() {
		this.client = new DefaultHttpClient();
		// proxy setting
		// client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
		// new HttpHost("172.28.66.108", 8080, "http"));
	}

	/**
	 * 抓取使用者資訊
	 * @throws Exception
	 */
	public void crawlUser() throws Exception {
		Collection<String> uids = getUidSet();
		ExecutorService service = Executors.newSingleThreadExecutor();
		for (String uid : uids) {
			service.execute(new GetPersonThread(uid));
		}
		service.shutdown();
		try {
			service.awaitTermination(WAIT_FOR_ALL_THREAD, TimeUnit.SECONDS);
		} catch (InterruptedException ignored) {
		}
	}

	/**
	 * 抓取訊息
	 * @throws Exception
	 */
	public void crawlMessage() throws Exception {
		Collection<String> uids = getUidSet();
		for (String uid : uids) {
			try {
				String msg = client.execute(new HttpGet(String.format(USER_URL,
						uid)), new BasicResponseHandler());
				Matcher m = MESSAGE.matcher(msg);
				if (m.find()) {
					String message = m.group(1);
					if (!StringUtils.contains(message, "http:")) {
						MessageBase mb = new MessageBase(Native2AsciiUtils
								.ascii2Native(message));
						messageBaseDao.save(mb);
					}
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

	/**
	 * 停止時 shutdown http client
	 */
	public void stop() {
		client.getConnectionManager().shutdown();
	}

	private Collection<String> getUidSet() throws Exception {
		Set<String> uids = new HashSet<String>();
		for (int i = 0; i < FANS_SIZE; i += 10) {
			String msg = client.execute(
					new HttpGet(String.format(UID_LINK, i)),
					new BasicResponseHandler());
			Matcher m = UID.matcher(msg);
			while (m.find()) {
				uids.add(m.group(1));
			}
		}

		return uids;
	}

	public class GetPersonThread extends Thread {

		private String uid;

		public GetPersonThread(final String uid) {
			this.uid = uid;
		}

		@Override
		public void run() {

			log.debug("get person information uid : " + uid);
			Person person = null;
			String msg;
			try {
				msg = client.execute(new HttpGet(String.format(USER_URL, uid)),
						new BasicResponseHandler());
				Matcher m = USER.matcher(msg);
				if (m.find()) {
					person = new Person();
					person.setName(Native2AsciiUtils.ascii2Native(m.group(4)));
					person.setGender(Gender.value(m.group(5)));
					String avatar = ("null".equals(m.group(3))) ? "" : m.group(
							3).replaceAll("\"", "");
					String url = String.format(IMG_URL, m.group(1), avatar);
					HttpResponse res = client.execute(new HttpGet(url));
					person.setPhoto(IOUtils.toByteArray(res.getEntity()
							.getContent()));
				} else {
					log.debug(msg);
				}
				if (person != null) {
					personDao.save(person);
				}
			} catch (Exception e) {
				log.error("get person information error uid is :" + uid, e);
			}
		}
	}

	/**
	 * 開始抓取的 main function
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring.xml" });
		PlurkCrawler crawler = (PlurkCrawler) context.getBean("plurkCrawler");
		crawler.crawlUser();
		crawler.crawlMessage();
		crawler.stop();

	}

}
