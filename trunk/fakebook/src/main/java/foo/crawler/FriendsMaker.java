package foo.crawler;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import foo.dao.PersonDao;
import foo.entity.Person;

/**
 * 亂數製造朋友關係
 * @author phil
 */
@Component
@Transactional
public class FriendsMaker {

	private static final Logger log = Logger.getLogger(FriendsMaker.class);

	@Autowired
	private PersonDao personDao;

	/**
	 * 亂數的朋友關係製造器
	 */
	public void make() {

		List<Person> all = personDao.findAll();
		Seed seed = new Seed(all.size());
		//每人產生 1~6 個朋友關係
		Seed size = new Seed(6);
		for (Person person : all) {
			int s = size.next() + 1;
			for (int i = 0; i < s; i++) {
				Person friend = null;
				do {
					friend = all.get(seed.next());
				} while (person.getId() == friend.getId());
				log.debug("add friend : " + friend.getName());
				person.addFriend(friend);
				friend.addFriend(person);
			}
			personDao.save(person);
		}
	}

	/**
	 * 執行亂數朋友關係的 main function
	 * @param args
	 */
	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring.xml" });
		FriendsMaker maker = (FriendsMaker) context.getBean("friendsMaker");
		maker.make();

	}

}
