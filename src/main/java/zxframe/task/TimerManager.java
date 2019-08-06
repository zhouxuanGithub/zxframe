/**
 * ZxFrame Java Library
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
package zxframe.task;

import java.util.Map;

import zxframe.config.ZxFrameConfig;
import zxframe.util.ServiceLocator;

/**
 * 定时器管理
 * //每间隔1分钟执行一次RankingTimer
 * ThreadResource.getTaskPool().scheduleWithFixedDelay(this,60*60,60*60,TimeUnit.SECONDS);
 * //每天定点执行某个任务(每晚23点59分59秒执行RankingTimer)
 * ThreadResource.getTaskPool().scheduleWithFixedDelay(this,DateUtil.getDiffOfSecond(23,59,59),60*60*24,TimeUnit.SECONDS);
 * //每周某星期定点执行某个任务(每星期天晚23点59分59秒执行RankingTimer)
 * ThreadResource.getTaskPool().scheduleWithFixedDelay(this,DateUtil.getDiffOfSecond(7,23,59,59),60*60*24*7,TimeUnit.SECONDS);
 * @author yp
 *
 */
public class TimerManager {
	public static void init() {
		if(ZxFrameConfig.useZXTask==false) {
			return;
		}
		Map<String, Object> beansWithAnnotationMap = ServiceLocator.getApplicationContext().getBeansWithAnnotation(org.springframework.context.annotation.Primary.class);  
		Class<? extends Object> clazz = null;
		for(Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()){  
			Object bean = entry.getValue();
		    clazz = bean.getClass();//获取到实例对象的class信息  
			try {
				if(clazz.isInterface()){
					continue;
				}
				if(clazz.isAnnotationPresent(Timer.class)){
					TaskRunnable tr=(TaskRunnable) bean;
					tr.init();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
