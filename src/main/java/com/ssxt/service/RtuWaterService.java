package com.ssxt.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONObject;
import com.ssxt.dao.RtuWaterDao;
import com.ssxt.entity.mysql.Log;
import com.ssxt.entity.mysql.RtuGroundWater;
import com.alibaba.fastjson.TypeReference;
import common.dao.GenericDao;
import common.http.AccData;
import common.http.HttpRequestUtil;
import common.service.GenericServiceImpl;
import common.util.ConstParam;
import common.util.DateConverter;

@Service(value = "rtuWaterService")
public class RtuWaterService extends GenericServiceImpl<RtuGroundWater, Integer> {
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RtuWaterService.class);

	public GenericDao<RtuGroundWater, Integer> getDao() {
		return rtuWaterDao;
	}

	@Resource(name = "rtuWaterDao")
	private RtuWaterDao rtuWaterDao;

	@Resource(name = "logService")
	private LogService logService;

	public AccData<RtuGroundWater> getHttp(Log logInfo) throws Exception {

		String url = ConstParam.getProperty("wather.url");
		String ip = ConstParam.getProperty("wahter.ip");
		String yczid = ConstParam.getProperty("wahter.yczid");
		String startTime = URLEncoder.encode(logInfo.getStartTime(), "utf-8");
		String endTime = URLEncoder.encode(logInfo.getEndTime(), "utf-8");
		StringBuffer parameter = new StringBuffer("ip=" + ip);
		parameter.append("&startTime=" + startTime).append("&endTime=" + endTime).append("&yczid=" + yczid);
		String json = HttpRequestUtil.httpRequest(url + parameter, "GET", null);
		log.info(url + parameter);
		AccData<RtuGroundWater> data = JSONObject.parseObject(json, new TypeReference<AccData<RtuGroundWater>>() {});
		return data;
	}

	@Transactional
	public void insert(List<RtuGroundWater> data) {
		for (int i = 0; i < data.size(); i++) {
			RtuGroundWater r = data.get(i);

			String hql = "from RtuGroundWater where YCZID=? and TT=?";
			List<RtuGroundWater> list = rtuWaterDao.getHibernateTemplate().find(hql,
					new Object[] { r.getYczid(), r.getTt() });
			if (list.size() > 0) {
				RtuGroundWater target = list.get(0);
				String[] ignore = { "id", "tt" };
				BeanUtils.copyProperties(r, target, ignore);
				rtuWaterDao.update(target);
				log.info("已经有这条数据了，已经更新");
			} else {
				rtuWaterDao.save(r);
			}
		}
	}
}
