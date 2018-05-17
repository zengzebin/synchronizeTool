package common.util;

/**
 * <b>项目名：</b>中山大学环境软件中心-大气监测管理系统<br/>
 * <b>包名：</b>com.diyeasy.common.util<br/>
 * <b>文件名：</b>DataUtil.java<br/>

 * <b>版本信息：</b><br/>
 * <b>日期：</b>2011-1-22-下午01:51:46<br/>
 * <b>Copyright (c)</b> 2011中山大学环境软件中心-版权所有<br/>
 * 
 */
 

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

 
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import common.json.JsonHelper;
import common.page.SqlCondGroup;

 




/**
 * 
 * <b>类名称：</b>DataUtil<br/>
 * <b>类描述：</b><br/>

 * <b>创建人：</b>杨培新<br/>

 * <b>修改人：</b>杨培新<br/>

 * <b>修改时间：</b>2011-1-22 下午01:51:46<br/>
 * <b>修改备注：</b><br/>
 * @version 1.0.0<br/>
 * 
 */

public class DataUtil {
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DataUtil.class);

	public static void main(String[] args) {
		Double a=1.3d,b=null;
		List<Double> dlist=new ArrayList<Double>();
		dlist.add(a);
		dlist.add(b);
		dlist.add(1.4);
		dlist.add(1.2);
		dlist.add(a);
		
		DataUtil.removeNull(dlist);
		System.out.println(dlist);
		System.out.println(getAverageDouble(dlist));
	}
	
	
	public static class ParamDefault{
		public static final String default_String="";
		public static final Byte default_Byte=-1;
		public static final Short default_Short=-1;
		public static final Integer default_Integer=-1;
		public static final BigDecimal default_BigDecimal=BigDecimal.ZERO;
		public static long default_Long=-1l;
		public static final Float default_Float=-1f;
		public static final Double default_Double=-1d;
	}

	/**
	 * 组装日志记录信息，第二个参数若不为异常，则拼接字符串;否则返回[obj[0],obj[1]];
	 * @param o
	 * @return 
	 *Object[]
	 * @exception 
	 * @since  1.0.0
	 */
	public static Object[] getLogArr(Object ... o){
		Object arr[]=null;
		if(o.length!=2||!(o[1] instanceof java.lang.Throwable)){
			StringBuilder buffer = new StringBuilder();  
			for(Object msg: o){
				 buffer.append(toString(msg)); 
			}
			arr=new Object[]{buffer.toString()};
		}
		else{
			arr=new Object[]{o[0], (Throwable) o[1]};
		}
		return arr;		
	}
	
	

	/**
	 * 获取根异常
	 * @param e
	 * @return
	 */
	public static String getRootCauseMsg(Throwable e){
		return getCauseMsg(getRootCause(e));
	}

	/**
	 * 获取根异常
	 * @param e
	 * @return
	 */
	public static Throwable getRootCause(Throwable e){
		Throwable r = null,t=e;
		while(t!=null){
			r=t;
			t=t.getCause();
		}
		return r;
	}

	/**
	 * 获取根异常
	 * @param e
	 * @return
	 */
	public static String getCauseMsg(Throwable e){
		String msg=e.getMessage();
		String errStart="Exception:";
		if(msg!=null&&msg.indexOf(errStart)!=-1){
			msg=msg.substring(msg.indexOf(errStart)+errStart.length());
		}
		System.out.println("DataUtil.getCauseMsg() " + msg);
		return msg;
	}


	/**
	 * 获取根异常
	 * @param e
	 * @return
	 */
	public static String error(Throwable e){
		return getCauseMsg(getRootCause(e));
	}
	/**
	 * 清除所有空值
	 * @param list
	 * @return
	 */
	public static List<?> removeNull(List<?> list){
		list.removeAll(Collections.singleton(null));
		return list;
	}
	/**
	 * 获取平均值
	 * @param list
	 * @return
	 */
    public static double getAverageDouble(Collection<Double> list) {
    	return getSumDouble(list)  / list.size();
    }

    //获取平均值
    public static int getAverageInt(Collection<Integer> list) {
    	return getSumInt(list) / list.size();
    }

    //获取平均值
    public static long getAverageLong(Collection<Long> list) {
        return getSumLong(list) / list.size();
    }

	/**
	 * 获取汇总值
	 * @param list
	 * @return
	 */
    public static double getSumDouble(Collection<Double> list) {
        double sum = 0;
        for (double bean : list) {sum += bean;}
        return sum;
    }

    //获取汇总值
    public static int getSumInt(Collection<Integer> list) {
    	int sum = 0;
        for (Integer bean : list) {
        	sum += bean.intValue();
		}
        return sum ;
    }

    //获取汇总值
    public static long getSumLong(Collection<Long> list) {
    	long sum = 0;
        for (Long bean : list) {
        	sum += bean.longValue();
		}
        return sum ;
    }

    //获取最大值
    public static <T extends Object & Comparable<? super T>> T getMax(Collection<? extends T> coll) {
    	return Collections.max(coll);
    }
    //获取最大值
    public static <T extends Object & Comparable<? super T>> T getMin(Collection<? extends T> coll) {
    	return Collections.min(coll);
    }
    

    //获取包含特定字符的位置
    public static int indexOfContain(Collection<String> list,String str) {
    	int idx=-1,n=0;    	
    	if(!DataUtil.isNull(str))
    	for (String bean : list) {
			if(str.contains(bean)){
				idx=n;break;
			}
    		n++;
		}
        return idx ;
    }

    //获取包含特定字符的位置
    public static int indexOfStart(Collection<String> list,String str) {
    	int idx=-1,n=0;    	
    	if(!DataUtil.isNull(str))
    	for (String bean : list) {
			if(str.startsWith(bean)){
				idx=n;break;
			}
    		n++;
		}
        return idx ;
    }
    
    /** 
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0 
     * @param version1 
     * @param version2 
     * @return 
     */  
    public static int compareVersion(String version1, String version2)  {  
        if (version1 == null || version2 == null) {  
            throw new RuntimeException("compareVersion error:illegal params.");  
        }  
        String[] va1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；  
        String[] va2 = version2.split("\\.");  
        int idx = 0;  
        int minLength = Math.min(va1.length, va2.length);//取最小长度值  
        int diff = 0;  
        while (idx < minLength  
                && (diff = va1[idx].length() - va2[idx].length()) == 0//先比较长度  
                && (diff = va1[idx].compareTo(va2[idx])) == 0) {//再比较字符  
            ++idx;  
        }  
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；  
        diff = (diff != 0) ? diff : va1.length - va2.length;  
        return diff;  
    }  
	/**
	 * 转换成字符
	 * @param obj
	 * @return 
	 *String
	 */
	public static String toString(Object obj){
		return obj==null?"null":obj.toString();
		
	}
	/**
	 * 
	 * 转换成字符
	 * @param obj
	 * @return 
	 *String
	 */
	public static String toString(Object[] obj){
		return obj==null?"null":java.util.Arrays.toString(obj);		
	}

	/**
	 * 
	 * @param obj
	 * @return 
	 *T[]
	 * @since  1.0.0
	 */
	public static  <T> T[] toArray(T ... obj){
		return obj;
	}

	/**
	 * 
	 * @param obj
	 * @return 
	 *T[]
	 * @since  1.0.0
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static  <T> T[] toArray(Class clz,List<T> obj){		
		if(obj==null)
			return null;
		return obj.toArray((T[])Array.newInstance(clz,obj.size()));
	}

	/**
	 * 获得之前的字符串
	 * @param src  源字符串
	 * @param flag 间隔符
	 * @param lastFlag 最后的间隔符(即从尾部开始找)
	 * @return 
	 *String
	 */
	public static String getStringBefore(String src,String flag){
		return getStringBefore(src, flag, false);
	}
	/**
	 * 获得之前的字符串
	 * @param src  源字符串
	 * @param flag 间隔符
	 * @param lastFlag 最后的间隔符(即从尾部开始找)
	 * @return 
	 *String
	 */
	public static String getStringBefore(String src,String flag,boolean  lastFlag){
		int start=lastFlag?src.lastIndexOf(flag):src.indexOf(flag);	
		if(start!=-1){
			src=src.substring(0,start);
		}
		return src;
	}

	/**
	 * 获得之后的字符串
	 * @param src  源字符串
	 * @param flag 间隔符
	 * @return 
	 *String
	 */
	public static String getStringAfter(String src,String flag){
		return getStringAfter(src, flag,false);
	}
	/**
	 * 获得之后的字符串
	 * @param src  源字符串
	 * @param flag 间隔符
	 * @param lastFlag 最后的间隔符(即从尾部开始找)
	 * @return 
	 *String
	 */
	public static String getStringAfter(String src,String flag,boolean  lastFlag){
		int start=lastFlag?src.lastIndexOf(flag):src.indexOf(flag);	
		if(start!=-1){
			src=src.substring(0,start);
		}
		return src;
	}
   
	/**
	 * 转换整形值<br/>
	 * @param s				要转换的值
	 * @param default_value 转换失败的默认值
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public static int parseInt(String s,int default_value){
		if(isNull(s))return default_value;
		try{
			return Integer.parseInt(s);
		}catch (Exception e) {
			log.debug("类型转换失败!", e);
			return default_value;
		}
	}
	/**
	 * 转换整形值<br/>
	 * @param s				要转换的值
	 * @param default_value 转换失败的默认值
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public static int parseInteger(String s,int default_value){
		return parseInt(s, default_value);
	}
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static int parseInteger(String s){
		return parseInteger(s, ParamDefault.default_Integer);
	}

	/**
	 * 转换整形值<br/>
	 * @param s				要转换的值
	 * @param default_value 转换失败的默认值
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public static Byte parseByte(String s,Byte default_value){
		if(isNull(s))return default_value;
		try{
			return Byte.valueOf(s);
		}catch (Exception e) {
			log.debug("类型转换失败!", e);
			return default_value;
		}
	}

	public static int parseByte(String s){
		return parseByte(s, ParamDefault.default_Byte);
	}

	/**
	 * 转换整形值<br/>
	 * @param s				要转换的值
	 * @param default_value 转换失败的默认值
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public static Short parseShort(String s,Short default_value){
		if(isNull(s))return default_value;
		try{
			return Short.valueOf(s);
		}catch (Exception e) {
			log.debug("类型转换失败!", e);
			return default_value;
		}
	}

	public static Short parseShort(String s){
		return parseShort(s, ParamDefault.default_Short);
	}
	/**
	 * 
	 * 转换长整形值<br/>
	 * @param s				要转换的值
	 * @param default_value 转换失败的默认值
	 * @return 
	 *long
	 * @exception 
	 * @since  1.0.0
	 */
	public static long parseLong(String s,long default_value){
		if(isNull(s))return default_value;
		try{
			return Long.parseLong(s);
		}catch (Exception e) {
			log.debug("类型转换失败!", e);
			return default_value;
		}
	}

	public static long parseLong(String s){

		return parseLong(s, ParamDefault.default_Long);
	}
	/**
	 * 
	 * 转换双精度浮点数Double值<br/>
	 * @param s				要转换的值
	 * @param default_value 转换失败的默认值
	 * @return 
	 *double
	 * @exception 
	 * @since  1.0.0
	 */
	public static double parseDouble(String s,double default_value){
		if(isNull(s))return default_value;
		try{
			return Double.parseDouble(s);
		}catch (Exception e) {
			log.debug("类型转换失败!", e);
			return default_value;
		}
	}

	public static Double parseDouble(String s){

		return parseDouble(s, ParamDefault.default_Double);
	}
	
	/**
	 * 
	 * 转换单精度浮点数Float值<br/>
	 * @param s				要转换的值
	 * @param default_value 转换失败的默认值
	 * @return 
	 *float
	 * @exception 
	 * @since  1.0.0
	 */
	public static float parseFloat(String s,float default_value){
		if(isNull(s))return default_value;
		try{
			return Float.parseFloat(s);
		}catch (Exception e) {
			log.debug("类型转换失败!", e);
			return default_value;
		}
	}

	public static float parseFloat(String s){

		return parseFloat(s, ParamDefault.default_Float);
	}
	/**
	 * 
	 * 转换双精度浮点数Double值<br/>
	 * @param s				要转换的值
	 * @param default_value 转换失败的默认值
	 * @return 
	 *double
	 * @exception 
	 * @since  1.0.0
	 */
	public static BigDecimal parseBigDecimal(String s,BigDecimal default_value){
		if(isNull(s))return default_value;
		try{
			return new BigDecimal(s);
		}catch (Exception e) {
			log.debug("类型转换失败!", e);
			return default_value;
		}
	}

	public static BigDecimal parseBigDecimal(String s){

		return parseBigDecimal(s, ParamDefault.default_BigDecimal);
	}
	/*
	 public static String valueOf(int i){
		return String.valueOf(i);
	}
	
	public static String valueOf(long i){
		return String.valueOf(i);
	}*/
	
	/**
	 * 返回一个非空字符，以下几个方法雷同<br/>
	 * @param str
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	public static String notNullString(String str){
		if(str==null)str="";
//		return str.trim();
		return str;
		
	}
	public static String notNullString(Object str){
//		if(str==null)str="";
////		return str.toString();
//		return str.toString().trim();
		return notNullString(str,"");
		
	}
	

	@SuppressWarnings("rawtypes")
	public static String notNullString(Object str,String defValue){
		if (str instanceof Collection) {
			return isNull((Collection) str)?defValue:str.toString();
		}
		if (str instanceof String) {
			return isNull((String) str)?defValue:str.toString();
		}
		return str==null||"".equals(str)?defValue:str.toString();
	}

	/**
	 * 
	 * @param o
	 * @param defValue
	 * @return
	 */
	public static Integer notNullInt(Integer o,Integer defValue){
		return isNull(o)||0==o?defValue:o;
		
	}


	/**
	 * 
	 * @param o
	 * @param defValue
	 * @return
	 */
	public static Integer notNullInt(Object o,Integer defValue){
		return isNull(o)?defValue:parseInt(o.toString(),defValue);
		
	}
	/**
	 * 
	 * @param o
	 * @param defValue
	 * @return
	 */
	public static Long notNullLong(Long o,Long defValue){
		return isNull(o)||0l==o?defValue:o;
		
	}

	/**
	 * 
	 * @param o
	 * @param defValue
	 * @return
	 */
	public static Long notNullLong(Object o,Long defValue){
		return isNull(o)?defValue:parseLong(o.toString(),defValue);
		
	}

	/**
	 * 
	 * @param o
	 * @param defValue
	 * @return
	 */
	public static Boolean notNullBoolean(Boolean o,Boolean defValue){
		return isNull(o)?defValue:o;
		
	}
	/**
	 * 判断是否为空，list无元素也返回空判断，以下几个方法雷同
	 * <br/>
	 * @param list
	 * @return 
	 *boolean
	 * @exception 
	 * @since  1.0.0
	 */
	public static boolean isNull(Collection<?> list){
		return list==null||list.isEmpty();
	}
	public static boolean isNotNull(List<?> list){
		return !isNull(list);
	}
	public static boolean isNull(String str){
		return str==null||"".equals(str.trim())||str.toUpperCase().equals("NULL");
	}

	public static String addCallback(String json,String callback){
		if(!DataUtil.isNull(callback)){
			json=String.format("%s(%s)",callback, json);
		}
		return json;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isNull(Object str){
		if (str instanceof Collection) {
			return isNull((Collection) str);
		}
		if (str instanceof String) {
			return isNull((String) str);
		}
		return str==null||"".equals(str);
	}
	/**
	 * 
	 * 编码字符
	 * @param s
	 * @param charset
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	public static String decode(String s,String charset){
		if(s==null||"".equals(s))return "";
		String str="";
		
		try {
			str=java.net.URLDecoder.decode(s,charset);
		} catch (java.io.UnsupportedEncodingException e) {
			log.error("使用"+charset+"解码字符串"+s+"出错!",e);
		}
		return str;
	}
	/**
	 * 解码字符
	 * @param s
	 * @param charset
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	public static String encode(String s,String charset){
		if(s==null||"".equals(s))return "";
		String str="";
		try {
			str=java.net.URLEncoder.encode(s,charset);
		} catch (java.io.UnsupportedEncodingException e) {
			log.error("使用"+charset+"编码字符串"+s+"出错!",e);
		}
		return str;
	}
	public static void printDoc(org.dom4j.Document doc,OutputStream os) throws java.io.IOException {
		java.io.Writer out = new java.io.PrintWriter(os);
    	org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();
    	org.dom4j.io.XMLWriter writer = new org.dom4j.io.XMLWriter(out, format);
	    writer.write(doc);
	    out.flush();
	 }
	public static org.dom4j.Document loadXML(String xmlfile)
	    throws java.io.FileNotFoundException, org.dom4j.DocumentException{
		log.debug("载入文件:"+xmlfile);
		org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
		org.dom4j.Document doc = reader.read(DataUtil.class.getResourceAsStream(xmlfile));
	    return doc;
	}
	/**
	 * 
	 * 将double值格式化
	 * @param date
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	public static String formatDouble(Double dou) {
		return String.format("%.2f", dou);
	}
	/**
	 * 将java.util.Date的日期转化为format格式的字符串
	 * 
	 * @param datestr
	 * @param pattern datestr的日期格式
	 * @return java.util.Date
	 */
	public static String formatDate(Date date, String format) {
		return DateConverter.formatDate(date,format);
	}
	
	/**
	 * 
	 * 将java.util.Date的日期转化为format格式的字符串
	 * @param date
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}
	/**
	 * 
	 * 将java.util.Date的日期转化为format格式的字符串
	 * @param date
	 * @param format
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	public static String formatTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 
	 * parseDate(这里用一句话描述这个方法的作用)<br/>
	 * @param date
	 * @param format
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date parseDate(String date, String format) {
		return DateConverter.parseDate(date,format);
	}
	/**
	 * 
	 * parseDate(这里用一句话描述这个方法的作用)<br/>
	 * @param date
	 * @param format
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date parseDate(String date) {
		return DateConverter.parseDate(date);
	}

	public static int charAtInt(String str,int pos){
		int i=-1;
		i= str.charAt(pos)-'0';
		return i;
	}
//	/**
//	 * 
//	 * 获取变更
//	 * @param field
//	 * @param newLine
//	 * @param from
//	 * @param to
//	 * @return 
//	 *String
//	 * @exception 
//	 * @since  1.0.0
//	 */
//	public static String getChangeStr(String field,String newLine,String from,String to) {
//		return "将" + field + "从【" + from + "】 修改为 【"+ to + "】" + newLine;
//	}
	/**
	 * 
	 * 获取变更，如果无变更返回""
	 * @param field
	 * @param newLine
	 * @param from
	 * @param to
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	public static String judgeChangeStr(Object field,Object newLine,Object from,Object to) {
		String str="";
		try {
			//to为空不处理
			if(to==null){
				System.out.println("# "+field +":"+ to);
				return "";
			}
			//ypx ext
//			//处理to为空
//			if(null==to)to="";
			//处理from为空
			if(from==null)from="";
			boolean changeFlag=false;
			String fromStr="",toStr="";

			//根据to的类型来处理
			
			//处理浮点数
			if (to instanceof Double||to instanceof Float){
				if(0==parseDouble(to.toString(), 0d))to=0d;
				if(0==parseDouble(from.toString(), 0d))from=0d;
				toStr=String.format("%.4f", to);
				fromStr=String.format("%.4f", from);
			}
			//处理时间
			else if (to instanceof Date){
				toStr=formatTime((Date)to);
				if (from instanceof Date)				
					fromStr=formatTime((Date)from);
			}
			//处理其他类型
			else{
				toStr=to.toString();
				fromStr=from.toString();
			}
			
			if(!toStr.equals(fromStr))changeFlag=true;
			if(changeFlag)
				str= "+" + field + "从【" + fromStr + "】 修改为 【"+ toStr + "】" + newLine;
//			System.out.println((changeFlag?"+":"-")+" "+ field+" "+fromStr +":"+ toStr);
		} catch (Exception e) {
			String message="判断变量是否更改异常";
			log.error(message, e);
			throw new RuntimeException(message);
		}
		return str;
	}

	/**
	 * 
	 * @param <T>
	 * @param schoolId
	 * @param userId
	 * @param userName
	 * @param reason
	 * @return String
	 */
	public static <T> String getAddHistory(String schoolId, Integer userId, String userName, String reason, T domain) {
		String newLine = "11";
		Date date = new Date();
		String history = formatDate(date) +" == TenantID: "+ schoolId +" 用户:[" + userName + "] (id:" + userId + ") 因为 " 
				+ reason + ", 新增记录" + newLine+"内容:"+JsonHelper.toJSONString(domain);	
		return history;
	}

	/**
	 * 
	 * 
	 * @param <T>
	 * @param schoolId
	 * @param userId
	 * @param userName
	 * @param reason
	 * @return String
	 */
	public static <T> String getUpdateHistory(String schoolId, Integer userId, String userName, String reason, T oldBean, T newBean) {
		 
		return null;
	}
	

	/**
	 * 
	 * 
	 * @param <T>
	 * @param schoolId
	 * @param userId
	 * @param userName
	 * @param reason
	 * @return String
	 */
	public static <T> String getUpdateHistoryByHbm(String schoolId, Integer userId, String userName, String reason, T oldBean, T newBean) {
		 
		return null;
	}
	/**
	 * 
	 * isChange
	 * 判断是否变更，以下几个方法雷同，只是参数类型不一样
	 * @param from
	 * @param to
	 * @return 
	 *boolean
	 * @exception 
	 * @since  1.0.0
	 */
	public boolean isChange(String from,String to) {
		return !(to==null)&&!to.equals(from);
	}
	public boolean isChange(Integer from,Integer to) {
		return !isNull(to)&&!to.equals(from);
	}
	public boolean isChange(Long from,Long to) {
		return !isNull(to)&&!to.equals(from);
	}
	public boolean isChange(Short from,Short to) {
		return !isNull(to)&&!to.equals(from);
	}
	public boolean isChange(Date from,Date to) {
		String from2=formatTime(from);
		String to2=formatTime(to);
		return !isNull(to2)&&!to2.equals(from2);
	}
	public boolean isChange(Object from,Object to) {
		if (from instanceof String&&to instanceof String){
			return isChange((String)from,(String)to);
		}
		if (from instanceof Integer&&to instanceof Integer){
			return isChange((Integer)from,(Integer)to);
		}
		if (from instanceof Long&&to instanceof Long){
			return isChange((Long)from,(Long)to);
		}
		if (from instanceof Short&&to instanceof Short){
			return isChange((Short)from,(Short)to);
		}
		return !isNull(to)&&!to.equals(from);
	}

	/**
	 * 抛出一个Runtime子类异常，用于用户自定义的错误控制
	 * @param msg 异常信息
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void showMsgException(String msg){
		throw new RuntimeException(msg);
	}
	/**
	 * map转化为list
	 * @param rmap
	 * @return 
	 *List<?>
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings({ "unchecked" })
	public static List<?> getValueList(Map<?,?> rmap){
		@SuppressWarnings("rawtypes")
		List rlist=new LinkedList();
		for (Iterator<?> iterator = rmap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<?,?> entry =  (Entry<?, ?>) iterator.next();
			rlist.add(entry.getValue());
			
		}
		return rlist;
	}
	/**
	 * 添加空格直至长度达到32位
	 * @param str
	 * @param len
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	public static String appendSpace(String str){
		return appendSpace(str, 32);
	}
	/**
	 * 添加空格直至长度达标
	 * @param str
	 * @param len
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	public static String appendSpace(String str,int len){
		StringBuilder sb = new StringBuilder(str);
		for (int i = str.length(); i < len; i++) {
			sb.append(' ');			
		}
		return sb.toString();
	}
	/**
	 * 响应 ajex提交的请求，返回字符串
	 */
	public static boolean responseResult(HttpServletResponse response,String result,String charset) {
		boolean b=false;
		try {
			/*
			  有时候会浏览器报错[Break on this error] (<pre>...</pre>)
			  加上这行就不会
			 */
			//1.设定返回类型
			response.setContentType("text/html;charset="+charset);
			//2.取参数
			response.setCharacterEncoding(charset);

			//3.返回页面
			response.getOutputStream().write(result.getBytes(charset));
			b=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	/**
	 * 转换大小写
	 * @param list
	 * @return 
	 *List<String>
	 * @exception 
	 * @since  1.0.0
	 */
	public static List<String> convert2Lower(List<String> list){
		List<String> nlist=new LinkedList<String>();
		if(isNotNull(list)){
			for (String str : list) {
				nlist.add(str==null?str:str.toLowerCase());
			}
		}
		return list;
		
	}
	/**
	 * 转换大小写
	 * @param list
	 * @return 
	 *List<String>
	 * @exception 
	 * @since  1.0.0
	 */
	public static List<String> convert2Upper(List<String> list){
		List<String> nlist=new LinkedList<String>();
		if(isNotNull(list)){
			for (String str : list) {
				nlist.add(str==null?str:str.toUpperCase());
			}
		}
		return list;
		
	}
	

	/**
	 * 转化成下拉列表书所需的map
	 * convert2KVOptionList(作用)<br/>
	 * (适用条件):<br/>
	 * (执行流程):<br/>
	 * (使用方法):<br/>
	 * (注意事项):<br/>
	 * @param map
	 * @return 
	 *List<Map<String,String>>
	 * @exception 
	 * @since  1.0.0
	 */
	public static List<Map<String,String>> convert2KVOptionList(Map<?,?> map){
		List<Map<String,String>> list=new LinkedList<Map<String,String>>();
		for (Entry<?, ?> entry : map.entrySet()) {
			Map<String,String> dMap=new HashMap<String,String>();
			dMap.put("trueValue", entry.getKey().toString());
			dMap.put("showValue", entry.getValue().toString());
			list.add(dMap);
			
		}
		return list;
		
	}
	

	/**
	 * 判断是否相等
	 * 过滤空指针异常
	 * @param obj1
	 * @param obj2
	 * @return 
	 *boolean
	 */
	public static boolean eq(Object obj1,Object obj2){
		boolean flag=false;
		if(obj1==null ){
			if(obj2==null){flag=true;}				
		}
		else {
			flag= obj1.equals(obj2);
		}
		return flag;
		
	}
	/**
	 * 判断是否相等
	 * 过滤空指针异常
	 * 全为字符串时不区分大小写
	 * @param obj1
	 * @param obj2
	 * @return 
	 *boolean
	 */
	public static boolean eq(String obj1,Object obj2){
		boolean flag=false;
		if(obj1==null ){
			if(obj2==null){flag=true;}				
		}
		else {
			if (obj2 instanceof String) {
				flag= obj1.equalsIgnoreCase((String) obj2);
				
			}
			else flag= obj1.equals(obj2);
		}
		return flag;		
	}
	/**
	 * 判断是否相等
	 * 过滤空指针异常
	 * 全为字符串时不区分大小写
	 * @param obj1
	 * @param obj2
	 * @return 
	 *boolean
	 */
	public static boolean eq(String obj1,String obj2){
		boolean flag=false;
		if(obj1==null ){
			if(obj2==null){flag=true;}				
		}
		else {
			flag= obj1.equalsIgnoreCase( obj2);
		}
		return flag;		
	}

    public final static String getCallerName(){
    	return getCallerName(1);
    }

    /**
     * 获取调用者名称
     * @param parentNum 追溯往上级别数
     * @return 
     *String
     * @since  1.0.0
     */
    public static String getCallerName(int parentNum){
    	StackTraceElement st=Thread.currentThread().getStackTrace()[3+parentNum];
    	String method=st.getMethodName();
    	int line=st.getLineNumber();
    	String className=st.getClassName();
    	int s=className.lastIndexOf(".");
    	if(s>0)
    	className=className.substring(s+1);
    	return String.format("%s-%s:%s", className,method,line);
    }
    

    /**
     * 获取泛型类型
     * getGenricType(作用)<br/>
     * (适用条件):<br/>
     * (执行流程):<br/>
     * (使用方法):<br/>
     * (注意事项):<br/>
     * @param clazz
     * @param index
     * @return 
     *Class<Object>
     * @exception 
     * @since  1.0.0
     */
	public static Class<Object> getGenricType(final Class clazz, final int index) {  
        Type genType = clazz.getGenericSuperclass();  
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
        return (Class) params[index];  
    }

    /**
     * 
     * @param name
     * @param value
     * @param sign
     * @param type
     * @return
     */
    public static SqlCondGroup SqlCon(String name, Object value, String sign, String type){
    	return new SqlCondGroup(name, value, sign, type);
    } 
    /**
     * 
     * @param name
     * @param value
     * @param sign
     * @param type
     * @return
     */
    public static SqlCondGroup SqlCon(String name, Object value){
    	return new SqlCondGroup(name, value);
    } 
    /**  
     * 判断一个类是否为基本数据类型。  
     * @param clazz 要判断的类。  
     * @return true 表示为基本数据类型。  
     */ 
    public static boolean isBaseDataType(Class clazz)  throws Exception 
    {   
        return 
        (   
            clazz.equals(String.class) ||   
            clazz.equals(Integer.class)||   
            clazz.equals(Byte.class) ||   
            clazz.equals(Long.class) ||   
            clazz.equals(Double.class) ||   
            clazz.equals(Float.class) ||   
            clazz.equals(Character.class) ||   
            clazz.equals(Short.class) ||   
            clazz.equals(BigDecimal.class) ||   
            clazz.equals(BigInteger.class) ||   
            clazz.equals(Boolean.class) ||   
            clazz.equals(Date.class) ||   
            clazz.equals(java.sql.Timestamp.class) ||
            clazz.equals(java.sql.Date.class) ||
//            clazz.equals(DateTime.class) ||
            clazz.isPrimitive()   
        );   
    }

	/**
	 * JSONP解析，返回JSON字符串
	 * @param jsonp
	 * @param callback
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	*/
	public static String cutJsonp(String jsonp, String callback) {
		String str=jsonp;
		if(isNull(callback)||jsonp.contains(callback)){
			str=jsonp.substring(jsonp.indexOf('(')+1 , jsonp.lastIndexOf(')'));
		}
		return str;
	}
}



class AsciiComparator<T> implements Comparator<T>{
	public int compare(Object o1, Object o2) {
		if(o1==o2){
			return 0;
		}else if(o1==null){
			return -1;
		}else{
			if(o1.toString().length()>o2.toString().length()){
				return 1;
			}else if(o1.toString().length()<o2.toString().length()){
				return -1;
			}else {
				return o1.toString().compareTo(o2.toString());
			}
		}
	}
	
	public static void main(String[] args){
		HashMap<Integer, String> hm = new HashMap<Integer, String>();
		for(int i=10;i>0;i--){
			hm.put(i, "xx"+i);
			if(i==5){
				hm.put(i, "中文"+i);
			}
		}
		
		Vector<String> vv = new Vector<String>();
		
		Iterator<String> it = hm.values().iterator();
		while(it.hasNext()){
			vv.add(it.next());
		}
		
		AsciiComparator<String> c = new AsciiComparator<String>();
	    Collections.sort(vv, c);
		
		for(int i=0;i<vv.size();i++){
			System.out.println(vv.get(i));
		}
		
	}
	

    
    public static String getErrorInfo(Throwable e) {  
        try {  
            StringWriter sw = new StringWriter();  
            PrintWriter pw = new PrintWriter(sw);  
            e.printStackTrace(pw);  
            return "\r\n" + sw.toString() + "\r\n";  
        } catch (Exception t) {  
            return "bad getErrorInfo";  
        }  
    }  
}

