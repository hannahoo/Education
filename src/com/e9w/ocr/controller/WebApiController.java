package com.e9w.ocr.controller;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import com.e9w.ocr.controller.fetcher.Fetcher;
import com.e9w.ocr.controller.fetcher.UrlEncodeFetcher;
import com.e9w.ocr.controller.render.DataRender;
import com.e9w.ocr.exception.ApiException;
import com.jfinal.core.Controller;

public class WebApiController extends Controller {

	private Map<String, String> headers = null;
	private Fetcher fetcher;
	private Logger logger;

	public WebApiController() {
	}

	public WebApiController(Logger logger) {
		this.logger = logger;
	}

	protected Map<String, String> getHeaders() {
		if (headers == null) {
			headers = new LinkedHashMap<String, String>();
			HttpServletRequest httpServletRequest = this.getRequest();
			Enumeration<String> names1 = httpServletRequest.getHeaderNames();

			while (names1.hasMoreElements()) {
				String name = names1.nextElement();
				name = name.toLowerCase();
				headers.put(name, httpServletRequest.getHeader(name));
			}
		}
		return headers;
	}

	protected String getRemoteAddr() {

		String onlineip = null;
		String[] envKeys = { "x-real-ip", "x-forwarded-for", "proxy-client-ip",
				"wl-proxy-client-ip", "remote_addr" };

		Map<String, String> headers = getHeaders();

		for (String key : envKeys) {
			String value = headers.get(key);
			if (value != null) {
				onlineip = value.toLowerCase();
				break;
			}
		}
		if (onlineip == null) {
			HttpServletRequest httpServletRequest = this.getRequest();
			onlineip = httpServletRequest.getRemoteAddr();
			if (onlineip == null) {
				onlineip = "0.0.0.0";
			}
		}

		return onlineip;

	}

	protected String getUserAgent() {
		return this.getRequest().getHeader("user-agent");
	}

	public void renderData() {
		this.renderData(null, null);
	}

	public void renderData(Object object) {
		this.renderData(object, null);
	}

	public void renderData(Object object, String statusMessage) {
		this.render(new DataRender(object, statusMessage));
	}

	public Fetcher fetch(Fetcher fetcher) {
		this.fetcher = fetcher;
		fetcher.setContext(this);
		return this.fetcher;
	}

	public Fetcher fetch() {
		return fetch(new UrlEncodeFetcher(this));
	}

	protected void renderException(String message, Throwable t) {
		if (logger != null) {
			Map<?, ?> parameterMap = null;

			if (this.fetcher != null) {
				parameterMap = this.fetcher.getParameterMap();
			}
			if (t instanceof ApiException) {
				logger.warn("{},params={},exception={}", message, parameterMap,
						((ApiException) t).getMessage());
			} else if (t instanceof Throwable) {
				message = message + ",params=" + parameterMap;
				logger.error(message, t);
			}
		}
		renderData(t, null);
	}

	//	protected <T extends Service> T getService(Class<T> clazz) {
	//		return ServiceManager.getService(clazz);
	//	}
}
