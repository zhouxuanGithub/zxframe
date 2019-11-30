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
package zxframe.properties.timer;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import zxframe.config.ZxFrameConfig;
import zxframe.properties.PropertiesCache;
import zxframe.properties.service.PropertiesService;
import zxframe.task.TaskRunnable;
import zxframe.task.ThreadResource;
import zxframe.task.Timer;
@Timer
public class PropertiesTimer implements TaskRunnable{
	@Resource
	private PropertiesService propertiesService;
	public void run() {
		String version=PropertiesCache.get("system-version");
		String cversion=propertiesService.getListVersion();
		if(!cversion.equals(version)) {
			PropertiesCache.init();
		}
	}
	public void init() {
		if(ZxFrameConfig.useDBProperties) {
			long delay = 60;// 间隔多少秒执行一次
			ThreadResource.getTaskPool().scheduleWithFixedDelay(this, delay,
							delay, TimeUnit.SECONDS);
		}
	}
}
