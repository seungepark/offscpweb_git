package com.ssshi.scpoff.mobile.dto;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 방배정 명단 정보 DTO
 * mobile_room_userlist 테이블과 매핑
 */
public class RoomUserListBean {
    
    private long uid;
    private Long oriUid;  // 원본 UID (INSERT 시 0, UPDATE 시 uid와 동일)
    private String projNo;
    private Integer schedulerInfoUid;
    private String trialKey;  // 스케줄 키 (필수)
    private String roomNo;
    private String department;
    private String name;
    private String phone;
    private Integer userUid;
    private String checkInDate;
    private String checkOutDate;
    private String status;
    private String delYn;
    private String androidId;
    private String mobileDeviceId;
    private Timestamp insertDate;
    private String regUsername;
    private Timestamp updateDate;
    private String updateUsername;
    private String remark;
    
    /**
     * 기본 생성자
     */
    public RoomUserListBean() {
    }
    
    /**
     * 생성자
     * @param uid 고유 ID
     * @param projNo 호선
     * @param schedulerInfoUid 스케줄 정보 UID
     * @param roomNo 방번호
     * @param department 부서
     * @param name 이름
     * @param phone 휴대폰번호
     * @param userUid 사용자 UID
     * @param checkInDate 방사용 시작일
     * @param checkOutDate 방사용 종료일
     * @param status 상태
     * @param delYn 삭제여부
     * @param androidId Android 기기 ID
     * @param mobileDeviceId 모바일 기기 ID
     * @param insertDate 등록일시
     * @param regUsername 등록자
     * @param updateDate 수정일시
     * @param updateUsername 수정자
     */
    public RoomUserListBean(long uid, String projNo, Integer schedulerInfoUid, String trialKey, String roomNo, 
                           String department, String name, String phone, Integer userUid, 
                           String checkInDate, String checkOutDate, String status, String delYn, 
                           String androidId, String mobileDeviceId,
                           Timestamp insertDate, String regUsername, Timestamp updateDate, String updateUsername,
                           String remark) {
        this.uid = uid;
        this.projNo = projNo;
        this.schedulerInfoUid = schedulerInfoUid;
        this.trialKey = trialKey;
        this.roomNo = roomNo;
        this.department = department;
        this.name = name;
        this.phone = phone;
        this.userUid = userUid;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
        this.delYn = delYn;
        this.androidId = androidId;
        this.mobileDeviceId = mobileDeviceId;
        this.insertDate = insertDate;
        this.regUsername = regUsername;
        this.updateDate = updateDate;
        this.updateUsername = updateUsername;
        this.remark = remark;
    }

    public RoomUserListBean(long uid, String projNo, Integer schedulerInfoUid, String trialKey, String roomNo, 
                           String department, String name, String phone, Integer userUid, 
                           String checkInDate, String checkOutDate, String status, String delYn, 
                           String androidId, String mobileDeviceId,
                           Timestamp insertDate, String regUsername, Timestamp updateDate, String updateUsername) {
        this(uid, projNo, schedulerInfoUid, trialKey, roomNo, department, name, phone, userUid,
                checkInDate, checkOutDate, status, delYn, androidId, mobileDeviceId,
                insertDate, regUsername, updateDate, updateUsername, null);
    }
    
    // Getter and Setter methods
    public long getUid() {
        return uid;
    }
    
    public void setUid(long uid) {
        this.uid = uid;
    }
    
    public Long getOriUid() {
        return oriUid;
    }
    
    public void setOriUid(Long oriUid) {
        this.oriUid = oriUid;
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
    
    public String getRoomNo() {
        return roomNo;
    }
    
    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Integer getUserUid() {
        return userUid;
    }
    
    public void setUserUid(Integer userUid) {
        this.userUid = userUid;
    }
    
    public String getCheckInDate() {
        return checkInDate;
    }
    
    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }
    
    public String getCheckOutDate() {
        return checkOutDate;
    }
    
    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDelYn() {
        return delYn;
    }
    
    public void setDelYn(String delYn) {
        this.delYn = delYn;
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
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getInsertDate() {
        return insertDate;
    }
    
    public void setInsertDate(Timestamp insertDate) {
        this.insertDate = insertDate;
    }
    
    public String getRegUsername() {
        return regUsername;
    }
    
    public void setRegUsername(String regUsername) {
        this.regUsername = regUsername;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getUpdateDate() {
        return updateDate;
    }
    
    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }
    
    public String getUpdateUsername() {
        return updateUsername;
    }
    
    public void setUpdateUsername(String updateUsername) {
        this.updateUsername = updateUsername;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    @Override
    public String toString() {
        return "RoomUserListBean{" +
                "uid=" + uid +
                ", oriUid=" + oriUid +
                ", projNo='" + projNo + '\'' +
                ", schedulerInfoUid='" + schedulerInfoUid + '\'' +
                ", trialKey='" + trialKey + '\'' +
                ", roomNo='" + roomNo + '\'' +
                ", department='" + department + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", userUid=" + userUid +
                ", checkInDate='" + checkInDate + '\'' +
                ", checkOutDate='" + checkOutDate + '\'' +
                ", status='" + status + '\'' +
                ", delYn='" + delYn + '\'' +
                ", androidId='" + androidId + '\'' +
                ", mobileDeviceId='" + mobileDeviceId + '\'' +
                ", insertDate='" + insertDate + '\'' +
                ", regUsername='" + regUsername + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", updateUsername='" + updateUsername + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}

