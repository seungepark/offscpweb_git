package com.ssshi.scpoff.mobile.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;
import com.ssshi.scpoff.constant.Const;
import com.ssshi.scpoff.dto.DomainBean;
import com.ssshi.scpoff.dto.DomainInfoBean; 
import com.ssshi.scpoff.dto.ParamBean;
import com.ssshi.scpoff.dto.ScheCrewBean;
import com.ssshi.scpoff.dto.ScheCrewInOutBean;
import com.ssshi.scpoff.dto.ScheMailBean;
import com.ssshi.scpoff.dto.ScheReportCompBean;
import com.ssshi.scpoff.dto.ScheReportDailyBean;
import com.ssshi.scpoff.dto.ScheReportDepartureBean;
import com.ssshi.scpoff.dto.ScheTcNoteBean;
import com.ssshi.scpoff.dto.ScheTcNoteFileInfoBean;
import com.ssshi.scpoff.dto.ScheTcNoteTcInfoBean;
import com.ssshi.scpoff.dto.ScheTrialInfoBean;
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
import com.ssshi.scpoff.mobile.dto.MobileScheCrewBean;
import com.ssshi.scpoff.util.ValidUtil;

/********************************************************************************
 * 프로그램 개요 : Mobile 데이터
 * 
 * 최초 작성자 : 피크닉
 * 최초 작성일 : 2025-06-23
 * 
 * 최종 수정자 : 피크닉
 * 최종 수정일 : 2025-06-23
 * 
 * 메모 : 없음
 *
 ********************************************************************************/

public class MobileScheDao {

	public static Map<String, Object> getschecrewList(String proj) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<MobileScheCrewBean> list = new ArrayList<MobileScheCrewBean>();
		boolean isError = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		System.out.println("pse, MobileScheDao getschecrewList");
		
		try {
			String sql = "SELECT UID, SCHEDULERINFOUID, KIND, DEPARTMENT, NAME, RANK" + 
					"		FROM schecrew" 
					;
			
			conn = DriverManager.getConnection(Const.DB_SCP_URL, Const.DB_SCP_USER, Const.DB_SCP_PW);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int idx = 1;
				MobileScheCrewBean row = new MobileScheCrewBean();
				row.setUid(rs.getInt("UID"));
				row.setName(rs.getString("NAME"));
				list.add(row);
			}
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException e) {}
		}
		
		resultMap.put(Const.LIST, list);
		resultMap.put(Const.ERRCODE, isError);
		
		return resultMap;
	}
	 
}
