package zxframe.util;

import java.util.ArrayList;
import java.util.List;

/**
 * string 锁对象获取
 * 借鉴ConcurrentHashMap的方式，将需要加锁的对象分为多个bucket，每个bucket加一个锁
 * @author zx
 *
 */
public class LockStringUtil {
	private static List<Object> lockKeys = new ArrayList<Object>();
	private static int keysize=1000;
	static {
		for(int i= 0;i<keysize;i++) {  
		   Object lockKey = new Object();  
		   lockKeys.add(lockKey);
		}  
	}
	public static Object getLock(String key) {
		return lockKeys.get(key.hashCode() % keysize);
	}
}
