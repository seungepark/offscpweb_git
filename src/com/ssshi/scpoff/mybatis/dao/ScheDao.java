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

import com.mysql.jdbc.Statement;
import com.ssshi.scpoff.constant.Const;
import com.ssshi.scpoff.dto.DomainBean;
import com.ssshi.scpoff.dto.DomainInfoBean;
import com.ssshi.scpoff.dto.ParamBean;
import com.ssshi.scpoff.dto.ScheCrewBean;
import com.ssshi.scpoff.dto.ScheCrewInOutBean;
import com.ssshi.scpoff.dto.ScheMailBean;
import com.ssshi.scpoff.dto.ScheReportCompBean;
import com.ssshi.scpoff.dto.ScheReportDailyBean;
import com.ssshi.scpoff.dto.ScheReportDepartureBean;
import com.ssshi.scpoff.dto.ScheTcNoteBean;
import com.ssshi.scpoff.dto.ScheTcNoteFileInfoBean;
import com.ssshi.scpoff.dto.ScheTcNoteTcInfoBean;
import com.ssshi.scpoff.dto.ScheTrialInfoBean;
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
import com.ssshi.scpoff.util.ValidUtil;

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

public class ScheDao {

	public static Map<String, Object> getDomainInfoListByDomainID(String domain) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<DomainInfoBean> list = new ArrayList<DomainInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT VAL, DESCRIPTION" + 
					"		FROM DOMAININFO" + 
					"		WHERE DOMAINUID IN (SELECT UID FROM DOMAIN D WHERE UPPER(D.DOMAIN) = UPPER('" + domain + "'))" + 
					"		ORDER BY UID";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				DomainInfoBean row = new DomainInfoBean();
				row.setVal(rs.getString(idx++));
				row.setDescription(rs.getString(idx++));
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> getScheList(SchedulerInfoBean bean) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<SchedulerInfoBean> list = new ArrayList<SchedulerInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT SQL_CALC_FOUND_ROWS" + 
					"			S.UID" + 
					"			, S.HULLNUM" + 
					"			, S.SCHEDTYPE" + 
					"			, (SELECT DESCRIPTION FROM DOMAININFO D WHERE DOMAINUID IN (SELECT UID FROM DOMAIN WHERE DOMAIN = 'SHIPTYPE') AND D.VAL = S.SHIPTYPE) AS SHIPTYPE" + 
					"			, DATE_FORMAT(S.SDATE, '%Y-%m-%d') as SDATE" + 
					"			, DATE_FORMAT(S.EDATE, '%Y-%m-%d') AS EDATE" + 
					"			, S.STATUS" + 
					"			, S.ISOFF" + 
					"			, TI.TRIALSTATUS" + 
					"			, SI.REGOWNER" + 
					"			, S.TRIALKEY" + 
					"			, SI.PROJSEQ" + 
					"			, DATE_FORMAT(S.INSERTDATE, '%Y-%m-%d %H:%i:%S') AS INSERTDATE" + 
					"			, CONCAT(UI.FIRSTNAME, ' ', UI.LASTNAME) AS INSERTNAME" + 
					"		FROM SCHEDULERINFO S" + 
					"		INNER JOIN SCHEDULERVERSIONINFO SVI ON (S.SCHEDULERVERSIONINFOUID = SVI.UID AND S.REVNUM = SVI.PLANREVNUM)" + 
					"		LEFT OUTER JOIN SCHETRIALINFO TI ON (S.UID = TI.SCHEDULERINFOUID)" + 
					"		LEFT OUTER JOIN SHIPINFO SI ON (S.HULLNUM = SI.SHIPNUM)" + 
					"		LEFT OUTER JOIN USERINFO UI ON (S.INSERTBY = UI.UID)" + 
					"		WHERE S.STATUS = 'ACT'";
			
			if(bean.getShiptype() != null && !"ALL".equals(bean.getShiptype())) {
				sql += " AND S.SHIPTYPE = '" + bean.getShiptype() + "'";
			}
			
			if(bean.getHullnum() != null && bean.getHullnum().length() > 0) {
				sql += " AND LOWER(S.HULLNUM) LIKE CONCAT('%', LOWER('" + bean.getHullnum() + "'), '%')";
			}
			
			if(bean.getOwnerName() != null && bean.getOwnerName().length() > 0) {
				sql += " AND OWNER IN (SELECT U.UID FROM USERINFO U WHERE LOWER(U.FIRSTNAME) LIKE CONCAT('%', LOWER('" + bean.getOwnerName() + "'), '%') OR LOWER(U.LASTNAME) LIKE CONCAT('%', LOWER('" + bean.getOwnerName() + "'), '%') )";
			}
			
			String sort = bean.getSort();
			
			if(sort != null) {
				if("shiptype".equals(sort)) {
					sql += " ORDER BY S.SHIPTYPE";
				}else if("hullnum".equals(sort)) {
					sql += " ORDER BY S.HULLNUM";
				}else if("desc".equals(sort)) {
					sql += " ORDER BY S.DESCRIPTION";
				}else if("ownerName".equals(sort)) {
					sql += " ORDER BY S.OWNER";
				}else if("department".equals(sort)) {
					sql += " ORDER BY S.DEPARTMENT";
				}else if("sdate".equals(sort)) {
					sql += " ORDER BY S.SDATE";
				}else if("edate".equals(sort)) {
					sql += " ORDER BY S.EDATE";
				}else if("trialStatus".equals(sort)) {
					sql += " ORDER BY TI.TRIALSTATUS";
				}else if("status".equals(sort)) {
					sql += " ORDER BY S.STATUS";
				}else if("regOwner".equals(sort)) {
					sql += " ORDER BY SI.REGOWNER";
				}else if("insertName".equals(sort)) {
					sql += " ORDER BY CONCAT(UI.FIRSTNAME, ' ', UI.LASTNAME)";
				}else if("schedType".equals(sort)) {
					sql += " ORDER BY S.SCHEDTYPE";
				}else if("series".equals(sort)) {
					sql += " ORDER BY SI.MAINHULLNUM, SI.SEQNO";
				}else {
					sql += " ORDER BY S.INSERTDATE";
				}
			}else {
				sql += " ORDER BY S.INSERTDATE";
			}
			
			if(bean.getOrder() != null && "desc".equals(bean.getOrder())) {
				sql += " DESC";
			}else {
				sql += " ASC";
			}
			
			sql += " LIMIT " + bean.getStart() + ", " + bean.getLimit();
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				SchedulerInfoBean row = new SchedulerInfoBean();
				row.setUid(rs.getInt(idx++));
				row.setHullnum(rs.getString(idx++));
				row.setSchedtype(rs.getString(idx++));
				row.setShiptype(rs.getString(idx++));
				row.setSdate(rs.getString(idx++));
				row.setEdate(rs.getString(idx++));
				row.setStatus(rs.getString(idx++));
				row.setIsOff(rs.getString(idx++));
				row.setTrialStatus(rs.getString(idx++));
				row.setRegOwner(rs.getString(idx++));
				row.setTrialKey(rs.getString(idx++));
				row.setProjSeq(rs.getString(idx++));
				row.setInsertdate(rs.getString(idx++));
				row.setInsertName(rs.getString(idx++));
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> getScheListCnt() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int totalCnt = 0;
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT FOUND_ROWS()";
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				totalCnt = rs.getInt(1);
				break;
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST_CNT, totalCnt);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> checkDownData(String uidList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Integer> list = new ArrayList<Integer>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT SI.UID" + 
					"	FROM SCHEDULERINFO SI" + 
					"		LEFT OUTER JOIN SCHETRIALINFO ST ON (SI.UID = ST.SCHEDULERINFOUID)" + 
					"	WHERE SI.STATUS = 'ACT' AND (ISOFF != 'Y' OR ISOFF IS NULL) AND (ST.TRIALSTATUS IS NULL OR ST.TRIALSTATUS = 'DEPART') AND SI.UID IN (" + uidList + ")";

			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				list.add(rs.getInt(1));
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downSchedulerInfo(String uidList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<SchedulerInfoBean> list = new ArrayList<SchedulerInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, HULLNUM, SHIPTYPE, DESCRIPTION, OWNER, DEPARTMENT, SDATE, EDATE" + 
					"		, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE, STATUS, SCHEDTYPE, SCHECREWSDATE" + 
					"		, SCHECREWPERIOD, SCHEDULERVERSIONINFOUID, REVNUM, TRIALKEY, KEYNO" + 
					"	FROM SCHEDULERINFO" + 
					"	WHERE UID IN (" + uidList + ")";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				SchedulerInfoBean row = new SchedulerInfoBean();
				row.setUid(rs.getInt(idx++));
				row.setHullnum(rs.getString(idx++));
				row.setShiptype(rs.getString(idx++));
				row.setDescription(rs.getString(idx++));
				row.setOwner(rs.getInt(idx++));
				row.setDepartment(rs.getString(idx++));
				row.setSdate(rs.getString(idx++));
				row.setEdate(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setInsertdate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				row.setStatus(rs.getString(idx++));
				row.setSchedtype(rs.getString(idx++));
				row.setScheCrewSdate(rs.getString(idx++));
				row.setScheCrewPeriod(rs.getInt(idx++));
				row.setSchedulerVersionInfoUid(rs.getInt(idx++));
				row.setRevnum(rs.getString(idx++));
				row.setTrialKey(rs.getString(idx++));
				row.setKeyNo(rs.getInt(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downSchedulerVersionInfo(String uidList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<SchedulerVersionInfoBean> list = new ArrayList<SchedulerVersionInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, PLANREVNUM, EXECREVNUM, COMPREVNUM" + 
					"		, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE" + 
					"	FROM SCHEDULERVERSIONINFO" + 
					"	WHERE UID IN (" + uidList + ")";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				SchedulerVersionInfoBean row = new SchedulerVersionInfoBean();
				row.setUid(rs.getInt(idx++));
				row.setPlanRevNum(rs.getString(idx++));
				row.setExecRevNum(rs.getString(idx++));
				row.setCompRevNum(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downSchedulerDetail(String uidList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<SchedulerDetailInfoBean> list = new ArrayList<SchedulerDetailInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, SCHEDINFOUID, CATEGORY, TCNUM, DESCRIPTION, CTYPE, LOADRATE, DTYPE" + 
					"		, CASE WHEN SDATE = '0000-00-00' THEN '0000-00-00' ELSE SDATE END AS SDATE, STIME" + 
					"		, CASE WHEN EDATE = '0000-00-00' THEN '0000-00-00' ELSE EDATE END AS EDATE, ETIME" + 
					"		, SEQ, PER, NOTE, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE" + 
					"		, READYTIME, CODEDETUID, SAMETCNUM, PERFORMANCESDATE, PERFORMANCESTIME, PERFORMANCEEDATE, PERFORMANCEETIME, CODEDETTCNUM, CODEDETDESC" + 
					"	FROM SCHEDULERDETAIL" + 
					"	WHERE SCHEDINFOUID IN (" + uidList + ")";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				SchedulerDetailInfoBean row = new SchedulerDetailInfoBean();
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
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downScheTrialInfo(String uidList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ScheTrialInfoBean> list = new ArrayList<ScheTrialInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, SCHEDULERINFOUID, TRIAL1SCHEDULERINFOUID, TRIAL2SCHEDULERINFOUID" + 
					"		, ITPTOTALREMAIN, ITPTOTALBASE, ITPTRIALREMAIN, ITPTRIALBASE" + 
					"		, ITPOUTFITTINGREMAIN, ITPOUTFITTINGBASE, PUNCHTOTAL, PUNCHTRIAL, PUNCHOUTFITTING" + 
					"		, FUELHFOSCHEDULER, FUELHFOPERFORMANCE, FUELHFOTEMP, FUELHFOUP, FUELHFODOWN" + 
					"		, FUELMGOSCHEDULER, FUELMGOPERFORMANCE, FUELMGOTEMP, FUELMGOUP, FUELMGODOWN" + 
					"		, FUELMDOSCHEDULER, FUELMDOPERFORMANCE, FUELMDOTEMP, FUELMDOUP, FUELMDODOWN" + 
					"		, FUELLNGSCHEDULER, FUELLNGPERFORMANCE, FUELLNGTEMP, FUELLNGUP, FUELLNGDOWN" + 
					"		, DRAFTFWDSCHEDULER, DRAFTFWDPERFORMANCE, DRAFTFWDTEMP, DRAFTFWDUP, DRAFTFWDDOWN" + 
					"		, DRAFTMIDSCHEDULER, DRAFTMIDPERFORMANCE, DRAFTMIDTEMP, DRAFTMIDUP, DRAFTMIDDOWN" + 
					"		, DRAFTAFTSCHEDULER, DRAFTAFTPERFORMANCE, DRAFTAFTTEMP, DRAFTAFTUP, DRAFTAFTDOWN" + 
					"		, CREWREMARK, REMARK, CONTRACTSPEED, MEASURESPEED, COMPREMARK, NOISEVIBRATION, TRIALSTATUS, ONGOCHANGEDATE" + 
					"		, INSERTDATE, INSERTBY, UPDATEDATE, UPDATEBY" + 
					"	FROM SCHETRIALINFO" + 
					"	WHERE SCHEDULERINFOUID IN (" + uidList + ")";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				ScheTrialInfoBean row = new ScheTrialInfoBean();
				row.setUid(rs.getInt(idx++));
				row.setSchedulerInfoUid(rs.getInt(idx++));
				row.setTrial1SchedulerInfoUid(rs.getInt(idx++));
				row.setTrial2SchedulerInfoUid(rs.getInt(idx++));
				row.setItpTotalRemain(ValidUtil.getInteger(rs, idx++));
				row.setItpTotalBase(ValidUtil.getInteger(rs, idx++));
				row.setItpTrialRemain(ValidUtil.getInteger(rs, idx++));
				row.setItpTrialBase(ValidUtil.getInteger(rs, idx++));
				row.setItpOutfittingRemain(ValidUtil.getInteger(rs, idx++));
				row.setItpOutfittingBase(ValidUtil.getInteger(rs, idx++));
				row.setPunchTotal(ValidUtil.getInteger(rs, idx++));
				row.setPunchTrial(ValidUtil.getInteger(rs, idx++));
				row.setPunchOutfitting(ValidUtil.getInteger(rs, idx++));
				row.setFuelHfoScheduler(ValidUtil.getFloat(rs, idx++));
				row.setFuelHfoPerformance(ValidUtil.getFloat(rs, idx++));
				row.setFuelHfoTemp(ValidUtil.getFloat(rs, idx++));
				row.setFuelHfoUp(ValidUtil.getFloat(rs, idx++));
				row.setFuelHfoDown(ValidUtil.getFloat(rs, idx++));
				row.setFuelMgoScheduler(ValidUtil.getFloat(rs, idx++));
				row.setFuelMgoPerformance(ValidUtil.getFloat(rs, idx++));
				row.setFuelMgoTemp(ValidUtil.getFloat(rs, idx++));
				row.setFuelMgoUp(ValidUtil.getFloat(rs, idx++));
				row.setFuelMgoDown(ValidUtil.getFloat(rs, idx++));
				row.setFuelMdoScheduler(ValidUtil.getFloat(rs, idx++));
				row.setFuelMdoPerformance(ValidUtil.getFloat(rs, idx++));
				row.setFuelMdoTemp(ValidUtil.getFloat(rs, idx++));
				row.setFuelMdoUp(ValidUtil.getFloat(rs, idx++));
				row.setFuelMdoDown(ValidUtil.getFloat(rs, idx++));
				row.setFuelLngScheduler(ValidUtil.getFloat(rs, idx++));
				row.setFuelLngPerformance(ValidUtil.getFloat(rs, idx++));
				row.setFuelLngTemp(ValidUtil.getFloat(rs, idx++));
				row.setFuelLngUp(ValidUtil.getFloat(rs, idx++));
				row.setFuelLngDown(ValidUtil.getFloat(rs, idx++));
				row.setDraftFwdScheduler(ValidUtil.getFloat(rs, idx++));
				row.setDraftFwdPerformance(ValidUtil.getFloat(rs, idx++));
				row.setDraftFwdTemp(ValidUtil.getFloat(rs, idx++));
				row.setDraftFwdUp(ValidUtil.getFloat(rs, idx++));
				row.setDraftFwdDown(ValidUtil.getFloat(rs, idx++));
				row.setDraftMidScheduler(ValidUtil.getFloat(rs, idx++));
				row.setDraftMidPerformance(ValidUtil.getFloat(rs, idx++));
				row.setDraftMidTemp(ValidUtil.getFloat(rs, idx++));
				row.setDraftMidUp(ValidUtil.getFloat(rs, idx++));
				row.setDraftMidDown(ValidUtil.getFloat(rs, idx++));
				row.setDraftAftScheduler(ValidUtil.getFloat(rs, idx++));
				row.setDraftAftPerformance(ValidUtil.getFloat(rs, idx++));
				row.setDraftAftTemp(ValidUtil.getFloat(rs, idx++));
				row.setDraftAftUp(ValidUtil.getFloat(rs, idx++));
				row.setDraftAftDown(ValidUtil.getFloat(rs, idx++));
				row.setCrewRemark(rs.getString(idx++));
				row.setRemark(rs.getString(idx++));
				row.setContractSpeed(ValidUtil.getFloat(rs, idx++));
				row.setMeasureSpeed(ValidUtil.getFloat(rs, idx++));
				row.setCompRemark(rs.getString(idx++));
				row.setNoiseVibration(rs.getString(idx++));
				row.setTrialStatus(rs.getString(idx++));
				row.setOngoChangeDate(rs.getString(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downScheCrew(String uidList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ScheCrewBean> list = new ArrayList<ScheCrewBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, SCHEDULERINFOUID, KIND, COMPANY, DEPARTMENT, NAME, RANK, IDNO" + 
					"		, WORKTYPE1, WORKTYPE2, MAINSUB, FOODSTYLE, PERSONNO" + 
					"		, PHONE, ISPLAN, ISNONE, INSERTDATE, INSERTBY, UPDATEDATE, UPDATEBY" + 
					"		, TRIALKEY, PROJECT, PROJNO" + 
					"	FROM SCHECREW" + 
					"	WHERE SCHEDULERINFOUID IN (" + uidList + ")";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				ScheCrewBean row = new ScheCrewBean();
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
				row.setInsertDate(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				row.setTrialKey(rs.getString(idx++));
				row.setProject(rs.getString(idx++));
				row.setProjNo(rs.getString(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downScheCrewInOut(String uidList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ScheCrewInOutBean> list = new ArrayList<ScheCrewInOutBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT SCIO.UID, SCIO.SCHECREWUID, SC.SCHEDULERINFOUID, SCIO.INOUTDATE, SCIO.SCHEDULERINOUT, SCIO.PERFORMANCEINOUT" + 
					"		, SCIO.INSERTDATE, SCIO.INSERTBY, SCIO.UPDATEDATE, SCIO.UPDATEBY" + 
					"	FROM SCHECREWINOUT SCIO" + 
					"		INNER JOIN SCHECREW SC ON SCIO.SCHECREWUID = SC.UID" +
					"	WHERE SCIO.SCHECREWUID IN (" + uidList + ")";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				ScheCrewInOutBean row = new ScheCrewInOutBean();
				row.setUid(rs.getInt(idx++));
				row.setScheCrewUid(rs.getInt(idx++));
				row.setSchedulerInfoUid(rs.getInt(idx++));
				row.setInOutDate(rs.getString(idx++));
				row.setSchedulerInOut(rs.getString(idx++));
				row.setPerformanceInOut(rs.getString(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downSchedulerInfoForSearch() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<SchedulerInfoBean> list = new ArrayList<SchedulerInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT SI.UID, SI.HULLNUM, SI.SHIPTYPE, SI.DESCRIPTION, SI.SDATE, SI.EDATE" + 
					"	FROM SCHEDULERINFO SI" + 
					"		LEFT OUTER JOIN SCHETRIALINFO ST ON (SI.UID = ST.SCHEDULERINFOUID)" + 
					"	WHERE ST.TRIALSTATUS = 'ARRIVE'";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				SchedulerInfoBean row = new SchedulerInfoBean();
				row.setUid(rs.getInt(idx++));
				row.setHullnum(rs.getString(idx++));
				row.setShiptype(rs.getString(idx++));
				row.setDescription(rs.getString(idx++));
				row.setSdate(rs.getString(idx++));
				row.setEdate(rs.getString(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downDomain() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<DomainBean> list = new ArrayList<DomainBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, SHIPINFOUID, DOMAIN, DESCRIPTION, DATATYPE, CAT, STATUS" + 
					"		, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE" + 
					"	FROM DOMAIN";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				DomainBean row = new DomainBean();
				row.setUid(rs.getInt(idx++));
				row.setShipInfoUid(rs.getInt(idx++));
				row.setDomain(rs.getString(idx++));
				row.setDescription(rs.getString(idx++));
				row.setDataType(rs.getString(idx++));
				row.setCat(rs.getString(idx++));
				row.setStatus(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downDomainInfo() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<DomainInfoBean> list = new ArrayList<DomainInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, DOMAINUID, VAL, INVAL, DESCRIPTION, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE" + 
					"	FROM DOMAININFO";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				DomainInfoBean row = new DomainInfoBean();
				row.setUid(rs.getInt(idx++));
				row.setDomainUid(rs.getInt(idx++));
				row.setVal(rs.getString(idx++));
				row.setInVal(rs.getString(idx++));
				row.setDescription(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downUserInfo() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<UserInfoBean> list = new ArrayList<UserInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, SHIPINFOUID, USERID, PW, FIRSTNAME, LASTNAME, POSCODE, LANGCODE, STATUS" + 
					"		, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE" + 
					"	FROM USERINFO";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				UserInfoBean row = new UserInfoBean();
				row.setUid(rs.getInt(idx++));
				row.setShipInfoUid(rs.getInt(idx++));
				row.setUserId(rs.getString(idx++));
				row.setPw(rs.getString(idx++));
				row.setFirstName(rs.getString(idx++));
				row.setLastName(rs.getString(idx++));
				row.setPosCode(rs.getString(idx++));
				row.setLangCode(rs.getString(idx++));
				row.setStatus(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downShipInfo() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ShipInfoBean> list = new ArrayList<ShipInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, SHIPNUM, MAINHULLNUM, PROJSEQ, SEQNO, TITLE, IMO, MMSI, SHIPTYPE, REGOWNER" + 
					"		, BUILTDATE, FLAG, GROSSTON, DWT, LOA, DRAUGHT, BUILDER, BUILTBY, WORKFINISH, SHIPCLASS" + 
					"		, TYPEMODEL, DOCK, LOC, ISSG, ISLOAD, ISUNLOAD, ISCOLD, ISTRIAL, FUEL, LC" + 
					"		, CREW1PRO, CREW1LEAD, CREW1ACT, CREW2PRO, CREW2LEAD, CREW2ACT, CREW3PRO, CREW3LEAD, CREW3ACT" + 
					"		, COMMAIN, COMSUB, OPERATE, ISDEFAULT" + 
					"		, STATUS, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE" + 
					"	FROM SHIPINFO";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				ShipInfoBean row = new ShipInfoBean();
				row.setUid(rs.getInt(idx++));
				row.setShipNum(rs.getString(idx++));
				row.setMainHullNum(rs.getString(idx++));
				row.setProjSeq(rs.getString(idx++));
				row.setSeqNo(rs.getInt(idx++));
				row.setTitle(rs.getString(idx++));
				row.setImo(rs.getString(idx++));
				row.setMmsi(rs.getString(idx++));
				row.setShipType(rs.getString(idx++));
				row.setRegOwner(rs.getString(idx++));
				row.setBuiltDate(rs.getString(idx++));
				row.setFlag(rs.getString(idx++));
				row.setGrossTon(rs.getString(idx++));
				row.setDwt(rs.getString(idx++));
				row.setLoa(rs.getString(idx++));
				row.setDraught(rs.getString(idx++));
				row.setBuilder(rs.getString(idx++));
				row.setBuiltBy(rs.getString(idx++));
				row.setWorkFinish(rs.getString(idx++));
				row.setShipClass(rs.getString(idx++));
				row.setTypeModel(rs.getString(idx++));
				row.setDock(rs.getString(idx++));
				row.setLoc(rs.getString(idx++));
				row.setIsSg(rs.getString(idx++));
				row.setIsLoad(rs.getString(idx++));
				row.setIsUnload(rs.getString(idx++));
				row.setIsCold(rs.getString(idx++));
				row.setIsTrial(rs.getString(idx++));
				row.setFuel(rs.getString(idx++));
				row.setLc(rs.getString(idx++));
				row.setCrew1Pro(rs.getString(idx++));
				row.setCrew1Lead(rs.getString(idx++));
				row.setCrew1Act(rs.getString(idx++));
				row.setCrew2Pro(rs.getString(idx++));
				row.setCrew2Lead(rs.getString(idx++));
				row.setCrew2Act(rs.getString(idx++));
				row.setCrew3Pro(rs.getString(idx++));
				row.setCrew3Lead(rs.getString(idx++));
				row.setCrew3Act(rs.getString(idx++));
				row.setComMain(rs.getString(idx++));
				row.setComSub(rs.getString(idx++));
				row.setOperate(rs.getString(idx++));
				row.setIsDefault(rs.getString(idx++));
				row.setStatus(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downScheMail() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ScheMailBean> list = new ArrayList<ScheMailBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, NAME, COMPANY, DEPARTMENT, RANK, EMAIL, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE" + 
					"	FROM SCHEMAIL";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				ScheMailBean row = new ScheMailBean();
				row.setUid(rs.getInt(idx++));
				row.setName(rs.getString(idx++));
				row.setCompany(rs.getString(idx++));
				row.setDepartment(rs.getString(idx++));
				row.setRank(rs.getString(idx++));
				row.setEmail(rs.getString(idx++));
				row.setInsertBy(rs.getInt(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setUpdateBy(rs.getInt(idx++));
				row.setUpdateDate(rs.getString(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downScheHierarchy() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ScheduleHierarchyBean> list = new ArrayList<ScheduleHierarchyBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, CODE, DISPLAYCODE, DESCRIPTION, CODELEVEL, PARENTUID" + 
					"	FROM SCHEDULEHIERARCHY";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				ScheduleHierarchyBean row = new ScheduleHierarchyBean();
				row.setUid(rs.getInt(idx++));
				row.setCode(rs.getString(idx++));
				row.setDisplaycode(rs.getString(idx++));
				row.setDescription(rs.getString(idx++));
				row.setCodelevel(rs.getInt(idx++));
				row.setParentuid(rs.getString(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downScheduleCodeDetail() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ScheduleCodeDetailBean> list = new ArrayList<ScheduleCodeDetailBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, SCHECODEINFOUID, LV1CODE, LV2CODE, LV3CODE" + 
					"		, LV4CODE, DISPLAYCODE, DESCRIPTION, CODELEVEL, DTYPE, CTYPE" + 
					"		, LOADSTR, PER, READYTIME, LOADRATE" + 
					"		, SCHEHIERARCHYUID, SEQ, SAMETCNUM" + 
					"	FROM SCHEDULECODEDETAIL";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				ScheduleCodeDetailBean row = new ScheduleCodeDetailBean();
				row.setUid(rs.getInt(idx++));
				row.setSchecodeinfouid(rs.getInt(idx++));
				row.setLv1code(rs.getString(idx++));
				row.setLv2code(rs.getString(idx++));
				row.setLv3code(rs.getString(idx++));
				row.setLv4code(rs.getString(idx++));
				row.setDisplaycode(rs.getString(idx++));
				row.setDescription(rs.getString(idx++));
				row.setCodelevel(rs.getInt(idx++));
				row.setDtype(rs.getString(idx++));
				row.setCtype(rs.getString(idx++));
				row.setLoadstr(rs.getString(idx++));
				row.setPer(rs.getFloat(idx++));
				row.setReadytime(rs.getFloat(idx++));
				row.setLoadrate(rs.getFloat(idx++));
				row.setSchehierarchyuid(rs.getInt(idx++));
				row.setSeq(rs.getFloat(idx++));
				row.setSametcnum(rs.getString(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downScheduleCodeInfo() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ScheduleCodeInfoBean> list = new ArrayList<ScheduleCodeInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, SHIPTYPE, DESCRIPTION, STATUS, REVNUM, SCHEDTYPE" + 
					"	FROM SCHEDULECODEINFO";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				ScheduleCodeInfoBean row = new ScheduleCodeInfoBean();
				row.setUid(rs.getInt(idx++));
				row.setShiptype(rs.getString(idx++));
				row.setDescription(rs.getString(idx++));
				row.setStatus(rs.getString(idx++));
				row.setRevnum(rs.getInt(idx++));
				row.setSchedtype(rs.getString(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downVsslReqInfo() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<VesselReqInfoBean> list = new ArrayList<VesselReqInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, HULLNUM, SHIPTYPE, DESCRIPTION, STATUS, REGISTERDOWNER, GROSSTONNAGE, DRAWN, CHECKED, MANAGER, SCHEDINFOUID" + 
					"	FROM VSSLREQINFO";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				VesselReqInfoBean row = new VesselReqInfoBean();
				row.setUid(rs.getInt(idx++));
				row.setHullNum(rs.getString(idx++));
				row.setShiptype(rs.getString(idx++));
				row.setDescription(rs.getString(idx++));
				row.setStatus(rs.getString(idx++));
				row.setRegisterdowner(rs.getString(idx++));
				row.setGrosstonnage(rs.getString(idx++));
				row.setDrawn(rs.getString(idx++));
				row.setChecked(rs.getString(idx++));
				row.setManager(rs.getString(idx++));
				row.setSchedinfouid(rs.getInt(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downVsslReqInfoDetail() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<VesselReqInfoDetailBean> list = new ArrayList<VesselReqInfoDetailBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, VSSLREQINFOUID, SEQ, REQINFOTITLE, ITEM, UNIT, NAME, RPM, LOADRATE" + 
					"	FROM VSSLREQINFODETAIL";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				VesselReqInfoDetailBean row = new VesselReqInfoDetailBean();
				row.setUid(rs.getInt(idx++));
				row.setVsslreqinfouid(rs.getInt(idx++));
				row.setSeq(rs.getInt(idx++));
				row.setReqinfotitle(rs.getString(idx++));
				row.setItem(rs.getString(idx++));
				row.setUnit(rs.getString(idx++));
				row.setName(rs.getString(idx++));
				row.setRpm(rs.getString(idx++));
				row.setLoadrate(rs.getString(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> downShipCond() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ShipCondBean> list = new ArrayList<ShipCondBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, SCHEDINFOUID, COND, SDATE, STIME, EDATE, ETIME" + 
					"	FROM SHIPCOND";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				ShipCondBean row = new ShipCondBean();
				row.setUid(rs.getInt(idx++));
				row.setSchedinfouid(rs.getInt(idx++));
				row.setCond(rs.getString(idx++));
				row.setSdate(rs.getString(idx++));
				row.setStime(rs.getString(idx++));
				row.setEdate(rs.getString(idx++));
				row.setEtime(rs.getString(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static boolean changeOffline(String uidList, int userUid) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			String sql = "UPDATE SCHEDULERINFO SET ISOFF = 'Y', OFFDATE = SYSDATE(), OFFBY = " + userUid + 
					"	WHERE UID IN (" + uidList + ")";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
	
	public static boolean uploadScheInfo(SchedulerInfoBean bean, ScheTrialInfoBean trialBean) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			conn.setAutoCommit(false);
			
			String sql = "UPDATE SCHEDULERINFO SET HULLNUM = ?, SHIPTYPE = ?, DESCRIPTION = ?, OWNER = ?, DEPARTMENT = ?, SDATE = ?, EDATE = ?," + 
					"			UPDATEBY = ?, UPDATEDATE = ?, STATUS = ?," + 
					"			SCHEDTYPE = ?, SCHECREWSDATE = ?, SCHECREWPERIOD = ?, SCHEDULERVERSIONINFOUID = ?, REVNUM = ?, TRIALKEY = ?, KEYNO = ?" + 
					"	WHERE UID = ?";
			
			int idx = 1;
			stmt = conn.prepareStatement(sql);
			stmt.setString(idx++, bean.getHullnum());
			stmt.setString(idx++, bean.getShiptype());
			stmt.setString(idx++, bean.getDescription());
			stmt.setInt(idx++, bean.getOwner());
			stmt.setString(idx++, bean.getDepartment());
			stmt.setString(idx++, bean.getSdate());
			stmt.setString(idx++, bean.getEdate());
			stmt.setInt(idx++, bean.getUpdateBy());
			stmt.setString(idx++, bean.getUpdateDate());
			stmt.setString(idx++, bean.getStatus());
			stmt.setString(idx++, bean.getSchedtype());
			stmt.setString(idx++, bean.getScheCrewSdate());
			stmt.setInt(idx++, bean.getScheCrewPeriod());
			stmt.setInt(idx++, bean.getSchedulerVersionInfoUid());
			stmt.setString(idx++, bean.getRevnum());
			stmt.setString(idx++, bean.getTrialKey());
			stmt.setInt(idx++, bean.getKeyNo());
			stmt.setInt(idx++, bean.getUid());
			stmt.executeUpdate();
			stmt.close();
			
			sql = "DELETE FROM SCHETRIALINFO WHERE SCHEDULERINFOUID = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, bean.getUid());
			stmt.executeUpdate();
			stmt.close();
			
			if(trialBean != null && trialBean.getSchedulerInfoUid() > 0) {
				sql = "INSERT INTO SCHETRIALINFO (SCHEDULERINFOUID, TRIAL1SCHEDULERINFOUID, TRIAL2SCHEDULERINFOUID, ITPTOTALREMAIN, ITPTOTALBASE, ITPTRIALREMAIN, ITPTRIALBASE, ITPOUTFITTINGREMAIN, ITPOUTFITTINGBASE," + 
						"		PUNCHTOTAL, PUNCHTRIAL, PUNCHOUTFITTING, FUELHFOSCHEDULER, FUELHFOPERFORMANCE, FUELHFOTEMP, FUELHFOUP, FUELHFODOWN," + 
						"		FUELMGOSCHEDULER, FUELMGOPERFORMANCE, FUELMGOTEMP, FUELMGOUP, FUELMGODOWN, FUELMDOSCHEDULER, FUELMDOPERFORMANCE, FUELMDOTEMP, FUELMDOUP, FUELMDODOWN," + 
						"		FUELLNGSCHEDULER, FUELLNGPERFORMANCE, FUELLNGTEMP, FUELLNGUP, FUELLNGDOWN, DRAFTFWDSCHEDULER, DRAFTFWDPERFORMANCE, DRAFTFWDTEMP, DRAFTFWDUP, DRAFTFWDDOWN," + 
						"		DRAFTMIDSCHEDULER, DRAFTMIDPERFORMANCE, DRAFTMIDTEMP, DRAFTMIDUP, DRAFTMIDDOWN, DRAFTAFTSCHEDULER, DRAFTAFTPERFORMANCE, DRAFTAFTTEMP, DRAFTAFTUP, DRAFTAFTDOWN," + 
						"		CREWREMARK, REMARK, CONTRACTSPEED, MEASURESPEED, COMPREMARK, NOISEVIBRATION, TRIALSTATUS, ONGOCHANGEDATE, INSERTDATE, INSERTBY, UPDATEDATE, UPDATEBY)" + 
						"	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?," + 
						"		?, ?, ?, ?, ?, ?, ?, ?," + 
						"		?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + 
						"		?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + 
						"		?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + 
						"		?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				idx = 1;
				stmt = conn.prepareStatement(sql);
				stmt.setInt(idx++, trialBean.getSchedulerInfoUid());
				stmt.setInt(idx++, trialBean.getTrial1SchedulerInfoUid());
				stmt.setInt(idx++, trialBean.getTrial2SchedulerInfoUid());
				
				if(trialBean.getItpTotalRemain() != null) {
					stmt.setInt(idx++, trialBean.getItpTotalRemain());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(trialBean.getItpTotalBase() != null) {
					stmt.setInt(idx++, trialBean.getItpTotalBase());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(trialBean.getItpTrialRemain() != null) {
					stmt.setInt(idx++, trialBean.getItpTrialRemain());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(trialBean.getItpTrialBase() != null) {
					stmt.setInt(idx++, trialBean.getItpTrialBase());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(trialBean.getItpOutfittingRemain() != null) {
					stmt.setInt(idx++, trialBean.getItpOutfittingRemain());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(trialBean.getItpOutfittingBase() != null) {
					stmt.setInt(idx++, trialBean.getItpOutfittingBase());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(trialBean.getPunchTotal() != null) {
					stmt.setInt(idx++, trialBean.getPunchTotal());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(trialBean.getPunchTrial() != null) {
					stmt.setInt(idx++, trialBean.getPunchTrial());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(trialBean.getPunchOutfitting() != null) {
					stmt.setInt(idx++, trialBean.getPunchOutfitting());
				}else {
					stmt.setNull(idx++, Types.INTEGER);
				}
				
				if(trialBean.getFuelHfoScheduler() != null) {
					stmt.setFloat(idx++, trialBean.getFuelHfoScheduler());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelHfoPerformance() != null) {
					stmt.setFloat(idx++, trialBean.getFuelHfoPerformance());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelHfoTemp() != null) {
					stmt.setFloat(idx++, trialBean.getFuelHfoTemp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelHfoUp() != null) {
					stmt.setFloat(idx++, trialBean.getFuelHfoUp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelHfoDown() != null) {
					stmt.setFloat(idx++, trialBean.getFuelHfoDown());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelMgoScheduler() != null) {
					stmt.setFloat(idx++, trialBean.getFuelMgoScheduler());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelMgoPerformance() != null) {
					stmt.setFloat(idx++, trialBean.getFuelMgoPerformance());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelMgoTemp() != null) {
					stmt.setFloat(idx++, trialBean.getFuelMgoTemp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelMgoUp() != null) {
					stmt.setFloat(idx++, trialBean.getFuelMgoUp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelMgoDown() != null) {
					stmt.setFloat(idx++, trialBean.getFuelMgoDown());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelMdoScheduler() != null) {
					stmt.setFloat(idx++, trialBean.getFuelMdoScheduler());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelMdoPerformance() != null) {
					stmt.setFloat(idx++, trialBean.getFuelMdoPerformance());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelMdoTemp() != null) {
					stmt.setFloat(idx++, trialBean.getFuelMdoTemp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelMdoUp() != null) {
					stmt.setFloat(idx++, trialBean.getFuelMdoUp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelMdoDown() != null) {
					stmt.setFloat(idx++, trialBean.getFuelMdoDown());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelLngScheduler() != null) {
					stmt.setFloat(idx++, trialBean.getFuelLngScheduler());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelLngPerformance() != null) {
					stmt.setFloat(idx++, trialBean.getFuelLngPerformance());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelLngTemp() != null) {
					stmt.setFloat(idx++, trialBean.getFuelLngTemp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelLngUp() != null) {
					stmt.setFloat(idx++, trialBean.getFuelLngUp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getFuelLngDown() != null) {
					stmt.setFloat(idx++, trialBean.getFuelLngDown());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftFwdScheduler() != null) {
					stmt.setFloat(idx++, trialBean.getDraftFwdScheduler());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftFwdPerformance() != null) {
					stmt.setFloat(idx++, trialBean.getDraftFwdPerformance());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftFwdTemp() != null) {
					stmt.setFloat(idx++, trialBean.getDraftFwdTemp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftFwdUp() != null) {
					stmt.setFloat(idx++, trialBean.getDraftFwdUp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftFwdDown() != null) {
					stmt.setFloat(idx++, trialBean.getDraftFwdDown());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftMidScheduler() != null) {
					stmt.setFloat(idx++, trialBean.getDraftMidScheduler());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftMidPerformance() != null) {
					stmt.setFloat(idx++, trialBean.getDraftMidPerformance());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftMidTemp() != null) {
					stmt.setFloat(idx++, trialBean.getDraftMidTemp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftMidUp() != null) {
					stmt.setFloat(idx++, trialBean.getDraftMidUp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftMidDown() != null) {
					stmt.setFloat(idx++, trialBean.getDraftMidDown());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftAftScheduler() != null) {
					stmt.setFloat(idx++, trialBean.getDraftAftScheduler());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftAftPerformance() != null) {
					stmt.setFloat(idx++, trialBean.getDraftAftPerformance());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftAftTemp() != null) {
					stmt.setFloat(idx++, trialBean.getDraftAftTemp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftAftUp() != null) {
					stmt.setFloat(idx++, trialBean.getDraftAftUp());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getDraftAftDown() != null) {
					stmt.setFloat(idx++, trialBean.getDraftAftDown());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				stmt.setString(idx++, trialBean.getCrewRemark());
				stmt.setString(idx++, trialBean.getRemark());
				
				if(trialBean.getContractSpeed() != null) {
					stmt.setFloat(idx++, trialBean.getContractSpeed());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}
				
				if(trialBean.getMeasureSpeed() != null) {
					stmt.setFloat(idx++, trialBean.getMeasureSpeed());
				}else {
					stmt.setNull(idx++, Types.DECIMAL);
				}

				stmt.setString(idx++, trialBean.getCompRemark());
				stmt.setString(idx++, trialBean.getNoiseVibration());
				stmt.setString(idx++, trialBean.getTrialStatus());
				stmt.setString(idx++, trialBean.getOngoChangeDate());
				stmt.setString(idx++, trialBean.getInsertDate());
				stmt.setInt(idx++, trialBean.getInsertBy());
				stmt.setString(idx++, trialBean.getUpdateDate());
				stmt.setInt(idx++, trialBean.getUpdateBy());
				stmt.executeUpdate();				
				stmt.close();
			}
			
			conn.commit();
		}catch(Exception e) {
			isError = true;
			conn.rollback();
			e.printStackTrace();
		}finally {
			try {
				conn.setAutoCommit(true);
				
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
	
	public static Map<String, Object> uploadScheDetailList(int scheInfoUid, List<SchedulerDetailInfoBean> list) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> delFileList = new ArrayList<String>();
		Map<Integer, Integer> newDetailUidList = new HashMap<Integer, Integer>();
		String sql = "";
		int idx = 1;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			conn.setAutoCommit(false);
			
			sql = "SELECT SAVENAME FROM SCHETCNOTEFILEINFO WHERE SCHEDULERINFOUID = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, scheInfoUid);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				delFileList.add(rs.getString(1));
			}
			
			stmt.close();
			
			sql = "DELETE FROM SCHEDULERDETAIL WHERE SCHEDINFOUID = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, scheInfoUid);
			stmt.executeUpdate();
			stmt.close();
			
			sql = "DELETE FROM SCHETCNOTE WHERE SCHEDULERINFOUID = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, scheInfoUid);
			stmt.executeUpdate();
			stmt.close();
			
			sql = "DELETE FROM SCHETCNOTETCINFO WHERE SCHEDULERINFOUID = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, scheInfoUid);
			stmt.executeUpdate();
			stmt.close();
			
			sql = "DELETE FROM SCHETCNOTEFILEINFO WHERE SCHEDULERINFOUID = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, scheInfoUid);
			stmt.executeUpdate();
			stmt.close();
			
			for(int i = 0; i < list.size(); i++) {
				SchedulerDetailInfoBean bean = list.get(i);
				
				sql = "INSERT INTO SCHEDULERDETAIL (SCHEDINFOUID, CATEGORY, TCNUM, DESCRIPTION, CTYPE, LOADRATE, DTYPE, SDATE, STIME, EDATE, ETIME," + 
						"		SEQ, PER, NOTE, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE, READYTIME, CODEDETUID, SAMETCNUM," + 
						"		PERFORMANCESDATE, PERFORMANCESTIME, PERFORMANCEEDATE, PERFORMANCEETIME, CODEDETTCNUM, CODEDETDESC)" + 
						"	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + 
						"		?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + 
						"		?, ?, ?, ?, ?, ?)";
				
				idx = 1;
				stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				stmt.setInt(idx++, bean.getSchedinfouid());
				stmt.setString(idx++, bean.getCategory());
				stmt.setString(idx++, bean.getTcnum());
				stmt.setString(idx++, bean.getDescription());
				stmt.setString(idx++, bean.getCtype());
				stmt.setString(idx++, bean.getLoadrate());
				stmt.setString(idx++, bean.getDtype());
				stmt.setString(idx++, bean.getSdate());
				stmt.setString(idx++, bean.getStime());
				stmt.setString(idx++, bean.getEdate());
				stmt.setString(idx++, bean.getEtime());
				stmt.setString(idx++, bean.getSeq());
				stmt.setString(idx++, bean.getPer());
				stmt.setString(idx++, bean.getNote());
				stmt.setInt(idx++, bean.getInsertBy());
				stmt.setString(idx++, bean.getInsertDate());
				stmt.setInt(idx++, bean.getUpdateBy());
				stmt.setString(idx++, bean.getUpdateDate());
				stmt.setString(idx++, bean.getReadytime());
				stmt.setString(idx++, bean.getCodedetuid());
				stmt.setString(idx++, bean.getSametcnum());
				stmt.setString(idx++, bean.getPerformancesdate());
				stmt.setString(idx++, bean.getPerformancestime());
				stmt.setString(idx++, bean.getPerformanceedate());
				stmt.setString(idx++, bean.getPerformanceetime());
				stmt.setString(idx++, bean.getCodedettcnum());
				stmt.setString(idx++, bean.getCodedetdesc());
				stmt.executeUpdate();
				rs = stmt.getGeneratedKeys();
				
				if(rs.next()) {
					int newDetailUid = rs.getInt(1);
					stmt.close();
					newDetailUidList.put(bean.getUid(), newDetailUid);
					
					if(bean.getTcNoteList() != null) {
						for(int x = 0; x < bean.getTcNoteList().size(); x++) {
							sql = "INSERT INTO SCHETCNOTE (SCHEDULERINFOUID, SCHEDULERDETAILUID, CODEKIND, CODE, STARTDATE, ENDDATE, REMARK, ISREPORT, INSERTDATE, INSERTBY, UPDATEDATE, UPDATEBY)" + 
									"	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
							
							ScheTcNoteBean note = bean.getTcNoteList().get(x);
							stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
							idx = 1;
							stmt.setInt(idx++, scheInfoUid);
							stmt.setInt(idx++, newDetailUid);
							stmt.setString(idx++, note.getCodeKind());
							stmt.setString(idx++, note.getCode());
							stmt.setString(idx++, note.getStartDate());
							stmt.setString(idx++, note.getEndDate());
							stmt.setString(idx++, note.getRemark());
							stmt.setString(idx++, note.getIsReport());
							stmt.setString(idx++, note.getInsertDate());
							stmt.setInt(idx++, note.getInsertBy());
							stmt.setString(idx++, note.getUpdateDate());
							stmt.setInt(idx++, note.getUpdateBy());
							stmt.executeUpdate();
							rs = stmt.getGeneratedKeys();
							
							if(rs.next()) {
								int newTcNoteUid = rs.getInt(1);
								stmt.close();
								
								if(note.getTcList() != null) {
									sql = "INSERT INTO SCHETCNOTETCINFO (SCHEDULERINFOUID, SCHETCNOTEUID, SCHEDULERDETAILUID, INSERTDATE, INSERTBY)" + 
											"	VALUES (?, ?, ?, ?, ?)";
									
									stmt = conn.prepareStatement(sql);
									
									for(int z = 0; z < note.getTcList().size(); z++) {
										ScheTcNoteTcInfoBean tcInfo = note.getTcList().get(z);
										idx = 1;
										stmt.setInt(idx++, scheInfoUid);
										stmt.setInt(idx++, newTcNoteUid);
										stmt.setInt(idx++, tcInfo.getSchedulerDetailUid());
										stmt.setString(idx++, tcInfo.getInsertDate());
										stmt.setInt(idx++, tcInfo.getInsertBy());
										stmt.executeUpdate();
									}
									
									stmt.close();
								}
								
								if(note.getFileList() != null) {
									sql = "INSERT INTO SCHETCNOTEFILEINFO (SCHEDULERINFOUID, SCHETCNOTEUID, FILENAME, SAVENAME, FILESIZE, FILETYPE, INSERTDATE, INSERTBY)" + 
											"	VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
									
									stmt = conn.prepareStatement(sql);
									
									for(int z = 0; z < note.getFileList().size(); z++) {
										ScheTcNoteFileInfoBean fileInfo = note.getFileList().get(z);
										idx = 1;
										stmt.setInt(idx++, scheInfoUid);
										stmt.setInt(idx++, newTcNoteUid);
										stmt.setString(idx++, fileInfo.getFileName());
										stmt.setString(idx++, fileInfo.getSaveName());
										stmt.setLong(idx++, fileInfo.getFileSize());
										stmt.setString(idx++, fileInfo.getFileType());
										stmt.setString(idx++, fileInfo.getInsertDate());
										stmt.setInt(idx++, fileInfo.getInsertBy());
										stmt.executeUpdate();
									}
									
									stmt.close();
								}
							}else {
								stmt.close();
							}
						}
					}
				}else {
					stmt.close();
				}
			}
			
			sql = "UPDATE SCHETCNOTETCINFO SET SCHEDULERDETAILUID = ?" +
					"	WHERE SCHEDULERINFOUID = ? AND SCHEDULERDETAILUID = ?";
			
			stmt = conn.prepareStatement(sql);
			
			for(int key : newDetailUidList.keySet()) {
				int newUid = newDetailUidList.get(key);
				idx = 1;
				stmt.setInt(idx++, newUid);
				stmt.setInt(idx++, scheInfoUid);
				stmt.setInt(idx++, key);
				stmt.executeUpdate();
			}
			
			stmt.close();
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
		
		resultMap.put(Const.LIST, delFileList);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static boolean uploadScheCrewList(int scheInfoUid, List<ScheCrewBean> list) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		int idx = 1;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			conn.setAutoCommit(false);
			
			sql = "DELETE FROM SCHECREWINOUT WHERE SCHECREWUID IN (SELECT UID FROM SCHECREW WHERE SCHEDULERINFOUID = ?)";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, scheInfoUid);
			stmt.executeUpdate();
			stmt.close();
			
			sql = "DELETE FROM SCHECREW WHERE SCHEDULERINFOUID = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, scheInfoUid);
			stmt.executeUpdate();
			stmt.close();
			
			for(int i = 0; i < list.size(); i++) {
				ScheCrewBean bean = list.get(i);
				
				sql = "INSERT INTO SCHECREW (SCHEDULERINFOUID, KIND, COMPANY, DEPARTMENT, NAME, RANK, IDNO, WORKTYPE1, WORKTYPE2," + 
						"		MAINSUB, FOODSTYLE, PERSONNO, PHONE, ISPLAN, ISNONE, INSERTDATE, INSERTBY, UPDATEDATE, UPDATEBY," + 
						"		TRIALKEY, PROJECT, PROJNO)" + 
						"	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?," + 
						"		?, ?, ?, ?, ?, 'N', ?, ?, ?, ?," + 
						"		?, ?, ?)";
				
				idx = 1;
				stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				stmt.setInt(idx++, bean.getSchedulerInfoUid());
				stmt.setString(idx++, bean.getKind());
				stmt.setString(idx++, bean.getCompany());
				stmt.setString(idx++, bean.getDepartment());
				stmt.setString(idx++, bean.getName());
				stmt.setString(idx++, bean.getRank());
				stmt.setString(idx++, bean.getIdNo());
				stmt.setString(idx++, bean.getWorkType1());
				stmt.setString(idx++, bean.getWorkType2());
				stmt.setString(idx++, bean.getMainSub());
				stmt.setString(idx++, bean.getFoodStyle());
				stmt.setString(idx++, bean.getPersonNo());
				stmt.setString(idx++, bean.getPhone());
				stmt.setString(idx++, bean.getIsPlan());
				stmt.setString(idx++, bean.getInsertDate());
				stmt.setInt(idx++, bean.getInsertBy());
				stmt.setString(idx++, bean.getUpdateDate());
				stmt.setInt(idx++, bean.getUpdateBy());
				stmt.setString(idx++, bean.getTrialKey());
				stmt.setString(idx++, bean.getProject());
				stmt.setString(idx++, bean.getProjNo());
				stmt.executeUpdate();
				rs = stmt.getGeneratedKeys();
				
				if(rs.next()) {
					int newCrewUid = rs.getInt(1);
					stmt.close();
					
					if(bean.getInOutList() != null) {
						sql = "INSERT INTO SCHECREWINOUT (SCHECREWUID, INOUTDATE, SCHEDULERINOUT, PERFORMANCEINOUT, INSERTDATE, INSERTBY, UPDATEDATE, UPDATEBY)" + 
								"	VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
						
						stmt = conn.prepareStatement(sql);
						
						for(int x = 0; x < bean.getInOutList().size(); x++) {
							ScheCrewInOutBean inout = bean.getInOutList().get(x);
							idx = 1;
							stmt.setInt(idx++, newCrewUid);
							stmt.setString(idx++, inout.getInOutDate());
							stmt.setString(idx++, inout.getSchedulerInOut());
							stmt.setString(idx++, inout.getPerformanceInOut());
							stmt.setString(idx++, inout.getInsertDate());
							stmt.setInt(idx++, inout.getInsertBy());
							stmt.setString(idx++, inout.getUpdateDate());
							stmt.setInt(idx++, inout.getUpdateBy());
							stmt.executeUpdate();
						}
						
						stmt.close();
					}
				}else {
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
		
		return isError;
	}
	
	public static boolean changeOnline(int uid, int userUid) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			String sql = "UPDATE SCHEDULERINFO SET ISOFF = 'N', OFFDATE = SYSDATE()" +
					"	WHERE UID = ? AND OFFBY = ?";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, uid);
			stmt.setInt(2, userUid);
			stmt.executeUpdate();
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
	
	public static Map<String, Object> searchEmail(ParamBean bean) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ScheMailBean> list = new ArrayList<ScheMailBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT NAME, COMPANY, DEPARTMENT, RANK, EMAIL FROM SCHEMAIL";
			
			if(bean.getSearch() != null && !bean.getSearch().isEmpty()) {
				sql += "	WHERE LOWER(NAME) LIKE CONCAT('%', LOWER(?), '%')" + 
						"		OR LOWER(COMPANY) LIKE CONCAT('%', LOWER(?), '%')" + 
						"		OR LOWER(DEPARTMENT) LIKE CONCAT('%', LOWER(?), '%')" + 
						"		OR LOWER(RANK) LIKE CONCAT('%', LOWER(?), '%')" + 
						"		OR LOWER(EMAIL) LIKE CONCAT('%', LOWER(?), '%')";
			}
			
			sql += "	LIMIT 10";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				ScheMailBean row = new ScheMailBean();
				row.setName(rs.getString(idx++));
				row.setCompany(rs.getString(idx++));
				row.setDepartment(rs.getString(idx++));
				row.setRank(rs.getString(idx++));
				row.setEmail(rs.getString(idx++));
				
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static Map<String, Object> insertReportDeparture(ScheReportDepartureBean bean) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int newUid = 0;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			String sql = "INSERT INTO SCHEREPORTDEPARTURE (SCHEDULERINFOUID, TRIAL1SCHEDULERINFOUID, TRIAL2SCHEDULERINFOUID," + 
					"		ITPTOTALREMAIN, ITPTOTALBASE, ITPTRIALREMAIN, ITPTRIALBASE, ITPOUTFITTINGREMAIN, ITPOUTFITTINGBASE, " +
					"		PUNCHTOTAL, PUNCHTRIAL, PUNCHOUTFITTING, FUELHFO, FUELMGO, FUELMDO, FUELLNG, DRAFTFWD, DRAFTMID, DRAFTAFT," + 
					"		CREWNAME1, CREWNAME2, CREWNAME3, CREWNAME4, CREWNAME5, CREWNAME6, CREWNAME7," + 
					"		CREWPHONE1, CREWPHONE2, CREWPHONE3, CREWPHONE4, CREWPHONE5, CREWPHONE6, CREWPHONE7," + 
					"		CREWCNTTOTAL, CREWCNT1, CREWCNT2, CREWCNT3, CREWCNT4, CREWCNT5, CREWCNT6, CREWCNT7, CREWCNT8, CREWCNT9," + 
					"		SEATRIALTEST, HULL, MACHINERY, ELECTRIC," + 
					"		REMARK, INSERTDATE, INSERTBY, UPDATEDATE, UPDATEBY)" + 
					"	VALUES (?, ?, ?," + 
					"		?, ?, ?, ?, ?, ?," + 
					"		?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + 
					"		?, ?, ?, ?, ?, ?, ?," + 
					"		?, ?, ?, ?, ?, ?, ?," + 
					"		?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + 
					"		?, ?, ?, ?," + 
					"		?, SYSDATE(), ?, SYSDATE(), ?)";
			
			int idx = 1;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(idx++, bean.getSchedulerInfoUid());
			stmt.setInt(idx++, bean.getTrial1SchedulerInfoUid());
			stmt.setInt(idx++, bean.getTrial2SchedulerInfoUid());
			
			if(bean.getItpTotalRemain() != null) {
				stmt.setInt(idx++, bean.getItpTotalRemain());
			}else {
				stmt.setNull(idx++, Types.INTEGER);
			}
			
			if(bean.getItpTotalBase() != null) {
				stmt.setInt(idx++, bean.getItpTotalBase());
			}else {
				stmt.setNull(idx++, Types.INTEGER);
			}
			
			if(bean.getItpTrialRemain() != null) {
				stmt.setInt(idx++, bean.getItpTrialRemain());
			}else {
				stmt.setNull(idx++, Types.INTEGER);
			}
			
			if(bean.getItpTrialBase() != null) {
				stmt.setInt(idx++, bean.getItpTrialBase());
			}else {
				stmt.setNull(idx++, Types.INTEGER);
			}
			
			if(bean.getItpOutfittingRemain() != null) {
				stmt.setInt(idx++, bean.getItpOutfittingRemain());
			}else {
				stmt.setNull(idx++, Types.INTEGER);
			}
			
			if(bean.getItpOutfittingBase() != null) {
				stmt.setInt(idx++, bean.getItpOutfittingBase());
			}else {
				stmt.setNull(idx++, Types.INTEGER);
			}
			
			if(bean.getPunchTotal() != null) {
				stmt.setInt(idx++, bean.getPunchTotal());
			}else {
				stmt.setNull(idx++, Types.INTEGER);
			}
			
			if(bean.getPunchTrial() != null) {
				stmt.setInt(idx++, bean.getPunchTrial());
			}else {
				stmt.setNull(idx++, Types.INTEGER);
			}
			
			if(bean.getPunchOutfitting() != null) {
				stmt.setInt(idx++, bean.getPunchOutfitting());
			}else {
				stmt.setNull(idx++, Types.INTEGER);
			}
			
			if(bean.getFuelHfo() != null) {
				stmt.setFloat(idx++, bean.getFuelHfo());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelMgo() != null) {
				stmt.setFloat(idx++, bean.getFuelMgo());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelMdo() != null) {
				stmt.setFloat(idx++, bean.getFuelMdo());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelLng() != null) {
				stmt.setFloat(idx++, bean.getFuelLng());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getDraftFwd() != null) {
				stmt.setFloat(idx++, bean.getDraftFwd());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getDraftMid() != null) {
				stmt.setFloat(idx++, bean.getDraftMid());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getDraftAft() != null) {
				stmt.setFloat(idx++, bean.getDraftAft());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			stmt.setString(idx++, bean.getCrewName1());
			stmt.setString(idx++, bean.getCrewName2());
			stmt.setString(idx++, bean.getCrewName3());
			stmt.setString(idx++, bean.getCrewName4());
			stmt.setString(idx++, bean.getCrewName5());
			stmt.setString(idx++, bean.getCrewName6());
			stmt.setString(idx++, bean.getCrewName7());
			stmt.setString(idx++, bean.getCrewPhone1());
			stmt.setString(idx++, bean.getCrewPhone2());
			stmt.setString(idx++, bean.getCrewPhone3());
			stmt.setString(idx++, bean.getCrewPhone4());
			stmt.setString(idx++, bean.getCrewPhone5());
			stmt.setString(idx++, bean.getCrewPhone6());
			stmt.setString(idx++, bean.getCrewPhone7());
			stmt.setInt(idx++, bean.getCrewCntTotal());
			stmt.setInt(idx++, bean.getCrewCnt1());
			stmt.setInt(idx++, bean.getCrewCnt2());
			stmt.setInt(idx++, bean.getCrewCnt3());
			stmt.setInt(idx++, bean.getCrewCnt4());
			stmt.setInt(idx++, bean.getCrewCnt5());
			stmt.setInt(idx++, bean.getCrewCnt6());
			stmt.setInt(idx++, bean.getCrewCnt7());
			stmt.setInt(idx++, bean.getCrewCnt8());
			stmt.setInt(idx++, bean.getCrewCnt9());
			stmt.setInt(idx++, bean.getSeaTrialTest());
			stmt.setInt(idx++, bean.getHull());
			stmt.setInt(idx++, bean.getMachinery());
			stmt.setInt(idx++, bean.getElectric());
			stmt.setString(idx++, bean.getRemark());
			stmt.setInt(idx++, bean.getUserUid());
			stmt.setInt(idx++, bean.getUserUid());
			
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			
			if(rs.next()) {
				newUid = rs.getInt(1);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.UID, newUid);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static boolean updateTrialStatus(ScheTrialInfoBean bean) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			String sql = "UPDATE SCHETRIALINFO SET TRIALSTATUS = ?, UPDATEDATE = SYSDATE(), UPDATEBY = ?";
			
			if("ONGO".equals(bean.getTrialStatus())) {
				sql += ", ONGOCHANGEDATE = SYSDATE()";
			}
			
			sql += "	WHERE SCHEDULERINFOUID = ?";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			int idx = 1;
			stmt = conn.prepareStatement(sql);
			stmt.setString(idx++, bean.getTrialStatus());
			stmt.setInt(idx++, bean.getUserUid());
			stmt.setInt(idx++, bean.getSchedulerInfoUid());
			stmt.executeUpdate();
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
	
	public static Map<String, Object> insertReportDaily(ScheReportDailyBean bean) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int newUid = 0;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			String sql = "INSERT INTO SCHEREPORTDAILY (SCHEDULERINFOUID," + 
					"		FUELHFO, FUELMGO, FUELMDO, FUELLNG, DRAFTFWD, DRAFTMID, DRAFTAFT," + 
					"		FUELHFOPREV, FUELMGOPREV, FUELMDOPREV, FUELLNGPREV, DRAFTFWDPREV, DRAFTMIDPREV, DRAFTAFTPREV," + 
					"		FUELHFOUP, FUELMGOUP, FUELMDOUP, FUELLNGUP, FUELHFODOWN, FUELMGODOWN, FUELMDODOWN, FUELLNGDOWN," + 
					"		CREWNAME1, CREWNAME2, CREWPHONE1, CREWPHONE2," + 
					"		CREWCNTTOTAL, CREWCNT1, CREWCNT2, CREWCNT3, CREWCNT4, CREWCNT5, CREWCNT6, CREWCNT7, CREWCNT8, CREWCNT9," + 
					"		CREWCNTTOTALCHANGE, CREWCNT1CHANGE, CREWCNT2CHANGE, CREWCNT3CHANGE, CREWCNT4CHANGE, CREWCNT5CHANGE, CREWCNT6CHANGE, CREWCNT7CHANGE, CREWCNT8CHANGE, CREWCNT9CHANGE," + 
					"		SEATRIALTEST, HULL, MACHINERY, ELECTRIC," + 
					"		STARTDATE, STARTTIME, ENDDATE, ENDTIME, PREDSTARTDATE, PREDSTARTTIME, PREDENDDATE, PREDENDTIME," + 
					"		REMARK, INSERTDATE, INSERTBY, UPDATEDATE, UPDATEBY)" + 
					"	VALUES (?," + 
					"		?, ?, ?, ?, ?, ?, ?," + 
					"		?, ?, ?, ?, ?, ?, ?," + 
					"		?, ?, ?, ?, ?, ?, ?, ?," + 
					"		?, ?, ?, ?," + 
					"		?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + 
					"		?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + 
					"		?, ?, ?, ?," + 
					"		?, ?, ?, ?, ?, ?, ?, ?," + 
					"		?, SYSDATE(), ?, SYSDATE(), ?)";
			
			int idx = 1;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(idx++, bean.getSchedulerInfoUid());
			
			if(bean.getFuelHfo() != null) {
				stmt.setFloat(idx++, bean.getFuelHfo());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelMgo() != null) {
				stmt.setFloat(idx++, bean.getFuelMgo());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelMdo() != null) {
				stmt.setFloat(idx++, bean.getFuelMdo());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelLng() != null) {
				stmt.setFloat(idx++, bean.getFuelLng());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getDraftFwd() != null) {
				stmt.setFloat(idx++, bean.getDraftFwd());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getDraftMid() != null) {
				stmt.setFloat(idx++, bean.getDraftMid());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getDraftAft() != null) {
				stmt.setFloat(idx++, bean.getDraftAft());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelHfoPrev() != null) {
				stmt.setFloat(idx++, bean.getFuelHfoPrev());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelMgoPrev() != null) {
				stmt.setFloat(idx++, bean.getFuelMgoPrev());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelMdoPrev() != null) {
				stmt.setFloat(idx++, bean.getFuelMdoPrev());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelLngPrev() != null) {
				stmt.setFloat(idx++, bean.getFuelLngPrev());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getDraftFwdPrev() != null) {
				stmt.setFloat(idx++, bean.getDraftFwdPrev());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getDraftMidPrev() != null) {
				stmt.setFloat(idx++, bean.getDraftMidPrev());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getDraftAftPrev() != null) {
				stmt.setFloat(idx++, bean.getDraftAftPrev());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelHfoUp() != null) {
				stmt.setFloat(idx++, bean.getFuelHfoUp());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelMgoUp() != null) {
				stmt.setFloat(idx++, bean.getFuelMgoUp());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelMdoUp() != null) {
				stmt.setFloat(idx++, bean.getFuelMdoUp());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelLngUp() != null) {
				stmt.setFloat(idx++, bean.getFuelLngUp());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelHfoDown() != null) {
				stmt.setFloat(idx++, bean.getFuelHfoDown());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelMgoDown() != null) {
				stmt.setFloat(idx++, bean.getFuelMgoDown());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelMdoDown() != null) {
				stmt.setFloat(idx++, bean.getFuelMdoDown());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelLngDown() != null) {
				stmt.setFloat(idx++, bean.getFuelLngDown());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			stmt.setString(idx++, bean.getCrewName1());
			stmt.setString(idx++, bean.getCrewName2());
			stmt.setString(idx++, bean.getCrewPhone1());
			stmt.setString(idx++, bean.getCrewPhone2());
			stmt.setInt(idx++, bean.getCrewCntTotal());
			stmt.setInt(idx++, bean.getCrewCnt1());
			stmt.setInt(idx++, bean.getCrewCnt2());
			stmt.setInt(idx++, bean.getCrewCnt3());
			stmt.setInt(idx++, bean.getCrewCnt4());
			stmt.setInt(idx++, bean.getCrewCnt5());
			stmt.setInt(idx++, bean.getCrewCnt6());
			stmt.setInt(idx++, bean.getCrewCnt7());
			stmt.setInt(idx++, bean.getCrewCnt8());
			stmt.setInt(idx++, bean.getCrewCnt9());
			stmt.setInt(idx++, bean.getCrewCntTotalChange());
			stmt.setInt(idx++, bean.getCrewCnt1Change());
			stmt.setInt(idx++, bean.getCrewCnt2Change());
			stmt.setInt(idx++, bean.getCrewCnt3Change());
			stmt.setInt(idx++, bean.getCrewCnt4Change());
			stmt.setInt(idx++, bean.getCrewCnt5Change());
			stmt.setInt(idx++, bean.getCrewCnt6Change());
			stmt.setInt(idx++, bean.getCrewCnt7Change());
			stmt.setInt(idx++, bean.getCrewCnt8Change());
			stmt.setInt(idx++, bean.getCrewCnt9Change());
			stmt.setInt(idx++, bean.getSeaTrialTest());
			stmt.setInt(idx++, bean.getHull());
			stmt.setInt(idx++, bean.getMachinery());
			stmt.setInt(idx++, bean.getElectric());
			stmt.setString(idx++, bean.getStartDate());
			stmt.setString(idx++, bean.getStartTime());
			stmt.setString(idx++, bean.getEndDate());
			stmt.setString(idx++, bean.getEndTime());
			stmt.setString(idx++, bean.getPredStartDate());
			stmt.setString(idx++, bean.getPredStartTime());
			stmt.setString(idx++, bean.getPredEndDate());
			stmt.setString(idx++, bean.getPredEndTime());
			stmt.setString(idx++, bean.getRemark());
			stmt.setInt(idx++, bean.getUserUid());
			stmt.setInt(idx++, bean.getUserUid());
			
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			
			if(rs.next()) {
				newUid = rs.getInt(1);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.UID, newUid);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static boolean updateTrialInfoForDaily(ScheTrialInfoBean bean) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			String sql = "UPDATE SCHETRIALINFO SET" + 
					"		FUELHFOPERFORMANCE = ?, FUELMGOPERFORMANCE = ?, FUELMDOPERFORMANCE = ?, FUELLNGPERFORMANCE = ?," + 
					"		DRAFTFWDPERFORMANCE = ?, DRAFTMIDPERFORMANCE = ?, DRAFTAFTPERFORMANCE = ?," + 
					"		FUELHFOTEMP = NULL, FUELMGOTEMP = NULL, FUELMDOTEMP = NULL, FUELLNGTEMP = NULL," + 
					"		DRAFTFWDTEMP = NULL, DRAFTMIDTEMP = NULL, DRAFTAFTTEMP = NULL," + 
					"		FUELHFOUP = NULL, FUELMGOUP = NULL, FUELMDOUP = NULL, FUELLNGUP = NULL," + 
					"		FUELHFODOWN = NULL, FUELMGODOWN = NULL, FUELMDODOWN = NULL, FUELLNGDOWN = NULL," + 
					"		UPDATEDATE = SYSDATE(), UPDATEBY = ?" + 
					"	WHERE SCHEDULERINFOUID = ?";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			int idx = 1;
			stmt = conn.prepareStatement(sql);
			
			if(bean.getFuelHfoPerformance() != null) {
				stmt.setFloat(idx++, bean.getFuelHfoPerformance());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelMgoPerformance() != null) {
				stmt.setFloat(idx++, bean.getFuelMgoPerformance());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelMdoPerformance() != null) {
				stmt.setFloat(idx++, bean.getFuelMdoPerformance());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelLngPerformance() != null) {
				stmt.setFloat(idx++, bean.getFuelLngPerformance());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getDraftFwdPerformance() != null) {
				stmt.setFloat(idx++, bean.getDraftFwdPerformance());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getDraftMidPerformance() != null) {
				stmt.setFloat(idx++, bean.getDraftMidPerformance());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getDraftAftPerformance() != null) {
				stmt.setFloat(idx++, bean.getDraftAftPerformance());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			stmt.setInt(idx++, bean.getUserUid());
			stmt.setInt(idx++, bean.getSchedulerInfoUid());
			stmt.executeUpdate();
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
	
	public static boolean insertTcPerformanceDateTimeForDaily(int scheInfoUid, int userUid) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			conn.setAutoCommit(false);
			
			String sql = "SELECT UID, PERFORMANCESDATE, PERFORMANCESTIME, PERFORMANCEEDATE, PERFORMANCEETIME" + 
						"	FROM SCHEDULERDETAIL" + 
						"	WHERE SCHEDINFOUID = ? AND (" + 
						"		(PERFORMANCESDATE IS NOT NULL AND PERFORMANCESTIME IS NOT NULL) OR (PERFORMANCEEDATE IS NOT NULL AND PERFORMANCEETIME IS NOT NULL)" + 
						"	)";
			
			List<SchedulerDetailInfoBean> tcPerformanceList = new ArrayList<SchedulerDetailInfoBean>();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, scheInfoUid);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				SchedulerDetailInfoBean row = new SchedulerDetailInfoBean();
				row.setUid(rs.getInt(idx++));
				row.setPerformancesdate(rs.getString(idx++));
				row.setPerformancestime(rs.getString(idx++));
				row.setPerformanceedate(rs.getString(idx++));
				row.setPerformanceetime(rs.getString(idx++));
				
				tcPerformanceList.add(row);
			}
			
			stmt.close();
			
			if(tcPerformanceList != null) {
				sql = "INSERT INTO SCHEDATETIME (SCHEDULERDETAILUID, STARTDATE, STARTTIME, ENDDATE, ENDTIME," + 
						"		KIND, REVDATE, INSERTDATE, INSERTBY, UPDATEDATE, UPDATEBY)" +
						"	VALUES (?, ?, ?, ?, ?," +
						"		'P', SYSDATE(), SYSDATE(), ?, SYSDATE(), ?)";
				
				stmt = conn.prepareStatement(sql);
				
				for(int i = 0; i < tcPerformanceList.size(); i++) {
					SchedulerDetailInfoBean detail = tcPerformanceList.get(i);
					int idx = 1;
					stmt.setInt(idx++, detail.getUid());
					stmt.setString(idx++, detail.getPerformancesdate());
					stmt.setString(idx++, detail.getPerformancestime());
					stmt.setString(idx++, detail.getPerformanceedate());
					stmt.setString(idx++, detail.getPerformanceetime());
					stmt.setInt(idx++, userUid);
					stmt.setInt(idx++, userUid);
					
					stmt.executeUpdate();
				}
				
				stmt.close();
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
		
		return isError;
	}
	
	public static Map<String, Object> insertReportComp(ScheReportCompBean bean) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int newUid = 0;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			String sql = "INSERT INTO SCHEREPORTCOMP (SCHEDULERINFOUID, TRIAL1SCHEDULERINFOUID, TRIAL2SCHEDULERINFOUID," + 
					"		FUELHFO, FUELMGO, FUELMDO, FUELLNG," + 
					"		CREWNAME1, CREWNAME2, CREWPHONE1, CREWPHONE2," + 
					"		CREWCNTTOTAL, CREWCNT1, CREWCNT2, CREWCNT3, CREWCNT4, CREWCNT5, CREWCNT6, CREWCNT7, CREWCNT8, CREWCNT9," + 
					"		SEATRIALTEST, HULL, MACHINERY, ELECTRIC," + 
					"		STARTDATE, STARTTIME, ENDDATE, ENDTIME, PERFORMANCESTARTDATE, PERFORMANCESTARTTIME, PERFORMANCEENDDATE, PERFORMANCEENDTIME," + 
					"		REMARK, INSERTDATE, INSERTBY, UPDATEDATE, UPDATEBY)" + 
					"	VALUES (?, ?, ?," + 
					"		?, ?, ?, ?," + 
					"		?, ?, ?, ?," + 
					"		?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + 
					"		?, ?, ?, ?," + 
					"		?, ?, ?, ?, ?, ?, ?, ?," + 
					"		?, SYSDATE(), ?, SYSDATE(), ?)";
			
			int idx = 1;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(idx++, bean.getSchedulerInfoUid());
			stmt.setInt(idx++, bean.getTrial1SchedulerInfoUid());
			stmt.setInt(idx++, bean.getTrial2SchedulerInfoUid());
			
			if(bean.getFuelHfo() != null) {
				stmt.setFloat(idx++, bean.getFuelHfo());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelMgo() != null) {
				stmt.setFloat(idx++, bean.getFuelMgo());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelMdo() != null) {
				stmt.setFloat(idx++, bean.getFuelMdo());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			if(bean.getFuelLng() != null) {
				stmt.setFloat(idx++, bean.getFuelLng());
			}else {
				stmt.setNull(idx++, Types.DECIMAL);
			}
			
			stmt.setString(idx++, bean.getCrewName1());
			stmt.setString(idx++, bean.getCrewName2());
			stmt.setString(idx++, bean.getCrewPhone1());
			stmt.setString(idx++, bean.getCrewPhone2());
			stmt.setInt(idx++, bean.getCrewCntTotal());
			stmt.setInt(idx++, bean.getCrewCnt1());
			stmt.setInt(idx++, bean.getCrewCnt2());
			stmt.setInt(idx++, bean.getCrewCnt3());
			stmt.setInt(idx++, bean.getCrewCnt4());
			stmt.setInt(idx++, bean.getCrewCnt5());
			stmt.setInt(idx++, bean.getCrewCnt6());
			stmt.setInt(idx++, bean.getCrewCnt7());
			stmt.setInt(idx++, bean.getCrewCnt8());
			stmt.setInt(idx++, bean.getCrewCnt9());
			stmt.setInt(idx++, bean.getSeaTrialTest());
			stmt.setInt(idx++, bean.getHull());
			stmt.setInt(idx++, bean.getMachinery());
			stmt.setInt(idx++, bean.getElectric());
			stmt.setString(idx++, bean.getStartDate());
			stmt.setString(idx++, bean.getStartTime());
			stmt.setString(idx++, bean.getEndDate());
			stmt.setString(idx++, bean.getEndTime());
			stmt.setString(idx++, bean.getPerformanceStartDate());
			stmt.setString(idx++, bean.getPerformanceStartTime());
			stmt.setString(idx++, bean.getPerformanceEndDate());
			stmt.setString(idx++, bean.getPerformanceEndTime());
			stmt.setString(idx++, bean.getRemark());
			stmt.setInt(idx++, bean.getUserUid());
			stmt.setInt(idx++, bean.getUserUid());
			
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			
			if(rs.next()) {
				newUid = rs.getInt(1);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.UID, newUid);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
	public static boolean insertMailLogList(String[] list, int scheInfoUid, String toStep, int toStepUid, String kind, int insertBy) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			String sql = "INSERT INTO SCHEMAILLOG (SCHEDULERINFOUID, TOSTEP, TOSTEPUID, KIND, EMAIL, INSERTDATE, INSERTBY)" + 
					"	VALUES (?, ?, ?, ?, ?, SYSDATE(), ?)";
			
			stmt = conn.prepareStatement(sql);
			
			for(int i = 0; i < list.length; i++) {
				int idx = 1;
				stmt.setInt(idx++, scheInfoUid);
				stmt.setString(idx++, toStep);
				stmt.setInt(idx++, toStepUid);
				stmt.setString(idx++, kind);
				stmt.setString(idx++, list[i]);
				stmt.setInt(idx++, insertBy);
				
				stmt.executeUpdate();
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
	
	public static boolean deleteLastMailList(int scheInfoUid) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			String sql = "DELETE FROM SCHEMAILLOG WHERE TOSTEP = 'LAST' AND SCHEDULERINFOUID = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, scheInfoUid);
			stmt.executeUpdate();
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
}
