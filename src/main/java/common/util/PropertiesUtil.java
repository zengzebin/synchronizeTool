package common.util;

 

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import org.hibernate.mapping.Value;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
/**
 * 获取properties文件配置信息工具类。
 * @author zhengxican
 * 
 */
public class PropertiesUtil {
	private static final String CONFIG_PROPERTIES = "config";

	/**
	 * 根据包路径获取propertices文件生成的bundle
	 * 
	 * @param packagePath
	 * @return
	 */
	public static ResourceBundle getPropertyBundle(String packagePath) {
		if (null == packagePath || "".equals(packagePath)) {
			packagePath = CONFIG_PROPERTIES;
		}
		return ResourceBundle.getBundle(packagePath);
	}

	/**
	 * 获取默认配置的propertices文件生成的bundle
	 * 
	 * @return
	 */
	public static ResourceBundle getPropertyBundle() {
		return getPropertyBundle(null);
	}

	/**
	 * 根据包路径和key读取信息
	 * 
	 * @param packagePath
	 * @param key
	 * @return
	 */
	public static String getProperty(String packagePath, String key) {
		if (null == key)
			return null;

		ResourceBundle bundle = getPropertyBundle(packagePath);
		String value = bundle.getString(key);
		String webRootPlaceHolder = "${webapp.root}";
		if (value.startsWith(webRootPlaceHolder)) {
			URI webRootUri = null;
			try {
				webRootUri = Thread.currentThread().getContextClassLoader().getResource("./../../").toURI();
			} catch (URISyntaxException e) {
				return null;
			}
			Path path = Paths.get(webRootUri);
			value = value.substring(webRootPlaceHolder.length());
			if (value.startsWith("/")) {
				value = '.' + value;
			}
			path = path.resolve(value);
			value = path.normalize().toString();
		}
		return value;
	}

	/**
	 * 根据key读取默认文件信息
	 * 
	 * @param packagePath
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return getProperty(null, key);
	}

	public static void main(String[] args) throws URISyntaxException {

//		ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
//		String s = bundle.getString("jdbc.username");
//		System.out.println(s);
		URI uri = new URI("file:/D:/JAVA/apache-tomcat-7.0.63/webapps/inspect/");
		Path path1 = Paths.get("./../../");
		Path path = Paths.get(new URI("file:/D:/JAVA/apache-tomcat-7.0.63/webapps/inspect/"));
		path = path.resolve("../../../");
		System.out.println(path.normalize());
	}
}
