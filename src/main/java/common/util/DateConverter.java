package common.util;

/**
 * <b>项目名：</b>中山大学环境软件中心-大气监测管理系统<br/>
 * <b>包名：</b>com.diyeasy.common.util<br/>
 * <b>文件名：</b>DateUtil.java<br/>

 * <b>版本信息：</b><br/>
 * <b>日期：</b>May 17, 2010-12:01:45 PM<br/>
 * <b>Copyright (c)</b> 2010中山大学环境软件中心-版权所有<br/>
 * 
 */
 
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

 

/**
 * 
 * <b>类名称：</b>DateUtil<br/>
 * <b>类描述：</b><br/>

 * <b>创建人：</b>杨培新<br/>

 * <b>修改人：</b>杨培新<br/>

 * <b>修改时间：</b>May 17, 2010 12:01:45 PM<br/>
 * <b>修改备注：</b><br/>
 * @version 3.0.0<br/>
 * 
 */
public class DateConverter implements Serializable{
	
	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 7340440534819814423L;
	public static final String DATE_FORMAT_DEFAULT="yyyy-MM-dd";
	public static final String DATE_FORMAT_TIME="yyyy-MM-dd HH:mm:ss";
	public static final boolean IS_ALLOW_THROW_DATE_EXCEPTION=false;
	
	private static final Map<String,SimpleDateFormat> date_map=new ConcurrentHashMap<String,SimpleDateFormat>();
	public static String MSG_PARSE_DATE_ERR="字符转换为时间错误";
	public static String MSG_FROMAT_DATE_ERR="时间格式化为字符错误";
	/**
	 * 私有构造函数
	 */
	private DateConverter() {
	}

	public Calendar getCalendar(){
		return Calendar.getInstance();
	}

	public Calendar getCalendar(long time){
		Calendar cal= Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal;
	}
	/**
	 * Date->Calendar
	 * @param date
	 * @return 
	 *Date
	 * @exception s
	 * @since  1.0.0
	 */
	public static Calendar getCalendar(Date date){
		Calendar g=Calendar.getInstance();
		g.setTime(date);
		return g;
	}
	/**
	 * Calendar->Date
	 * @param calendar
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getDate(Calendar calendar){
		return calendar.getTime();
	}



	/**
	 * 返回已添加指定时间间隔的日期
	 * 
	 * @param interval
	 *            表示要添加的时间间隔("y":年;"d":天;"m":月;如有必要可以自行增加)
	 * @param number
	 *            表示要添加的时间间隔的个数
	 * @param date
	 *            java.util.Date()
	 * @return String 2005-5-12格式的日期字串
	 */
	public static String dateAdd(String interval, int number,
			java.util.Date date) {
		String strTmp = "";
		Calendar gc = getCalendar(date);
		// 加若干年
		if ("y".equals(interval)) {
			int currYear = gc.get(Calendar.YEAR);
			gc.set(Calendar.YEAR, currYear + number);
		}
		// 加若干月
		else if ("m".equals(interval)) {
			int currMonth = gc.get(Calendar.MONTH);
			gc.set(Calendar.MONTH, currMonth + number);
		}
		// 加若干天
		else if ("d".equals(interval)) {
			int currDay = gc.get(Calendar.DATE);
			gc.set(Calendar.DATE, currDay + number);
		}
		// 加若小时
		else if ("h".equals(interval)) {
			int currDay = gc.get(Calendar.HOUR);
			gc.set(Calendar.HOUR, currDay + number);
		}
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(DATE_FORMAT_TIME);
		strTmp = bartDateFormat.format(gc.getTime());
		return strTmp;
	}

	/**
	 * <p>
	 * 返回两个日期之间的间隔天数
	 * </p>
	 * 
	 * @param a
	 *            java.util.Date
	 * @param b
	 *            java.util.Date
	 * @return int 间隔数
	 */
	public static int dateDiff(java.util.Date a, java.util.Date b) {
		int tempDifference = 0;
		int difference = 0;
		Calendar earlier = Calendar.getInstance();
		Calendar later = Calendar.getInstance();

		if (a.compareTo(b) < 0) {
			earlier.setTime(a);
			later.setTime(b);
		} else {
			earlier.setTime(b);
			later.setTime(a);
		}

		while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR)) {
			tempDifference = 365 * (later.get(Calendar.YEAR) - earlier
					.get(Calendar.YEAR));
			difference += tempDifference;

			earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
		}

		if (earlier.get(Calendar.DAY_OF_YEAR) != later
				.get(Calendar.DAY_OF_YEAR)) {
			tempDifference = later.get(Calendar.DAY_OF_YEAR)
					- earlier.get(Calendar.DAY_OF_YEAR);
			difference += tempDifference;

			earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
		}

		return difference;
	}	

	/**
	 * 获取当天的年龄
	 * @param date
	 * @return 
	 *Integer
	 * @exception 
	 * @since  1.0.0
	 */
	public static Integer getAgeFromBirthday(Date date){
		return new Date().getYear() - date.getYear();
	}

	public static void main(String[] args) {
		Date date=parseDate("1984年10月25日","yyyy年MM月dd日");
//		System.out.println(DateUtil.getThisYear() - DateUtil
//				.getYear(date));
	}

	/**
	 * 返回今天是本年的第几周
	 * @param calendar
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */

	public static int getWeekOfYear(Calendar calendar) {
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 返回今天是本年的第几周
	 * @param calendar
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */

	public static int getWeekOfMonth(Calendar calendar) {
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}
	
	/**
	 * 返回本年的第几天
	 * @param calendar
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */

	public static int getDayOfYear(Calendar calendar) {
		return calendar.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取某一(年内)周的开头天
	 * @param year
	 * @param week
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getYearWeekFirstDay(int year,int week){
		return getYearFieldFirstDay(year,Calendar.WEEK_OF_YEAR,week);
	}

	/**
	 * 获取某一(年内)周的结尾天
	 * @param year
	 * @param week
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getYearWeekLastDay(int year,int week){
		return getYearFieldLastDay(year,Calendar.WEEK_OF_YEAR,week);
	}
	/**
	 * 获取某一(年内)周的开头天
	 * @param date
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getYearWeekFirstDay(Date date){
		Calendar gc=getCalendar(date);
		int year=gc.get(Calendar.YEAR);
		int week=gc.get(Calendar.WEEK_OF_YEAR);		
		return getYearWeekFirstDay(year,week);
	}
	/**
	 * 获取某一(年内)周的结尾天
	 * @param date
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getYearWeekLastDay(Date date){
		Calendar gc=getCalendar(date);
		int year=gc.get(Calendar.YEAR);
		int week=gc.get(Calendar.WEEK_OF_YEAR);		
		return getYearWeekLastDay(year,week);
	}
//	/**
//	 * 获取某一(月内)周的开头天
//	 * @param year
//	 * @param week
//	 * @return 
//	 *Date
//	 * @exception 
//	 * @since  1.0.0
//	 */
//	public static Date getMonthWeekFirstDay(int year,int week){
//		return getYearFieldFirstDay(year,Calendar.WEEK_OF_MONTH,week);
//	}
//	/**
//	 * 获取某一(月内)周的开头天
//	 * @param date
//	 * @return 
//	 *Date
//	 * @exception 
//	 * @since  1.0.0
//	 */
//	public static Date getMonthWeekFirstDay(Date date){
//		Calendar gc=getCalendar(date);
//		int year=gc.get(Calendar.YEAR);
//		int week=gc.get(Calendar.WEEK_OF_MONTH);		
//		return getMonthWeekFirstDay(year,week);
//	}
//
//	/**
//	 * 获取某一(月内)周的结尾天
//	 * @param year
//	 * @param week
//	 * @return 
//	 *Date
//	 * @exception 
//	 * @since  1.0.0
//	 */
//	public static Date getMonthWeekLastDay(int year,int week){
//		return getYearFieldLastDay(year,Calendar.WEEK_OF_MONTH,week);
//	}
//	/**
//	 * 获取某一(月内)周的结尾天
//	 * @param date
//	 * @return 
//	 *Date
//	 * @exception 
//	 * @since  1.0.0
//	 */
//	public static Date getMonthWeekLastDay(Date date){
//		Calendar gc=getCalendar(date);
//		int year=gc.get(Calendar.YEAR);
//		int week=gc.get(Calendar.WEEK_OF_MONTH);		
//		return getMonthWeekLastDay(year,week);
//	}
	/**
	 * 
	 * 获取某一月的开头天
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getMonthFirstDay(Date date){
		return getYearFieldFirstDay(date.getYear(),Calendar.MONTH,date.getMonth());
	}
	/**
	 * 
	 * 获取某一月的结尾天
	 * @param year
	 * @param month
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getMonthLastDay(Date date){
		return getYearFieldFirstDay(date.getYear(),Calendar.MONTH,date.getMonth());
	}
	/**
	 * 
	 * 获取某一月的开头天
	 * @param year
	 * @param month
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getMonthFirstDay(int year,int month){
		return getYearFieldFirstDay(year,Calendar.MONTH,month);
	}
	/**
	 * 
	 * 获取某一月的结尾天
	 * @param year
	 * @param month
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getMonthLastDay(int year,int month){
		return getYearFieldFirstDay(year,Calendar.MONTH,month);
	}
	/**
	 * 
	 * 获取某一年的开头天
	 * @param year
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getYearFirstDay(int year){
		Calendar gc = Calendar.getInstance();
		gc.setTime(new Date(year-1900,0,1));	//月份从0开始
		return gc.getTime();
	}
	/**
	 * 获取某字段的第一天
	 * @param date
	 * @param field
	 * @param fieldValue
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getFieldFirstDay(Date date,int field,int fieldValue){
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);	//月份从0开始
		gc.set(field, fieldValue);
		return gc.getTime();
	}
	/**
	 * 获取年内某字段的第一天
	 * @param year
	 * @param field 字段的id,从Calendar查询，如Calendar.MONTH
	 * @param fieldValue 字段的值,如field为Calendar.MONTH,fieldValue为2，则为3月份
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getYearFieldFirstDay(int year,int field,int fieldValue){
		Calendar gc = Calendar.getInstance();
		gc.setTime(new Date(year-1900,0,1));	//月份从0开始
		gc.set(field, fieldValue);
		return gc.getTime();
	}
	/**
	 * 获取年内某字段的最后一天
	 * @param year
	 * @param field 字段的id,从Calendar查询，如Calendar.MONTH
	 * @param fieldValue 字段的值,如field为Calendar.MONTH,fieldValue为2，则为3月份
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date getYearFieldLastDay(int year,int field,int fieldValue){
		Calendar gc = Calendar.getInstance();
		gc.setTime(new Date(year-1900,0,1));	//月份从0开始
		gc.set(field, fieldValue+1);
		gc.roll(field, -1);
		return gc.getTime();
	}

	/**
	 * 
	 * getTotalDay<br/>
	 * 获取当月的天数,从正常月份计算，即从1开始 <br/>
	 * @return int
	 * @exception 
	 * @since  1.0.0
	 */
	public static int getMonthTotalDay(int year,int month)
	{
        return getMonthTotalDay(getCalendar(new Date(year-1900,month-1,1)));
	}
	/**
	 * 
	 * getTotalDay<br/>
	 * 获取当月的天数 <br/>
	 * @return int
	 * @exception 
	 * @since  1.0.0
	 */
	public static int getMonthTotalDay(Calendar calender)
	{
        return calender.getActualMaximum(Calendar.DAY_OF_MONTH);   
	}
	/**
	 * 获取当月的星期数
	 * @param firstDayOfWeek
	 * @param calender
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public static int getMonthTotalWeek(int firstDayOfWeek,Calendar calender)
	{
		calender.setFirstDayOfWeek(firstDayOfWeek);
        return calender.getActualMaximum(Calendar.WEEK_OF_MONTH);   
	}
	/**
	 * 星期map
	 */
	public static final Map<Integer, String> weekMap = new HashMap<Integer, String>(){{
		put(Calendar.MONDAY, "一");
		put(Calendar.TUESDAY, "二");
		put(Calendar.WEDNESDAY, "三");
		put(Calendar.THURSDAY, "四");
		put(Calendar.FRIDAY, "五");
		put(Calendar.SATURDAY, "六");
		put(Calendar.SUNDAY, "日");
	}};
	/**
	 * 
	 * 获取某日期是星期第几天
	 * @param date
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public static int getDayOfWeek(Date date) {
		Calendar c = getCalendar(date);
		int week = c.get(Calendar.DAY_OF_WEEK);
		return week;
	}
	/**
	 * 
	 * 获取某日期是星期第几天
	 * @param date
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public static int getDayOfWeek(String date) {
		Calendar c = getCalendar(parseDate(date));
		int week = c.get(Calendar.DAY_OF_WEEK);
		return week;
	}
	/**
	 * 
	 * 获取某日期是星期第几天
	 * @param date
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public static int getDayOfWeek(Calendar calendar) {
		int week = 0;
		week = calendar.get(Calendar.DAY_OF_WEEK);
		return week;
	}
	/**
	 * 获取某日期是星期几
	 * @param date
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	public static String getStrDayOfWeek(Date date) {
		Calendar c = getCalendar(date);
		String week = "未知";
		try {
			week = weekMap.get(c.get(Calendar.DAY_OF_WEEK));
		} catch (Exception e) {
			if(IS_ALLOW_THROW_DATE_EXCEPTION)
				throw new RuntimeException("日期转换失败"+date);
		}
		return week;
	}
	/**
	 * 获取某日期是星期几
	 * @param date
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	public static String getStrDayOfWeek(Calendar c) {
		String week = "未知";
		try {
			week = weekMap.get(c.get(Calendar.DAY_OF_WEEK));
		} catch (Exception e) {
			if(IS_ALLOW_THROW_DATE_EXCEPTION)
				throw new RuntimeException("日期转换失败"+c);
		}
		return week;
	}
	/**
	 * 
	 * 获取星期映射,一二三四五六日
	 * @return 
	 *Map<Integer,String>
	 * @exception 
	 * @since  1.0.0
	 */
	public static Map<Integer, String> getWeekMap() {
		return weekMap;
	}
	/**
	 * 星期数字转字符
	 * @param i
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	public static String getWeekStr(int i){
		return weekMap.get(i);
	}

	/**
	 * 获取日期format
	 * @param format
	 * @return 
	 *SimpleDateFormat
	 * @exception 
	 * @since  1.0.0
	 */
	private static SimpleDateFormat getDateFormat(String format) {
		if(date_map.get(format)==null){
			date_map.put(format, new SimpleDateFormat(format));
		}
		return date_map.get(format);
	}
	/**
	 * 将java.util.Date的日期转化为format格式的字符串
	 * 
	 * @param datestr
	 * @param pattern datestr的日期格式
	 * @return java.util.Date
	 */
	public static String formatDate(Date date, String format) {
		String str=null;
		if(date==null){
			return str;
		}
		try {
			SimpleDateFormat sdf = getDateFormat(format);
			synchronized (sdf) {
				str= sdf.format(date);
			}
		} catch (Exception e) {
			if(IS_ALLOW_THROW_DATE_EXCEPTION)
				throw new RuntimeException(MSG_FROMAT_DATE_ERR+date+",format:"+format);
		}
		return str;
	}

	/**
	 * 将java.util.Date的日期转化为format格式的字符串
	 * 
	 * @param datestr
	 * @param pattern datestr的日期格式
	 * @return java.util.Date
	 */
	public static String formatDate(String date, String format) {		
		return formatDate(parseDate(date,format),format);
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
		return formatDate(date, DATE_FORMAT_DEFAULT);
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
		return formatDate(date, DATE_FORMAT_TIME);
	}

	/**
	 * 日期转换
	 * @param date
	 * @param sdf
	 * @return 
	 *Date
	 * @exception 
	 * @since  1.0.0
	 */
	public static Date parseDate(String date,SimpleDateFormat sdf){
		Date i_date=null;
		if(date==null){
			return i_date;
		}
		try{
			if(sdf==null){
				sdf=getDateFormat(DATE_FORMAT_DEFAULT);			
			}
			synchronized (sdf){
				i_date= sdf.parse(date);
			}
		}catch (Exception e) {
			if(IS_ALLOW_THROW_DATE_EXCEPTION)
				throw new RuntimeException(MSG_PARSE_DATE_ERR+":"+date+",format:"+sdf.toPattern());
		}
		return i_date;
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
		return parseDate(date, getDateFormat(format));
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
		return parseDate(date, getDateFormat(DATE_FORMAT_DEFAULT));
	}
	public static String dateAdd(String date, int add) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DEFAULT);
		try {
			c.setTime(sdf.parse(date));
			c.add(c.DATE, add);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdf.format(c.getTime());
	}

	public static String monthAdd(String date, int add) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DEFAULT);
		try {
			c.setTime(sdf.parse(date));
			c.add(c.MONTH, add);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdf.format(c.getTime());
	}

	public static long dateDiff(String sdate1, String sdate2) {
		long diff = 0;
		Date date1 = null;
		Date date2 = null;
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_DEFAULT);
		try {
			date1 = format.parse(sdate1);
			date2 = format.parse(sdate2);
			diff = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diff;
	}

	/**
	 * 按指定日期单位计算两个日期间的间隔
	 * 
	 * @param timeInterval
	 *            总共支持year,quarter,month,week,day,hour,minute,second这几种时间间隔
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long dateDiff(String timeInterval, String sdate1,
			String sdate2) {
		Date date1 = null;
		Date date2 = null;
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_TIME);
		try {
			date1 = format.parse(sdate1);
			date2 = format.parse(sdate2);

			if ("year".equals(timeInterval)) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date1);
				int time = calendar.get(Calendar.YEAR);
				calendar.setTime(date2);
				return time - calendar.get(Calendar.YEAR);
			}

			if ("quarter".equals(timeInterval)) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date1);
				int time = calendar.get(Calendar.YEAR) * 4;
				calendar.setTime(date2);
				time -= calendar.get(Calendar.YEAR) * 4;
				calendar.setTime(date1);
				time += calendar.get(Calendar.MONTH) / 4;
				calendar.setTime(date2);
				return time - calendar.get(Calendar.MONTH) / 4;
			}

			if ("month".equals(timeInterval)) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date1);
				int time = calendar.get(Calendar.YEAR) * 12;
				calendar.setTime(date2);
				time -= calendar.get(Calendar.YEAR) * 12;
				calendar.setTime(date1);
				time += calendar.get(Calendar.MONTH);
				calendar.setTime(date2);
				return time - calendar.get(Calendar.MONTH);
			}

			if ("week".equals(timeInterval)) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date1);
				int time = calendar.get(Calendar.YEAR) * 52;
				calendar.setTime(date2);
				time -= calendar.get(Calendar.YEAR) * 52;
				calendar.setTime(date1);
				time += calendar.get(Calendar.WEEK_OF_YEAR);
				calendar.setTime(date2);
				return time - calendar.get(Calendar.WEEK_OF_YEAR);
			}

			if ("day".equals(timeInterval)) {
				long time = date1.getTime() / 1000 / 60 / 60 / 24;
				return time - date2.getTime() / 1000 / 60 / 60 / 24;
			}

			if ("hour".equals(timeInterval)) {
				long time = date1.getTime() / 1000 / 60 / 60;
				return time - date2.getTime() / 1000 / 60 / 60;
			}

			if ("minute".equals(timeInterval)) {
				long time = date1.getTime() / 1000 / 60;
				return time - date2.getTime() / 1000 / 60;
			}

			if ("second".equals(timeInterval)) {
				long time = date1.getTime() / 1000;
				return time - date2.getTime() / 1000;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date1.getTime() - date2.getTime();
	}
	/**
	 * 获得当前年份，格式为: 2008-08-08
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		return formatDate(new Date());
	}
	/**
	 * 获得当前年份，格式为: 2008-08-08
	 * 
	 * @return
	 */
	public static String getCurrentDateTime() {
		return formatTime(new Date());
	}

	/**
	 * 获得当前月份，格式为: 200801
	 * 
	 * @return
	 */
	public static int getCurrentMonth() {

		SimpleDateFormat format = getDateFormat("yyyyMM");

		String cm = format.format(new Date());

		return Integer.parseInt(cm);
	}

	public static int getCurrentMonthMin() {

		SimpleDateFormat format = new SimpleDateFormat("MM");

		String cm = format.format(new Date());

		return Integer.parseInt(cm);
	}

	/**
	 * 获得当前年份，格式为: 2008
	 * 
	 * @return
	 */
	public static int getCurrentYear() {

		SimpleDateFormat format = new SimpleDateFormat("yyyy");

		String cm = format.format(new Date());

		return Integer.parseInt(cm);
	}
	public static Timestamp getTimeBySystem(){
		return new Timestamp(new Date().getTime());
		
	}
	
	
	public static Date PreviousDate(){
		Calendar ca = Calendar.getInstance();//得到一个Calendar的实例 
		ca.setTime(new Date()); //设置时间为当前时间 
		ca.add(Calendar.DATE, -1);
		return ca.getTime();
	}
	
}
