package foo.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import foo.crawler.ScriptGenerator;
import foo.dao.PersonDao;
import foo.entity.Group;
import foo.entity.Person;
import foo.entity.Person.Gender;

/**
 * Spring Controller
 * �t�d�������� person client request �B�z
 * @author phil
 */
@Controller
public class PersonController {

	private static final int MALE = 1;
	private static final int FEMALE = 2;
	@Autowired
	private PersonDao personDao;
	@Autowired
	private ScriptGenerator generator;

	/**
	 * ���� form.do �� request�A��� person form ����J�e��
	 * @param person
	 * @param model
	 */
	@RequestMapping
	public void form(Person person, ModelMap model) {
		model.addAttribute("gender", Gender.values());
	}

	/**
	 * @param ��ܩҦ��B��
	 */
	@RequestMapping
	public void friends(ModelMap model) {
		model.addAttribute("all", personDao.findAll());
	}

	/**
	 * �x�s person object
	 * @param person
	 * @param pic �j�Y��
	 * @return
	 * @throws IOException
	 */
	@RequestMapping
	public String save(Person person, @RequestParam("pic")
	MultipartFile pic) throws IOException {
		person.setPhoto(pic.getBytes());
		personDao.save(person);

		return "redirect:form.do";
	}

	/**
	 * ��ܫ��w id �� person object
	 * @param id
	 * @param model
	 */
	@RequestMapping
	@Transactional
	public void list(Integer id, ModelMap model) {
		model.addAttribute("person", personDao.findById(id));
	}

	/**
	 * ��ܩҦ��B��
	 * @param model
	 */
	@RequestMapping
	public void listAll(ModelMap model) {
		model.addAttribute("people", personDao.findAll());
	}

	/**
	 * ���o���w id �� person �ñN�Ҧ��B�ͦC�X�A�H json �覡��X
	 * @param id
	 * @param writer
	 * @throws IOException
	 */
	@RequestMapping
	public void getFriends(int id, Writer writer) throws IOException {
		int[] friends = personDao.findFriendsIds(id);
		writer.write(JSONArray.fromObject(friends).toString());
	}

	/**
	 * �x�s�Ҧ��B��
	 * @param selfId
	 * @param friendIds
	 * @param writer
	 * @throws IOException
	 */
	@RequestMapping
	public void saveFriends(int selfId, int[] friendIds, Writer writer)
			throws IOException {
		try {
			Person self = personDao.findById(selfId);
			self.replaceAllFriends(friendIds);
			personDao.save(self);
			writer.write("saved");
		} catch (Exception e) {
			writer.write("Error:\n" + e.getMessage());
		}
	}

	/**
	 * ��ܤj�Y��
	 * @param id
	 * @param outputStream
	 * @throws IOException
	 */
	@RequestMapping
	public void photo(int id, OutputStream outputStream) throws IOException {
		outputStream.write(personDao.findById(id).getPhoto());
	}

	/**
	 * �H json �榡��X���w id �� person 
	 * @param writer
	 * @param pid
	 * @throws IOException
	 */
	@RequestMapping
	public void person(Writer writer, Integer pid) throws IOException {
		writer.write(generator.generateByPersonId(pid));
	}

	/**
	 * �H json �榡��X���w id �� group
	 * @param writer
	 * @param gid
	 * @throws IOException
	 */
	@RequestMapping
	public void group(Writer writer, Long gid) throws IOException {
		writer.write(generator.generateByGroupId(gid));
	}

	/**
	 * ��J�H�W�ɨ��Ʈw�d�ߨñN���G�H json �榡��X
	 * @param writer
	 * @param filter
	 * @throws IOException
	 */
	@RequestMapping
	public void filter(Writer writer, String filter) throws IOException {
		if (StringUtils.isNotEmpty(filter)) {
			String[] text = filter.split(" ");
			writer.write(generator.generateByFilter(text));
		} else {
			writer.write(generator.generateByFilter(new String[] { filter }));
		}
	}

	/**
	 * ��X�B�ͤj�� count �ƪ��H
	 * @param writer
	 * @param count
	 * @throws IOException
	 */
	@RequestMapping
	public void friendsCountGt(Writer writer, int count) throws IOException {
		writer.write(generator.generate(personDao.findByFriendsCount(count)));
	}
	
	/**
	 * ��X day �ѥH�e���H
	 * @param writer
	 * @param day
	 * @throws IOException
	 */
	@RequestMapping
	public void daysAgo(Writer writer, int day) throws IOException {
		writer.write(generator.generate(personDao.findByDayAgo(day)));
	}

	/**
	 * �H�ʧO�� filter ����X�A�k�ʡB�k��
	 * @param writer
	 * @param gender
	 * @throws IOException
	 */
	@RequestMapping
	public void gender(Writer writer, int gender) throws IOException {

		Collection<Person> all = new HashSet<Person>(0);

		switch (gender) {
		case MALE:
			all = personDao.findByGender(Gender.Male);
			break;
		case FEMALE:
			all = personDao.findByGender(Gender.Female);
			break;
		default:
			all = personDao.findAll();
			break;
		}

		writer.write(generator.generate(all));
	}

	/**
	 * ��ܵ�ı�ƪ��e��
	 */
	@RequestMapping
	public void visualize() {
	}

	/**
	 * �C�X person �ҰѻP�� group
	 * @param personId
	 * @param model
	 */
	@RequestMapping
	public void listGroup(int personId, ModelMap model) {
		model.addAttribute("person", personDao.findById(personId));
	}

	/**
	 * �H json �榡��X person �ҰѻP���Ҧ� group
	 * @param pid
	 * @param writer
	 * @throws IOException
	 */
	@RequestMapping
	public void listGroupJson(int pid, Writer writer) throws IOException {
		JSONArray ja = new JSONArray();
		Set<Group> groups = personDao.findById(pid).getGroups();
		for (Group group : groups) {
			JSONObject jp = new JSONObject();
			jp.put("id", group.getId());
			jp.put("name", group.getName());
			ja.add(jp);
		}

		writer.write(ja.toString());
	}

}
