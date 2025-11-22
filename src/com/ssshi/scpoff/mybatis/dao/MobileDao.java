package com.ssshi.scpoff.mybatis.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssshi.scpoff.constant.Const; 
import com.ssshi.scpoff.mobile.dto.MobileScheCrewDetailBean;
import com.ssshi.scpoff.mobile.dto.MobileRoomInfoBean;
import com.ssshi.scpoff.mobile.dto.MobileRoomUserlistBean;
import com.ssshi.scpoff.mobile.dto.MobileLiftBoatInfoBean;
import com.ssshi.scpoff.mobile.dto.MobileLiftBoatCrewInfoBean;
import com.ssshi.scpoff.mobile.dto.MobileNotificationBean;
import com.ssshi.scpoff.mobile.dto.MobileOfflineCommandInfoBean;
import com.ssshi.scpoff.mobile.dto.MobileMealUserinfoBean;
import com.ssshi.scpoff.mobile.dto.MobileMealSmsUserinfoBean;

/********************************************************************************
 * 프로그램 개요 : Mobile 승선자 시스템
 * 
 * 작성자 : picnic.company11@gmail.com  
 *
 ********************************************************************************/

public class MobileDao {
 
	public static Map<String, Object> downMobileScheCrewdetail(String schedulerInfoUidList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<MobileScheCrewDetailBean> list = new ArrayList<MobileScheCrewDetailBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, SCHECREWUID, SCHEDULERINFOUID, ROLE1, ROLE2, TERMINAL, NOTEBOOK" + 
					"		, MODEL_NAME, SERIAL_NUMBER, FOREIGNER, PASSPORT_NUMBER, ORDER_STATUS" + 
					"		, ORDER_DATE, ORDER_UID, DELETE_YN, DELETE_DATE, DELETE_UID, QR_YN, REG_DATE" + 
					"	FROM SCHECREWDETAILINFO" + 
					"	WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				MobileScheCrewDetailBean row = new MobileScheCrewDetailBean();
				row.setUid(rs.getInt(idx++));
				row.setScheCrewUid(rs.getInt(idx++));
				row.setSchedulerInfoUid(rs.getInt(idx++));
				row.setRole1(rs.getString(idx++));
				row.setRole2(rs.getString(idx++));
				row.setTerminal(rs.getString(idx++));
				row.setNotebook(rs.getString(idx++));
				row.setModelName(rs.getString(idx++));
				row.setSerialNumber(rs.getString(idx++));
				row.setForeigner(rs.getString(idx++));
				row.setPassportNumber(rs.getString(idx++));
				row.setOrderStatus(rs.getString(idx++));
				row.setOrderDate(rs.getString(idx++));
				row.setOrderUid(rs.getString(idx++));
				row.setDeleteYn(rs.getString(idx++));
				row.setDeleteDate(rs.getString(idx++));
				row.setDeleteUid(rs.getString(idx++));
				row.setQrYn(rs.getString(idx++));
				row.setRegDate(rs.getString(idx++));
				
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
	
	public static Map<String, Object> downMobileRoomInfo(String schedulerInfoUidList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<MobileRoomInfoBean> list = new ArrayList<MobileRoomInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, PROJ_NO, SCHEDULERINFOUID, TRIALKEY, DECK, ROOM_NO, ROOM_NAME" + 
					"		, TEL, SIZE_M2, BED_COUNT, BATHROOM_YN, STATUS, DEL_YN" + 
					"		, ANDROID_ID, MOBILE_DEVICE_ID, INSERT_DATE, REG_USERNAME, UPDATE_DATE, UPDATE_USERNAME" + 
					"	FROM MOBILE_ROOM_INFO" + 
					"	WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				MobileRoomInfoBean row = new MobileRoomInfoBean();
				row.setUid(rs.getInt(idx++));
				row.setProjNo(rs.getString(idx++));
				row.setSchedulerInfoUid(rs.getInt(idx++));
				row.setTrialKey(rs.getString(idx++));
				row.setDeck(rs.getString(idx++));
				row.setRoomNo(rs.getString(idx++));
				row.setRoomName(rs.getString(idx++));
				row.setTel(rs.getString(idx++));
				row.setSizeM2(rs.getString(idx++));
				Integer bedCount = rs.getInt(idx++);
				if(rs.wasNull()) {
					bedCount = null;
				}
				row.setBedCount(bedCount);
				row.setBathroomYn(rs.getString(idx++));
				row.setStatus(rs.getString(idx++));
				row.setDelYn(rs.getString(idx++));
				row.setAndroidId(rs.getString(idx++));
				row.setMobileDeviceId(rs.getString(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setRegUsername(rs.getString(idx++));
				row.setUpdateDate(rs.getString(idx++));
				row.setUpdateUsername(rs.getString(idx++));
				
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
	
	public static Map<String, Object> downMobileRoomUserlist(String schedulerInfoUidList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<MobileRoomUserlistBean> list = new ArrayList<MobileRoomUserlistBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, PROJ_NO, SCHEDULERINFOUID, TRIALKEY, ROOM_NO, DEPARTMENT, NAME" + 
					"		, PHONE, USER_UID, CHECK_IN_DATE, CHECK_OUT_DATE, STATUS, DEL_YN" + 
					"		, ANDROID_ID, MOBILE_DEVICE_ID, INSERT_DATE, REG_USERNAME, UPDATE_DATE, UPDATE_USERNAME, REMARK" + 
					"	FROM MOBILE_ROOM_USERLIST" + 
					"	WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				MobileRoomUserlistBean row = new MobileRoomUserlistBean();
				row.setUid(rs.getInt(idx++));
				row.setProjNo(rs.getString(idx++));
				Integer schedulerInfoUid = rs.getInt(idx++);
				if(rs.wasNull()) {
					schedulerInfoUid = null;
				}
				row.setSchedulerInfoUid(schedulerInfoUid);
				row.setTrialKey(rs.getString(idx++));
				row.setRoomNo(rs.getString(idx++));
				row.setDepartment(rs.getString(idx++));
				row.setName(rs.getString(idx++));
				row.setPhone(rs.getString(idx++));
				Integer userUid = rs.getInt(idx++);
				if(rs.wasNull()) {
					userUid = null;
				}
				row.setUserUid(userUid);
				row.setCheckInDate(rs.getString(idx++));
				row.setCheckOutDate(rs.getString(idx++));
				row.setStatus(rs.getString(idx++));
				row.setDelYn(rs.getString(idx++));
				row.setAndroidId(rs.getString(idx++));
				row.setMobileDeviceId(rs.getString(idx++));
				row.setInsertDate(rs.getString(idx++));
				row.setRegUsername(rs.getString(idx++));
				row.setUpdateDate(rs.getString(idx++));
				row.setUpdateUsername(rs.getString(idx++));
				row.setRemark(rs.getString(idx++));
				
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
	
	public static Map<String, Object> downMobileLiftBoatInfo(String schedulerInfoUidList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<MobileLiftBoatInfoBean> list = new ArrayList<MobileLiftBoatInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, PROJ_NO, SCHEDULERINFOUID, TRIALKEY, LIFTBOAT_CNT, LIFTBOAT_HEADCNT" + 
					"		, LIFTRAFT_CNT, LIFTRAFT_HEADCNT, ANDROID_ID, INSERT_DATE, INSERTBY, REG_DEVICE" + 
					"	FROM MOBILE_LIFT_BOAT_INFO" + 
					"	WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				MobileLiftBoatInfoBean row = new MobileLiftBoatInfoBean();
				row.setUid(rs.getInt(idx++));
				row.setProjNo(rs.getString(idx++));
				Long schedulerInfoUid = rs.getLong(idx++);
				if(rs.wasNull()) {
					schedulerInfoUid = null;
				}
				row.setSchedulerInfoUid(schedulerInfoUid);
				row.setTrialKey(rs.getString(idx++));
				Long liftboatCnt = rs.getLong(idx++);
				if(rs.wasNull()) {
					liftboatCnt = null;
				}
				row.setLiftboatCnt(liftboatCnt);
				Long liftboatHeadcnt = rs.getLong(idx++);
				if(rs.wasNull()) {
					liftboatHeadcnt = null;
				}
				row.setLiftboatHeadcnt(liftboatHeadcnt);
				Long liftraftCnt = rs.getLong(idx++);
				if(rs.wasNull()) {
					liftraftCnt = null;
				}
				row.setLiftraftCnt(liftraftCnt);
				Long liftraftHeadcnt = rs.getLong(idx++);
				if(rs.wasNull()) {
					liftraftHeadcnt = null;
				}
				row.setLiftraftHeadcnt(liftraftHeadcnt);
				row.setAndroidId(rs.getString(idx++));
				row.setInsertDate(rs.getString(idx++));
				Integer insertby = rs.getInt(idx++);
				if(rs.wasNull()) {
					insertby = null;
				}
				row.setInsertby(insertby);
				row.setRegDevice(rs.getString(idx++));
				
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
	
	public static Map<String, Object> downMobileLiftBoatCrewinfo(String schedulerInfoUidList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<MobileLiftBoatCrewInfoBean> list = new ArrayList<MobileLiftBoatCrewInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, PROJ_NO, SCHEDULERINFOUID, TRIALKEY, BOAT_NAME, USER_UID" + 
					"		, ROLE, ANDROID_ID, INSERT_DATE, INSERTBY, REG_DEVICE" + 
					"	FROM MOBILE_LIFT_BOAT_CREWINFO" + 
					"	WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				MobileLiftBoatCrewInfoBean row = new MobileLiftBoatCrewInfoBean();
				Long uid = rs.getLong(idx++);
				if(rs.wasNull()) {
					uid = null;
				}
				row.setUid(uid);
				row.setProjNo(rs.getString(idx++));
				Long schedulerInfoUid = rs.getLong(idx++);
				if(rs.wasNull()) {
					schedulerInfoUid = null;
				}
				row.setSchedulerInfoUid(schedulerInfoUid);
				row.setTrialKey(rs.getString(idx++));
				row.setBoatName(rs.getString(idx++));
				Integer userUid = rs.getInt(idx++);
				if(rs.wasNull()) {
					userUid = null;
				}
				row.setUserUid(userUid);
				row.setRole(rs.getString(idx++));
				row.setAndroidId(rs.getString(idx++));
				java.sql.Timestamp insertDate = rs.getTimestamp(idx++);
				row.setInsertDate(insertDate);
				Integer insertBy = rs.getInt(idx++);
				if(rs.wasNull()) {
					insertBy = null;
				}
				row.setInsertBy(insertBy);
				row.setRegDevice(rs.getString(idx++));
				
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
	
	public static Map<String, Object> downMobileNotification(String schedulerInfoUidList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<MobileNotificationBean> list = new ArrayList<MobileNotificationBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT ORIID, NOTICEID, PROJ_NO, TRIALKEY, SCHEDULERINFOUID, TYPECODE" + 
					"		, NCONTENT, STATCODE, START_DT, FINISHDT, DELETEYN" + 
					"		, AUTHORID, CREATEDT, EDITORID, UPDATEDT" + 
					"	FROM MOBILE_NOTIFICATION" + 
					"	WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				MobileNotificationBean row = new MobileNotificationBean();
				row.setOriid(rs.getInt(idx++));
				Integer noticeid = rs.getInt(idx++);
				if(rs.wasNull()) {
					noticeid = null;
				}
				row.setNoticeid(noticeid);
				row.setProjNo(rs.getString(idx++));
				row.setTrialKey(rs.getString(idx++));
				row.setSchedulerInfoUid(rs.getString(idx++));
				row.setTypecode(rs.getString(idx++));
				row.setNcontent(rs.getString(idx++));
				row.setStatcode(rs.getString(idx++));
				row.setStartDt(rs.getString(idx++));
				row.setFinishdt(rs.getString(idx++));
				row.setDeleteyn(rs.getString(idx++));
				row.setAuthorid(rs.getString(idx++));
				row.setCreatedt(rs.getString(idx++));
				row.setEditorid(rs.getString(idx++));
				row.setUpdatedt(rs.getString(idx++));
				
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
	
	public static Map<String, Object> downMobileOfflineCommandInfo(String schedulerInfoUidList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<MobileOfflineCommandInfoBean> list = new ArrayList<MobileOfflineCommandInfoBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT UID, PROJ_NO, SCHEDULERINFOUID, TRIALKEY, COMMAND_IP, COMMAND_PORT" + 
					"		, ANDROID_ID, INSERT_DATE, INSERTBY, REG_DEVICE" + 
					"	FROM MOBILE_OFFLINE_COMMAND_INFO" + 
					"	WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				MobileOfflineCommandInfoBean row = new MobileOfflineCommandInfoBean();
				row.setUid(rs.getInt(idx++));
				row.setProjNo(rs.getString(idx++));
				Long schedulerInfoUid = rs.getLong(idx++);
				if(rs.wasNull()) {
					schedulerInfoUid = null;
				}
				row.setSchedulerInfoUid(schedulerInfoUid);
				row.setTrialKey(rs.getString(idx++));
				row.setCommandIp(rs.getString(idx++));
				row.setCommandPort(rs.getString(idx++));
				row.setAndroidId(rs.getString(idx++));
				row.setInsertDate(rs.getString(idx++));
				Integer insertby = rs.getInt(idx++);
				if(rs.wasNull()) {
					insertby = null;
				}
				row.setInsertby(insertby);
				row.setRegDevice(rs.getString(idx++));
				
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
	
	// ========== 온라인 DB 업로드 메서드 ==========
	
	/**
	 * 온라인 DB에 모바일 승선자 상세 정보 업로드 (삭제 후 삽입)
	 */
	public static boolean uploadMobileScheCrewdetail(String schedulerInfoUidList, List<MobileScheCrewDetailBean> list) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			conn.setAutoCommit(false);
			
			// 기존 데이터 삭제 (일괄 삭제)
			String sql = "DELETE FROM SCHECREWDETAILINFO WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();
			
			// 데이터 삽입
			if(list != null && list.size() > 0) {
				sql = "INSERT INTO SCHECREWDETAILINFO (" +
						"SCHECREWUID, SCHEDULERINFOUID, ROLE1, ROLE2, TERMINAL, NOTEBOOK" +
						", MODEL_NAME, SERIAL_NUMBER, FOREIGNER, PASSPORT_NUMBER, ORDER_STATUS" +
						", ORDER_DATE, ORDER_UID, DELETE_YN, DELETE_DATE, DELETE_UID, QR_YN, REG_DATE) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				stmt = conn.prepareStatement(sql);
				
				for(int i = 0; i < list.size(); i++) {
					MobileScheCrewDetailBean bean = list.get(i);
					int idx = 1;
					stmt.setInt(idx++, bean.getScheCrewUid());
					stmt.setInt(idx++, bean.getSchedulerInfoUid());
					stmt.setString(idx++, bean.getRole1());
					stmt.setString(idx++, bean.getRole2());
					stmt.setString(idx++, bean.getTerminal());
					stmt.setString(idx++, bean.getNotebook());
					stmt.setString(idx++, bean.getModelName());
					stmt.setString(idx++, bean.getSerialNumber());
					stmt.setString(idx++, bean.getForeigner());
					stmt.setString(idx++, bean.getPassportNumber());
					stmt.setString(idx++, bean.getOrderStatus());
					stmt.setString(idx++, bean.getOrderDate());
					stmt.setString(idx++, bean.getOrderUid());
					stmt.setString(idx++, bean.getDeleteYn());
					stmt.setString(idx++, bean.getDeleteDate());
					stmt.setString(idx++, bean.getDeleteUid());
					stmt.setString(idx++, bean.getQrYn());
					stmt.setString(idx++, bean.getRegDate());
					
					stmt.executeUpdate();
				}
				stmt.close();
			}
			
			conn.commit();
		}catch(Exception e) {
			isError = true;
			if(conn != null) conn.rollback();
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.setAutoCommit(true);
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
	
	/**
	 * 온라인 DB에 모바일 방정보 업로드 (삭제 후 삽입)
	 */
	public static boolean uploadMobileRoomInfo(String schedulerInfoUidList, List<MobileRoomInfoBean> list) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			conn.setAutoCommit(false);
			
			// 기존 데이터 삭제 (일괄 삭제)
			String sql = "DELETE FROM MOBILE_ROOM_INFO WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();
			
			// 데이터 삽입
			if(list != null && list.size() > 0) {
				sql = "INSERT INTO MOBILE_ROOM_INFO (" +
						"PROJ_NO, SCHEDULERINFOUID, TRIALKEY, DECK, ROOM_NO, ROOM_NAME" +
						", TEL, SIZE_M2, BED_COUNT, BATHROOM_YN, STATUS, DEL_YN" +
						", ANDROID_ID, MOBILE_DEVICE_ID, INSERT_DATE, REG_USERNAME, UPDATE_DATE, UPDATE_USERNAME) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				stmt = conn.prepareStatement(sql);
				
				for(int i = 0; i < list.size(); i++) {
					MobileRoomInfoBean bean = list.get(i);
					int idx = 1;
					
					// 디버깅: PROJ_NO 값 확인
					if(bean.getProjNo() == null || bean.getProjNo().isEmpty()) {
						System.out.println("[uploadMobileRoomInfo] WARNING: PROJ_NO가 null입니다. UID: " + bean.getUid() + ", SCHEDULERINFOUID: " + bean.getSchedulerInfoUid());
					}
					
					stmt.setString(idx++, bean.getProjNo());
					if(bean.getSchedulerInfoUid() != null) {
						stmt.setInt(idx++, bean.getSchedulerInfoUid());
					} else {
						stmt.setNull(idx++, java.sql.Types.INTEGER);
					}
					stmt.setString(idx++, bean.getTrialKey());
					stmt.setString(idx++, bean.getDeck());
					stmt.setString(idx++, bean.getRoomNo());
					stmt.setString(idx++, bean.getRoomName());
					stmt.setString(idx++, bean.getTel());
					stmt.setString(idx++, bean.getSizeM2());
					if(bean.getBedCount() != null) {
						stmt.setInt(idx++, bean.getBedCount());
					} else {
						stmt.setNull(idx++, java.sql.Types.INTEGER);
					}
					stmt.setString(idx++, bean.getBathroomYn());
					stmt.setString(idx++, bean.getStatus());
					stmt.setString(idx++, bean.getDelYn());
					stmt.setString(idx++, bean.getAndroidId());
					stmt.setString(idx++, bean.getMobileDeviceId());
					stmt.setString(idx++, bean.getInsertDate());
					stmt.setString(idx++, bean.getRegUsername());
					stmt.setString(idx++, bean.getUpdateDate());
					stmt.setString(idx++, bean.getUpdateUsername());
					
					stmt.executeUpdate();
				}
				stmt.close();
			}
			
			conn.commit();
		}catch(Exception e) {
			isError = true;
			if(conn != null) conn.rollback();
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.setAutoCommit(true);
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
	
	/**
	 * 온라인 DB에 모바일 방배정명단정보 업로드 (삭제 후 삽입)
	 */
	public static boolean uploadMobileRoomUserlist(String schedulerInfoUidList, List<MobileRoomUserlistBean> list) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			conn.setAutoCommit(false);
			
			// 기존 데이터 삭제 (일괄 삭제)
			String sql = "DELETE FROM MOBILE_ROOM_USERLIST WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();
			
			// 데이터 삽입
			if(list != null && list.size() > 0) {
				sql = "INSERT INTO MOBILE_ROOM_USERLIST (" +
						"PROJ_NO, SCHEDULERINFOUID, TRIALKEY, ROOM_NO, DEPARTMENT, NAME" +
						", PHONE, USER_UID, CHECK_IN_DATE, CHECK_OUT_DATE, STATUS, DEL_YN" +
						", ANDROID_ID, MOBILE_DEVICE_ID, INSERT_DATE, REG_USERNAME, UPDATE_DATE, UPDATE_USERNAME, REMARK) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				stmt = conn.prepareStatement(sql);
				
				for(int i = 0; i < list.size(); i++) {
					MobileRoomUserlistBean bean = list.get(i);
					int idx = 1;
					stmt.setString(idx++, bean.getProjNo());
					if(bean.getSchedulerInfoUid() != null) {
						stmt.setInt(idx++, bean.getSchedulerInfoUid());
					} else {
						stmt.setNull(idx++, java.sql.Types.INTEGER);
					}
					stmt.setString(idx++, bean.getTrialKey());
					stmt.setString(idx++, bean.getRoomNo());
					stmt.setString(idx++, bean.getDepartment());
					stmt.setString(idx++, bean.getName());
					stmt.setString(idx++, bean.getPhone());
					if(bean.getUserUid() != null) {
						stmt.setInt(idx++, bean.getUserUid());
					} else {
						stmt.setNull(idx++, java.sql.Types.INTEGER);
					}
					stmt.setString(idx++, bean.getCheckInDate());
					stmt.setString(idx++, bean.getCheckOutDate());
					stmt.setString(idx++, bean.getStatus());
					stmt.setString(idx++, bean.getDelYn());
					stmt.setString(idx++, bean.getAndroidId());
					stmt.setString(idx++, bean.getMobileDeviceId());
					stmt.setString(idx++, bean.getInsertDate());
					stmt.setString(idx++, bean.getRegUsername());
					stmt.setString(idx++, bean.getUpdateDate());
					stmt.setString(idx++, bean.getUpdateUsername());
					stmt.setString(idx++, bean.getRemark());
					
					stmt.executeUpdate();
				}
				stmt.close();
			}
			
			conn.commit();
		}catch(Exception e) {
			isError = true;
			if(conn != null) conn.rollback();
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.setAutoCommit(true);
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
	
	/**
	 * 온라인 DB에 모바일 구명정기본 정보 업로드 (삭제 후 삽입)
	 */
	public static boolean uploadMobileLiftBoatInfo(String schedulerInfoUidList, List<MobileLiftBoatInfoBean> list) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			conn.setAutoCommit(false);
			
			// 기존 데이터 삭제 (일괄 삭제)
			String sql = "DELETE FROM MOBILE_LIFT_BOAT_INFO WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();
			
			// 데이터 삽입
			if(list != null && list.size() > 0) {
				sql = "INSERT INTO MOBILE_LIFT_BOAT_INFO (" +
						"PROJ_NO, SCHEDULERINFOUID, TRIALKEY, LIFTBOAT_CNT, LIFTBOAT_HEADCNT" +
						", LIFTRAFT_CNT, LIFTRAFT_HEADCNT, ANDROID_ID, INSERT_DATE, INSERTBY, REG_DEVICE) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				stmt = conn.prepareStatement(sql);
				
				for(int i = 0; i < list.size(); i++) {
					MobileLiftBoatInfoBean bean = list.get(i);
					int idx = 1;
					stmt.setString(idx++, bean.getProjNo());
					if(bean.getSchedulerInfoUid() != null) {
						stmt.setLong(idx++, bean.getSchedulerInfoUid());
					} else {
						stmt.setNull(idx++, java.sql.Types.BIGINT);
					}
					stmt.setString(idx++, bean.getTrialKey());
					if(bean.getLiftboatCnt() != null) {
						stmt.setLong(idx++, bean.getLiftboatCnt());
					} else {
						stmt.setNull(idx++, java.sql.Types.BIGINT);
					}
					if(bean.getLiftboatHeadcnt() != null) {
						stmt.setLong(idx++, bean.getLiftboatHeadcnt());
					} else {
						stmt.setNull(idx++, java.sql.Types.BIGINT);
					}
					if(bean.getLiftraftCnt() != null) {
						stmt.setLong(idx++, bean.getLiftraftCnt());
					} else {
						stmt.setNull(idx++, java.sql.Types.BIGINT);
					}
					if(bean.getLiftraftHeadcnt() != null) {
						stmt.setLong(idx++, bean.getLiftraftHeadcnt());
					} else {
						stmt.setNull(idx++, java.sql.Types.BIGINT);
					}
					stmt.setString(idx++, bean.getAndroidId());
					stmt.setString(idx++, bean.getInsertDate());
					if(bean.getInsertby() != null) {
						stmt.setInt(idx++, bean.getInsertby());
					} else {
						stmt.setNull(idx++, java.sql.Types.INTEGER);
					}
					stmt.setString(idx++, bean.getRegDevice());
					
					stmt.executeUpdate();
				}
				stmt.close();
			}
			
			conn.commit();
		}catch(Exception e) {
			isError = true;
			if(conn != null) conn.rollback();
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.setAutoCommit(true);
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
	
	/**
	 * 온라인 DB에 모바일 구명정배정 인원 명단 정보 업로드 (삭제 후 삽입)
	 */
	public static boolean uploadMobileLiftBoatCrewinfo(String schedulerInfoUidList, List<MobileLiftBoatCrewInfoBean> list) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			conn.setAutoCommit(false);
			
			// 기존 데이터 삭제 (일괄 삭제)
			String sql = "DELETE FROM MOBILE_LIFT_BOAT_CREWINFO WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();
			
			// 데이터 삽입
			if(list != null && list.size() > 0) {
				sql = "INSERT INTO MOBILE_LIFT_BOAT_CREWINFO (" +
						"PROJ_NO, SCHEDULERINFOUID, TRIALKEY, BOAT_NAME, USER_UID" +
						", ROLE, ANDROID_ID, INSERT_DATE, INSERTBY, REG_DEVICE) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				stmt = conn.prepareStatement(sql);
				
				for(int i = 0; i < list.size(); i++) {
					MobileLiftBoatCrewInfoBean bean = list.get(i);
					int idx = 1;
					stmt.setString(idx++, bean.getProjNo());
					if(bean.getSchedulerInfoUid() != null) {
						stmt.setLong(idx++, bean.getSchedulerInfoUid());
					} else {
						stmt.setNull(idx++, java.sql.Types.BIGINT);
					}
					stmt.setString(idx++, bean.getTrialKey());
					stmt.setString(idx++, bean.getBoatName());
					if(bean.getUserUid() != null) {
						stmt.setInt(idx++, bean.getUserUid());
					} else {
						stmt.setNull(idx++, java.sql.Types.INTEGER);
					}
					stmt.setString(idx++, bean.getRole());
					stmt.setString(idx++, bean.getAndroidId());
					if(bean.getInsertDate() != null) {
						stmt.setTimestamp(idx++, bean.getInsertDate());
					} else {
						stmt.setNull(idx++, java.sql.Types.TIMESTAMP);
					}
					if(bean.getInsertBy() != null) {
						stmt.setInt(idx++, bean.getInsertBy());
					} else {
						stmt.setNull(idx++, java.sql.Types.INTEGER);
					}
					stmt.setString(idx++, bean.getRegDevice());
					
					stmt.executeUpdate();
				}
				stmt.close();
			}
			
			conn.commit();
		}catch(Exception e) {
			isError = true;
			if(conn != null) conn.rollback();
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.setAutoCommit(true);
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
	
	/**
	 * 온라인 DB에 모바일 공지사항 정보 업로드 (삭제 후 삽입)
	 */
	public static boolean uploadMobileNotification(String schedulerInfoUidList, List<MobileNotificationBean> list) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			conn.setAutoCommit(false);
			
			// 기존 데이터 삭제 (일괄 삭제) - SCHEDULERINFOUID가 text 타입이므로 문자열 비교
			String sql = "DELETE FROM MOBILE_NOTIFICATION WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();
			
			// 데이터 삽입
			if(list != null && list.size() > 0) {
				sql = "INSERT INTO MOBILE_NOTIFICATION (" +
						"NOTICEID, PROJ_NO, TRIALKEY, SCHEDULERINFOUID, TYPECODE" +
						", NCONTENT, STATCODE, START_DT, FINISHDT, DELETEYN" +
						", AUTHORID, CREATEDT, EDITORID, UPDATEDT) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				stmt = conn.prepareStatement(sql);
				
				for(int i = 0; i < list.size(); i++) {
					MobileNotificationBean bean = list.get(i);
					int idx = 1;
					if(bean.getNoticeid() != null) {
						stmt.setInt(idx++, bean.getNoticeid());
					} else {
						stmt.setNull(idx++, java.sql.Types.INTEGER);
					}
					stmt.setString(idx++, bean.getProjNo());
					stmt.setString(idx++, bean.getTrialKey());
					stmt.setString(idx++, bean.getSchedulerInfoUid());
					stmt.setString(idx++, bean.getTypecode());
					stmt.setString(idx++, bean.getNcontent());
					stmt.setString(idx++, bean.getStatcode());
					stmt.setString(idx++, bean.getStartDt());
					stmt.setString(idx++, bean.getFinishdt());
					stmt.setString(idx++, bean.getDeleteyn());
					stmt.setString(idx++, bean.getAuthorid());
					stmt.setString(idx++, bean.getCreatedt());
					stmt.setString(idx++, bean.getEditorid());
					stmt.setString(idx++, bean.getUpdatedt());
					
					stmt.executeUpdate();
				}
				stmt.close();
			}
			
			conn.commit();
		}catch(Exception e) {
			isError = true;
			if(conn != null) conn.rollback();
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.setAutoCommit(true);
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
	
	/**
	 * 온라인 DB에 모바일 오프라인 코멘더 서버 정보 업로드 (삭제 후 삽입)
	 */
	public static boolean uploadMobileOfflineCommandInfo(String schedulerInfoUidList, List<MobileOfflineCommandInfoBean> list) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			conn.setAutoCommit(false);
			
			// 기존 데이터 삭제 (일괄 삭제)
			String sql = "DELETE FROM MOBILE_OFFLINE_COMMAND_INFO WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();
			
			// 데이터 삽입
			if(list != null && list.size() > 0) {
				sql = "INSERT INTO MOBILE_OFFLINE_COMMAND_INFO (" +
						"PROJ_NO, SCHEDULERINFOUID, TRIALKEY, COMMAND_IP, COMMAND_PORT" +
						", ANDROID_ID, INSERT_DATE, INSERTBY, REG_DEVICE) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				stmt = conn.prepareStatement(sql);
				
				for(int i = 0; i < list.size(); i++) {
					MobileOfflineCommandInfoBean bean = list.get(i);
					int idx = 1;
					stmt.setString(idx++, bean.getProjNo());
					if(bean.getSchedulerInfoUid() != null) {
						stmt.setLong(idx++, bean.getSchedulerInfoUid());
					} else {
						stmt.setNull(idx++, java.sql.Types.BIGINT);
					}
					stmt.setString(idx++, bean.getTrialKey());
					stmt.setString(idx++, bean.getCommandIp());
					stmt.setString(idx++, bean.getCommandPort());
					stmt.setString(idx++, bean.getAndroidId());
					stmt.setString(idx++, bean.getInsertDate());
					if(bean.getInsertby() != null) {
						stmt.setInt(idx++, bean.getInsertby());
					} else {
						stmt.setNull(idx++, java.sql.Types.INTEGER);
					}
					stmt.setString(idx++, bean.getRegDevice());
					
					stmt.executeUpdate();
				}
				stmt.close();
			}
			
			conn.commit();
		}catch(Exception e) {
			isError = true;
			if(conn != null) conn.rollback();
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.setAutoCommit(true);
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
	
	/**
	 * 온라인 DB에 모바일 시운전 식사 실적 정보 업로드 (삭제 후 삽입)
	 */
	public static boolean uploadMobileMealUserinfo(String schedulerInfoUidList, List<MobileMealUserinfoBean> list) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			conn.setAutoCommit(false);
			
			// 기존 데이터 삭제 (일괄 삭제)
			String sql = "DELETE FROM MOBILE_MEAL_USERINFO WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();
			
			// 데이터 삽입
			if(list != null && list.size() > 0) {
				sql = "INSERT INTO MOBILE_MEAL_USERINFO (" +
						"PROJ_NO, SCHEDULERINFOUID, TRIALKEY, SCHEUSERUID, DEPT_NAME" +
						", MEAL_TIME, MEAL_GUBUN, MEAL_DATE, QR_YN" +
						", ANDROID_ID, MOBILE_DEVICE_ID, INSERTBY, INSERTDATE, UPDATEBY, UPDATEDATE) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				stmt = conn.prepareStatement(sql);
				
				for(int i = 0; i < list.size(); i++) {
					MobileMealUserinfoBean bean = list.get(i);
					int idx = 1;
					stmt.setString(idx++, bean.getProjNo());
					if(bean.getSchedulerInfoUid() != null) {
						stmt.setInt(idx++, bean.getSchedulerInfoUid());
					} else {
						stmt.setNull(idx++, java.sql.Types.INTEGER);
					}
					stmt.setString(idx++, bean.getTrialKey());
					if(bean.getScheuserUid() != null) {
						stmt.setInt(idx++, bean.getScheuserUid());
					} else {
						stmt.setNull(idx++, java.sql.Types.INTEGER);
					}
					stmt.setString(idx++, bean.getDeptName());
					stmt.setString(idx++, bean.getMealTime());
					stmt.setString(idx++, bean.getMealGubun());
					stmt.setString(idx++, bean.getMealDate());
					stmt.setString(idx++, bean.getQrYn());
					stmt.setString(idx++, bean.getAndroidId());
					stmt.setString(idx++, bean.getMobileDeviceId());
					if(bean.getInsertby() != null) {
						stmt.setInt(idx++, bean.getInsertby());
					} else {
						stmt.setNull(idx++, java.sql.Types.INTEGER);
					}
					stmt.setString(idx++, bean.getInsertdate());
					if(bean.getUpdateby() != null) {
						stmt.setInt(idx++, bean.getUpdateby());
					} else {
						stmt.setNull(idx++, java.sql.Types.INTEGER);
					}
					stmt.setString(idx++, bean.getUpdatedate());
					
					stmt.executeUpdate();
				}
				stmt.close();
			}
			
			conn.commit();
		}catch(Exception e) {
			isError = true;
			if(conn != null) conn.rollback();
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.setAutoCommit(true);
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
	
	/**
	 * 온라인 DB에 모바일 식사 신청자 SMS 전송 정보 업로드 (삭제 후 삽입)
	 */
	public static boolean uploadMobileMealSmsUserinfo(String schedulerInfoUidList, List<MobileMealSmsUserinfoBean> list) throws Exception {
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			conn.setAutoCommit(false);
			
			// 기존 데이터 삭제 (일괄 삭제)
			String sql = "DELETE FROM MOBILE_MEAL_SMS_USERINFO WHERE SCHEDULERINFOUID IN (" + schedulerInfoUidList + ")";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();
			
			// 데이터 삽입
			if(list != null && list.size() > 0) {
				sql = "INSERT INTO MOBILE_MEAL_SMS_USERINFO (" +
						"PROJ_NO, SCHEDULERINFOUID, TRIALKEY, ANCHOR_MEAL_UID, REG_USERNAME" +
						", PHONE, INSERT_DATE, REG_DEVICE, SMSYN, SMSR_RETURN, SMS_INSERT_DATE) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				stmt = conn.prepareStatement(sql);
				
				for(int i = 0; i < list.size(); i++) {
					MobileMealSmsUserinfoBean bean = list.get(i);
					int idx = 1;
					stmt.setString(idx++, bean.getProjNo());
					if(bean.getSchedulerInfoUid() != null) {
						stmt.setLong(idx++, bean.getSchedulerInfoUid());
					} else {
						stmt.setNull(idx++, java.sql.Types.BIGINT);
					}
					stmt.setString(idx++, bean.getTrialKey());
					stmt.setLong(idx++, bean.getAnchorMealUid());
					stmt.setString(idx++, bean.getRegUsername());
					stmt.setString(idx++, bean.getPhone());
					stmt.setString(idx++, bean.getInsertDate());
					stmt.setString(idx++, bean.getRegDevice());
					stmt.setString(idx++, bean.getSmsyn());
					stmt.setString(idx++, bean.getSmsrReturn());
					stmt.setString(idx++, bean.getSmsInsertDate());
					
					stmt.executeUpdate();
				}
				stmt.close();
			}
			
			conn.commit();
		}catch(Exception e) {
			isError = true;
			if(conn != null) conn.rollback();
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.setAutoCommit(true);
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		return isError;
	}
}

