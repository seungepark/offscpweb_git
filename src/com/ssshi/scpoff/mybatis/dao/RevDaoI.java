package com.ssshi.scpoff.mybatis.dao;

import com.ssshi.scpoff.dto.ScheCrewBean;
import com.ssshi.scpoff.dto.ScheCrewInOutBean;
import com.ssshi.scpoff.dto.ScheDateTimeBean;
import com.ssshi.scpoff.dto.ScheTcNoteBean;
import com.ssshi.scpoff.dto.ScheTcNoteFileInfoBean;
import com.ssshi.scpoff.dto.ScheTcNoteTcInfoBean;
import com.ssshi.scpoff.dto.ScheTrialInfoBean;
import com.ssshi.scpoff.dto.SchedulerDetailInfoBean;
import com.ssshi.scpoff.dto.SchedulerInfoBean;
import com.ssshi.scpoff.dto.SchedulerVersionInfoBean;

/********************************************************************************
 * 프로그램 개요 : Revision
 * 
 * 최초 작성자 : KHJ
 * 최초 작성일 : 2025-02-26
 * 
 * 최종 수정자 : KHJ
 * 최종 수정일 : 2025-02-26
 * 
 * 메모 : 없음
 * 
 * Copyright 2025 by SiriusB. Confidential and proprietary information
 * This document contains information, which is the property of SiriusB, 
 * and is furnished for the sole purpose of the operation and the maintenance.  
 * Copyright © 2025 SiriusB.  All rights reserved.
 *
 ********************************************************************************/

public interface RevDaoI {
	
	int insertSchedulerInfo(SchedulerInfoBean bean) throws Exception;
	
	int insertSchedulerVersionInfo(SchedulerVersionInfoBean bean) throws Exception;
	
	int insertSchedulerDetail(SchedulerDetailInfoBean bean) throws Exception;
	
	int insertScheDatetime(ScheDateTimeBean bean) throws Exception;
	
	int insertScheTrialInfo(ScheTrialInfoBean bean) throws Exception;
	
	int insertScheCrew(ScheCrewBean bean) throws Exception;
	
	int insertScheCrewInOut(ScheCrewInOutBean bean) throws Exception;
	
	int insertScheTcNote(ScheTcNoteBean bean) throws Exception;
	
	int insertScheTcNoteFileInfo(ScheTcNoteFileInfoBean bean) throws Exception;
	
	int insertScheTcNoteTcInfo(ScheTcNoteTcInfoBean bean) throws Exception;
	
	int deleteScheTcNoteTcInfo(int schedulerInfoUid) throws Exception;
	
	int deleteScheTcNoteFileInfo(int schedulerInfoUid) throws Exception;
	
	int deleteScheTcNote(int schedulerInfoUid) throws Exception;
	
	int deleteScheCrewInOut(int schedulerInfoUid) throws Exception;
	
	int deleteScheCrew(int schedulerInfoUid) throws Exception;
	
	int deleteScheTrialInfo(int schedulerInfoUid) throws Exception;
	
	int deleteScheDatetime(int schedulerInfoUid) throws Exception;
	
	int deleteSchedulerDetail(int schedulerInfoUid) throws Exception;
	
	int deleteSchedulerVersionInfo(int schedulerInfoUid) throws Exception;
	
	int deleteSchedulerInfo(int uid) throws Exception;
}
