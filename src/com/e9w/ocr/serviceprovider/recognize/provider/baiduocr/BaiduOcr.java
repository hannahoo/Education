package com.e9w.ocr.serviceprovider.recognize.provider.baiduocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.e9w.ocr.serviceprovider.recognize.Recognize;
import com.e9w.ocr.util.Result;
import com.e9w.ocr.util.UtilBase64;
import com.google.gson.Gson;

public class BaiduOcr  extends  Recognize{

	@Override
	public String recognize(File file) {
		
		String ret = null;
		try {
			ret = getImgString(file.getPath());
			ret = ret.replaceAll("[^0-9 a-z A-Z \u4e00-\u9fa5]", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public int getProviderId() {

		return 2;
	}
	public static String OCR_URL = "http://apis.baidu.com/apistore/idlocr/ocr";

	public static String API_KEY = "b8ced4e3b2bf6124b095e13ad8e9a785";

	public static String getImgString(String imgPath) throws Exception {

		try {
			String imgBase64Str = UtilBase64.getImageStr(imgPath);

			String httpArg = "fromdevice=pc&clientip=10.10.10.0&detecttype=LocateRecognize&languagetype=CHN_ENG&imagetype=1&image="
					+ imgBase64Str;
			
			String jsonResult = request(OCR_URL, httpArg);
			
		    return json2Str(jsonResult);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("解析请求处理异常", e);
		}

	}
	
	private static String json2Str(String json) {
		
		Result result = new Gson().fromJson(json, Result.class);
		
		if(result.isSuccess()) {
			StringBuffer sb = new StringBuffer();
			List<Map<String, Object>> retDatas = result.getRetData();
			for(Map<String, Object> ret : retDatas) {
				sb.append(ret.get("word"));
			}
			return sb.toString();
		} else {
			throw new RuntimeException(result.getErrMsg());
		}
		
	}

	/**
	 * @param urlAll
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 * @throws Exception
	 */
	private static String request(String httpUrl, String httpArg)
			throws Exception {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();

		URL url = new URL(httpUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		// 填入apikey到HTTP header
		connection.setRequestProperty("apikey", API_KEY);
		connection.setDoOutput(true);
		connection.getOutputStream().write(httpArg.getBytes("UTF-8"));
		connection.connect();
		InputStream is = connection.getInputStream();
		reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		String strRead = null;
		while ((strRead = reader.readLine()) != null) {
			sbf.append(strRead);
			sbf.append("\r\n");
		}
		reader.close();
		result = sbf.toString();

		return result;
	}
}
