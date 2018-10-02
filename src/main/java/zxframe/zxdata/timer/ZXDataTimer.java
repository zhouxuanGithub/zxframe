package zxframe.zxdata.timer;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zxframe.config.ZxFrameConfig;
import zxframe.jpa.dao.BaseDao;
import zxframe.task.TaskRunnable;
import zxframe.task.ThreadResource;
import zxframe.task.Timer;
import zxframe.zxdata.mapper.ZXDataMapper;
import zxframe.zxdata.service.ZXDataService;

@Timer
public class ZXDataTimer implements TaskRunnable{
	private Logger logger = LoggerFactory.getLogger(ZXDataTimer.class);  
	@Resource
	private ZXDataService sZXDataService;
	@Resource
	private BaseDao baseDao;
	public void run() {
		int tableCode=0;
		while(true) {
			try {
				Integer t = (Integer)baseDao.get(ZXDataMapper.selectByG2T,-1);
				t = t/sZXDataService.grooveCount;
				if(tableCode>=t) {
					tableCode=0;
				}else {
					tableCode++;
				}
				String table="zxdata"+tableCode;
				sZXDataService.deleteByETime(table);
			} catch (Exception e) {
				logger.error("检查过期数据报错",e);
			}
		}
	}
	
	public void init() {
		if(ZxFrameConfig.useZXData) {
			int initialDelay=1000*30;
			ThreadResource.getTaskPool().schedule(this,initialDelay,TimeUnit.SECONDS);
		}
	}
}
