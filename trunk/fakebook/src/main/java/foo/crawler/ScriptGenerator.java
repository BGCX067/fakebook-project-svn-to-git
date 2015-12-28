package foo.crawler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import foo.dao.GroupDao;
import foo.dao.PersonDao;
import foo.entity.Group;
import foo.entity.Message;
import foo.entity.Person;

/**
 * 用來產生 Hyperbolic browser 所需要的 script 資訊，會以 json 的格式輸出
 * @author phil
 */
@Component
@Transactional
public class ScriptGenerator {

	@Autowired
	private PersonDao personDao;
	@Autowired
	private GroupDao groupDao;

	/**
	 * 產生指定 id person 的資料集
	 * @param pid
	 * @return
	 */
	public String generateByPersonId(Integer pid) {
		Collection<Person> all = new HashSet<Person>();
		if (pid == null) {
			all = personDao.findAll();
		} else {
			Person p = personDao.findById(pid);
			all.add(p);
			all.addAll(p.getFriends());
		}

		return generate(all);
	}

	/**
	 * 產生指定群組 id 的資料集
	 * @param gid
	 * @return
	 */
	public String generateByGroupId(Long gid) {
		Collection<Person> all = new HashSet<Person>();
		if (gid == null) {
			all = personDao.findAll();
		} else {
			all = groupDao.findById(gid).getMembers();
		}

		return generate(all);
	}

	/**
	 * 產生以 filter 為條件過濾之後的資料集
	 * @param filter
	 * @return
	 */
	public String generateByFilter(String[] filter) {
		Collection<Person> all = new HashSet<Person>();
		Collection<Person> filters = new HashSet<Person>();
		if (filter == null) {
			all = personDao.findAll();
		} else {
			for (int i = 0; i < filter.length; i++) {
				filters.addAll(personDao.findLikeName(filter[i]));
			}
			for (Person person : filters) {
				all.add(person);
				all.addAll(person.getFriends());
			}
		}

		return generate(all);
	}

	/**
	 * 產生多個人的資料集
	 * @param people
	 * @return
	 */
	public String generate(Collection<Person> people) {

		JSONArray ja = new JSONArray();
		Collection<Person> connected = new HashSet<Person>(people.size());
		for (Person person : people) {
			JSONObject jp = new JSONObject();
			jp.put("id", "node" + person.getId());
			jp.put("name", nodeFormat(person));
			JSONArray jaData = new JSONArray();
			JSONObject joData = new JSONObject();
			joData.put("key", "weight");
			int nw = person.getSendFromMe().size();
			joData.put("value", nw);
			joData.put("sendFromMe", nw);
			joData.put("gender", person.getGender());
			joData.put("friends", person.getFriends().size());
			Set<Message> msgs = person.getSendFromMe();
			DateTime today = new DateTime(2009, 6, 26, 0, 0, 0, 0);
			DateTime day3 = today.minusDays(3);
			DateTime day5 = today.minusDays(5);
			DateTime week1 = today.minusWeeks(1);
			DateTime month1 = today.minusMonths(6);
			for (Message msg : msgs) {
				long stamp = msg.getStamp().getTime();
				if (today.isBefore(stamp)) {
					joData.put("today", "true");
					break;
				}
			}
			if (joData.get("today") == null) {
				joData.put("today", "false");
			}
			for (Message msg : msgs) {
				long stamp = msg.getStamp().getTime();
				if (day3.isBefore(stamp)) {
					joData.put("day3", "true");
					break;
				}
			}
			if (joData.get("day3") == null) {
				joData.put("day3", "false");
			}
			for (Message msg : msgs) {
				long stamp = msg.getStamp().getTime();
				if (day5.isBefore(stamp)) {
					joData.put("day5", "true");
					break;
				}
			}
			if (joData.get("day5") == null) {
				joData.put("day5", "false");
			}
			for (Message msg : msgs) {
				long stamp = msg.getStamp().getTime();
				if (week1.isBefore(stamp)) {
					joData.put("week1", "true");
					break;
				}
			}
			if (joData.get("week1") == null) {
				joData.put("week1", "false");
			}
			for (Message msg : msgs) {
				long stamp = msg.getStamp().getTime();
				if (month1.isBefore(stamp)) {
					joData.put("month1", "true");
					break;
				}
			}
			if (joData.get("month1") == null) {
				joData.put("month1", "false");
			}
			Set<Group> groups = person.getGroups();
			StringBuilder sb = new StringBuilder();
			for (Group group : groups) {
				sb.append("<a href='javascript:void(0)' groupId='");
				sb.append(group.getId());
				sb.append("'>");
				sb.append(group.getName());
				sb.append("</a><br/>");
			}
			joData.put("groupLinks", sb.toString());
			jaData.add(joData);
			jp.put("data", jaData);
			JSONArray jaAdj = new JSONArray();
			for (Person friend : person.getFriends()) {
				if (people.contains(friend) && !connected.contains(friend)) {
					JSONObject joAdj = new JSONObject();
					joAdj.put("nodeTo", "node" + friend.getId());
					JSONObject joAdjData = new JSONObject();
					int count = person.getSendFromMe(friend).size()
							+ friend.getSendFromMe(person).size();
					count = count / 2;
					count = (count < 1) ? 1 : count;
					joAdjData.put("weight", (count > 5) ? 5 : count);
					joAdjData.put("color", (person.getGender() == friend
							.getGender()) ? "#4094c4" : "#f6af3a");
					joAdj.put("data", joAdjData);
					jaAdj.add(joAdj);
					connected.add(person);
				}

			}
			jp.put("adjacencies", jaAdj);
			ja.add(jp);
		}

		return ja.toString();

	}

	/**
	 * 產生大頭照的區塊
	 * @param person
	 * @return
	 */
	private String nodeFormat(Person person) {
		return "<img src='/fakebook/person/photo.do?id=" + person.getId()
				+ "' weight='40' height='40' /><br><span>" + person.getName()
				+ "</span>";
	}

	/**
	 * 測試輸出的 main function
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring.xml" });
		ScriptGenerator generator = (ScriptGenerator) context
				.getBean("scriptGenerator");
		System.out.println(generator.generateByPersonId(null));
	}

}
