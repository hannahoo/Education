package com.e9w.ocr.helper;

import com.e9w.ocr.serviceprovider.ServiceProviderManager;
import com.e9w.ocr.serviceprovider.searchengine.SearchEngine;
import com.e9w.ocr.util.Env;

public class SearchEngineHelper {

	public static SearchEngine getDefaultSearchEngine() {
		return ServiceProviderManager.getServiceProvider(
				Env.getDefaultSearchEngineProvider(), SearchEngine.class);
	}

}
