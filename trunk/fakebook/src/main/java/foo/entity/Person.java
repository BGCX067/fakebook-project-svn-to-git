package foo.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OrderBy;

/**
 * person entity object
 * @author phil
 */
@Entity
public class Person implements Serializable {

	private static final long serialVersionUID = 1L;
	public Integer id;
	public String name;
	public Gender gender;
	private byte[] photo;
	private Set<Person> friends = new HashSet<Person>();
	private Set<Group> groups = new HashSet<Group>();
	private Set<Message> sendFromMe = new HashSet<Message>();
	private Set<Message> sendToMe = new HashSet<Message>();

	public Person(String name, Gender gender) {
		this.name = name;
		this.gender = gender;
	}

	public Person() {
	}

	public Person(String name, Gender gender, byte[] photo) {
		this(name, gender);
		this.photo = photo;
	}

	public Person(int id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Enumerated
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "MEDIUMBLOB")
	public byte[] getPhoto() {
		return photo;
	}

	@ManyToMany
	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.TRUE)
	public Set<Person> getFriends() {
		return friends;
	}

	@SuppressWarnings("unused")
	private void setFriends(Set<Person> friends) {
		this.friends = friends;
	}

	public void addFriend(Person friend) {
		this.friends.add(friend);
	}

	@ManyToMany(mappedBy = "members")
	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.TRUE)
	public Set<Group> getGroups() {
		return groups;
	}

	@SuppressWarnings("unused")
	private void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public void addGroup(Group group) {
		this.groups.add(group);
	}

	@OneToMany(mappedBy = "from")
	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause = "stamp desc")
	public Set<Message> getSendFromMe() {
		return sendFromMe;
	}

	@Transient
	public Set<Message> getSendFromMe(Person friend) {
		Set<Message> sublist = new HashSet<Message>();
		for (Message msg : this.sendFromMe) {
			if (msg.getTo().equals(friend)) {
				sublist.add(msg);
			}
		}
		return sublist;
	}

	@SuppressWarnings("unused")
	private void setSendFromMe(Set<Message> sendFromMe) {
		this.sendFromMe = sendFromMe;
	}

	@OneToMany(mappedBy = "to")
	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy(clause = "stamp desc")
	public Set<Message> getSendToMe() {
		return sendToMe;
	}

	public void replaceAllFriends(int[] friendIds) {
		this.friends.clear();
		for (int id : friendIds) {
			if (this.id != id) {// exclude self
				// some trick create a fake person replace load from database
				this.addFriend(new Person(id));
			}
		}
	}

	@SuppressWarnings("unused")
	private void setSendToMe(Set<Message> sendToMe) {
		this.sendToMe = sendToMe;
	}

	public void post(Message msg, Person to) {
		msg.setFrom(this);
		msg.setTo(to);
	}

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || !(other instanceof Person))
			return false;
		Person castOther = (Person) other;
		return new EqualsBuilder().append(name, castOther.getId()).append(
				gender, castOther.getGender()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(name).append("gender").toHashCode();
	}

	public static enum Gender {
		Female, Male;

		public static Gender value(String gender) {
			return (gender.equals("0") ? Female : Male);
		}

		@Override
		public String toString() {
			return (ordinal() == 0) ? "Female" : "Male";
		}

	}

}