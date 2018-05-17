package common.util;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ssxt.entity.FileInfo;
 
import java.sql.Connection;

public class ToolUtil {

	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ToolUtil.class);

	/**
	 * 解析配置文件
	 * 
	 * @param taskName
	 * @return
	 */
	public static synchronized FileInfo analysisFile(String taskName) {

		log.info("当前线程" + Thread.currentThread().getId() + "  " + "开始解析" + taskName + ".properties");

		FileInfo config = new FileInfo();
		try {
			PropertyFile property = new PropertyFile(taskName + ".properties");

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

			config.setSourceSql(replaceStr(sourceSql, property, taskName));
			log.info(Thread.currentThread().getName() + taskName + ".properties 解析完成");
		} catch (Exception e) {
			log.error("当前线程" + Thread.currentThread().getId() + " " + "任务:" + taskName + " 配置文件解析失败", e);
			config = null;
		}

		return config;

	}

	public static synchronized String createInsertSql(DynamicBean bean, String table, String taskName) {
		String insertSql = null;
		try {

			Object object = bean.getObject();
			Class clazz = object.getClass();
			Field[] fields = clazz.getDeclaredFields();
			StringBuffer sqlFiled = new StringBuffer("insert into " + table + " (");
			StringBuffer valuesFiled = new StringBuffer();
			for (int j = 0; j < fields.length; j++) {
				Field field = fields[j];
				String fieldName = field.getName().replace("$cglib_prop_", "").trim();
				String fieldtype = field.getGenericType().toString().replace("class", "").trim();
				sqlFiled.append(fieldName + ",");
				// System.out.println(fieldtype);
				if (fieldtype.equals("java.lang.Integer"))
					valuesFiled.append(bean.getValue(fieldName) + ",");
				else {
					if (bean.getValue(fieldName) == null)
						valuesFiled.append(bean.getValue(fieldName) + ",");
					else
						valuesFiled.append("'" + bean.getValue(fieldName) + "',");
				}

			}
			insertSql = sqlFiled.subSequence(0, sqlFiled.length() - 1).toString() + ") values("
					+ valuesFiled.subSequence(0, valuesFiled.length() - 1).toString() + ")";

		} catch (Exception e) {
			log.error("任务:" + taskName + " 创建插入语句失败 ===" + insertSql, e);
		}
		return insertSql;
	}

	public static synchronized String createUpdateSql(DynamicBean bean, String table, String taskName, String where) {
		String sql = null;
		try {
			Object object = bean.getObject();
			Class clazz = object.getClass();
			Field[] fields = clazz.getDeclaredFields();
			StringBuffer updateSql = new StringBuffer("update " + table + " set ");
			String xmString;
			for (int j = 0; j < fields.length; j++) {
				Field field = fields[j];
				String fieldName = field.getName().replace("$cglib_prop_", "").trim();
				String fieldtype = field.getGenericType().toString().replace("class", "").trim();

				// System.out.println(fieldtype);
				if (fieldtype.equals("java.lang.Integer"))
					updateSql.append(fieldName + "=" + bean.getValue(fieldName) + ",");
				else {
					if (bean.getValue(fieldName) == null)
						updateSql.append(fieldName + "=" + bean.getValue(fieldName) + ",");
					else
						updateSql.append(fieldName + "='" + bean.getValue(fieldName) + "',");
				}

			}

			int index = where.indexOf("where");
			sql = updateSql.substring(0, updateSql.length() - 1) + where.substring(index - 1, where.length());

		} catch (Exception e) {
			log.error("任务:" + taskName + " 创建更新语句失败===", e);
		}
		return sql;
	}

	/**
	 * 是否存在
	 * 
	 * @param beans
	 * @param taskName
	 */
	public static synchronized String IsExistSql(DynamicBean bean, String sql, String taskName) {
		try {

			while (true) {
				String replace;
				int startIndex = sql.indexOf("[");
				int endIndex = sql.indexOf("]");
				if (startIndex == -1)
					break;
				replace = sql.substring(startIndex + 1, endIndex);

				// System.out.print(replace.toLowerCase() + "=" +
				// bean.getValue((replace.toLowerCase())).toString());
				sql = sql.replaceAll("\\[" + replace + "\\]", bean.getValue((replace.toLowerCase())).toString());
				// sql = sql.replaceAll("\\{" + replace + "\\}",
				// bean.getValue((replace)).toString());

			}
		} catch (Exception e) {
			log.error("任务:" + taskName + " 生成是否有记录语句失败  模板是====" + sql, e);
		}
		return sql;
	}

	public static String replaceStr(String sourceSql, PropertyFile property, String taskName) {
		try {
			String replace;
			while (true) {

				int startIndex = sourceSql.indexOf("{");
				int endIndex = sourceSql.indexOf("}");
				if (startIndex == -1)
					break;
				replace = sourceSql.substring(startIndex + 1, endIndex);

				/* System.out.print(replace); */
				if (replace.contains("designatedTime")) {
					String[] designatedTime = property.getProperty(replace).split(",");
					sourceSql = sourceSql.replaceAll("\\{" + replace + "\\}",
							getDate(designatedTime[0], designatedTime[1], designatedTime[2]));
				} else {
					sourceSql = sourceSql.replaceAll("\\{" + replace + "\\}", property.getProperty(replace));
				}

			}
		} catch (Exception e) {
			log.error("任务:" + taskName + " 查询语句生成失败  ", e);
		}
		return sourceSql;
	}

	/*
	 * public static String createInsertSql(String [] fileds, String table,
	 * String taskName) { String sql = ""; try { StringBuffer sqlFiled = new
	 * StringBuffer("insert into " + table + " ("); StringBuffer valuesFiled =
	 * new StringBuffer();
	 * 
	 * for (int i = 0; i < fileds.length; i++) { String field = fileds[i];
	 * sqlFiled.append(field + ","); valuesFiled.append("?,"); } sql =
	 * sqlFiled.subSequence(0, sqlFiled.length() - 1).toString() + ") values(" +
	 * valuesFiled.subSequence(0, valuesFiled.length() - 1).toString() + ")";
	 * 
	 * } catch (Exception e) { log.error("任务:" + taskName + " 创建插入语句失败 ", e);
	 * e.printStackTrace(); } return sql; }
	 */

	/**
	 * 创建动态bean
	 * 
	 * @param fieldName
	 * @param fieldType
	 * @return
	 * @throws Exception
	 */
	public static synchronized HashMap createBean(String[] fieldName, String[] fieldType, String taskName)
			throws Exception {
		// HashMap propertyMap = new HashMap();
		HashMap <String,Class<?>>propertyMap = new LinkedHashMap<String,Class<?>>();
		try {

			for (int i = 0; i < fieldName.length; i++) {
				propertyMap.put(fieldName[i], Class.forName(getType(fieldType[i])));
			}
			// propertyMap.put("update", Class.forName(getType("int")));
		} catch (Exception e) {
			log.error("任务:" + taskName + " 动态bean生成失败  ", e);
			propertyMap = null;
		}
		return propertyMap;

	}

	public static synchronized String getType(String value) {
		String type = "java.lang.String";

		if (value.equals("int"))
			type = "java.lang.Integer";

		else if (value.equals("double"))
			type = "java.lang.Double";

		else if (value.equals("float"))
			type = "java.lang.Float";

		else if (value.equals("long"))
			type = "java.lang.Long";

		else if (value.equals("decimal") || value.equals("numeric"))
			type = "java.math.BigDecimal";
		return type;

	}

	/**
	 * 格式化字符串
	 * 
	 * @param str
	 * @param arr
	 * @return
	 */
	private synchronized String fillStringByArgs(String str, List<String> arr, String taskName) {
		try {
			Matcher m = Pattern.compile("\\{(\\d)\\}").matcher(str);
			while (m.find()) {
				str = str.replace(m.group(), arr.get(Integer.parseInt(m.group(1))));
			}
		} catch (Exception e) {
			log.error("任务:" + taskName + " 模板替换失败  " + str, e);
			str = "error";
		}
		return str;
	}

	/**
	 * 获取时间
	 * 
	 * @param format
	 * @param day
	 * @param hour
	 * @return
	 */
	public static synchronized String getDate(String format, String hour, String min) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.HOUR, Integer.parseInt(hour));
		calendar.add(calendar.MINUTE, Integer.parseInt(min));
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		String dateString = formatter.format(date);
		return dateString;
	}

	public static synchronized DataConnection getDataConnection(FileInfo config, String taskName, String type) {
		// log.info("当前线程"+Thread.currentThread().getId()+" "+"获取数据源"+taskName);
		DataConnection db = null;
		if (type.equals("source")) {
			db = new DataConnection(config.getSourceDbType(), config.getSourceIp(), config.getSourcePort(),
					config.getSourceDbName(), config.getSourceUsername(), config.getSourcePassword(), taskName);
		} else {
			db = new DataConnection(config.getTargetDbType(), config.getTargetIp(), config.getTargetPort(),
					config.getTargetDbName(), config.getTargetUsername(), config.getTargetPassword(), taskName);
		}
		// log.info("当前线程"+Thread.currentThread().getId()+"
		// "+"获取数据源完成"+taskName);
		return db;
	}

}
