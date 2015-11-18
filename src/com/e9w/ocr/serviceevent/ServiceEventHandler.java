package com.e9w.ocr.serviceevent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.e9w.ocr.jfinal.plug.event.Event;
import com.e9w.ocr.jfinal.plug.event.EventHandler;
import com.jfinal.plugin.activerecord.Record;

public abstract class ServiceEventHandler implements EventHandler {

	protected static final int cause_type_activation = 1; //razor_product_client_activation	应用激活
	protected static final int cause_type_download = 2; //razor_product_download	应用下载
	protected static final int cause_type_browse = 3; //razor_user_share_browse_reward_instance	活动页面浏览
	protected static final int cause_type_payment = 4; //razor_payment_order	支付额外奖励
	protected static final int cause_type_retention = 5; //razor_product_client_retention	应用留存
	protected static final int cause_type_mail = 6; //razor_user_mail_receipt	邮件奖励
	protected static final int cause_type_task = 7; //razor_user_task_reward_instance	任务奖励
	protected static final int cause_type_share = 8; //razor_user_share_reward_instance	页面分享
	protected static final int cause_type_signin = 9; //razor_user_signin_reward_instance	签到

	final static org.slf4j.Logger logger = LoggerFactory
			.getLogger(ServiceEventHandler.class);

	@Override
	public void handle(Event event) {

		try {

			handle((ServiceEvent) event);
		} catch (Throwable e) {
			logger.error("ServiceEventHandler exception ", e);
		}

	}

	protected abstract void handle(ServiceEvent serviceEvent)
			throws ServiceEventException;

	protected Map<String, Object> record2Map(Record record) {
		if (record == null) {
			return null;
		}
		Map<String, Object> m = record.getColumns();
		return m;
	}

	protected List<Map<String, Object>> record2Map(List<Record> list) {

		List<Map<String, Object>> result = new ArrayList<>();
		if (list != null && list.size() > 0) {
			for (Record r : list) {
				result.add(record2Map(r));
			}
		}
		return result;
	}

	//	public PageList<Map<String, Object>> page2PageList(Page<Record> page) {
	//		PageList<Map<String, Object>> result = new PageList<>(
	//				record2Map(page.getList()), page.getTotalRow(),
	//				page.getPageNumber(), page.getPageSize());
	//		return result;
	//	}
	//
	//	protected String processTemplate(String template, Record record) {
	//		return processTemplate(template, record2Map(record));
	//	}
	//
	//	protected String processTemplate(Record record, String templatefield) {
	//		String template = record.getStr(templatefield);
	//		if (template != null) {
	//			Map<String, Object> map = record2Map(record);
	//			map.remove(templatefield);
	//			template = processTemplate(template, map);
	//		}
	//		return template;
	//	}
	//
	//	protected String processTemplate(String template, Map<String, Object> map) {
	//		String result = template;
	//		if (map != null) {
	//			for (Map.Entry<String, Object> entry : map.entrySet()) {
	//				String variantName = "{" + entry.getKey() + "}";
	//				String value = "" + entry.getValue();
	//				result = result.replace(variantName, value);
	//			}
	//		}
	//		return result;
	//
	//	}
	//
	//	protected Parameter record2Parameter(Record record) {
	//		if (record == null) {
	//			return null;
	//		}
	//		Map<String, Object> m = record.getColumns();
	//		return new Parameter(m);
	//	}

}
