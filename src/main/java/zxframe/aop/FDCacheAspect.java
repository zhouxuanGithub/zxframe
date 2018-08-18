package zxframe.aop;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import zxframe.cache.annotation.FDCache;
import zxframe.cache.local.LocalCacheManager;
import zxframe.cache.redis.RedisCacheManager;
import zxframe.util.JsonUtil;

/**
 * 方法级缓存
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
	@Pointcut("@annotation(zxframe.cache.annotation.FDCache)")
	public void getAopPointcut() {
	}
	@Around(value="getAopPointcut()")
	public Object aroundMethod(ProceedingJoinPoint pjd) throws Exception{
		Object result = null;
		String startKey = getFnKeyStarts(pjd);
		FDCache fdCache = chm.get(startKey);
		if(fdCache==null) {
			String methodName=pjd.getSignature().getName();
			Class<?> classTarget=pjd.getTarget().getClass();
			Class<?>[] par=((MethodSignature) pjd.getSignature()).getParameterTypes();
			Method objMethod=classTarget.getMethod(methodName, par);
		    fdCache=objMethod.getAnnotation(FDCache.class);
		}
		StringBuffer keySbs=new StringBuffer();
		keySbs.append(startKey);
		Object[] args = pjd.getArgs();
		for (int i = 0; i < args.length; i++) {
			keySbs.append("-");
			keySbs.append(JsonUtil.obj2Json(pjd.getArgs()[i]));
		}
		String key=keySbs.toString();
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
		return cls.getSimpleName()+"_"+cls.getName().hashCode()+"_"+joinPoint.getSignature().getName()+"_";
    }
}
