package com.weixin.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;



/**
 * 数字工具
 * @author Alan
 * @date 2015年5月6日
 * @version V1.0.0
 */
public abstract class Numbers {
	private Numbers(){
		
	}

	public static final String PATTERN_CURRENCY	= "#,##0.00";
	public static final String PATTERN_PERCENT		= "#,##0.00%";
	
	private static Integer scale					= 2;
	private static RoundingMode roundingMode 		= RoundingMode.DOWN;
	
	/**
	 * 加
	 * 
	 * @param num
	 * @param another
	 * @return
	 */
	public static BigDecimal add(BigDecimal num, BigDecimal another) {
		return num.add(another).setScale(scale, roundingMode);
	}
	
	/**
	 * 减
	 * 
	 * @param num
	 * @param another
	 * @return
	 */
	public static BigDecimal subtract(BigDecimal num, BigDecimal another) {
		return num.subtract(another).setScale(scale, roundingMode);
	}
	
	/**
	 * 乘
	 * 
	 * @param num
	 * @param another
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal num, BigDecimal another) {
		return num.multiply(another).setScale(scale, roundingMode);
	}
	
	/**
	 * 除
	 * 
	 * @param num
	 * @param another
	 * @return
	 */
	public static BigDecimal divide(BigDecimal num, BigDecimal another) {
		return num.divide(another, scale, roundingMode);
	}
	
	public static BigDecimal divide(BigDecimal num, BigDecimal another,int scale) {
		return num.divide(another, scale, roundingMode);
	}
	
	/**
	 * 读取格式器
	 * 
	 * @param pattern
	 * @return
	 */
	public static NumberFormat getFormat(String pattern) {
		return new DecimalFormat(pattern);
	}
	
	/**获取本地金额格式
	 * @param pattern
	 * @return
	 */
	public static NumberFormat getCurrencyFormat() {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format;
	}
	
	/**
	 * 格式化数字
	 * 
	 * @param pattern
	 * @param number
	 * @return
	 */
	public static String format(String pattern, Number number) {
		return getFormat(pattern).format(number.doubleValue());
	}
	
	/**
	 * 解析数字字符串
	 * 
	 * @param pattern
	 * @param source
	 * @return
	 * @throws ParseException
	 */
	public static Number parse(String pattern, String source) throws ParseException {
		return getFormat(pattern).parse(source);
	}
	
	/**
	 * 格式化金额
	 * 
	 * @param number
	 * @return
	 */
	public static String currency(Number number) {
		BigDecimal currency = new BigDecimal(number.toString()).setScale(2, RoundingMode.DOWN);
		return getCurrencyFormat().format(currency);
	}
	
	/**
	 * 格式化百分比
	 * 
	 * @param number
	 * @return
	 */
	public static String percent(Number number) {
		BigDecimal percent = new BigDecimal(number.toString()).setScale(4, RoundingMode.DOWN);
		return format(PATTERN_PERCENT, percent);
	}
	
}