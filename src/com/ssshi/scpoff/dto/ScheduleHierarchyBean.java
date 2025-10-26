package com.ssshi.scpoff.dto;

public class ScheduleHierarchyBean {
	
	private int uid;
	private String code;
	private String displaycode;	
	private String description; 	
	private int codelevel;	
	private String parentuid;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDisplaycode() {
		return displaycode;
	}
	public void setDisplaycode(String displaycode) {
		this.displaycode = displaycode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCodelevel() {
		return codelevel;
	}
	public void setCodelevel(int codelevel) {
		this.codelevel = codelevel;
	}
	public String getParentuid() {
		return parentuid;
	}
	public void setParentuid(String parentuid) {
		this.parentuid = parentuid;
	} 	
}
