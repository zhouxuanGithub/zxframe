package zxframe.properties.timer;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import zxframe.properties.PropertiesCache;
import zxframe.properties.service.PropertiesService;
import zxframe.task.TaskRunnable;
import zxframe.task.ThreadResource;
import zxframe.util.ServiceLocator;
@Component
public class PropertiesTimer extends TaskRunnable{
	private static PropertiesService propertiesService;
	public void run() {
		String version=PropertiesCache.get("system-version");
		if(propertiesService==null) {
			propertiesService = ServiceLocator.getSpringBean("propertiesService");
		}
		String cversion=propertiesService.getListVersion();
		if(!cversion.equals(version)) {
			PropertiesCache.init();
		}
	}
	@PostConstruct
	public void start() {
		long initialDelay = 60*5;// 第一次延迟多少秒开始执行
		long delay = 60;// 间隔多少秒执行一次
		ThreadResource.getTaskPool().scheduleWithFixedDelay(new PropertiesTimer(), initialDelay,
						delay, TimeUnit.SECONDS);
	}

}
