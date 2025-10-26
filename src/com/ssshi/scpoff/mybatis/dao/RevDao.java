package com.ssshi.scpoff.mybatis.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssshi.scpoff.constant.Const;
import com.ssshi.scpoff.dto.ScheCrewBean;
import com.ssshi.scpoff.dto.ScheCrewInOutBean;
import com.ssshi.scpoff.dto.ScheDateTimeBean;
import com.ssshi.scpoff.dto.ScheTcNoteBean;
import com.ssshi.scpoff.dto.ScheTcNoteFileInfoBean;
import com.ssshi.scpoff.dto.ScheTcNoteTcInfoBean;
import com.ssshi.scpoff.dto.ScheTrialInfoBean;
import com.ssshi.scpoff.dto.SchedulerDetailInfoBean;
import com.ssshi.scpoff.dto.SchedulerInfoBean;
import com.ssshi.scpoff.dto.SchedulerVersionInfoBean;
import com.ssshi.scpoff.util.CommonUtil;
import com.ssshi.scpoff.util.ValidUtil;

/********************************************************************************
 * 프로그램 개요 : Revision
 * 
 * 최초 작성자 : KHJ
 * 최초 작성일 : 2025-02-26
 * 
 * 최종 수정자 : KHJ
 * 최종 수정일 : 2025-02-26
 * 
 * 메모 : 없음
 * 
 * Copyright 2025 by SiriusB. Confidential and proprietary information
 * This document contains information, which is the property of SiriusB, 
 * and is furnished for the sole purpose of the operation and the maintenance.  
 * Copyright © 2025 SiriusB.  All rights reserved.
 *
 ********************************************************************************/

public class RevDao {
	
	/// 데이터 개정.
	public static Map<String, Object> createRevData(int schedulerInfoUid, String revKind) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		int idx = 1;
		
		SchedulerInfoBean revSchedulerInfoBean = null;
		SchedulerVersionInfoBean revSchedulerVersionInfoBean = null;
		List<SchedulerDetailInfoBean> revSchedulerDetailInfoList = null;
		List<ScheDateTimeBean> revScheDateTimeList = null;
		ScheTrialInfoBean revScheTrialInfoBean = null;
		List<ScheCrewBean> revScheCrewList = null;
		List<ScheCrewInOutBean> revScheCrewInOutList = null;
		List<ScheTcNoteBean> revScheTcNoteList = null;
		List<ScheTcNoteFileInfoBean> revScheTcNoteFileInfoList = null;
		List<ScheTcNoteTcInfoBean> revScheTcNoteTcInfoList = null;
		String revDay = CommonUtil.getDateStr(false);
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			conn.setAutoCommit(false);
			
			sql = "SELECT UID, HULLNUM, SHIPTYPE, DESCRIPTION, OWNER, DEPARTMENT, SDATE, EDATE, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE," + 
				"		STATUS, SCHEDTYPE, SCHECREWSDATE, SCHECREWPERIOD, SCHEDULERVERSIONINFOUID, REVNUM, TRIALKEY, KEYNO, ISOFF, OFFBY, OFFDATE" + 
				"	FROM SCHEDULERINFO" + 
				"	WHERE UID = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, schedulerInfoUid);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				idx = 1;
				revSchedulerInfoBean = new SchedulerInfoBean();
				revSchedulerInfoBean.setRevKind(revKind);
				revSchedulerInfoBean.setRevDay(revDay);
				revSchedulerInfoBean.setUid(rs.getInt(idx++));
				revSchedulerInfoBean.setHullnum(rs.getString(idx++));
				revSchedulerInfoBean.setShiptype(rs.getString(idx++));
				revSchedulerInfoBean.setDescription(rs.getString(idx++));
				revSchedulerInfoBean.setOwner(rs.getInt(idx++));
				revSchedulerInfoBean.setDepartment(rs.getString(idx++));
				revSchedulerInfoBean.setSdate(rs.getString(idx++));
				revSchedulerInfoBean.setEdate(rs.getString(idx++));
				revSchedulerInfoBean.setInsertBy(rs.getInt(idx++));
				revSchedulerInfoBean.setInsertdate(rs.getString(idx++));
				revSchedulerInfoBean.setUpdateBy(rs.getInt(idx++));
				revSchedulerInfoBean.setUpdateDate(rs.getString(idx++));
				revSchedulerInfoBean.setStatus(rs.getString(idx++));
				revSchedulerInfoBean.setSchedtype(rs.getString(idx++));
				revSchedulerInfoBean.setScheCrewSdate(rs.getString(idx++));
				revSchedulerInfoBean.setScheCrewPeriod(rs.getInt(idx++));
				revSchedulerInfoBean.setSchedulerVersionInfoUid(rs.getInt(idx++));
				revSchedulerInfoBean.setRevnum(rs.getString(idx++));
				revSchedulerInfoBean.setTrialKey(rs.getString(idx++));
				revSchedulerInfoBean.setKeyNo(rs.getInt(idx++));
				revSchedulerInfoBean.setIsOff(rs.getString(idx++));
				revSchedulerInfoBean.setOffBy(rs.getInt(idx++));
				revSchedulerInfoBean.setOffDate(rs.getString(idx++));
			}
			
			rs.close();
			stmt.close();
			
			if(revSchedulerInfoBean != null && revSchedulerInfoBean.getSchedulerVersionInfoUid() > 0) {
				sql = "SELECT UID, PLANREVNUM, EXECREVNUM, COMPREVNUM, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE" + 
					"	FROM SCHEDULERVERSIONINFO" + 
					"	WHERE UID = ?";
				
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, revSchedulerInfoBean.getSchedulerVersionInfoUid());
				rs = stmt.executeQuery();
				
				if(rs.next()) {
					idx = 1;
					revSchedulerVersionInfoBean = new SchedulerVersionInfoBean();
					revSchedulerVersionInfoBean.setRevKind(revKind);
					revSchedulerVersionInfoBean.setRevDay(revDay);
					revSchedulerVersionInfoBean.setUid(rs.getInt(idx++));
					revSchedulerVersionInfoBean.setPlanRevNum(rs.getString(idx++));
					revSchedulerVersionInfoBean.setExecRevNum(rs.getString(idx++));
					revSchedulerVersionInfoBean.setCompRevNum(rs.getString(idx++));
					revSchedulerVersionInfoBean.setInsertBy(rs.getInt(idx++));
					revSchedulerVersionInfoBean.setInsertDate(rs.getString(idx++));
					revSchedulerVersionInfoBean.setUpdateBy(rs.getInt(idx++));
					revSchedulerVersionInfoBean.setUpdateDate(rs.getString(idx++));
				}
				
				rs.close();
				stmt.close();
			}
			
			sql = "SELECT UID, SCHEDINFOUID, CATEGORY, TCNUM, DESCRIPTION, CTYPE, LOADRATE, DTYPE," + 
				"		CASE WHEN SDATE = '0000-00-00' THEN '0000-00-00' ELSE SDATE END AS SDATE, STIME," + 
				"		CASE WHEN EDATE = '0000-00-00' THEN '0000-00-00' ELSE EDATE END AS EDATE, ETIME," + 
				"		SEQ, PER, NOTE," + 
				"		INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE, READYTIME, CODEDETUID, SAMETCNUM," + 
				"		PERFORMANCESDATE, PERFORMANCESTIME, PERFORMANCEEDATE, PERFORMANCEETIME, CODEDETTCNUM, CODEDETDESC" + 
				"	FROM SCHEDULERDETAIL" + 
				"	WHERE SCHEDINFOUID = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, schedulerInfoUid);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				if(revSchedulerDetailInfoList == null) {
					revSchedulerDetailInfoList = new ArrayList<SchedulerDetailInfoBean>();
				}
				
				idx = 1;
				SchedulerDetailInfoBean row = new SchedulerDetailInfoBean();
				row.setRevKind(revKind);
				row.setRevDay(revDay);
				row.setUid(rs.getInt(idx++));
				row.setSchedinfouid(rs.getInt(idx++));
				row.setCategory(rs.getString(idx++));
				row.setTcnum(rs.getString(idx++));
				row.setDescription(rs.getString(idx++));
				row.setCtype(rs.getString(idx++));
				row.setLoadrate(rs.getString(idx++));
				row.setDtype(rs.getString(idx++));
				row.setSdate(rs.getString(idx++));
				row.setStime(rs.getString(idx++));
				row.setEdate(rs.getString(idx++));
				row.setEtime(rs.getString(idx++));
				row.setSeq(rs.getString(idx++));
				row.setPer(rs.getString(idx++));
				row.setNote(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				row.setReadytime(rs.getString(idx++));
				row.setCodedetuid(rs.getString(idx++));
				row.setSametcnum(rs.getString(idx++));
				row.setPerformancesdate(rs.getString(idx++));
				row.setPerformancestime(rs.getString(idx++));
				row.setPerformanceedate(rs.getString(idx++));
				row.setPerformanceetime(rs.getString(idx++));
				row.setCodedettcnum(rs.getString(idx++));
				row.setCodedetdesc(rs.getString(idx++));
				
				revSchedulerDetailInfoList.add(row);
			}
			
			rs.close();
			stmt.close();
			
			sql = "SELECT UID, SCHEDULERDETAILUID, STARTDATE, STARTTIME, ENDDATE, ENDTIME, KIND, REVDATE, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE" + 
				"	FROM SCHEDATETIME" + 
				"	WHERE SCHEDULERDETAILUID IN (SELECT UID FROM SCHEDULERDETAIL WHERE SCHEDINFOUID = ?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, schedulerInfoUid);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				if(revScheDateTimeList == null) {
					revScheDateTimeList = new ArrayList<ScheDateTimeBean>();
				}
				
				idx = 1;
				ScheDateTimeBean row = new ScheDateTimeBean();
				row.setRevKind(revKind);
				row.setRevDay(revDay);
				row.setUid(rs.getInt(idx++));
				row.setSchedulerDetailUid(rs.getInt(idx++));
				row.setStartDate(rs.getString(idx++));
				row.setStartTime(rs.getString(idx++));
				row.setEndDate(rs.getString(idx++));
				row.setEndTime(rs.getString(idx++));
				row.setKind(rs.getString(idx++));
				row.setRevDate(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				
				revScheDateTimeList.add(row);
			}
			
			rs.close();
			stmt.close();
			
			sql = "SELECT UID, SCHEDULERINFOUID, TRIAL1SCHEDULERINFOUID, TRIAL2SCHEDULERINFOUID," + 
				"		ITPTOTALREMAIN, ITPTOTALBASE, ITPTRIALREMAIN, ITPTRIALBASE, ITPOUTFITTINGREMAIN, ITPOUTFITTINGBASE," + 
				"		PUNCHTOTAL, PUNCHTRIAL, PUNCHOUTFITTING," + 
				"		FUELHFOSCHEDULER, FUELHFOPERFORMANCE, FUELHFOTEMP, FUELHFOUP, FUELHFODOWN," + 
				"		FUELMGOSCHEDULER, FUELMGOPERFORMANCE, FUELMGOTEMP, FUELMGOUP, FUELMGODOWN," + 
				"		FUELMDOSCHEDULER, FUELMDOPERFORMANCE, FUELMDOTEMP, FUELMDOUP, FUELMDODOWN," + 
				"		FUELLNGSCHEDULER, FUELLNGPERFORMANCE, FUELLNGTEMP, FUELLNGUP, FUELLNGDOWN," + 
				"		DRAFTFWDSCHEDULER, DRAFTFWDPERFORMANCE, DRAFTFWDTEMP, DRAFTFWDUP, DRAFTFWDDOWN," + 
				"		DRAFTMIDSCHEDULER, DRAFTMIDPERFORMANCE, DRAFTMIDTEMP, DRAFTMIDUP, DRAFTMIDDOWN," + 
				"		DRAFTAFTSCHEDULER, DRAFTAFTPERFORMANCE, DRAFTAFTTEMP, DRAFTAFTUP, DRAFTAFTDOWN," + 
				"		CREWREMARK, REMARK, CONTRACTSPEED, MEASURESPEED, COMPREMARK, NOISEVIBRATION, TRIALSTATUS, ONGOCHANGEDATE," + 
				"		INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE" + 
				"	FROM SCHETRIALINFO" + 
				"	WHERE SCHEDULERINFOUID = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, schedulerInfoUid);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				idx = 1;
				revScheTrialInfoBean = new ScheTrialInfoBean();
				revScheTrialInfoBean.setRevKind(revKind);
				revScheTrialInfoBean.setRevDay(revDay);
				revScheTrialInfoBean.setUid(rs.getInt(idx++));
				revScheTrialInfoBean.setSchedulerInfoUid(rs.getInt(idx++));
				revScheTrialInfoBean.setTrial1SchedulerInfoUid(rs.getInt(idx++));
				revScheTrialInfoBean.setTrial2SchedulerInfoUid(rs.getInt(idx++));
				revScheTrialInfoBean.setItpTotalRemain(ValidUtil.getInteger(rs, idx++));
				revScheTrialInfoBean.setItpTotalBase(ValidUtil.getInteger(rs, idx++));
				revScheTrialInfoBean.setItpTrialRemain(ValidUtil.getInteger(rs, idx++));
				revScheTrialInfoBean.setItpTrialBase(ValidUtil.getInteger(rs, idx++));
				revScheTrialInfoBean.setItpOutfittingRemain(ValidUtil.getInteger(rs, idx++));
				revScheTrialInfoBean.setItpOutfittingBase(ValidUtil.getInteger(rs, idx++));
				revScheTrialInfoBean.setPunchTotal(ValidUtil.getInteger(rs, idx++));
				revScheTrialInfoBean.setPunchTrial(ValidUtil.getInteger(rs, idx++));
				revScheTrialInfoBean.setPunchOutfitting(ValidUtil.getInteger(rs, idx++));
				revScheTrialInfoBean.setFuelHfoScheduler(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelHfoPerformance(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelHfoTemp(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelHfoUp(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelHfoDown(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelMgoScheduler(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelMgoPerformance(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelMgoTemp(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelMgoUp(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelMgoDown(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelMdoScheduler(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelMdoPerformance(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelMdoTemp(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelMdoUp(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelMdoDown(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelLngScheduler(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelLngPerformance(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelLngTemp(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelLngUp(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setFuelLngDown(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftFwdScheduler(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftFwdPerformance(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftFwdTemp(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftFwdUp(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftFwdDown(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftMidScheduler(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftMidPerformance(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftMidTemp(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftMidUp(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftMidDown(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftAftScheduler(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftAftPerformance(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftAftTemp(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftAftUp(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setDraftAftDown(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setCrewRemark(rs.getString(idx++));
				revScheTrialInfoBean.setRemark(rs.getString(idx++));
				revScheTrialInfoBean.setContractSpeed(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setMeasureSpeed(ValidUtil.getFloat(rs, idx++));
				revScheTrialInfoBean.setCompRemark(rs.getString(idx++));
				revScheTrialInfoBean.setNoiseVibration(rs.getString(idx++));
				revScheTrialInfoBean.setTrialStatus(rs.getString(idx++));
				revScheTrialInfoBean.setOngoChangeDate(rs.getString(idx++));
				revScheTrialInfoBean.setInsertBy(rs.getInt(idx++));
				revScheTrialInfoBean.setInsertDate(rs.getString(idx++));
				revScheTrialInfoBean.setUpdateBy(rs.getInt(idx++));
				revScheTrialInfoBean.setUpdateDate(rs.getString(idx++));
			}
			
			rs.close();
			stmt.close();
			
			sql = "SELECT UID, SCHEDULERINFOUID, KIND, COMPANY, DEPARTMENT, NAME, RANK, IDNO, WORKTYPE1, WORKTYPE2," + 
				"		MAINSUB, FOODSTYLE, PERSONNO, PHONE, ISPLAN, ISNONE, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE" + 
				"	FROM SCHECREW" + 
				"	WHERE SCHEDULERINFOUID = ?";
				
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, schedulerInfoUid);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				if(revScheCrewList == null) {
					revScheCrewList = new ArrayList<ScheCrewBean>();
				}
				
				idx = 1;
				ScheCrewBean row = new ScheCrewBean();
				row.setRevKind(revKind);
				row.setRevDay(revDay);
				row.setUid(rs.getInt(idx++));
				row.setSchedulerInfoUid(rs.getInt(idx++));
				row.setKind(rs.getString(idx++));
				row.setCompany(rs.getString(idx++));
				row.setDepartment(rs.getString(idx++));
				row.setName(rs.getString(idx++));
				row.setRank(rs.getString(idx++));
				row.setIdNo(rs.getString(idx++));
				row.setWorkType1(rs.getString(idx++));
				row.setWorkType2(rs.getString(idx++));
				row.setMainSub(rs.getString(idx++));
				row.setFoodStyle(rs.getString(idx++));
				row.setPersonNo(rs.getString(idx++));
				row.setPhone(rs.getString(idx++));
				row.setIsPlan(rs.getString(idx++));
				row.setIsNone(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				
				revScheCrewList.add(row);
			}
			
			rs.close();
			stmt.close();
			
			sql = "SELECT UID, SCHECREWUID, INOUTDATE, SCHEDULERINOUT, PERFORMANCEINOUT, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE" + 
				"	FROM SCHECREWINOUT" + 
				"	WHERE SCHECREWUID IN (SELECT UID FROM SCHECREW WHERE SCHEDULERINFOUID = ?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, schedulerInfoUid);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				if(revScheCrewInOutList == null) {
					revScheCrewInOutList = new ArrayList<ScheCrewInOutBean>();
				}
				
				idx = 1;
				ScheCrewInOutBean row = new ScheCrewInOutBean();
				row.setRevKind(revKind);
				row.setRevDay(revDay);
				row.setUid(rs.getInt(idx++));
				row.setScheCrewUid(rs.getInt(idx++));
				row.setInOutDate(rs.getString(idx++));
				row.setSchedulerInOut(rs.getString(idx++));
				row.setPerformanceInOut(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				
				revScheCrewInOutList.add(row);
			}
			
			rs.close();
			stmt.close();
			
			sql = "SELECT UID, SCHEDULERINFOUID, SCHEDULERDETAILUID, CODEKIND, CODE, STARTDATE, ENDDATE, REMARK, ISREPORT," + 
				"		INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE" + 
				"	FROM SCHETCNOTE" + 
				"	WHERE SCHEDULERINFOUID = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, schedulerInfoUid);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				if(revScheTcNoteList == null) {
					revScheTcNoteList = new ArrayList<ScheTcNoteBean>();
				}
				
				idx = 1;
				ScheTcNoteBean row = new ScheTcNoteBean();
				row.setRevKind(revKind);
				row.setRevDay(revDay);
				row.setUid(rs.getInt(idx++));
				row.setSchedulerInfoUid(rs.getInt(idx++));
				row.setSchedulerDetailUid(rs.getInt(idx++));
				row.setCodeKind(rs.getString(idx++));
				row.setCode(rs.getString(idx++));
				row.setStartDate(rs.getString(idx++));
				row.setEndDate(rs.getString(idx++));
				row.setRemark(rs.getString(idx++));
				row.setIsReport(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				
				revScheTcNoteList.add(row);
			}
			
			rs.close();
			stmt.close();
			
			sql = "SELECT UID, SCHEDULERINFOUID, SCHETCNOTEUID, FILENAME, SAVENAME, FILESIZE, FILETYPE, INSERTBY, INSERTDATE" + 
				"	FROM SCHETCNOTEFILEINFO" + 
				"	WHERE SCHEDULERINFOUID = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, schedulerInfoUid);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				if(revScheTcNoteFileInfoList == null) {
					revScheTcNoteFileInfoList = new ArrayList<ScheTcNoteFileInfoBean>();
				}
				
				idx = 1;
				ScheTcNoteFileInfoBean row = new ScheTcNoteFileInfoBean();
				row.setRevKind(revKind);
				row.setRevDay(revDay);
				row.setUid(rs.getInt(idx++));
				row.setSchedulerInfoUid(rs.getInt(idx++));
				row.setScheTcNoteUid(rs.getInt(idx++));
				row.setFileName(rs.getString(idx++));
				row.setSaveName(rs.getString(idx++));
				row.setFileSize(rs.getInt(idx++));
				row.setFileType(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setInsertDate(rs.getString(idx++));
				
				revScheTcNoteFileInfoList.add(row);
			}
			
			rs.close();
			stmt.close();
			
			sql = "SELECT UID, SCHEDULERINFOUID, SCHETCNOTEUID, SCHEDULERDETAILUID, INSERTBY, INSERTDATE" + 
				"	FROM SCHETCNOTETCINFO" + 
				"	WHERE SCHEDULERINFOUID = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, schedulerInfoUid);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				if(revScheTcNoteTcInfoList == null) {
					revScheTcNoteTcInfoList = new ArrayList<ScheTcNoteTcInfoBean>();
				}
				
				idx = 1;
				ScheTcNoteTcInfoBean row = new ScheTcNoteTcInfoBean();
				row.setRevKind(revKind);
				row.setRevDay(revDay);
				row.setUid(rs.getInt(idx++));
				row.setSchedulerInfoUid(rs.getInt(idx++));
				row.setScheTcNoteUid(rs.getInt(idx++));
				row.setSchedulerDetailUid(rs.getInt(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setInsertDate(rs.getString(idx++));
				
				revScheTcNoteTcInfoList.add(row);
			}
			
			rs.close();
			stmt.close();
			
			if(revSchedulerInfoBean != null) {
				sql = "INSERT INTO REVSCHEDULERINFO (ORGUID, REVKIND, REVDAY, HULLNUM, SHIPTYPE, DESCRIPTION, OWNER, DEPARTMENT, SDATE, EDATE, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE," + 
					"		STATUS, SCHEDTYPE, SCHECREWSDATE, SCHECREWPERIOD, SCHEDULERVERSIONINFOUID, REVNUM, TRIALKEY, KEYNO, ISOFF, OFFBY, OFFDATE)" + 
					"	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + 
					"		?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				stmt = conn.prepareStatement(sql);
				
				idx = 1;
				stmt.setInt(idx++, revSchedulerInfoBean.getUid());
				stmt.setString(idx++, revSchedulerInfoBean.getRevKind());
				stmt.setString(idx++, revSchedulerInfoBean.getRevDay());
				stmt.setString(idx++, revSchedulerInfoBean.getHullnum());
				stmt.setString(idx++, revSchedulerInfoBean.getShiptype());
				stmt.setString(idx++, revSchedulerInfoBean.getDescription());
				stmt.setInt(idx++, revSchedulerInfoBean.getOwner());
				stmt.setString(idx++, revSchedulerInfoBean.getDepartment());
				stmt.setString(idx++, revSchedulerInfoBean.getSdate());
				stmt.setString(idx++, revSchedulerInfoBean.getEdate());
				stmt.setInt(idx++, revSchedulerInfoBean.getInsertBy());
				stmt.setString(idx++, revSchedulerInfoBean.getInsertdate());
				stmt.setInt(idx++, revSchedulerInfoBean.getUpdateBy());
				stmt.setString(idx++, revSchedulerInfoBean.getUpdateDate());
				stmt.setString(idx++, revSchedulerInfoBean.getStatus());
				stmt.setString(idx++, revSchedulerInfoBean.getSchedtype());
				stmt.setString(idx++, revSchedulerInfoBean.getScheCrewSdate());
				stmt.setInt(idx++, revSchedulerInfoBean.getScheCrewPeriod());
				stmt.setInt(idx++, revSchedulerInfoBean.getSchedulerVersionInfoUid());
				stmt.setString(idx++, revSchedulerInfoBean.getRevnum());
				stmt.setString(idx++, revSchedulerInfoBean.getTrialKey());
				stmt.setInt(idx++, revSchedulerInfoBean.getKeyNo());
				stmt.setString(idx++, revSchedulerInfoBean.getIsOff());
				stmt.setInt(idx++, revSchedulerInfoBean.getOffBy());
				stmt.setString(idx++, revSchedulerInfoBean.getOffDate());
				
				stmt.executeUpdate();
				stmt.close();
			}
			
			if(revSchedulerVersionInfoBean != null) {
				sql = "INSERT INTO REVSCHEDULERVERSIONINFO (ORGUID, REVKIND, REVDAY, PLANREVNUM, EXECREVNUM, COMPREVNUM, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE)" + 
					"	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				stmt = conn.prepareStatement(sql);
				
				idx = 1;
				stmt.setInt(idx++, revSchedulerVersionInfoBean.getUid());
				stmt.setString(idx++, revSchedulerVersionInfoBean.getRevKind());
				stmt.setString(idx++, revSchedulerVersionInfoBean.getRevDay());
				stmt.setString(idx++, revSchedulerVersionInfoBean.getPlanRevNum());
				stmt.setString(idx++, revSchedulerVersionInfoBean.getExecRevNum());
				stmt.setString(idx++, revSchedulerVersionInfoBean.getCompRevNum());
				stmt.setInt(idx++, revSchedulerVersionInfoBean.getInsertBy());
				stmt.setString(idx++, revSchedulerVersionInfoBean.getInsertDate());
				stmt.setInt(idx++, revSchedulerVersionInfoBean.getUpdateBy());
				stmt.setString(idx++, revSchedulerVersionInfoBean.getUpdateDate());
				
				stmt.executeUpdate();
				stmt.close();
			}
			
			if(revSchedulerDetailInfoList != null) {
				sql = "INSERT INTO REVSCHEDULERDETAIL (ORGUID, REVKIND, REVDAY, SCHEDINFOUID, CATEGORY, TCNUM, DESCRIPTION," +
					"		CTYPE, LOADRATE, DTYPE, SDATE, STIME, EDATE, ETIME, SEQ, PER, NOTE," +
					"		INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE, READYTIME, CODEDETUID, SAMETCNUM," + 
					"		PERFORMANCESDATE, PERFORMANCESTIME, PERFORMANCEEDATE, PERFORMANCEETIME, CODEDETTCNUM, CODEDETDESC)" + 
					"	VALUES (?, ?, ?, ?, ?, ?, ?," +
					"		?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
					"		?, ?, ?, ?, ?, ?, ?," +
					"		?, ?, ?, ?, ?, ?)";
				
				for(int i = 0; i < revSchedulerDetailInfoList.size(); i++) {
					SchedulerDetailInfoBean row = revSchedulerDetailInfoList.get(i);
					stmt = conn.prepareStatement(sql);
					
					idx = 1;
					stmt.setInt(idx++, row.getUid());
					stmt.setString(idx++, row.getRevKind());
					stmt.setString(idx++, row.getRevDay());
					stmt.setInt(idx++, row.getSchedinfouid());
					stmt.setString(idx++, row.getCategory());
					stmt.setString(idx++, row.getTcnum());
					stmt.setString(idx++, row.getDescription());
					stmt.setString(idx++, row.getCtype());
					stmt.setString(idx++, row.getLoadrate());
					stmt.setString(idx++, row.getDtype());
					stmt.setString(idx++, row.getSdate());
					stmt.setString(idx++, row.getStime());
					stmt.setString(idx++, row.getEdate());
					stmt.setString(idx++, row.getEtime());
					stmt.setString(idx++, row.getSeq());
					stmt.setString(idx++, row.getPer());
					stmt.setString(idx++, row.getNote());
					stmt.setInt(idx++, row.getInsertBy());
					stmt.setString(idx++, row.getInsertDate());
					stmt.setInt(idx++, row.getUpdateBy());
					stmt.setString(idx++, row.getUpdateDate());
					stmt.setString(idx++, row.getReadytime());
					stmt.setString(idx++, row.getCodedetuid());
					stmt.setString(idx++, row.getSametcnum());
					stmt.setString(idx++, row.getPerformancesdate());
					stmt.setString(idx++, row.getPerformancestime());
					stmt.setString(idx++, row.getPerformanceedate());
					stmt.setString(idx++, row.getPerformanceetime());
					stmt.setString(idx++, row.getCodedettcnum());
					stmt.setString(idx++, row.getCodedetdesc());
					
					stmt.executeUpdate();
					stmt.close();
				}
			}
			
			if(revScheDateTimeList != null) {
				sql = "INSERT INTO REVSCHEDATETIME (ORGUID, REVKIND, REVDAY, SCHEDULERDETAILUID, STARTDATE, STARTTIME, ENDDATE, ENDTIME, KIND, REVDATE, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE)" + 
					"	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				for(int i = 0; i < revScheDateTimeList.size(); i++) {
					ScheDateTimeBean row = revScheDateTimeList.get(i);
					stmt = conn.prepareStatement(sql);
					
					idx = 1;
					stmt.setInt(idx++, row.getUid());
					stmt.setString(idx++, row.getRevKind());
					stmt.setString(idx++, row.getRevDay());
					stmt.setInt(idx++, row.getSchedulerDetailUid());
					stmt.setString(idx++, row.getStartDate());
					stmt.setString(idx++, row.getStartTime());
					stmt.setString(idx++, row.getEndDate());
					stmt.setString(idx++, row.getEndTime());
					stmt.setString(idx++, row.getKind());
					stmt.setString(idx++, row.getRevDate());
					stmt.setInt(idx++, row.getInsertBy());
					stmt.setString(idx++, row.getInsertDate());
					stmt.setInt(idx++, row.getUpdateBy());
					stmt.setString(idx++, row.getUpdateDate());
					
					stmt.executeUpdate();
					stmt.close();
				}
			}
			
			if(revScheTrialInfoBean != null) {
				sql = "INSERT INTO REVSCHETRIALINFO (ORGUID, REVKIND, REVDAY, SCHEDULERINFOUID, TRIAL1SCHEDULERINFOUID, TRIAL2SCHEDULERINFOUID," + 
					"		ITPTOTALREMAIN, ITPTOTALBASE, ITPTRIALREMAIN, ITPTRIALBASE, ITPOUTFITTINGREMAIN, ITPOUTFITTINGBASE," + 
					"		PUNCHTOTAL, PUNCHTRIAL, PUNCHOUTFITTING," + 
					"		FUELHFOSCHEDULER, FUELHFOPERFORMANCE, FUELHFOTEMP, FUELHFOUP, FUELHFODOWN," + 
					"		FUELMGOSCHEDULER, FUELMGOPERFORMANCE, FUELMGOTEMP, FUELMGOUP, FUELMGODOWN," + 
					"		FUELMDOSCHEDULER, FUELMDOPERFORMANCE, FUELMDOTEMP, FUELMDOUP, FUELMDODOWN," + 
					"		FUELLNGSCHEDULER, FUELLNGPERFORMANCE, FUELLNGTEMP, FUELLNGUP, FUELLNGDOWN," + 
					"		DRAFTFWDSCHEDULER, DRAFTFWDPERFORMANCE, DRAFTFWDTEMP, DRAFTFWDUP, DRAFTFWDDOWN," + 
					"		DRAFTMIDSCHEDULER, DRAFTMIDPERFORMANCE, DRAFTMIDTEMP, DRAFTMIDUP, DRAFTMIDDOWN," + 
					"		DRAFTAFTSCHEDULER, DRAFTAFTPERFORMANCE, DRAFTAFTTEMP, DRAFTAFTUP, DRAFTAFTDOWN," + 
					"		CREWREMARK, REMARK, CONTRACTSPEED, MEASURESPEED, COMPREMARK, NOISEVIBRATION, TRIALSTATUS, ONGOCHANGEDATE," + 
					"		INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE)" + 
					"	VALUES (?, ?, ?, ?, ?, ?," + 
					"			?, ?, ?, ?, ?, ?," + 
					"			?, ?, ?," + 
					"			?, ?, ?, ?, ?," + 
					"			?, ?, ?, ?, ?," + 
					"			?, ?, ?, ?, ?," + 
					"			?, ?, ?, ?, ?," + 
					"			?, ?, ?, ?, ?," + 
					"			?, ?, ?, ?, ?," + 
					"			?, ?, ?, ?, ?," + 
					"			?, ?, ?, ?, ?, ?, ?, ?," + 
					"			?, ?, ?, ?)";
				
				stmt = conn.prepareStatement(sql);
				
				idx = 1;
				stmt.setInt(idx++, revScheTrialInfoBean.getUid());
				stmt.setString(idx++, revScheTrialInfoBean.getRevKind());
				stmt.setString(idx++, revScheTrialInfoBean.getRevDay());
				stmt.setInt(idx++, revScheTrialInfoBean.getSchedulerInfoUid());
				stmt.setInt(idx++, revScheTrialInfoBean.getTrial1SchedulerInfoUid());
				stmt.setInt(idx++, revScheTrialInfoBean.getTrial2SchedulerInfoUid());
				
				if(revScheTrialInfoBean.getItpTotalRemain() != null) {
					stmt.setInt(idx++, revScheTrialInfoBean.getItpTotalRemain());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(revScheTrialInfoBean.getItpTotalBase() != null) {
					stmt.setInt(idx++, revScheTrialInfoBean.getItpTotalBase());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(revScheTrialInfoBean.getItpTrialRemain() != null) {
					stmt.setInt(idx++, revScheTrialInfoBean.getItpTrialRemain());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(revScheTrialInfoBean.getItpTrialBase() != null) {
					stmt.setInt(idx++, revScheTrialInfoBean.getItpTrialBase());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(revScheTrialInfoBean.getItpOutfittingRemain() != null) {
					stmt.setInt(idx++, revScheTrialInfoBean.getItpOutfittingRemain());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(revScheTrialInfoBean.getItpOutfittingBase() != null) {
					stmt.setInt(idx++, revScheTrialInfoBean.getItpOutfittingBase());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(revScheTrialInfoBean.getPunchTotal() != null) {
					stmt.setInt(idx++, revScheTrialInfoBean.getPunchTotal());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(revScheTrialInfoBean.getPunchTrial() != null) {
					stmt.setInt(idx++, revScheTrialInfoBean.getPunchTrial());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(revScheTrialInfoBean.getPunchOutfitting() != null) {
					stmt.setInt(idx++, revScheTrialInfoBean.getPunchOutfitting());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(revScheTrialInfoBean.getFuelHfoScheduler() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelHfoScheduler());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelHfoPerformance() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelHfoPerformance());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelHfoTemp() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelHfoTemp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelHfoUp() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelHfoUp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelHfoDown() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelHfoDown());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelMgoScheduler() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelMgoScheduler());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelMgoPerformance() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelMgoPerformance());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelMgoTemp() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelMgoTemp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelMgoUp() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelMgoUp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelMgoDown() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelMgoDown());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelMdoScheduler() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelMdoScheduler());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelMdoPerformance() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelMdoPerformance());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelMdoTemp() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelMdoTemp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelMdoUp() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelMdoUp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelMdoDown() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelMdoDown());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelLngScheduler() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelLngScheduler());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelLngPerformance() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelLngPerformance());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelLngTemp() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelLngTemp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelLngUp() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelLngUp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getFuelLngDown() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getFuelLngDown());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftFwdScheduler() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftFwdScheduler());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftFwdPerformance() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftFwdPerformance());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftFwdTemp() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftFwdTemp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftFwdUp() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftFwdUp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftFwdDown() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftFwdDown());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftMidScheduler() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftMidScheduler());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftMidPerformance() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftMidPerformance());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftMidTemp() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftMidTemp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftMidUp() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftMidUp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftMidDown() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftMidDown());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftAftScheduler() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftAftScheduler());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftAftPerformance() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftAftPerformance());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftAftTemp() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftAftTemp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftAftUp() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftAftUp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getDraftAftDown() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getDraftAftDown());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				stmt.setString(idx++, revScheTrialInfoBean.getCrewRemark());
				stmt.setString(idx++, revScheTrialInfoBean.getRemark());
				
				if(revScheTrialInfoBean.getContractSpeed() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getContractSpeed());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(revScheTrialInfoBean.getMeasureSpeed() != null) {
					stmt.setFloat(idx++, revScheTrialInfoBean.getMeasureSpeed());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}

				stmt.setString(idx++, revScheTrialInfoBean.getCompRemark());
				stmt.setString(idx++, revScheTrialInfoBean.getNoiseVibration());
				stmt.setString(idx++, revScheTrialInfoBean.getTrialStatus());
				stmt.setString(idx++, revScheTrialInfoBean.getOngoChangeDate());
				stmt.setInt(idx++, revScheTrialInfoBean.getInsertBy());
				stmt.setString(idx++, revScheTrialInfoBean.getInsertDate());
				stmt.setInt(idx++, revScheTrialInfoBean.getUpdateBy());
				stmt.setString(idx++, revScheTrialInfoBean.getUpdateDate());
				
				stmt.executeUpdate();
				stmt.close();
			}
			
			if(revScheCrewList != null) {
				sql = "INSERT INTO REVSCHECREW (ORGUID, REVKIND, REVDAY, SCHEDULERINFOUID, KIND, COMPANY, DEPARTMENT, NAME, RANK, IDNO, WORKTYPE1, WORKTYPE2," + 
					"		MAINSUB, FOODSTYLE, PERSONNO, PHONE, ISPLAN, ISNONE, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE)" + 
					"	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + 
					"		?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				for(int i = 0; i < revScheCrewList.size(); i++) {
					ScheCrewBean row = revScheCrewList.get(i);
					stmt = conn.prepareStatement(sql);
					
					idx = 1;
					stmt.setInt(idx++, row.getUid());
					stmt.setString(idx++, row.getRevKind());
					stmt.setString(idx++, row.getRevDay());
					stmt.setInt(idx++, row.getSchedulerInfoUid());
					stmt.setString(idx++, row.getKind());
					stmt.setString(idx++, row.getCompany());
					stmt.setString(idx++, row.getDepartment());
					stmt.setString(idx++, row.getName());
					stmt.setString(idx++, row.getRank());
					stmt.setString(idx++, row.getIdNo());
					stmt.setString(idx++, row.getWorkType1());
					stmt.setString(idx++, row.getWorkType2());
					stmt.setString(idx++, row.getMainSub());
					stmt.setString(idx++, row.getFoodStyle());
					stmt.setString(idx++, row.getPersonNo());
					stmt.setString(idx++, row.getPhone());
					stmt.setString(idx++, row.getIsPlan());
					stmt.setString(idx++, row.getIsNone());
					stmt.setInt(idx++, row.getInsertBy());
					stmt.setString(idx++, row.getInsertDate());
					stmt.setInt(idx++, row.getUpdateBy());
					stmt.setString(idx++, row.getUpdateDate());
					
					stmt.executeUpdate();
					stmt.close();
				}
			}
			
			if(revScheCrewInOutList != null) {
				sql = "INSERT INTO REVSCHECREWINOUT (ORGUID, REVKIND, REVDAY, SCHECREWUID, INOUTDATE, SCHEDULERINOUT, PERFORMANCEINOUT, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE)" + 
					"	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				for(int i = 0; i < revScheCrewInOutList.size(); i++) {
					ScheCrewInOutBean row = revScheCrewInOutList.get(i);
					stmt = conn.prepareStatement(sql);
					
					idx = 1;
					stmt.setInt(idx++, row.getUid());
					stmt.setString(idx++, row.getRevKind());
					stmt.setString(idx++, row.getRevDay());
					stmt.setInt(idx++, row.getScheCrewUid());
					stmt.setString(idx++, row.getInOutDate());
					stmt.setString(idx++, row.getSchedulerInOut());
					stmt.setString(idx++, row.getPerformanceInOut());
					stmt.setInt(idx++, row.getInsertBy());
					stmt.setString(idx++, row.getInsertDate());
					stmt.setInt(idx++, row.getUpdateBy());
					stmt.setString(idx++, row.getUpdateDate());
					
					stmt.executeUpdate();
					stmt.close();
				}
			}
			
			if(revScheTcNoteList != null) {
				sql = "INSERT INTO REVSCHETCNOTE (ORGUID, REVKIND, REVDAY, SCHEDULERINFOUID, SCHEDULERDETAILUID, CODEKIND, CODE, STARTDATE, ENDDATE, REMARK, ISREPORT," + 
					"		INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE)" + 
					"	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + 
					"		?, ?, ?, ?)";
				
				for(int i = 0; i < revScheTcNoteList.size(); i++) {
					ScheTcNoteBean row = revScheTcNoteList.get(i);
					stmt = conn.prepareStatement(sql);
					
					idx = 1;
					stmt.setInt(idx++, row.getUid());
					stmt.setString(idx++, row.getRevKind());
					stmt.setString(idx++, row.getRevDay());
					stmt.setInt(idx++, row.getSchedulerInfoUid());
					stmt.setInt(idx++, row.getSchedulerDetailUid());
					stmt.setString(idx++, row.getCodeKind());
					stmt.setString(idx++, row.getCode());
					stmt.setString(idx++, row.getStartDate());
					stmt.setString(idx++, row.getEndDate());
					stmt.setString(idx++, row.getRemark());
					stmt.setString(idx++, row.getIsReport());
					stmt.setInt(idx++, row.getInsertBy());
					stmt.setString(idx++, row.getInsertDate());
					stmt.setInt(idx++, row.getUpdateBy());
					stmt.setString(idx++, row.getUpdateDate());
					
					stmt.executeUpdate();
					stmt.close();
				}
			}
			
			if(revScheTcNoteFileInfoList != null) {
				sql = "INSERT INTO REVSCHETCNOTEFILEINFO (ORGUID, REVKIND, REVDAY, SCHEDULERINFOUID, SCHETCNOTEUID, FILENAME, SAVENAME, FILESIZE, FILETYPE, INSERTBY, INSERTDATE)" + 
					"	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				for(int i = 0; i < revScheTcNoteFileInfoList.size(); i++) {
					ScheTcNoteFileInfoBean row = revScheTcNoteFileInfoList.get(i);
					stmt = conn.prepareStatement(sql);
					
					idx = 1;
					stmt.setInt(idx++, row.getUid());
					stmt.setString(idx++, row.getRevKind());
					stmt.setString(idx++, row.getRevDay());
					stmt.setInt(idx++, row.getSchedulerInfoUid());
					stmt.setInt(idx++, row.getScheTcNoteUid());
					stmt.setString(idx++, row.getFileName());
					stmt.setString(idx++, row.getSaveName());
					stmt.setLong(idx++, row.getFileSize());
					stmt.setString(idx++, row.getFileType());
					stmt.setInt(idx++, row.getInsertBy());
					stmt.setString(idx++, row.getInsertDate());
					
					stmt.executeUpdate();
					stmt.close();
				}
			}
			
			if(revScheTcNoteTcInfoList != null) {
				sql = "INSERT INTO REVSCHETCNOTETCINFO (ORGUID, REVKIND, REVDAY, SCHEDULERINFOUID, SCHETCNOTEUID, SCHEDULERDETAILUID, INSERTBY, INSERTDATE)" + 
					"	VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				
				for(int i = 0; i < revScheTcNoteTcInfoList.size(); i++) {
					ScheTcNoteTcInfoBean row = revScheTcNoteTcInfoList.get(i);
					stmt = conn.prepareStatement(sql);
					
					idx = 1;
					stmt.setInt(idx++, row.getUid());
					stmt.setString(idx++, row.getRevKind());
					stmt.setString(idx++, row.getRevDay());
					stmt.setInt(idx++, row.getSchedulerInfoUid());
					stmt.setInt(idx++, row.getScheTcNoteUid());
					stmt.setInt(idx++, row.getSchedulerDetailUid());
					stmt.setInt(idx++, row.getInsertBy());
					stmt.setString(idx++, row.getInsertDate());
					
					stmt.executeUpdate();
					stmt.close();
				}
			}
			
			conn.commit();
		}catch(Exception e) {
			isError = true;
			conn.rollback();
			e.printStackTrace();
		}finally {
			try {
				conn.setAutoCommit(true);
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put("revSchedulerInfoBean", revSchedulerInfoBean);
		resultMap.put("revSchedulerVersionInfoBean", revSchedulerVersionInfoBean);
		resultMap.put("revSchedulerDetailInfoList", revSchedulerDetailInfoList);
		resultMap.put("revScheDateTimeList", revScheDateTimeList);
		resultMap.put("revScheTrialInfoBean", revScheTrialInfoBean);
		resultMap.put("revScheCrewList", revScheCrewList);
		resultMap.put("revScheCrewInOutList", revScheCrewInOutList);
		resultMap.put("revScheTcNoteList", revScheTcNoteList);
		resultMap.put("revScheTcNoteFileInfoList", revScheTcNoteFileInfoList);
		resultMap.put("revScheTcNoteTcInfoList", revScheTcNoteTcInfoList);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
}
