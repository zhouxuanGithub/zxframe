package zxframe;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import zxframe.cache.redis.RedisCacheManager;
import zxframe.config.ZxFrameConfig;
import zxframe.jpa.datasource.DataManager;
import zxframe.jpa.datasource.DataSourceManager;
import zxframe.jpa.util.ScanningClass;
import zxframe.task.TimerManager;

@Component
public class ZxFrameStart {
	public void start(ApplicationContext applicationContext) {
		//加载xml配置
		ZxFrameConfig.loadZxFrameConfig();
		//类扫描，加载需要的模型
		ScanningClass.init();
		//加载mapper配置文件
		ZxFrameConfig.loadZxMapperConfig();
		//redis加载
		RedisCacheManager.init();
		//数据库加载
		DataSourceManager.init();
		//数据库表加载
		DataManager.init();
		//timer加载
		TimerManager.init();
	}
}
