package com.ssxt.job;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

import com.ssxt.entity.FileInfo;
import com.ssxt.entity.mysql.JobTask;
import com.ssxt.entity.mysql.Log;

import common.util.CacheMgr;
import common.util.ConstParam;
import common.util.DataConnection3;
import common.util.PropertyFile;
import common.util.ToolUtil3;
import common.util.ToolUtil;

/**
 * @Description: 定时任务管理类
 * 
 * @ClassName: QuartzManager
 * @Copyright: Copyright (c) 2014
 * 
 * @author Comsys-LZP
 * @date 2014-6-26 下午03:15:52
 * @version V2.0
 */

@Service("quartzManager")
public class QuartzManager {
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(QuartzManager.class);

	private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
	private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";
	/*
	 * 当初我初始化的是 SchedulerFactoryBean schedulerFactoryBean； 这样是注入不进去的 报下面的错
	 * nested exception is
	 * org.springframework.beans.factory.BeanNotOfRequiredTypeException: Bean
	 * named 'schedulerFactoryBean' must be of type
	 * [org.springframework.scheduling.quartz.SchedulerFactoryBean], but was
	 * actually of type [org.quartz.impl.StdScheduler>]
	 * 看spring源码可以知道，其实spring得到的是一个工厂bean，得到的不是它本身，而是它负责创建的org.quartz.impl.
	 * StdScheduler对象 所以要使用Scheduler对象
	 */
	@Resource
	private Scheduler scheduler;

	public Scheduler getScheduler() {
		return scheduler;

	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * 添加一个定时任务
	 */
	public void addJob(JobTask job) throws SchedulerException {

		// Class classz = getClass(job.getClassName());
		Class classz = SynchronousJob.class;
		// 这里获取任务信息数据
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), JOB_GROUP_NAME);
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		if (trigger == null) {
			// 不存在，创建一个
			/*
			 * JobDetail jobDetail =
			 * JobBuilder.newJob(classz).withIdentity(job.getJobName(),
			 * JOB_GROUP_NAME).build();
			 */

			JobDetail jobDetail = JobBuilder.newJob(classz).withIdentity(job.getJobName(), JOB_GROUP_NAME)
					.usingJobData("taskName", job.getJobName()).build();

			jobDetail.getJobDataMap().put("scheduleJob", job);
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getJobTime());
			// 按新的cronExpression表达式构建一个新的trigger
			trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), JOB_GROUP_NAME)
					.withSchedule(scheduleBuilder).build();
			scheduler.pauseJob(jobDetail.getKey());// 暂停任务
			scheduler.scheduleJob(jobDetail, trigger);
			log.info("=========创建任务:" + job.getJobName());
			// scheduler.shutdown();

		} else {
			// Trigger已存在，那么更新相应的定时设置
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getJobTime());
			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		}
	}

	/**
	 * 启动所有定时任务
	 */
	public void startJobs() {
		try {
			scheduler.start();
			scheduler.resumeAll(); // 恢复全部任务
			log.info("=======启动全部任务=========");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public boolean initTask() {
		boolean is = true;
		String[] jobName = ConstParam.getProperty("task.name").split(",");
		String[] jobTime = ConstParam.getProperty("task.cron").split(",");
		if (jobName.length != jobTime.length) {
			log.error("config.properties 配置的数目不对请检查");
			return false;
		}

		for (int i = 0; i < jobTime.length; i++) {
			JobTask job = new JobTask();
			job.setJobTime(jobTime[i].trim());
			job.setJobName(jobName[i].trim());

			try {
				addJob(job);
				initFileConfig(jobName[i].trim());
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				log.error(jobName[i] + "任务调度配置错误", e);
				is = false;
				break;
			} catch (IOException e) {
				log.error(jobName[i] + "任务配置解析错误", e);
				is = false;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(jobName[i] + "任务初始化错误", e);
				is = false;
			}
			if (!is)
				break;

		}
		return is;
	}

	public boolean initTask2() {
		boolean is = true;
		String[] jobClass = ConstParam.getProperty("task.class").split(",");
		String[] jobName = ConstParam.getProperty("task.name").split(",");
		String[] jobTime = ConstParam.getProperty("task.cron").split(",");
		if ((jobName.length != jobTime.length) || (jobTime.length != jobClass.length)) {
			log.error("config.properties 配置的数目不对请检查");
			return false;
		}

		for (int i = 0; i < jobTime.length; i++) {
			JobTask job = new JobTask();
			job.setJobTime(jobTime[i].trim());
			job.setJobName(jobName[i].trim());
			job.setClassName(jobClass[i].trim());
			try {
				addJob(job);
				initFileConfig(jobName[i].trim());
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				log.error("任务配置错误", e);
				is = false;
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("初始化错误", e);
				is = false;
				break;
			}

		}
		return is;
	}

	public void initFileConfig(String jobName) throws Exception {
		PropertyFile property = new PropertyFile(jobName + ".properties");
		FileInfo config = new FileInfo();
		config.setSourceDbType(property.getProperty("source.dbType"));
		config.setSourceIp(property.getProperty("source.Ip"));
		config.setSourcePort(property.getProperty("source.port"));
		config.setSourceDbName(property.getProperty("source.dbName"));
		config.setSourceUsername(property.getProperty("source.username"));
		config.setSourcePassword(property.getProperty("source.password"));
		config.setSourceFieldName(property.getProperty("sourceFieldName").toLowerCase());

		config.setTargetDbType(property.getProperty("target.dbType"));
		config.setTargetIp(property.getProperty("target.Ip"));
		config.setTargetPort(property.getProperty("target.port"));
		config.setTargetDbName(property.getProperty("target.dbName"));
		config.setTargetUsername(property.getProperty("target.username"));
		config.setTargetPassword(property.getProperty("target.password"));
		config.setTargetFieldName(property.getProperty("targetFieldName").toLowerCase());

		String sourceSql = property.getProperty("sourceSql");

		config.setTargetTable(property.getProperty("targetTable"));
		config.setFieldType(property.getProperty("fieldType").toLowerCase());
		config.setShowSql(property.getProperty("showSql"));
		config.setExistSql((property.getProperty("existSql")));

		config.setSourceSql(ToolUtil.replaceStr(sourceSql, property, jobName));
		// log.info(Thread.currentThread().getName() + jobName + ".properties
		// 解析完成");

		CacheMgr.getInstance().addCache(jobName, config);
	}

	public Class getClass(String className) {
		Class classz = null;
		if (className.equals("SynchronousJob")) {
			classz = SynchronousJob.class;
		}
		// else if (className.equals("SynchronousJob3")) {
		// classz = SynchronousJob3.class;
		// }
		/*
		 * } else if (className.equals("SynchronousJob3")) { classz =
		 * SynchronousJob3.class; }
		 */

		return classz;
	}

}