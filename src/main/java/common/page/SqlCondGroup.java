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


/**
 * 
 * <b>类名称：</b>SqlConditionGroup<br/>
 * <b>类描述：</b>Sql条件，用户构造标准化查询<br/>

 * <b>创建人：</b>杨培新<br/>

 * <b>修改人：</b>杨培新<br/>

 * <b>修改时间：</b>May 18, 2010 9:57:41 AM<br/>
 * <b>修改备注：</b><br/>
 * @version 1.0.0<br/>
 * 
 */
public class SqlCondGroup implements Serializable{
	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = -8084952294833119220L;
	public static final String DEFAULT_TYPE=")and(";
	public static final String DEFAULT_SIGN="=";
	private String name="";
	private Object value="";
	private String sign="";
	private String type="";
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return java.util.Arrays.asList(name, value, sign, type).toString();

	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((sign == null) ? 0 : sign.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SqlCondGroup other = (SqlCondGroup) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sign == null) {
			if (other.sign != null)
				return false;
		} else if (!sign.equals(other.sign))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	/**
	 * Sql参数查询条件，符合为默认符号(DEFAULT_SIGN),连接类型为默认连接(IDao.DEFAULT_TYPE)
	 *
	 * @param name  属性名
	 * @param value 值，可以是Array,List或者单值
	 */
	public SqlCondGroup(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
		this.sign = DEFAULT_SIGN;
		this.type= DEFAULT_TYPE;
	}

	/**
	 * Sql参数查询条件，连接类型为默认连接(IDao.DEFAULT_TYPE)
	 *
	 * @param name  属性名
	 * @param value 值，可以是Array,List或者单值
	 * @param sign  符号,如>,<,=,like,<>,in,isnull等等
	 */
	public SqlCondGroup(String name, Object value, String sign) {
		super();
		this.name = name;
		this.value = value;
		this.sign = sign;
		this.type= DEFAULT_TYPE;
	}

	/**
	 * Sql参数查询条件
	 *
	 * @param name  属性名
	 * @param value 值，可以是Array,List或者单值
	 * @param sign  符号,如>,<,=,like,<>,in,isnull等等
	 * @param type  连接类型,and或or，视情况可加括号
	 */
	public SqlCondGroup(String name, Object value, String sign, String type) {
		super();
		this.name = name;
		this.value = value;
		this.sign = sign;
		this.type = type;
	}

	/**
	 * name
	 *
	 * @return  the name
	 * @since   1.0.0
	 */
	
	public String getName() {
		return name;
	}

	/**
	 * value
	 *
	 * @return  the value
	 * @since   1.0.0
	 */
	
	public Object getValue() {
		return value;
	}

	/**
	 * sign
	 *
	 * @return  the sign
	 * @since   1.0.0
	 */
	
	public String getSign() {
		return sign;
	}

	/**
	 * type
	 *
	 * @return  the type
	 * @since   1.0.0
	 */
	
	public String getType() {
		return type;
	}
	
	
}
