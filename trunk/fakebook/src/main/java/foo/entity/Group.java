package foo.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Group entity object
 * @author phil
 */
@Entity
@Table(name = "Groups")
public class Group implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long Id;
	private String name;
	private String description;
	private byte[] photo;
	private Set<Person> members = new HashSet<Person>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany
	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.TRUE)
	public Set<Person> getMembers() {
		return members;
	}

	@SuppressWarnings("unused")
	private void setMembers(Set<Person> members) { // hibernate use only
		this.members = members;
	}

	public void addMember(Person person) {
		person.addGroup(this);
		this.members.add(person);
	}

	public void replaceAllMembers(int[] memberIds) {
		this.members.clear();
		for (int id : memberIds) {
			if (this.Id != id) {// exclude self
				// some trick create a fake person replace load from database
				this.addMember(new Person(id));
			}
		}
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "MEDIUMBLOB")
	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
