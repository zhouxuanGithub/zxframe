package zxframe.util;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import redis.clients.jedis.ShardedJedis;
import zxframe.cache.redis.RedisCacheManager;

/**
 * 分布式锁
 * @author 周璇
 */
@Component
public class DistributedLocks {
	@Resource
	private RedisCacheManager redisCacheManager;
	/**
	 * 尝试获得锁，不阻塞
	 * @param key key
	 * @param seconds 有效时间，秒
	 * @return
	 */
	public boolean getLock(String key,int seconds) {
		boolean success=false;
		ShardedJedis resource = null;
		try {
			resource = redisCacheManager.getShardedJedis();
			long r = resource.setnx(key, String.valueOf(System.currentTimeMillis()+seconds*1000));
			if(r==1) {
				success=true;
			}else {
				if(Long.valueOf(resource.get(key))<System.currentTimeMillis()) {
					String newExpireTime=String.valueOf(System.currentTimeMillis()+seconds*1000);
					String currentExpireTime = resource.getSet(key,newExpireTime);
					if(newExpireTime.equals(currentExpireTime)) {
						success=true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(resource!=null){
				resource.close();
			}
		}
		return success;
	}
	/**
	 * 强制获得锁，阻塞
	 * @param key key
	 * @param seconds 有效时间，秒
	 * @return
	 */
	public void mustGetLock(String key,int seconds) {
		boolean success=false;
		while(!success) {
			success = getLock(key,seconds);
			if(!success) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 释放锁
	 * @param key key
	 */
	public void unLock(String key) {
		ShardedJedis resource = null;
		try {
			resource = redisCacheManager.getShardedJedis();
			resource.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(resource!=null){
				resource.close();
			}
		}
	}
}
