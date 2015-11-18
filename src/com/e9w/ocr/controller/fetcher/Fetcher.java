package com.e9w.ocr.controller.fetcher;

import java.util.Map;

import com.e9w.ocr.lang.Parameter;
import com.jfinal.core.Controller;

public abstract class Fetcher extends Parameter {

	private Controller controller;

	//private Map<String, Object> parameterMap;

	public Fetcher(Controller controller) {
		setContext(controller);
	}

	public Fetcher() {

	}

	public final void setContext(Controller controller) {
		this.controller = controller;
		this.setParameterMap(buildParameterMap());
	}

	public Controller getController() {
		return controller;
	}

	protected abstract Map<String, Object> buildParameterMap();

}
