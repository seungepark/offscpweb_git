package com.ssshi.scpoff.mobile.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.ssshi.scpoff.mobile.dto.MobileScheCrewBean;
 
/********************************************************************************
 * 프로그램 개요 : Mobile 데이터
 * 
 * 최초 작성자 : 피크닉
 * 최초 작성일 : 2025-06-23
 * 
 * 최종 수정자 : 피크닉
 * 최종 수정일 : 2025-06-23
 * 
 * 메모 : 없음
 *
 ********************************************************************************/
public interface MobileScheDaoI {
	
	// --- DbDaoI ---
	List<MobileScheCrewBean> getschecrewList(String proj) throws Exception;
	 
	 
}
