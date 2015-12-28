package foo.dao;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Repository;

import foo.entity.Group;
import foo.entity.Person;

/**
 * 負責 group 的資料庫操作
 * @author phil
 */
@Repository
public class GroupDao extends GenericDao<Group, Long> {

	/**
	 * find member ids by id
	 * @param id
	 * @return
	 */
	public int[] findMemberIdsById(Long id) {
		Group group = findById(id);
		Set<Integer> ids = new HashSet<Integer>();
		for (Person p : group.getMembers()) {
			ids.add(p.getId());
		}

		return ArrayUtils.toPrimitive((Integer[]) ids.toArray(new Integer[0]));
	}
}
