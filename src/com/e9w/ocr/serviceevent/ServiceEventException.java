package com.e9w.ocr.serviceevent;

import com.e9w.ocr.exception.ApiException;

public class ServiceEventException extends ApiException {

	/**   */
	private static final long serialVersionUID = 7785046025402776377L;

	public ServiceEventException(int code, String message, Throwable t) {
		super(code, message, t);
		// TODO Auto-generated constructor stub
	}

	public ServiceEventException(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

}
