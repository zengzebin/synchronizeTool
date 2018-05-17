package com.ssxt.web;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ssxt.entity.mysql.Log;

import com.ssxt.job.SynchronousJob;

import com.ssxt.service.LogService;
import com.ssxt.service.RtuWaterService;

import common.dao.dynamic.sessionFactory.CustomerContextHolder;
import common.dao.dynamic.templet.DataSourceHolder;
import common.page.PageControl;
import common.page.SqlBuffer;
import common.util.AccData;
import common.util.CtxHelper;
import common.util.DateConverter;
import common.util.SpringContextUtil;

@Controller
@RequestMapping(value = "/api/test/")
public class RtuWaterContoller {
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RtuWaterContoller.class);

	@RequestMapping(value = "getHttp", method = RequestMethod.GET)
	public void getDeviceList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AccData ad = CtxHelper.createFailAccData();
		Log logInfo = new Log();
		try {
			/*
			 * Timestamp tim = new Timestamp(System.currentTimeMillis());
			 * PageControl pageControl = PageControl.getPageEnableInstance();
			 * pageControl.setPageSize(1000); pageControl.setPageNo(1);
			 * SqlBuffer sbf = SqlBuffer.begin(); String startTime =
			 * request.getParameter("startTime"); String YCZID =
			 * request.getParameter("YCZID"); String ip=
			 * request.getParameter("ip"); SimpleDateFormat dateFormater = new
			 * SimpleDateFormat("yyyy-MM-dd " + startTime); Date date = new
			 * Date();
			 * 
			 * Date previous = DateConverter.PreviousDate(); sbf.add("TT",
			 * dateFormater.format(previous), ">=", "and"); sbf.add("TT",
			 * dateFormater.format(date), "<", "and");
			 */

			SynchronousJob job = new SynchronousJob();
			job.execute(null);
			ad.setSuccess(true);
			CtxHelper.writeToJson(response, ad);
		} catch (RuntimeException e) {
			log.error("鍦颁笅姘存暟鎹悓姝�-RtuWaterContoller-getRtuWaterData", e);
			ad.setSuccess(false);
			CtxHelper.writeToJson(response, ad);
		} catch (Exception e) {
			log.error("鍦颁笅姘存暟鎹悓姝�-RtuWaterContoller-getRtuWaterData", e);
			ad.setSuccess(false);
			CtxHelper.writeToJson(response, ad);
		}

	}

}
