package com.ssshi.scpoff.mybatis.dao;

import java.util.List;

import com.ssshi.scpoff.mobile.dto.MobileScheCrewBean;
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

public interface MobileDaoI {
 
	
	void deleteMobileScheCrewDetail(int scheCrewUid); 
	
	void insertMobileScheCrewDetail(MobileScheCrewDetailBean bean);
	
	void deleteMobileRoomInfo(int schedulerInfoUid);
	
	void insertMobileRoomInfo(MobileRoomInfoBean bean);
	
	void deleteMobileRoomUserlist(int schedulerInfoUid);
	
	void insertMobileRoomUserlist(MobileRoomUserlistBean bean);
	
	void deleteMobileLiftBoatInfo(int schedulerInfoUid);
	
	void insertMobileLiftBoatInfo(MobileLiftBoatInfoBean bean);
	
	void deleteMobileLiftBoatCrewinfo(int schedulerInfoUid);
	
	void insertMobileLiftBoatCrewinfo(MobileLiftBoatCrewInfoBean bean);
	
	void deleteMobileNotification(int schedulerInfoUid);
	
	void insertMobileNotification(MobileNotificationBean bean);
	
	void deleteMobileOfflineCommandInfo(int schedulerInfoUid);
	
	void insertMobileOfflineCommandInfo(MobileOfflineCommandInfoBean bean);
	
	// 오프라인 DB 조회 메서드 (업로드용)
	List<MobileScheCrewDetailBean> upMobileScheCrewdetail(String schedulerInfoUidList);
	
	List<MobileRoomInfoBean> upMobileRoomInfo(String schedulerInfoUidList);
	
	List<MobileRoomUserlistBean> upMobileRoomUserlist(String schedulerInfoUidList);
	
	List<MobileLiftBoatInfoBean> upMobileLiftBoatInfo(String schedulerInfoUidList);
	
	List<MobileLiftBoatCrewInfoBean> upMobileLiftBoatCrewinfo(String schedulerInfoUidList);
	
	List<MobileNotificationBean> upMobileNotification(String schedulerInfoUidList);
	
	List<MobileOfflineCommandInfoBean> upMobileOfflineCommandInfo(String schedulerInfoUidList);
	
	List<MobileMealUserinfoBean> upMobileMealUserinfo(String schedulerInfoUidList);
	
	List<MobileMealSmsUserinfoBean> upMobileMealSmsUserinfo(String schedulerInfoUidList);
 
}
