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
package zxframe.cache.redis;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisCluster;
import zxframe.cache.mgr.CacheModelManager;
import zxframe.config.ZxFrameConfig;
import zxframe.jpa.ex.JpaRuntimeException;
import zxframe.jpa.model.DataModel;
import zxframe.jpa.model.NullObject;
import zxframe.util.JsonUtil;
import zxframe.util.MathUtil;
import zxframe.util.SerializeUtils;
import zxframe.util.ZxSequenceId;

@Component
public class RedisCacheManager {
	private static Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);
	@Autowired(required=false)
	public JedisCluster cluster;
	
	public void put(String group,String key,Object value) {
		if(value==null) {
			return;
		}
		DataModel cm = CacheModelManager.getDataModelByGroup(group);
		if(cm.isRcCache()) {
			try {
				key = getNewKey(cm,key);
				int seconds= cm.getRcETime()+MathUtil.nextInt(60);//防止缓存雪崩，*秒随机间隔
				if(value instanceof NullObject) {
					seconds=5;//防止缓存穿透
				}
				if(value instanceof List) {
					if(((List)value).size()==0) {
						seconds=5;//防止缓存穿透
					}
				}
				cluster.setex(key.getBytes(),seconds,SerializeUtils.serialize(value));
				if(ZxFrameConfig.showlog) {
					logger.info("redis put group:"+group+" key:"+key+" , value:"+JsonUtil.obj2Json(value)+" etime:"+seconds);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public Object get(String group,String key) {
		DataModel cm = CacheModelManager.getDataModelByGroup(group);
		if(cm.isRcCache()) {
			try {
				key = getNewKey(cm,key);
				byte[] bs = cluster.get(key.getBytes());
				Object value=null;
				if(bs!=null&&cm!=null) {
					value=SerializeUtils.deSerialize(bs);
				}
				if(ZxFrameConfig.showlog) {
					logger.info("redis get group:"+group+" key:"+key+" , value:"+(value==null?"":JsonUtil.obj2Json(value)));
				}
				return value;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public void remove(String group,String key) {
		DataModel cm = CacheModelManager.getDataModelByGroup(group);
		if(cm.isRcCache()) {
			try {
				key = getNewKey(cm,key);
				cluster.del(key.getBytes());
				if(ZxFrameConfig.showlog) {
					logger.info("redis remove group:"+group+" key:"+key);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 组删除采用group版本控制，并非真正的删除。
	 * 需要缓存机制实现方式有过期时间控制和内存满后淘汰策略（移除设置过过期时间并且最少使用的key）
	 * @param group
	 */
	public void remove(String group){
		DataModel cm = CacheModelManager.getDataModelByGroup(group);
		if(cm.isRcCache()) {
			try {
				if(!cm.isStrictRW()) {
	//				logger.error("此缓存模型需要开启严格读写(strictRW=true)才能进行删除, "+cm.toString());
					logger.error("This caching model needs to open strict read and write (strictRW=true) to delete, "+cm.toString());
					return;
				}
				String key=getGroupVsKey(cm);
				String groupVersion=getNewGroupVersion();
				cluster.set(key, groupVersion);
				if(ZxFrameConfig.showlog) {
					logger.info("redis remove group:"+group);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 获得新的组id
	 * @param cm
	 * @return
	 */
	private String getNewGroup(DataModel cm) {
		if(cm.isStrictRW()) {
			try {
				String key=getGroupVsKey(cm);
				String groupVersion = cluster.get(key);
				if(groupVersion==null) {
					groupVersion=getNewGroupVersion();
					cluster.set(key, groupVersion);
				}
				return cm.getGroup()+"_"+groupVersion;
			} catch (Exception e) {
				throw new JpaRuntimeException(e);
			}
		}else {
			return cm.getGroup();
		}
	}
	
	/**
	 * 获得新key
	 * @param cm
	 * @param key
	 * @return
	 */
	private String getNewKey(DataModel cm,String key) {
		return ZxFrameConfig.rKeyPrefix+"_"+getNewGroup(cm)+"_"+key;
	}
	/**
	 * 获得group版本号
	 * @return
	 */
	private String getNewGroupVersion() {
		return new Date().getTime()+"_"+MathUtil.nextInt(100000)+"_"+ZxSequenceId.getSequenceId();
	}
	private String getGroupVsKey(DataModel cm) {
		return ZxFrameConfig.rKeyPrefix+"_"+cm.getGroup()+"_vs";
	}
	public void close() {
		
	}
}
