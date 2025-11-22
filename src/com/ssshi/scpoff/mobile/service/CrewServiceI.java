package com.ssshi.scpoff.mobile.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.ssshi.scpoff.dto.ParamBean;
 
public interface CrewServiceI {
	
	Map<String, Object> onlineMobileDownload(HttpServletRequest request, ParamBean bean) throws Exception;
	
	Map<String, Object> offlineMobileUpload(HttpServletRequest request, ParamBean bean) throws Exception;
}
