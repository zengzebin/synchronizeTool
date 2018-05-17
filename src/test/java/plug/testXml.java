package plug;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import common.util.PropertyFile;

public class testXml {
	public static void getProperties() {
		Properties props = new Properties();
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		Map<String, String> fileInfo = new HashMap<String, String>();
		try {

			// props.load(PropertyFile.class.getClassLoader().getResourceAsStream("configure/FF031.properties","UTF-8"));
			props.load(new InputStreamReader(
					PropertyFile.class.getClassLoader().getResourceAsStream("configure/FF031.properties"), "UTF-8"));
			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String property = props.getProperty(key);
				while (true) {
					int index = key.indexOf('.');
					if (index == -1)
						break;
					String replace = key.substring(index, index + 2);
					String replaceOk = key.substring(index+1, index + 2).toUpperCase();
					key=key.replace(replace, replaceOk);
					
				 
					
				}

				System.out.println(key + "=" + property);
				fileInfo.put(key, property);
				map.put("FF031", fileInfo);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testXml.getProperties();
	}

}
