package com.ssshi.scpoff.aop;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.ssshi.scpoff.constant.Const;

/********************************************************************************
 * 프로그램 개요 : AOP
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

@Aspect
public class CommonAop {
	
	static Logger logger = Logger.getLogger(CommonAop.class);
	
	public CommonAop() {
		try {
			String file = "C:/FILEINFO_SCP/Log/System.out.error.log";
			String datePattern = "'.'yy-MM-dd'.'HH";
			String conversionPattern = "[%d{yy/MM/dd HH:mm:ss.SSS}] %-5p %c{2}(%13F:%L) [%t] %3x - %m%n";
			
			PatternLayout layout = new PatternLayout(conversionPattern);
			ConsoleAppender consoleApd = new ConsoleAppender(layout);
//			DailyRollingFileAppender fileApd = new DailyRollingFileAppender(layout, file, datePattern);
		
//			fileApd.setThreshold(Level.ERROR);
//			fileApd.setAppend(true);
			
			logger.addAppender(consoleApd);
//			logger.addAppender(fileApd);
			logger.setLevel(Level.ERROR);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * throwingLogging
	 * @param joinPoint : 
	 * @param ex		: 
	 */
	@AfterThrowing(pointcut="execution(* com.ssshi.scpoff.*..*.*(..))", throwing="ex")
	public void throwingLogging(JoinPoint joinPoint, Throwable ex) {
		StringBuffer msg = new StringBuffer();
		
		msg.append("\n================= [Exception Info] ==================");
		msg.append("\nMethod Name : " + joinPoint.getSignature().getName());
		msg.append("\nMessage : -------------------------------------------");
		
		for(int i = 0; i < ex.getStackTrace().length; i++) {
			msg.append("\n" + ex.getStackTrace()[i].toString());
		}
		
		msg.append("\n-----------------------------------------------------");
		
		logger.error(msg.toString());
	}
	
	/**
	 * requestCheck
	 * @param joinPoint  : 
	 * @return modelAndView : 
	 * @throws Throwable
	 */
	@Around("within(@org.springframework.stereotype.Controller *) && execution(* *(..)) && !@annotation(com.ssshi.scpoff.util.NoAop)")
	public Object requestCheck(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
		String path = request.getServletPath();
		Object mav = "redirect:/index.html";

		if("/index.html".equals(path) || "/login.html".equals(path) || "/logout.html".equals(path)) {
			mav = (String) joinPoint.proceed();
		}else {
			if((request.getSession().getAttribute(Const.SS_USERINFO)) != null) {
				mav = (String) joinPoint.proceed();
			}
		}
		
		return mav;
	}
}
