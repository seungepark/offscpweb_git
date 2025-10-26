package com.ssshi.scpoff.general.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssshi.scpoff.constant.Const;
import com.ssshi.scpoff.constant.DBConst;
import com.ssshi.scpoff.dto.ParamBean;
import com.ssshi.scpoff.dto.UserInfoBean;
import com.ssshi.scpoff.mybatis.dao.GeneralDao;
import com.ssshi.scpoff.mybatis.dao.GeneralDaoI;

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

@Service
public class GeneralService implements GeneralServiceI {

	@Autowired
	private GeneralDaoI dao;

	@Override
	public boolean isLogined(HttpServletRequest request) throws Exception {
		if(((UserInfoBean) request.getSession().getAttribute(Const.SS_USERINFO)) != null) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public Map<String, Object> login(HttpServletRequest request, ParamBean bean) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 온라인 로그인 시도.
		Map<String, Object> online = GeneralDao.login(bean);
		UserInfoBean user = (UserInfoBean) online.get(Const.BEAN);
		boolean isError = (boolean) online.get(Const.ERRCODE);
		
		if(isError) {				// 오프라인 상태.
			user = dao.login(bean);		// 오프라인 로그인 시도.
		}else {						// 온라인 상태.
			if(user != null) {			// 온라인 로그인 됨.
				// 온라인 로그인 사용자 정보 가져와서 오프라인 DB에 갱신.
				dao.deleteUser(user.getUserId());
				dao.insertUser(user);
			}
		}
		
		if(user != null) {
			HttpSession session = request.getSession();
			session.setAttribute(Const.SS_USERINFO, user);
			
			resultMap.put(Const.RESULT, DBConst.SUCCESS);
			resultMap.put(Const.ERRCODE, Const.OK);
		}else {
			resultMap.put(Const.RESULT, DBConst.FAIL);
			resultMap.put(Const.ERRCODE, Const.ERRCODE_INVALID);
		}
		
		return resultMap;
	}
}