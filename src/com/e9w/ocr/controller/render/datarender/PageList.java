package com.e9w.ocr.controller.render.datarender;

import java.util.ArrayList;
import java.util.Collection;

public class PageList<E> extends ArrayList<E> {

	/**   */
	private static final long serialVersionUID = 829008655060053360L;
	private long total;
	private final int pageIndex;
	private final int pageSize;

	public PageList(Collection<? extends E> c, long total, int pageIndex,
			int pageSize) {
		super(c);
		this.total = total;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}

//	public PageList(Collection<? extends E> c, long total, int pageIndex,
//			int pageSize, boolean flag) {
//		super(c);
//		if(c.size() == pageSize)
//		{
//			total = (pageIndex + 1) * pageSize;
//		}
//		else
//		{
//			total = (pageIndex - 1) * pageSize + c.size();
//		}
//		this.total = total;
//		this.pageIndex = pageIndex;
//		this.pageSize = pageSize;
//	}
	
	public PageList(long total, int pageIndex, int pageSize) {
		super();
		this.total = total;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}

	public PageList(int pageIndex, int pageSize) {
		super();
		this.total = 0;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}
