package com.e9w.ocr.helper;

import com.e9w.ocr.serviceprovider.ServiceProviderManager;
import com.e9w.ocr.serviceprovider.recognize.Recognize;
import com.e9w.ocr.util.Env;

public class RecognizeHelper {
	public static Recognize getDefaultRecognize() {
		return ServiceProviderManager.getServiceProvider(
				Env.getDefaultRecognizeProvider(), Recognize.class);
	}

}
