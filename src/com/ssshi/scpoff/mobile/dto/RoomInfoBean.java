package com.ssshi.scpoff.mobile.dto;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 방배정 정보 DTO
 */
public class RoomInfoBean {
    
    private int uid;
    private String projNo;
    private Integer schedulerInfoUid;
    private String trialKey;
    private String deck;
    private String roomNo;
    private String roomName;
    private String tel;
    private String sizeM2;
    private int bedCount;
    private String bathroomYn;
    private String status;
    private String delYn;
    private String androidId;
    private String mobileDeviceId;
    private Timestamp insertDate;
    private String regUsername;
    private Timestamp updateDate;
    private String updateUsername;
    
    /**
     * 기본 생성자
     */
    public RoomInfoBean() {
    }
    
    /**
     * 생성자
     * @param uid 방 정보 UID
     * @param projNo 프로젝트 번호
     * @param schedulerInfoUid 스케줄러 정보 UID
     * @param trialKey 스케줄 키
     * @param deck 갑판
     * @param roomNo 방 번호
     * @param roomName 방 이름
     * @param tel 전화번호
     * @param sizeM2 크기(제곱미터)
     * @param bedCount 침대 수
     * @param bathroomYn 욕실 여부
     * @param status 상태
     * @param delYn 삭제여부
     * @param androidId 안드로이드 기기 ID
     * @param mobileDeviceId 모바일 기기 ID
     * @param insertDate 등록 날짜
     * @param regUsername 등록 사용자명
     * @param updateDate 수정 날짜
     * @param updateUsername 수정 사용자명
     */
    public RoomInfoBean(int uid, String projNo, Integer schedulerInfoUid, String trialKey, String deck, String roomNo, 
                       String roomName, String tel, String sizeM2, int bedCount, String bathroomYn, 
                       String status, String delYn, String androidId, String mobileDeviceId,
                       Timestamp insertDate, String regUsername, Timestamp updateDate, String updateUsername) {
        this.uid = uid;
        this.projNo = projNo;
        this.schedulerInfoUid = schedulerInfoUid;
        this.trialKey = trialKey;
        this.deck = deck;
        this.roomNo = roomNo;
        this.roomName = roomName;
        this.tel = tel;
        this.sizeM2 = sizeM2;
        this.bedCount = bedCount;
        this.bathroomYn = bathroomYn;
        this.status = status;
        this.delYn = delYn;
        this.androidId = androidId;
        this.mobileDeviceId = mobileDeviceId;
        this.insertDate = insertDate;
        this.regUsername = regUsername;
        this.updateDate = updateDate;
        this.updateUsername = updateUsername;
    }
    
    // Getter and Setter methods
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

    public String getDeck() {
        return deck;
    }
    
    public void setDeck(String deck) {
        this.deck = deck;
    }
    
    public String getRoomNo() {
        return roomNo;
    }
    
    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
    
    public String getRoomName() {
        return roomName;
    }
    
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    
    public String getTel() {
        return tel;
    }
    
    public void setTel(String tel) {
        this.tel = tel;
    }
    
    public String getSizeM2() {
        return sizeM2;
    }
    
    public void setSizeM2(String sizeM2) {
        this.sizeM2 = sizeM2;
    }
    
    public int getBedCount() {
        return bedCount;
    }
    
    public void setBedCount(int bedCount) {
        this.bedCount = bedCount;
    }
    
    public String getBathroomYn() {
        return bathroomYn;
    }
    
    public void setBathroomYn(String bathroomYn) {
        this.bathroomYn = bathroomYn;
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
    
    @Override
    public String toString() {
        return "RoomInfoBean{" +
                "uid=" + uid +
                ", projNo='" + projNo + '\'' +
                ", schedulerInfoUid='" + schedulerInfoUid + '\'' +
                ", trialKey='" + trialKey + '\'' +
                ", deck='" + deck + '\'' +
                ", roomNo='" + roomNo + '\'' +
                ", roomName='" + roomName + '\'' +
                ", tel='" + tel + '\'' +
                ", sizeM2='" + sizeM2 + '\'' +
                ", bedCount=" + bedCount +
                ", bathroomYn='" + bathroomYn + '\'' +
                ", status='" + status + '\'' +
                ", delYn='" + delYn + '\'' +
                ", androidId='" + androidId + '\'' +
                ", mobileDeviceId='" + mobileDeviceId + '\'' +
                ", insertDate='" + insertDate + '\'' +
                ", regUsername='" + regUsername + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", updateUsername='" + updateUsername + '\'' +
                '}';
    }
}
