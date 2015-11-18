package com.e9w.ocr.util;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import com.e9w.ocr.lang.Parameter;

public class Env {

	private static Charset charset;
	private static int timeout = -1;
	private static int cacheProviderId = -1;

	private static int fileStorageProviderId = -1;

	private static String fileUploadDir;
	private static String fileCacheDir;

	private static int fileUploadSize = -1;

	private static String searchIndexDir;

	private static Parameter parameter;

	private static String shell;
	// private static String shellScriptFile;

	private static String managementPassword;

	// private static String promotionFileWorkDir;

	private static long cacheTimeOut = -1;

	private static int defaultSearchEngineProvider = -1;

	private static int defaultRecognizeProvider = -1;

	private static String host;

	private static String indexUrl;

	private static String outlinePath;

	public static int getDefaultSearchEngineProvider() {
		if (defaultSearchEngineProvider == -1) {
			defaultSearchEngineProvider = parameter.i(
					"default_search_engine_provider", 1);
		}
		return defaultSearchEngineProvider;
	}

	public static int getDefaultRecognizeProvider() {
		if (defaultRecognizeProvider == -1) {
			defaultRecognizeProvider = parameter.i(
					"default_recognize_provider", 2);
		}
		return defaultRecognizeProvider;
	}

	public static String getFileCacheDir() {
		if (fileCacheDir == null) {
			fileCacheDir = parameter.s("file_cache_dir", "/web/cache");
			File file = new File(fileCacheDir);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		return fileCacheDir;
	}

	public static String getManagementPassword() {
		if (managementPassword == null) {
			managementPassword = parameter.s("management_password",
					"12345qwert");

		}
		return managementPassword;
	}

	public static String getShell() {
		if (shell == null) {
			shell = parameter.s("shell", "/usr/bin/bash");

		}
		return shell;
	}

	public static String getSearchIndexDir() {
		if (searchIndexDir == null) {
			searchIndexDir = parameter.s("search_index_dir",
					"/web/search_index");
			File file = new File(searchIndexDir);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		return searchIndexDir;
	}

	public static Charset getCharset() {
		if (charset == null) {
			charset = Charset.forName(parameter.s("charset", "UTF-8"));
		}
		return charset;
	}

	public static int getTimeout() {
		if (timeout == -1) {
			timeout = parameter.i("timeout", 3000);
		}
		return timeout;
	}

	public static long getCacheTimeout() {
		if (cacheTimeOut == -1) {
			cacheTimeOut = parameter.i("cache_timeout", 600);
		}
		return cacheTimeOut;
	}

	public static int getCacheProviderId() {
		if (cacheProviderId == -1) {
			cacheProviderId = parameter.i("cache_provider_id", 2);
		}
		return cacheProviderId;
	}

	public static int getFileStorageProviderId() {
		if (fileStorageProviderId == -1) {
			fileStorageProviderId = parameter.i("file_storage_providerid", 1);
		}
		return fileStorageProviderId;
	}

	public static String getFileUploadDir() {
		if (fileUploadDir == null) {
			fileUploadDir = parameter.s("file_upload_dir", "/web/upload");
			File file = new File(fileUploadDir);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		return fileUploadDir;
	}

	public static int getFileUploadSize() {
		if (fileUploadSize == -1) {
			fileUploadSize = parameter.i("file_upload_size", 4000000);
		}
		return fileUploadSize;
	}

	private static void closeQuietly(Closeable c) {

		if (c != null) {
			try {
				c.close();
			} catch (Exception e) {
			}
		}
	}

	public static void load() {
		init();
	}

	public static void init() {

		InputStream is = null;
		try {
			is = WorkingResourceUtil.getInputStream("env.properties");
			Properties properties = new Properties();

			properties.load(is);

			parameter = new Parameter(properties);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			closeQuietly(is);
		}

	}

	public static String getHost() {
		if (host == null) {
			host = parameter.s("host", "http://localhost:8080");

		}
		return host;
	}

	public static String getIndexUrl() {
		if (indexUrl == null) {
			indexUrl = parameter.s("indexUrl",
					"jdbc:mysql://192.169.1.3:3306/ocr?user=ocr&password=ocr");

		}
		return indexUrl;
	}

	public static String getOutlinePath() {
		if (outlinePath == null) {
			outlinePath = parameter.s("outlinePath",
					"E:\\j2eeWorkspace\\ocr\\WebContent\\html\\outline.json");

		}
		return outlinePath;
	}
}
