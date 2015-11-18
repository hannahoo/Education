package com.e9w.ocr.serviceevent;


public class ServiceEventFactory {

	//	public static ServiceEvent createBalanceEvent(int userId, int currencyType,
	//			int type, int balance, String description, int causeId,
	//			int causeType) {
	//
	//		return new BalanceEvent(userId, currencyType, type, balance,
	//				description, causeId, causeType);
	//	}
	//
	//	public static ServiceEvent createBalanceEvent(int userId,
	//			Map<String, Object> rewardParamter, int causeId, int causeType) {
	//		Parameter p = new Parameter(rewardParamter);
	//		int currencyType = p.i("currency_type");
	//		int reward = p.i("reward");
	//		String description = TemplateUtil.process(rewardParamter,
	//				"reward_template");
	//		int type = 1;
	//		return createBalanceEvent(userId, currencyType, type, reward,
	//				description, causeId, causeType);
	//
	//	}
	//
	//	public static ServiceEvent createPustEvent(int userId, String message,
	//			int type) {
	//		return new PushEvent(userId, message, type);
	//	}
	//
	//	public static ServiceEvent createPustEvent(int userId, String message) {
	//		return createPustEvent(userId, message, 1);
	//	}
	//
	//	public static ServiceEvent createTaskEvent(int userId, String actionValue,
	//			String actionName, String actionService, int times) {
	//
	//		return new TaskEvent(userId, actionValue, actionName, actionService,
	//				times);
	//	}
	//
	//	public static ServiceEvent createTaskEvent(int userId, Object actionValue,
	//			String actionName, Class<? extends Service> clazz) {
	//		String actionValue2 = actionValue != null ? actionValue.toString()
	//				: StringUtil.EMPTY_STRING;
	//		return createTaskEvent(userId, "" + actionValue, actionValue2,
	//				clazz.getSimpleName(), 1);
	//	}
}
