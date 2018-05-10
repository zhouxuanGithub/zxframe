package zxframe.cache.mgr;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import zxframe.cache.local.LocalCacheManager;
import zxframe.cache.model.CacheModel;
import zxframe.cache.redis.RedisCacheManager;
import zxframe.cache.transaction.CacheTransaction;
import zxframe.util.JsonUtil;

/**
 * 缓存管理类
 * @author zx
 *
 */
@Component
public class CacheManager {
	private Logger logger = LoggerFactory.getLogger(CacheManager.class);
	@Resource
	CacheTransaction ct;
	@Resource
	RedisCacheManager rcm;
	@Resource
	LocalCacheManager lcm;
	public void put(CacheModel cm,String id,Object value) {
		if(cm==null) {
			logger.error("CacheManager.put失败,CacheModel不能为空");
			return;
		}
		if(!CacheModelManager.checkCacheModel(cm)) {
			logger.error("CacheManager.put失败,无效的Group,请给["+cm.getGroup()+"]该对象添加Cache注解。");
			return;
		}
		if(cm.isLcCache()) {
			lcm.put(cm.getGroup(), id, value);
		}
		if(cm.isRcCache()) {
			rcm.put(cm, id, value);
		}
	}

	public Object get(String group,String id) {
		CacheModel cm = CacheModelManager.getCacheModelByGroup(group);
		if(cm.isLcCache()&&cm.isRcCache()) {
			Object o = lcm.get(group, id);
			if(o==null) {
				o=rcm.get(group, id);
			}
			return o;
		}else if(cm.isLcCache()) {
			return lcm.get(group, id);
		}else if(cm.isRcCache()) {
			return rcm.get(group, id);
		}
		return null;
	}
	public void remove(String group) {
		//清理事务中的数据
		ct.removePSData(group, null);
		//删除缓存中的数据
		CacheModel cm = CacheModelManager.getCacheModelByGroup(group);
		if(cm.isLcCache()) {
			lcm.remove(group);
		}
		if(cm.isRcCache()) {
			rcm.remove(group);
		}
	}
	public void remove(String group,String key) {
		//清理事务中的数据
		ct.removePSData(group, key);
		//删除缓存中的数据
		CacheModel cm = CacheModelManager.getCacheModelByGroup(group);
		if(cm.isLcCache()) {
			lcm.remove(group,key);
		}
		if(cm.isRcCache()) {
			rcm.remove(group,key);
		}
	}
	/**
	 * 获得组
	 * @param clas
	 * @return
	 */
	public String getGroup(Class clas) {
		CacheModel cm = CacheModelManager.getCacheModelByGroup(clas.getName());
		if(cm==null) {
			logger.error("此Class不是缓存模型，不能获取Group");
			return null;
		}
		return cm.getGroup();
	}
	/**
	 * 清理查询缓存
	 * @param cm 缓存模型
	 * @param args sql参数
	 */
	public void removeQueryCache(CacheModel cm,Object... args) {
		if(cm.isQueryCache()) {
			remove(cm.getGroup(),getQueryKey(cm.getSql(), args));
		}
	}
	/**
	 * 获得查询key
	 * @param sql
	 * @param args
	 * @return
	 */
	public static String getQueryKey(String sql,Object... args) {
		return sql+(args==null?"":JsonUtil.obj2Json(args));
	}
	/**
	 * 关闭访问资源
	 */
	public void close() {
		rcm.close();
	}
}
