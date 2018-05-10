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
	 * 尝试获得锁
	 * 获取成功或失败都会返回，不阻塞
	 * @param key key
	 * @param ms 有效时间，毫秒
	 * @return
	 */
	public boolean getLock(String key,int ms) {
		boolean success=false;
		ShardedJedis resource = null;
		try {
			resource = redisCacheManager.getShardedJedis();
			if(resource.setnx(key, String.valueOf(System.currentTimeMillis()+ms))==1) {
				success=true;
			}else {
				String oldExpireTime=resource.get(key);
				if(Long.valueOf(oldExpireTime)<System.currentTimeMillis()) {
					if(resource.getSet(key,String.valueOf(System.currentTimeMillis()+ms)).equals(oldExpireTime)) {
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
	 * 强制获得锁，直到获取到锁为止，阻塞
	 * @param key key
	 * @param ms 有效时间，毫秒
	 * @return
	 */
	public void mustGetLock(String key,int ms) {
		boolean success=false;
		while(!success) {
			success = getLock(key,ms);
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
