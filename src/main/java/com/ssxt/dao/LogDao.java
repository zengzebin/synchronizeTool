package com.ssxt.dao;

 


import org.springframework.stereotype.Repository;

import com.ssxt.entity.mysql.Log;

import common.dao.GenericDaoMysqlImpl;
  

@Repository(value = "logDao")
public   class LogDao extends GenericDaoMysqlImpl<Log, Integer> { 
}
