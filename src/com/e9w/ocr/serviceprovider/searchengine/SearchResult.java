package com.e9w.ocr.serviceprovider.searchengine;

public class SearchResult {

	private final long Id;
	private final float score;

	public SearchResult(long id, float score) {
		super();
		Id = id;
		this.score = score;
	}

	public long getId() {
		return Id;
	}

	public float getScore() {
		return score;
	}

}
