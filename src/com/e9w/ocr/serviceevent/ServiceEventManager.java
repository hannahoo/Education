package com.e9w.ocr.serviceevent;

import com.e9w.ocr.jfinal.plug.event.EventPlugin;

public class ServiceEventManager {

	private static EventPlugin eventPlugin;

	public static void init(EventPlugin eventPlugin) {
		ServiceEventManager.eventPlugin = eventPlugin;
	}

	//	public static void postPushEvent(int userId, String message, int type) {
	//		eventPlugin.addEvent(new PushEvent(userId, message, type));
	//	}
	//
	//	public static void postPushEvent(int userId, String message) {
	//		postPushEvent(userId, message, 1);
	//	}
	//
	//	public static void postBalanceEvent(int userId, int currencyType, int type,
	//			int balance, String description, int causeId, int causeType) {
	//		eventPlugin.addEvent(new BalanceEvent(userId, currencyType, type,
	//				balance, description, causeId, causeType));
	//	}
	//
	//	public static void postBalanceEvent(int userId,
	//			Map<String, Object> rewardParamter, int causeId, int causeType) {
	//		Parameter p = new Parameter(rewardParamter);
	//		int currencyType = p.i("currency_type");
	//		int reward = p.i("reward");
	//		String description = TemplateUtil.process(rewardParamter,
	//				"reward_template");
	//		int type = 1;
	//		eventPlugin.addEvent(new BalanceEvent(userId, currencyType, type,
	//				reward, description, causeId, causeType));
	//	}
	//
	//	public static void postTaskEvent(int userId, String actionValue,
	//			String actionName, String actionService, int times) {
	//		eventPlugin.addEvent(new TaskEvent(userId, actionValue, actionName,
	//				actionService, times));
	//		
	//	}
	public static void postEvent(ServiceEvent event) {
		eventPlugin.addEvent(event);

	}

}
