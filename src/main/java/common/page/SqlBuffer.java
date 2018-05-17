package common.page;

/**
 * <b>项目名：</b>中山大学环境软件中心-大气监测管理系统<br/>
 * <b>包名：</b>com.diyeasy.common.sql<br/>
 * <b>文件名：</b>SqlConditionGroup.java<br/>

 * <b>版本信息：</b><br/>
 * <b>日期：</b>May 18, 2010-9:57:41 AM<br/>
 * <b>Copyright (c)</b> 2010中山大学环境软件中心-版权所有<br/>
 * 
 */
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import common.util.DataUtil;

 
/**
 * 
 * <b>类名称：</b>SqlConditionGroup<br/>
 * <b>类描述：</b>Sql条件，用户构造标准化查询<br/>
 * 
 * <b>创建人：</b>杨培新<br/>
 * 
 * <b>修改人：</b>杨培新<br/>
 * 
 * <b>修改时间：</b>May 18, 2010 9:57:41 AM<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */
public class SqlBuffer implements Serializable {
	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = -8084952294833119220L;

	private List<SqlCondGroup> conList = null;

	public SqlBuffer() {
		super();
		conList = new LinkedList<SqlCondGroup>();
	}

	public static SqlBuffer begin() {
		return new SqlBuffer();
	}

	/**
	 * 添加查询条件
	 * 过滤value为空
	 * @param name
	 * @param value
	 * @param sign
	 * @param type
	 * @return SqlBuffer
	 */
	public SqlBuffer add(String name, Object value, String sign, String type) {
		if (!DataUtil.isNull(value))
			conList.add(new SqlCondGroup(name, value, sign, type));
		return this;
	}

	/**
	 * 
	 * 添加查询条件
	 * 过滤value为空
	 * @param name
	 * @param value
	 * @return SqlBuffer
	 */
	public SqlBuffer add(String name, Object value, String sign) {
		if (!DataUtil.isNull(value))
			conList.add(new SqlCondGroup(name, value, sign));
		return this;
	}

	/**
	 * 添加查询条件
	 * 过滤value为空
	 * @param name
	 * @param value
	 * @return SqlBuffer
	 */
	public SqlBuffer add(String name, Object value) {
		if (!DataUtil.isNull(value))
			conList.add(new SqlCondGroup(name, value));
		return this;
	}

	/**
	 * 
	 * 添加模糊查询条件
	 * 过滤value为空
	 * @param name
	 * @param value
	 * @return SqlBuffer
	 */
	public SqlBuffer addLike(String name, Object value) {
		if (!DataUtil.isNull(value))
			conList.add(new SqlCondGroup(name, "%" + value + "%", "like"));
		return this;
	}

	/**
	 * 
	 * 添加模糊查询条件
	 * 过滤value为空
	 * @param name
	 * @param value
	 * @return SqlBuffer
	 */
	public SqlBuffer addLike(String name, Object value, String type) {
		if (!DataUtil.isNull(value))
			conList.add(new SqlCondGroup(name, "%" + value + "%", "like", type));
		return this;
	}

	/**
	 * 添加前缀查询条件
	 * 过滤value为空
	 * @param name
	 * @param value
	 * @return SqlBuffer
	 */
	public SqlBuffer addEndWith(String name, Object value) {
		if (!DataUtil.isNull(value))
			conList.add(new SqlCondGroup(name, value + "%", "like"));
		return this;
	}

	/**
	 * 
	 * 添加前缀查询条件
	 * 过滤value为空
	 * @param name
	 * @param value
	 * @return SqlBuffer
	 */
	public SqlBuffer addEndWith(String name, Object value, String type) {
		if (!DataUtil.isNull(value))
			conList.add(new SqlCondGroup(name, value + "%", "like", type));
		return this;
	}

	/**
	 * 
	 * 添加后缀查询条件
	 * 过滤value为空
	 * @param name
	 * @param value
	 * @return SqlBuffer
	 */
	public SqlBuffer addStartWith(String name, Object value) {
		if (!DataUtil.isNull(value))
			conList.add(new SqlCondGroup(name, "%" + value, "like"));
		return this;
	}

	/**
	 * 
	 * 添加后缀查询条件
	 * 过滤value为空
	 * @param name
	 * @param value
	 * @return SqlBuffer
	 */
	public SqlBuffer addStartWith(String name, Object value, String type) {
		if (!DataUtil.isNull(value))
			conList.add(new SqlCondGroup(name, "%" + value, "like", type));
		return this;
	}

	/**
	 * 添加直接查询条件
	 * 
	 * @param name
	 * @param value
	 * @return SqlBuffer
	 */
	public SqlBuffer addText(String text, String type) {
		conList.add(new SqlCondGroup("", null, text, type));
		return this;
	}


	/**
	 * 添加加一个属性对多个搜索值条件
	 * 默认用in
	 * 去重
	 * @param name
	 * @param value
	 * @return 
	 *SqlBuffer
	 * @exception 
	 * @since  1.0.0
	 */
	public SqlBuffer addListStr(String name, String value) {
		return addListStr(name, value, "in", "and");
	}
	/**
	 * 添加加一个属性对多个搜索值条件
	 * 默认用in
	 * 去重
	 * @param name
	 * @param value
	 * @param sign
	 * @param type
	 * @return SqlBuffer
	 */
	public SqlBuffer addListStr(String name, String value, String sign, String type) {
		if (!DataUtil.isNull(value)){			
			conList.add(new SqlCondGroup(name, Arrays.asList(value.split(",")), sign, type));
		}
		return this;
	}

	/**
	 * 
	 * 添加加一个属性对多个搜索值条件
	 * 默认用in
	 * 去重
	 * -1过滤
	 * @param name
	 * @param value
	 * @return 
	 *SqlBuffer
	 * @exception 
	 * @since  1.0.0
	 */
	public SqlBuffer addListLong(String name, String value) {
		return addListLong(name, value, "in", "and");
	}
	/**
	 * 添加加一个属性对多个搜索值条件
	 * 默认用in
	 * 去重
	 * -1过滤
	 * @param name
	 * @param value
	 * @param sign
	 * @param type
	 * @return SqlBuffer
	 */
	public SqlBuffer addListLong(String name, String value, String sign, String type) {
		if (!DataUtil.isNull(value)){			
			List<String> list=Arrays.asList(value.split(","));	
			Set<Long> listn=new HashSet<Long>();
			for (String str : list) {
				long l=DataUtil.parseLong(str,-1);
				if(l!=-1)
				listn.add(l);
			}
			List<Long> listm=new ArrayList<Long>();
			listm.addAll(listn);
			conList.add(new SqlCondGroup(name, listm, sign, type));
		}
		return this;
	}
	/**
	 * 添加加一个属性对多个搜索值条件
	 * 默认用in
	 * 去重
	 * -1过滤
	 * @param name
	 * @param value
	 * @param sign
	 * @param type
	 * @return SqlBuffer
	 */
	public SqlBuffer addList(String name, List<?> list, String sign, String type) {
		conList.add(new SqlCondGroup(name, list, sign, type));
		return this;
	}
	/**
	 * 
	 * 添加加一个属性对多个搜索值条件
	 * 默认用in
	 * 去重
	 * -1过滤
	 * @param name
	 * @param value
	 * @return 
	 *SqlBuffer
	 * @exception 
	 * @since  1.0.0
	 */
	public SqlBuffer addListInteger(String name, String value) {
		return addListInteger(name, value, "in", "and");
	}
	/**
	 * 添加加一个属性对多个搜索值条件
	 * 默认用in
	 * 去重
	 * -1过滤
	 * @param name
	 * @param value
	 * @param sign
	 * @param type
	 * @return SqlBuffer
	 */
	public SqlBuffer addListInteger(String name, String value, String sign, String type) {
		if (!DataUtil.isNull(value)){			
			List<String> list=Arrays.asList(value.split(","));	
			Set<Integer> listn=new HashSet<Integer>();
			for (String str : list) {
				int l=DataUtil.parseInteger(str,-1);
				if(l!=-1)
				listn.add(l);
			}
			List<Integer> listm=new ArrayList<Integer>();
			listm.addAll(listn);
			conList.add(new SqlCondGroup(name, listm, sign, type));
		}
		return this;
	}

	/**
	 * 
	 * 添加加一个属性对多个搜索值条件
	 * 默认用in
	 * 去重
	 * -1过滤
	 * @param name
	 * @param value
	 * @return 
	 *SqlBuffer
	 * @exception 
	 * @since  1.0.0
	 */
	public SqlBuffer addListDouble(String name, String value) {
		return addListDouble(name, value, "in", "and");
	}
	/**
	 * 添加加一个属性对多个搜索值条件
	 * 默认用in
	 * 去重
	 * -1过滤
	 * @param name
	 * @param value
	 * @param sign
	 * @param type
	 * @return SqlBuffer
	 */
	public SqlBuffer addListDouble(String name, String value, String sign, String type) {
		if (!DataUtil.isNull(value)){			
			List<String> list=Arrays.asList(value.split(","));	
			Set<Double> listn=new HashSet<Double>();
			for (String str : list) {
				double l=DataUtil.parseDouble(str,-1);
				if(l!=-1)
				listn.add(l);
			}
			List<Double> listm=new ArrayList<Double>();
			listm.addAll(listn);
			conList.add(new SqlCondGroup(name, listm, sign, type));
		}
		return this;
	}
	
	/**
	 * 
	 * end(作用)<br/>
	 * 
	 * @return List<SqlCondGroup>
	 */
	public List<SqlCondGroup> end() {
		return conList;
	}
}
