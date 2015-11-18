package com.e9w.ocr.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;

public class UtilBase64 {

	// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	@SuppressWarnings("deprecation")
	public static String getImageStr(String imgFilePath)
			throws UnsupportedEncodingException {
		byte[] data = null;

		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] base64Bytes = Base64.encodeBase64(data);
		String base64ImageStr = new String(base64Bytes, "utf-8");

		String base64ImageEncodeStr = URLEncoder.encode(base64ImageStr);

		// 对字节数组Base64编码
		// BASE64Encoder encoder = new BASE64Encoder();
		// return encoder.encode(data);// 返回Base64编码过的字节数组字符串

		return base64ImageEncodeStr;
	}

	// 对字节数组字符串进行Base64解码并生成图片
	public static boolean generateImage(String imgStr, String imgFilePath) {
		if (imgStr == null) // 图像数据为空
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] bytes = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}