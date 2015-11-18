package com.e9w.ocr.serviceprovider.recognize;

import java.io.File;

import com.e9w.ocr.service.ServiceException;
import com.e9w.ocr.serviceprovider.ServiceProvider;

public abstract class Recognize implements ServiceProvider{



	@Override
	public Class<? extends ServiceProvider> getSpiClass() {

		return Recognize.class;
	}
	
	public abstract String recognize(File file)throws ServiceException ;

}
