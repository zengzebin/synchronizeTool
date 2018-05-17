package common.dao.dynamic.sessionFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.TypeHelper;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

/**
 * <b>function:</b> 动态数据源实现
 * 
 * @author hoojo
 * @createDate 2013-10-12 下午03:31:31
 * @file DynamicSessionFactoryImpl.java
 * @package com.hoo.framework.spring.support.core
 * @project SHMB
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public class DynamicSessionFactoryImpl implements DynamicSessionFactory {

	private static final long serialVersionUID = 5384069312247414885L;

	private Map<Object, SessionFactory> targetSessionFactorys;
	private SessionFactory defaultTargetSessionFactory;

	/**
	 * @see com.hoo.framework.spring.support.core.DynamicSessionFactory#getHibernateSessionFactory()
	 *      <b>function:</b> 重写这个方法，这里最关键
	 * @author hoojo
	 * @createDate 2013-10-18 上午10:45:25
	 */

	public SessionFactory getHibernateSessionFactory() {
		SessionFactory targetSessionFactory = targetSessionFactorys.get(CustomerContextHolder.getCustomerType());
		if (targetSessionFactory != null) {
			return targetSessionFactory;
		} else if (defaultTargetSessionFactory != null) {
			return defaultTargetSessionFactory;
		}
		return null;
	}

	public void close() throws HibernateException {
		this.getHibernateSessionFactory().close();
	}

	public boolean containsFetchProfileDefinition(String s) {
		return this.getHibernateSessionFactory().containsFetchProfileDefinition(s);
	}

	public void evict(Class clazz) throws HibernateException {
		this.getHibernateSessionFactory().evict(clazz);
	}

	public void evict(Class clazz, Serializable serializable) throws HibernateException {
		this.getHibernateSessionFactory().evict(clazz, serializable);
	}

	public void evictCollection(String s) throws HibernateException {
		this.getHibernateSessionFactory().evictCollection(s);
	}

	public void evictCollection(String s, Serializable serializable) throws HibernateException {
		this.getHibernateSessionFactory().evictCollection(s, serializable);
	}

	public void evictEntity(String entity) throws HibernateException {
		this.getHibernateSessionFactory().evictEntity(entity);
	}

	public void evictEntity(String entity, Serializable serializable) throws HibernateException {
		this.getHibernateSessionFactory().evictEntity(entity, serializable);
	}

	public void evictQueries() throws HibernateException {
		this.getHibernateSessionFactory().evictQueries();
	}

	public void evictQueries(String queries) throws HibernateException {
		this.getHibernateSessionFactory().evictQueries(queries);
	}

	public Map<String, ClassMetadata> getAllClassMetadata() {
		return this.getHibernateSessionFactory().getAllClassMetadata();
	}

	public Map getAllCollectionMetadata() {
		return this.getHibernateSessionFactory().getAllClassMetadata();
	}

	public Cache getCache() {
		return this.getHibernateSessionFactory().getCache();
	}

	public ClassMetadata getClassMetadata(Class clazz) {
		return this.getHibernateSessionFactory().getClassMetadata(clazz);
	}

	public ClassMetadata getClassMetadata(String classMetadata) {
		return this.getHibernateSessionFactory().getClassMetadata(classMetadata);
	}

	public CollectionMetadata getCollectionMetadata(String collectionMetadata) {
		return this.getHibernateSessionFactory().getCollectionMetadata(collectionMetadata);
	}

	public Session getCurrentSession() throws HibernateException {
		return this.getHibernateSessionFactory().getCurrentSession();
	}

	public Set getDefinedFilterNames() {
		return this.getHibernateSessionFactory().getDefinedFilterNames();
	}

	public FilterDefinition getFilterDefinition(String definition) throws HibernateException {
		return this.getHibernateSessionFactory().getFilterDefinition(definition);
	}

	public Statistics getStatistics() {
		return this.getHibernateSessionFactory().getStatistics();
	}

	public TypeHelper getTypeHelper() {
		return this.getHibernateSessionFactory().getTypeHelper();
	}

	public boolean isClosed() {
		return this.getHibernateSessionFactory().isClosed();
	}

	public Session openSession() throws HibernateException {
		return this.getHibernateSessionFactory().openSession();
	}

	public Session openSession(Interceptor interceptor) throws HibernateException {
		return this.getHibernateSessionFactory().openSession(interceptor);
	}

	public Session openSession(Connection connection) {
		return this.getHibernateSessionFactory().openSession(connection);
	}

	public Session openSession(Connection connection, Interceptor interceptor) {
		return this.getHibernateSessionFactory().openSession(connection, interceptor);
	}

	public StatelessSession openStatelessSession() {
		return this.getHibernateSessionFactory().openStatelessSession();
	}

	public StatelessSession openStatelessSession(Connection connection) {
		return this.getHibernateSessionFactory().openStatelessSession(connection);
	}

	public Reference getReference() throws NamingException {
		return this.getHibernateSessionFactory().getReference();
	}

	public void setTargetSessionFactorys(Map<Object, SessionFactory> targetSessionFactorys) {
		this.targetSessionFactorys = targetSessionFactorys;
	}

	public void setDefaultTargetSessionFactory(SessionFactory defaultTargetSessionFactory) {
		this.defaultTargetSessionFactory = defaultTargetSessionFactory;
	}

}
