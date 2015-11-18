package com.e9w.ocr.util;

public class Status {

	public final static Status STATUS_SUCCESS = new Status();

	private String querySign;
	private String errMsg;
	private int errNum;
	
	public Status() {
		this.errNum = 0;
		this.errMsg = null;
	}
	
	public Status(String errMsg) {
		this.errNum = 9;
		this.errMsg = errMsg;
	}

	public Status(int errNum, String errMsg) {
		this.errNum = errNum;
		this.errMsg = errMsg;
	}

	public String getQuerySign() {
		return querySign;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public int getErrNum() {
		return errNum;
	}
	
	public boolean isSuccess() {
		if(errNum==0) {
			return true;
		} else {
			return false;
		}
		
	}

}
