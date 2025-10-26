package com.ssshi.scpoff.general.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ssshi.scpoff.dto.ParamBean;

public interface GeneralServiceI {
	
	boolean isLogined(HttpServletRequest request) throws Exception;
	
	Map<String, Object> login(HttpServletRequest request, ParamBean bean) throws Exception;
}
