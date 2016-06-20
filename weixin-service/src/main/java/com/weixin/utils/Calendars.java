package com.weixin.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Calendars {
	private static final Logger LOGGER = LoggerFactory.getLogger(Calendars.class);
	private static final String PATTERN_DATE = "yyyy-MM-dd";
	private static final String PATTERN_TIME = "HH:mm:ss";
	private static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	
	private Calendars(){
		
	}
	/**
	 * 读取日期格式
	 * 
	 * @param pattern
	 * @return
	 */
	public static DateFormat getDateFormat(String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		return format;
	}
	
	/**
	 * 格式化日期
	 * 
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static String format(String pattern, Date date) {
		return getDateFormat(pattern).format(date);
	}
	
	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return HH:mm:ss
	 */
	public static String format2Time (Date date) {
		return getDateFormat(PATTERN_TIME).format(date);
	}
	
	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String format2DateTime (Date date) {
		return getDateFormat(PATTERN_DATE_TIME).format(date);
	}
	
	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String format2Date (Date date) {
		return getDateFormat(PATTERN_DATE).format(date);
	}
	
	/**
	 * 格式化日期
	 * 
	 * @param pattern
	 * @return
	 */
	public static String format(String pattern) {
		return format(pattern, new Date());
	}
	
	public static Date stringToDate(String dateStr, String formatStr) {
		DateFormat dd = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			LOGGER.error("转换异常",e);
		}

		return date;
	}
	
	public static Date stringToDate(String dateStr) {
		DateFormat dd = new SimpleDateFormat(Calendars.PATTERN_DATE);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			LOGGER.error("转换异常",e);
		}

		return date;
	}
	
	public static Date string2DateTime (String dateStr) {
		DateFormat dd = new SimpleDateFormat(Calendars.PATTERN_DATE_TIME);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			LOGGER.error("转换异常",e);
		}

		return date;
	}
}