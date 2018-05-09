package zxframe.util;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import zxframe.ZxFrameStart;

/**
 * @preserve
 * @author zhoux
 */
@Component
public class ServiceLocator implements ApplicationContextAware {
	@Resource
	private ZxFrameStart ZxFrameStart;
	private static ApplicationContext applicationContext;
	private static Map<String, Object> cacheBeans = new HashMap<String, Object>();
	
	
	/**
	 * @preserve
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		ServiceLocator.applicationContext = applicationContext;
		ZxFrameStart.start(applicationContext);
	}
	/**
	 * @preserve
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public static <T> T getSpringBean(String beanId) {
		T bean = (T)cacheBeans.get(beanId);
		if (bean != null) {
			return bean;
		}
		bean = (T)getApplicationContext().getBean(beanId);
		if (bean != null) {
			cacheBeans.put(beanId, bean);
		}
		return bean;
	}
}
