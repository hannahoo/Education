package com.e9w.ocr.util;

import java.util.Arrays;
import java.util.List;

public class StringUtil {

	public static final String EMPTY_STRING = "";

	public static boolean isEmptyOrNull(String s) {
		return s == null || s.length() == 0 || s.trim().length() == 0;
	}

	public static boolean isEmpty(String s) {
		if (s == null) {
			return true;
		}
		if (s.length() == 0) {
			return true;
		}
		if (s.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static String getEmptyString(String s) {
		return isEmpty(s) == false ? s : EMPTY_STRING;

	}

	public static String markString(String s, int left) {

		String result = s;
		int x = left;
		if (s != null && s.length() > 0) {
			int begin = x;
			int length = s.length();
			int index = s.indexOf('@');
			int end = index > 0 ? index - x : length - x;
			if (end > begin) {
				char[] chars = s.toCharArray();
				for (int i = begin; i < end; i++) {
					chars[i] = '*';
				}
				result = new String(chars);
			}
		}
		return result;
	}

	public static String toHexString(byte[] data) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(data[i] & 0xff);
			if (hex.length() == 1) {
				sb.append("0");
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	public static <T> String join(Object sp, List<T> params) {
		StringBuffer sb = null;
		new StringBuffer();

		for (Object p : params) {
			if (sb == null) {
				sb = new StringBuffer();
			} else {
				sb.append(sp);
			}
			sb.append(p);
		}
		return sb.toString();

	}

	public static <T> String joinArray(Object sp, T[] params) {

		return join(sp, Arrays.asList(params));
	}

	public static String joinTimes(Object sp, Object s, int times) {
		Object[] params = new Object[times];
		for (int i = 0; i < times; i++) {
			params[i] = s;
		}
		return join(sp, params);
	}

	public static String join(Object sp, Object... params) {
		return join(sp, Arrays.asList(params));
	}

}
