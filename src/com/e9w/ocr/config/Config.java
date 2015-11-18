package com.e9w.ocr.config;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.e9w.ocr.controller.ClientController;
import com.e9w.ocr.controller.HomeController;
import com.e9w.ocr.controller.IOSPurchaseController;
import com.e9w.ocr.controller.JSPController;
import com.e9w.ocr.jfinal.plug.event.EventPlugin;
import com.e9w.ocr.serviceevent.ServiceEventManager;
import com.e9w.ocr.startup.Startup;
import com.e9w.ocr.util.WorkingResourceUtil;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IContainerFactory;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.druid.DruidPlugin;

public class Config extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {

	}

	@Override
	public void configRoute(Routes me) {

		me.add("/", HomeController.class);

		me.add("/client", ClientController.class);

		me.add("/ios", IOSPurchaseController.class);
		
		me.add("/jsp", JSPController.class);
	}

	@Override
	public void configPlugin(Plugins me) {

//		C3p0Plugin c3p0Plugin = new C3p0Plugin(
//				WorkingResourceUtil.getFile("jdbc.properties"));
//		me.add(c3p0Plugin);
		Map<String, String> config = WorkingResourceUtil
				.loadPropertFile("jdbc.properties");
		DruidPlugin druidPlugin = new DruidPlugin(config.get("jdbcUrl"),
				config.get("user"), config.get("password"),
				config.get("driverClass"), "stat");
		me.add(druidPlugin);

		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
//		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.setContainerFactory(new IContainerFactory() {

			public Map<String, Object> getAttrsMap() {
				return new LinkedHashMap<String, Object>();
			}

			public Map<String, Object> getColumnsMap() {
				return new LinkedHashMap<String, Object>();
			}

			public Set<String> getModifyFlagSet() {
				return new LinkedHashSet<String>();
			}
		});
		me.add(arp);

		EventPlugin eventPlugin = new EventPlugin(3);

		ServiceEventManager.init(eventPlugin);
		me.add(eventPlugin);

	}

	@Override
	public void afterJFinalStart() {
		// TODO Auto-generated method stub
		super.afterJFinalStart();
		Startup.start();
	}

	@Override
	public void configInterceptor(Interceptors me) {

	}

	@Override
	public void configHandler(Handlers me) {

	}

	@Override
	public void beforeJFinalStop() {
		Startup.stop();
		super.beforeJFinalStop();
	}

}
