package com.e9w.ocr.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.e9w.ocr.controller.render.datarender.PageList;
import com.e9w.ocr.lang.Parameter;
import com.e9w.ocr.serviceevent.ServiceEvent;
import com.e9w.ocr.serviceevent.ServiceEventManager;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public abstract class Service {

	protected static final int cause_type_activation = 1; //razor_product_client_activation	应用激活
	protected static final int cause_type_download = 2; //razor_product_download	应用下载
	protected static final int cause_type_browse = 3; //razor_user_share_browse_reward_instance	活动页面浏览
	protected static final int cause_type_payment = 4; //razor_payment_order	支付额外奖励
	protected static final int cause_type_retention = 5; //razor_product_client_retention	应用留存
	protected static final int cause_type_mail = 6; //razor_user_mail_receipt	邮件奖励
	protected static final int cause_type_task = 7; //razor_user_task_reward_instance	任务奖励
	protected static final int cause_type_share = 8; //razor_user_share_reward_instance	页面分享
	protected static final int cause_type_signin = 9; //razor_user_signin_reward_instance	签到

	Service() {

	}

	public Class<?> getActualClass() {

		Class<?> clazz = this.getClass();
		while (clazz.getSimpleName().contains("EnhancerByCGLIB")) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass == null) {
				break;
			}
			clazz = superClass;

		}
		return clazz;
	}

	public String getServiceName() {
		return getActualClass().getSimpleName();
	}

	protected void postEvent(ServiceEvent event) {
		ServiceEventManager.postEvent(event);
	}

	protected void throwException(int code, String message)
			throws ServiceException {

		throw new ServiceException(this.getServiceName(), code, message);
	}

	protected Map<String, Object> record2map(Record record) {
		if (record == null) {
			return null;
		}
		Map<String, Object> m = record.getColumns();
		return m;
	}

	protected Parameter record2param(Record record) {
		if (record == null) {
			return null;
		}
		Map<String, Object> m = record.getColumns();
		return new Parameter(m);
	}

	protected List<Map<String, Object>> record2list(List<Record> list) {

		List<Map<String, Object>> result = new ArrayList<>();
		if (list != null && list.size() > 0) {
			for (Record r : list) {
				result.add(record2map(r));
			}
		}
		return result;
	}

	protected <T> Map<T, Map<String, Object>> list2map(List<Record> list,
			String keyField) {
		Map<T, Map<String, Object>> result = new LinkedHashMap<T, Map<String, Object>>();
		//List<Map<String, Object>> list = new ArrayList<>();
		if (list != null && list.size() > 0) {
			for (Record r : list) {
				Map<String, Object> m = record2map(r);
				T key = (T) m.get(keyField);
				if (key == null) {
					continue;
				}
				result.put(key, m);
			}
		}
		return result;
	}

	public PageList<Map<String, Object>> page2PageList(Page<Record> page) {
		PageList<Map<String, Object>> result = new PageList<>(
				record2list(page.getList()), page.getTotalRow(),
				page.getPageNumber(), page.getPageSize());
		return result;
	}

	protected String processTemplate(String template, Record record) {
		return processTemplate(template, record2map(record));
	}

	protected String processTemplate(Record record, String templatefield) {
		String template = record.getStr(templatefield);
		if (template != null) {
			Map<String, Object> map = record2map(record);
			map.remove(templatefield);
			template = processTemplate(template, map);
		}
		return template;
	}

	protected String processTemplate(String template, Map<String, Object> map) {
		String result = template;
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String variantName = "{" + entry.getKey() + "}";
				String value = "" + entry.getValue();
				result = result.replace(variantName, value);
			}
		}
		return result;
	}
}
