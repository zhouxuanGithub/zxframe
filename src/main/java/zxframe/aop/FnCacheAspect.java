/**
 * ZxFrame Java Library
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
import zxframe.cache.mgr.CacheManager;
import zxframe.cache.redis.RedisCacheManager;
import zxframe.jpa.model.NullObject;
import zxframe.util.JsonUtil;
import zxframe.util.LockStringUtil;

@Aspect
@Component
public class FnCacheAspect {
	@Resource
	private LocalCacheManager lcm;
	@Resource
	private RedisCacheManager rcm;
	@Resource
	private CacheManager cacheManager;
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
		//尝试去缓存获取
		result=cacheManager.get(group, key);
		if(result==null) {
			synchronized (LockStringUtil.getLock(key)) {//防止缓存击穿
				result=cacheManager.get(group, key);
				if(result==null) {
					try {
						result = pjd.proceed();//执行
					} catch (Throwable e) {
						throw e;
					}
					if(result==null) {
						//防缓存穿透处理
						result=new NullObject();
					}
					cacheManager.put(group, key, result);
				}
			}
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
