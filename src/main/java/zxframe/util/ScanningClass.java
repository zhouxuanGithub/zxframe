package zxframe.util;

import java.util.Map;

import org.springframework.stereotype.Component;

import zxframe.cache.mgr.CacheModelManager;
import zxframe.jpa.annotation.DataMapper;
import zxframe.jpa.annotation.Model;
import zxframe.task.TaskRunnable;
import zxframe.task.Timer;


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
				if(clazz.isAnnotationPresent(Model.class)||clazz.isAnnotationPresent(DataMapper.class)){
					CacheModelManager.loadDataModelByGroup(clazz.getName());
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
//		    Class<? extends Object>  [] interfaces = clazz.getInterfaces();  
//		    for(Class<? extends Object>  aInterface : interfaces){  
//		        //接口信息 
//		    	System.err.println(aInterface);
//		    }  
		}
		//后续加载部分
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
