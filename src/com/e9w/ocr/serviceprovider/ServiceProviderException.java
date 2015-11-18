package com.e9w.ocr.serviceprovider;

import com.e9w.ocr.exception.ApiException;

public class ServiceProviderException extends ApiException {

	/**   */
	private static final long serialVersionUID = -8472784202096839751L;

	private final String providerName;

	public ServiceProviderException(String providerName, int code,
			String message, Throwable t) {
		super(code, message, t);
		this.providerName = providerName;
		// TODO Auto-generated constructor stub
	}

	public ServiceProviderException(String providerName, int code,
			String message) {
		super(code, message);
		this.providerName = providerName;
		// TODO Auto-generated constructor stub
	}

	public String getProviderName() {
		return providerName;
	}

}
