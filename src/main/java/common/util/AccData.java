package common.util;

/**
 * <b>项目名：</b>中山大学环境软件中心-大气监测管理系统<br/>
 * <b>包名：</b>com.diyeasy.common.bean<br/>
 * <b>文件名：</b>ExtLoadModel.java<br/>

 * <b>版本信息：</b><br/>
 * <b>日期：</b>Apr 21, 2010-1:10:41 PM<br/>
 * <b>Copyright (c)</b> 2010中山大学环境软件中心-版权所有<br/>
 * 
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import common.json.FastJsonUtils;
import common.json.JsonHelper;

/**
 * 
 * <b>类名称：</b>ExtLoadModel<br/>
 * <b>类描述：</b><br/>
 * 
 * <b>创建人：</b>杨培新<br/>
 * 
 * <b>修改人：</b>杨培新<br/>
 * 
 * <b>修改时间：</b>Apr 21, 2010 1:10:41 PM<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 * 
 */

public class AccData implements Serializable {

	public AccData() {
		super();
	}

	public AccData(boolean success, String msg, Object data) {
		super();
		this.success = success;
		this.data = data;
		this.msg = msg;
	}

	public AccData(boolean success, String msg, long totalCount, Object data, String pageStr, String otherStr,
			int num) {
		super();
		this.totalCount = totalCount;
		this.success = success;
		this.data = data;
		this.msg = msg;
		this.pageStr = pageStr;
		this.otherStr = otherStr;
		this.num = num;
	}

	private int num;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */

	private static final long serialVersionUID = 4329587389191100467L;
	/**
	 * 记录总数，适用分页
	 */
	private long totalCount;
	/**
	 * 进程是否正常，IE必须
	 */
	private boolean success;
	/**
	 * 返回数据列表,必须是String或者Coolection的实现
	 */
	private Object data;
	/**
	 * 返回的提示信息
	 */
	private String msg;

	/**
	 * 
	 */
	private String url;

	private int errcode = 0;
	/**
	 * 分页bar HTML内容，现在都是前台js分页，所以目前这个没有用
	 */
	private String pageStr = "";

	private String otherStr;

	public String getOtherStr() {
		return otherStr;
	}

	public void setOtherStr(String otherStr) {
		this.otherStr = otherStr;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	/**
	 * 
	 * setData(封装保存将要输出的数据)<br/>
	 * (这里描述这个方法适用条件 – 可选)<br/>
	 * (这里描述这个方法的执行流程 – 可选)<br/>
	 * (这里描述这个方法的使用方法 – 可选)<br/>
	 * (存入的对象必须是String或者Collection的子类 – 可选)<br/>
	 * 
	 * @param data
	 *            void
	 * @exception @since
	 *                1.0.0
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * 
	 * 把当前实例格式化为json数据<br/>
	 * 
	 * @return
	 * @throws MapperException
	 *             String
	 * @exception @since
	 *                1.0.0
	 */
	public String toJson() {

		if (this.success) {
			return toJsonSuccess();
			// return FastJsonUtils.toJSONString(this);
		} else {
			this.setData(null);
			return toJsonSuccess();
//			return toJsonFail();
		}
	}

	public String toJsonSuccess() {
		String[] exclude = null;
		String[] arr = new String[] { "errcode", "pageStr" };
		if (data != null) {
			if (data.getClass().isArray() || data instanceof java.util.Collection) {
				exclude = new String[] { "errcode" };
			} else
				exclude = arr;
		} else
			exclude = arr;
		return toJson(exclude);
	}

	public String toJsonFail() {
		String[] exclude = new String[] { "success", "data", "totalCount", "url", "otherStr" };
		if (errcode <= 0)
			errcode = Err.server_interal_error;
		this.setErrcode(errcode);
		return toJson(exclude);
	}

	/**
	 * 
	 * 把当前实例格式化为json数据<br/>
	 * 
	 * @return
	 * @throws MapperException
	 *             String
	 * @exception @since
	 *                1.0.0
	 */

	public String toJson(String[] filterNames) {
		return JsonHelper.toJSONStringFormat(filterNames, this, false);
	}

	/**
	 * totalCount
	 *
	 * @return the totalCount
	 * @since 1.0.0
	 */

	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount
	 *            the totalCount to set
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * msg
	 *
	 * @return the msg
	 * @since 1.0.0
	 */

	public String getMsg() {
		return msg;
	}

	/**
	 * 注入<br/>
	 * 
	 * @param msg
	 *            the msg to set
	 * @since 1.0.0
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * pageStr
	 *
	 * @return the pageStr
	 * @since 1.0.0
	 */

	public String getPageStr() {
		return pageStr;
	}

	/**
	 * pageStr<br/>
	 * 
	 * @param pageStr
	 *            the pageStr to set
	 * @since 1.0.0
	 */
	public void setPageStr(String pageStr) {
		this.pageStr = pageStr;
	}

	/**
	 * url
	 *
	 * @return the url
	 * @since 1.0.0
	 */

	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * errcode
	 *
	 * @return the errcode
	 * @since 1.0.0
	 */

	public int getErrcode() {
		return errcode;
	}

	/**
	 * @param errcode
	 *            the errcode to set
	 */
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

}
