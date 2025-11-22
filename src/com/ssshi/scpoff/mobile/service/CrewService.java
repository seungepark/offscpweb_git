package com.ssshi.scpoff.mobile.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;

import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssshi.scpoff.constant.Const;
import com.ssshi.scpoff.constant.DBConst;
import com.ssshi.scpoff.dto.ParamBean;
import com.ssshi.scpoff.mobile.dto.MobileScheCrewBean;
import com.ssshi.scpoff.mobile.dto.MobileScheCrewDetailBean;
import com.ssshi.scpoff.mobile.dto.MobileRoomInfoBean;
import com.ssshi.scpoff.mobile.dto.MobileRoomUserlistBean;
import com.ssshi.scpoff.mobile.dto.MobileLiftBoatInfoBean;
import com.ssshi.scpoff.mobile.dto.MobileLiftBoatCrewInfoBean;
import com.ssshi.scpoff.mobile.dto.MobileNotificationBean;
import com.ssshi.scpoff.mobile.dto.MobileOfflineCommandInfoBean;
import com.ssshi.scpoff.mobile.dto.MobileMealSmsUserinfoBean;
import com.ssshi.scpoff.mobile.dto.MobileMealUserinfoBean;
import com.ssshi.scpoff.mybatis.dao.MobileDao;
import com.ssshi.scpoff.mybatis.dao.MobileDaoI;

import org.springframework.transaction.annotation.Transactional;
 

/********************************************************************************
 * 프로그램 개요 : 모바일 승선자 시스템 데이터 동기화 서비스 온라인(SCP OnLine)<->오프라인 (코멘더 노트북)
 * 
 * 작성자 : picnic.company11@gmail.com  
 ********************************************************************************/

@Service
public class CrewService implements CrewServiceI {

	 
	
	@Autowired
	private MobileDaoI offMobileDao;
	
	
	/**
	 * 온라인 모바일 다운로드
	 */
	@Override
	public Map<String, Object> onlineMobileDownload(HttpServletRequest request, ParamBean bean) throws Exception {
	
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isErr = false;
		
		//스케줄넘버 (schedulerInfoUid) 리스트
		int[] schedulerInfoUidArr = bean.getUidArr(); 
		String schedulerInfoUidList = "";
		if(schedulerInfoUidArr != null && schedulerInfoUidArr.length > 0) {
			schedulerInfoUidList = Arrays.stream(schedulerInfoUidArr).mapToObj(String::valueOf).collect(Collectors.joining(","));
		 
			System.out.println("========================================");
			System.out.println("[onlineMoileDownload] 시작 - schedulerInfoUidList: " + schedulerInfoUidList);
			
			//순서 : 온라인 SC DB 조회, 오프라인DB삭제, 오프라인DB삽입
			// 1. 모바일 승선자 상세 정보  (schedulerInfoUid 기준)  
			Map<String, Object> mobileCrewDetail = MobileDao.downMobileScheCrewdetail(schedulerInfoUidList);
			@SuppressWarnings("unchecked")
			List<MobileScheCrewDetailBean> listMobileCrewDetail = (List<MobileScheCrewDetailBean>) mobileCrewDetail.get(Const.LIST);
			boolean isDetailErr = (boolean) mobileCrewDetail.get(Const.ERRCODE);
			
			// 데이터 가져온 수량 로그
			int dataCount = (listMobileCrewDetail != null) ? listMobileCrewDetail.size() : 0;
			System.out.println("[onlineMoileDownload] 데이터 조회 완료 - 수량: " + dataCount + "건, 에러여부: " + isDetailErr);
			
			if(!isDetailErr && listMobileCrewDetail != null && listMobileCrewDetail.size() > 0) {
				// 기존 모바일 상세 데이터 삭제 - schedulerInfoUid 기준
				int deleteCount = 0;
				for(int i = 0; i < schedulerInfoUidArr.length; i++) {
					offMobileDao.deleteMobileScheCrewDetail(schedulerInfoUidArr[i]);
					deleteCount++;
				}
				System.out.println("[onlineMoileDownload] 데이터 삭제 완료 - 삭제된 schedulerInfoUid 수: " + deleteCount + "개");
				
				// 모바일 상세 데이터 삽입 
				int insertCount = 0;
				for(int i = 0; i < listMobileCrewDetail.size(); i++) {
					offMobileDao.insertMobileScheCrewDetail(listMobileCrewDetail.get(i));
					insertCount++;
				}
				System.out.println("[onlineMoileDownload] 데이터 삽입 완료 - 삽입된 데이터 수: " + insertCount + "건");
			} else {
				if(isDetailErr) {
					System.out.println("[onlineMoileDownload] 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[onlineMoileDownload] 삽입할 데이터 없음 - 데이터 수량: " + dataCount + "건");
				}
			}
			
			if(isDetailErr) {
				isErr = true;
			}
			
			// 2. 모바일 방정보 정보  (schedulerInfoUid 기준)  
			Map<String, Object> mobileRoomInfo = MobileDao.downMobileRoomInfo(schedulerInfoUidList);
			@SuppressWarnings("unchecked")
			List<MobileRoomInfoBean> listMobileRoomInfo = (List<MobileRoomInfoBean>) mobileRoomInfo.get(Const.LIST);
			boolean isRoomInfoErr = (boolean) mobileRoomInfo.get(Const.ERRCODE);
			
			// 데이터 가져온 수량 로그
			int roomInfoCount = (listMobileRoomInfo != null) ? listMobileRoomInfo.size() : 0;
			System.out.println("[onlineMoileDownload] 방정보 데이터 조회 완료 - 수량: " + roomInfoCount + "건, 에러여부: " + isRoomInfoErr);
			
			if(!isRoomInfoErr && listMobileRoomInfo != null && listMobileRoomInfo.size() > 0) {
				// 기존 모바일 방정보 데이터 삭제 - schedulerInfoUid 기준
				int deleteRoomInfoCount = 0;
				for(int i = 0; i < schedulerInfoUidArr.length; i++) {
					offMobileDao.deleteMobileRoomInfo(schedulerInfoUidArr[i]);
					deleteRoomInfoCount++;
				}
				System.out.println("[onlineMoileDownload] 방정보 데이터 삭제 완료 - 삭제된 schedulerInfoUid 수: " + deleteRoomInfoCount + "개");
				
				// 모바일 방정보 데이터 삽입 
				int insertRoomInfoCount = 0;
				for(int i = 0; i < listMobileRoomInfo.size(); i++) {
					offMobileDao.insertMobileRoomInfo(listMobileRoomInfo.get(i));
					insertRoomInfoCount++;
				}
				System.out.println("[onlineMoileDownload] 방정보 데이터 삽입 완료 - 삽입된 데이터 수: " + insertRoomInfoCount + "건");
			} else {
				if(isRoomInfoErr) {
					System.out.println("[onlineMoileDownload] 방정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[onlineMoileDownload] 삽입할 방정보 데이터 없음 - 데이터 수량: " + roomInfoCount + "건");
				}
			}
			
			if(isRoomInfoErr) {
				isErr = true;
			}
			
			// 3. 모바일 방배정명단정보 정보  (schedulerInfoUid 기준)  
			Map<String, Object> mobileRoomUserlist = MobileDao.downMobileRoomUserlist(schedulerInfoUidList);
			@SuppressWarnings("unchecked")
			List<MobileRoomUserlistBean> listMobileRoomUserlist = (List<MobileRoomUserlistBean>) mobileRoomUserlist.get(Const.LIST);
			boolean isRoomUserlistErr = (boolean) mobileRoomUserlist.get(Const.ERRCODE);
			
			// 데이터 가져온 수량 로그
			int roomUserlistCount = (listMobileRoomUserlist != null) ? listMobileRoomUserlist.size() : 0;
			System.out.println("[onlineMoileDownload] 방배정명단정보 데이터 조회 완료 - 수량: " + roomUserlistCount + "건, 에러여부: " + isRoomUserlistErr);
			
			if(!isRoomUserlistErr && listMobileRoomUserlist != null && listMobileRoomUserlist.size() > 0) {
				// 기존 모바일 방배정명단정보 데이터 삭제 - schedulerInfoUid 기준
				int deleteRoomUserlistCount = 0;
				for(int i = 0; i < schedulerInfoUidArr.length; i++) {
					offMobileDao.deleteMobileRoomUserlist(schedulerInfoUidArr[i]);
					deleteRoomUserlistCount++;
				}
				System.out.println("[onlineMoileDownload] 방배정명단정보 데이터 삭제 완료 - 삭제된 schedulerInfoUid 수: " + deleteRoomUserlistCount + "개");
				
				// 모바일 방배정명단정보 데이터 삽입 
				int insertRoomUserlistCount = 0;
				for(int i = 0; i < listMobileRoomUserlist.size(); i++) {
					offMobileDao.insertMobileRoomUserlist(listMobileRoomUserlist.get(i));
					insertRoomUserlistCount++;
				}
				System.out.println("[onlineMoileDownload] 방배정명단정보 데이터 삽입 완료 - 삽입된 데이터 수: " + insertRoomUserlistCount + "건");
			} else {
				if(isRoomUserlistErr) {
					System.out.println("[onlineMoileDownload] 방배정명단정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[onlineMoileDownload] 삽입할 방배정명단정보 데이터 없음 - 데이터 수량: " + roomUserlistCount + "건");
				}
			}
			
			if(isRoomUserlistErr) {
				isErr = true;
			}
			
			// 4. 모바일 구명정기본 정보  (schedulerInfoUid 기준)  
			Map<String, Object> mobileLiftBoatInfo = MobileDao.downMobileLiftBoatInfo(schedulerInfoUidList);
			@SuppressWarnings("unchecked")
			List<MobileLiftBoatInfoBean> listMobileLiftBoatInfo = (List<MobileLiftBoatInfoBean>) mobileLiftBoatInfo.get(Const.LIST);
			boolean isLiftBoatInfoErr = (boolean) mobileLiftBoatInfo.get(Const.ERRCODE);
			
			// 데이터 가져온 수량 로그
			int liftBoatInfoCount = (listMobileLiftBoatInfo != null) ? listMobileLiftBoatInfo.size() : 0;
			System.out.println("[onlineMoileDownload] 구명정기본정보 데이터 조회 완료 - 수량: " + liftBoatInfoCount + "건, 에러여부: " + isLiftBoatInfoErr);
			
			if(!isLiftBoatInfoErr && listMobileLiftBoatInfo != null && listMobileLiftBoatInfo.size() > 0) {
				// 기존 모바일 구명정기본정보 데이터 삭제 - schedulerInfoUid 기준
				int deleteLiftBoatInfoCount = 0;
				for(int i = 0; i < schedulerInfoUidArr.length; i++) {
					offMobileDao.deleteMobileLiftBoatInfo(schedulerInfoUidArr[i]);
					deleteLiftBoatInfoCount++;
				}
				System.out.println("[onlineMoileDownload] 구명정기본정보 데이터 삭제 완료 - 삭제된 schedulerInfoUid 수: " + deleteLiftBoatInfoCount + "개");
				
				// 모바일 구명정기본정보 데이터 삽입 
				int insertLiftBoatInfoCount = 0;
				for(int i = 0; i < listMobileLiftBoatInfo.size(); i++) {
					offMobileDao.insertMobileLiftBoatInfo(listMobileLiftBoatInfo.get(i));
					insertLiftBoatInfoCount++;
				}
				System.out.println("[onlineMoileDownload] 구명정기본정보 데이터 삽입 완료 - 삽입된 데이터 수: " + insertLiftBoatInfoCount + "건");
			} else {
				if(isLiftBoatInfoErr) {
					System.out.println("[onlineMoileDownload] 구명정기본정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[onlineMoileDownload] 삽입할 구명정기본정보 데이터 없음 - 데이터 수량: " + liftBoatInfoCount + "건");
				}
			}
			
			if(isLiftBoatInfoErr) {
				isErr = true;
			}
			
			// 5. 모바일 구명정배정 인원 명단 정보  (schedulerInfoUid 기준)  
			Map<String, Object> mobileLiftBoatCrewinfo = MobileDao.downMobileLiftBoatCrewinfo(schedulerInfoUidList);
			@SuppressWarnings("unchecked")
			List<MobileLiftBoatCrewInfoBean> listMobileLiftBoatCrewinfo = (List<MobileLiftBoatCrewInfoBean>) mobileLiftBoatCrewinfo.get(Const.LIST);
			boolean isLiftBoatCrewinfoErr = (boolean) mobileLiftBoatCrewinfo.get(Const.ERRCODE);
			
			// 데이터 가져온 수량 로그
			int liftBoatCrewinfoCount = (listMobileLiftBoatCrewinfo != null) ? listMobileLiftBoatCrewinfo.size() : 0;
			System.out.println("[onlineMoileDownload] 구명정배정 인원 명단정보 데이터 조회 완료 - 수량: " + liftBoatCrewinfoCount + "건, 에러여부: " + isLiftBoatCrewinfoErr);
			
			if(!isLiftBoatCrewinfoErr && listMobileLiftBoatCrewinfo != null && listMobileLiftBoatCrewinfo.size() > 0) {
				// 기존 모바일 구명정배정 인원 명단정보 데이터 삭제 - schedulerInfoUid 기준
				int deleteLiftBoatCrewinfoCount = 0;
				for(int i = 0; i < schedulerInfoUidArr.length; i++) {
					offMobileDao.deleteMobileLiftBoatCrewinfo(schedulerInfoUidArr[i]);
					deleteLiftBoatCrewinfoCount++;
				}
				System.out.println("[onlineMoileDownload] 구명정배정 인원 명단정보 데이터 삭제 완료 - 삭제된 schedulerInfoUid 수: " + deleteLiftBoatCrewinfoCount + "개");
				
				// 모바일 구명정배정 인원 명단정보 데이터 삽입 
				int insertLiftBoatCrewinfoCount = 0;
				for(int i = 0; i < listMobileLiftBoatCrewinfo.size(); i++) {
					offMobileDao.insertMobileLiftBoatCrewinfo(listMobileLiftBoatCrewinfo.get(i));
					insertLiftBoatCrewinfoCount++;
				}
				System.out.println("[onlineMoileDownload] 구명정배정 인원 명단정보 데이터 삽입 완료 - 삽입된 데이터 수: " + insertLiftBoatCrewinfoCount + "건");
			} else {
				if(isLiftBoatCrewinfoErr) {
					System.out.println("[onlineMoileDownload] 구명정배정 인원 명단정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[onlineMoileDownload] 삽입할 구명정배정 인원 명단정보 데이터 없음 - 데이터 수량: " + liftBoatCrewinfoCount + "건");
				}
			}
			
			if(isLiftBoatCrewinfoErr) {
				isErr = true;
			}
			
			// 6. 모바일 공지사항 정보  (schedulerInfoUid 기준)  
			Map<String, Object> mobileNotification = MobileDao.downMobileNotification(schedulerInfoUidList);
			@SuppressWarnings("unchecked")
			List<MobileNotificationBean> listMobileNotification = (List<MobileNotificationBean>) mobileNotification.get(Const.LIST);
			boolean isNotificationErr = (boolean) mobileNotification.get(Const.ERRCODE);
			
			// 데이터 가져온 수량 로그
			int notificationCount = (listMobileNotification != null) ? listMobileNotification.size() : 0;
			System.out.println("[onlineMoileDownload] 공지사항정보 데이터 조회 완료 - 수량: " + notificationCount + "건, 에러여부: " + isNotificationErr);
			
			if(!isNotificationErr && listMobileNotification != null && listMobileNotification.size() > 0) {
				// 기존 모바일 공지사항정보 데이터 삭제 - schedulerInfoUid 기준
				int deleteNotificationCount = 0;
				for(int i = 0; i < schedulerInfoUidArr.length; i++) {
					offMobileDao.deleteMobileNotification(schedulerInfoUidArr[i]);
					deleteNotificationCount++;
				}
				System.out.println("[onlineMoileDownload] 공지사항정보 데이터 삭제 완료 - 삭제된 schedulerInfoUid 수: " + deleteNotificationCount + "개");
				
				// 모바일 공지사항정보 데이터 삽입 
				int insertNotificationCount = 0;
				for(int i = 0; i < listMobileNotification.size(); i++) {
					offMobileDao.insertMobileNotification(listMobileNotification.get(i));
					insertNotificationCount++;
				}
				System.out.println("[onlineMoileDownload] 공지사항정보 데이터 삽입 완료 - 삽입된 데이터 수: " + insertNotificationCount + "건");
			} else {
				if(isNotificationErr) {
					System.out.println("[onlineMoileDownload] 공지사항정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[onlineMoileDownload] 삽입할 공지사항정보 데이터 없음 - 데이터 수량: " + notificationCount + "건");
				}
			}
			
			if(isNotificationErr) {
				isErr = true;
			}
			
			// 7. 모바일 OFFLine 코멘더 서버 정보  (schedulerInfoUid 기준)  
			Map<String, Object> mobileOfflineCommandInfo = MobileDao.downMobileOfflineCommandInfo(schedulerInfoUidList);
			@SuppressWarnings("unchecked")
			List<MobileOfflineCommandInfoBean> listMobileOfflineCommandInfo = (List<MobileOfflineCommandInfoBean>) mobileOfflineCommandInfo.get(Const.LIST);
			boolean isOfflineCommandInfoErr = (boolean) mobileOfflineCommandInfo.get(Const.ERRCODE);
			
			// 데이터 가져온 수량 로그
			int offlineCommandInfoCount = (listMobileOfflineCommandInfo != null) ? listMobileOfflineCommandInfo.size() : 0;
			System.out.println("[onlineMoileDownload] 오프라인 코멘더 서버정보 데이터 조회 완료 - 수량: " + offlineCommandInfoCount + "건, 에러여부: " + isOfflineCommandInfoErr);
			
			if(!isOfflineCommandInfoErr && listMobileOfflineCommandInfo != null && listMobileOfflineCommandInfo.size() > 0) {
				// 기존 모바일 오프라인 코멘더 서버정보 데이터 삭제 - schedulerInfoUid 기준
				int deleteOfflineCommandInfoCount = 0;
				for(int i = 0; i < schedulerInfoUidArr.length; i++) {
					offMobileDao.deleteMobileOfflineCommandInfo(schedulerInfoUidArr[i]);
					deleteOfflineCommandInfoCount++;
				}
				System.out.println("[onlineMoileDownload] 오프라인 코멘더 서버정보 데이터 삭제 완료 - 삭제된 schedulerInfoUid 수: " + deleteOfflineCommandInfoCount + "개");
				
				// 모바일 오프라인 코멘더 서버정보 데이터 삽입 
				int insertOfflineCommandInfoCount = 0;
				for(int i = 0; i < listMobileOfflineCommandInfo.size(); i++) {
					offMobileDao.insertMobileOfflineCommandInfo(listMobileOfflineCommandInfo.get(i));
					insertOfflineCommandInfoCount++;
				}
				System.out.println("[onlineMoileDownload] 오프라인 코멘더 서버정보 데이터 삽입 완료 - 삽입된 데이터 수: " + insertOfflineCommandInfoCount + "건");
			} else {
				if(isOfflineCommandInfoErr) {
					System.out.println("[onlineMoileDownload] 오프라인 코멘더 서버정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[onlineMoileDownload] 삽입할 오프라인 코멘더 서버정보 데이터 없음 - 데이터 수량: " + offlineCommandInfoCount + "건");
				}
			}
			
			if(isOfflineCommandInfoErr) {
				isErr = true;
			}
			
			System.out.println("[onlineMoileDownload] 종료 - 최종 에러여부: " + isErr);
			System.out.println("========================================");
		}
		
		resultMap.put(Const.RESULT, DBConst.SUCCESS);
		resultMap.put(Const.ERRCODE, isErr);
		
		return resultMap;
	}
	
	/**
	 * 오프라인 모바일 업로드
	 * 오프라인 DB 데이터를 온라인 DB로 업로드
	 */
	@Override
	public Map<String, Object> offlineMobileUpload(HttpServletRequest request, ParamBean bean) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isErr = false;
		
		System.out.println("========================================");
		System.out.println("[offlineMobileUpload] 함수 호출됨");
		
		//스케줄넘버 (schedulerInfoUid) 리스트
		int[] schedulerInfoUidArr = bean.getUidArr();

		String schedulerInfoUidList = "";
		if(schedulerInfoUidArr != null && schedulerInfoUidArr.length > 0) {
			schedulerInfoUidList = Arrays.stream(schedulerInfoUidArr).mapToObj(String::valueOf).collect(Collectors.joining(","));
		 
			System.out.println("[offlineMobileUpload] 시작 - schedulerInfoUidList: " + schedulerInfoUidList);
			
			//순서 : 오프라인 DB 조회, 온라인 DB 삭제, 온라인 DB 삽입
			// 1. 모바일 승선자 상세 정보  (schedulerInfoUid 기준)  
			List<MobileScheCrewDetailBean> listMobileCrewDetail = null;
			boolean isDetailErr = false;
			try {
				listMobileCrewDetail = offMobileDao.upMobileScheCrewdetail(schedulerInfoUidList);
				isDetailErr = (listMobileCrewDetail == null);
				System.out.println("[offlineMobileUpload] 승선자 상세정보 조회 완료 - listMobileCrewDetail: " + (listMobileCrewDetail != null ? "not null" : "null"));
			} catch(Exception e) {
				isDetailErr = true;
				System.out.println("[offlineMobileUpload] 승선자 상세정보 조회 중 예외 발생: " + e.getMessage());
				e.printStackTrace();
			}
			
			// 데이터 가져온 수량 로그
			int dataCount = (listMobileCrewDetail != null) ? listMobileCrewDetail.size() : 0;
			System.out.println("[offlineMobileUpload] 승선자 상세정보 데이터 조회 완료 - 수량: " + dataCount + "건, 에러여부: " + isDetailErr);
			
			if(!isDetailErr && listMobileCrewDetail != null && listMobileCrewDetail.size() > 0) {
				// 온라인 DB로 일괄 업로드
				boolean uploadErr = MobileDao.uploadMobileScheCrewdetail(schedulerInfoUidList, listMobileCrewDetail);
				if(uploadErr) {
					isErr = true;
					System.out.println("[offlineMobileUpload] 승선자 상세정보 업로드 실패");
				} else {
					System.out.println("[offlineMobileUpload] 승선자 상세정보 업로드 완료 - 데이터 수: " + dataCount + "건");
				}
			} else {
				if(isDetailErr) {
					System.out.println("[offlineMobileUpload] 승선자 상세정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[offlineMobileUpload] 업로드할 승선자 상세정보 데이터 없음 - 데이터 수량: " + dataCount + "건");
				}
			}
			
			if(isDetailErr) {
				isErr = true;
			}
			
			// 2. 모바일 방정보 정보  (schedulerInfoUid 기준)  
			List<MobileRoomInfoBean> listMobileRoomInfo = offMobileDao.upMobileRoomInfo(schedulerInfoUidList);
			boolean isRoomInfoErr = (listMobileRoomInfo == null);
			
			int roomInfoCount = (listMobileRoomInfo != null) ? listMobileRoomInfo.size() : 0;
			System.out.println("[offlineMobileUpload] 방정보 데이터 조회 완료 - 수량: " + roomInfoCount + "건, 에러여부: " + isRoomInfoErr);
			
			if(!isRoomInfoErr && listMobileRoomInfo != null && listMobileRoomInfo.size() > 0) {
				// 온라인 DB로 일괄 업로드
				boolean uploadErr = MobileDao.uploadMobileRoomInfo(schedulerInfoUidList, listMobileRoomInfo);
				if(uploadErr) {
					isErr = true;
					System.out.println("[offlineMobileUpload] 방정보 업로드 실패");
				} else {
					System.out.println("[offlineMobileUpload] 방정보 업로드 완료 - 데이터 수: " + roomInfoCount + "건");
				}
			} else {
				if(isRoomInfoErr) {
					System.out.println("[offlineMobileUpload] 방정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[offlineMobileUpload] 업로드할 방정보 데이터 없음 - 데이터 수량: " + roomInfoCount + "건");
				}
			}
			
			if(isRoomInfoErr) {
				isErr = true;
			}
			
			// 3. 모바일 방배정명단정보 정보  (schedulerInfoUid 기준)  
			List<MobileRoomUserlistBean> listMobileRoomUserlist = offMobileDao.upMobileRoomUserlist(schedulerInfoUidList);
			boolean isRoomUserlistErr = (listMobileRoomUserlist == null);
			
			int roomUserlistCount = (listMobileRoomUserlist != null) ? listMobileRoomUserlist.size() : 0;
			System.out.println("[offlineMobileUpload] 방배정명단정보 데이터 조회 완료 - 수량: " + roomUserlistCount + "건, 에러여부: " + isRoomUserlistErr);
			
			if(!isRoomUserlistErr && listMobileRoomUserlist != null && listMobileRoomUserlist.size() > 0) {
				// 온라인 DB로 일괄 업로드
				boolean uploadErr = MobileDao.uploadMobileRoomUserlist(schedulerInfoUidList, listMobileRoomUserlist);
				if(uploadErr) {
					isErr = true;
					System.out.println("[offlineMobileUpload] 방배정명단정보 업로드 실패");
				} else {
					System.out.println("[offlineMobileUpload] 방배정명단정보 업로드 완료 - 데이터 수: " + roomUserlistCount + "건");
				}
			} else {
				if(isRoomUserlistErr) {
					System.out.println("[offlineMobileUpload] 방배정명단정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[offlineMobileUpload] 업로드할 방배정명단정보 데이터 없음 - 데이터 수량: " + roomUserlistCount + "건");
				}
			}
			
			if(isRoomUserlistErr) {
				isErr = true;
			}
			
			// 4. 모바일 구명정기본 정보  (schedulerInfoUid 기준)  
			List<MobileLiftBoatInfoBean> listMobileLiftBoatInfo = offMobileDao.upMobileLiftBoatInfo(schedulerInfoUidList);
			boolean isLiftBoatInfoErr = (listMobileLiftBoatInfo == null);
			
			int liftBoatInfoCount = (listMobileLiftBoatInfo != null) ? listMobileLiftBoatInfo.size() : 0;
			System.out.println("[offlineMobileUpload] 구명정기본정보 데이터 조회 완료 - 수량: " + liftBoatInfoCount + "건, 에러여부: " + isLiftBoatInfoErr);
			
			if(!isLiftBoatInfoErr && listMobileLiftBoatInfo != null && listMobileLiftBoatInfo.size() > 0) {
				// 온라인 DB로 일괄 업로드
				boolean uploadErr = MobileDao.uploadMobileLiftBoatInfo(schedulerInfoUidList, listMobileLiftBoatInfo);
				if(uploadErr) {
					isErr = true;
					System.out.println("[offlineMobileUpload] 구명정기본정보 업로드 실패");
				} else {
					System.out.println("[offlineMobileUpload] 구명정기본정보 업로드 완료 - 데이터 수: " + liftBoatInfoCount + "건");
				}
			} else {
				if(isLiftBoatInfoErr) {
					System.out.println("[offlineMobileUpload] 구명정기본정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[offlineMobileUpload] 업로드할 구명정기본정보 데이터 없음 - 데이터 수량: " + liftBoatInfoCount + "건");
				}
			}
			
			if(isLiftBoatInfoErr) {
				isErr = true;
			}
			
			// 5. 모바일 구명정배정 인원 명단 정보  (schedulerInfoUid 기준)  
			List<MobileLiftBoatCrewInfoBean> listMobileLiftBoatCrewinfo = offMobileDao.upMobileLiftBoatCrewinfo(schedulerInfoUidList);
			boolean isLiftBoatCrewinfoErr = (listMobileLiftBoatCrewinfo == null);
			
			int liftBoatCrewinfoCount = (listMobileLiftBoatCrewinfo != null) ? listMobileLiftBoatCrewinfo.size() : 0;
			System.out.println("[offlineMobileUpload] 구명정배정 인원 명단정보 데이터 조회 완료 - 수량: " + liftBoatCrewinfoCount + "건, 에러여부: " + isLiftBoatCrewinfoErr);
			
			if(!isLiftBoatCrewinfoErr && listMobileLiftBoatCrewinfo != null && listMobileLiftBoatCrewinfo.size() > 0) {
				// 온라인 DB로 일괄 업로드
				boolean uploadErr = MobileDao.uploadMobileLiftBoatCrewinfo(schedulerInfoUidList, listMobileLiftBoatCrewinfo);
				if(uploadErr) {
					isErr = true;
					System.out.println("[offlineMobileUpload] 구명정배정 인원 명단정보 업로드 실패");
				} else {
					System.out.println("[offlineMobileUpload] 구명정배정 인원 명단정보 업로드 완료 - 데이터 수: " + liftBoatCrewinfoCount + "건");
				}
			} else {
				if(isLiftBoatCrewinfoErr) {
					System.out.println("[offlineMobileUpload] 구명정배정 인원 명단정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[offlineMobileUpload] 업로드할 구명정배정 인원 명단정보 데이터 없음 - 데이터 수량: " + liftBoatCrewinfoCount + "건");
				}
			}
			
			if(isLiftBoatCrewinfoErr) {
				isErr = true;
			}
			
			// 6. 모바일 공지사항 정보  (schedulerInfoUid 기준)  
			List<MobileNotificationBean> listMobileNotification = offMobileDao.upMobileNotification(schedulerInfoUidList);
			boolean isNotificationErr = (listMobileNotification == null);
			
			int notificationCount = (listMobileNotification != null) ? listMobileNotification.size() : 0;
			System.out.println("[offlineMobileUpload] 공지사항정보 데이터 조회 완료 - 수량: " + notificationCount + "건, 에러여부: " + isNotificationErr);
			
			if(!isNotificationErr && listMobileNotification != null && listMobileNotification.size() > 0) {
				// 온라인 DB로 일괄 업로드
				boolean uploadErr = MobileDao.uploadMobileNotification(schedulerInfoUidList, listMobileNotification);
				if(uploadErr) {
					isErr = true;
					System.out.println("[offlineMobileUpload] 공지사항정보 업로드 실패");
				} else {
					System.out.println("[offlineMobileUpload] 공지사항정보 업로드 완료 - 데이터 수: " + notificationCount + "건");
				}
			} else {
				if(isNotificationErr) {
					System.out.println("[offlineMobileUpload] 공지사항정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[offlineMobileUpload] 업로드할 공지사항정보 데이터 없음 - 데이터 수량: " + notificationCount + "건");
				}
			}
			
			if(isNotificationErr) {
				isErr = true;
			}
			
			// 7. 모바일 OFFLine 코멘더 서버 정보  (schedulerInfoUid 기준)  
			List<MobileOfflineCommandInfoBean> listMobileOfflineCommandInfo = offMobileDao.upMobileOfflineCommandInfo(schedulerInfoUidList);
			boolean isOfflineCommandInfoErr = (listMobileOfflineCommandInfo == null);
			
			int offlineCommandInfoCount = (listMobileOfflineCommandInfo != null) ? listMobileOfflineCommandInfo.size() : 0;
			System.out.println("[offlineMobileUpload] 오프라인 코멘더 서버정보 데이터 조회 완료 - 수량: " + offlineCommandInfoCount + "건, 에러여부: " + isOfflineCommandInfoErr);
			
			if(!isOfflineCommandInfoErr && listMobileOfflineCommandInfo != null && listMobileOfflineCommandInfo.size() > 0) {
				// 온라인 DB로 일괄 업로드
				boolean uploadErr = MobileDao.uploadMobileOfflineCommandInfo(schedulerInfoUidList, listMobileOfflineCommandInfo);
				if(uploadErr) {
					isErr = true;
					System.out.println("[offlineMobileUpload] 오프라인 코멘더 서버정보 업로드 실패");
				} else {
					System.out.println("[offlineMobileUpload] 오프라인 코멘더 서버정보 업로드 완료 - 데이터 수: " + offlineCommandInfoCount + "건");
				}
			} else {
				if(isOfflineCommandInfoErr) {
					System.out.println("[offlineMobileUpload] 오프라인 코멘더 서버정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[offlineMobileUpload] 업로드할 오프라인 코멘더 서버정보 데이터 없음 - 데이터 수량: " + offlineCommandInfoCount + "건");
				}
			}
			
			if(isOfflineCommandInfoErr) {
				isErr = true;
			}
			
			// 8. 모바일 시운전 식사 실적 정보  (schedulerInfoUid 기준)  
			List<MobileMealUserinfoBean> listMobileMealUserinfo = offMobileDao.upMobileMealUserinfo(schedulerInfoUidList);
			boolean isMealUserinfoErr = (listMobileMealUserinfo == null);
			
			int mealUserinfoCount = (listMobileMealUserinfo != null) ? listMobileMealUserinfo.size() : 0;
			System.out.println("[offlineMobileUpload] 시운전 식사 실적정보 데이터 조회 완료 - 수량: " + mealUserinfoCount + "건, 에러여부: " + isMealUserinfoErr);
			
			if(!isMealUserinfoErr && listMobileMealUserinfo != null && listMobileMealUserinfo.size() > 0) {
				// 온라인 DB로 일괄 업로드
				boolean uploadErr = MobileDao.uploadMobileMealUserinfo(schedulerInfoUidList, listMobileMealUserinfo);
				if(uploadErr) {
					isErr = true;
					System.out.println("[offlineMobileUpload] 시운전 식사 실적정보 업로드 실패");
				} else {
					System.out.println("[offlineMobileUpload] 시운전 식사 실적정보 업로드 완료 - 데이터 수: " + mealUserinfoCount + "건");
				}
			} else {
				if(isMealUserinfoErr) {
					System.out.println("[offlineMobileUpload] 시운전 식사 실적정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[offlineMobileUpload] 업로드할 시운전 식사 실적정보 데이터 없음 - 데이터 수량: " + mealUserinfoCount + "건");
				}
			}
			
			if(isMealUserinfoErr) {
				isErr = true;
			}
			
			// 10. 모바일 식사 신청자 SMS 전송 정보  (schedulerInfoUid 기준)  
			List<MobileMealSmsUserinfoBean> listMobileMealSmsUserinfo = offMobileDao.upMobileMealSmsUserinfo(schedulerInfoUidList);
			boolean isMealSmsUserinfoErr = (listMobileMealSmsUserinfo == null);
			
			int mealSmsUserinfoCount = (listMobileMealSmsUserinfo != null) ? listMobileMealSmsUserinfo.size() : 0;
			System.out.println("[offlineMobileUpload] 식사 신청자 SMS 전송정보 데이터 조회 완료 - 수량: " + mealSmsUserinfoCount + "건, 에러여부: " + isMealSmsUserinfoErr);
			
			if(!isMealSmsUserinfoErr && listMobileMealSmsUserinfo != null && listMobileMealSmsUserinfo.size() > 0) {
				// 온라인 DB로 일괄 업로드
				boolean uploadErr = MobileDao.uploadMobileMealSmsUserinfo(schedulerInfoUidList, listMobileMealSmsUserinfo);
				if(uploadErr) {
					isErr = true;
					System.out.println("[offlineMobileUpload] 식사 신청자 SMS 전송정보 업로드 실패");
				} else {
					System.out.println("[offlineMobileUpload] 식사 신청자 SMS 전송정보 업로드 완료 - 데이터 수: " + mealSmsUserinfoCount + "건");
				}
			} else {
				if(isMealSmsUserinfoErr) {
					System.out.println("[offlineMobileUpload] 식사 신청자 SMS 전송정보 데이터 조회 실패 - 에러 발생");
				} else {
					System.out.println("[offlineMobileUpload] 업로드할 식사 신청자 SMS 전송정보 데이터 없음 - 데이터 수량: " + mealSmsUserinfoCount + "건");
				}
			}
			
			if(isMealSmsUserinfoErr) {
				isErr = true;
			}
			
			System.out.println("[offlineMobileUpload] 종료 - 최종 에러여부: " + isErr);
			System.out.println("========================================");
		} else {
			System.out.println("[offlineMobileUpload] schedulerInfoUidArr가 null이거나 비어있어서 처리하지 않습니다.");
			System.out.println("========================================");
		}
		
		resultMap.put(Const.RESULT, DBConst.SUCCESS);
		resultMap.put(Const.ERRCODE, isErr);
		
		return resultMap;
	}
	
	 
}
	

