package com.e9w.ocr.controller;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e9w.ocr.constant.ErrorCode;
import com.e9w.ocr.controller.fetcher.Fetcher;
import com.e9w.ocr.exception.ApiException;
import com.e9w.ocr.service.RecognizeService;
import com.e9w.ocr.service.SearchService;
import com.e9w.ocr.util.Env;
import com.jfinal.upload.UploadFile;

public class JSPController extends WebApiController {
	final static Logger logger = LoggerFactory
			.getLogger(ClientController.class);

	

	public void addQuestion() {
		try {

			Fetcher f = this.fetch();
			logger.debug("addQuestion,params={}", f.getParameterMap());
			String title = f.s("title");
			String question = f.s("question");
			String answer = f.s("answer");
			SearchService searchService = new SearchService();
			Boolean res = searchService.addQuestion(title, question, answer);
			//this.renderData(res.toString());
			this.renderJsp("..\\AddResult.jsp");
		} catch (Throwable e) {
			logger.error("addQuestion", e);
			this.renderData(e);
		}
	}

	public void addResult() {
		try {

			
			this.renderJsp("..\\AddQues.jsp");
		} catch (Throwable e) {
			logger.error("addQuestion", e);
			this.renderData(e);
		}
	}
}
