package zxframe.cache.redis;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import zxframe.cache.mgr.CacheModelManager;
import zxframe.config.ZxFrameConfig;
import zxframe.jpa.ex.JpaRuntimeException;
import zxframe.jpa.model.DataModel;
import zxframe.util.CServerUUID;
import zxframe.util.JsonUtil;
import zxframe.util.SerializeUtils;

@Component
public class RedisCacheManager {
	private static Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);  
	public static JedisCluster cluster;
	
	public static void init() {
		if(!ZxFrameConfig.ropen) {
			return;
		}
		// 池基本配置 
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        config.setBlockWhenExhausted(true);
        //设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
        config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy"); 
        //是否启用pool的jmx管理功能, 默认true
        config.setJmxEnabled(true);
        //MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name=" + "pool" + i); 默认为"pool", JMX不熟,具体不知道是干啥的...默认就好.
        config.setJmxNamePrefix("pool");
        //是否启用后进先出, 默认true
        config.setLifo(true);
        //最大空闲连接数, 默认8个
        config.setMaxIdle(8);
        //最大连接数, 默认8个
        config.setMaxTotal(100);
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        config.setMaxWaitMillis(1000);
        //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        config.setMinEvictableIdleTimeMillis(1800000);
        //最小空闲连接数, 默认0
        config.setMinIdle(0);
        //每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        config.setNumTestsPerEvictionRun(3);
        //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)   
        config.setSoftMinEvictableIdleTimeMillis(1800000);
        //在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(true);
        //链接返回时检查
        config.setTestOnReturn(false);
        //在空闲时检查有效性, 默认false
        config.setTestWhileIdle(true);
        //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        config.setTimeBetweenEvictionRunsMillis(30000);
        
        String[] serverArray = ZxFrameConfig.rClusters.split(",");
        Set<HostAndPort> nodes = new HashSet<>();

        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        //注意：这里超时时间不要太短，他会有超时重试机制。而且其他像httpclient、dubbo等RPC框架也要注意这点
        cluster = new JedisCluster(nodes, 1000, 1000, 1, ZxFrameConfig.rPassword,config);
	}
	public void put(String group,String key,Object value) {
		if(value==null) {
			return;
		}
		DataModel cm = CacheModelManager.getDataModelByGroup(group);
		if(cm.isRcCache()) {
			try {
				key = getNewKey(cm,key);
				cluster.setex(key.getBytes(),cm.getRcETime(),SerializeUtils.serialize(value));
				if(ZxFrameConfig.showcache) {
					logger.info("redis put group:"+group+" key:"+key+" , value:"+JsonUtil.obj2Json(value));
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
				key = getNewKey(CacheModelManager.getDataModelByGroup(group),key);
				byte[] bs = cluster.get(key.getBytes());
				Object value=null;
				if(bs!=null&&cm!=null) {
					value=SerializeUtils.deSerialize(bs);
				}
				if(ZxFrameConfig.showcache) {
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
				key = getNewKey(CacheModelManager.getDataModelByGroup(group),key);
				cluster.del(key.getBytes());
				if(ZxFrameConfig.showcache) {
					logger.info("redis remove group:"+group+" key:"+key);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 组删除采用group版本控制，并非真正的删除。
	 * 需要缓存机制实现方式有过期时间控制和内存满后淘汰策略（移除设置过过期时间并且最近最少使用的key）
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
				if(ZxFrameConfig.showcache) {
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
		return new Random().nextInt(100000)+"_"+CServerUUID.getSequenceId();
	}
	private String getGroupVsKey(DataModel cm) {
		return ZxFrameConfig.rKeyPrefix+"_"+cm.getGroup()+"_vs";
	}
	public void close() {
		
	}
}
