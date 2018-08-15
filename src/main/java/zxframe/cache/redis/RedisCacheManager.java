package zxframe.cache.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import zxframe.aop.ServiceAspect;
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
	public static ShardedJedisPool pool;
	
	//正在使用的ShardedJedis
	private ConcurrentMap<String, ShardedJedis> shardedJedisMap=new ConcurrentHashMap<String, ShardedJedis>();
	public static void init() {
		if(!ZxFrameConfig.ropen) {
			return;
		}
		// 池基本配置 
		JedisPoolConfig config = new JedisPoolConfig();
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
        config.setMaxIdle(ZxFrameConfig.rMaxIdle);
        //最大连接数, 默认8个
        config.setMaxTotal(ZxFrameConfig.rMaxTotal);
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        config.setMaxWaitMillis(3000);
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
        List<JedisShardInfo> jdsInfoList =new ArrayList<JedisShardInfo>();
        String ss="";
        try {
        	ArrayList<String> servers = ZxFrameConfig.rList;
			for (int i = 0; i < servers.size(); i++) {
				String[] server = servers.get(i).split(":");
				String host = server[0];
			    int port = Integer.parseInt(server[1]);
			    ss+=" ["+host+":"+port+"]";
			    JedisShardInfo info = new JedisShardInfo(host, port);
				info.setPassword(server[2]);	
				jdsInfoList.add(info);
			}
			logger.info("redis cache init:"+ss);
		} catch (NumberFormatException e) {
			logger.error("redis address error！");
			e.printStackTrace();
		}
        // 构造池 
        pool = new ShardedJedisPool(config, jdsInfoList); 
	}
	public void put(String group,String key,Object value) {
		DataModel cm = CacheModelManager.getDataModelByGroup(group);
		if(cm.isRcCache()) {
			ShardedJedis sj=null;
			try {
				key = getNewKey(cm,key);
				sj = getResource();
				sj.setex(key.getBytes(),cm.getRcETime(),SerializeUtils.serialize(value));
				if(ZxFrameConfig.showcache) {
					logger.info("redis put group:"+group+" key:"+key+" , value:"+JsonUtil.obj2Json(value));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(!Thread.currentThread().getName().startsWith(ServiceAspect.THREADNAMESTARTS)) {
					if(sj!=null) {
						sj.close();
					}
				}
			}
		}
	}
	public Object get(String group,String key) {
		DataModel cm = CacheModelManager.getDataModelByGroup(group);
		if(cm.isRcCache()) {
			ShardedJedis sj=null;
			try {
				key = getNewKey(CacheModelManager.getDataModelByGroup(group),key);
				sj = getResource();
				byte[] bs = sj.get(key.getBytes());
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
			}finally {
				if(!Thread.currentThread().getName().startsWith(ServiceAspect.THREADNAMESTARTS)) {
					if(sj!=null) {
						sj.close();
					}
				}
			}
		}
		return null;
	}
	public void remove(String group,String key) {
		DataModel cm = CacheModelManager.getDataModelByGroup(group);
		if(cm.isRcCache()) {
			ShardedJedis sj=null;
			try {
				key = getNewKey(CacheModelManager.getDataModelByGroup(group),key);
				sj = getResource();
				sj.del(key.getBytes());
				if(ZxFrameConfig.showcache) {
					logger.info("redis remove group:"+group+" key:"+key);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(!Thread.currentThread().getName().startsWith(ServiceAspect.THREADNAMESTARTS)) {
					if(sj!=null) {
						sj.close();
					}
				}
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
			ShardedJedis sj=null;
			try {
				if(!cm.isStrictRW()) {
	//				logger.error("此缓存模型需要开启严格读写(strictRW=true)才能进行删除, "+cm.toString());
					logger.error("This caching model needs to open strict read and write (strictRW=true) to delete, "+cm.toString());
					return;
				}
				String key=getGroupVsKey(cm);
				String groupVersion=getNewGroupVersion();
				sj=getResource();
				sj.set(key, groupVersion);
				if(ZxFrameConfig.showcache) {
					logger.info("redis remove group:"+group);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(!Thread.currentThread().getName().startsWith(ServiceAspect.THREADNAMESTARTS)) {
					if(sj!=null) {
						sj.close();
					}
				}
			}
		}
	}
	/**
	 * 获得jedis访问资源
	 * @return
	 */
	public ShardedJedis getShardedJedis() {
		return pool.getResource();
	}
	/**
	 * 获得访问redis资源
	 * 当前线程获得一次redis链接
	 */
	private ShardedJedis getResource() {
		//判断线程名判断，如果不是自定义线程，则改名
		String transactionId = Thread.currentThread().getName();
//		if(!Thread.currentThread().getName().startsWith(ServiceAspect.THREADNAMESTARTS)) {
//			transactionId=ServiceAspect.THREADNAMESTARTS+"_"+CServerUUID.getSequenceId();
//			Thread.currentThread().setName(transactionId);
//			if(ZxFrameConfig.showlog) {
//				logger.warn("redis thread out , new:"+transactionId);
//			}
//		}
		ShardedJedis sj=shardedJedisMap.get(transactionId);
		if(sj==null) {
			sj=pool.getResource();
			if(Thread.currentThread().getName().startsWith(ServiceAspect.THREADNAMESTARTS)) {
				shardedJedisMap.put(transactionId, sj);
				if(ZxFrameConfig.showlog) {
					logger.info("redis start:"+transactionId+" size:"+shardedJedisMap.size());
				}
			}
		}
		return sj;
	}
	/**
	 * 关闭访问redis资源
	 */
	public void close() {
		String transactionId = Thread.currentThread().getName();
		ShardedJedis sj=shardedJedisMap.remove(transactionId);
		if(sj!=null) {
			sj.close();
			if(ZxFrameConfig.showlog) {
				logger.info("redis clear:"+transactionId+" size:"+shardedJedisMap.size());
			}
		}
		else {
			if(ZxFrameConfig.showlog) {
				logger.info("redis check:"+transactionId+" size:"+shardedJedisMap.size());
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
			ShardedJedis resource = null;
			try {
				resource=getResource();
				String key=getGroupVsKey(cm);
				String groupVersion = resource.get(key);
				if(groupVersion==null) {
					groupVersion=getNewGroupVersion();
					resource.set(key, groupVersion);
				}
				return cm.getGroup()+"_"+groupVersion;
			} catch (Exception e) {
				throw new JpaRuntimeException(e);
			}finally {
				if(!Thread.currentThread().getName().startsWith(ServiceAspect.THREADNAMESTARTS)) {
					if(resource!=null) {
						resource.close();
					}
				}
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
}
