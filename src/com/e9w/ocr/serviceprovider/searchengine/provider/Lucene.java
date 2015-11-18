package com.e9w.ocr.serviceprovider.searchengine.provider;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.e9w.ocr.constant.ErrorCode;
import com.e9w.ocr.serviceprovider.ServiceProviderException;
import com.e9w.ocr.serviceprovider.searchengine.IndexBuilder;
import com.e9w.ocr.serviceprovider.searchengine.SearchContent;
import com.e9w.ocr.serviceprovider.searchengine.SearchEngine;
import com.e9w.ocr.serviceprovider.searchengine.SearchResult;
import com.e9w.ocr.util.IOUtils;
import com.e9w.ocr.util.Md5Util;

public class Lucene extends SearchEngine implements IndexBuilder {

	private static int provider_id = 1;

	final static Logger logger = LoggerFactory.getLogger(SearchEngine.class);
	final static String last_index_file = "last_index_dir.txt";

	static class ParserSearcher {

		public ParserSearcher(QueryParser parser, IndexSearcher searcher) {
			super();
			this.parser = parser;
			this.searcher = searcher;
		}

		QueryParser parser;
		IndexSearcher searcher;
	}

	private static volatile ParserSearcher parserSearcher;
	private static Object lock = new Object();

	@Override
	public int getProviderId() {
		return provider_id;
	}

	protected File getLastIndexDir() {
		File lastIndexFile = new File(indexPath, last_index_file);
		FileReader fr = null;
		File indexDirFile = null;

		try {
			fr = new FileReader(lastIndexFile);
			char[] md5 = new char[1024];
			int readsize = fr.read(md5);
			indexDirFile = new File(indexPath, new String(md5, 0, readsize));
			if (!indexDirFile.exists() || !indexDirFile.isDirectory()) {
				indexDirFile = null;
			}

		} catch (Exception e) {
			logger.error("读取属性异常", e);

		} finally {
			IOUtils.closeQuietly(fr);
		}

		return indexDirFile;
	}

	protected void createSearcher(File indexDirFile)
			throws ServiceProviderException {

		Path p1 = Paths.get(indexDirFile.getAbsolutePath());
		Directory indexDir;
		try {
			indexDir = FSDirectory.open(p1);
		} catch (IOException e) {
			throw new ServiceProviderException(this.getClass().getSimpleName(),
					ErrorCode.IO_EXCEPTION, "访问目录异常," + p1, e);
		}

		QueryParser parser = new QueryParser("content", new IKAnalyzer());
		IndexSearcher searcher;
		try {
			searcher = new IndexSearcher(DirectoryReader.open(indexDir));
		} catch (IOException e) {
			throw new ServiceProviderException(this.getClass().getSimpleName(),
					ErrorCode.IO_EXCEPTION, "访问目录异常," + indexDirFile, e);
		}

		parserSearcher = new ParserSearcher(parser, searcher);

	}

	public List<SearchResult> search(String content, int maxItems)
			throws ServiceProviderException {
		if (parserSearcher == null) {
			File indexDirFile = getLastIndexDir();
			if (indexDirFile == null) {
				throw new ServiceProviderException(this.getClass()
						.getSimpleName(), ErrorCode.ILLEGAL_STATE,
						"无内容可搜索,请先添加需要搜索的内容");
			}
			synchronized (lock) {
				if (parserSearcher == null) {
					createSearcher(indexDirFile);
				}
			}

		}

		Query query;
		try {
			query = parserSearcher.parser.parse(content);
		} catch (ParseException e) {
			throw new ServiceProviderException(this.getClass().getSimpleName(),
					ErrorCode.INTERNAL_EXCEPTION, "解析异常", e);
		}
		TopDocs topDocs;
		try {
			topDocs = parserSearcher.searcher.search(query, maxItems);
		} catch (Exception e) {
			throw new ServiceProviderException(this.getClass().getSimpleName(),
					ErrorCode.INTERNAL_EXCEPTION, "搜索异常", e);
		}

		List<SearchResult> list = new ArrayList<SearchResult>();
		ScoreDoc[] hits = topDocs.scoreDocs;
		logger.debug("Results found: " + topDocs.totalHits);
		for (int i = 0; i < hits.length; i++) {

			try {
				Document doc = parserSearcher.searcher.doc(hits[i].doc);
				logger.debug(doc.get("id") + " " + doc.get("description")
						+ " (" + hits[i].score + ")");
				long id = Long.parseLong(doc.get("id"));
				float score = hits[i].score;
				SearchResult searchResult = new SearchResult(id, score);
				list.add(searchResult);
			} catch (Throwable e) {
				logger.error("create searchResult ", e);
			}

		}
		return list;

	}

	IndexWriter indexWriter = null;

	@Override
	public boolean hasSearchContent() {
		return getLastIndexDir() != null;
	}

	@Override
	public IndexBuilder getIndexBuilder(String indexId) {

		//return null;
		IndexBuilder indexBuilder = new Lucene();
		return indexBuilder;
	}

	@Override
	public void close() throws Exception {
		IOUtils.closeQuietly(indexWriter);
	}

	@Override
	public void setContents(List<SearchContent> contents)
			throws ServiceProviderException {
		for (SearchContent content : contents) {
			setContent(content);
		}
	}

	@Override
	public void setContent(SearchContent content)
			throws ServiceProviderException {
		Document doc = new Document();
		doc.add(new LongField("id", content.getId(), Field.Store.YES));
		doc.add(new TextField("content", content.getContent(),// textField是一大块需要经过分词的文本,StringField是一个不需要分词，而直接用于索引的字符串
				Field.Store.YES));
		try {
			indexWriter.addDocument(doc);
		} catch (IOException e) {

			logger.error("add doc exception doc:id=" + doc.get("id"), e);
			throw new ServiceProviderException(this.getClass().getSimpleName(),
					ErrorCode.IO_EXCEPTION, "添加索引异常", e);
		}
	}

	@Override
	public void open(String id) throws ServiceProviderException {
		String md5 = Md5Util.digest(id);
		File dir = new File(indexPath, md5);
		if (dir.exists()) {
			// dir.mkdirs();

			return;
		} else {
			dir.mkdirs();
		}
		Path p1 = Paths.get(dir.getAbsolutePath());
		Directory indexDir;
		try {
			indexDir = FSDirectory.open(p1);
		} catch (IOException e) {
			throw new ServiceProviderException(this.getClass().getSimpleName(),
					ErrorCode.IO_EXCEPTION, "访问目录异常," + p1, e);
		}

		// IndexWriter indexWriter = null;

		try {

			IndexWriterConfig config = new IndexWriterConfig(new IKAnalyzer());
			indexWriter = new IndexWriter(indexDir, config);

		} catch (IOException e) {
			throw new ServiceProviderException(this.getClass().getSimpleName(),
					ErrorCode.IO_EXCEPTION, "创建索引异常", e);
		} finally {
			// IOUtils.closeQuietly(indexWriter);
		}

		FileWriter fw = null;
		File lastIndexFile = new File(indexPath, last_index_file);
		try {
			fw = new FileWriter(lastIndexFile);
			fw.write(md5);
		} catch (IOException e) {
			throw new ServiceProviderException(this.getClass().getSimpleName(),
					ErrorCode.IO_EXCEPTION, "写入文件异常,"
							+ lastIndexFile.getAbsolutePath(), e);
		} finally {
			IOUtils.closeQuietly(fw);
		}
	}

}
