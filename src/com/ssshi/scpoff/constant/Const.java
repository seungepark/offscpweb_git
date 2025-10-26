package com.ssshi.scpoff.constant;

/********************************************************************************
 * 프로그램 개요 : Constant
 * 
 * 최초 작성자 : KHJ
 * 최초 작성일 : 2024-07-17
 * 
 * 최종 수정자 : KHJ
 * 최종 수정일 : 2024-08-26
 * 
 * 메모 : 없음
 * 
 * Copyright 2024 by SiriusB. Confidential and proprietary information
 * This document contains information, which is the property of SiriusB, 
 * and is furnished for the sole purpose of the operation and the maintenance.  
 * Copyright © 2024 SiriusB.  All rights reserved.
 *
 ********************************************************************************/

public interface Const {
	
	String API_SCP_URL_ROOT = "http://127.0.0.1:8080/scp";
	String DB_SCP_URL = "jdbc:mysql://127.0.0.1:3306/scp";
	String DB_SCP_USER = "scp";
	String DB_SCP_PW = "shi1234";
	
	String RESULT = "result";				// 단순 결과 attribute
	String LIST = "list";					// 목록형 attribute
	String BEAN = "bean";					// 객체형 attribute
	String LIST_CNT = "listCnt";			// 목록 총 개수 attribute
	String ERRCODE = "errCode";				// 에러코드 attribute
	String ISERR = "isErr";					// 에러 attribute
	String UID = "uid";						// uid attribute
	
	String OK = "OK";
	String FAIL = "FAIL";
	String ERRCODE_INVALID = "INVALID";
	String ERRCODE_CANT_CHANGE = "ECC";
	String ERRCODE_EXIST_USERID = "EEU";
	String ERRCODE_EXIST_SHIP = "EES";
	String ERRCODE_EXIST_CRON = "EEC";
	String ERRCODE_CANT_DELETE = "ECD";
	String ERRCODE_REQUIRED_ENTRY = "ERE";
	String ERRCODE_NOT_INPRG = "ENI";
	String ERRCODE_IS_COMP = "EIC";
	String ERRCODE_IS_REVISED = "EIR";
	String ERRCODE_NO_IMO = "ECI";
	String ERRCODE_NO_WORKER = "ECW";
	String ERRCODE_EXIST_MAP = "EEM";
	String ERRCODE_NO_TEMP = "ENT";
	String ERRCODE_CANT_DELETE_PART = "ECDP";
	String ERRCODE_CANT_DELETE_ALL = "ECDA";
	String ERRCODE_CANT_DELETE_DOMAIN = "ECDD";
	String ERRCODE_EXIST_SCHED = "EESD";
	String ERRCODE_EMPTY_INFO = "EEI";
	String ERRCODE_EXIST_REPORT = "EER";
	String ERRCODE_ONGOTIME_OVER = "EOO";
	String ERRCODE_FIRST_UPLOAD = "EFU";
	
	String SS_USERINFO = "userInfo";				// 세션 : 사용자 정보
}
