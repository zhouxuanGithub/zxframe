package zxframe.util;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import zxframe.cache.redis.RedisCacheManager;
import zxframe.config.ZxFrameConfig;

/**
 * 分布式锁
 * @author 周璇
 */
@Component
public class DistributedLocks {
	@Resource
	RedisCacheManager rcm;
	/**
	 * 尝试获得锁
	 * 获取成功或失败都会返回，不阻塞
	 * @param key key
	 * @param ms 有效时间，毫秒
	 * @return
	 */
	public boolean getLock(String key,int ms) {
		if(!ZxFrameConfig.ropen) {
			throw new RuntimeException("请先开启redis缓存才能使用分布式锁！");
		}
		boolean success=false;
		try {
			if(rcm.cluster.setnx(key, String.valueOf(System.currentTimeMillis()+ms))==1) {
				success=true;
			}else {
				String oldExpireTime=rcm.cluster.get(key);
				if(oldExpireTime!=null&&Long.valueOf(oldExpireTime)<System.currentTimeMillis()) {
					String oldExpireTimeTemp = rcm.cluster.getSet(key,String.valueOf(System.currentTimeMillis()+ms));
					if(oldExpireTimeTemp!=null && (oldExpireTimeTemp).equals(oldExpireTime)) {
						success=true;
					}
				}
			}
		} catch (Exception e) {
			throw e;
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
		if(!ZxFrameConfig.ropen) {
			throw new RuntimeException("请先开启redis缓存才能使用分布式锁！");
		}
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
		if(!ZxFrameConfig.ropen) {
			throw new RuntimeException("请先开启redis缓存才能使用分布式锁！");
		}
		try {
			rcm.cluster.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
