package foo.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import foo.dao.GroupDao;
import foo.dao.PersonDao;
import foo.entity.Group;

/**
 * Spring Controller
 * �t�d�������� group client request �B�z
 * @author phil
 */
@Controller
public class GroupController {

	@Autowired
	private GroupDao groupDao;
	@Autowired
	private PersonDao personDao;

	/**
	 * ��ܿ�J form 
	 * @param group Group ����
	 * @param model spring modeMap
	 */
	@RequestMapping
	public void form(Group group, ModelMap model) {
	}

	/**
	 * �x�s group ��T�ܸ�Ʈw
	 * @param group
	 * @param pic
	 * @return �� Form
	 * @throws IOException
	 */
	@RequestMapping
	public String save(Group group, @RequestParam("pic")
	MultipartFile pic) throws IOException {
		group.setPhoto(pic.getBytes());
		groupDao.save(group);

		return "redirect:form.do";
	}

	/**
	 * �C�X�Ҧ� Group
	 * @param model
	 */
	@RequestMapping
	public void listAll(ModelMap model) {
		model.addAttribute("groups", groupDao.findAll());
	}

	/**
	 * �����w id �� group
	 * @param id
	 * @param model
	 */
	@RequestMapping
	public void list(long id, ModelMap model) {
		model.addAttribute("group", groupDao.findById(id));
	}

	/**
	 * �C�X�Ҧ� group �H�ΩҦ� group �� member
	 * @param model
	 */
	@RequestMapping
	public void groups(ModelMap model) {
		model.addAttribute("allPeople", personDao.findAll());
		model.addAttribute("allGroups", groupDao.findAll());
	}

	/**
	 * ���o���w id group �����Ҧ� member
	 * @param id
	 * @param writer
	 * @throws IOException
	 */
	@RequestMapping
	public void getMembers(long id, Writer writer) throws IOException {
		int[] memberIds = groupDao.findMemberIdsById(id);
		writer.write(JSONArray.fromObject(memberIds).toString());
	}

	/**
	 * �x�s�Ҧ� group
	 * @param groupId
	 * @param memberIds
	 * @param writer
	 * @throws IOException
	 */
	@RequestMapping
	public void saveMembers(long groupId, int[] memberIds, Writer writer)
			throws IOException {
		try {
			Group group = groupDao.findById(groupId);
			group.replaceAllMembers(memberIds);
			groupDao.save(group);
			writer.write("saved");
		} catch (Exception e) {
			writer.write("Error:\n" + e.getMessage());
		}
	}

	/**
	 * ��ܹϤ�
	 * @param id
	 * @param outputStream
	 * @throws IOException
	 */
	@RequestMapping
	public void photo(long id, OutputStream outputStream) throws IOException {
		outputStream.write(groupDao.findById(id).getPhoto());
	}

}
