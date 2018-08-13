package zxframe.aop;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import zxframe.cache.annotation.FDCache;
import zxframe.cache.local.LocalCacheManager;
import zxframe.cache.redis.RedisCacheManager;
import zxframe.config.ZxFrameConfig;
import zxframe.util.JsonUtil;

/**
 * 方法级缓存
 * 方法第一个参数为key值使用，其他参数任意
 * @author zx
 *
 */
@Aspect
@Component
public class FDCacheAspect {
	@Resource
	private LocalCacheManager lcm;
	@Resource
	private RedisCacheManager rcm;
	private static ConcurrentHashMap<String,FDCache> chm=new ConcurrentHashMap<>();
	@Around("@within(zxframe.cache.annotation.FDCache)")
	public Object aroundMethod(ProceedingJoinPoint pjd) throws Exception{
		Object result = null;
		String startKey = getFnKeyStarts(pjd);
		String key=startKey+JsonUtil.obj2Json(pjd.getArgs()[0]);
		FDCache fdCache = chm.get(startKey);
		if(fdCache==null) {
			String methodName=pjd.getSignature().getName();
			Class<?> classTarget=pjd.getTarget().getClass();
			Class<?>[] par=((MethodSignature) pjd.getSignature()).getParameterTypes();
			Method objMethod=classTarget.getMethod(methodName, par);
		    fdCache=objMethod.getAnnotation(FDCache.class);
		}
		//执行目标方法
		try {
			try {
				//前置通知
				result=lcm.get(fdCache.group(), key);
				if(result==null) {
					result=rcm.get(fdCache.group(), key);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(result==null) {
				//执行
				result = pjd.proceed();
				try {
					//后置通知
					if(result!=null) {
						lcm.put(fdCache.group(), key, result);
						rcm.put(fdCache.group(), key, result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Throwable e) {
			//异常通知
			throw new RuntimeException(e);
			//不抛出异常的话，异常就被上面抓住，执行下去，返回result，result值为null，转换为int
		}
		//返回通知
		return result;
	}

    private static String getFnKeyStarts(ProceedingJoinPoint joinPoint) {
		Class cls = joinPoint.getSignature().getDeclaringType();
		return ZxFrameConfig.rKeyPrefix+cls.getSimpleName()+"_"+cls.getName().hashCode()+"_"+joinPoint.getSignature().getName()+"_";
    }
}
