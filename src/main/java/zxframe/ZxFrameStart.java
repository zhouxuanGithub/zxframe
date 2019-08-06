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
