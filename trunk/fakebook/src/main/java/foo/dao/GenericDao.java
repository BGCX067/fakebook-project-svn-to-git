package foo.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 負責儲存資料的 Generic object
 * @param <T>
 * @param <ID>
 * @author phil
 */
@SuppressWarnings("unchecked")
@Transactional
public abstract class GenericDao<T extends Serializable, ID extends Serializable> {

	protected final static Logger log = Logger.getLogger(GenericDao.class);

	@Autowired
	private SessionFactory sessionFactory;

	public Class<T> getPersistentClass() {
		return (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * find object by id
	 * @param id
	 * @return
	 */
	public T findById(ID id) {
		return (T) getCurrentSession().get(getPersistentClass(), id);
	}

	/**
	 * find all object
	 * @return
	 */
	public List<T> findAll() {
		return findByCriteria();
	}

	/**
	 * save object to database
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public T save(T entity) {
		getCurrentSession().saveOrUpdate(entity);
		return entity;
	}

	/**
	 * delete object form database
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void delete(T entity) {
		getCurrentSession().delete(entity);
	}

	/**
	 * find object by criteria
	 * @param criterion
	 * @return
	 */
	protected List<T> findByCriteria(Criterion... criterion) {
		Criteria crit = getCurrentSession()
				.createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return crit.list();
	}

	/**
	 * get hibernate current session object
	 * @return
	 */
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}