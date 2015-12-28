package foo.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * message base entity object
 * @author phil
 */
@Entity
public class MessageBase implements Serializable {

	private static final long serialVersionUID = 1L;
	public Integer id;
	private String content;

	public MessageBase(String msg) {
		this.content = msg;
	}

	public MessageBase() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
