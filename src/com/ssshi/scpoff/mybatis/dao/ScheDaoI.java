package com.ssshi.scpoff.mybatis.dao;

import java.util.List;

import com.ssshi.scpoff.dto.DomainBean;
import com.ssshi.scpoff.dto.DomainInfoBean;
import com.ssshi.scpoff.dto.ParamBean;
import com.ssshi.scpoff.dto.ScheCrewBean;
import com.ssshi.scpoff.dto.ScheCrewInOutBean;
import com.ssshi.scpoff.dto.ScheCrewListBean;
import com.ssshi.scpoff.dto.ScheMailBean;
import com.ssshi.scpoff.dto.ScheMailLogBean;
import com.ssshi.scpoff.dto.ScheReportCompBean;
import com.ssshi.scpoff.dto.ScheReportDailyBean;
import com.ssshi.scpoff.dto.ScheReportDepartureBean;
import com.ssshi.scpoff.dto.ScheTcNoteBean;
import com.ssshi.scpoff.dto.ScheTcNoteFileInfoBean;
import com.ssshi.scpoff.dto.ScheTcNoteTcInfoBean;
import com.ssshi.scpoff.dto.ScheTrialInfoBean;
import com.ssshi.scpoff.dto.ScheduleBean;
import com.ssshi.scpoff.dto.ScheduleCodeDetailBean;
import com.ssshi.scpoff.dto.ScheduleCodeInfoBean;
import com.ssshi.scpoff.dto.ScheduleHierarchyBean;
import com.ssshi.scpoff.dto.SchedulerDetailInfoBean;
import com.ssshi.scpoff.dto.SchedulerInfoBean;
import com.ssshi.scpoff.dto.SchedulerVersionInfoBean;
import com.ssshi.scpoff.dto.ShipCondBean;
import com.ssshi.scpoff.dto.ShipInfoBean;
import com.ssshi.scpoff.dto.UserInfoBean;
import com.ssshi.scpoff.dto.VesselReqInfoBean;
import com.ssshi.scpoff.dto.VesselReqInfoDetailBean;

/********************************************************************************
 * 프로그램 개요 : Sche
 * 
 * 최초 작성자 : KHJ
 * 최초 작성일 : 2024-07-17
 * 
 * 최종 수정자 : KHJ
 * 최종 수정일 : 2025-05-12
 * 
 * 메모 : 없음
 * 
 * Copyright 2024 by SiriusB. Confidential and proprietary information
 * This document contains information, which is the property of SiriusB, 
 * and is furnished for the sole purpose of the operation and the maintenance.  
 * Copyright © 2024 SiriusB.  All rights reserved.
 *
 ********************************************************************************/

public interface ScheDaoI {
	
	// --- DbDaoI ---
	List<DomainInfoBean> getDomainInfoListByDomainID(String domain) throws Exception;
	
	// --- ManagerDaoI ---
	int updateScheduler(SchedulerInfoBean bean) throws Exception;
	
	int insertSchedulerDetail(SchedulerDetailInfoBean bean) throws Exception;
	
	int updateSchedulerDetail(SchedulerDetailInfoBean bean) throws Exception;
	
	int deleteSchedulerDetail(int uid) throws Exception;
	
	// 실적관리 목록. (ManagerDaoI.getSchedulerDepartureList)
	List<SchedulerInfoBean> getScheList(SchedulerInfoBean bean) throws Exception;

	// 실적관리 목록 개수. (ManagerDaoI.getSchedulerDepartureListCnt)
	int getScheListCnt() throws Exception;
	
	// 시운전 상태 (계획, 결과)
	String getTrialStatus(int schedulerInfoUid) throws Exception;
	
	// 승선자 목록 (계획, 결과).
	List<ScheCrewBean> getCrewList(int schedulerInfoUid) throws Exception;
	
	// 승선자별 승하선 정보 (계획, 결과).
	List<ScheCrewInOutBean> getCrewInOutList(int scheCrewUid) throws Exception;
	
	// 승선자 UID 목록 (계획).
	List<Integer> getCrewUidList(int schedulerInfoUid) throws Exception;
	
	// 기존 승선자 목록 삭제 (계획).
	int deleteCrewList(int schedulerInfoUid) throws Exception;
	
	// 기존 승선자 승하선 정보 삭제 (계획, 결과).
	int deleteCrewInoutList(int scheCrewUid) throws Exception;
	
	// 승선자 입력 (계획, 결과).
	int insertCrew(ScheCrewBean bean) throws Exception;
	
	// 승선자 승하선 정보 입력 (계획, 결과).
	int insertCrewInOut(ScheCrewInOutBean bean) throws Exception;
	
	// 커맨더 정보 (계획, 결과).
	List<ScheCrewBean> getCommanderInfoList(int schedulerInfoUid) throws Exception;
	
	// 주요 승선자 정보 (계획).
	ScheCrewBean getMainCrewInfo(ScheCrewBean bean) throws Exception;
	
	// 승선자 수 정보 (계획, 결과).
	List<ScheCrewBean> getCrewCntInfoList(int schedulerInfoUid) throws Exception;
	
	// 승선자 수 정보 (결과 - 완료 보고).
	List<ScheCrewBean> getCrewCntInfoListForComp(int schedulerInfoUid) throws Exception;
	
	// 시운전 정보 (계획).
	ScheTrialInfoBean getTrialInfo(int schedulerInfoUid) throws Exception;
	
	// 시운전 항목 개수 정보 (계획, 결과).
	List<Integer> getTcCntList(int schedulerInfoUid) throws Exception;
	
	// 시리즈 선박 정보 (계획).
	List<ShipInfoBean> getSeriesList() throws Exception;
	
	// 시운전 실적 검색 (계획).
	List<SchedulerInfoBean> searchTrial(ParamBean bean) throws Exception;
	
	// 기존 시운전 정보 삭제 (계획).
	int deleteTrialInfo(int schedulerInfoUid) throws Exception;
	
	// 시운전 정보 입력 (계획).
	int insertTrialInfo(ScheTrialInfoBean bean) throws Exception;
	
	// 출항 보고서 스케줄 정보 (계획).
	ScheduleBean getScheduleDateTime(int uid) throws Exception;
	
	// 출항 보고서 시운전 실적 - 해당 시운전 계획 수행 시간 (계획).
	int getTotalPlanTime(int uid) throws Exception;
	
	// 출항/완료 보고서 시운전 실적 - 이전 시운전 실적 수행 시간 (계획, 결과).
	int getTotalTrialTime(int uid) throws Exception;
	
	// 출항/완료 보고서 시운전 실적 - Fuel 연료 실적 (계획, 결과).
	ScheReportCompBean getTrialFuel(int uid) throws Exception;
	
	// 출항 보고서 입력 (계획).
	int insertReportDeparture(ScheReportDepartureBean bean) throws Exception;
	
	// 시운전정보 시운전상태 업데이트 (계획, 결과).
	int updateTrialStatus(ScheTrialInfoBean bean) throws Exception;
	
	// 메일링 이력 입력 (계획, 결과).
	int insertMailLog(ScheMailLogBean bean) throws Exception;
	
	// 도메인 값 가져오기 (결과).
	List<DomainInfoBean> getDomainInfoList(String domain) throws Exception;
	
	// 시운전 스케줄 정보 조회 (ManagerDaoI).
	SchedulerInfoBean getScheduler(int uid) throws Exception;
	
	// 시운전 스케줄 상세 목록 조회 (ManagerDaoI)
	List<SchedulerDetailInfoBean> getSchedulerRowDataList(SchedulerDetailInfoBean bean) throws Exception;
	
	// 특이사항 목록 (결과).
	List<ScheTcNoteBean> getTcNoteList(int uid) throws Exception;
	
	// 특이사항 관련 TC 목록 (결과).
	List<ScheTcNoteTcInfoBean> getTcNoteTcList(int uid) throws Exception;
	
	// 특이사항 파일 목록 (결과).
	List<ScheTcNoteFileInfoBean> getTcNoteFileList(int uid) throws Exception;
	
	// 특이사항 신규 생성 (결과).
	int insertTcNote(ScheTcNoteBean bean) throws Exception;
	
	// 특이사항 수정 (결과).
	int updateTcNote(ScheTcNoteBean bean) throws Exception;
	
	// 특이사항 관련 TC 목록 전체 삭제 (결과).
	int deleteTcNoteTcList(int uid) throws Exception;
	
	// 특이사항 관련 TC 생성 (결과).
	int insertTcNoteTc(ScheTcNoteTcInfoBean bean) throws Exception;
	
	// 특이사항 파일 삭제 (결과).
	int deleteTcNoteFile(int uid) throws Exception;
	
	// 특이사항 파일 전체 삭제 (결과).
	int deleteTcNoteFileList(int uid) throws Exception;
	
	// 특이사항 파일 생성 (결과).
	int insertTcNoteFile(ScheTcNoteFileInfoBean bean) throws Exception;
		
	// 삭제 가능한 (계획에서 생성 X, 이전에 결과에서 생성 X) 승선자 UID 목록 (결과).
	List<Integer> getCrewUidListForDelete(ScheCrewListBean bean) throws Exception;
	
	// 승선자 삭제 (결과).
	int deleteCrew(int scheCrewUid) throws Exception;
	
	// 승선자 승하선 실적 업데이트 (결과).
	int updateCrewInOut(ScheCrewInOutBean bean) throws Exception;
	
	// 시운전 정보 (결과).
	ScheTrialInfoBean getTrialInfoForResult(int schedulerInfoUid) throws Exception;
	
	// 시운전 정보 업데이트 (결과).
	int updateTrialInfo(ScheTrialInfoBean bean) throws Exception;
	
	// 일일 보고서 존재 여부 (결과).
	Integer getExistDailyReport(int uid) throws Exception;
	
	// 마지막 일일 보고서 (결과).
	ScheReportDailyBean getLastDailyReport(int uid) throws Exception;
	
	// 일일 보고를 위한 승선자 목록 (결과).
	List<ScheCrewBean> getCrewListForDaily(int schedulerInfoUid) throws Exception;
	
	// 일일 보고를 위한 승선자별 승하선 정보 (결과).
	List<ScheCrewInOutBean> getCrewInOutListForDaily(int scheCrewUid) throws Exception;
	
	// 일일 보고서 입력 (결과).
	int insertReportDaily(ScheReportDailyBean bean) throws Exception;
	
	// 시운전정보 일일보고 후 초기화 (결과).
	int updateTrialInfoForDaily(ScheTrialInfoBean bean) throws Exception;
	
	// 날짜/시간 실적 정보 복사 (결과).
	List<SchedulerDetailInfoBean> getTcPerformanceDateTimeListForDaily(int schedulerInfoUid) throws Exception;
	
	// 날짜/시간 실적 정보 복사생성 (결과).
	int insertTcPerformanceDateTimeForDaily(SchedulerDetailInfoBean bean) throws Exception;
	
	// 시운전 연료 사용량 계산 (결과).
	ScheTrialInfoBean getTotalFuelDown(int schedulerInfoUid) throws Exception;
	
	// 완료보고서 표시 특이사항 목록 (결과).
	List<ScheTcNoteBean> getTcNoteListForComp(int schedulerInfoUid) throws Exception;
	
	// 완료보고서 표시 특이사항 TC 목록 (결과).
	List<ScheTcNoteTcInfoBean> getTcNoteTcListForComp(int scheTcNoteUid) throws Exception;
	
	// 완료보고서 표시 특이사항 파일 목록 (결과).
	List<ScheTcNoteFileInfoBean> getTcNoteFileListForComp(int scheTcNoteUid) throws Exception;
	
	// 완료 보고서 입력 (결과).
	int insertReportComp(ScheReportCompBean bean) throws Exception;
	
	// 이메일 검색.
	List<ScheMailBean> searchEmail(ParamBean bean) throws Exception;
	
	// *** 다운로드. *** //
	int deleteSchedulerInfoForDown(int uid) throws Exception;
	
	int deleteSchedulerVersionInfoForDown(int uid) throws Exception;
	
	int deleteSchedulerDetailForDown(int uid) throws Exception;
	
	int deleteScheTrialInfoForDown(int uid) throws Exception;
	
	int deleteScheCrewForDown(int uid) throws Exception;
	
	List<Integer> getCrewUidListForDown(int schedulerInfoUid) throws Exception;
	
	int deleteScheCrewInOutForDown(int uid) throws Exception;
	
	int deleteSchedulerInfoSearchForDown() throws Exception;
	
	int deleteDomainForDown() throws Exception;
	
	int deleteDomainInfoForDown() throws Exception;
	
	int deleteUserInfoForDown() throws Exception;
	
	int deleteShipInfoForDown() throws Exception;
	
	int deleteScheMailForDown() throws Exception;
	
	int deleteScheHierarchyForDown() throws Exception;
	
	int deleteScheduleCodeDetailForDown() throws Exception;
	
	int deleteScheduleCodeInfoForDown() throws Exception;
	
	int deleteVsslReqInfoForDown() throws Exception;
	
	int deleteVsslReqInfoDetailForDown() throws Exception;
	
	int deleteShipCondForDown() throws Exception;
	
	int insertSchedulerInfoForDown(SchedulerInfoBean bean) throws Exception;
	
	int insertSchedulerVersionInfoForDown(SchedulerVersionInfoBean bean) throws Exception;
	
	int insertSchedulerDetailForDown(SchedulerDetailInfoBean bean) throws Exception;
	
	int insertScheTrialInfoForDown(ScheTrialInfoBean bean) throws Exception;
	
	int insertScheCrewForDown(ScheCrewBean bean) throws Exception;
	
	Integer getCrewUidForDown(int scheCrewUid) throws Exception;
	
	int insertScheCrewInOutForDown(ScheCrewInOutBean bean) throws Exception;
	
	int insertSchedulerInfoSearchForDown(SchedulerInfoBean bean) throws Exception;
	
	int insertDomainForDown(DomainBean bean) throws Exception;
	
	int insertDomainInfoForDown(DomainInfoBean bean) throws Exception;
	
	int insertUserInfoForDown(UserInfoBean bean) throws Exception;
	
	int insertShipInfoForDown(ShipInfoBean bean) throws Exception;
	
	int insertScheMailForDown(ScheMailBean bean) throws Exception;
	
	int insertScheHierarchyForDown(ScheduleHierarchyBean bean) throws Exception;
	
	int insertScheduleCodeDetailForDown(ScheduleCodeDetailBean bean) throws Exception;
	
	int insertScheduleCodeInfoForDown(ScheduleCodeInfoBean bean) throws Exception;
	
	int insertVsslReqInfoForDown(VesselReqInfoBean bean) throws Exception;
	
	int insertVsslReqInfoDetailForDown(VesselReqInfoDetailBean bean) throws Exception;
	
	int insertShipCondForDown(ShipCondBean bean) throws Exception;
	
	SchedulerInfoBean getSchedulerInfoForUp(int uid) throws Exception;
	
	ScheTrialInfoBean getScheTrialInfoForUp(int uid) throws Exception;
	
	List<SchedulerDetailInfoBean> getSchedulerDetailForUp(int uid) throws Exception;
	
	List<ScheTcNoteBean> getScheTcNoteForUp(int uid) throws Exception;
	
	List<ScheTcNoteTcInfoBean> getScheTcNoteTcInfoForUp(int uid) throws Exception;
	
	List<ScheTcNoteFileInfoBean> getScheTcNoteFileInfoForUp(int uid) throws Exception;
	
	List<ScheCrewBean> getScheCrewForUp(int uid) throws Exception;
	
	List<ScheCrewInOutBean> getScheCrewInOutForUp(int uid) throws Exception;
	
	int updateSchedulerInfoForUp(int uid) throws Exception;
	
	int updateScheTrialInfoForUp(int uid) throws Exception;
	
	int updateSchedulerDetailForUp(int uid) throws Exception;
	
	int updateScheTcNoteForUp(int uid) throws Exception;
	
	int updateScheTcNoteTcInfoForUp(int uid) throws Exception;
	
	int updateScheTcNoteFileInfoForUp(int uid) throws Exception;
	
	int updateScheCrewForUp(int uid) throws Exception;
	
	int updateScheCrewInOutForUp(int uid) throws Exception;
	
	List<String> getFileNameForOn(int uid) throws Exception;
	
	int deleteSchedulerInfoForOn(int uid) throws Exception;
	
	int deleteScheTrialInfoForOn(int uid) throws Exception;
	
	int deleteScheDateTimeForOn(int uid) throws Exception;
	
	int deleteSchedulerDetailForOn(int uid) throws Exception;
	
	int deleteScheTcNoteForOn(int uid) throws Exception;
	
	int deleteScheTcNoteTcInfoForOn(int uid) throws Exception;
	
	int deleteScheTcNoteFileInfoForOn(int uid) throws Exception;
	
	int deleteScheCrewInOutForOn(int uid) throws Exception;
	
	int deleteScheCrewForOn(int uid) throws Exception;
	
	int deleteScheReportDepartureForOn(int uid) throws Exception;
	
	int deleteScheReportDailyForOn(int uid) throws Exception;
	
	int deleteScheReportCompForOn(int uid) throws Exception;
	
	int checkUploaded(int uid) throws Exception;
	
	// TC번호 검색.
	List<ScheduleCodeDetailBean> getScheduleTcNumSearchList(ScheduleCodeDetailBean bean) throws Exception;
	
	// TC번호 검색 전체 개수.
	int getScheduleTcNumSearchListCnt() throws Exception;
	
	// TC삭제용 schetcnote.uid 검색
	Integer getScheTcNoteUid(int detailUid) throws Exception;
	
	// TC삭제용 schetcnotefileinfo.savename 목록 검색
	List<String> getScheTcNoteFileInfoSaveNameList(int noteUid) throws Exception;
	
	// TC삭제용 schetcnote 삭제
	int deleteScheTcNote(int uid) throws Exception;
	
	// TC삭제용 schetcnotetcinfo 삭제
	int deleteScheTcNoteTcInfoList(int noteUid) throws Exception;
	
	// TC삭제용 schetcnotefileinfo 삭제
	int deleteScheTcNoteFileInfoList(int noteUid) throws Exception;
	
	/// 스케쥴 보고서 --------------- ///
	// Domain ID 문자값으로 도메인 조회
	List<DomainInfoBean> getDomainInfoListByDomainId(String domain) throws Exception;
	
	// 스케쥴러의 uid 로 활성화된 호선별 필요 정보 불러 오기(보고서에서 사용)
	List<VesselReqInfoDetailBean> getVesselReqInfoDetListBySchedinfoUid(ParamBean bean) throws Exception;
	
	int getVesselReqInfoDetListBySchedinfoUidCnt() throws Exception;
	
	// 스케쥴러의 uid 로 활성화된 호선별 필요 정보 불러오기(보고서에서 사용)
	VesselReqInfoBean getVesselReqInfoBySchedinfoUid(ParamBean bean) throws Exception;
	
	// 호선별 선박 상태 정보
	List<ShipCondBean> getShipCondList(ParamBean bean) throws Exception;
	/// --------------- 스케쥴 보고서 ///
	
	/// 250309 ///
	// 승선 여부 확인.
	List<String> getPerformanceInOutForCrewDay(ScheCrewInOutBean bean) throws Exception;
	
	// 날짜 정보 (오늘, 스케쥴 시작일/종료일).
	SchedulerInfoBean getDateInfoForCrewDay(int uid) throws Exception;
	
	// 연료 추세 정보.
	List<ScheTrialInfoBean> getFuelDateList(int uid) throws Exception;
	
	// 완료 보고 데이터.
	ScheTrialInfoBean getTrialCompData(int uid) throws Exception;
	
	/// 250317 ///
	// 일일 보고서 TC 목록.
	List<SchedulerDetailInfoBean> getDailyReportTcList(int uid) throws Exception;
	
	// 마지막 메일링 가져오기.
	List<ScheMailBean> getLastMailList(int uid) throws Exception;
	
	// 메일링 목록 가져오기. 
	List<ScheMailBean> getMailList() throws Exception;
	
	// 마지막 메일링 삭제.
	int deleteLastMailList(int uid) throws Exception;
	
	// 기존에 입력된 승하선 실적 삭제
	int deleteCrewInoutPerformanceList(int scheCrewUid) throws Exception;
	
	// 보고서 스케줄 정보 (출항 시 계획)
	ScheduleBean getScheduleDateTimeDeparture(int uid) throws Exception;
	
	// 보고서 스케줄 정보 (기본 계획)
	ScheduleBean getScheduleDateTimePlan(int uid) throws Exception;
	
	// 보고서 스케줄 정보 (실적)
	ScheduleBean getScheduleDateTimePerformance(int uid) throws Exception;
	
	// 표준코드 1레벨 목록.
	List<ScheduleHierarchyBean> get1LevelCodeList() throws Exception;
}
