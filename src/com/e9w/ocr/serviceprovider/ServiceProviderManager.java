package com.e9w.ocr.serviceprovider;

import java.util.HashMap;
import java.util.Map;

import com.e9w.ocr.serviceprovider.recognize.provider.baiduocr.BaiduOcr;
import com.e9w.ocr.serviceprovider.recognize.provider.tesseract.Tesseract;
import com.e9w.ocr.serviceprovider.searchengine.provider.Lucene;

public class ServiceProviderManager {

	static Map<Class<? extends ServiceProvider>, Map<Integer, ServiceProvider>> spis = new HashMap<>();

	// static {
	// loadServiceProivers();
	// }

	public static <T extends ServiceProvider> void addServiceProvider(
			T serviceProvider) {
		if (serviceProvider != null) {
			Map<Integer, ServiceProvider> providers = spis.get(serviceProvider
					.getSpiClass());
			if (providers == null) {
				providers = new HashMap<>();
				spis.put(serviceProvider.getSpiClass(), providers);
			}
			providers.put(serviceProvider.getProviderId(), serviceProvider);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends ServiceProvider> T getServiceProvider(
			int providerId, Class<T> spiClass) {

		Map<Integer, ServiceProvider> providers = spis.get(spiClass);
		if (providers == null) {
			return null;
		}
		return (T) providers.get(providerId);

	}

	public static void loadServiceProviders() {

		addServiceProvider(new Lucene());
		addServiceProvider(new Tesseract());
		addServiceProvider(new BaiduOcr());
	}

}
