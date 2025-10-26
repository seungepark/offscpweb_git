package com.ssshi.scpoff.dto;

public class VesselReqInfoBean {

    private int uid;
    private String hullNum;
    private String shiptype;
    private String description;
    private String status;
    private String registerdowner;
    private String grosstonnage;
    private String drawn;
    private String checked;
    private String manager;
    private int  schedinfouid;
    
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getHullNum() {
		return hullNum;
	}
	public void setHullNum(String hullNum) {
		this.hullNum = hullNum;
	}
	public String getShiptype() {
		return shiptype;
	}
	public void setShiptype(String shiptype) {
		this.shiptype = shiptype;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRegisterdowner() {
		return registerdowner;
	}
	public void setRegisterdowner(String registerdowner) {
		this.registerdowner = registerdowner;
	}
	public String getGrosstonnage() {
		return grosstonnage;
	}
	public void setGrosstonnage(String grosstonnage) {
		this.grosstonnage = grosstonnage;
	}
	public String getDrawn() {
		return drawn;
	}
	public void setDrawn(String drawn) {
		this.drawn = drawn;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public int getSchedinfouid() {
		return schedinfouid;
	}
	public void setSchedinfouid(int schedinfouid) {
		this.schedinfouid = schedinfouid;
	}
}
