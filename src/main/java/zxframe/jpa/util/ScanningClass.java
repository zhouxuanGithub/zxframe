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
package zxframe.jpa.util;

import java.util.Map;

import org.springframework.stereotype.Component;

import zxframe.cache.mgr.CacheModelManager;
import zxframe.jpa.annotation.DataModelScanning;
import zxframe.jpa.annotation.Model;
import zxframe.util.ServiceLocator;


/**
 * 类扫描，加载需要的模型
 * @preserve
 * @author zhoux
 *
 */
@Component
public class ScanningClass extends ClassLoader {
	public static void init() {
		Map<String, Object> beansWithAnnotationMap = ServiceLocator.getApplicationContext().getBeansWithAnnotation(org.springframework.context.annotation.Primary.class);  
		Class<? extends Object> clazz = null;
		//优先加入模型类
		for(Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()){  
			Object bean = entry.getValue();
		    clazz = bean.getClass();//获取到实例对象的class信息  
			try {
				if(clazz.isInterface()){
					continue;
				}
				if(clazz.isAnnotationPresent(Model.class)||clazz.isAnnotationPresent(DataModelScanning.class)){
					CacheModelManager.loadDataModelByGroup(clazz.getName());
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		//读取mapper文件
		
	}
}
