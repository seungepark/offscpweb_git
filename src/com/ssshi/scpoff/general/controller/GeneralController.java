package com.ssshi.scpoff.general.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ssshi.scpoff.dto.ParamBean;
import com.ssshi.scpoff.general.service.GeneralServiceI;

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

@Controller
public class GeneralController {

	@Autowired
	private GeneralServiceI service;
	
	/** 기본 화면 분기 */
	@RequestMapping(value="/index.html")
	public String index(HttpServletRequest request) throws Exception {
		if(service.isLogined(request)) {
			return "redirect:/sche/off.html";
		}else {
			return "general/login";
		}
	}
	
	/** 로그인 체크 */
	@RequestMapping(value="/login.html", method=RequestMethod.POST)
	public String login(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.login(request, bean));
		
		return "general/loginReq";
	}
	
	/** 로그아웃 */
	@RequestMapping(value="/logout.html")
	public String logout(HttpServletRequest request) throws Exception {
		request.getSession().invalidate();
		
		return "redirect:/index.html";
	}
}