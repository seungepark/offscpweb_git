package com.ssshi.scpoff.mobile.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 모바일 승선자 승하선 정보 DTO (SCHECREWINOUT 테이블)
 */
public class MobileCrewInOutBean {
    
    // 기본 정보
    @JsonProperty("uid")
    private Long uid;                    // UID (승하선 고유 ID)
    @JsonProperty("project")
    private String project;              // PROJECT (호선번호)
    @JsonProperty("userUid")
    private Long userUid;                // SCHECREWUID (승선자정보UID) - userUid로 변경
    @JsonProperty("trialKey")
    private String trialKey;             // TRIALKEY (SCP 스케쥴 KEY)
    @JsonProperty("schedulerInfoUid")
    private Long schedulerInfoUid;       // SCHEDULERINFOUID (스케쥴UID)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Long schedulerCrewInfoUid;   // SCHEDULERINFOUID (스케쥴UID) - body 입력용
    @JsonProperty("inOutDate")
    private String inOutDate;           // INOUTDATE (일자)
    @JsonProperty("barkingDate")
    private String barkingDate;          // INOUTDATE (일자) - body 입력용
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String schedulerInOut;      // SCHEDULERINOUT (계획승하선)
    @JsonProperty("performanceInOut")
    private String performanceInOut;    // PERFORMANCEINOUT (실적승하선)
    @JsonProperty("barkingCode")
    private String barkingCode;          // PERFORMANCEINOUT (실적승하선) - body 입력용
    
    // 모바일 관련 정보
    @JsonProperty("qrYn")
    private String qrYn;                // QR_YN (QR 상태: Y: QR인식, N: QR분실, I: 신규등록)
    @JsonProperty("androidId")
    private String androidId;           // ANDROID_ID (Android 기기 ID)
    
    // 관리 정보
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Long insertBy;              // INSERTBY (생성자) - 서버에서 1로 설정
    @JsonProperty("insertDate")
    private String insertDate;          // INSERTDATE (생성일) - body에서 받음
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Long updateBy;              // UPDATEBY (수정자) - 서버에서 1로 설정
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String updateDate;          // UPDATEDATE (수정일) - 서버에서 NOW()로 설정
    
    // 기본 생성자
    public MobileCrewInOutBean() {
        System.out.println("=== MobileCrewInOutBean 객체가 생성되었습니다! ===");
    }
    
    // Getters and Setters
    public Long getUid() {
        return uid;
    }
    
    public void setUid(Long uid) {
        this.uid = uid;
    }
    
    public Long getUserUid() {
        return userUid;
    }
    
    public void setUserUid(Long userUid) {
        this.userUid = userUid;
    }
    
    public String getProject() {
        return project;
    }
    
    public void setProject(String project) {
        this.project = project;
    }
    
    public String getTrialKey() {
        return trialKey;
    }
    
    public void setTrialKey(String trialKey) {
        this.trialKey = trialKey;
    }
    
    public Long getSchedulerInfoUid() {
        return schedulerInfoUid;
    }
    
    public void setSchedulerInfoUid(Long schedulerInfoUid) {
        this.schedulerInfoUid = schedulerInfoUid;
    }
    
    public Long getSchedulerCrewInfoUid() {
        return schedulerCrewInfoUid;
    }
    
    public void setSchedulerCrewInfoUid(Long schedulerCrewInfoUid) {
        this.schedulerCrewInfoUid = schedulerCrewInfoUid;
    }
    
    public String getInOutDate() {
        return inOutDate;
    }
    
    public void setInOutDate(String inOutDate) {
        this.inOutDate = inOutDate;
    }
    
    public String getBarkingDate() {
        return barkingDate;
    }
    
    public void setBarkingDate(String barkingDate) {
        this.barkingDate = barkingDate;
    }
    
    public String getSchedulerInOut() {
        return schedulerInOut;
    }
    
    public void setSchedulerInOut(String schedulerInOut) {
        this.schedulerInOut = schedulerInOut;
    }
    
    public String getPerformanceInOut() {
        return performanceInOut;
    }
    
    public void setPerformanceInOut(String performanceInOut) {
        this.performanceInOut = performanceInOut;
    }
    
    public String getBarkingCode() {
        return barkingCode;
    }
    
    public void setBarkingCode(String barkingCode) {
        this.barkingCode = barkingCode;
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
    
    public Long getInsertBy() {
        return insertBy;
    }
    
    public void setInsertBy(Long insertBy) {
        this.insertBy = insertBy;
    }
    
    public String getInsertDate() {
        return insertDate;
    }
    
    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }
    
    public Long getUpdateBy() {
        return updateBy;
    }
    
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }
    
    public String getUpdateDate() {
        return updateDate;
    }
    
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
    
    
    @Override
    public String toString() {
        return "MobileCrewInOutBean{" +
                "uid=" + uid +
                ", project='" + project + '\'' +
                ", userUid=" + userUid +
                ", trialKey='" + trialKey + '\'' +
                ", schedulerInfoUid=" + schedulerInfoUid +
                ", schedulerCrewInfoUid=" + schedulerCrewInfoUid +
                ", inOutDate='" + inOutDate + '\'' +
                ", barkingDate='" + barkingDate + '\'' +
                ", schedulerInOut='" + schedulerInOut + '\'' +
                ", performanceInOut='" + performanceInOut + '\'' +
                ", barkingCode='" + barkingCode + '\'' +
                ", qrYn='" + qrYn + '\'' +
                ", androidId='" + androidId + '\'' +
                ", insertBy=" + insertBy +
                ", insertDate=" + insertDate +
                ", updateBy=" + updateBy +
                ", updateDate=" + updateDate +
                '}';
    }
} 