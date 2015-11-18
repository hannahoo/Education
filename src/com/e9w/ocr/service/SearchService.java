package com.e9w.ocr.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e9w.ocr.constant.ErrorCode;
import com.e9w.ocr.controller.render.datarender.PageList;
import com.e9w.ocr.helper.SearchEngineHelper;
import com.e9w.ocr.serviceprovider.ServiceProviderException;
import com.e9w.ocr.serviceprovider.searchengine.IndexBuilder;
import com.e9w.ocr.serviceprovider.searchengine.SearchContent;
import com.e9w.ocr.serviceprovider.searchengine.SearchEngine;
import com.e9w.ocr.serviceprovider.searchengine.SearchResult;
import com.e9w.ocr.util.DbUtil;
import com.e9w.ocr.util.Env;
import com.e9w.ocr.util.StringUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class SearchService extends Service {

	final static Logger logger = LoggerFactory.getLogger(SearchService.class);

	final static int max_items = 5;
	Date lastUpdate = null;

	public List<Map<String, Object>> search(String content, int maxItems)
			throws ServiceException {

		SearchEngine searchEngine = SearchEngineHelper.getDefaultSearchEngine();
		if (searchEngine == null) {
			throw new ServiceException(this.getServiceName(),
					ErrorCode.ILLEGAL_STATE, "没找到对应的搜索引擎");
		}

		ensureHasContent(searchEngine);

		List<SearchResult> searchResults;
		try {
			searchResults = searchEngine.search(content, maxItems);
		} catch (ServiceProviderException e) {
			throw new ServiceException(this.getServiceName(), e);
		}
		if (searchResults.size() == 0) {
			throw new ServiceException(this.getServiceName(),
					ErrorCode.NOT_FOUND_SPEC_DATA, "没找搜索结果");
		}

		// --sort

		Object[] ids = new Long[searchResults.size()];
		final Map<Long, Float> score = new HashMap<Long, Float>();
		for (int i = 0; i < ids.length; i++) {
			ids[i] = searchResults.get(i).getId();
			logger.debug("ids[i] " + ids[i]);
			score.put(searchResults.get(i).getId(), searchResults.get(i)
					.getScore());
		}
		String in = StringUtil.joinTimes(",", "?", ids.length);
		String sqlIn = "select question_id,title,title_id from ocr_question "
				+ "where question_id in ( " + in + ")";
		// String sqlIn =
		// "SELECT  q.question_id,q.title,q.content,  q.title_id, q.content_html,  a.content_html AS answer_html FROM ocr_question q  INNER JOIN ocr_answer a    ON q.question_id = a.question_id WHERE q.question_id IN ( "
		// + in + ")";
		System.out.println(sqlIn);
		List<Map<String, Object>> list = record2list(Db.find(sqlIn, ids));

		Collections.sort(list, new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Float f1 = score.get(o1.get("question_id"));
				Float f2 = score.get(o2.get("question_id"));
				return f2.compareTo(f1);
			}

		});

		return list;
	}

	public List<Map<String, Object>> getAnswerList(long questionId) {
		String sql = "select answer_id,content,content_html from ocr_answer where question_id=?";
		return record2list(Db.find(sql, questionId));
	}

	private void ensureHasContent(SearchEngine searchEngine)
			throws ServiceException {

		if (!searchEngine.hasSearchContent()) {
			synchronized (searchEngine) {
				if (!searchEngine.hasSearchContent()) {
					updateSearchContent(searchEngine);
				}
			}

		}
	}

	public void updateSearchContent(SearchEngine searchEngine)
			throws ServiceException {

		String select = "select question_id,content ";
		String from = "from ocr_question";
		IndexBuilder indexBuilder = null;
		if (true) {
			// lastUpdate = update;

			String id = "" + System.currentTimeMillis();// DateUtil.format(lastUpdate,
														// "yyyy-MM-dd-HH-mm-ss");
			try {
				indexBuilder = searchEngine.getIndexBuilder(id);
				indexBuilder.open(id);
				// searchEngine.setIndexFile(id);
				int pageIndex = 0;
				int pageSize = 1000;
				// String url =
				// "jdbc:mysql://192.169.1.3:3306/ocr?user=ocr&password=ocr";
				String url = Env.getIndexUrl();
				try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				long start = System.currentTimeMillis();
				long count = 0;

				Connection con = null;
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					con = DriverManager.getConnection(url);

					ps = (PreparedStatement) con.prepareStatement(
							"select question_id,content from ocr_question",
							ResultSet.TYPE_FORWARD_ONLY,
							ResultSet.CONCUR_READ_ONLY);

					ps.setFetchSize(Integer.MIN_VALUE);

					ps.setFetchDirection(ResultSet.FETCH_REVERSE);

					rs = ps.executeQuery();

					while (rs.next()) {

						// 此处处理业务逻辑
						try {
							indexBuilder.setContent(new SearchContent(rs
									.getLong("question_id"), null, rs
									.getString("content")));
						} catch (ServiceProviderException e) {
							logger.error("setContent", e);
						}
						if (count++ % 5000 == 0) {

							System.out.println(" 写入到第  " + (count / 5000)
									+ " 个文件中！");
							long end = System.currentTimeMillis();
							System.out.println(" time:" + (end - start) / 1000
									+ "s");
						}

					}
					System.out.println("取回数据量为  " + count + " 行！");
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (rs != null) {
							rs.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						if (ps != null) {
							ps.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						if (con != null) {
							con.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				// }
			} catch (ServiceProviderException e1) {
				logger.error("updateSearchContent", e1);
			} finally {
				try {
					indexBuilder.close();
				} catch (Exception e) {

				}
			}

		}
		// long end = System.currentTimeMillis();
		// System.out.println("time to index   "+( end-start));
	}

	private static Object lock = new Object();

	public Boolean addQuestion(String title, String question, String answer) {
		Record user = new Record().set("title", title);
		user.set("content", question);
		user.set("content_id", 0);
		user.set("content_html", 0);
		long max_id = 0;
		synchronized (lock) {
			Db.save("ocr_question", user);
			String sql = "select max(question_id) from ocr_question";
			max_id = (long) Db.queryNumber(sql);
		}
		Record user1 = new Record().set("question_id", max_id);
		user1.set("content", answer);
		user1.set("content_html", 0);
		Db.save("ocr_answer", user1);
		return true;
	}

	public PageList<Map<String, Object>> getQuestion1(int title_id, int page,
			int pagesize, int subject_id) {
		String sql = "SELECT  question_id,title,title_id  FROM ocr_question  WHERE title_id = ? and subject_id = ? LIMIT ?,?;";
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

	public PageList<Map<String, Object>> getQuestion2(int title_id, int page,
			int pagesize, int subject_id) {

		Page<Record> pagerecord = Db.paginate(page, pagesize,
				"select question_id,title,title_id",
				"FROM ocr_question  WHERE title_id = ? and subject_id = ?",
				title_id, subject_id);
		PageList<Map<String, Object>> listdata = page2PageList(pagerecord);
		List<Record> tmplist = pagerecord.getList();
		for (Iterator iter = listdata.iterator(); iter.hasNext();) {

			Map map = (Map) iter.next();
			// long ques_id = (long) map.get("question_id");

			map.put("url",
					Env.getHost() + "/ocr/html/r.html?qid="
							+ map.get("question_id"));
		}
		return listdata;

	}
	
	public PageList<Map<String, Object>> getQuestion(int title_id, int page,
			int pagesize, int subject_id) {

		PageList<Map<String, Object>> pagelistdata = DbUtil.paginate(page, pagesize,
				"select question_id,title,title_id",
				"FROM ocr_question  WHERE title_id = ? and subject_id = ?",
				title_id, subject_id);
		for (Iterator iter = pagelistdata.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			map.put("url",
					Env.getHost() + "/ocr/html/r.html?qid="
							+ map.get("question_id"));
		}
		return pagelistdata;

	}

	public Map<String, Object> getQuestionAndAnswer(long question_id) {
		String sql = "SELECT q.content_html AS question_html,  a.content_html AS answer_html FROM ocr_question q  INNER JOIN ocr_answer a   ON q.question_id = a.question_id WHERE q.question_id = ? limit 1";
		return record2map(Db.findFirst(sql, question_id));
	}

	public Map<String, Object> getOutline() {
		Map m_root = new HashMap();
		// List l_grade = new List();
		List<Map<String, Object>> l_grade = new ArrayList<Map<String, Object>>();
		m_root.put("name", "root");
		m_root.put("grade_list", l_grade);
		String sql4 = "select grade,grade_id from ocr_grade";
		List<Map<String, Object>> list_grade = record2list(Db.find(sql4));
		for (Iterator q = list_grade.iterator(); q.hasNext();) {

			Map map_q = (Map) q.next();
			String grade = (String) map_q.get("grade");
			int grade_id = (int) map_q.get("grade_id");

			Map m_grade = new HashMap();
			m_grade.put("grade_name", grade);
			m_grade.put("grade_id", grade_id);
			List<Map<String, Object>> l_subject = new ArrayList<Map<String, Object>>();
			m_grade.put("subject_list", l_subject);

			String sql = "select subject,subject_id from ocr_subject where grade_id = ?";
			List<Map<String, Object>> list_subject = record2list(Db.find(sql,
					grade_id));
			for (Iterator i = list_subject.iterator(); i.hasNext();) {

				Map map = (Map) i.next();
				String subject = (String) map.get("subject");
				int subject_id = (int) map.get("subject_id");

				Map m_subject = new HashMap();
				m_subject.put("subject_name", subject);
				m_subject.put("subject_id", subject_id);

				List<Map<String, Object>> l_term = new ArrayList<Map<String, Object>>();
				m_subject.put("term_list", l_term);

				String sql1 = "select term,term_id from ocr_term where term_id<3";
				if (grade_id > 9) {
					sql1 = "select term,term_id from ocr_term where term_id>2";
				}
				List<Map<String, Object>> list_term = record2list(Db.find(sql1));
				for (Iterator j = list_term.iterator(); j.hasNext();) {
					Map map_j = (Map) j.next();
					String term = (String) map_j.get("term");
					int term_id = (int) map_j.get("term_id");

					Map m_term = new HashMap();
					m_term.put("term_name", term);
					m_term.put("term_id", term_id);
					List<Map<String, Object>> l_section = new ArrayList<Map<String, Object>>();
					m_term.put("section_list", l_section);

					String sql2 = "select section,section_id from ocr_section where grade_id=? and term_id=? and subject_id=?";
					List<Map<String, Object>> list_section = record2list(Db
							.find(sql2, grade_id, term_id, subject_id));
					for (Iterator k = list_section.iterator(); k.hasNext();) {
						Map map_k = (Map) k.next();
						String section = (String) map_k.get("section");
						int section_id = (int) map_k.get("section_id");

						Map m_section = new HashMap();
						m_section.put("section_name", section);
						m_section.put("section_id", section_id);
						List<Map<String, Object>> l_title = new ArrayList<Map<String, Object>>();
						m_section.put("title_list", l_title);

						String sql3 = "select title,title_id from ocr_title where section_id=? and grade_id=? and term_id=? and subject_id=?";
						List<Map<String, Object>> list_title = record2list(Db
								.find(sql3, section_id, grade_id, term_id,
										subject_id));

						for (Iterator l = list_title.iterator(); l.hasNext();) {
							Map map_l = (Map) l.next();
							String title = (String) map_l.get("title");
							int title_id = (int) map_l.get("title_id");

							Map m_title = new HashMap();
							m_title.put("title_name", title);
							m_title.put("title_id", title_id);
							l_title.add(m_title);
						}
						l_section.add(m_section);
					}
					l_term.add(m_term);
				}
				l_subject.add(m_subject);

			}
			l_grade.add(m_grade);
		}
		// try {
		// String path = "E:\\result\\outline.json";
		// File file = new File(path);
		// if (!file.exists())
		// file.createNewFile();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// FileOutputStream out = new FileOutputStream(file); // 如果追加方式用true
		// StringBuffer sb = new StringBuffer();
		// // sb.append("-----------" + sdf.format(new Date()) +
		// // "------------\n");
		// sb.append(JsonUtil.toJson(m_root) + "\n");
		// out.write(sb.toString().getBytes("utf-8"));// 注意需要转换对应的字符集
		// out.close();
		// } catch (IOException ex) {
		// System.out.println(ex.getStackTrace());
		// }
		// return JsonUtil.toJson(m_root);
		return m_root;
	}
	
}
