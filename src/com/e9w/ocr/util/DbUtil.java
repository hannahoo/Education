package com.e9w.ocr.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.e9w.ocr.controller.render.datarender.PageList;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class DbUtil{


	public static PageList<Map<String, Object>> paginate(int page, int pagesize, String string,
			String string2, int title_id, int subject_id) {
		String sql = string + " " + string2 + " LIMIT ?,?;";
		long total = 0;
		List<Map<String, Object>> listdata = record2list(Db.find(sql, title_id,
				subject_id, (page - 1) * pagesize, pagesize));
		for (Iterator<Map<String, Object>> iter = listdata.iterator(); iter
				.hasNext();) {
			Map map = (Map) iter.next();
			map.put("url",
					Env.getHost() + "/ocr/html/r.html?qid="
							+ map.get("question_id"));
		}
		if (listdata.size() == pagesize) {
			total = (page + 1) * pagesize;
		} else {
			total = (page - 1) * pagesize + listdata.size();
		}
		PageList<Map<String, Object>> pagelistdata = new PageList(listdata,
				total, page, pagesize);
		return pagelistdata;
	}
	
	private static List<Map<String, Object>> record2list(List<Record> list) {

		List<Map<String, Object>> result = new ArrayList<>();
		if (list != null && list.size() > 0) {
			for (Record r : list) {
				result.add(record2map(r));
			}
		}
		return result;
	}
	private static Map<String, Object> record2map(Record record) {
		if (record == null) {
			return null;
		}
		Map<String, Object> m = record.getColumns();
		return m;
	}

}
