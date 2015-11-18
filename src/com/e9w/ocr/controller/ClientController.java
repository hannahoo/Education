package com.e9w.ocr.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e9w.ocr.constant.ErrorCode;
import com.e9w.ocr.controller.fetcher.Fetcher;
import com.e9w.ocr.controller.render.datarender.PageList;
import com.e9w.ocr.exception.ApiException;
import com.e9w.ocr.helper.SearchEngineHelper;
import com.e9w.ocr.service.RecognizeService;
import com.e9w.ocr.service.SearchService;
import com.e9w.ocr.serviceprovider.searchengine.SearchEngine;
import com.e9w.ocr.util.Env;
import com.e9w.ocr.util.JsonUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

public class ClientController extends WebApiController {
	final static Logger logger = LoggerFactory
			.getLogger(ClientController.class);

	public void recognizeAndSearch() {
		try {

			// ServletInputStream in = this.getRequest().getInputStream();
			//
			// // If required, wrap the real input stream with classes that
			// // "enhance" its behaviour for performance and stability
			// byte[] buffer = new byte[1024];
			// StringBuffer sb = new StringBuffer();
			// int size;
			// while ((size = in.read(buffer, 0, 1024)) > 0) {
			//
			// sb.append(new String(buffer, 0, size));
			// }
			// logger.debug("recognizeAndSearch,inputstream={}", sb.toString());

			List<UploadFile> files = this.getFiles(Env.getFileUploadDir(),
					Env.getFileUploadSize());

			Fetcher f = this.fetch();
			logger.debug("recognizeAndSearch,params={}", f.getParameterMap());

			if (files.size() != 1) {
				throw new ApiException(ErrorCode.PARAMER_ILLEGAL, "必须上传一个图片文件");
			}

			UploadFile file1 = files.get(0);

			RecognizeService recognizeService = new RecognizeService();
			String content = recognizeService.recognize(file1.getFile());

			Record user = new Record();
			user.set("pic_name", file1.getFileName());
			user.set("text", content);	
			SearchService searchService = new SearchService();
			List<Map<String, Object>> listdata = searchService.search(content,
					5);
			int cnt = 1;
			for (Iterator iter = listdata.iterator(); iter.hasNext();) {

				Map map = (Map) iter.next();
				long ques_id = (long) map.get("question_id");
				user.set("qid_"+Integer.toString(cnt), (int)ques_id);
				map.put("url",
						Env.getHost() + "/ocr/html/r.html?qid="
								+ map.get("question_id"));
				cnt++;
			}
			this.renderData(listdata);
			Db.save("ocr_record", user);

		} catch (Throwable e) {
			logger.error("recognizeAndSearch", e);
			this.renderData(e);
		}
	}

	public void recognize() {
		try {

			List<UploadFile> files = this.getFiles(Env.getFileUploadDir(),
					Env.getFileUploadSize());

			Fetcher f = this.fetch();
			logger.debug("recognize,params={}", f.getParameterMap());

			if (files.size() != 1) {
				throw new ApiException(ErrorCode.PARAMER_ILLEGAL, "必须上传一个图片文件");
			}

			UploadFile file1 = files.get(0);

			RecognizeService recognizeService = new RecognizeService();
			String content = recognizeService.recognize(file1.getFile());

			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("content", content);
			this.renderData(data);

		} catch (Throwable e) {
			logger.error("recognize", e);
			this.renderData(e);
		}
	}

	public void search() {
		try {

			Fetcher f = this.fetch();
			logger.debug("search,params={}", f.getParameterMap());
			String content = f.s("content");
			// String utf8 = new String(content.getBytes("ISO-8859-1"),"UTF-8");
			// logger.debug("search,params111111={}", utf8);
			content = content.replaceAll("[^0-9 a-z A-Z \u4e00-\u9fa5]", "");
			SearchService searchService = new SearchService();
			List<Map<String, Object>> listdata = searchService.search(content,
					5);
			for (Iterator iter = listdata.iterator(); iter.hasNext();) {

				Map map = (Map) iter.next();
				// long ques_id = (long) map.get("question_id");

				map.put("url",
						Env.getHost() + "/ocr/html/r.html?qid="
								+ map.get("question_id"));
			}
			this.renderData(listdata);

		} catch (Throwable e) {
			logger.error("search", e);
			this.renderData(e);
		}
	}

	public void getAnswer() {
		try {

			Fetcher f = this.fetch();
			logger.debug("getAnswer,params={}", f.getParameterMap());
			long questionId = f.i("question_id");
			SearchService searchService = new SearchService();
			List<Map<String, Object>> listdata = searchService
					.getAnswerList(questionId);
			this.renderData(listdata);

		} catch (Throwable e) {
			logger.error("getAnswer", e);
			this.renderData(e);
		}
	}

	public void getQuestion() {
		try {
			Fetcher f = this.fetch();
			logger.debug("search,params={}", f.getParameterMap());
			int title_id = f.i("title_id");
			int page = f.i("page");
			int pagesize = f.i("pagesize");
			int subject_id = f.i("subject_id");
			SearchService searchService = new SearchService();
			PageList<Map<String, Object>> listdata = searchService.getQuestion(
					title_id, page, pagesize, subject_id);
			this.renderData(listdata);

		} catch (Throwable e) {
			logger.error("searchWithTitle", e);
			this.renderData(e);
		}

	}

	public void getQuestionAndAnswer() {
		try {

			Fetcher f = this.fetch();
			logger.debug("getQuestionAndAnswer,params={}", f.getParameterMap());
			long questionId = f.i("question_id");
			SearchService searchService = new SearchService();
			Map<String, Object> listdata = searchService
					.getQuestionAndAnswer(questionId);
			this.renderData(listdata);

		} catch (Throwable e) {
			logger.error("getAnswer", e);
			this.renderData(e);
		}

	}

	public void getOutline() {
		try {
			Fetcher f = this.fetch();
			logger.debug("search,params={}", f.getParameterMap());
			SearchService searchService = new SearchService();
			// Map<String, Object> map = searchService.getOutline();
			// String json = searchService.getOutline();
			// System.out.println("json "+json);
			StringBuffer sb = new StringBuffer();
			String tempstr = null;
			try {
				String path = Env.getOutlinePath();
				File file = new File(path);
				if (!file.exists())
					throw new FileNotFoundException();
				FileInputStream fis = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fis, "utf-8"));
				while ((tempstr = br.readLine()) != null)
					sb.append(tempstr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// JsonUtil.fromJsonToMap(sb.toString());
			this.renderData(JsonUtil.fromJsonToMap(sb.toString()));

		} catch (Throwable e) {
			logger.error("searchWithTitle", e);
			this.renderData(e);
		}
	}

	public void buildIndex() {
		try {
			SearchEngine searchEngine = SearchEngineHelper
					.getDefaultSearchEngine();

			SearchService searchService = new SearchService();

			searchService.updateSearchContent(searchEngine);

			this.renderData();

		} catch (Throwable e) {
			logger.error("buildIndex", e);
			this.renderData(e);
		}
	}

}
