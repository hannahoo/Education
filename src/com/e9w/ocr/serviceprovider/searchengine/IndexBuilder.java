package com.e9w.ocr.serviceprovider.searchengine;

import java.util.List;

import com.e9w.ocr.serviceprovider.ServiceProviderException;

public interface IndexBuilder extends AutoCloseable {

	public abstract void setContents(List<SearchContent> contents)
			throws ServiceProviderException;

	public abstract void setContent(SearchContent content)
			throws ServiceProviderException;

	public abstract void open(String id) throws ServiceProviderException;

}
