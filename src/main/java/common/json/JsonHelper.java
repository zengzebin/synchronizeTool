package common.json;


 
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.collection.PersistentCollection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import common.util.DataUtil;
 

public class JsonHelper {
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JsonHelper.class);

    public static final String  dateFormat = "yyyy-MM-dd HH:mm:ss";  
//    private static SerializeConfig mapping = new SerializeConfig();   
//    static {         
//        mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
//    }  
	public static JSONObject parseObject(String text) {
		return JSON.parseObject(text);
	}
    public static <T> T parseObject(String text, Class<T> clazz) {
    	return JSON.parseObject(text,clazz);
    }
    public static <T> T parseObject(String text, TypeReference<T> type) {
    	return (T) JSON.parseObject(text,type);
    }
    
	public static String toJSONString(Object o) {
		return JSON.toJSONString(o, SerializerFeature.WriteMapNullValue);
	}

	public static String toJSONStringFilterNull(Object o) {
		return JSON.toJSONString(o);
	}
	public static String toJSONStringSort(Object o) {
		return JSON.toJSONString(o, SerializerFeature.WriteMapNullValue,SerializerFeature.SortField);
	}

	public static String toJSONString(String[] filterNames, Object obj) {
		return toJSONString(filterNames, obj, true);
	}

	public static String toJSONString(String[] filterNames, Object obj, boolean isInclude){   
		final List<String> includeList=!isInclude?null:Arrays.asList(filterNames);
		final List<String> excludeList=isInclude?null:Arrays.asList(filterNames);          
        return toJSONString(obj, includeList, excludeList, false);
	  } 

	public static String toJSONStringFormat(String[] filterNames, Object obj, boolean isInclude){   
		final List<String> includeList=!isInclude?null:Arrays.asList(filterNames);
		final List<String> excludeList=isInclude?null:Arrays.asList(filterNames);          
        return toJSONString(obj, includeList, excludeList,true);
	  } 

	public static String toJSONString(Object obj, List<String> includeList,List<String> excludeList ,boolean format){                  
		// fastjson 过滤不需要序列化的属性,防止进入死循环     	
        PropertyFilter filter = new BasePropertyFilter(includeList,excludeList);          
        SerializeWriter writer = new SerializeWriter();  
        JSONSerializer serializer = new JSONSerializer(writer);   
        serializer.getPropertyFilters().add(filter);  
        if(format){
            serializer.setDateFormat(dateFormat);
            serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
            serializer.config(SerializerFeature.WriteMapNullValue, true);            
        }        
        serializer.write(obj); 
        return writer.toString();
	  } 	
	
	public static SerializeWriter toJSON(final String[] filterNames, Object obj, boolean isIn){                  
		// fastjson 过滤不需要序列化的属性,防止进入死循环         
		final boolean isInclude = isIn;         
		PropertyFilter filter = new PropertyFilter() {              
			public boolean apply(Object source, String name, Object value) {                 
				for (int i = 0; i < filterNames.length; i++) {                      
					if (filterNames[i].equals(name)) {                                                               
						return isInclude ? true : false;                     
					}                 
				}                  //return true; // 不过滤                  
				return !isInclude ? true : false;             
				}        
			};          
			SerializeWriter sw = new SerializeWriter(); 	
			JSONSerializer serializer = new JSONSerializer(sw);
			serializer.getPropertyFilters().add(filter);
			serializer.write(obj);
			return sw;
	    } 

	public static final PropertyFilter  notCollectionFilter = new PropertyFilter(){
		public boolean apply(Object source, String name, Object value) {
			Class<?> clz=value.getClass();
			if(clz.isAssignableFrom(Collection.class)){
				return false;
			}
			return true;
		};
	};
	public static  SerializeConfig config=new SerializeConfig();
//	static{
//		config.addFilter(clazz, filter);
//	}
	
	/**
	 * 输出jsonObject,只输出一级目录，不输出集合类型跟PersistentCollection类型
	 * (适用条件):<br/>
	 * (执行流程):<br/>
	 * (使用方法):<br/>
	 * (注意事项):<br/>
	 * @param javaObject
	 * @return 
	 *JSONObject
	 * @exception 
	 * @since  1.0.0
	 */
	public static JSONObject toJSONSimple(Object javaObject) {
//		SerializeWriter sw = new SerializeWriter(); 
//		JSONSerializer jsonSerializer = new JSONSerializer(sw);
//		jsonSerializer.getPropertyFilters().add(notCollectionFilter);
//		jsonSerializer.write(javaObject);
	    Class<?> clazz = javaObject.getClass();


	    ObjectSerializer serializer = config.getObjectWriter(clazz);
	    if (serializer instanceof JavaBeanSerializer) {
	      JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer)serializer;

	      javaBeanSerializer.getPropertyFilters().add(notCollectionFilter);
	      JSONObject json = new JSONObject();
	      try {
	    	Map<String, Object> values = javaBeanSerializer.getFieldValuesMap(javaObject);
	        
	        for (Map.Entry<String, Object> entry : values.entrySet()){

	        	if(entry.getValue()==null){
	        		continue;
	        	}
				Class<?> clz=entry.getValue().getClass();
//				log.info("class:{}-{}",entry.getKey() ,clz);
				//org.hibernate.collection.PersistentSet
				if(entry.getValue() instanceof Collection||entry.getValue() instanceof PersistentCollection){

					log.info("continue:{}",clz);
					continue;
				}
		        json.put((String)entry.getKey(), JSON.toJSON(entry.getValue()));
	        }
	      }
	      catch (Exception e) {
	        throw new JSONException("toJSON error", e);
	      }
	      return json;
	    }

	    String text = toJSONString(javaObject);
	    return (JSONObject)JSON.parse(text);
	}

	/**
	 * 输出jsonObject,只输出java基本类型跟以"java"开头的包的类型，不输出集合类型
	 * (适用条件):<br/>
	 * (执行流程):<br/>
	 * (使用方法):<br/>
	 * (注意事项):<br/>
	 * @param javaObject
	 * @return 
	 *JSONObject
	 * @exception 
	 * @since  1.0.0
	 */
	public static JSONObject toJSONBasic(Object javaObject) {
//		SerializeWriter sw = new SerializeWriter(); 
//		JSONSerializer jsonSerializer = new JSONSerializer(sw);
//		jsonSerializer.getPropertyFilters().add(notCollectionFilter);
//		jsonSerializer.write(javaObject);
	    Class<?> clazz = javaObject.getClass();


	    ObjectSerializer serializer = config.getObjectWriter(clazz);
	    if (serializer instanceof JavaBeanSerializer) {
	      JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer)serializer;

//	      javaBeanSerializer.getPropertyFilters().add(notCollectionFilter);
	      JSONObject json = new JSONObject();
	      try {
	    	Map<String, Object> values = javaBeanSerializer.getFieldValuesMap(javaObject);
	        
	        for (Map.Entry<String, Object> entry : values.entrySet()){
	        	if(entry.getValue()==null){
	        		continue;
	        	}

				Class<?> clz=entry.getValue().getClass();
//				log.info("class:{}-{}",entry.getKey() ,clz);
				//org.hibernate.collection.PersistentSet
				
				if((!DataUtil.isBaseDataType(clz)&&!clz.getName().startsWith("java"))
						||entry.getValue() instanceof java.util.Collection
						){

					log.info("continue:{}",clz);
					continue;
				}
		        json.put((String)entry.getKey(), JSON.toJSON(entry.getValue()));
	        }
	      }
	      catch (Exception e) {
	        throw new JSONException("toJSON error", e);
	      }
	      return json;
	    }

	    String text = toJSONString(javaObject);
	    return (JSONObject)JSON.parse(text);
	}

	public static Object toJSON(Object javaObject, SerializeConfig config) {
		return JSON.toJSON(javaObject, config);
	}
	

	public static <T> T toJavaObject(JSON json, Class<T> clazz) {
		return JSON.toJavaObject(json, clazz);
	}

}