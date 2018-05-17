package common.util;

 

import java.util.HashMap;
import java.util.Map;

/**
 * <b>类名称：</b>Err<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>杨培新<br/>
 * <b>修改人：</b>杨培新<br/>
 * 
 * <b>修改时间：</b>2016年7月7日 上午10:33:51<br/>
 * <b>修改备注：</b><br/>
 * @version 1.0.0<br/>
 */
public class Err{
//	90到99为，最典型的异常
//	普通异常，不要放到90到99
	public static final int not_login=50;	//未登陆
	public static final int not_powered=51;	//没有权限
	public static final int param_not_match=51;	//参数不足
	/**
	 * 服务器内部异常
	 */
	public static final int server_interal_error=98;	//服务器内部异常
	public static final int AAA=97;	//AAA异常
	public static Map<Integer, String> codeMap = new HashMap<Integer, String>(){{
		put(not_login,"未登录");
		put(not_powered,"没有权限");
		put(param_not_match,"参数不足");
		put(server_interal_error,"服务器内部异常");
		put(AAA,"AAA异常");
	}};

}