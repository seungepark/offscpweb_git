package com.ssshi.scpoff.mybatis.dao;

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

public interface GeneralDaoI {
	
	UserInfoBean login(ParamBean bean) throws Exception;
	
	int deleteUser(String userId) throws Exception;
	
	int insertUser(UserInfoBean bean) throws Exception;
}
