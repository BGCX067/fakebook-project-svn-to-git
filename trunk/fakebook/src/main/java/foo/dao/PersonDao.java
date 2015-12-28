package foo.dao;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import foo.entity.Message;
import foo.entity.Person;
import foo.entity.Person.Gender;

/**
 * 負責 person object 資料庫的操作
 * @author phil
 */
@Repository
public class PersonDao extends GenericDao<Person, Integer> {

	/**
	 * find person object by person name
	 * @param name
	 * @return
	 */
	public Collection<Person> findByName(String name) {
		return findByCriteria(Restrictions.eq("name", name));
	}

	/**
	 * find friend ids by friend id
	 * @param id
	 * @return
	 */
	public int[] findFriendsIds(Integer id) {
		Person person = findById(id);
		Set<Integer> ids = new HashSet<Integer>();
		for (Person f : person.getFriends()) {
			ids.add(f.getId());
		}

		return ArrayUtils.toPrimitive((Integer[]) ids.toArray(new Integer[0]));
	}

	/**
	 * find people by name like filter
	 * @param filter
	 * @return
	 */
	public Collection<Person> findLikeName(String filter) {
		return findByCriteria(Restrictions.like("name", filter,
				MatchMode.ANYWHERE));
	}

	/**
	 * find people by gender
	 * @param gender
	 * @return
	 */
	public Collection<Person> findByGender(Gender gender) {
		return findByCriteria(Restrictions.eq("gender", gender));
	}

	/**
	 * find friends by friends count
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<Person> findByFriendsCount(int count) {

		Query query = getCurrentSession().createQuery(
				"from " + Person.class.getName()
						+ " as p where p.friends.size >= :count");
		query.setInteger("count", count);

		return query.list();
	}

	/**
	 * find people by days ago
	 * @param day
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<Person> findByDayAgo(int day) {
		DateTime base = new DateTime().plusDays(day);
		Query query = getCurrentSession().createQuery(
				"from " + Person.class.getName()
						+ " as p where p.id in (select m.to.id from "
						+ Message.class.getName()
						+ " as m where m.stamp >= :stamp)").setDate("stamp",
				DateUtils.truncate(base.toDate(), Calendar.DATE));
		return query.list();
	}
}
