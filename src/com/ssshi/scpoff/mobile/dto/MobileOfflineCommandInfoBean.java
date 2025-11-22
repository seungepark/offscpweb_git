package com.ssshi.scpoff.mobile.dto;

import org.apache.ibatis.type.Alias;

@Alias("mobileOfflineCommandInfoBean")
public class MobileOfflineCommandInfoBean {

	private int uid;
	private String projNo;
	private Long schedulerInfoUid;
	private String trialKey;
	private String commandIp;
	private String commandPort;
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
	public String getCommandIp() {
		return commandIp;
	}
	public void setCommandIp(String commandIp) {
		this.commandIp = commandIp;
	}
	public String getCommandPort() {
		return commandPort;
	}
	public void setCommandPort(String commandPort) {
		this.commandPort = commandPort;
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
