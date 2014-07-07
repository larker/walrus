package org.j4.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

public class DateUtils {
	/**
	 * 日期格式化
	 * 
	 * @return 返回格式化后的日期，若无法格式化则返回空字符串，不抛出异常
	 */
	public static String format(Date date, String pattern) {
		return formatDate(date, pattern);
	}

	/**
	 * 日期格式化
	 * 
	 * @return 返回格式化后的日期，若无法格式化则返回空字符串，不抛出异常
	 */
	public static String format(Date date, String pattern, String defaultString) {
		return formatDate(date, pattern, defaultString);
	}

	/**
	 * 日期格式化
	 * 
	 * @return 返回格式化后的日期，若无法格式化则返回空字符串，不抛出异常
	 */
	public static String formatDate(Date date, String pattern) {
		return formatDate(date, pattern, "");
	}

	/**
	 * 日期格式化
	 * 
	 * @return 返回格式化后的日期，若无法格式化则返回空字符串，不抛出异常
	 */
	public static String formatDate(Date date, String pattern,
			String defaultString) {
		if (date == null)
			return defaultString;
		try {
			return DateFormatUtils.format(date, pattern);
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return defaultString;
	}

	/**
	 * 比较两个日期是否是同一天，若无法比较则返回false，不抛出异常
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null && date2 == null)
			return true;
		else if (date1 != null && date2 != null) {
			try {
				return org.apache.commons.lang3.time.DateUtils.isSameDay(date1,
						date2);
			} catch (Exception ex) {
				// ex.printStackTrace();
			}
		}
		return false;
	}

	public static Date parseDate(String str, String pattern) {
		return parseDate(str, new String[] { pattern });
	}

	public static Date parseDate(String str, String[] patterns) {
		try {
			return org.apache.commons.lang3.time.DateUtils.parseDate(str,
					patterns);
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return null;
	}

	public static Date getDayInWeek(Date date, int whichDay) {
		return getDayInWeek(date, whichDay, false);
	}

	public static Date getDayInWeek(Date date, int whichDay, boolean mondayFirst) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DAY_OF_WEEK);
		if (mondayFirst) {
			day = (day == Calendar.SUNDAY ? day + 7 : day);
			whichDay = (whichDay == Calendar.SUNDAY ? whichDay + 7 : whichDay);
		}
		int interval = whichDay - day;

		c.add(Calendar.DATE, interval);
		// if(mondayFirst && whichDay == Calendar.SUNDAY){
		// c.add(Calendar.DATE, 7);
		// }

		return c.getTime();
	}

}
