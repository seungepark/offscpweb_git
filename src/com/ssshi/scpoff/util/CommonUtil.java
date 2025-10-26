package com.ssshi.scpoff.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class CommonUtil {

	public static final int LIST_LIMIT = 10;
	public static final int LIST_LIMIT_FOR_ALL = 300;
	
	public static int getListStart(int page) {
		return LIST_LIMIT * (page - 1);
	}
	
	public static String getDateStr(boolean isYesterday) {
		Calendar cal = Calendar.getInstance();
		
		if(isYesterday) {
			cal.add(Calendar.DATE, -1);
		}
		
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		return cal.get(Calendar.YEAR) + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day;
	}
	
	public static String getNowStr() {
		return Calendar.getInstance().getTime().toString();
	}
	
	/**
	 * 입력된 두 날짜 사이의 차이값 검사
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int calculateDateDifference(String date1, String date2) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate1 = LocalDate.parse(date1, formatter);
		LocalDate localDate2 = LocalDate.parse(date2, formatter);

		return localDate1.until(localDate2).getDays();
	}

	/**
	 * 입력된 버전의 숫자값 추출
	 * @param input
	 * @return
	 */
	public static int extractNumbers(String input) {
		String numberPart = input.replaceAll("\\D+", "");

		if (!numberPart.isEmpty()) {
			return Integer.parseInt(numberPart);
		} else {
			return 0;
		}
	}
}
