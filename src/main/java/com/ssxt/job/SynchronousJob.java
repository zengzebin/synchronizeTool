package com.ssxt.job;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import com.ssxt.entity.FileInfo;
import com.ssxt.entity.mysql.Log;
import com.ssxt.entity.mysql.RtuGroundWater;
import com.ssxt.service.LogService;
import com.ssxt.service.RtuWaterService;

import common.http.AccData;
import common.util.CacheMgr;
import common.util.ConstParam;
import common.util.DataConnection;
import common.util.DateConverter;
import common.util.DynamicBean;
import common.util.PropertyFile;
import common.util.SpringContextUtil;
import common.util.ToolUtil;

import java.sql.Timestamp;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SynchronousJob implements Job {
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SynchronousJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

//		JobDetail job = arg0.getJobDetail();
//		String taskName = job.getJobDataMap().get("taskName").toString();
		/*
		 * Task2 task2 = new Task2(taskName); //task2.init();
		 */
		String taskName ="demo";
		try {
			log.info("============= " + taskName + " 任务开始执行 ==============");
			ToolUtil tool = new ToolUtil();
//			FileInfo config = ToolUtil.analysisFile(taskName);
		    FileInfo config = CacheMgr.getInstance().getValue(taskName);
			// config=null;
			if (config == null)
				return;

			DataConnection source = ToolUtil.getDataConnection(config, taskName, "source");

			String[] sourceFieldName = config.getSourceFieldName().split(",");// 获取查询的列
			String[] targetFieldName = config.getTargetFieldName().split(","); // 目标列
			String[] fieldTypes = config.getFieldType().split(",");// 字段类型

			// 动态生成bean
			HashMap propertyMap = ToolUtil.createBean(targetFieldName, fieldTypes, taskName);
			if (propertyMap == null)
				return;

			Connection sourceCon = source.getConnection(taskName + " 获取源连接 ");// 获取源连接
			if (sourceCon == null) {
				return;
			}
			// 查询数据
			PreparedStatement p=source.findSql(config.getSourceSql(), sourceCon);
			List<DynamicBean> list = source.getFindSql(p, propertyMap, config.getSourceSql(), sourceFieldName,
					targetFieldName, fieldTypes);

			log.info(taskName + "  查询结果记录数=" + list.size());

			source.freeConnection(sourceCon);
			if (list.size() != 0) {

				

				DataConnection target = tool.getDataConnection(config, taskName, "target");
			
				log.info("******************任务：" + taskName + " 开始组装更新插入语句***************");
				
				List<String> insertSqls = new ArrayList<String>();
				List<String> updateSqls = new ArrayList<String>();
				List<String> sqls = new ArrayList<String>();

				//判断是否更新语句
				if (config.getExistSql() != null) {
					Connection targetCon = target.getConnection(taskName + " 获取更新插入目标连接源  ");// 获取目标连接
					if (targetCon == null)
						return;
					Iterator<DynamicBean> iter = list.iterator();
					while (iter.hasNext()) {
						DynamicBean bean = iter.next();
						String existSql = tool.IsExistSql(bean, config.getExistSql(), taskName);
						int count = target.IsExistDB(targetCon, existSql);
						if (count > 0) {
							//更新语句
							String updateSql = tool.createUpdateSql(bean, config.getTargetTable(), taskName, existSql);
							updateSqls.add(updateSql);
							//移除掉防止在生成插入语句
							iter.remove();
							
						}
					}
					target.freeConnection(targetCon);
					
				}

				//插入语句
				for (int i = 0; i < list.size(); i++) {
					DynamicBean bean = list.get(i);
					String insertSql = tool.createInsertSql(bean, config.getTargetTable(), taskName);
					insertSqls.add(insertSql);
				}

				sqls.addAll(updateSqls);
				sqls.addAll(insertSqls);

				log.info("******************任务：" + taskName + " 开始执行更新插入语句***************");

				Connection targetCon = target.getConnection(taskName + " 获取更新插入目标连接源  ");// 获取目标连接
				for (String sql : sqls) {
					if (config.getShowSql().equals("true"))
						target.insertOrUpdateDB(targetCon, sql, true);
					else
						target.insertOrUpdateDB(targetCon, sql, false);
				}
				target.freeConnection(targetCon);
				log.info("****************任务：" + taskName + " 结束 *******************");
			} else {
				log.info("***************任务：" + taskName + " 程序不到数据无法同步，结束当前任务 ***************");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("任务:" + taskName + " 同步失败 ", e);
		}

	}

	private void createSql(String taskName, FileInfo config, List<DynamicBean> list, DataConnection target,
			Connection targetCon, List<String> insertSqls, List<String> updateSqls) {
		if (config.getExistSql() != null) {
			Iterator<DynamicBean> iter = list.iterator();
			while (iter.hasNext()) {
				DynamicBean bean = iter.next();
				String existSql = ToolUtil.IsExistSql(bean, config.getExistSql(), taskName);
				int count = target.IsExistDB(targetCon, existSql);
				if (count > 0) {
					String updateSql = ToolUtil.createUpdateSql(bean, config.getTargetTable(), taskName, existSql);
					updateSqls.add(updateSql);
					iter.remove();
				}
			}
		}

		for (int i = 0; i < list.size(); i++) {
			DynamicBean bean = list.get(i);
			String insertSql = ToolUtil.createInsertSql(bean, config.getTargetTable(), taskName);
			insertSqls.add(insertSql);
		}
	}

	/**
	 * 获取对象
	 * 
	 * @param config
	 * @param taskName
	 * @param type
	 * @return
	 */
	public DataConnection getDataConnection(FileInfo config, String taskName, String type) {
		DataConnection db = null;
		if (type.equals("source")) {
			db = new DataConnection(config.getSourceDbType(), config.getSourceIp(), config.getSourcePort(),
					config.getSourceDbName(), config.getSourceUsername(), config.getSourcePassword(), taskName);
		} else {
			db = new DataConnection(config.getTargetDbType(), config.getTargetIp(), config.getTargetPort(),
					config.getTargetDbName(), config.getTargetUsername(), config.getTargetPassword(), taskName);
		}

		return db;
	}

	public static void main(String[] args) throws URISyntaxException {
		// System.out.println(SynchronousJob.getDate(0,0, "yyyy-MM-dd
		// HH:mm:ss"));
		String url = "jdbc:mysql://{ip}:{port}/{dbName}";
		String sql = "jdbc:mysql://{ip}:{port}/{dbName}";
		sql = url.replaceAll("\\{ip\\}", "22");
		System.out.println(sql);
		StringBuffer select = new StringBuffer();
		/*
		 * String replace; while (true) {
		 * 
		 * int startIndex = sql.indexOf("{"); int endIndex = sql.indexOf("}");
		 * if (startIndex == -1) break; replace = sql.substring(startIndex + 1,
		 * endIndex);
		 * 
		 * System.out.print(replace); sql = sql.replaceAll("\\{" + replace +
		 * "\\}", "a.name"); } System.out.println(sql);
		 */

	}

}