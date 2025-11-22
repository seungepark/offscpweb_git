package com.ssshi.scpoff.mobile.dto;

import org.apache.ibatis.type.Alias;

@Alias("mobileLiftBoatInfoBean")
public class MobileLiftBoatInfoBean {

	private int uid;
	private String projNo;
	private Long schedulerInfoUid;
	private String trialKey;
	private Long liftboatCnt;
	private Long liftboatHeadcnt;
	private Long liftraftCnt;
	private Long liftraftHeadcnt;
	private String androidId;
	private String insertDate;
	private Integer insertby;
	private String regDevice;
	
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
	public Long getLiftboatCnt() {
		return liftboatCnt;
	}
	public void setLiftboatCnt(Long liftboatCnt) {
		this.liftboatCnt = liftboatCnt;
	}
	public Long getLiftboatHeadcnt() {
		return liftboatHeadcnt;
	}
	public void setLiftboatHeadcnt(Long liftboatHeadcnt) {
		this.liftboatHeadcnt = liftboatHeadcnt;
	}
	public Long getLiftraftCnt() {
		return liftraftCnt;
	}
	public void setLiftraftCnt(Long liftraftCnt) {
		this.liftraftCnt = liftraftCnt;
	}
	public Long getLiftraftHeadcnt() {
		return liftraftHeadcnt;
	}
	public void setLiftraftHeadcnt(Long liftraftHeadcnt) {
		this.liftraftHeadcnt = liftraftHeadcnt;
	}
	public String getAndroidId() {
		return androidId;
	}
	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}
	public String getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}
	public Integer getInsertby() {
		return insertby;
	}
	public void setInsertby(Integer insertby) {
		this.insertby = insertby;
	}
	public String getRegDevice() {
		return regDevice;
	}
	public void setRegDevice(String regDevice) {
		this.regDevice = regDevice;
	}
}
