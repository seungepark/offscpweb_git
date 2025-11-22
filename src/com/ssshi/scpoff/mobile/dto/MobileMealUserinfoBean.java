package com.ssshi.scpoff.mobile.dto;

import org.apache.ibatis.type.Alias;

@Alias("mobileMealUserinfoBean")
public class MobileMealUserinfoBean {

	private int uid;
	private String projNo;
	private Integer schedulerInfoUid;
	private String trialKey;
	private Integer scheuserUid;
	private String deptName;
	private String mealTime;
	private String mealGubun;
	private String mealDate;
	private String qrYn;
	private String androidId;
	private String mobileDeviceId;
	private Integer insertby;
	private String insertdate;
	private Integer updateby;
	private String updatedate;
	
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
	public Integer getSchedulerInfoUid() {
		return schedulerInfoUid;
	}
	public void setSchedulerInfoUid(Integer schedulerInfoUid) {
		this.schedulerInfoUid = schedulerInfoUid;
	}
	public String getTrialKey() {
		return trialKey;
	}
	public void setTrialKey(String trialKey) {
		this.trialKey = trialKey;
	}
	public Integer getScheuserUid() {
		return scheuserUid;
	}
	public void setScheuserUid(Integer scheuserUid) {
		this.scheuserUid = scheuserUid;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getMealTime() {
		return mealTime;
	}
	public void setMealTime(String mealTime) {
		this.mealTime = mealTime;
	}
	public String getMealGubun() {
		return mealGubun;
	}
	public void setMealGubun(String mealGubun) {
		this.mealGubun = mealGubun;
	}
	public String getMealDate() {
		return mealDate;
	}
	public void setMealDate(String mealDate) {
		this.mealDate = mealDate;
	}
	public String getQrYn() {
		return qrYn;
	}
	public void setQrYn(String qrYn) {
		this.qrYn = qrYn;
	}
	public String getAndroidId() {
		return androidId;
	}
	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}
	public String getMobileDeviceId() {
		return mobileDeviceId;
	}
	public void setMobileDeviceId(String mobileDeviceId) {
		this.mobileDeviceId = mobileDeviceId;
	}
	public Integer getInsertby() {
		return insertby;
	}
	public void setInsertby(Integer insertby) {
		this.insertby = insertby;
	}
	public String getInsertdate() {
		return insertdate;
	}
	public void setInsertdate(String insertdate) {
		this.insertdate = insertdate;
	}
	public Integer getUpdateby() {
		return updateby;
	}
	public void setUpdateby(Integer updateby) {
		this.updateby = updateby;
	}
	public String getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}
}

