package common.util;

 
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * <b>类名称：</b>ConstParam<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>杨培新<br/>
 * <b>修改人：</b>杨培新<br/>
 * 
 * <b>修改时间：</b>2016年6月12日 上午9:20:03<br/>
 * <b>修改备注：</b><br/>
 * @version 1.0.0<br/>
 */
public class ConstParam {
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ConstParam.class);

	public static final String DATE_FORMAT="yyMMddHHmmss";
	public static final String DATE_FORMAT_MIN="yyMMddHHmm";
	

	public static final String FILE_SEP=System.getProperty("file.separator");
	public static final String Line_SEP=System.getProperty("line.separator");
	public static final String WEB_LINE_SEP="<br/>";
	public static final String WEB_NEW_LINE="\n";
	public static final String SYS_NEW_LINE=Line_SEP;
	public static final int DEFAULT_PAGE_SIZE=20;

	public static final int STATUS_ENABLE=1;
	public static final String MSG_OK="OK";
	public static final String MSG_NO="NO";
	public static final Integer ID_WRONG=Integer.valueOf(-1);
	public static final Integer ID_ZERO=Integer.valueOf(0);
	
	public static Properties config = new Properties();
//	private static Properties settings = new Properties();
	/**
	 * 通用的配置
	 */
	private static final String DEFAULT_CONFIG_FILE = "config.properties";

	public static final String OrderNetActive="zaixian";
	public static final String OrderNetInActive="lixian";
	public static final String OrderUp="shangbao";
	public static final String OrderDown="zhaoce";
	
	static {
		/**
		 * 开发环境使用配置文件，相当于DEFAULT_CONFIG_FILE+DEFAULT_SETTINGS_FILE
		 */
		String devFile="dev.properties";
		log.info("################################");
		boolean devflag=ConstParam.class.getClassLoader().getResource(devFile)!=null;
		try {
			config.load(ConstParam.class.getClassLoader().getResourceAsStream("config.properties"));
			 
		} catch (IOException e) {
			log.error("加载配置文件出错!",e);
		}
	}
	public static final String PROJECT_NAME=config.getProperty("prj.name");
	

	

	/**
	 * 是否允许DateUtil抛出日期转换异常
	 */
	public static final boolean IS_ALLOW_THROW_DATE_EXCEPTION=true;
		
	
	/**
	 * 是否对下拉框站点列表进行权限细分
	 */
	public static final boolean USE_RIGHT_CHK_IN_OPT_STATION=true;
	
	public static final boolean URL_CHECK_ENABLE=false;
	
	
	
	/** 
     * 根据key得到value的值 
     */  
    public static String getProperty(String key)  
    {  
        return config.getProperty(key);  
    }  
	
	
}
