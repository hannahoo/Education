package com.e9w.ocr.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

import com.e9w.ocr.helper.SearchEngineHelper;
import com.e9w.ocr.serviceprovider.ServiceProviderManager;
import com.e9w.ocr.serviceprovider.searchengine.SearchEngine;
import com.e9w.ocr.util.Env;
import com.e9w.ocr.util.WorkingResourceUtil;

public class Startup {
	final static Logger logger = LoggerFactory.getLogger(Startup.class);

	public static void start() {

		Env.load();
		try {
			reloadLogConfig();
		} catch (Exception e) {

			throw new RuntimeException("初始化日志配置异常", e);
		}
		logger.info("start ... ");

		logger.info("start loadServiceProviders ");
		ServiceProviderManager.loadServiceProviders();

		logger.info("init searchEgine ");
		SearchEngine searchEgine = SearchEngineHelper.getDefaultSearchEngine();
		if (searchEgine == null) {
			throw new RuntimeException("不存在的默认搜索引擎");
		}
		searchEgine.setIndexPath(Env.getSearchIndexDir());
	}

	public static void stop() {
		logger.info("stop ... ");
	}

	public static void reloadLogConfig() throws Exception {
		LoggerContext context = (LoggerContext) LoggerFactory
				.getILoggerFactory();

		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(context);

		context.reset();
		configurator.doConfigure(WorkingResourceUtil.getFile("logback.xml"));
	}
}
