package foo.crawler;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import foo.dao.GroupDao;
import foo.dao.PersonDao;
import foo.entity.Group;
import foo.entity.Person;

/**
 * �üƲ��͸s�շ|�����Y���s�y��
 * @author phil
 */
@Component
@Transactional
public class MemberMaker {

	private static final Logger log = Logger.getLogger(MemberMaker.class);

	@Autowired
	private PersonDao personDao;
	@Autowired
	private GroupDao groupDao;

	/**
	 * ���Ͷüƪ��s�շ|�����Y
	 */
	public void make() {

		Collection<Group> allGroup = groupDao.findAll();
		List<Person> allPeople = personDao.findAll();
		Seed seed = new Seed(allPeople.size());
		Seed size = new Seed(8);
		for (Group group : allGroup) {
			int s = size.next() + 2;
			for (int i = 0; i < s; i++) {
				Person m = allPeople.get(seed.next());
				log.debug("add member : " + m.getName());
				group.addMember(m);
			}
			groupDao.save(group);
		}
	}

	/**
	 * ����üƸs�����Y�� main function
	 * @param args
	 */
	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring.xml" });
		MemberMaker maker = (MemberMaker) context.getBean("memberMaker");
		maker.make();

	}

}
