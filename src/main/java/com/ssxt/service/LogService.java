package com.ssxt.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

 
import com.ssxt.dao.LogDao;
 
import com.ssxt.entity.mysql.Log;
 

import common.dao.GenericDao;
import common.dao.dynamic.sessionFactory.CustomerContextHolder;
import common.page.PageControl;
import common.page.SqlBuffer;
import common.service.GenericServiceImpl;

@Service(value = "logService")
public class LogService extends GenericServiceImpl<Log, Integer> {

	public GenericDao<Log, Integer> getDao() {
		return logDao;
	}

	@Resource(name = "logDao")
	private LogDao logDao;

}
