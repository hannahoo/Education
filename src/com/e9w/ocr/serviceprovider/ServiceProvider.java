package com.e9w.ocr.serviceprovider;

public interface ServiceProvider {

	//public void loadConfig();

	public int getProviderId();

	public Class<? extends ServiceProvider> getSpiClass();

}
