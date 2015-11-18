package com.e9w.ocr.lang;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import com.e9w.ocr.util.ParameterUtil;

public class Parameter {

	private Map<?, ?> parameterMap;
	protected Charset charset = Charset.forName("ISO-8859-1");

	public Parameter(Map<?, ?> parameterMap) {

		this.parameterMap = parameterMap;
		if (parameterMap == null) {
			throw new IllegalArgumentException("参数列表不能为空");
		}

	}

	protected Parameter() {
		this.parameterMap = new LinkedHashMap<Object, Object>();
	}

	protected void setParameterMap(Map<?, ?> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public Map<?, ?> getParameterMap() {
		return parameterMap;
	}

	public int i(String name) throws IllegalArgumentException {
		return ParameterUtil.integerParam(parameterMap, name);
	}

	public int i(String name, int defaultValue) {
		return ParameterUtil.integerParam(parameterMap, name, defaultValue);
	}

	public long l(String name) throws IllegalArgumentException {
		return ParameterUtil.longParam(parameterMap, name);
	}

	public long l(String name, int defaultValue) {
		return ParameterUtil.longParam(parameterMap, name, defaultValue);
	}

	public boolean b(String name) throws IllegalArgumentException {
		return ParameterUtil.booleanParam(parameterMap, name);

	}

	public boolean b(String name, boolean defaultValue) {
		return ParameterUtil.booleanParam(parameterMap, name, defaultValue);
	}

	public double d(String name) throws IllegalArgumentException {
		return ParameterUtil.doubleParam(parameterMap, name);
	}

	public double d(String name, double defaultValue) {
		return ParameterUtil.doubleParam(parameterMap, name, defaultValue);
	}

	public String s(String name) throws IllegalArgumentException {

		return ParameterUtil.stringParam(parameterMap, name);

	}

	public String s(String name, String defaultValue) {
		return ParameterUtil.stringParam(parameterMap, name, defaultValue);
	}

	public String[] as(String name) {
		return ParameterUtil.stringArrayParam(parameterMap, name, 0);
	}

	@Override
	public String toString() {
		if (parameterMap != null) {
			return parameterMap.toString();
		} else {
			return "{}";
		}

	}

}
