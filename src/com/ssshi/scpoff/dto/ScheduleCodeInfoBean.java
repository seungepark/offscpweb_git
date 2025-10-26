package com.ssshi.scpoff.dto;

public class ScheduleCodeInfoBean {
	
	private int uid;
	private String shiptype;
	private String description;
	private String status;
	private int revnum;
	private String schedtype;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
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
	public int getRevnum() {
		return revnum;
	}
	public void setRevnum(int revnum) {
		this.revnum = revnum;
	}
	public String getSchedtype() {
		return schedtype;
	}
	public void setSchedtype(String schedtype) {
		this.schedtype = schedtype;
	}
}
