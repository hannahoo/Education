package com.e9w.ocr.util;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
	protected static Gson createGson() {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.create();
		return gson;
	}

	public static String toJson(Object obj) {
		Gson gson = createGson();
		return gson.toJson(obj);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		Gson gson = createGson();
		return gson.fromJson(json, clazz);
	}

	public static <T> Map<String, T> fromJsonToMap(String json) {
		Gson gson = createGson();
		return gson.fromJson(json, new TypeToken<Map<String, T>>() {
		}.getType());
	}
}
