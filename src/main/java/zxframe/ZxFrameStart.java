package zxframe;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import zxframe.cache.redis.RedisCacheManager;
import zxframe.config.ZxFrameConfig;
import zxframe.jpa.datasource.DataManager;
import zxframe.jpa.datasource.DataSourceManager;
import zxframe.task.TimerManager;

@Component
public class ZxFrameStart {
	public void start(ApplicationContext applicationContext) {
		//加载xml配置
		ZxFrameConfig.loadZxFrameConfig();
		//redis加载
		RedisCacheManager.init();
		//数据库加载
		DataSourceManager.init();
		//类扫描，加载需要的模型
		ScanningClass.init();
		//数据库表加载
		DataManager.init();
		//timer加载
		TimerManager.init();
	}
}
