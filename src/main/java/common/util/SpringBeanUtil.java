package common.util;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Column;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
 
/**
 * <b>类名称：</b>SpringBeanUtil<br/>
 * <b>类描述：</b><br/>
 * <b>创建人：</b>杨培新<br/>
 * <b>修改人：</b>杨培新<br/>
 * 
 * <b>修改时间：</b>2016年7月7日 下午4:05:32<br/>
 * <b>修改备注：</b><br/>
 * @version 1.0.0<br/>
 */
public class SpringBeanUtil implements ApplicationContextAware {

	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SpringBeanUtil.class);
	/*
	 * private ApplicationContext applicationContext;
	 * 
	 * public ApplicationContext getApplicationContext() { return
	 * applicationContext; }
	 * 
	 * public void setApplicationContext(ApplicationContext applicationContext)
	 * throws BeansException { this.applicationContext = applicationContext; }
	 */

	/*
	 * class TestClass {
	 * 
	 * static int testInt = 0 ;
	 * 
	 * public static setTestInt ( int a ) { TestClass.testInt = a ; }
	 * 
	 * public void setInt ( int a1 ) { setTestInt ( a1 ); } }
	 */

	private  ApplicationContext applicationContext;

	public  ApplicationContext getApplicationContext() {
		if(applicationContext==null){
			synchronized (this) {
				PropertyConfigurator.configure(ClassLoader.getSystemResource("cs/log4j.properties"));
				applicationContext=new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
			}
		}
		return applicationContext;
	}
	/**
	*私有的默认构造子
	*/
	private SpringBeanUtil() {
	}

	/**
	*静态工厂方法
	*/
	public static SpringBeanUtil getInstance() {
		return SpringBeanUtilInner.instance;
	}

	private static class SpringBeanUtilInner {
		//静态初始化器，由JVM来保证线程安全
		private static SpringBeanUtil instance = new SpringBeanUtil();
	}

	public  void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 获得Spring管理的Bean<br/>
	 * (适用条件):byType<br/>
	 * @param clasz
	 * @return 
	 *T
	 * @exception 
	 * @since  1.0.0
	 */
	public static <T> T getBean( Class<T> clasz) {
		return getInstance().getApplicationContext().getBean(clasz);
	}

	/**
	 * 
	 * 获得Spring管理的Bean<br/>
	 * (适用条件):byName<br/>
	 * @param id
	 * @param clasz
	 * @return 
	 *T
	 * @since  1.0.0
	 */
	public static <T> T getBean(String id, Class<T> clasz) {
		return getInstance().getApplicationContext().getBean(id, clasz);
	}

	/**
	 * 获得初始设置类
	 * @return 
	 *InitConfig
	 * @since  1.0.0
	 */
	/*public static InitConfig getInitConfig() {
		return getInstance().getApplicationContext().getBean(InitConfig.class);
	}*/


	private static String getCommentFromDef(String str){
		String bak=str;
		boolean flag=bak.contains("COMMENT");
		if(!flag)
			return null;
		String[] arr=new String[]{"COMMENT","'"};
		for (int i = 0; i < arr.length; i++) {
			String tmp=arr[i];
			if(bak.contains(tmp)){
				bak=bak.substring(bak.indexOf(tmp)+tmp.length());
			}
		}
		String tmp="'";
		if(bak.contains(tmp)){
			bak=bak.substring(bak.lastIndexOf(tmp)+tmp.length());
		}
		return bak;
	}
	//format " + %s(%s) :【%s】 ==>【%s】\"
	/**
	 * 获得变量比较的访问字符串
	 * @param src
	 * @param target
	 * @param format
	 * @return 
	 *String
	 */
	public static  String  compareFileldStr(Object src, Object target ,String format) {
		Class<Object> cls_src = (Class<Object>) src.getClass().getSuperclass();
		Field[] filesDb = cls_src.getDeclaredFields();
		Class<Object> cls_target = (Class<Object>) target.getClass().getSuperclass();
		StringBuilder sb = new StringBuilder();
		for (Field field : filesDb) {
			String field_name=field.getName();
			if("serialVersionUID".equals(field_name))
				continue;
			log.debug("field_name:{}",field_name);
			String getMethodName = "get"
					+ field.getName().substring(0, 1).toUpperCase()
					+ field.getName().substring(1);
			try {
				Method mhd_src = (Method) BeanUtils.findMethod(cls_src, getMethodName);
				Method mhd_target = (Method) BeanUtils.findMethod(cls_target, getMethodName);
				Object val_src = mhd_src.invoke(src);
				Object val_target = mhd_target.invoke(target);
				if (val_src != null && !val_src.equals(val_target)) {
						String comment=field_name;
						//自定义实现的注解
						Column meta = mhd_src.getAnnotation(javax.persistence.Column.class);  
						if(meta!= null){
							comment=getCommentFromDef(meta.columnDefinition());
						}
						if(DataUtil.isNull(comment)){
							comment=field_name;
						}
						log.debug("修改内容:{},修改后:{}",val_src,val_target);
						sb.append(String.format(format, comment ,field_name,val_src, val_target));	
				}
//			} catch (NoSuchMethodException e) {
//				log.debug("没有这个方法可显示调用",e);
			} catch (SecurityException e) {
				log.error("变量安全访问异常:",e);
			} catch (Exception e) {
				log.error("变量比较异常:",e);
			}
		}
		return sb.toString();
	}

	/**
	 * 获得变量比较的访问字符串
	 * @param src
	 * @param target
	 * @param format
	 * @return 
	 *String
	 */
	public static  String  compareFileldChange(Object src, Object target ,String format) {
		StringBuilder sb = new StringBuilder();
		JSONObject json_src=(JSONObject) JSON.toJSON(src);
		JSONObject json_target=(JSONObject) JSON.toJSON(target);
		for (Map.Entry<String, Object> entry : json_target.entrySet()) {
			String key=entry.getKey();
			Object value=entry.getValue();
			if(DataUtil.isNull(value))
				continue;
			Object value_old=json_src.get(key);
			if(value.equals(value_old))
				continue;	
			
		}
		return sb.toString();
	}
	/**
	 * 获得空属性输出
	 * @param source
	 * @return 
	 *String[]
	 * @exception 
	 * @since  1.0.0
	 */
	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}
	/**
	 * 获得忽略属性输出
	 * @param source
	 * @return 
	 *String[]
	 * @exception 
	 * @since  1.0.0
	 */
	public static String[] getIgnorePropertyNames(Object source , String[] includePropertyName) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> include = new HashSet<String>(Arrays.asList((includePropertyName)));
		Set<String> ignore = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			if(!include.contains(pd.getName())){
				ignore.add(pd.getName());
			};
		}
		String[] result = new String[ignore.size()];
		return ignore.toArray(result);
	}
	/**
	 * 复制属性，忽略空属性
	 * (适用条件):需要忽略被复制对象的空属性时<br/>
	 * (注意事项):目标对象的属性不为空，不会被空属性覆盖<br/>
	 * @param src
	 * @param target 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void copyPropertiesIgnoreNull(Object src, Object target) {
		BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	}

	/**
	 * 复制属性，忽略空属性
	 * (适用条件):需要忽略被复制对象的空属性时<br/>
	 * (注意事项):目标对象的属性不为空，不会被空属性覆盖<br/>
	 * @param src
	 * @param target 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void copyPropertiesIgnoreNull(Object src, Object target,String[] ignoreNames) {
		Set<String> ignore = new HashSet<String>(Arrays.asList(ignoreNames));
		ignore.addAll(Arrays.asList(getNullPropertyNames(src)));
		BeanUtils.copyProperties(src, target, ignore.toArray(new String[ignore.size()]));
	}
	/**
	 * 复制属性，忽略空属性
	 * (适用条件):需要忽略被复制对象的空属性时<br/>
	 * (注意事项):目标对象的属性不为空，不会被空属性覆盖<br/>
	 * @param src
	 * @param target 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void copyPropertiesIgnoreNullAndCase(Map<String,Object> src, Object target,String[] ignoreNames) {
		Set<String> ignore = new HashSet<String>(Arrays.asList(ignoreNames));
		ignore.addAll(Arrays.asList(getNullPropertyNames(src)));
		Map<String,Object> cc=new HashMap<String,Object>();
		for ( Entry<String, Object> entry : src.entrySet()) {
			String key=entry.getKey().substring(0, 1).toLowerCase()+entry.getKey().substring(1);
			cc.put(key, entry.getValue());			
		}
		BeanUtils.copyProperties(src, target, ignore.toArray(new String[ignore.size()]));
	}
	/**
	 * 复制属性
	 * (注意事项):目标对象的属性不为空，会被空属性覆盖<br/>
	 * @param src
	 * @param target 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void copyProperties(Object src, Object target) {
		BeanUtils.copyProperties(src, target);
	}
}
