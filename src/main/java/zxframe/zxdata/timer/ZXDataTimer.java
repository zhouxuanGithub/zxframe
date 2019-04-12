package zxframe.zxdata.timer;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zxframe.config.ZxFrameConfig;
import zxframe.task.TaskRunnable;
import zxframe.task.ThreadResource;
import zxframe.task.Timer;
import zxframe.zxdata.service.ZXDataService;

@Timer
public class ZXDataTimer implements TaskRunnable{
	private Logger logger = LoggerFactory.getLogger(ZXDataTimer.class);  
	@Resource
	private ZXDataService sZXDataService;
	public void run() {
		try {
			Integer t = sZXDataService.getMaxTableCode();
			for (int i = 0; i <= t; i++) {
				sZXDataService.deleteByETime("zxdata"+i);
			}
		} catch (Exception e) {
			logger.error("检查过期数据报错",e);
		}
	}
	
	public void init() {
		if(ZxFrameConfig.useZXData) {
			int initialDelay=30;
			ThreadResource.getTaskPool().scheduleWithFixedDelay(this,initialDelay,initialDelay,TimeUnit.SECONDS);
		}
	}
}
