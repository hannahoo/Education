package com.e9w.ocr.controller;

import com.jfinal.core.Controller;

public class HomeController extends Controller {

	public void index() {
		//this.renderText("welcome to ocr");
		this.renderJsp("AddQues.jsp");
	}

}
