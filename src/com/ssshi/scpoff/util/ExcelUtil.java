package com.ssshi.scpoff.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;

/********************************************************************************
 * 프로그램 개요 : Excel Util
 * 
 * 최초 작성자 : KHJ
 * 최초 작성일 : 2024-04-01
 * 
 * 최종 수정자 : KHJ
 * 최종 수정일 : 2024-04-18
 * 
 * 메모 : 없음
 * 
 * Copyright 2024 by SiriusB. Confidential and proprietary information
 * This document contains information, which is the property of SiriusB, 
 * and is furnished for the sole purpose of the operation and the maintenance.  
 * Copyright © 2024 SiriusB.  All rights reserved.
 *
 ********************************************************************************/

public class ExcelUtil {

	public static void createSelectBox(Sheet sheet, Cell cell, String[] vals) {
		// 데이터 유효성 검사 설정.
		DataValidationHelper validationHelper = sheet.getDataValidationHelper();
		DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(vals);
		
		// 셀에 데이터 유효성 검사 적용.
		DataValidation validation = validationHelper.createValidation(constraint, new CellRangeAddressList(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex()));
		validation.setSuppressDropDownArrow(true);
		validation.setShowErrorBox(true);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		
		sheet.addValidationData(validation);
	}
}
