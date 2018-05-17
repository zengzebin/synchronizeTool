package common.util;

 

/**
 * 
 * 
 * <b>类名称：</b>LoginParam<br/>
 * <b>类描述：</b>登录的基本参数信<br/>

 * <b>创建人：</b>杨培新<br/>

 * <b>修改人：</b>杨培新<br/>

 * <b>修改时间：</b>Sep 7, 2011 9:22:20 PM<br/>
 * <b>修改备注：</b><br/>
 * @version 1.0.0<br/>
 *
 */
public class LoginParam{
	//用户过滤器是否控制登录，默认false
	public  static final  boolean USE_FILTER_LOGIN_CONTROL="true".equals(ConstParam.config.getProperty("prj.USE_FILTER_LOGIN_CONTROL"));	
	//用户登录是否校验密码，默认true
	public  static final  boolean USE_LOGIN_PWD_CHECK=!"false".equals(ConstParam.config.getProperty("prj.USE_LOGIN_PWD_CHECK"));		
	//用户登录是否校验验证码，默认false
	public  static final  boolean USE_LOGIN_RNDIMG_CHECK="true".equals(ConstParam.config.getProperty("prj.USE_LOGIN_RNDIMG_CHECK"));	
	//是否采用cookie，默认true
	public  static final  boolean USE_COOKIE=!"false".equals(ConstParam.config.getProperty("prj.USE_COOKIE"));	
	//是否采用session，默认true
	public  static final  boolean USE_SESSION=true;
	
	public  static final  String SESSION_USER_NAME="CUser";
	
	public	static final String SESSION_DEPT="CDept";
	
	public  static final  String SESSION_ROLE_NAME="CRole";
	
	public  static final String COOKIES_URL = "/";
	
	public  static final String COOKIES_USER_NAME=ConstParam.PROJECT_NAME;
	
	public  static final int   COOKIES_USER_SAVETIME=86400;// 一天(按秒算)
	
	public  static final long   LOG_OUT_AFTER_VISIT_TIME=15*60*1000;//按秒算
//	public	static final String USE_LOGIN_URL="/supervise/login.html";
	public	static final String USE_LOGIN_URL=String.format("/%s/login.html",ConstParam.config.getProperty("prj.name"));
	public	static final String SESSION_USER_PERMITSSION="userpermission";
	public	static final String SESSION_ROLE_PERMITSSION="rolepermission";
	public	static final String SESSION_USER_MODULE="usermodule";
	public	static final String SESSION_ROLE_MODULE="rolemodule";
	public	static final String SESSION_PERMITSSION="permission";
	
}