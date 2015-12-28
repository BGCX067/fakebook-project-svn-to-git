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
 * 負責接受有關 group client request 處理
 * @author phil
 */
@Controller
public class GroupController {

	@Autowired
	private GroupDao groupDao;
	@Autowired
	private PersonDao personDao;

	/**
	 * 顯示輸入 form 
	 * @param group Group 物件
	 * @param model spring modeMap
	 */
	@RequestMapping
	public void form(Group group, ModelMap model) {
	}

	/**
	 * 儲存 group 資訊至資料庫
	 * @param group
	 * @param pic
	 * @return 到 Form
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
	 * 列出所有 Group
	 * @param model
	 */
	@RequestMapping
	public void listAll(ModelMap model) {
		model.addAttribute("groups", groupDao.findAll());
	}

	/**
	 * 找到指定 id 的 group
	 * @param id
	 * @param model
	 */
	@RequestMapping
	public void list(long id, ModelMap model) {
		model.addAttribute("group", groupDao.findById(id));
	}

	/**
	 * 列出所有 group 以及所有 group 的 member
	 * @param model
	 */
	@RequestMapping
	public void groups(ModelMap model) {
		model.addAttribute("allPeople", personDao.findAll());
		model.addAttribute("allGroups", groupDao.findAll());
	}

	/**
	 * 取得指定 id group 中的所有 member
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
	 * 儲存所有 group
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
	 * 顯示圖片
	 * @param id
	 * @param outputStream
	 * @throws IOException
	 */
	@RequestMapping
	public void photo(long id, OutputStream outputStream) throws IOException {
		outputStream.write(groupDao.findById(id).getPhoto());
	}

}
