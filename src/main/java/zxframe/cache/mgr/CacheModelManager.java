package zxframe.cache.mgr;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import zxframe.cache.annotation.Cache;
import zxframe.jpa.model.DataModel;
import zxframe.jpa.annotation.DataModelScanning;
import zxframe.jpa.annotation.Id;
import zxframe.jpa.annotation.Model;
import zxframe.jpa.annotation.Transient;
import zxframe.jpa.annotation.Version;
import zxframe.jpa.ex.JpaRuntimeException;

/**
 * 缓存模型管理
 * @author zx
 *
 */
@Component
public class CacheModelManager {
	private static Logger logger = LoggerFactory.getLogger(CacheModelManager.class);  
	//缓存模型
	private static ConcurrentMap<String, DataModel> cacheModelMap=new ConcurrentHashMap<String, DataModel>();
	//未开启缓存的模型
	private static ConcurrentMap<String, Boolean> isNoHasDataModelMap=new ConcurrentHashMap<String, Boolean>();
	//模型主键字段
	public static ConcurrentMap<String, Field> cacheIdFieldMap=new ConcurrentHashMap<String, Field>();
	//模型id字段
	public static ConcurrentMap<String, Id> cacheIdAnnotation=new ConcurrentHashMap<String, Id>();
	//模型可用字段
	public static ConcurrentMap<String, ConcurrentHashMap<String,Field>> cacheFieldsMap=new ConcurrentHashMap<String, ConcurrentHashMap<String,Field>>();
	//模型版本字段
	public static ConcurrentMap<String, Field> cacheIdVersionMap=new ConcurrentHashMap<String, Field>();
	//模型注解保存
	public static ConcurrentMap<String, Model> cacheModelAnnotation=new ConcurrentHashMap<String, Model>();
	//模型注解保存-key为简单名称
	public static ConcurrentMap<String, Model> cacheModelJAnnotation=new ConcurrentHashMap<String, Model>();
	/**
	 * 根据Group获取缓存模型获取
	 * @param group
	 * @return
	 */
	public static DataModel getDataModelByGroup(String group) {
		return cacheModelMap.get(group);
	}
	/**
	 * 根据class获取缓存模型获取
	 * @param cls
	 * @return
	 */
	public static DataModel loadDataModelByGroup(String cls) {
		DataModel cm = cacheModelMap.get(cls);
		if(cm!=null) {
			return cm;
		}
		if(isNoHasDataModelMap.containsKey(cls)) {
			return null;
		}
//		else {
//			if(!cls.startsWith("zxframe")) {//并非自身程序的对象
//				isNoHasDataModelMap.put(cls, true);
//				return null;
//			}
//		}
		try {
			cm=new DataModel();
			cm.setGroup(cls);
			//未加载过，尝试获取
			Class clazz = Class.forName(cls);
			ConcurrentHashMap<String,Field> groupflds = new ConcurrentHashMap<String,Field>();
			cacheFieldsMap.put(cls, groupflds);
			//解析类字段上的注解
			Field[] field = clazz.getDeclaredFields();  
	        if(field != null){  
	            for(Field fie : field){  
	            	if(!fie.isAccessible()){  
	                    fie.setAccessible(true);  
	            	}
                    //主键注解
                    Id ida= fie.getAnnotation(Id.class); 
                    if(ida!=null) {
                    	cacheIdAnnotation.put(cls, ida);
                    	cacheIdFieldMap.put(cls, fie);
                    }
                    //model版本注解
                    Version version=fie.getAnnotation(Version.class); 
                    if(version!=null) {
                    	cacheIdVersionMap.put(cls, fie);
                    }
                    //属性无效注解
	            	Transient Transient= fie.getAnnotation(Transient.class);
	            	if(Transient==null) {
	            		groupflds.put(fie.getName(),fie);
	            	}
	            }
	        }
			//解析类上面的注解
			StringBuffer sb=new StringBuffer();
			sb.append("load model >>>> ").append(cls).append(" ");
			if(clazz.isAnnotationPresent(DataModelScanning.class)) {
				sb.append("[mapper]");
				isNoHasDataModelMap.put(cls, true);
			}
			else if(!clazz.isAnnotationPresent(Cache.class)) {
				sb.append("[no open cache]");
				isNoHasDataModelMap.put(cls, true);
			}
			boolean isQC=false;
			for (Annotation anno : clazz.getDeclaredAnnotations()) {//获得所有的注解
				//缓存注解
				if(anno.annotationType().equals(Cache.class) ){
					Cache gce=(Cache)anno;
					cm.setLcCache(gce.lcCache());
					cm.setRcCache(gce.rcCache());
					cm.setRcETime(gce.rcETime());
					cm.setStrictRW(gce.strictRW());
					cm.setQueryCache(false);//单模型不能支持查询缓存
					sb.append(cm.toString());
					cacheModelMap.put(cls, cm);
				}
				if(anno.annotationType().equals(Model.class)){
					cacheModelAnnotation.put(cls,(Model)anno);
					if(cacheModelJAnnotation.get(clazz.getSimpleName().toLowerCase())==null) {
						cacheModelJAnnotation.put(clazz.getSimpleName().toLowerCase(), (Model)anno);
					}else {
						throw new JpaRuntimeException("不能存在两个相同的类名，否则无法区分跨库多数据源，请修改类名和表名。"+clazz.getSimpleName());
					}
				}
				//查询缓存
				if(anno.annotationType().equals(DataModelScanning.class) ){
					isQC=true;
				}
			}
			if(isQC) {
				//如果是查询缓存模型，则执行下里面的方法
				Method[] methods = clazz.getDeclaredMethods();
				if(methods!=null) {
					for (int i = 0; i < methods.length; i++) {
						try {
							Method m=methods[i];
							CacheModelManager.addQueryDataModel((DataModel) methods[i].invoke(clazz.newInstance()));;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if(!cls.startsWith("zxframe")) {
				logger.info(sb.toString());
			}
			return cm;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 检查缓存模型是否有效
	 * @param cm
	 * @return
	 */
	public static boolean checkDataModel(DataModel cm) {
		if(cm==null||isNoHasDataModelMap.containsKey(cm.getGroup())) {
			return false;
		}
		return true;
	}
	/**
	 * 添加查询缓存模型
	 * @param cm
	 */
	public static void addQueryDataModel(DataModel cm) {
		if(!cacheModelMap.containsKey(cm.getGroup())) {
			StringBuffer sb=new StringBuffer();
			sb.append("load model >>>> ").append(cm.getGroup()).append(" ");
			sb.append(cm.toString());
			if(!cm.getGroup().startsWith("zxframe")) {
				logger.info(sb.toString());
			}
			cacheModelMap.put(cm.getGroup(), cm);
		}
	}
}
