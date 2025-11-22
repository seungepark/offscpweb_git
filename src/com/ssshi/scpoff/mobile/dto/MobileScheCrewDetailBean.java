package com.ssshi.scpoff.mobile.dto;

import org.apache.ibatis.type.Alias;

@Alias("mobileScheCrewDetailBean")
public class MobileScheCrewDetailBean {

	private int uid;
	private int scheCrewUid;
	private int schedulerInfoUid;
	private String role1;
	private String role2;
	private String terminal;
	private String notebook;
	private String modelName;
	private String serialNumber;
	private String foreigner;
	private String passportNumber;
	private String orderStatus;
	private String orderDate;
	private String orderUid;
	private String deleteYn;
	private String deleteDate;
	private String deleteUid;
	private String qrYn;
	private String regDate;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getScheCrewUid() {
		return scheCrewUid;
	}
	public void setScheCrewUid(int scheCrewUid) {
		this.scheCrewUid = scheCrewUid;
	}
	public int getSchedulerInfoUid() {
		return schedulerInfoUid;
	}
	public void setSchedulerInfoUid(int schedulerInfoUid) {
		this.schedulerInfoUid = schedulerInfoUid;
	}
	public String getRole1() {
		return role1;
	}
	public void setRole1(String role1) {
		this.role1 = role1;
	}
	public String getRole2() {
		return role2;
	}
	public void setRole2(String role2) {
		this.role2 = role2;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getNotebook() {
		return notebook;
	}
	public void setNotebook(String notebook) {
		this.notebook = notebook;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getForeigner() {
		return foreigner;
	}
	public void setForeigner(String foreigner) {
		this.foreigner = foreigner;
	}
	public String getPassportNumber() {
		return passportNumber;
	}
	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderUid() {
		return orderUid;
	}
	public void setOrderUid(String orderUid) {
		this.orderUid = orderUid;
	}
	public String getDeleteYn() {
		return deleteYn;
	}
	public void setDeleteYn(String deleteYn) {
		this.deleteYn = deleteYn;
	}
	public String getDeleteDate() {
		return deleteDate;
	}
	public void setDeleteDate(String deleteDate) {
		this.deleteDate = deleteDate;
	}
	public String getDeleteUid() {
		return deleteUid;
	}
	public void setDeleteUid(String deleteUid) {
		this.deleteUid = deleteUid;
	}
	public String getQrYn() {
		return qrYn;
	}
	public void setQrYn(String qrYn) {
		this.qrYn = qrYn;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
}

