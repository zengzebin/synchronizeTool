package com.ssxt.entity.mysql;

 
 

import java.sql.Timestamp;

/**
 * RtuGroundWater entity. @author MyEclipse Persistence Tools
 */

public class RtuGroundWater implements java.io.Serializable {

	// Fields

	private Integer id;
	private String yczid;
	private String functioncode;
	private Timestamp tt;
	private Timestamp insertdt;
	private String ff0e1;
	private String ff0e2;
	private String ff0e3;
	private String ff0e4;
	private String ff0e5;
	private String ff0e6;
	private String ff031;
	private String ff032;
	private String ff033;
	private String ff034;
	private String ff035;
	private String ff036;
	private String vt;

	// Constructors

	/** default constructor */
	public RtuGroundWater() {
	}

	/** minimal constructor */
	public RtuGroundWater(Integer id, String yczid, Timestamp tt) {
		this.id = id;
		this.yczid = yczid;
		this.tt = tt;
	}

	/** full constructor */
	public RtuGroundWater(Integer id, String yczid, String functioncode,
			Timestamp tt, Timestamp insertdt, String ff0e1, String ff0e2,
			String ff0e3, String ff0e4, String ff0e5, String ff0e6,
			String ff031, String ff032, String ff033, String ff034,
			String ff035, String ff036, String vt) {
		this.id = id;
		this.yczid = yczid;
		this.functioncode = functioncode;
		this.tt = tt;
		this.insertdt = insertdt;
		this.ff0e1 = ff0e1;
		this.ff0e2 = ff0e2;
		this.ff0e3 = ff0e3;
		this.ff0e4 = ff0e4;
		this.ff0e5 = ff0e5;
		this.ff0e6 = ff0e6;
		this.ff031 = ff031;
		this.ff032 = ff032;
		this.ff033 = ff033;
		this.ff034 = ff034;
		this.ff035 = ff035;
		this.ff036 = ff036;
		this.vt = vt;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getYczid() {
		return this.yczid;
	}

	public void setYczid(String yczid) {
		this.yczid = yczid;
	}

	public String getFunctioncode() {
		return this.functioncode;
	}

	public void setFunctioncode(String functioncode) {
		this.functioncode = functioncode;
	}

	public Timestamp getTt() {
		return this.tt;
	}

	public void setTt(Timestamp tt) {
		this.tt = tt;
	}

	public Timestamp getInsertdt() {
		return this.insertdt;
	}

	public void setInsertdt(Timestamp insertdt) {
		this.insertdt = insertdt;
	}

	public String getFf0e1() {
		return this.ff0e1;
	}

	public void setFf0e1(String ff0e1) {
		this.ff0e1 = ff0e1;
	}

	public String getFf0e2() {
		return this.ff0e2;
	}

	public void setFf0e2(String ff0e2) {
		this.ff0e2 = ff0e2;
	}

	public String getFf0e3() {
		return this.ff0e3;
	}

	public void setFf0e3(String ff0e3) {
		this.ff0e3 = ff0e3;
	}

	public String getFf0e4() {
		return this.ff0e4;
	}

	public void setFf0e4(String ff0e4) {
		this.ff0e4 = ff0e4;
	}

	public String getFf0e5() {
		return this.ff0e5;
	}

	public void setFf0e5(String ff0e5) {
		this.ff0e5 = ff0e5;
	}

	public String getFf0e6() {
		return this.ff0e6;
	}

	public void setFf0e6(String ff0e6) {
		this.ff0e6 = ff0e6;
	}

	public String getFf031() {
		return this.ff031;
	}

	public void setFf031(String ff031) {
		this.ff031 = ff031;
	}

	public String getFf032() {
		return this.ff032;
	}

	public void setFf032(String ff032) {
		this.ff032 = ff032;
	}

	public String getFf033() {
		return this.ff033;
	}

	public void setFf033(String ff033) {
		this.ff033 = ff033;
	}

	public String getFf034() {
		return this.ff034;
	}

	public void setFf034(String ff034) {
		this.ff034 = ff034;
	}

	public String getFf035() {
		return this.ff035;
	}

	public void setFf035(String ff035) {
		this.ff035 = ff035;
	}

	public String getFf036() {
		return this.ff036;
	}

	public void setFf036(String ff036) {
		this.ff036 = ff036;
	}

	public String getVt() {
		return this.vt;
	}

	public void setVt(String vt) {
		this.vt = vt;
	}

}