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
 

public class DataConnection {
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DataConnection.class);

	/**
	 * 数据库连接地址
	 */
	private String url;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 密码
	 */
	private String password;

	private String driver;

	private String ip;
	private String port;
	private String dbName;
	private String dbType;
	private String taskName;

	/*
	 * public DataConnection(String driver, String url, String userName, String
	 * password) { this.driver = driver; this.url = url; this.userName =
	 * userName; this.password = password; }
	 */

	public DataConnection(String dbType, String ip, String port, String dbName, String userName, String password,
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
	public synchronized Connection getConnection(String taskName) {
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
			} else if (dbType.equals("oracle")) {
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
	public synchronized void freeConnection(Connection conn) {
		try {
			conn.close();
			// log.info("任务:" + taskName + " 数据库关闭 " + conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("任务:" + taskName + " 数据库关闭失败  ", e);
		}
	}

	public void closeStatement(Statement s) {
		try {
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized void closePreparedStatement(PreparedStatement p) {
		try {
			p.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized void closeResultSet(ResultSet rs) {
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 查询
	 * 
	 * @param conn
	 * @param propertyMap
	 * @param sql
	 * @param sourceFieldName
	 * @param targetFieldName
	 * @param fieldType
	 * @return
	 */
	public synchronized List<DynamicBean> getFindSql(PreparedStatement pstmt, HashMap propertyMap, String sql,
			String[] sourceFieldName, String[] targetFieldName, String[] fieldType) {

		log.info("任务:" + taskName + " 语句 " + sql);
		log.info("任务:" + taskName + " 类型 " + propertyMap.toString());
		List<DynamicBean> list = new ArrayList<DynamicBean>();
		ResultSet rs = null;
		String sourceFieldName1=null; 
		String fieldType1=null;
		String targetFieldName1=null;
		try {
			rs = pstmt.executeQuery();
			int col = rs.getMetaData().getColumnCount();

			while (rs.next()) {

				DynamicBean bean = new DynamicBean(propertyMap);

				for (int i = 0; i < sourceFieldName.length; i++) {
					// System.out.println(rs.getObject(sourceFieldName[i]));
					sourceFieldName1 = sourceFieldName[i];
					fieldType1 = fieldType[i];
					targetFieldName1 = targetFieldName[i];
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

			log.error("任务:" + taskName + " 类型转换错误 sourceFieldName:"+sourceFieldName1+" fieldType:"+fieldType1+" targetFieldName:"+targetFieldName1+" "  + e);
		} catch (Exception e) {
			log.error("任务:" + taskName + " 类型转换错误 sourceFieldName:"+sourceFieldName1+" fieldType:"+fieldType1+" targetFieldName:"+targetFieldName1+" "  + e);
		} finally {
			closePreparedStatement(pstmt);
			closeResultSet(rs);
		}

		return list;
	}

	/**
	 * 查询sql语句
	 * 
	 * @param sql
	 * @param conn
	 * @return
	 */
	public PreparedStatement findSql(String sql, Connection conn) {
		log.info("任务:" + taskName + "  开始执行查询" + sql);
		List<DynamicBean> list = new ArrayList<DynamicBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {

			log.error("任务:" + taskName + "   查询失败  " + sql);
		} catch (Exception e) {
			log.error("任务:" + taskName + "   查询失败  " + sql);
		} finally {
			return pstmt;
		}
	}

	/**
	 * 插入更新
	 * 
	 * @param conn
	 * @param sql
	 */
	public synchronized void insertOrUpdateDB(Connection conn, String sql, boolean showSql) {

		Statement stmt = null;
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (showSql)
				log.info("任务:" + taskName + "  插入或更新语句sql===" + sql);
			stmt.execute(sql);

		} catch (Exception e) {
			log.error("任务:" + taskName + " 插入或更新语句sql===" + sql, e);

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
	public synchronized int IsExistDB(Connection conn, String sql) {
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
			log.error("任务:" + taskName + " 查询是否有记录失败=== " + sql, e);
			// TODO: handle exception
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
		}
		return count;

	}

}
