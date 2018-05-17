package com.ssxt.dbConfig;

import java.util.Map;

import com.ssxt.entity.DbConfig;

public interface DbInfo {
	
	public DbConfig getDbConfig(Map <String,String> info);

}
