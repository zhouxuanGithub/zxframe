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
package zxframe.cache.model;

import zxframe.config.ZxFrameConfig;
import zxframe.jpa.annotation.DataModelScanning;
import zxframe.jpa.model.DataModel;

@DataModelScanning
public class DefaultCacheDataModel {
	public final static String zxframeDefaultCacheModelGroup="zxframe-cache-default";
	public DataModel initZxframeDefaultCacheModel() {
		//如果远程缓存开启，则优先使用远程缓存
		//否则使用本地缓存
		//远程和本地二选一
		DataModel cm =new DataModel();
		cm.setGroup(zxframeDefaultCacheModelGroup);
		cm.setLcCache(ZxFrameConfig.ropen==true?false:true);
		cm.setRcCache(ZxFrameConfig.ropen);
		return cm;
	}
}
