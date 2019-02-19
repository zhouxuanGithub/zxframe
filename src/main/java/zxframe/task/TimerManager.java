package zxframe.task;

import java.util.Map;

import zxframe.config.ZxFrameConfig;
import zxframe.util.ServiceLocator;

public class TimerManager {
	public static void init() {
		if(ZxFrameConfig.useZXTask==false) {
			return;
		}
		Map<String, Object> beansWithAnnotationMap = ServiceLocator.getApplicationContext().getBeansWithAnnotation(org.springframework.context.annotation.Primary.class);  
		Class<? extends Object> clazz = null;
		for(Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()){  
			Object bean = entry.getValue();
		    clazz = bean.getClass();//获取到实例对象的class信息  
			try {
				if(clazz.isInterface()){
					continue;
				}
				if(clazz.isAnnotationPresent(Timer.class)){
					TaskRunnable tr=(TaskRunnable) bean;
					tr.init();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
