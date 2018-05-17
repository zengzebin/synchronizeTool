package com.ssxt.dbConfig.Impl;

import java.util.Map;

import com.ssxt.dbConfig.DbInfo;
import com.ssxt.entity.DbConfig;

public class SqlServerInfo implements DbInfo {

	@Override
	public DbConfig getDbConfig(Map<String, String> info) {
		DbConfig config = new DbConfig();
		config.setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		String url = "jdbc:sqlserver://{ip}:{port};DatabaseName={dbName}";
		url = url.replaceAll("\\{ip\\}", info.get("ip"));
		url = url.replaceAll("\\{port\\}", info.get("port"));
		url = url.replaceAll("\\{dbName\\}", info.get("dbName"));
		config.setUrl(url);
//		config.setUserName(info.get("userName"));
//		config.setPassword(info.get("password"));
		// TODO Auto-generated method stub
		return config;
	}

}
