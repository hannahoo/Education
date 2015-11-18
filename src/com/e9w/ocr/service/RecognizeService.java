package com.e9w.ocr.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e9w.ocr.constant.ErrorCode;
import com.e9w.ocr.helper.RecognizeHelper;
import com.e9w.ocr.serviceprovider.recognize.Recognize;
import com.e9w.ocr.util.Env;
import com.e9w.ocr.util.IOUtils;
import com.e9w.ocr.util.WorkingResourceUtil;

public class RecognizeService extends Service {

	final static long max_filesize = 2048;
	final static Logger logger = LoggerFactory
			.getLogger(RecognizeService.class);

	public String recognize(File imageFile) throws ServiceException {
		
		
		Recognize recognize=RecognizeHelper.getDefaultRecognize();	
		return recognize.recognize(imageFile);
		
		

	}
}
