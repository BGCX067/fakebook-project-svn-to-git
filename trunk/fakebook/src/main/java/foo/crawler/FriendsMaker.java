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
 * �üƻs�y�B�����Y
 * @author phil
 */
@Component
@Transactional
public class FriendsMaker {

	private static final Logger log = Logger.getLogger(FriendsMaker.class);

	@Autowired
	private PersonDao personDao;

	/**
	 * �üƪ��B�����Y�s�y��
	 */
	public void make() {

		List<Person> all = personDao.findAll();
		Seed seed = new Seed(all.size());
		//�C�H���� 1~6 �ӪB�����Y
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
	 * ����üƪB�����Y�� main function
	 * @param args
	 */
	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring.xml" });
		FriendsMaker maker = (FriendsMaker) context.getBean("friendsMaker");
		maker.make();

	}

}
