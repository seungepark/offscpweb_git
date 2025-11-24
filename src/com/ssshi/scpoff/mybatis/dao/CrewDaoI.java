package com.ssshi.scpoff.mybatis.dao;

import java.util.List;
import java.util.Map;

import com.ssshi.scpoff.dto.RegistrationCrewRequestBean;
import com.ssshi.scpoff.dto.DomainInfoBean;
import com.ssshi.scpoff.dto.RegistrationCrewQtyBean;

/********************************************************************************
 * 프로그램 개요 : Crew
 * 
 * 최초 작성자 : 
 * 최초 작성일 : 
 * 
 * 최종 수정자 : 
 * 최종 수정일 : 
 * 
 * 메모 : 없음
 * 
 * Copyright 2025 by SiriusB. Confidential and proprietary information
 * This document contains information, which is the property of SiriusB, 
 * and is furnished for the sole purpose of the operation and the maintenance.  
 * Copyright © 2025 SiriusB.  All rights reserved.
 *
 ********************************************************************************/

public interface CrewDaoI {
	// 호선 목록(시운전).
	List<DomainInfoBean> getShipList() throws Exception;

	// 시운전 실적집계리스트
	List<RegistrationCrewRequestBean> getCrewMealResultList(RegistrationCrewRequestBean bean) throws Exception;
	
	// 시운전 식사신청 수량 (계획).
	List<RegistrationCrewQtyBean> getCrewPlanQtyList(int crewUid) throws Exception;
	
	// 시운전 식사신청 수량 (실적).
	List<RegistrationCrewQtyBean> getCrewResultQtyList(RegistrationCrewQtyBean bean) throws Exception;
	
	// 시운전 실적 목록 개수.
	int getCrewMealListCnt() throws Exception;
	
	// 실적에 존재하는 부서 조합 목록(시운전)
	List<RegistrationCrewRequestBean> getCrewResultDeptCombinations(RegistrationCrewRequestBean bean) throws Exception;
}
