package com.ssshi.scpoff.mobile.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 모바일 구명정배정 인원 명단 DTO
 * mobile_lift_boat_crewinfo 테이블과 매핑
 */
public class MobileLiftBoatCrewInfoBean implements Serializable {
    
    private Long uid;                    // 고유 ID
    private String projNo;               // 호선
    private Long schedulerInfoUid;       // 스케쥴UID
    private String trialKey;             // 스케쥴KEY값 schedulerinfo.TRIALKEY
 //   private Long schcrewUid;             // 스케쥴KEY값 schedulerinfo.TRIALKEY
    private String boatName;             // 배정보트명
    private Integer userUid;             // 사용자 UID (시스템 연동용)
    private String role;                 // OP : Operator, M : Manager (인원점검), L : Launcing, C : Crew (기타 탑승자)
    private String androidId;            // device Android ID
    private Timestamp insertDate;        // 저장일자
    private Integer insertBy;            // 생성자
    private String regDevice;            // 등록 디바이스 WEB:웹사이트, MW:모바일

    public MobileLiftBoatCrewInfoBean() {}

    public Long getUid() {
        return uid;
    }
    
    public void setUid(Long uid) {
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
 
    
    public String getBoatName() {
        return boatName;
    }
    
    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }
    
    public Integer getUserUid() {
        return userUid;
    }
    
    public void setUserUid(Integer userUid) {
        this.userUid = userUid;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getAndroidId() {
        return androidId;
    }
    
    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getInsertDate() {
        return insertDate;
    }
    
    public void setInsertDate(Timestamp insertDate) {
        this.insertDate = insertDate;
    }
    
    public Integer getInsertBy() {
        return insertBy;
    }
    
    public void setInsertBy(Integer insertBy) {
        this.insertBy = insertBy;
    }
    
    public String getRegDevice() {
        return regDevice;
    }
    
    public void setRegDevice(String regDevice) {
        this.regDevice = regDevice;
    }

    @Override
    public String toString() {
        return "MobileLiftBoatCrewInfoBean{" +
                "uid=" + uid +
                ", projNo='" + projNo + '\'' +
                ", schedulerInfoUid=" + schedulerInfoUid +
                ", trialKey='" + trialKey + '\'' + 
                ", boatName='" + boatName + '\'' +
                ", userUid=" + userUid +
                ", role='" + role + '\'' +
                ", androidId='" + androidId + '\'' +
                ", insertDate=" + insertDate +
                ", insertBy=" + insertBy +
                ", regDevice='" + regDevice + '\'' +
                '}';
    }
}
