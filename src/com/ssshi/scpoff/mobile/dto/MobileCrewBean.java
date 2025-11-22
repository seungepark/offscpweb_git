package com.ssshi.scpoff.mobile.dto;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 모바일 승선자 정보 DTO 
 */
public class MobileCrewBean {
    
    // 기본 정보
    @JsonProperty("uid")
    private Long uid;                    // 승선자 고유 ID (DB UID)
    @JsonProperty("MUid")
    private Long mUid;                   // 태블릿의 schCrewUID (body에서 받은 값)
    @JsonProperty("schedulerInfoUid")
    private int schedulerInfoUid;       // 스케줄러 정보 UID
    @JsonProperty("kind")
    private String kind;                // 승선자 종류 (선원, 승객 등)
    @JsonProperty("company")
    private String company;             // 소속 회사
    @JsonProperty("department")
    private String department;          // 부서
    @JsonProperty("name")
    private String name;                // 이름
    @JsonProperty("rank")
    private String rank;                // 직급/계급
    @JsonProperty("idNo")
    private String idNo;                // 주민번호/여권번호
    @JsonProperty("workType1")
    private String workType1;           // 작업 유형 1
    @JsonProperty("workType2")
    private String workType2;           // 작업 유형 2
    @JsonProperty("mainSub")
    private String mainSub;             // 주/부 구분
    @JsonProperty("foodStyle")
    private String foodStyle;           // 식사 스타일
    @JsonProperty("personNo")
    private String personNo;            // 주민번호
    @JsonProperty("gender")
    private String gender;              // 성별
    @JsonProperty("phone")
    private String phone;               // 연락처
    @JsonProperty("trialKey")
    private String trialKey;            // SCP 스케줄 KEY
    @JsonProperty("project")
    private String project;             // 호선번호
    @JsonProperty("projNo")
    private String projNo;              // 호선번호 (projNo로도 매핑)
    
    // 상태 정보
    @JsonProperty("isPlan")
    private String isPlan;              // 계획 여부 (Y/N)
    private int userUid;                // 사용자 UID
    private int cnt;                    // 카운트
    private String isBoard;             // 승선 여부 (Y/N)
    private String isUnboard;           // 하선 여부 (Y/N)
    private int diff;                   // 차이값
    private String isNone;              // 없음 여부 (Y/N)
    
    // 승하선 날짜 정보 (SCHECREWINOUT 테이블에서 조인)
    @JsonIgnore
    private String planIndate;          // 계획 승선일
    @JsonIgnore
    private String planOutdate;         // 계획 하선일
    @JsonIgnore
    private String performanceIndate;   // 실적 승선일
    @JsonIgnore
    private String performanceOutdate;  // 실적 하선일
    
    // QR 관련 정보 (SCHECREWINOUT 테이블에서 조인)
    @JsonIgnore
    private String qrYn;                // QR 상태 (Y: QR인식, N: QR분실, I: 신규등록)
    @JsonProperty("androidId")
    private String androidId;           // Android 기기 ID
    
    // 관리 정보
    @JsonProperty("insertBy")
    private int insertBy;               // 등록자
    @JsonProperty("insertDate")
    private Timestamp insertDate;       // 등록일시
    @JsonProperty("updateBy")
    private int updateBy;               // 수정자
    @JsonProperty("updateDate")
    private Timestamp updateDate;       // 수정일시
    @JsonProperty("orgUid")
    private Long orgUid;                 // 온라인 SCP DB ID (태블릿에서 신규 등록시 null, 업데이트시 값있음)
    private String revKind;             // 수정 종류
    private int revDay;                 // 수정 일수
    
    // 기본 생성자
    public MobileCrewBean() {
        System.out.println("=== MobileCrewBean 객체가 생성되었습니다! ===");
    }
    
    // Getters and Setters
    
    
    public Long getUid() {
        return uid;
    }
    
    public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

    public void setUid(Long uid) {
        this.uid = uid;
    }
    
    public Long getMUid() {
        return mUid;
    }
    
    public void setMUid(Long mUid) {
        this.mUid = mUid;
    }
    
    public int getSchedulerInfoUid() {
        return schedulerInfoUid;
    }
    
    public void setSchedulerInfoUid(int schedulerInfoUid) {
        this.schedulerInfoUid = schedulerInfoUid;
    }
    
    public String getKind() {
        return kind;
    }
    
    public void setKind(String kind) {
        this.kind = kind;
    }
    
    public String getCompany() {
        return company;
    }
    
    public void setCompany(String company) {
        this.company = company;
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
    
    public String getRank() {
        return rank;
    }
    
    public void setRank(String rank) {
        this.rank = rank;
    }
    
    public String getIdNo() {
        return idNo;
    }
    
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
    
    public String getWorkType1() {
        return workType1;
    }
    
    public void setWorkType1(String workType1) {
        this.workType1 = workType1;
    }
    
    public String getWorkType2() {
        return workType2;
    }
    
    public void setWorkType2(String workType2) {
        this.workType2 = workType2;
    }
    
    public String getMainSub() {
        return mainSub;
    }
    
    public void setMainSub(String mainSub) {
        this.mainSub = mainSub;
    }
    
    public String getFoodStyle() {
        return foodStyle;
    }
    
    public void setFoodStyle(String foodStyle) {
        this.foodStyle = foodStyle;
    }
    
    public String getPersonNo() {
        return personNo;
    }
    
    public void setPersonNo(String personNo) {
        this.personNo = personNo;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getTrialKey() {
        return trialKey;
    }
    
    public void setTrialKey(String trialKey) {
        this.trialKey = trialKey;
    }
    
    public String getProject() {
        return project;
    }
    
    public void setProject(String project) {
        this.project = project;
    }
    
    public String getProjNo() {
        return projNo;
    }
    
    public void setProjNo(String projNo) {
        this.projNo = projNo;
    }
    
    
    public String getIsPlan() {
        return isPlan;
    }
    
    public void setIsPlan(String isPlan) {
        this.isPlan = isPlan;
    }
    
    public int getUserUid() {
        return userUid;
    }
    
    public void setUserUid(int userUid) {
        this.userUid = userUid;
    }
    
    public int getCnt() {
        return cnt;
    }
    
    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
    
    public String getIsBoard() {
        return isBoard;
    }
    
    public void setIsBoard(String isBoard) {
        this.isBoard = isBoard;
    }
    
    public String getIsUnboard() {
        return isUnboard;
    }
    
    public void setIsUnboard(String isUnboard) {
        this.isUnboard = isUnboard;
    }
    
    public int getDiff() {
        return diff;
    }
    
    public void setDiff(int diff) {
        this.diff = diff;
    }
    
    public String getIsNone() {
        return isNone;
    }
    
    public void setIsNone(String isNone) {
        this.isNone = isNone;
    }
    
    public String getPlanIndate() {
        return planIndate;
    }
    
    public void setPlanIndate(String planIndate) {
        this.planIndate = planIndate;
    }
    
    public String getPlanOutdate() {
        return planOutdate;
    }
    
    public void setPlanOutdate(String planOutdate) {
        this.planOutdate = planOutdate;
    }
    
    public String getPerformanceIndate() {
        return performanceIndate;
    }
    
    public void setPerformanceIndate(String performanceIndate) {
        this.performanceIndate = performanceIndate;
    }
    
    public String getPerformanceOutdate() {
        return performanceOutdate;
    }
    
    public void setPerformanceOutdate(String performanceOutdate) {
        this.performanceOutdate = performanceOutdate;
    }
    
    public int getInsertBy() {
        return insertBy;
    }
    
    public void setInsertBy(int insertBy) {
        this.insertBy = insertBy;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getInsertDate() {
        return insertDate;
    }
    
    public void setInsertDate(Timestamp insertDate) {
        this.insertDate = insertDate;
    }
    
    public int getUpdateBy() {
        return updateBy;
    }
    
    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getUpdateDate() {
        return updateDate;
    }
    
    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }
    
    public Long getOrgUid() {
        return orgUid;
    }
    
    public void setOrgUid(Long orgUid) {
        this.orgUid = orgUid;
    }
    
    public String getRevKind() {
        return revKind;
    }
    
    public void setRevKind(String revKind) {
        this.revKind = revKind;
    }
    
    public int getRevDay() {
        return revDay;
    }
    
    public void setRevDay(int revDay) {
        this.revDay = revDay;
    }
    
    // QR 관련 정보 getter/setter
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
    
    @Override
    public String toString() {
        return "MobileCrewBean{" +
                "uid=" + uid +
                ", mUid=" + mUid +
                ", schedulerInfoUid=" + schedulerInfoUid +
                ", kind='" + kind + '\'' +
                ", company='" + company + '\'' +
                ", department='" + department + '\'' +
                ", name='" + name + '\'' +
                ", rank='" + rank + '\'' +
                ", idNo='" + idNo + '\'' +
                ", workType1='" + workType1 + '\'' +
                ", workType2='" + workType2 + '\'' +
                ", mainSub='" + mainSub + '\'' +
                ", foodStyle='" + foodStyle + '\'' +
                ", personNo='" + personNo + '\'' +
                ", phone='" + phone + '\'' +
                ", trialKey='" + trialKey + '\'' +
                ", project='" + project + '\'' +
                ", projNo='" + projNo + '\'' +
                ", gender='" + gender + '\'' +
                ", isPlan='" + isPlan + '\'' +
                ", userUid=" + userUid +
                ", cnt=" + cnt +
                ", isBoard='" + isBoard + '\'' +
                ", isUnboard='" + isUnboard + '\'' +
                ", diff=" + diff +
                ", isNone='" + isNone + '\'' +
                ", planIndate='" + planIndate + '\'' +
                ", planOutdate='" + planOutdate + '\'' +
                ", performanceIndate='" + performanceIndate + '\'' +
                ", performanceOutdate='" + performanceOutdate + '\'' +
                ", qrYn='" + qrYn + '\'' +
                ", androidId='" + androidId + '\'' +
                ", insertBy=" + insertBy +
                ", insertDate=" + insertDate +
                ", updateBy=" + updateBy +
                ", updateDate=" + updateDate +
                ", orgUid=" + orgUid +
                ", revKind='" + revKind + '\'' +
                ", revDay=" + revDay +
                '}';
    }
} 