package foo.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * message entity object
 * @author phil
 */
@Entity
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	public Integer id;
	private String content;
	private Person from;
	private Person to;
	private Date stamp = new Date();

	public Message(String msg) {
		this.content = msg;
	}

	public Message() {
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

	@ManyToOne
	public Person getFrom() {
		return from;
	}

	public void setFrom(Person from) {
		this.from = from;
	}

	@ManyToOne
	public Person getTo() {
		return to;
	}

	public void setTo(Person to) {
		this.to = to;
	}

	public Date getStamp() {
		return stamp;
	}

	public void setStamp(Date stamp) {
		this.stamp = stamp;
	}

}
