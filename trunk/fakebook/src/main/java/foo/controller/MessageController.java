package foo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import foo.dao.MessageDao;
import foo.dao.PersonDao;
import foo.entity.Message;

/**
 * Spring Controller
 * 負責接受有關 message client request 處理
 * @author phil
 */
@Controller
public class MessageController {

	@Autowired
	private MessageDao messageDao;
	@Autowired
	private PersonDao personDao;

	/**
	 * 顯示 message form 畫面
	 * @param model
	 */
	@RequestMapping
	public void form(ModelMap model) {
		model.addAttribute("people", personDao.findAll());
	}

	/**
	 * 儲存一則 message
	 * @param content
	 * @param from
	 * @param to
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping
	public String save(String content, int from, int to, ModelMap model)
			throws IOException {
		Message msg = new Message(content);
		msg.setFrom(personDao.findById(from));
		msg.setTo(personDao.findById(to));
		messageDao.save(msg);

		return "redirect:form.do";
	}

	/**
	 * 列出所有 message
	 * @param id
	 * @param model
	 */
	@RequestMapping
	public void list(Integer id, ModelMap model) {
		model.addAttribute("person", personDao.findById(id));
	}

}
