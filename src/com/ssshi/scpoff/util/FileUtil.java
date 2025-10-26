package com.ssshi.scpoff.util;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

/********************************************************************************
 * 프로그램 개요 : File Util
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

public class FileUtil {
	
	public static final String FILE_DIR_ROOT = "C:\\FILEINFO_SCP";
	public static final String FILE_DIR_FOR_NOTE = "\\NOTE";
	
	public static final String NOTE_FILE = "NOTE";
	public static final String DEFAULT_FILE_NAME = "img.png";

	public static String getSaveFileName(String kind) {
		return System.currentTimeMillis() + "_" + ((int)(Math.random() * 100) + 1) + "_" + kind;
	}
	
	public static String getFileType(String name) {
		String exe = null;
		String type = null;
		
		if(name == null || name.length() <= 4) {
			exe = FilenameUtils.getExtension(DEFAULT_FILE_NAME);
		}else {
			exe = FilenameUtils.getExtension(name);
		}
		
		if("png".equals(exe.toLowerCase())) {
			type = "PNG";
		}else if("jpg".equals(exe.toLowerCase()) || "jpeg".equals(exe.toLowerCase())) {
			type = "JPG";
		}else if("pdf".equals(exe.toLowerCase())) {
			type = "PDF";
		}else {
			type = exe;
		}
		
		return type;
	}
	
	public static boolean deleteFile(String path) {
		File file = new File(FILE_DIR_ROOT + path);
		
		if(file.exists()) {
			return file.delete();
		}else {
			return false;
		}
	}
}
