package common.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import common.page.PageControl;
import common.page.SqlCondGroup;
import common.util.DataUtil;
import common.util.SpringBeanUtil;

@SuppressWarnings("unchecked")
public abstract class GenericDaoMysqlImpl<T extends Serializable, PK extends Serializable> extends HibernateDaoSupport
		implements GenericDao<T, PK> {
	// 实体类类型(由构造方法自动赋值)
	private Class<T> entityClass;

	@Resource(name = "mySqlTemplate")
	public void setHibernateTemplate1(HibernateTemplate mysql) {
		// TODO Auto-generated method stub
		super.setHibernateTemplate(mysql);
	}

	/**
	 * 修改数据
	 * 
	 * @param oldBean
	 * @param newBean
	 * @param sysUser
	 * @param schoolId
	 * @param reason
	 *            void
	 * @exception @since
	 *                1.0.0
	 */

	public void updateDomain(String schoolId, Integer userId, String userName, String reason, T oldBean, T newBean) {

		String history = DataUtil.getUpdateHistory(schoolId, userId, userName, reason, oldBean, newBean);
		log.warn(history);
		update(oldBean);
	}

	/**
	 * 
	 * 创建数据<br/>
	 * 
	 * @param domain
	 * @param sysUser
	 * @param schoolId
	 * @param reason
	 * @return Serializable
	 * @exception @since
	 *                1.0.0
	 */
	public Serializable saveDomain(String schoolId, Integer userId, String userName, String reason, T domain) {

		log.warn(DataUtil.getAddHistory(schoolId, userId, userName, reason, domain));
		return save(domain);
	}

	/**
	 * 获得日志类以便控制日志输出
	 */
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GenericDaoMysqlImpl.class);

	// 构造方法，根据实例类自动获取实体类类型
	public GenericDaoMysqlImpl() {
		this.entityClass = null;
		Class c = getClass();
		Type t = c.getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) t).getActualTypeArguments();
			this.entityClass = (Class<T>) p[0];
		}
	}

	// -------------------- 基本检索、增加、修改、删除操作 --------------------
	// 根据主键获取实体。如果没有相应的实体，返回 null。
	public T get(PK id) {

		return (T) getHibernateTemplate().get(entityClass, id);
	}

	// 根据主键获取实体并加锁。如果没有相应的实体，返回 null。
	public T getWithLock(PK id, LockMode lock) {
		T t = (T) getHibernateTemplate().get(entityClass, id, lock);
		if (t != null) {
			this.flush(); // 立即刷新，否则锁不会生效。
		}
		return t;
	}

	// 根据主键获取实体。如果没有相应的实体，抛出异常。
	public T load(PK id) {
		return (T) getHibernateTemplate().load(entityClass, id);
	}

	// 根据主键获取实体并加锁。如果没有相应的实体，抛出异常。
	public T loadWithLock(PK id, LockMode lock) {
		T t = (T) getHibernateTemplate().load(entityClass, id, lock);
		if (t != null) {
			this.flush(); // 立即刷新，否则锁不会生效。
		}
		return t;
	}

	// 获取全部实体。
	public List<T> loadAll() {
		return (List<T>) getHibernateTemplate().loadAll(entityClass);
	}

	// 根据实例查询
	public List<T> queryByExample(T exampleInstance) {
		return getHibernateTemplate().findByExample(exampleInstance);
	}

	// 根据实例查询第一个实体
	public T getFirstByExample(T entity) {
		List<T> list = this.queryByExample(entity);
		if (null != list && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	// 存储实体到数据库
	public PK save(T entity) {
		return (PK) getHibernateTemplate().save(entity);
	}

	public void saveAll(List<T> object) {
		for (T entity : object) {
			getHibernateTemplate().save(entity);
		}
	}

	// 增加或更新实体
	public void saveOrUpdate(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
	}

	// 增加或更新集合中的全部实体
	public void saveOrUpdateAll(Collection<T> entities) {
		getHibernateTemplate().saveOrUpdateAll(entities);
	}

	// 更新实体
	public void update(T entity) {

		getHibernateTemplate().update(entity);
	}

	// 更新实体并加锁
	public void updateWithLock(T entity, LockMode lock) {
		getHibernateTemplate().update(entity, lock);
		this.flush(); // 立即刷新，否则锁不会生效。
	}

	// 删除指定的实体
	public void delete(T entity) {
		getHibernateTemplate().delete(entity);
	}

	// 加锁并删除指定的实体
	public void deleteWithLock(T entity, LockMode lock) {
		getHibernateTemplate().delete(entity, lock);
		this.flush(); // 立即刷新，否则锁不会生效。
	}

	// 根据主键删除指定实体
	public void deleteByKey(PK id) {
		this.delete(this.load(id));
	}

	// 根据主键加锁并删除指定的实体
	public void deleteByKeyWithLock(PK id, LockMode lock) {
		this.deleteWithLock(this.load(id), lock);
	}

	// 删除集合中的全部实体
	public void deleteAll(Collection<T> entities) {
		getHibernateTemplate().deleteAll(entities);
	}

	// -------------------- HSQL ----------------------------------------------
	// 使用HSQL语句检索数据
	public List find(String queryString) {
		return getHibernateTemplate().find(queryString);
	}

	// 使用带参数的HSQL语句检索数据
	public List findByParam(String queryString, Object value) {
		return getHibernateTemplate().find(queryString, value);
	}

	// 使用带参数的HSQL语句检索数据
	public List findByParam(String queryString, Object[] values) {
		return getHibernateTemplate().find(queryString, values);
	}

	// 使用带命名的参数的HSQL语句检索数据
	public List findByNamedParam(String queryString, String[] paramNames, Object[] values) {
		return getHibernateTemplate().findByNamedParam(queryString, paramNames, values);
	}

	// 使用带命名的参数的HSQL语句检索数据
	public List findByNamedParam(String queryString, Map<String, Object> paramMap) {
		List<String> paramNamesList = new ArrayList<String>();
		List<Object> valuesList = new ArrayList<Object>();
		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			paramNamesList.add(entry.getKey());
			valuesList.add(entry.getValue());
		}
		return getHibernateTemplate().findByNamedParam(queryString, paramNamesList.toArray(new String[1]),
				valuesList.toArray());
	}

	// 使用带命名的参数的HSQL语句检索数据
	public List findByValueBean(String queryString, Object valueBean) {
		return getHibernateTemplate().findByValueBean(queryString, valueBean);
	}

	// 使用命名的HSQL语句检索数据
	public List findByNamedQuery(String queryName) {
		return getHibernateTemplate().findByNamedQuery(queryName);
	}

	// 使用带参数的命名HSQL语句检索数据
	public List findByNamedQuery(String queryName, Object[] values) {
		return getHibernateTemplate().findByNamedQuery(queryName, values);
	}

	// 使用带命名参数的命名HSQL语句检索数据
	public List findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values) {
		return getHibernateTemplate().findByNamedQueryAndNamedParam(queryName, paramNames, values);
	}

	// 使用带命名参数的命名HSQL语句检索数据
	public List findByNamedQueryAndNamedParam(String queryName, Map<String, Object> paramMap) {
		List<String> paramNamesList = new ArrayList<String>();
		List<Object> valuesList = new ArrayList<Object>();
		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			paramNamesList.add(entry.getKey());
			valuesList.add(entry.getValue());
		}
		return getHibernateTemplate().findByNamedQueryAndNamedParam(queryName, paramNamesList.toArray(new String[1]),
				valuesList.toArray());
	}

	// 使用带命名的参数的HSQL语句检索数据
	public List findByNamedQueryAndValueBean(String queryName, Object valueBean) {
		return getHibernateTemplate().findByNamedQueryAndValueBean(queryName, valueBean);
	}

	// 使用HSQL语句检索数据，返回 Iterator，使用缓存，推荐只读对象使用
	public Iterator iterate(String queryString) {
		return getHibernateTemplate().iterate(queryString);
	}

	// 使用带参数HSQL语句检索数据，返回 Iterator，使用缓存，推荐只读对象使用
	public Iterator iterate(String queryString, Object[] values) {
		return getHibernateTemplate().iterate(queryString, values);
	}

	// 关闭检索返回的 Iterator，使用缓存，推荐只读对象使用
	public void closeIterator(Iterator it) {
		getHibernateTemplate().closeIterator(it);
	}

	// 使用HSQL语句直接增加、更新、删除实体
	public int bulkUpdate(String queryString) {
		return getHibernateTemplate().bulkUpdate(queryString);
	}

	// 使用带参数的HSQL语句增加、更新、删除实体
	public int bulkUpdate(String queryString, Object[] values) {
		return getHibernateTemplate().bulkUpdate(queryString, values);
	}

	// 使用带参数的HSQL语句增加、更新、删除实体
	public int executeUpdate(final String queryString, Object[] values) {
		final Object[] finaelValues = values;
		return getHibernateTemplate().execute(new HibernateCallback() {
			// @Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				for (int i = 0; i < finaelValues.length; i++) {
					if (finaelValues[i] == null) {
						continue;
					}
					query.setParameter(i, finaelValues[i]);
				}
				return query.executeUpdate();
			}
		});
	}

	// 使用带参数的HSQL语句增加、更新、删除实体
	public int executeUpdate(final String queryString, Map<String, Object> paramMap) {
		final Map<String, Object> final_paramMap = paramMap;
		return getHibernateTemplate().execute(new HibernateCallback() {
			// @Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				query.setProperties(final_paramMap);
				return query.executeUpdate();
			}
		});
	}

	// -------------------------------- Others --------------------------------

	// 加锁指定的实体
	public void lock(T entity, LockMode lockMode) {
		getHibernateTemplate().lock(entity, lockMode);
	}

	// 强制初始化指定的实体
	public void initialize(Object proxy) {
		getHibernateTemplate().initialize(proxy);
	}

	// 强制立即更新缓冲数据到数据库（否则仅在事务提交时才更新）
	public void flush() {
		getHibernateTemplate().flush();
	}

	// 查询实例的数量
	public long getCount() {
		List<Long> list = getHibernateTemplate().find("SELECT COUNT(1) FROM " + entityClass.getName());
		long count = 0;
		if (list != null && list.size() != 0)
			count = list.get(0);
		return count;
	}

	// 清除旧对象的session
	public void clearSession() {
	}

	// 接管实例（未实现）
	public void reattach(T persistentObject) {
	}

	// 脱管实例（未实现）
	public void detach(T persistentObject) {
	}

//	@Autowired
//	public void setSessionFactory0(SessionFactory sessionFactory) {
//		super.setSessionFactory(sessionFactory);
//	}

	/// ypx

	/**
	 * 返回按属性条件查询的结果列表
	 * 
	 * @param pageControl
	 *            用来控制排序和分页的参数类
	 * @param propertyName
	 *            属性名
	 * @param value
	 *            属性值
	 * @param sign
	 *            符号,如=/>/>=</<=/is null(相应的value必须为空)等
	 * @param type
	 *            条件连接类型(and还是or)
	 * @return 查询结果(List列表)
	 */

	@SuppressWarnings("unchecked")
	public List<T> findByProperties(PageControl pageControl, String[] propertyName, Object[] value, String[] sign,
			String[] type) {
		StringBuffer hql = createPreWhereSql(pageControl);

		if (propertyName != null && propertyName.length > 0) {
			hql.append(" where (");
			accessSQL(hql, propertyName, value, sign, type);
			hql.append(" )");
		}

		try {
			return (List<T>) getListForPage(hql.toString(), pageControl, value);
		} catch (RuntimeException re) {
			throw re;
		}

	}

	@SuppressWarnings("unchecked")
	public List<T> findBySQL(String hql) {
		try {
			return (List<T>) getHibernateTemplate().find(hql);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * 使用SQL语句(实际上是HQL语句)查询数据，附加查询参数
	 * 
	 * @param hql
	 *            查询语句
	 * @param value
	 *            属性值
	 * @return 查询结果(List列表)
	 */
	@SuppressWarnings("unchecked")
	public List<T> findBySQL(String hql, Object[] value) {
		try {
			return (List<T>) getHibernateTemplate().find(hql, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List findByCondition(PageControl pageControl, List<SqlCondGroup> conList) {
		int listSize = conList.size();
		List<String> propertyName = new ArrayList<String>(listSize);
		List<String> sign = new ArrayList<String>(listSize);
		List<String> type = new ArrayList<String>(listSize);
		List<List> valueList = new ArrayList<List>(listSize);

		accessCondition(conList, propertyName, sign, type, valueList);
		return findByInProperties(pageControl, propertyName.toArray(new String[] {}), valueList.toArray(new List[] {}),
				sign.toArray(new String[] {}), type.toArray(new String[] {}));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diyeasy.common.dao.IDao#findBySQL(com.diyeasy.common.sql.PageControl,
	 * java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public List findByNativeSQL(PageControl pageControl, String sql, Class returnType) {
		try {
			return (List) findByNativeSQL(pageControl, sql, new Object[] {}, returnType);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diyeasy.common.dao.IDao#findBySQL(com.diyeasy.common.sql.PageControl,
	 * java.lang.String, java.lang.Object[], java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public List findByNativeSQL(PageControl pageControl, String sql, Object[] object, Class returnType) {
		try {
			if (returnType == null)
				returnType = entityClass;
			return getNativeListForPage(sql, pageControl, object, returnType);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diyeasy.common.dao.IDao#findByNativeCondition(com.diyeasy.common.sql.
	 * PageControl, java.util.List, java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public List findByNativeCondition(PageControl pageControl, List<SqlCondGroup> conList, String preWhereSql,
			Class returnType) {
		int listSize = conList.size();
		List<String> propertyName = new ArrayList<String>(listSize);
		List<String> sign = new ArrayList<String>(listSize);
		List<String> type = new ArrayList<String>(listSize);
		List<List> valueList = new ArrayList<List>(listSize);

		accessCondition(conList, propertyName, sign, type, valueList);
		return findByNativeInProperties(pageControl, preWhereSql, propertyName.toArray(new String[] {}),
				valueList.toArray(new List[] {}), sign.toArray(new String[] {}), type.toArray(new String[] {}),
				returnType);
	}

	/**
	 * 根据conList转换添加value条件
	 * 
	 * @param conList
	 * @param propertyName
	 * @param sign
	 * @param type
	 * @param valueList
	 *            void
	 * @exception @since
	 *                1.0.0
	 */
	@SuppressWarnings("unchecked")
	private static void accessCondition(List<SqlCondGroup> conList, List<String> propertyName, List<String> sign,
			List<String> type, List<List> valueList) {
		if (conList != null)
			for (Iterator<SqlCondGroup> iterator = conList.iterator(); iterator.hasNext();) {
				SqlCondGroup sqlCon = iterator.next();
				propertyName.add(sqlCon.getName());
				sign.add(sqlCon.getSign());
				if (!(sqlCon.getType() == null || "".equals(sqlCon.getType())))
					type.add(sqlCon.getType());
				Object obj = sqlCon.getValue();
				if (obj == null)
					valueList.add((List) null);
				else if (obj instanceof List) {
					if (((List) obj).isEmpty())
						valueList.add((List) null);
					else
						valueList.add((List) obj);
				} else if (obj.getClass().isArray()) {
					List<Object> list_tmp = new ArrayList<Object>();
					for (Object o : (Object[]) obj) {
						list_tmp.add(o);
					}
					valueList.add(list_tmp);
				} else
					valueList.add(Arrays.asList(obj));
			}
	}

	/**
	 * 使用原生SQL语句查询分页信息
	 * 
	 * @param sql
	 *            查询语句
	 * @param pageControl
	 *            用来控制排序和分页的参数类
	 * @param value
	 *            参数值
	 * @return 查询结果(List列表)
	 * @throws Exception
	 */
	// 通用分页处理
	@SuppressWarnings("unchecked")
	public List<?> getNativeListForPage(final String sql, PageControl pageControl, final Object[] value,
			final Class returnType) throws RuntimeException {
		Session session = getSession();
		List<?> pagelist = null;
		Assert.hasText(sql);
		try {
			// String preFromSql=pageControl.getPreFromSql();
			String groupBy = pageControl.getGroupBy();
			String order = pageControl.getOrder();
			String tmp = sql;
			// if(preFromSql!=null)tmp=preFromSql+" "+tmp;
			if (groupBy != null)
				tmp = tmp + " " + groupBy;
			if (pageControl.isUseCount()) {
				long count = getSqlTotalCount(session, tmp, value);
				pageControl.setRowCount(count); // 更新pagecontrol的各种属性 important
			}
			if (order != null)
				tmp = tmp + " " + order;
			final String allSql = new String(tmp);
			final boolean pcUseCache = pageControl.isUseCache();

			log.debug("SQL查询:" + allSql);

			if (pageControl.isUseQuery()) { // 是否分页
				final int start = pageControl.getStart();
				final int size = pageControl.getPageSize();
				final boolean pcUsePage = pageControl.isUsePage();
				log.debug("start=" + start + ",size=" + size);
				pagelist = getHibernateTemplate().executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {

						SQLQuery query = session.createSQLQuery(allSql);
						// 如果是map类型,返回的是数据库列名为键的键值对，如"user_name"而非"userName"
						if (returnType == Map.class) {
							query.setResultTransformer(org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP);
						} else if (returnType == List.class) {
							query.setResultTransformer(org.hibernate.transform.Transformers.TO_LIST);
						} else {
							query.addEntity(returnType);
						}

						// 只有同时确定使用缓存，才能确定
						if (pcUseCache)
							query.setCacheable(getHibernateTemplate().isCacheQueries());
						else
							query.setCacheable(false);
						if (pcUsePage)
							query.setFirstResult(start).setMaxResults(size);
						setSqlQueryParameter(query, value);
						List<?> result = query.list();
						return result;
					}
				});
			}
		} catch (RuntimeException e) {
			log.error("SQL数据库查询出错:", e);
			throw e;
		} finally {
			// session.close();
			releaseSession(session);
		}
		pageControl.setList(pagelist);
		return pagelist;
	}

	private static void setQueryParameter(final Query query, final Object[] value) {
		if (value != null)
			for (int i = 0, j = 0; j < value.length;) {
				if (value[j] != null) {
					query.setParameter(i++, value[j]);
				}
				j++;
			}
	}

	private static void setSqlQueryParameter(final Query query, final Object[] value) {
		if (value != null)
			for (int i = 0, j = 0; j < value.length;) {
				if (value[j] != null) {
					query.setParameter(i++, value[j]);
				}
				j++;
			}

	}

	public List findByNativeInProperties(PageControl pageControl, String preWhereSql, String[] propertyName,
			List[] valueList, String[] sign, String[] type, Class returnType) {
		StringBuffer hql = new StringBuffer(preWhereSql + " ");
		if (propertyName != null && propertyName.length > 0) {
			// where条件已有
			boolean notContainsWhere = notContainsSimple(hql, "where");
			if (notContainsWhere)
				hql.append(" where ");
			hql.append("(");

			accessSQL(hql, propertyName, valueList, sign, type);

			hql.append(")");
		}

		try {
			return (List<T>) getNativeListForPage(hql.toString(), pageControl, accessValue(valueList), returnType);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * 
	 * 根据conList转换添加SQL条件
	 * 
	 * @param hql
	 * @param propertyName
	 * @param valueList
	 * @param sign
	 * @param type
	 *            void
	 * @exception @since
	 *                1.0.0
	 */
	@SuppressWarnings("unchecked")
	private static void accessSQL(StringBuffer hql, String[] propertyName, List[] valueList, String[] sign,
			String[] type) {
		int i = 0;
		for (i = 0; i < propertyName.length - 1; i++) {
			hql.append( /* " model."+ */propertyName[i] + " " + sign[i]);
			if (valueList[i] != null && valueList[i].size() > 1) {
				hql.append('(');
				for (int j = 0; j < valueList[i].size() - 1; j++) {
					hql.append("?,");
				}
				hql.append("?) " + type[i] + " ");
			} else {
				hql.append(
						(valueList[i] != null && !valueList[i].isEmpty() && valueList[i].get(0) != null ? " (?) " : " ")
								+ type[i] + " ");
			}

		}
		hql.append( /* " model."+ */propertyName[i] + " " + sign[i]);
		if (valueList[i] != null && valueList[i].size() > 1) {
			hql.append('(');
			for (int j = 0; j < valueList[i].size() - 1; j++) {
				hql.append("?,");
			}
			hql.append("?) ");
		} else {
			hql.append(valueList[i] != null && !valueList[i].isEmpty() && valueList[i].get(0) != null ? " (?) " : " ");
		}
		hql.append(getLastType(propertyName, type));
	}

	/**
	 * 判断是否包含，不区分大小写
	 * 
	 * @param hql
	 * @param str
	 * @return boolean
	 * @exception @since
	 *                1.0.0
	 */
	private static boolean notContains(StringBuffer hql, String str) {
		return notContains(hql.toString(), str);
	}

	/**
	 * 判断是否包含，不区分大小写
	 * 
	 * @param hql
	 * @param str
	 * @return boolean
	 * @exception @since
	 *                1.0.0
	 */
	private static boolean notContains(String hql, String str) {
		return (!hql.toLowerCase().contains(str));
	}

	/**
	 * 判断是否包含，不区分大小写
	 * 
	 * @param hql
	 * @param str
	 * @return boolean
	 * @exception @since
	 *                1.0.0
	 */
	private static boolean notContainsSimple(StringBuffer hql, String str) {
		return (!hql.toString().contains(str));
	}

	/**
	 * 
	 * 获取最后的连接词<br/>
	 * (这里描述这个方法适用条件 – 可选)<br/>
	 * (这里描述这个方法的执行流程 – 可选)<br/>
	 * (这里描述这个方法的使用方法 – 可选)<br/>
	 * (这里描述这个方法的注意事项 – 可选)<br/>
	 * 
	 * @param propertyName
	 * @param type
	 * @return String
	 * @exception @since
	 *                1.0.0
	 */
	private static String getLastType(String[] propertyName, String[] type) {
		// 判断最后一个type是否有非or/and的符号如)，若有则附加上
		int tLan = type.length;
		if (tLan == propertyName.length && type[tLan - 1] != null) {
			String tmp = type[tLan - 1].toLowerCase().trim();
			if (!tmp.contains("or") && !tmp.contains("and")) {
				return tmp;
			}
		}
		return " ";
	}

	/**
	 * 使用原生sql语句查询记录个数
	 * 
	 * @param sql
	 *            查询语句
	 * @param pageControl
	 *            用来控制排序和分页的参数类
	 * @param value
	 *            参数值
	 * @return 统计结果
	 */

	public long getSqlTotalCount(String sql, Object[] value) {
		return getTotalCount(getSession(), sql, value);
	}

	/**
	 * 使用原生sql语句查询记录个数
	 * 
	 * @param session
	 * @param sql
	 *            查询语句
	 * @param pageControl
	 *            用来控制排序和分页的参数类
	 * @param value
	 *            参数值
	 * @return 统计结果
	 */
	// 计算表的总行数

	public static long getSqlTotalCount(final Session session, String sql, Object[] value) {
		String count = "0";
		// sql=removeOrders(sql); //去掉排序符号
		String countStr = "select count(1) from (" + sql + ")PickSun";
		Query query = session.createSQLQuery(countStr);
		setSqlQueryParameter(query, value);
		List<?> list = query.list();
		if (list == null || list.size() == 0)
			return 0;
		count = list.get(0).toString();
		return Long.parseLong(count);
	}

	/**
	 * 使用HQL语句查询记录个数
	 * 
	 * @param hql
	 *            查询语句
	 * @param pageControl
	 *            用来控制排序和分页的参数类
	 * @param value
	 *            参数值
	 * @return 统计结果
	 */

	public long getTotalCount(String hql, Object[] value) {
		return getTotalCount(getSession(), hql, value);
	}

	/**
	 * 使用HQL语句查询记录个数
	 * 
	 * @param session
	 * @param hql
	 *            查询语句
	 * @param pageControl
	 *            用来控制排序和分页的参数类
	 * @param value
	 *            参数值
	 * @return 统计结果
	 */
	// 计算表的总行数

	public static long getTotalCount(final Session session, String hql, Object[] value) {
		String count = "0";
		hql = removeOrders(hql); // 去掉排序符号
		int sql_from = hql.indexOf("from");
		String countStr = "";
		countStr = "select count(*) " + hql.substring(sql_from);
		Query query = session.createQuery(countStr);
		setQueryParameter(query, value);
		List<?> list = query.list();
		if (list == null || list.size() == 0)
			return 0;
		count = list.get(0).toString();
		return Long.parseLong(count);
	}

	/**
	 * 返回按属性条件查询的结果列表,采用in方式,即propertyName中第一个propertyName对应valueList中的第一个List，
	 * 以此类推
	 * 
	 * @param pageControl
	 *            用来控制排序和分页的参数类
	 * @param propertyName
	 *            属性名
	 * @param valueList
	 *            属性值列表
	 * @param sign
	 *            符号,如=/>/>=</<=/is null(相应的value必须为空)/in等
	 * @param type
	 *            条件连接类型(and还是or)
	 * @return 查询结果(List列表)
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByInProperties(PageControl pageControl, String[] propertyName, List[] valueList, String[] sign,
			String[] type) {
		StringBuffer hql = createPreWhereSql(pageControl);

		if (propertyName != null && propertyName.length > 0) {
			hql.append(" where (");
			accessSQL(hql, propertyName, valueList, sign, type);
			hql.append(" ) ");
		}

		try {
			return (List<T>) getListForPage(hql.toString().replaceAll("\\(\\?\\)", "\\?"), pageControl,
					accessValue(valueList));
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * 创建where之前的sql语句
	 * 
	 * @param pageControl
	 * @return StringBuffer
	 * @exception @since
	 *                1.0.0
	 */
	private StringBuffer createPreWhereSql(PageControl pageControl) {
		String preFromSql = pageControl.getPreFromSql();
		StringBuffer hql = null;
		boolean notContainsFrom = "".equals(preFromSql) || null == preFromSql;
		if (notContainsFrom)
			hql = new StringBuffer("from " + entityClass.getName() + " as model ");
		else
			hql = new StringBuffer("");
		return hql;
	}

	/**
	 * 使用HQL语句查询分页信息
	 * 
	 * @param hql
	 *            查询语句
	 * @param pageControl
	 *            用来控制排序和分页的参数类
	 * @param value
	 *            参数值
	 * @return 查询结果(List列表)
	 * @throws Exception
	 */
	// 通用分页处理
	public List<?> getListForPage(final String hql, PageControl pageControl, final Object[] value)
			throws RuntimeException {
		Session session = getSession();
		List<?> pagelist = null;

		// Assert.hasText(hql);
		try {

			String preFromSql = pageControl.getPreFromSql();
			String groupBy = pageControl.getGroupBy();
			String order = pageControl.getOrder();
			String tmp = hql;
			if (preFromSql != null)
				tmp = preFromSql + " " + tmp;
			if (groupBy != null)
				tmp = tmp + " " + groupBy;
			if (pageControl.isUseCount()) {
				long count = getTotalCount(session, tmp, value);
				pageControl.setRowCount(count); // 更新pagecontrol的各种属性 important
			}
			if (order != null)
				tmp = tmp + " " + order;
			final String allHql = new String(tmp);
			final boolean pcUseCache = pageControl.isUseCache();
			log.debug("HQL查询:{}", allHql);
			if (pageControl.isUseQuery()) { // 是否分页
				final int start = pageControl.getStart();
				final int size = pageControl.getPageSize();
				final boolean pcUsePage = pageControl.isUsePage();
				log.debug("start, size={}, {}", start, size);

				pagelist = getHibernateTemplate().executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {

						Query query = session.createQuery(allHql);

						// 只有同时确定使用缓存，才能确定
						if (pcUseCache)
							query.setCacheable(getHibernateTemplate().isCacheQueries());
						else
							query.setCacheable(false);

						if (pcUsePage)
							query.setFirstResult(start).setMaxResults(size);

						setQueryParameter(query, value);
						List<?> result = query.list();
						return result;
					}
				});
			}
		} catch (RuntimeException e) {
			log.error("HQL数据库查询出错:", e);
			throw e;
		} finally {
			// session.close();
			releaseSession(session);
		}
		pageControl.setList(pagelist);
		return pagelist;
	}

	/**
	 * 去掉排序字符串
	 * 
	 * @param hql
	 * @return
	 */
	public static String removeOrders(final String hql) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 把所有List的元素拿出来放到一个Array里面
	 * 
	 * @param valueList
	 * @return Object[]
	 * @exception @since
	 *                1.0.0
	 */
	@SuppressWarnings("unchecked")
	public static Object[] accessValue(List[] valueList) {
		List value = new ArrayList();
		for (int j = 0; j < valueList.length; j++) {
			if (valueList[j] != null)
				for (Iterator iterator = valueList[j].iterator(); iterator.hasNext();) {
					value.add(iterator.next());
				}
		}
		return value.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diyeasy.common.dao.IDao#findBySQL(com.diyeasy.common.sql.PageControl,
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List findBySQL(PageControl pageControl, String sql) {
		try {
			return getListForPage(sql, pageControl);
		} catch (RuntimeException re) {
			throw re;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diyeasy.common.dao.IDao#findBySQL(com.diyeasy.common.sql.PageControl,
	 * java.lang.String, java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public List findBySQL(PageControl pageControl, String sql, Object[] object) {
		try {
			return getListForPage(sql, pageControl, object);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * 返回带统计函数、按属性条件查询的结果列表（List
	 * <Map>）,采用in方式,即propertyName中第一个propertyName对应valueList中的第一个List，以此类推
	 * 必须在列前带表的别名如a.colmn1,c.colmn2 非常适合多表并且所得字段分布于多表的查询(外连接支持，但需要配置映射字段)
	 * 非常适合极其复杂的查询(外连接支持，但需要配置映射字段) 若带有统计函数，请不要使用分页功能
	 * 
	 * @param pageControl
	 *            用来控制排序和分页的参数类
	 * @param fromTable
	 *            from表名，多表如A m left outer join B n
	 * @param useProperty
	 *            需要获得的参数名
	 * @param alias
	 *            别名，用map.get(别名)获取参数值:
	 * @param propertyName
	 *            属性名
	 * @param valueList
	 *            属性值列表
	 * @param sign
	 *            符号,如=/>/>=</<=/is null(相应的value必须为空)/in等
	 * @param type
	 *            条件连接类型(and还是or)
	 * @return 查询结果(List列表)
	 */
	@SuppressWarnings("unchecked")
	public List findByStatistics(PageControl pageControl, String fromTable, String[] useProperty, String[] alias,
			String[] propertyName, List[] valueList, String[] sign, String[] type) {
		StringBuffer hql = new StringBuffer("select new Map(");
		hql.append(useProperty[0] + " as " + alias[0]);
		for (int t = 1; t < useProperty.length; t++) {
			hql.append("," + useProperty[t] + " as " + alias[t]);
		}
		hql.append(") from " + fromTable);
		if (propertyName != null && propertyName.length > 0) {
			// where条件已有
			boolean notContainsWhere = notContains(hql, "where");
			if (notContainsWhere)
				hql.append(" where ");
			hql.append("(");

			accessSQL(hql, propertyName, valueList, sign, type);

			hql.append(")");

		}

		try {
			return getListForPage(hql.toString(), pageControl, accessValue(valueList));
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<?> getListForPage(final String hql, PageControl pageControl) throws RuntimeException {
		return getListForPage(hql, pageControl, null);
	}

	public List<?> getListForPage(final String hql, PageControl pageControl, final Object value) throws Exception {
		return getListForPage(hql, pageControl, new Object[] { value });
	}

	/**
	 * 
	 * 根据conList转换添加SQL条件
	 * 
	 * @param hql
	 * @param propertyName
	 * @param value
	 * @param sign
	 * @param type
	 *            void
	 * @exception @since
	 *                1.0.0
	 */
	private static void accessSQL(StringBuffer hql, String[] propertyName, Object[] value, String[] sign,
			String[] type) {
		int i = 0;
		for (i = 0; i < propertyName.length - 1; i++) {
			hql.append(
					/* " model."+ */propertyName[i] + " " + sign[i] + (value[i] != null ? " ? " : " ") + type[i] + " ");
		}
		hql.append( /* " model."+ */propertyName[i] + " " + sign[i] + (value[i] != null ? " ? " : " "));
		hql.append(getLastType(propertyName, type));
	}

	public boolean existDomain(T domain) {
		List<SqlCondGroup> conList = new ArrayList<SqlCondGroup>();
		PageControl pageControl = PageControl.getCountOnlyInstance();
		findByCondition(pageControl, conList);
		return pageControl.getRowCount() > 0;
	}

	public int updateByNativeCondition(String updateSql, List<SqlCondGroup> conList) {
		int listSize = conList.size();
		List<String> propertyName = new ArrayList<String>(listSize);
		List<String> sign = new ArrayList<String>(listSize);
		List<String> type = new ArrayList<String>(listSize);
		List<List> valueList = new ArrayList<List>(listSize);

		accessCondition(conList, propertyName, sign, type, valueList);
		return updateByNativeInProperties(updateSql, propertyName.toArray(new String[] {}),
				valueList.toArray(new List[] {}), sign.toArray(new String[] {}), type.toArray(new String[] {}));
	}

	public int updateByCondition(String updateSql, List<SqlCondGroup> conList) {
		int listSize = conList.size();
		List<String> propertyName = new ArrayList<String>(listSize);
		List<String> sign = new ArrayList<String>(listSize);
		List<String> type = new ArrayList<String>(listSize);
		List<List> valueList = new ArrayList<List>(listSize);

		accessCondition(conList, propertyName, sign, type, valueList);
		return updateByInProperties(updateSql, propertyName.toArray(new String[] {}), valueList.toArray(new List[] {}),
				sign.toArray(new String[] {}), type.toArray(new String[] {}));
	}

	public int updateByCondition(List<SqlCondGroup> setList, List<SqlCondGroup> conList) {
		int listSize = conList.size();
		StringBuffer sb = new StringBuffer("update " + entityClass.getName() + " set ");
		List<String> propertyName = new ArrayList<String>(listSize);
		List<String> sign = new ArrayList<String>(listSize);
		List<String> type = new ArrayList<String>(listSize);
		List<List> valueList = new ArrayList<List>(listSize);
		String linkStr = "";
		for (SqlCondGroup con : setList) {
			if (!DataUtil.isNull(con.getValue())) {
				// sb.append(linkStr +" "+ con.getName()+" "+ " = " +
				// con.getValue() + " ");
				sb.append(linkStr + " " + con.getName() + " " + " = ? ");
				valueList.add(Arrays.asList(con.getValue()));
				linkStr = " , ";
			}
		}

		if (conList != null && conList.size() > 0) {
			sb.append(" where ");
			accessCondition(conList, propertyName, sign, type, valueList);
		}

		return updateByInProperties(sb.toString(), propertyName.toArray(new String[] {}),
				valueList.toArray(new List[] {}), sign.toArray(new String[] {}), type.toArray(new String[] {}));
	}

	/**
	 * 根据sql修改表格，要根据属性条件判断
	 * 
	 * @param updateSql
	 * @param propertyName
	 * @param valueList
	 * @param sign
	 * @param type
	 * @return int
	 * @exception @since
	 *                1.0.0
	 */
	@SuppressWarnings("unchecked")
	public int updateByNativeInProperties(String updateSql, String[] propertyName, List[] valueList, String[] sign,
			String[] type) {
		StringBuffer hql = new StringBuffer(updateSql);

		if (propertyName != null && propertyName.length > 0) {
			hql.append("  (");
			accessSQL(hql, propertyName, valueList, sign, type);
			hql.append(" ) ");
		}

		try {
			return updateByNativeSql(hql.toString().replaceAll("\\(\\?\\)", "\\?"), accessValue(valueList));
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * 返回按属性条件查询的结果列表,采用in方式,即propertyName中第一个propertyName对应valueList中的第一个List，
	 * 以此类推
	 * 
	 * @param pageControl
	 *            用来控制排序和分页的参数类
	 * @param propertyName
	 *            属性名
	 * @param valueList
	 *            属性值列表
	 * @param sign
	 *            符号,如=/>/>=</<=/is null(相应的value必须为空)/in等
	 * @param type
	 *            条件连接类型(and还是or)
	 * @return
	 * @return 查询结果(List列表)
	 */
	@SuppressWarnings("unchecked")
	public int updateByInProperties(String updateSql, String[] propertyName, List[] valueList, String[] sign,
			String[] type) {
		StringBuffer hql = new StringBuffer(updateSql);

		if (propertyName != null && propertyName.length > 0) {
			hql.append("  (");
			accessSQL(hql, propertyName, valueList, sign, type);
			hql.append(" ) ");
		}

		try {
			return updateByHql(hql.toString().replaceAll("\\(\\?\\)", "\\?"), accessValue(valueList));
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * 根据sql进行修改
	 * 
	 * @param hql
	 * @param value
	 * @return
	 * @throws RuntimeException
	 *             int
	 * @exception @since
	 *                1.0.0
	 */
	public int updateByHql(final String hql, final Object[] value) throws RuntimeException {
		Session session = getSession();
		T bean = null;
		int count = 0;
		// Assert.hasText(hql);
		try {
			final String tmp = hql;
			{
				count = getHibernateTemplate().execute(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {

						Query query = session.createQuery(tmp);

						setQueryParameter(query, value);
						int count = query.executeUpdate();
						return count;
					}
				});
			}
		} catch (RuntimeException e) {
			log.error("HQL数据库查询出错:", e);
			throw e;
		} finally {
			// session.close();
			releaseSession(session);
		}
		return count;
	}

	/**
	 * 根据sql进行修改
	 * 
	 * @param hql
	 * @param value
	 * @return
	 * @throws RuntimeException
	 *             int
	 * @exception @since
	 *                1.0.0
	 */
	public int updateByNativeSql(final String hql, final Object[] value) throws RuntimeException {
		Session session = getSession();
		T bean = null;
		int count = 0;
		// Assert.hasText(hql);
		try {
			final String tmp = hql;
			{
				count = getHibernateTemplate().execute(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {

						Query query = session.createSQLQuery(tmp);

						setSqlQueryParameter(query, value);
						int count = query.executeUpdate();
						return count;
					}
				});
			}
		} catch (RuntimeException e) {
			log.error("HQL数据库查询出错:", e);
			throw e;
		} finally {
			// session.close();
			releaseSession(session);
		}
		return count;
	}

}
