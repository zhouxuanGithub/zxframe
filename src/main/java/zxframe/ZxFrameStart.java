package zxframe;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import zxframe.cache.redis.RedisCacheManager;
import zxframe.config.ZxFrameConfig;
import zxframe.jpa.datasource.DataSourceManager;
import zxframe.util.ScanningClass;

@Component
public class ZxFrameStart {
	public void start(ApplicationContext applicationContext) {
		//类扫描，加载需要的模型
		ScanningClass.init();
		//加载xml配置
		ZxFrameConfig.loadZxFrameConfig();
		//redis加载
		RedisCacheManager.init();
		//数据库加载
		DataSourceManager.init();
		//定期检测读库熔断
		// TODO: 
		//定期检测泄漏事务存储
		// TODO: 
		//定期检测数据源配置，热更新
		// TODO:
	}
}
