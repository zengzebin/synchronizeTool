package com.ssxt.entity.mysql;

import java.sql.Timestamp;

/**
 * Log entity. @author MyEclipse Persistence Tools
 */

public class Log implements java.io.Serializable {

	// Fields

		private Integer id;
		private Integer typeId;
		private String content;
		private String ip;
		private Integer totality;
		private Integer page;
		private Integer pageSize;

		private String startTime;
		private String endTime;
		private Timestamp insertTime;
		private Integer error;

		// Constructors

		public Integer getError() {
			return error;
		}

		public void setError(Integer error) {
			this.error = error;
		}

		/** default constructor */
		public Log() {
		}

		/** minimal constructor */
		public Log(Integer id, Timestamp insertTime) {
			this.id = id;
			this.insertTime = insertTime;
		}

		/** full constructor */
		public Log(Integer id, Integer typeId, String content, String ip, Integer totality, Integer page, String startTime,
				String endTime, Timestamp insertTime, Integer pageSize, Integer error) {
			this.id = id;
			this.typeId = typeId;
			this.content = content;
			this.ip = ip;
			this.totality = totality;
			this.page = page;
			this.startTime = startTime;
			this.endTime = endTime;
			this.insertTime = insertTime;
			this.pageSize = pageSize;
			this.error = error;
		}

		// Property accessors

		public Integer getId() {
			return this.id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getTypeId() {
			return this.typeId;
		}

		public void setTypeId(Integer typeId) {
			this.typeId = typeId;
		}

		public String getContent() {
			return this.content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getIp() {
			return this.ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public Integer getTotality() {
			return this.totality;
		}

		public void setTotality(Integer totality) {
			this.totality = totality;
		}

		public Integer getPage() {
			return this.page;
		}

		public void setPage(Integer page) {
			this.page = page;
		}

		public String getStartTime() {
			return this.startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return this.endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public Timestamp getInsertTime() {
			return this.insertTime;
		}

		public void setInsertTime(Timestamp insertTime) {
			this.insertTime = insertTime;
		}

		public Integer getPageSize() {
			return pageSize;
		}

		public void setPageSize(Integer pageSize) {
			this.pageSize = pageSize;
		}

}