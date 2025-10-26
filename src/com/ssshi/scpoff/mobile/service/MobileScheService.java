package com.ssshi.scpoff.mobile.service;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssshi.scpoff.constant.Const;
import com.ssshi.scpoff.constant.DBConst;
import com.ssshi.scpoff.dto.DomainBean;
import com.ssshi.scpoff.dto.DomainInfoBean;
import com.ssshi.scpoff.dto.ParamBean;
import com.ssshi.scpoff.dto.ScheCrewBean;
import com.ssshi.scpoff.dto.ScheCrewCntBean;
import com.ssshi.scpoff.dto.ScheCrewInOutBean;
import com.ssshi.scpoff.dto.ScheCrewListBean;
import com.ssshi.scpoff.dto.ScheDateTimeBean;
import com.ssshi.scpoff.dto.ScheMailBean;
import com.ssshi.scpoff.dto.ScheMailLogBean;
import com.ssshi.scpoff.dto.ScheReportCompBean;
import com.ssshi.scpoff.dto.ScheReportDailyBean;
import com.ssshi.scpoff.dto.ScheReportDepartureBean;
import com.ssshi.scpoff.dto.ScheTcNoteBean;
import com.ssshi.scpoff.dto.ScheTcNoteFileInfoBean;
import com.ssshi.scpoff.dto.ScheTcNoteTcInfoBean;
import com.ssshi.scpoff.dto.ScheTrialInfoBean;
import com.ssshi.scpoff.dto.ScheduleBean;
import com.ssshi.scpoff.dto.ScheduleCodeDetailBean;
import com.ssshi.scpoff.dto.ScheduleCodeInfoBean;
import com.ssshi.scpoff.dto.ScheduleHierarchyBean;
import com.ssshi.scpoff.dto.SchedulerDetailInfoBean;
import com.ssshi.scpoff.dto.SchedulerInfoBean;
import com.ssshi.scpoff.dto.SchedulerVersionInfoBean;
import com.ssshi.scpoff.dto.ShipCondBean;
import com.ssshi.scpoff.dto.ShipInfoBean;
import com.ssshi.scpoff.dto.UserInfoBean;
import com.ssshi.scpoff.dto.VesselReqInfoBean;
import com.ssshi.scpoff.dto.VesselReqInfoDetailBean;
import com.ssshi.scpoff.mobile.dao.MobileScheDao;
import com.ssshi.scpoff.mobile.dao.MobileScheDaoI;
import com.ssshi.scpoff.mobile.dto.MobileScheCrewBean;
import com.ssshi.scpoff.mybatis.dao.RevDao;
import com.ssshi.scpoff.mybatis.dao.RevDaoI;
import com.ssshi.scpoff.mybatis.dao.ScheDao;
import com.ssshi.scpoff.mybatis.dao.ScheDaoI;
import com.ssshi.scpoff.util.CommonUtil;
import com.ssshi.scpoff.util.ExcelUtil;
import com.ssshi.scpoff.util.FileUtil;
import com.ssshi.scpoff.util.SmtpUtil;
import com.ssshi.scpoff.util.ValidUtil;

/********************************************************************************
 * 프로그램 개요 : Mobile 데이터
 * 
 * 최초 작성자 : 피크닉 최초 작성일 : 2025-06-23
 * 
 * 최종 수정자 : 피크닉 최종 수정일 : 2025-06-23
 * 
 * 메모 : 없음
 *
 ********************************************************************************/

@Service
public class MobileScheService implements MobileScheServiceI {

	@Autowired
	private MobileScheDaoI Mobiledao;

	@Override
	public Map<String, Object> getschecrewList(String proj) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<MobileScheCrewBean> data = Mobiledao.getschecrewList(null);

		resultMap.put(Const.LIST + "Schecrew", data);
	 

		return resultMap;
	}
 
}
