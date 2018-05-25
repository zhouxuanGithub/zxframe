package zxframe.cache.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import zxframe.config.ZxFrameConfig;
import zxframe.util.JsonUtil;
import zxframe.util.SerializeUtils;
/**
 * 数据本地缓存
 * 设计：存储不经常改变的数据，定时清理
 * @author 周璇
 */
@Component
public class LocalCacheManager {
	private static final Logger logger = LoggerFactory.getLogger(LocalCacheManager.class);  
	private static CacheManager  cacheManager=null;
	static {
		cacheManager = CacheManager.create();
	}
	public Object get(String group,String key) {
		Cache cache = getCache(group);
		Element element = cache.get(key);
		if(element!=null) {
			byte[] bytes = (byte[]) element.getObjectValue();
			Object value =SerializeUtils.deSerialize(bytes);
			if(ZxFrameConfig.showlog) {
				logger.info("ehcache get key:"+key+" , value:"+JsonUtil.obj2Json(value)+" lcacheSize:"+cache.getSize());
			}
			return value;
		}
		return null;
	}
	public void put(String group,String key,Object value) {
		Cache cache = getCache(group);
		Element element = new Element(key,SerializeUtils.serialize(value));
		cache.put(element);
		if(ZxFrameConfig.showlog) {
			logger.info("ehcache put key:"+key+" , value:"+JsonUtil.obj2Json(value)+" lcacheSize:"+cache.getSize());
		}
	}
	public void remove(String group,String key) {
		Cache cache = getCache(group);
		cache.remove(key);
		if(ZxFrameConfig.showlog) {
			logger.info("ehcache remove key:"+key+" lcacheSize:"+cache.getSize());
		}
	}
	public void remove(String group){
		Cache cache = getCache(group);
		cache.removeAll();
		if(ZxFrameConfig.showlog) {
			logger.info("ehcache remove group:"+group+" lcacheSize:"+cache.getSize());
		}
	}
	private Cache getCache(String group) {
		Cache cache = cacheManager.getCache(group);
		if(cache==null) {
			cacheManager.addCache(group);
			cache = cacheManager.getCache(group);
			logger.info("ehcache cache init :"+cache.toString());
		}
		return cache;
	}
}
