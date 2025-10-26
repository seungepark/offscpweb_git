package com.ssshi.scpoff.dto;

public class DomainInfoBean {

	private int uid;
	private int domainUid;
	private String val;
	private String inVal;
	private String description;
	private String insertDate;
	private int insertBy;
	private String updateDate;
	private int updateBy;
	
	private int userUid;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getDomainUid() {
		return domainUid;
	}
	public void setDomainUid(int domainUid) {
		this.domainUid = domainUid;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getInVal() {
		return inVal;
	}
	public void setInVal(String inVal) {
		this.inVal = inVal;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getUserUid() {
		return userUid;
	}
	public void setUserUid(int userUid) {
		this.userUid = userUid;
	}
	public String getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}
	public int getInsertBy() {
		return insertBy;
	}
	public void setInsertBy(int insertBy) {
		this.insertBy = insertBy;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public int getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(int updateBy) {
		this.updateBy = updateBy;
	}
}
