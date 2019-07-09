package zxframe.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import zxframe.cache.annotation.FnCache;
import zxframe.cache.local.LocalCacheManager;
import zxframe.cache.redis.RedisCacheManager;
import zxframe.jpa.model.NullObject;
import zxframe.util.JsonUtil;

/**
 * @author zx
 *
 */
@Aspect
@Component
public class FnCacheAspect {
	@Resource
	private LocalCacheManager lcm;
	@Resource
	private RedisCacheManager rcm;
	private static ConcurrentHashMap<String,ConcurrentHashMap<String,FnCache>> serviceFnMap=new ConcurrentHashMap<>();
	@Pointcut("@annotation(zxframe.cache.annotation.FnCache)")
	public void getAopPointcut() {
	}
	@Around(value="getAopPointcut()")
	public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable{
		Object result = null;
		FnCache sfc = getServiceFnCache(pjd);
		String group=sfc.group();
		String key=getCacheKey(pjd,sfc);
		try {
			result=lcm.get(group,key);
			if(result==null) {
				result=rcm.get(group, key);
				lcm.put(group, key, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(result==null) {
				result = pjd.proceed();//执行
				if(result==null) {
					//防缓存穿透处理
					result=new NullObject();
				}
				try {
					lcm.put(group, key, result);
					rcm.put(group, key, result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Throwable e) {
			throw e;
		}
		//防缓存穿透处理
		if(result instanceof NullObject) {
			return null;
		}
		return result;
	}
	//获得缓存key
	private String getCacheKey(ProceedingJoinPoint pjd,FnCache sfc) {
		Signature signature = pjd.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		String[] ps = methodSignature.getParameterNames();
		Object[] args = pjd.getArgs();
		String startKey = ServiceAspect.getJoinPointUUID(pjd);
		StringBuffer keySbs=new StringBuffer();
		keySbs.append(startKey);
		List<String> asList = Arrays.asList(sfc.key());
		for (int i = 0; i < args.length; i++) {
			if(sfc.key().length==0 || asList.contains(ps[i])) {
				keySbs.append("-");
				keySbs.append(JsonUtil.obj2Json(pjd.getArgs()[i]));
			}
		}
		return keySbs.toString();
	}
	//获得方法缓存配置
	private FnCache getServiceFnCache(ProceedingJoinPoint joinPoint) throws NoSuchMethodException, SecurityException {
		ConcurrentHashMap<String, FnCache> sfcm = serviceFnMap.get(joinPoint.getSignature().getDeclaringType().getName());
		if(sfcm==null) {
			sfcm=new ConcurrentHashMap<>();
			serviceFnMap.put(joinPoint.getSignature().getDeclaringType().getName(), sfcm);
		}
		FnCache serviceFnCache = sfcm.get(joinPoint.getSignature().getName());
		if(serviceFnCache==null) {
			String methodName=joinPoint.getSignature().getName();
			Class<?> classTarget=joinPoint.getTarget().getClass();
			Class<?>[] par=((MethodSignature) joinPoint.getSignature()).getParameterTypes();
			Method objMethod=classTarget.getMethod(methodName, par);
			serviceFnCache = objMethod.getAnnotation(FnCache.class);
			sfcm.put(joinPoint.getSignature().getName(), serviceFnCache);
		}
		return serviceFnCache;
	}
}
