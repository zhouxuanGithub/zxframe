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
package zxframe.cache.mgr;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import zxframe.cache.local.LocalCacheManager;
import zxframe.cache.redis.RedisCacheManager;
import zxframe.cache.transaction.CacheTransaction;
import zxframe.jpa.ex.JpaRuntimeException;
import zxframe.jpa.model.DataModel;
import zxframe.jpa.util.SQLParsing;
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
	public void put(String group,String key,Object value) {
		try {
			DataModel cm = CacheModelManager.getDataModelByGroup(group);
			if(cm==null) {
				logger.error("CacheManager.put失败,DataModel不能为空");
				return;
			}
			if(!CacheModelManager.checkDataModel(cm)) {
				logger.error("CacheManager.put失败,无效的Group,请给["+cm.getGroup()+"]该对象添加Cache注解。");
				return;
			}
			if(cm.isLcCache()) {
				lcm.put(cm.getGroup(), key, value);
			}
			if(cm.isRcCache()) {
				rcm.put(cm.getGroup(), key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object get(String group,String key) {
		try {
			DataModel cm = CacheModelManager.getDataModelByGroup(group);
			if(cm.isLcCache()&&cm.isRcCache()) {
				Object o = lcm.get(group, key);
				if(o==null) {
					o=rcm.get(group, key);
					//填补本地缓存
					lcm.put(group, key, o);
				}
				return o;
			}else if(cm.isLcCache()) {
				return lcm.get(group, key);
			}else if(cm.isRcCache()) {
				return rcm.get(group, key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void remove(String group) {
		try {
			//清理事务中的数据
			ct.removePSData(group, null);
			//删除缓存中的数据
			DataModel cm = CacheModelManager.getDataModelByGroup(group);
			if(cm.isLcCache()) {
				lcm.remove(group);
			}
			if(cm.isRcCache()) {
				rcm.remove(group);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void remove(String group,String key) {
		try {
			//清理事务中的数据
			ct.removePSData(group, key);
			//删除缓存中的数据
			DataModel cm = CacheModelManager.getDataModelByGroup(group);
			if(cm.isLcCache()) {
				lcm.remove(group,key);
			}
			if(cm.isRcCache()) {
				rcm.remove(group,key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 清理查询缓存
	 * @param cm 缓存模型
	 * @param args sql参数
	 */
	public void removeQueryCache(String group,Object... args) {
		removeQueryCache(group,null,args);
	}
	public void removeQueryCache(String group,Map map,Object... args) {
		try {
			DataModel cm = CacheModelManager.getDataModelByGroup(group);
			if(cm.isQueryCache()) {
				remove(cm.getGroup(),getQueryKey(SQLParsing.replaceSQL(cm.getSql(),map), args));
			}else {
				throw new JpaRuntimeException("查询缓存清理失败，未开启查询缓存【group:"+group+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
