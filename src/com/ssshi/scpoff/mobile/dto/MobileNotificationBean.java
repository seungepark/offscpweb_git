package com.ssshi.scpoff.mobile.dto;

import org.apache.ibatis.type.Alias;

@Alias("mobileNotificationBean")
public class MobileNotificationBean {

	private int oriid;
	private Integer noticeid;
	private String projNo;
	private String trialKey;
	private String schedulerInfoUid;
	private String typecode;
	private String ncontent;
	private String statcode;
	private String startDt;
	private String finishdt;
	private String deleteyn;
	private String authorid;
	private String createdt;
	private String editorid;
	private String updatedt;
	
	public int getOriid() {
		return oriid;
	}
	public void setOriid(int oriid) {
		this.oriid = oriid;
	}
	public Integer getNoticeid() {
		return noticeid;
	}
	public void setNoticeid(Integer noticeid) {
		this.noticeid = noticeid;
	}
	public String getProjNo() {
		return projNo;
	}
	public void setProjNo(String projNo) {
		this.projNo = projNo;
	}
	public String getTrialKey() {
		return trialKey;
	}
	public void setTrialKey(String trialKey) {
		this.trialKey = trialKey;
	}
	public String getSchedulerInfoUid() {
		return schedulerInfoUid;
	}
	public void setSchedulerInfoUid(String schedulerInfoUid) {
		this.schedulerInfoUid = schedulerInfoUid;
	}
	public String getTypecode() {
		return typecode;
	}
	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}
	public String getNcontent() {
		return ncontent;
	}
	public void setNcontent(String ncontent) {
		this.ncontent = ncontent;
	}
	public String getStatcode() {
		return statcode;
	}
	public void setStatcode(String statcode) {
		this.statcode = statcode;
	}
	public String getStartDt() {
		return startDt;
	}
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
	public String getFinishdt() {
		return finishdt;
	}
	public void setFinishdt(String finishdt) {
		this.finishdt = finishdt;
	}
	public String getDeleteyn() {
		return deleteyn;
	}
	public void setDeleteyn(String deleteyn) {
		this.deleteyn = deleteyn;
	}
	public String getAuthorid() {
		return authorid;
	}
	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}
	public String getCreatedt() {
		return createdt;
	}
	public void setCreatedt(String createdt) {
		this.createdt = createdt;
	}
	public String getEditorid() {
		return editorid;
	}
	public void setEditorid(String editorid) {
		this.editorid = editorid;
	}
	public String getUpdatedt() {
		return updatedt;
	}
	public void setUpdatedt(String updatedt) {
		this.updatedt = updatedt;
	}
}
