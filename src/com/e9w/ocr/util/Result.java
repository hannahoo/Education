package com.e9w.ocr.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result {

	private String querySign;
	private String errMsg;
	private int errNum;

	private final Map<String, Object> rect;

	private final List<Map<String, Object>> retData;

	public Result(List<Map<String, Object>> list) {
		this(list, null);
	}

	public Result(List<Map<String, Object>> list, String statusMessage) {
		super();
		this.rect = null;
		this.retData = list;
	}

	public Result(Map<String, Object> object) {
		this(object, null);
	}

	public Result(Map<String, Object> object, String statusMessage) {
		super();
		this.rect = object;
		this.retData = null;

	}

	public Result(int dummy, Object[]... data) {
		super();
		this.retData = null;
		Map<String, Object> map = new HashMap<String, Object>();
		this.rect = map;
		for (Object[] o : data) {
			if (o.length == 2 && o[0] != null && o[1] != null) {
				map.put(o[0].toString(), o[1]);
			}
		}
	}

	public Result(int code, String message) {
		this.rect = null;
		this.retData = null;
	}

	public Map<String, Object> getRect() {
		return rect;
	}

	public List<Map<String, Object>> getRetData() {
		return retData;
	}

	public String getQuerySign() {
		return querySign;
	}

	public void setQuerySign(String querySign) {
		this.querySign = querySign;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public int getErrNum() {
		return errNum;
	}

	public void setErrNum(int errNum) {
		this.errNum = errNum;
	}
	
	public boolean isSuccess() {
		if(errNum==0) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getErrorMsg() {
		return errMsg;
	}

}
