package com.ssxt.Listener;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.stereotype.Component;

import com.ssxt.job.QuartzManager;

import common.util.SpringContextUtil;

public class InitTaskListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		// 执行初始化操作
		QuartzManager quartzManager = (QuartzManager) SpringContextUtil.getBean("quartzManager");
		boolean is=quartzManager.initTask();
		if(is)
		quartzManager.startJobs();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
