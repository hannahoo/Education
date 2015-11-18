package com.e9w.ocr.serviceprovider.searchengine;

import java.util.List;

import com.e9w.ocr.serviceprovider.ServiceProvider;
import com.e9w.ocr.serviceprovider.ServiceProviderException;

public abstract class SearchEngine implements ServiceProvider {

	protected String indexPath = "/web/search_index/";

	@Override
	public Class<? extends ServiceProvider> getSpiClass() {
		return SearchEngine.class;
	}

	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}

	public abstract boolean hasSearchContent();

	public abstract List<SearchResult> search(String content, int maxItems)
			throws ServiceProviderException;

	public abstract IndexBuilder getIndexBuilder(String indexId);

}
