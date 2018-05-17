package common.dao.dynamic.sessionFactory;

import org.hibernate.SessionFactory;
/**
 * <b>function:</b> 动态SessionFactory接口
 * @author hoojo
 * @createDate 2013-10-12 下午03:29:52
 * @file DynamicSessionFactory.java
 * @package com.hoo.framework.spring.support.core
 * @project SHMB
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface DynamicSessionFactory extends SessionFactory { 
	public SessionFactory getHibernateSessionFactory();
}
