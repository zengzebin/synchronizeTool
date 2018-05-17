package common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssxt.dbConfig.DbInfo;
import com.ssxt.dbConfig.Impl.MysqlInfo;
import com.ssxt.dbConfig.Impl.OracleInfo;
import com.ssxt.dbConfig.Impl.SqlServerInfo;
import com.ssxt.entity.DbConfig;
 

public class DataConnection3 {
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DataConnection3.class);

	/**
	 * 数据库连接地址
	 */
	private static String url;
	/**
	 * 用户名
	 */
	private static String userName;
	/**
	 * 密码
	 */
	private static String password;

	private static String driver;

	private static String ip;
	private static String port;
	private static String dbName;
	private static String dbType;
	private static String taskName;

	/*
	 * public DataConnection(String driver, String url, String userName, String
	 * password) { this.driver = driver; this.url = url; this.userName =
	 * userName; this.password = password; }
	 */

	public DataConnection3(String dbType, String ip, String port, String dbName, String userName, String password,
			String taskName) {
		this.ip = ip;
		this.port = port;
		this.dbType = dbType;
		this.dbName = dbName;
		this.userName = userName;
		this.password = password;
		this.taskName = taskName;
	}

	/**
	 * 建立数据库连接
	 * 
	 * @return
	 * @throws SQLException
	 */ 
	public  Connection getConnection(String taskName) {
		Connection conn = null;
		Map<String, String> info = new HashMap<String, String>();
		info.put("port", port);
		info.put("ip", ip);
		info.put("dbName", dbName);
		DbInfo dbInfo = null;
		try {
			if (dbType.equals("mysql")) {
				dbInfo = new MysqlInfo();
			} else if (dbType.equals("sqlserver")) {
				dbInfo = new SqlServerInfo();
			} else if (dbType.equals("orcalre")) {
				dbInfo = new OracleInfo();
			} else {
				log.error("任务:" + taskName + " 找不到连接的数据库信息");
				return null;
			}
			DbConfig dbConfig = dbInfo.getDbConfig(info);
			Class.forName(dbConfig.getDriver());
			conn = DriverManager.getConnection(dbConfig.getUrl(), userName, password);
			log.info("任务:" + taskName + " 连接成功！" + conn);
		} catch (Exception e) {
			log.error("任务:" + taskName + " 连接失败", e);
		}
		return conn;
	}

	/**
	 * 释放连接
	 * 
	 * @param conn
	 */
	public void freeConnection(Connection conn) {
		try {
			conn.close();
			log.info("任务:" + taskName + " 数据库关闭  " + conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("任务:" + taskName + " 数据库关闭失败  ", e);
		}
	}

	public   void closeStatement(Statement s) {
		try {
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public   void closePreparedStatement(PreparedStatement p) {
		try {
			p.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public   void closeResultSet(ResultSet rs) {
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 查询
	 * @param conn
	 * @param propertyMap
	 * @param sql
	 * @param sourceFieldName
	 * @param targetFieldName
	 * @param fieldType
	 * @return
	 */
	public  List<DynamicBean> getFindSql(Connection conn, HashMap propertyMap, String sql,
			String[] sourceFieldName, String[] targetFieldName, String[] fieldType) {
		log.info("任务:" + taskName + " 查询语句  " + sql);
		List<DynamicBean> list = new ArrayList<DynamicBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int col = rs.getMetaData().getColumnCount();
			while (rs.next()) {

				/*
				 * for (int i = 1; i <= col; i++) {
				 * System.out.print(rs.getString(i) + "\t");
				 * 
				 * if ((i == 2) && (rs.getString(i).length() < 8)) {
				 * System.out.print("\t"); } }
				 */

				DynamicBean bean = new DynamicBean(propertyMap);
				for (int i = 0; i < sourceFieldName.length; i++) {
					// System.out.println(rs.getObject(sourceFieldName[i]));

					if (fieldType[i].equals("int"))
						bean.setValue(targetFieldName[i], rs.getInt(sourceFieldName[i]));
					// bean.setValue(targetFieldName[i],(Integer)(rs.getObject(sourceFieldName[i])));

					else if (fieldType[i].equals("double"))
						bean.setValue(targetFieldName[i], rs.getDouble(sourceFieldName[i]));
					// bean.setValue(targetFieldName[i],
					// Double.parseDouble(rs.getObject(sourceFieldName[i]).toString()));

					else if (fieldType[i].equals("float"))
						bean.setValue(targetFieldName[i], rs.getFloat(sourceFieldName[i]));
					// bean.setValue(targetFieldName[i],(Float)(rs.getObject(sourceFieldName[i])));

					else if (fieldType[i].equals("long"))
						bean.setValue(targetFieldName[i], rs.getLong(sourceFieldName[i]));
					// bean.setValue(targetFieldName[i],Long.valueOf(rs.getObject(sourceFieldName[i]).toString()).longValue());

					else if (fieldType[i].equals("decimal") || fieldType[i].equals("numeric"))
						bean.setValue(targetFieldName[i], rs.getBigDecimal(sourceFieldName[i]));

					else
						bean.setValue(targetFieldName[i], rs.getString(sourceFieldName[i]));
					// bean.setValue(targetFieldName[i],
					// rs.getObject(sourceFieldName[i]).toString());
					// bean.setValue("update", 0);
				}
				list.add(bean);
			}

		} catch (SQLException e) {
			log.error("任务:" + taskName + " 查询失败查询语句或者类型匹配问题 sql= "+sql, e);
		} catch (ClassCastException e) {

			log.error("任务:" + taskName + " 类型转换错误 sql="+sql, e);
		} finally {
			closePreparedStatement(pstmt);
			closeResultSet(rs);
		}
		return list;
	}

	// public static void insertDB(Connection conn, List<DynamicBean> list,
	// String sql) {
	// PreparedStatement ps = null;
	// log.info("任务:" + taskName + " 插入语句 " + sql);
	// try {
	// ps = conn.prepareStatement(sql);
	// int count = 0;
	// for (int i = 0; i < list.size(); i++) {
	// count++;
	// DynamicBean bean = list.get(i);
	// Object object = bean.getObject();
	// // 通过反射查看所有方法名
	// Class clazz = object.getClass();
	// Method[] methods = clazz.getDeclaredMethods();
	// Field[] fields = clazz.getDeclaredFields();
	//
	// for (int j = 0; j < fields.length; j++) {
	// Field field = fields[j];
	// String fieldtype = field.getGenericType().toString().replace("class",
	// "").trim();
	// String fieldName = field.getName().replace("$cglib_prop_", "").trim();
	// if (fieldtype.equals("java.lang.String"))
	// ps.setString(j + 1, bean.getValue(fieldName).toString());
	//
	// else if (fieldtype.equals("java.lang.Integer"))
	// ps.setInt(j + 1, Integer.parseInt(bean.getValue(fieldName).toString()));
	//
	// else if (fieldtype.equals("java.lang.Double"))
	// ps.setDouble(j + 1,
	// Double.parseDouble(bean.getValue(fieldName).toString()));
	//
	// else if (fieldtype.equals("java.lang.Float"))
	// ps.setFloat(j + 1, (Float) bean.getValue(fieldName));
	//
	// else if (fieldtype.equals("java.lang.Long"))
	// ps.setLong(j + 1, Long.valueOf(bean.getValue(fieldName).toString()));
	//
	// else if (fieldtype.equals("java.math.BigDecimal"))
	// ps.setBigDecimal(j + 1, new
	// BigDecimal(bean.getValue(fieldName).toString()));
	// else
	// ps.setString(j + 1, bean.getValue(fieldName).toString());
	//
	// }
	// ps.addBatch();
	// if (count == 50) {
	// ps.executeBatch();
	// count = 0;
	// }
	//
	// }
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// log.error("任务:" + taskName + " 插入失败请检查插入语句或者类型匹配问题 ", e);
	// } finally {
	// try {
	// ps.executeBatch();
	// ps.close();
	//
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	//
	// }

	/**
	 * 插入更新
	 * 
	 * @param conn
	 * @param sql
	 */
	public    void insertOrUpdateDB(Connection conn, String sql) {

		Statement stmt = null;
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			log.info("任务:" + taskName + "  插入更新语句  " + sql);
			stmt.execute(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("任务:" + taskName + " 插入更新失败sql=" + sql, e);

		} catch (Exception e) {
			log.error("任务:" + taskName + " 插入更新失败sql=" + sql, e);

		} finally {
			closeStatement(stmt);
		}

	}

	/**
	 * 查找是否存在记录
	 * 
	 * @param conn
	 * @param sql
	 * @return
	 */
	public   int IsExistDB(Connection conn, String sql) {
		int count = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			if (conn != null) {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				if (rs.next()) {
					count = rs.getInt(1);
				}
			}
		} catch (Exception e) {
			log.error("任务:" + taskName + " 查询失败  " + sql, e);
			// TODO: handle exception
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
		}
		return count;

	}

}
