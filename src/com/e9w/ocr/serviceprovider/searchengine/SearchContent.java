package com.e9w.ocr.serviceprovider.searchengine;

public class SearchContent {
	private final long Id;
	private final String title;
	private final String content;
	// add html content, pictures

	public SearchContent(long id, String title, String content) {
		super();
		Id = id;
		this.title = title;
		this.content = content;
	}

	public long getId() {
		return Id;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

}
