package com.e9w.ocr.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import net.sf.json.JSONObject;














import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e9w.ocr.controller.fetcher.Fetcher;
import com.e9w.ocr.service.SearchService;
import com.e9w.ocr.util.IOS_Verify;
import com.e9w.ocr.util.JsonUtil;
import com.e9w.ocr.util.Md5Util;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


public class IOSPurchaseController extends WebApiController {
	final static Logger logger = LoggerFactory
			.getLogger(IOSPurchaseController.class);
	private HttpServletRequest request;
	private HttpServletResponse response;
	public final static String STATE_D = "no response fron ios server";
	public final static String STATE_C = "the receipt is invalid";
	public final static String STATE_B = "the receipt is valid, but has been checked";
	public final static String STATE_A = "the receipt is valid, success!";

	/**
	 * 客户端向服务器验证
	 * 
	 * 
	 * * checkState A 验证成功有效(返回收据) B 账单有效，但己经验证过 C 服务器数据库中没有此账单(无效账单) D 不处理
	 * 
	 * @return
	 * @throws IOException
	 */
	public void search() throws IOException {

		Fetcher f = this.fetch();
		logger.debug("search,params={}", f.getParameterMap());
//		String signature = f.s("signature");
//		String purchase_info = f.s("purchase-info");
//		String environment = f.s("environment");
//		String pod = f.s("pod");
//		String signing_status = f.s("signing-status");
//		Map<String,Object> receive=new HashMap<String,Object>();
//		receive.put("signature", signature);
//		receive.put("purchase-info", purchase_info);
//		receive.put("environment", environment);
//		receive.put("pod", pod);
//		receive.put("signing-status", signing_status);
//        //String receipt=JsonUtil.toJson(receive);
//		String receipt = String.format(Locale.CHINA,"{\"signature\"=\"" + signature + "\";\"purchase-info\"=\"" + purchase_info +  "\";\"environment\"=\"" + environment +  "\";\"pod\"=\"" + pod + "\";\"signing-status\"=\"" + signing_status + "\"}");  
//		System.out.println("receipt "+receipt);
		String receipt = f.s("content");
		System.out.println("receipt "+receipt);
		String environment = f.s("environment");
		// 拿到收据的MD5
		// String md5_receipt=MD5.md5Digest(receipt);
		
		String md5_receipt = Md5Util.digest(receipt);
		// 默认是无效账单
		String result = STATE_C + "#" + md5_receipt;
		// 查询数据库，看是否是己经验证过的账号
		boolean isExists = false;
		String sql = "select receipt_id,receipt from ocr_ios_purchase where receipt=?";
		if( Db.find(sql, md5_receipt) != null){
			isExists = true;
		}
		String verifyResult = null;
		if (!isExists) {
			verifyResult = IOS_Verify.buyAppVerify(receipt, environment);
			System.out.println("verifyResult:"+verifyResult);
			if (verifyResult == null) {
				// 苹果服务器没有返回验证结果
				result = STATE_D + "#" + md5_receipt;
			} else {
				// 跟苹果验证有返回结果------------------
				Map<String,Object> data=JsonUtil.fromJsonToMap(verifyResult);
				Set keys = data.keySet( );  
				if(keys != null) {  
					Iterator iterator = keys.iterator( );  
					while(iterator.hasNext( )) {  
						Object key = iterator.next( );  
						Object value = data.get(key);  
						System.out.println(key.toString()+"   "+value.toString());
					}  
				}  
				Object states = data.get("status");
				String sta = states.toString();
				if (sta.equals("0")||sta.equals("0.0"))// 验证成功
				{
					Map<String,Object> r_receipt = (Map<String, Object>) data.get("receipt");
					
					// 产品ID
					String product_id = (String) r_receipt.get("product_id");
					// 数量
					String quantity = (String) r_receipt.get("quantity");
					// 跟苹果的服务器验证成功
					result = STATE_A + "#" + md5_receipt + "_" + product_id
							+ "_" + quantity;
					// 交易日期
					String purchase_date =  (String) data.get
							("purchase_date");
					//保存到数据库
					Record user = new Record().set("receipt", md5_receipt);//.set("age", 25);
					Db.save("ocr_ios_purchase", user);
					//user.get("id")；
				} else {
					// 账单无效
					result = STATE_C + "#" + md5_receipt;
				}
				// 跟苹果验证有返回结果------------------
			}
			// 传上来的收据有购买信息==end=============
		} else {
			// 账单有效，但己验证过
			result = STATE_B + "#" + md5_receipt;
		}
		// 返回结果
		try {
			System.out.println("验证结果     " + result);
			System.out.println();
			//response.getWriter().write(result);
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("content", result);
			this.renderData(data);
		} catch (Throwable e) {
			logger.error("ios purchase", e);
			this.renderData(e);
		}
	}
	
	
}
