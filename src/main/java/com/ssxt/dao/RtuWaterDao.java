package com.ssxt.dao;

import org.springframework.stereotype.Repository;

import com.ssxt.entity.mysql.RtuGroundWater;

import common.dao.GenericDaoMysqlImpl;
import common.dao.GenericDaoWatherImpl;

@Repository(value = "rtuWaterDao")
public class RtuWaterDao extends GenericDaoMysqlImpl<RtuGroundWater, Integer> {
}
