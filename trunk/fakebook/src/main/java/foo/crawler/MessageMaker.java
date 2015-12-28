package foo.crawler;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import foo.dao.MessageBaseDao;
import foo.dao.MessageDao;
import foo.dao.PersonDao;
import foo.entity.Message;
import foo.entity.MessageBase;
import foo.entity.Person;

/**
 * 亂數產生訊息的製造器
 * @author phil
 */
@Component
@Transactional
public class MessageMaker {

	private static Logger log = Logger.getLogger(MessageMaker.class);

	@Autowired
	private PersonDao personDao;
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private MessageBaseDao messageBaseDao;

	/**
	 *  產生亂數的發佈訊息
	 */
	public void make() {
		List<MessageBase> messagebase = messageBaseDao.findAll();
		Seed base = new Seed(messagebase.size());
		// random date seed
		DateTime start = new DateTime(new DateTime(2009, 1, 1, 0, 0, 0, 0));
		DateTime end = new DateTime(2009, 6, 26, 0, 0, 0, 0);
		DateSeed dateSeed = new DateSeed(start, end);
		// random message seed
		Seed size = new Seed(6);
		for (Person person : personDao.findAll()) {
			for (Person friend : person.getFriends()) {
				int s = size.next() + 1;
				for (int i = 0; i < s; i++) {
					Message msg = new Message(messagebase.get(base.next())
							.getContent());
					msg.setStamp(dateSeed.next());
					person.post(msg, friend);
					log.debug("save message:" + msg.getContent());
					messageDao.save(msg);
				}
			}
		}
	}

	/**
	 * 執行亂數發佈訊息的 main function
	 * @param args
	 */
	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring.xml" });
		MessageMaker maker = (MessageMaker) context.getBean("messageMaker");
		maker.make();

	}

}
