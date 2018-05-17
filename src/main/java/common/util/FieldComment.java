package common.util;

/**
 * <b>项目名：</b>DMS+运维系统平台<br/>
 * <b>包名：</b>com.ssxt.rdbox.common.info<br/>
 * <b>文件名：</b>FieldComment.java<br/>

 * <b>版本信息：</b><br/>
 * <b>日期：</b>2016年9月14日-上午10:55:01<br/>
 * <b>Copyright (c)</b> 2016圣世信通-版权所有<br/>
 * 
 */
 
/**
 * <b>类名称：</b>FieldComment<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>杨培新<br/>
 * <b>修改人：</b>杨培新<br/>
 * 
 * <b>修改时间：</b>2016年9月14日 上午10:55:01<br/>
 * <b>修改备注：</b><br/>
 * @version 1.0.0<br/>
 */
public class FieldComment {

	private String name;
	private String comment;
	private Class<?> type;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
}
