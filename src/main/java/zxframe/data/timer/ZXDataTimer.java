/**
 * ZxFrame Java Library
 * https://github.com/zhouxuanGithub/zxframe
 *
 * Copyright (c) 2019 zhouxuan
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package zxframe.data.timer;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zxframe.config.ZxFrameConfig;
import zxframe.task.TaskRunnable;
import zxframe.task.ThreadResource;
import zxframe.task.Timer;
import zxframe.util.DateUtil;
import zxframe.data.service.ZXDataManager;

@Timer
public class ZXDataTimer implements TaskRunnable{
	private Logger logger = LoggerFactory.getLogger(ZXDataTimer.class);  
	@Resource
	private ZXDataManager sZXDataService;
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
			int start=DateUtil.getDiffOfSecond(3,59,59);
			//int start=30;
			int initialDelay=60*60*24;
			ThreadResource.getTaskPool().scheduleWithFixedDelay(this,start,initialDelay,TimeUnit.SECONDS);
		}
	}
}