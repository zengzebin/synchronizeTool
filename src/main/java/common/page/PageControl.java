package common.page;

/**
 * <b>项目名：</b>南粤人才市场-DOSS系统<br/>
 * <b>包名：</b>com.diyeasy.common.sql<br/>
 * <b>文件名：</b>PageControl.java<br/>
 * <b>@author 杨培新<br/></b>
 * <b>版本信息：</b><br/>
 * <b>日期：</b>2009-10-27-上午10:51:56<br/>
 * <b>Copyright (c)</b> 2009南粤人才市场-版权所有<br/>
 * 
 */
 

import java.util.List;

public class PageControl implements java.io.Serializable {
	
	private static final long serialVersionUID = -4481687572865020155L;

	public static final int QUERY_ONLY=0;
	public static final int QUERY_COUNT=1;
	public static final int COUNT_ONLY=2;
	/**
	 * 当前页码
	 */
	protected int pageNo = 1; // 当前页码
	
	
	/**
	 * 起始记录
	 */
	private int start = 0; // 起始记录

	/**
	 * 每页行数
	 */
	private int pageSize = 10; // 每页行数

	/**
	 * 总行数
	 */
	private long rowCount; // 总行数

	/**
	 * 总页数
	 */
	private int pageCount; // 总页数

	/**
	 * 前一页是否能用
	 */
	private boolean usePrevious;// 前一页是否能用	

	/**
	 * 后一页是否能用
	 */
	private boolean useBehind;// 后一页是否能用

	/**
	 * 是否分页
	 */
	private boolean usePage = false;// 是否分页

	/**
	 * 是否统计总数
	 */
	private boolean useCount = false;// 是否统计总数

	/**
	 * 排序
	 */
	private String order = null;

	/**
	 * 是执行查询
	 */
	private boolean useQuery = true;// 是否执行查询
	
	/**
	 * 查询方式
	 */
	private int executeType = QUERY_ONLY;// 
	
	/**
	 * 使用缓存
	 */
	private boolean useCache = true;//

	/**
	 * 设置排序字符串,必须以"order by"开头,否则无效
	 * @param order
	 * 				要设置的排序字符串
	 */
	public PageControl setOrder(String order) {
		if (order.indexOf("order by") > -1)
			this.order = order;
		return this;
	}
	/**
	 * 
	 */
	private String preFromSql=null;
	/**
	 * 
	 */
	private String groupBy=null;
	
	private List<?> list = null;
	
	/**
	 * 获得排序字符串
	 * @return 
	 *              排序字符串
	 */

	public String getOrder() {
		return order;
	}

	public int getPageNo() {
		return pageNo;
	}

	public PageControl setPageNo(int pageNo) {
		this.pageNo = pageNo;
		if(pageNo<1)pageNo=1;
		start=(pageNo-1)* pageSize;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public PageControl setPageSize(int pageSize) {
		if (pageSize == 0) {// 0-->不分页
			usePage 	=	false;
			pageCount 	=	1;
			pageNo 		=	1;
			usePrevious =	false;
			useBehind 	=	false;
			usePage		=	false;
		}
		this.pageSize = pageSize;
		return this;
	}

	public long getRowCount() {
		return rowCount;
	}

	public PageControl setRowCount(long rowCount) {
		this.rowCount = rowCount;
		if(0==rowCount){
			pageCount=0;
			pageNo=1;
			usePrevious=false;
			useBehind=false;
		}
		else {
			// pageCount=(rowCount % pageSize ==0 )?(rowCount /pageSize ) : (
			// rowCount / pageSize +1);
			pageCount = (int) ((rowCount + pageSize - 1) / pageSize);
			if (pageNo > pageCount)
				pageNo = pageCount;
			usePrevious = pageNo == 1 ? false : true;
			useBehind = pageNo == pageCount ? false : true;
		}
		return this;
	}

	public int getPageCount() {
		return pageCount;
	}

	public PageControl setPageCount(int pageCount) {
		this.pageCount = pageCount;
		return this;
	}

	public boolean isUsePrevious() {
		return usePrevious;
	}

	public PageControl setUsePrevious(boolean usePrevious) {
		this.usePrevious = usePrevious;
		return this;
	}

	public boolean isUseBehind() {
		return useBehind;
	}

	public PageControl setUseBehind(boolean useBehind) {
		this.useBehind = useBehind;
		return this;
	}

	public boolean isUsePage() {
		return usePage;
	}

	public PageControl setUsePage(boolean usePage) {
		this.usePage = usePage;
		return this;
	}

	public boolean isUseCount() {
		return useCount;
	}

	/**
	 * 返回当前页的记录条数
	 *
	 * @return 当前页的记录条数
	 */
	public int getPageRowCount() {
		if (pageSize == 0)
			return (int) rowCount;
		if (rowCount == 0)
			return 0;
		if (pageNo != getPageCount())
			return pageSize;
		return (int) (rowCount - (getPageCount() - 1) * pageSize);
	}

	/**
	 * list
	 *
	 * @return  the list
	 * @since   1.0.0
	 */
	
	public List<?> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 * @since  1.0.0
	 */
	public PageControl setList(List<?> list) {
		this.list = list;
		return this;	
	}

	/**
	 * start
	 *
	 * @return  the start
	 * @since   1.0.0
	 */
	
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 * @since  1.0.0
	 */
	public PageControl setStart(int start) {
		this.start = start;
		pageNo=start/pageSize+1;
		return this;
	}

	/**
	 * executeType
	 *
	 * @return  the executeType
	 * @since   1.0.0
	 */
	
	public int getExecuteType() {
		return executeType;
	}

	/**
	 * @param executeType the executeType to set
	 * @since  1.0.0
	 */
	public PageControl setExecuteType(int executeType) {
		this.executeType = executeType;
		switch (executeType) {
		case	QUERY_COUNT:
				useQuery=true;
				useCount=true;			
				break;
		case	QUERY_ONLY:
				useQuery=true;
				useCount=false;	
				break;
		case	COUNT_ONLY:
				useQuery=false;
				useCount=true;	
				break;
		

		default:
			break;
		}
		return this;
	
	}

	/**
	 * useQuery
	 *
	 * @return  the useQuery
	 * @since   1.0.0
	 */
	
	public boolean isUseQuery() {
		return useQuery;
	}

	/**
	 * useCache
	 *
	 * @return  the useCache
	 * @since   1.0.0
	 */
	
	public boolean isUseCache() {
		return useCache;
	}

	/**
	 * useCache<br/>
	 * @param useCache the useCache to set
	 * @since  1.0.0
	 */
	public PageControl setUseCache(boolean useCache) {
		this.useCache = useCache;
		return this;
	}
	
	/**
	 * getQueryOnlyInstance(返回一个只查询的PageControl设定实例)<br/>
	 * (这里描述这个方法适用条件 – 可选)<br/>
	 * (这里描述这个方法的执行流程 – 可选)<br/>
	 * (这里描述这个方法的使用方法 – 可选)<br/>
	 * (该方法为生成新实例)<br/> 
	 *PageControl
	 * @exception 
	 * @since  1.0.0
	 */
	public static PageControl getQueryOnlyInstance() {
		return getInstance(PageControl.QUERY_ONLY,false);		
	}
	/**
	 * getCountOnlyInstance(返回一个只统计的PageControl设定实例)<br/>
	 * (这里描述这个方法适用条件 – 可选)<br/>
	 * (这里描述这个方法的执行流程 – 可选)<br/>
	 * (这里描述这个方法的使用方法 – 可选)<br/>
	 * (该方法为生成新实例)<br/> 
	 *PageControl
	 * @exception 
	 * @since  1.0.0
	 */
	public static PageControl getCountOnlyInstance() {
		return getInstance(PageControl.COUNT_ONLY,false);
	}
	/**
	 * getPageEnableInstance(返回一个可分页、带统计的PageControl设定实例)<br/>
	 * (这里描述这个方法适用条件 – 可选)<br/>
	 * (这里描述这个方法的执行流程 – 可选)<br/>
	 * (这里描述这个方法的使用方法 – 可选)<br/>
	 * (该方法为生成新实例)<br/> 
	 *PageControl
	 * @exception 
	 * @since  1.0.0
	 */
	public static PageControl getPageEnableInstance(int start,int pageSize) {
		PageControl pageControl=getInstance(PageControl.QUERY_COUNT,true);
		pageControl.setPageSize(pageSize);
		pageControl.setStart(start);
		return pageControl;	
	}
	/**
	 * getPageEnableWithoutCountInstance(返回一个可分页、不带统计的PageControl设定实例)<br/>
	 * (这里描述这个方法适用条件 – 可选)<br/>
	 * (这里描述这个方法的执行流程 – 可选)<br/>
	 * (这里描述这个方法的使用方法 – 可选)<br/>
	 * (该方法为生成新实例)<br/> 
	 *PageControl
	 * @exception 
	 * @since  1.0.0
	 */
	public static PageControl getPageEnableWithoutCountInstance(int start,int pageSize) {
		PageControl pageControl=getInstance(PageControl.QUERY_ONLY,true);
		pageControl.setPageSize(pageSize);
		pageControl.setStart(start);
		return pageControl;	
	}
	/**
	 * getPageEnableInstance(返回一个可分页、带统计的PageControl设定实例)<br/>
	 * (这里描述这个方法适用条件 – 可选)<br/>
	 * (这里描述这个方法的执行流程 – 可选)<br/>
	 * (这里描述这个方法的使用方法 – 可选)<br/>
	 * (该方法为生成新实例)<br/> 
	 *PageControl
	 * @exception 
	 * @since  1.0.0
	 */
	public static PageControl getPageEnableInstance() {
		return getInstance(PageControl.QUERY_COUNT,true);	
	}
	/**
	 * getPageDisableInstance(返回一个不可分页、带统计的PageControl设定实例)<br/>
	 * (这里描述这个方法适用条件 – 可选)<br/>
	 * (这里描述这个方法的执行流程 – 可选)<br/>
	 * (这里描述这个方法的使用方法 – 可选)<br/>
	 * (该方法为生成新实例)<br/> 
	 *PageControl
	 * @exception 
	 * @since  1.0.0
	 */
	public static PageControl getPageDisableInstance() {
		return getInstance(PageControl.QUERY_COUNT,false);		
	}
	/**
	 * getInstance(返回一个可以设定的PageControl设定实例)<br/>
	 * (这里描述这个方法适用条件 – 可选)<br/>
	 * (这里描述这个方法的执行流程 – 可选)<br/>
	 * (这里描述这个方法的使用方法 – 可选)<br/>
	 * (该方法为生成新实例)<br/> 
	 *PageControl
	 * @exception 
	 * @since  1.0.0
	 */
	public static PageControl getInstance(int excuteType,boolean usePage) {
		PageControl pageControl=new PageControl();
		pageControl.setUsePage(usePage);
		pageControl.setExecuteType(excuteType);
		return pageControl;		
	}

	/**
	 * preFromSql
	 *
	 * @return  the preFromSql
	 * @since   1.0.0
	 */
	
	public String getPreFromSql() {
		return preFromSql;
	}

	/**
	 * preFromSql<br/>
	 * @param preFromSql the preFromSql to set
	 * @since  1.0.0
	 */
	public PageControl setPreFromSql(String preFromSql) {
		this.preFromSql = preFromSql;
		return this;
	}

	
	/**
     * 返回分页控制的html代码
     * 
     * @return String 专用方法(为了调用方便,暂时放在该工具类中),因为调用处较多,暂不拆分
     */
    public String getShowPage() {
        StringBuffer sbPage = new StringBuffer("\n");
        String PATH_ROOT="..";
        //if (pageCount > 1) {
            if (usePrevious)
                sbPage.append("<a href=\"javascript:GoPage(1)\">"
               				+ "<img src=\""+PATH_ROOT+"/images/firs_button.gif\" border=\"0\" valign=\"absbottom\"></a>"
							+ "&nbsp;&nbsp;<a href=\"javascript:GoPage("
                            + (pageNo - 1)
                            + ")\"><img src=\""+PATH_ROOT+"/images/prev_button.gif\" border=\"0\" valign=\"absbottom\"></a>");
            if (useBehind)
                sbPage.append("&nbsp;&nbsp;<a href=\"javascript:GoPage("
                            + (pageNo + 1)
                            + ")\"><img src=\""+PATH_ROOT+"/images/next_button.gif\" border=\"0\" valign=\"absbottom\"></a>"
							+ "&nbsp;&nbsp;<a href=\"javascript:GoPage("
                            + pageCount
                            + ")\"><img src=\""+PATH_ROOT+"/images/last_button.gif\" border=\"0\" valign=\"absbottom\"></a>\n");

            //if (pageCount > 1) {
                sbPage.append("&nbsp;&nbsp;共<b>" + pageCount + "</b>页\n");
                sbPage.append("&nbsp;&nbsp;每页<b>" + pageSize + "</b>条记录（<font color=\"red\">共<b>" + rowCount + "</b>条</font>）\n");
                sbPage.append("&nbsp;&nbsp;当前第<select name=\"ipage\" onchange=\"GoPage(this.value)\">    \n");
                int i = ((pageNo+5)/10-1)*10;
                if(i<0)
                	i=0;
                int maxLen=i+20;
                if(maxLen>pageCount)
                	maxLen=pageCount;
                for (i++; i <= maxLen; i++) {
                    if (i == pageNo)
                        sbPage.append("        <option selected value=\"" + i
                                + "\">" + i + "</option>  \n");
                    else
                        sbPage.append("        <option value=\"" + i + "\">"
                                + i + "</option>  \n");
                }
                sbPage.append("      </select>页\n");
                sbPage.append("<a href=\"javascript:GoPage("+pageNo+")\" valign=\"absbottom\">  刷新</a>");
            //}
        //}
        return sbPage.toString();
    }

	/**
	 * groupBy
	 *
	 * @return  the groupBy
	 * @since   1.0.0
	 */
	
	public String getGroupBy() {
		return groupBy;
	}

	/**
	 * @param groupBy the groupBy to set
	 */
	public PageControl setGroupBy(String groupBy) {
		this.groupBy = groupBy;
		return this;
	}
}
