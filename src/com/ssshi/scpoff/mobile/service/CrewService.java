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
import com.ssshi.scpoff.dto.RegistrationCrewQtyBean;
import com.ssshi.scpoff.dto.RegistrationCrewRequestBean;
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
import com.ssshi.scpoff.mybatis.dao.CrewDaoI;
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
	
	@Autowired
	private CrewDaoI crewDao;
	
	@Override
	public Map<String, Object> crewResultMeal(HttpServletRequest request, RegistrationCrewRequestBean bean) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		/* 조회조건 : 호선리스트 */
		resultMap.put(Const.LIST + "Ship", crewDao.getShipList());
		
		// 조회 조건 확인: inDate와 outDate가 모두 있어야 조회 수행
		boolean hasSearchCondition = (bean.getInDate() != null && !bean.getInDate().isEmpty()) 
								  && (bean.getOutDate() != null && !bean.getOutDate().isEmpty());
		
		if(!hasSearchCondition) {
			System.out.println("[crewResultMeal] 조회 조건이 없어 조회를 수행하지 않습니다. (inDate 또는 outDate가 없음)");
			resultMap.put(Const.LIST, new ArrayList<RegistrationCrewRequestBean>());
			resultMap.put(Const.LIST_CNT, 0);
			resultMap.put(Const.LIST + "Ship", crewDao.getShipList());
			return resultMap;
		}
		
		System.out.println("[crewResultMeal] getMealResultList 쿼리 조회 시작");
		List<RegistrationCrewRequestBean> crewList = crewDao.getCrewMealResultList(bean);
		System.out.println("[crewResultMeal] getMealResultList 쿼리 조회 완료 - 결과 건수: " + (crewList != null ? crewList.size() : 0));
		
		// UID가 NULL인 항목(실적 전용 행)을 별도로 필터링
		List<RegistrationCrewRequestBean> filteredAnchList = new ArrayList<RegistrationCrewRequestBean>();
		List<RegistrationCrewRequestBean> resultOnlyList = new ArrayList<RegistrationCrewRequestBean>();
		if(crewList != null) {
			for(RegistrationCrewRequestBean item : crewList) {
				if(item.getUid() > 0) {
					// 계획이 있는 항목 (UID가 있음)
					filteredAnchList.add(item);
				} else {
					// 실적만 있는 항목 (UID가 NULL)
					resultOnlyList.add(item);
					System.out.println("[crewResultMeal] 실적 전용 행 발견 - 부서: " + item.getDepartment() + ", 날짜: " + item.getMealDate() + ", 호선: " + item.getProjNo());
				}
			}
		}
		System.out.println("[crewResultMeal] 계획 행 필터링 완료 - 계획 행: " + filteredAnchList.size() + ", 실적 전용 행: " + resultOnlyList.size());
		
		crewList = filteredAnchList;  // 계획 행만 사용
		
		resultMap.put(Const.LIST_CNT, crewDao.getCrewMealListCnt());
		System.out.println("[crewResultMeal] LIST_CNT: " + crewDao.getCrewMealListCnt());

		//계획
		System.out.println("[crewResultMeal] 계획(plan) 리스트 설정 시작");
		if(crewList != null) {				
			for(int i = 0; i < crewList.size(); i++) {
				RegistrationCrewRequestBean item = crewList.get(i);
				System.out.println("[crewResultMeal] 계획 조회 - UID: " + item.getUid() + ", 부서: " + item.getDepartment() + ", 날짜: " + item.getMealDate());
				if(item.getUid() > 0) {
					List<RegistrationCrewQtyBean> planList = crewDao.getCrewPlanQtyList(item.getUid());
					item.setPlanList(planList);
					System.out.println("[crewResultMeal]   -> 계획 건수: " + (planList != null ? planList.size() : 0));
					if(planList != null && planList.size() > 0) {
						for(int j = 0; j < planList.size(); j++) {
							RegistrationCrewQtyBean plan = planList.get(j);
							System.out.println("[crewResultMeal]     계획 상세[" + j + "]: 날짜=" + plan.getPlanMealDate() + ", 시간=" + plan.getPlanMealTime() + ", 구분=" + plan.getPlanMealGubun() + ", 수량=" + plan.getPlanMealQty());
						}
					}
				} else {
					System.out.println("[crewResultMeal]   -> UID가 없어 계획 조회 스킵");
					item.setPlanList(new ArrayList<RegistrationCrewQtyBean>());
				}
			}
			System.out.println("[crewResultMeal] 계획(plan) 리스트 설정 완료 - 전체 항목 수: " + crewList.size());
		}
		
		//실적 - 조회기간 전체에 대한 실적을 한번에 조회
		System.out.println("[crewResultMeal] 실적(result) 수량 조회 시작");
		if(crewList != null) {
			// 조회기간 전체에 대한 실적을 한번에 조회
			RegistrationCrewQtyBean allResultQty = new RegistrationCrewQtyBean();
			allResultQty.setProjNo(bean.getShip() != null && !bean.getShip().isEmpty() && !bean.getShip().equals("ALL") ? bean.getShip() : null);
			allResultQty.setInDate(bean.getInDate());
			allResultQty.setOutDate(bean.getOutDate());
			
			System.out.println("[crewResultMeal] getCrewResultQtyList 쿼리 파라미터:");
			System.out.println("  - projNo: " + allResultQty.getProjNo());
			System.out.println("  - inDate: " + allResultQty.getInDate());
			System.out.println("  - outDate: " + allResultQty.getOutDate());
			
			System.out.println("[crewResultMeal] getCrewResultQtyList 쿼리 실행 시작");
			List<RegistrationCrewQtyBean> allResultList = crewDao.getCrewResultQtyList(allResultQty);
			System.out.println("[v] getCrewResultQtyList 쿼리 실행 완료 - 전체 실적 건수: " + (allResultList != null ? allResultList.size() : 0));
			
			// 각 항목별로 부서, 날짜에 맞는 실적 필터링
			System.out.println("[crewResultMeal] 항목별 실적 필터링 시작");
			for(int z = 0; z < crewList.size(); z++) {
				RegistrationCrewRequestBean item = crewList.get(z);
				List<RegistrationCrewQtyBean> filteredResultList = new ArrayList<RegistrationCrewQtyBean>();
				
				System.out.println("[crewResultMeal] 항목[" + z + "] 필터링 - 부서: " + item.getDepartment() + ", 날짜: " + item.getMealDate() + ", 호선: " + item.getProjNo());
				
				if(allResultList != null) {
					int matchCount = 0;
					for(RegistrationCrewQtyBean result : allResultList) {
						// 부서, 날짜, 호선이 일치하는 실적만 추가
						boolean deptMatch = (item.getDepartment() == null && result.getDepartment() == null) ||
										   (item.getDepartment() != null && item.getDepartment().equals(result.getDepartment()));
						String itemDate = item.getMealDate() != null ? item.getMealDate().substring(0, Math.min(10, item.getMealDate().length())) : null;
						String resultDate = result.getResultMealDate() != null && result.getResultMealDate().length() >= 10 ? result.getResultMealDate().substring(0, 10) : result.getResultMealDate();
						boolean dateMatch = (itemDate == null && resultDate == null) ||
										   (itemDate != null && resultDate != null && itemDate.equals(resultDate));
						boolean projMatch = (item.getProjNo() == null && result.getProjNo() == null) ||
										   (item.getProjNo() != null && item.getProjNo().equals(result.getProjNo()));
						
						if(deptMatch && dateMatch && projMatch) {
							filteredResultList.add(result);
							matchCount++;
							System.out.println("[crewResultMeal]   -> 매칭된 실적: 부서=" + result.getDepartment() + ", 날짜=" + resultDate + ", 시간=" + result.getResultMealTime() + ", 구분=" + result.getResultMealGubun() + ", 수량=" + result.getResultMealQty());
						}
					}
					System.out.println("[crewResultMeal] 항목[" + z + "] 필터링 완료 - 매칭된 실적 건수: " + matchCount);
				}
				item.setResultList(filteredResultList);
			}
			System.out.println("[crewResultMeal] 항목별 실적 필터링 완료 - 전체 항목 수: " + crewList.size());
		}

		// 전체 실적 리스트를 조회하여 전달
		System.out.println("[crewResultMeal] ensureResultOnlyDepartmentsIncluded_Crew 호출 시작");
		// ensureResultOnlyDepartmentsIncluded 호출 전 계획 데이터 확인
		if(crewList != null) {
			System.out.println("[crewResultMeal] ensureResultOnlyDepartmentsIncluded_Crew 호출 전 계획 데이터 확인:");
			for(int i = 0; i < crewList.size(); i++) {
				RegistrationCrewRequestBean item = crewList.get(i);
				System.out.println("[crewResultMeal]   항목[" + i + "] UID: " + item.getUid() + ", 계획 건수: " + (item.getPlanList() != null ? item.getPlanList().size() : 0));
			}
		}
		RegistrationCrewQtyBean allResultQtyForInclude = new RegistrationCrewQtyBean();
		allResultQtyForInclude.setProjNo(bean.getShip() != null && !bean.getShip().isEmpty() && !bean.getShip().equals("ALL") ? bean.getShip() : null);
		allResultQtyForInclude.setInDate(bean.getInDate());
		allResultQtyForInclude.setOutDate(bean.getOutDate());
		List<RegistrationCrewQtyBean> allResultListForInclude = crewDao.getCrewResultQtyList(allResultQtyForInclude);
		System.out.println("[crewResultMeal] ensureResultOnlyDepartmentsIncluded 호출 전 - crewList 건수: " + (crewList != null ? crewList.size() : 0));
		crewList = ensureResultOnlyDepartmentsIncluded_Crew(crewList, bean, allResultListForInclude);
		System.out.println("[crewResultMeal] ensureResultOnlyDepartmentsIncluded 호출 후 - crewList 건수: " + (crewList != null ? crewList.size() : 0));
		// ensureResultOnlyDepartmentsIncluded 호출 후 계획 데이터 확인
		if(crewList != null) {
			System.out.println("[crewResultMeal] ensureResultOnlyDepartmentsIncluded 호출 후 계획 데이터 확인:");
			for(int i = 0; i < crewList.size(); i++) {
				RegistrationCrewRequestBean item = crewList.get(i);
				System.out.println("[crewResultMeal]   항목[" + i + "] UID: " + item.getUid() + ", 계획 건수: " + (item.getPlanList() != null ? item.getPlanList().size() : 0));
			}
		}

		//resultMap.put(Const.BEAN, dao.getScheduler(bean.getUid()));
		resultMap.put(Const.LIST, crewList);
		//resultMap.put("status", dao.getTrialStatus(bean.getUid()));
		
		
		System.out.println("[crewResultMeal] 최종 결과 - anchList 건수: " + (crewList != null ? crewList.size() : 0));
		System.out.println("========== crewResultMeal() 함수 종료 ==========");
		return resultMap;
	}
	
	private List<RegistrationCrewRequestBean> ensureResultOnlyDepartmentsIncluded_Crew(List<RegistrationCrewRequestBean> crewList, RegistrationCrewRequestBean filterBean, List<RegistrationCrewQtyBean> allResultList) throws Exception {
		if (crewList == null) {
			crewList = new ArrayList<>();
		}
		
		Map<String, RegistrationCrewRequestBean> crewMap = new LinkedHashMap<>();
		for (RegistrationCrewRequestBean item : crewList) {
			String key = buildMealDepartmentKey(item.getProjNo(), item.getDepartment(), item.getMealDate());
			crewMap.put(key, item);
		}
		
		List<RegistrationCrewRequestBean> resultCombos = crewDao.getCrewResultDeptCombinations(filterBean);
		if (resultCombos != null) {
			for (RegistrationCrewRequestBean combo : resultCombos) {
				String key = buildMealDepartmentKey(combo.getProjNo(), combo.getDepartment(), combo.getMealDate());
				RegistrationCrewRequestBean existing = crewMap.get(key);
				if (existing == null) {
					RegistrationCrewRequestBean newBean = new RegistrationCrewRequestBean();
					newBean.setProjNo(combo.getProjNo());
					newBean.setDepartment(combo.getDepartment());
					newBean.setMealDate(combo.getMealDate());
					newBean.setPlanList(new ArrayList<RegistrationCrewQtyBean>());
					
					// 전체 실적 리스트에서 필터링
					List<RegistrationCrewQtyBean> filteredResultListForNew = new ArrayList<RegistrationCrewQtyBean>();
					if(allResultList != null) {
						for(RegistrationCrewQtyBean result : allResultList) {
							boolean deptMatch = (combo.getDepartment() == null && result.getDepartment() == null) ||
											   (combo.getDepartment() != null && combo.getDepartment().equals(result.getDepartment()));
							String comboDate = combo.getMealDate() != null ? combo.getMealDate().substring(0, Math.min(10, combo.getMealDate().length())) : null;
							String resultDate = result.getResultMealDate() != null && result.getResultMealDate().length() >= 10 ? result.getResultMealDate().substring(0, 10) : result.getResultMealDate();
							boolean dateMatch = (comboDate == null && resultDate == null) ||
											   (comboDate != null && resultDate != null && comboDate.equals(resultDate));
							boolean projMatch = (combo.getProjNo() == null && result.getProjNo() == null) ||
											   (combo.getProjNo() != null && combo.getProjNo().equals(result.getProjNo()));
							
							if(deptMatch && dateMatch && projMatch) {
								filteredResultListForNew.add(result);
							}
						}
					}
					newBean.setResultList(filteredResultListForNew);
					
					crewList.add(newBean);
					crewMap.put(key, newBean);
				} else if (existing.getResultList() == null || existing.getResultList().isEmpty()) {
					// 전체 실적 리스트에서 필터링
					List<RegistrationCrewQtyBean> filteredResultListForExisting = new ArrayList<RegistrationCrewQtyBean>();
					if(allResultList != null) {
						for(RegistrationCrewQtyBean result : allResultList) {
							boolean deptMatch = (combo.getDepartment() == null && result.getDepartment() == null) ||
											   (combo.getDepartment() != null && combo.getDepartment().equals(result.getDepartment()));
							String comboDate = combo.getMealDate() != null ? combo.getMealDate().substring(0, Math.min(10, combo.getMealDate().length())) : null;
							String resultDate = result.getResultMealDate() != null && result.getResultMealDate().length() >= 10 ? result.getResultMealDate().substring(0, 10) : result.getResultMealDate();
							boolean dateMatch = (comboDate == null && resultDate == null) ||
											   (comboDate != null && resultDate != null && comboDate.equals(resultDate));
							boolean projMatch = (combo.getProjNo() == null && result.getProjNo() == null) ||
											   (combo.getProjNo() != null && combo.getProjNo().equals(result.getProjNo()));
							
							if(deptMatch && dateMatch && projMatch) {
								filteredResultListForExisting.add(result);
							}
						}
					}
					existing.setResultList(filteredResultListForExisting);
				}
			}
		}
		return crewList;
	}
	
	private String buildMealDepartmentKey(String projNo, String department, String mealDate) {
		String safeProjNo = projNo == null ? "" : projNo.trim();
		String safeDept = department == null ? "" : department.trim();
		String safeDate = mealDate == null ? "" : mealDate.trim();
		return safeProjNo + "|" + safeDept + "|" + safeDate;
	}
	
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
	

