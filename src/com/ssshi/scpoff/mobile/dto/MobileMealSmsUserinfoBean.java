package com.ssshi.scpoff.mobile.dto;

import org.apache.ibatis.type.Alias;

@Alias("mobileMealSmsUserinfoBean")
public class MobileMealSmsUserinfoBean {

	private int uid;
	private String projNo;
	private Long schedulerInfoUid;
	private String trialKey;
	private Long anchorMealUid;
	private String regUsername;
	private String phone;
	private String insertDate;
	private String regDevice;
	private String smsyn;
	private String smsrReturn;
	private String smsInsertDate;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getProjNo() {
		return projNo;
	}
	public void setProjNo(String projNo) {
		this.projNo = projNo;
	}
	public Long getSchedulerInfoUid() {
		return schedulerInfoUid;
	}
	public void setSchedulerInfoUid(Long schedulerInfoUid) {
		this.schedulerInfoUid = schedulerInfoUid;
	}
	public String getTrialKey() {
		return trialKey;
	}
	public void setTrialKey(String trialKey) {
		this.trialKey = trialKey;
	}
	public Long getAnchorMealUid() {
		return anchorMealUid;
	}
	public void setAnchorMealUid(Long anchorMealUid) {
		this.anchorMealUid = anchorMealUid;
	}
	public String getRegUsername() {
		return regUsername;
	}
	public void setRegUsername(String regUsername) {
		this.regUsername = regUsername;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}
	public String getRegDevice() {
		return regDevice;
	}
	public void setRegDevice(String regDevice) {
		this.regDevice = regDevice;
	}
	public String getSmsyn() {
		return smsyn;
	}
	public void setSmsyn(String smsyn) {
		this.smsyn = smsyn;
	}
	public String getSmsrReturn() {
		return smsrReturn;
	}
	public void setSmsrReturn(String smsrReturn) {
		this.smsrReturn = smsrReturn;
	}
	public String getSmsInsertDate() {
		return smsInsertDate;
	}
	public void setSmsInsertDate(String smsInsertDate) {
		this.smsInsertDate = smsInsertDate;
	}
}

