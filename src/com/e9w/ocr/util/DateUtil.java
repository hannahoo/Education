package com.e9w.ocr.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {

	private static long mills_per_second = 1000;
	private static long mills_per_minute = 60 * mills_per_second;
	private static long mills_per_hour = 60 * mills_per_minute;
	private static long mills_per_day = 24 * mills_per_hour;

	private static String format_default = "yyyy-MM-dd HH:mm:ss";

	private static String format_iso8601 = "yyyy-MM-ddTHH:mm:ss";

	public static void setDefaultFormat(String format) {
		format_default = format;
	}

	public static String format(Date date, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	public static String format(Date date) {
		return format(date, format_default);
	}

	public static String format(String format) {
		return format(new Date(), format);
	}

	public static String format() {
		return format(new Date(), format_default);
	}

	public static String formatIso8061(Date date) {
		return format(new Date(), format_iso8601);
	}

	// static public long utilNextDay() {
	//
	// Calendar.getInstance();
	//
	// // new Cal
	// // Date d=new Date();
	// // d.get
	// }

	static public Date getSpanDays(Date d, int days) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d);
		// c.setTimeInMillis(millis);
		c1.add(Calendar.DATE, days);

		Calendar c2 = Calendar.getInstance();
		c2.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH),
				c1.get(Calendar.DATE));
		return c2.getTime();
	}

	public static long now2nextDayMilli(Date date, int days) {
		Date nextDay = DateUtils.addDays(date, 1);
		nextDay = DateUtils.truncate(nextDay, Calendar.DATE);
		return nextDay.getTime() - date.getTime();
	}

	public static long now2nextDayMilli() {
		return now2nextDayMilli(new Date(), 1);
	}

	public static Date getDay(Date date) {
		return DateUtils.truncate(date, Calendar.DATE);
	}

	public static Date getYesterday() {
		Date date = new Date();
		date = DateUtils.addDays(date, -1);
		return getDay(date);
	}

	public static Date getToday() {
		return getDay(new Date());
	}

	private static long getMilliseconds(int days, int hours, int minutes,
			int seconds) {
		long milliseconds = 0;
		milliseconds += days * mills_per_day;
		milliseconds += hours * mills_per_hour;
		milliseconds += minutes * mills_per_minute;
		milliseconds += seconds * mills_per_second;
		return milliseconds;

	}

	public static Date floor(Date date, int days, int hours, int minutes,
			int seconds) {
		return floor(date, getMilliseconds(days, hours, minutes, seconds));
	}

	public static long floor(long time, int days, int hours, int minutes,
			int seconds) {

		return floor(time, getMilliseconds(days, hours, minutes, seconds));
	}

	public static long floor(long time, long milliseconds) {
		long result = time / milliseconds;
		return result * milliseconds;
	}

	public static Date floor(Date date, long milliseconds) {
		return new Date(floor(date.getTime(), milliseconds)
				- getMilliseconds(0, 8, 0, 0));
	}

	public static void main(String[] args) {

		//System.currentTimeMillis()
		Date date1 = new Date();
		Date date2 = DateUtil.floor(date1, 1, 0, 0, 0);
		Date date3 = new Date(date1.getTime());
		System.out.println(DateUtil.format(date1));
		System.out.println(DateUtil.format(date2));
		System.out.println(DateUtil.format(date3));
	}
}
