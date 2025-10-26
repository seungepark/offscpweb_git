package com.ssshi.scpoff.sche.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ssshi.scpoff.constant.Const;
import com.ssshi.scpoff.constant.DBConst;
import com.ssshi.scpoff.dto.ParamBean;
import com.ssshi.scpoff.dto.ScheCrewListBean;
import com.ssshi.scpoff.dto.ScheMailLogBean;
import com.ssshi.scpoff.dto.ScheTcNoteBean;
import com.ssshi.scpoff.dto.ScheTrialInfoBean;
import com.ssshi.scpoff.dto.ScheduleCodeDetailBean;
import com.ssshi.scpoff.dto.SchedulerDetailInfoBean;
import com.ssshi.scpoff.dto.SchedulerInfoBean;
import com.ssshi.scpoff.sche.service.ScheServiceI;

/********************************************************************************
 * 프로그램 개요 : Sche
 * 
 * 최초 작성자 : KHJ
 * 최초 작성일 : 2024-07-17
 * 
 * 최종 수정자 : KHJ
 * 최종 수정일 : 2025-05-12
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
public class ScheController {

	@Autowired
	private ScheServiceI service;
	
	@RequestMapping(value="/sche/on.html")
	public String onlineList(HttpServletRequest request, ModelMap model) throws Exception {
		model.addAllAttributes(service.onlineList());

		return "sche/onlineList";
	}
	
	@RequestMapping(value="/sche/getScheListForOnline.html")
	public String getScheListForOnline(HttpServletRequest request, ModelMap model, SchedulerInfoBean bean) throws Exception {
		model.addAllAttributes(service.getScheListForOnline(bean));
		
		return "sche/getScheListForOnline";
	}
	
	@RequestMapping(value="/sche/on/download.html")
	public String onlineDownload(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.onlineDownload(request, bean));
		
		return "sche/onlineDownload";
	}
	
	@RequestMapping(value="/sche/off.html")
	public String offList(HttpServletRequest request, ModelMap model) throws Exception {
		model.addAllAttributes(service.offList());
		
		return "sche/offList";
	}
	
	@RequestMapping(value="/sche/getScheList.html")
	public String getScheList(HttpServletRequest request, ModelMap model, SchedulerInfoBean bean) throws Exception {
		model.addAllAttributes(service.getScheList(bean));
		
		return "sche/getScheList";
	}
	
	@RequestMapping(value="/sche/scheChart.html")
	public String scheChart(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.scheChart(request, bean));
		String status = (String) model.get("status");
		
		if(DBConst.SCHE_TRIAL_INFO_TRIALSTATUS_ONGOING.equals(status) || DBConst.SCHE_TRIAL_INFO_TRIALSTATUS_ARRIVAL.equals(status)) {
			return "forward:/sche/resultChart.html";
		}else {
			return "forward:/sche/planChart.html";
		}
	}
	
	@RequestMapping(value="/sche/planChart.html")
	public String planChart(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.planChart(request, bean));
		
		return "sche/planChart";
	}
	
	@RequestMapping(value="/sche/planChartSave.html", method=RequestMethod.POST)
	public String planChartSave(HttpServletRequest request, ModelMap model, SchedulerInfoBean bean) throws Exception{
		model.addAllAttributes(service.planChartSave(request, bean));
		
		return "share/resultCode";
	}
	
	@RequestMapping(value="/sche/planCrew.html")
	public String planCrew(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.planCrew(request, bean));
		
		return "sche/planCrew";
	}
	
	@RequestMapping(value="/sche/downCrewExcel.html")
	public void downCrewExcel(HttpServletRequest request, HttpServletResponse response, ParamBean bean) throws Exception {
		service.downCrewExcel(response, bean);
	}
	
	@RequestMapping(value="/sche/planCrewSave.html", method=RequestMethod.POST)
	public String planCrewSave(HttpServletRequest request, ModelMap model, ScheCrewListBean bean) throws Exception {
		model.addAllAttributes(service.planCrewSave(request, bean));
		
		return "share/resultCode";
	}
	
	@RequestMapping(value="/sche/planInfo.html")
	public String planInfo(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.planInfo(request, bean));
		
		return "sche/planInfo";
	}
	
	@RequestMapping(value="/sche/searchTrial.html", method=RequestMethod.POST)
	public String searchTrial(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAttribute(Const.LIST, service.searchTrial(bean));
		
		return "sche/searchTrial";
	}
	
	@RequestMapping(value="/sche/planInfoSave.html", method=RequestMethod.POST)
	public String planInfoSave(HttpServletRequest request, ModelMap model, ScheTrialInfoBean bean) throws Exception {
		model.addAllAttributes(service.planInfoSave(request, bean));
		
		return "share/resultCode";
	}
	
	@RequestMapping(value="/sche/planDepartureReport.html")
	public String planDepartureReport(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.planDepartureReport(request, bean));
		
		return "sche/planDepartureReport";
	}
	
	@RequestMapping(value="/sche/planDepartureReportSubmit.html", method=RequestMethod.POST)
	public String planDepartureReportSubmit(HttpServletRequest request, ModelMap model, ScheMailLogBean bean) throws Exception {
		model.addAllAttributes(service.planDepartureReportSubmit(request, bean));
		
		return "sche/planDepartureReportSubmit";
	}
	
	@RequestMapping(value="/sche/resultChart.html")
	public String resultChart(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.resultChart(request, bean));
		
		return "sche/resultChart";
	}
	
	@RequestMapping(value="/sche/resultChartSave.html", method=RequestMethod.POST)
	public String resultChartSave(HttpServletRequest request, ModelMap model, SchedulerInfoBean bean) throws Exception{
		model.addAllAttributes(service.resultChartSave(request, bean));
		
		return "share/resultCode";
	}
	
	@RequestMapping(value="/sche/getScheRowList.html")
	public String getScheRowList(HttpServletRequest request, ModelMap model, SchedulerDetailInfoBean bean) throws Exception{
		model.addAllAttributes(service.getScheRowList(bean));
		
		return "sche/getScheRowList";
	}
	
	@RequestMapping(value="/sche/getTcNoteList.html", method=RequestMethod.POST)
	public String getTcNoteList(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAttribute(Const.LIST, service.getTcNoteList(request, bean));
		
		return "sche/getTcNoteList";
	}
	
	@RequestMapping(value="/sche/saveNote.html", method=RequestMethod.POST)
	public String saveNote(HttpServletRequest request, ModelMap model, ScheTcNoteBean bean) throws Exception {
		model.addAllAttributes(service.saveNote(request, bean));
		
		return "share/result";
	}
	
	@RequestMapping(value="/sche/resultCrew.html")
	public String resultCrew(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.resultCrew(request, bean));
		
		return "sche/resultCrew";
	}
	
	@RequestMapping(value="/sche/resultCrewSave.html", method=RequestMethod.POST)
	public String resultCrewSave(HttpServletRequest request, ModelMap model, ScheCrewListBean bean) throws Exception {
		model.addAllAttributes(service.resultCrewSave(request, bean));
		
		return "share/result";
	}
	
	@RequestMapping(value="/sche/resultInfo.html")
	public String resultInfo(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.resultInfo(request, bean));
		
		return "sche/resultInfo";
	}
	
	@RequestMapping(value="/sche/resultInfoSave.html", method=RequestMethod.POST)
	public String resultInfoSave(HttpServletRequest request, ModelMap model, ScheTrialInfoBean bean) throws Exception {
		model.addAllAttributes(service.resultInfoSave(request, bean));
		
		return "share/result";
	}
	
	@RequestMapping(value="/sche/resultDailyReport.html")
	public String resultDailyReport(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.resultDailyReport(request, bean));
		
		return "sche/resultDailyReport";
	}
	
	@RequestMapping(value="/sche/resultDailyReportSubmit.html", method=RequestMethod.POST)
	public String resultDailyReportSubmit(HttpServletRequest request, ModelMap model, ScheMailLogBean bean) throws Exception {
		model.addAllAttributes(service.resultDailyReportSubmit(request, bean));
		
		return "share/resultCode";
	}
	
	@RequestMapping(value="/sche/resultCompReport.html")
	public String resultCompReport(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.resultCompReport(request, bean));
		
		return "sche/resultCompReport";
	}
	
	@RequestMapping(value="/sche/resultCompReportSubmit.html", method=RequestMethod.POST)
	public String resultCompReportSubmit(HttpServletRequest request, ModelMap model, ScheMailLogBean bean) throws Exception {
		model.addAllAttributes(service.resultCompReportSubmit(request, bean));
		
		return "share/resultCode";
	}
	
	@RequestMapping(value="/sche/searchEmail.html", method=RequestMethod.POST)
	public String searchEmail(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.searchEmail(bean));
		
		return "sche/searchEmail";
	}
	
	@RequestMapping(value="/sche/off/upload.html", method=RequestMethod.POST)
	public void upload(HttpServletRequest request, HttpServletResponse response, ModelMap model, ParamBean bean) throws Exception {
		service.upload(request, response, bean);
	}
	
	@RequestMapping(value="/sche/off/changeOn.html", method=RequestMethod.POST)
	public String changeOn(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.changeOn(request, bean));
		
		return "sche/changeOn";
	}
	
	@RequestMapping(value="/sche/getScheTcNumSearchList.html")
	public String getScheduleTcNumSearchList(HttpServletRequest request, ModelMap model, ScheduleCodeDetailBean bean) throws Exception{
		model.addAllAttributes(service.getScheduleTcNumSearchList(request, bean));
		
		return "sche/getScheduleCodeDetList";
	}
	
	@RequestMapping(value="/sche/planReportSchedule.html")
	public String planReportSchedule(HttpServletRequest request, ModelMap model) throws Exception {
		model.addAllAttributes(service.reportSchedule(request));
		
		return "sche/planReportSchedule";
	}
	
	@RequestMapping(value="/sche/resultReportSchedule.html")
	public String resultReportSchedule(HttpServletRequest request, ModelMap model) throws Exception {
		model.addAllAttributes( service.reportSchedule(request));
		
		return "sche/resultReportSchedule";
	}
	
	@RequestMapping(value="/sche/getVesselReqInfoDetListByHullNum.html")
	public String getVesselReqInfoDetListByHullNum(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAllAttributes(service.getVesselReqInfoDetListByHullNum(bean));

		return "sche/getVesselReqInfoDetList";
	}
	
	@RequestMapping(value="/sche/getShipCondList.html")
	public String getShipCondList(HttpServletRequest request, ModelMap model, ParamBean bean) throws Exception {
		model.addAttribute(Const.LIST, service.getShipCondList(bean));

		return "sche/getShipCondList";
	}
}