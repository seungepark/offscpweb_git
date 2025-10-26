package com.ssshi.scpoff.mybatis.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ssshi.scpoff.constant.Const;
import com.ssshi.scpoff.dto.ParamBean;
import com.ssshi.scpoff.dto.UserInfoBean;

/********************************************************************************
 * 프로그램 개요 : General
 * 
 * 최초 작성자 : KHJ
 * 최초 작성일 : 2024-07-17
 * 
 * 최종 수정자 : KHJ
 * 최종 수정일 : 2024-08-25
 * 
 * 메모 : 없음
 * 
 * Copyright 2024 by SiriusB. Confidential and proprietary information
 * This document contains information, which is the property of SiriusB, 
 * and is furnished for the sole purpose of the operation and the maintenance.  
 * Copyright © 2024 SiriusB.  All rights reserved.
 *
 ********************************************************************************/

public class GeneralDao {

	public static Map<String, Object> login(ParamBean bean) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfoBean user = null;
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, SHIPINFOUID, USERID, PW, FIRSTNAME, LASTNAME, POSCODE, LANGCODE, STATUS, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE" + 
					"		FROM USERINFO" + 
					"		WHERE STATUS = 'ACT' AND USERID = '" + bean.getUuid()+ "' AND PW = PASSWORD('" + bean.getPw() + "')";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				user = new UserInfoBean();
				user.setUid(rs.getInt(idx++));
				user.setShipInfoUid(rs.getInt(idx++));
				user.setUserId(rs.getString(idx++));
				user.setPw(rs.getString(idx++));
				user.setFirstName(rs.getString(idx++));
				user.setLastName(rs.getString(idx++));
				user.setPosCode(rs.getString(idx++));
				user.setLangCode(rs.getString(idx++));
				user.setStatus(rs.getString(idx++));
				user.setInsertBy(rs.getInt(idx++));
				user.setInsertDate(rs.getString(idx++));
				user.setUpdateBy(rs.getInt(idx++));
				user.setUpdateDate(rs.getString(idx++));
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
		
		resultMap.put(Const.BEAN, user);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	
//	public static List<ScdvmEvntSchBean> getDashQuayList() throws Exception {
//		List<ScdvmEvntSchBean> list = new ArrayList<ScdvmEvntSchBean>();
//		Connection conn = null;
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		
//		try {
//			String sql = "SELECT SE.PROJ_NO AS PROJNO, SE.ACTV, SE.WK_CNTS AS WKCNTS, SE.DA_STDT AS DASTDT, SE.DA_FNDT AS DAFNDT," + 
//					"			SE.SKND, SE.NEW_SKND AS NEWSKND, SQ.QUAY_NM AS QUAYNM, 10 AS PER" + 
//					"		FROM SCDVW_EVNT_SCH_DAEJOEN SE" + 
//					"			LEFT OUTER JOIN SCDVW_QUAY_PROJ_DAEJOEN SQ ON (SE.PROJ_NO = SQ.PROJ_NO)" + 
//					"		WHERE SE.ACTV IN ('H00000730000', 'H00000730001', 'H00000780000')" + 
//					"			AND DATE_SUB(DATE_FORMAT(SYSDATE(), '%Y-%m-%d'), INTERVAL 7 DAY) <= STR_TO_DATE(SE.DA_STDT, '%Y%m%d')" + 
//					"			AND DATE_ADD(DATE_FORMAT(SYSDATE(), '%Y-%m-%d'), INTERVAL 7 DAY) >= STR_TO_DATE(SE.DA_STDT, '%Y%m%d')" + 
//					"		ORDER BY SE.DA_STDT";
//			
//			conn = DriverManager.getConnection(URL, USER, PW);
//			stmt = conn.prepareStatement(sql);
//			rs = stmt.executeQuery();
//			
//			while(rs.next()) {
//				int idx = 1;
//				ScdvmEvntSchBean event = new ScdvmEvntSchBean();
//				event.setProjNo(rs.getString(idx++));
//				event.setActv(rs.getString(idx++));
//				event.setWkCnts(rs.getString(idx++));
//				event.setDaStdt(rs.getString(idx++));
//				event.setDaFndt(rs.getString(idx++));
//				event.setSknd(rs.getString(idx++));
//				event.setNewSknd(rs.getString(idx++));
//				event.setQuayNm(rs.getString(idx++));
//				event.setPer(rs.getInt(idx++));
//				list.add(event);
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				if(rs != null) rs.close();
//				if(stmt != null) stmt.close();
//				if(conn != null) conn.close();
//			}catch(SQLException e) {}
//		}
//		
//		return list;
//	}
//	
//	public static List<ScdvmEvntSchBean> getDashEventList() throws Exception {
//		List<ScdvmEvntSchBean> list = new ArrayList<ScdvmEvntSchBean>();
//		Connection conn = null;
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		
//		try {
//			String sql = "SELECT PROJ_NO AS PROJNO, OWN, NEW_SKND AS NEWSKND, ACTV, IFNULL(RT_STDT, DA_STDT) AS STDT, IFNULL(RT_FNDT, DA_FNDT) AS FNDT," + 
//					"		TIMESTAMPDIFF(DAY, IFNULL(RT_FNDT, DA_FNDT), SYSDATE()) AS DIFF," + 
//					"		(" + 
//					"			SELECT S.QUAY_NM FROM SCDVW_QUAY_PROJ_DAEJOEN S" + 
//					"			WHERE S.PROJ_NO = SE.PROJ_NO" + 
//					"				AND STR_TO_DATE(S.ST_DTM, '%Y%m%d%H%i') <= SYSDATE()" + 
//					"				AND STR_TO_DATE(S.FN_DTM, '%Y%m%d%H%i') > SYSDATE()" + 
//					"			LIMIT 1" + 
//					"		) AS QUAYNM" + 
//					"	FROM SCDVW_EVNT_SCH_DAEJOEN SE" + 
//					"	WHERE PROJ_NO IN (" + 
//					"			SELECT PROJ_NO FROM SCDVW_EVNT_SCH_DAEJOEN" + 
//					"			WHERE PROJ_NO IN (" + 
//					"					SELECT PROJ_NO" + 
//					"					FROM SCDVW_EVNT_SCH_DAEJOEN" + 
//					"					WHERE ACTV = 'H00000050000' AND DATE_SUB(STR_TO_DATE(DA_FNDT, '%Y%m%d'), INTERVAL 14 DAY) <= DATE_FORMAT(SYSDATE(), '%Y-%m-%d')" + 
//					"				)" + 
//					"				AND ACTV = 'H00000070000' AND STR_TO_DATE(DA_FNDT, '%Y%m%d') >= DATE_FORMAT(SYSDATE(), '%Y-%m-%d')" + 
//					"		)" + 
//					"		AND ACTV IN (" + 
//					"			'H00000050000', 'H00000510000', 'H00000531000', 'H00000540000', 'H00000710000', 'H00000712000'," + 
//					"			'H00000730000', 'H00000730001', 'H00000750000', 'H00000223000', 'H00000780000', 'H00000070000'" + 
//					"		)" + 
//					"	ORDER BY PROJ_NO, DA_FNDT";
//			
//			conn = DriverManager.getConnection(URL, USER, PW);
//			stmt = conn.prepareStatement(sql);
//			rs = stmt.executeQuery();
//			
//			while(rs.next()) {
//				int idx = 1;
//				ScdvmEvntSchBean event = new ScdvmEvntSchBean();
//				event.setProjNo(rs.getString(idx++));
//				event.setOwn(rs.getString(idx++));
//				event.setNewSknd(rs.getString(idx++));
//				event.setActv(rs.getString(idx++));
//				event.setStdt(rs.getString(idx++));
//				event.setFndt(rs.getString(idx++));
//				event.setDiff(rs.getInt(idx++));
//				event.setQuayNm(rs.getString(idx++));
//				list.add(event);
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				if(rs != null) rs.close();
//				if(stmt != null) stmt.close();
//				if(conn != null) conn.close();
//			}catch(SQLException e) {}
//		}
//		
//		return list;
//	}
}
