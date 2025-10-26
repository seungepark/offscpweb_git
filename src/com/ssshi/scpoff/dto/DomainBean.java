package com.ssshi.scpoff.dto;

import java.util.List;

public class DomainBean {

	private int uid;
	private int shipInfoUid;
	private String domain;
	private String description;
	private String dataType;
	private String cat;
	private String status;
	private String insertDate;
	private int insertBy;
	private String updateDate;
	private int updateBy;
	
	private String[] inValList;
	private String[] valList;
	private String[] descList;
	private int[] uidList;
	private int[] delList;
	private String[] delValList;
	private String val;
	private int userUid;
	private List<DomainInfoBean> infoList;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getCat() {
		return cat;
	}
	public void setCat(String cat) {
		this.cat = cat;
	}
	public int getShipInfoUid() {
		return shipInfoUid;
	}
	public void setShipInfoUid(int shipInfoUid) {
		this.shipInfoUid = shipInfoUid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String[] getInValList() {
		return inValList;
	}
	public void setInValList(String[] inValList) {
		this.inValList = inValList;
	}
	public String[] getValList() {
		return valList;
	}
	public void setValList(String[] valList) {
		this.valList = valList;
	}
	public String[] getDescList() {
		return descList;
	}
	public void setDescList(String[] descList) {
		this.descList = descList;
	}
	public int[] getUidList() {
		return uidList;
	}
	public void setUidList(int[] uidList) {
		this.uidList = uidList;
	}
	public int[] getDelList() {
		return delList;
	}
	public void setDelList(int[] delList) {
		this.delList = delList;
	}
	public String[] getDelValList() {
		return delValList;
	}
	public void setDelValList(String[] delValList) {
		this.delValList = delValList;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public int getUserUid() {
		return userUid;
	}
	public void setUserUid(int userUid) {
		this.userUid = userUid;
	}
	public List<DomainInfoBean> getInfoList() {
		return infoList;
	}
	public void setInfoList(List<DomainInfoBean> infoList) {
		this.infoList = infoList;
	}
}
