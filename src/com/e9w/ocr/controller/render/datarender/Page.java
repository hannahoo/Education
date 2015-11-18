package com.e9w.ocr.controller.render.datarender;

public class Page {
	private final int pageindex;
	private final int pagesize;
	private final int total;

	public Page(int pageindex, int pagesize, int total) {
		super();
		this.pageindex = pageindex;
		this.pagesize = pagesize;
		this.total = total;
	}

	public int getPageindex() {
		return pageindex;
	}

	public int getPagesize() {
		return pagesize;
	}

	public int getTotal() {
		return total;
	}

}
