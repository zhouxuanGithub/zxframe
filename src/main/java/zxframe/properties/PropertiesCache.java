package zxframe.properties;


import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import zxframe.properties.model.Properties;
import zxframe.properties.service.PropertiesService;
import zxframe.util.ServiceLocator;


public final class PropertiesCache{
	private static ConcurrentMap<String, String> properties=new ConcurrentHashMap<String, String>();
	private static PropertiesService propertiesService;
	static{
		init();
	}
	public final static String get(String key)
	{
		return properties.get(key);
	}
	//存在性能和线程安全问题，只提供基本的键值操作,不做频繁更新
	public final static void update(String key,String value) {
		propertiesService.updateProperties(key, value);
		init();
	}
	public final static int getInt(String key)
	{
		if(properties.containsKey(key)) {
			return Integer.parseInt(PropertiesCache.get(key));
		}else {
			return 0;
		}
	}
	public final static boolean getBoolean(String key)
	{
		if(properties.containsKey(key)) {
			return PropertiesCache.get(key).equals("true");
		}else {
			return false;
		}
	}
	public static void init(){
		ConcurrentMap<String, String> propertieMap=new ConcurrentHashMap<String, String>();
		if(propertiesService==null) {
			propertiesService = ServiceLocator.getSpringBean("propertiesService");
		}
		List<Properties> list = propertiesService.getList();
		for (int i = 0; i < list.size(); i++) {
			Properties p = list.get(i);
			propertieMap.put(p.getKey(), p.getValue());
		}
		properties=propertieMap;
	}
}
