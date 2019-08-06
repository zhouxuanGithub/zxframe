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
package zxframe.jpa.datasource;

import zxframe.config.ZxFrameConfig;
import zxframe.properties.service.PropertiesService;
import zxframe.util.ServiceLocator;

public class DataManager {
	//初始化数据
	public static void init() {
		if(ZxFrameConfig.useDBProperties) {
			((PropertiesService)ServiceLocator.getSpringBean("propertiesService")).initPropertiesTable();
		}
	}
}
