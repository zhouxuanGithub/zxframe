package zxframe.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import zxframe.config.ZxFrameConfig;

@Aspect
@Component
public class FDCacheAspect {
	@Around("@within(zxframe.cache.annotation.FDCache)")
	public Object aroundMethod(ProceedingJoinPoint pjd){
		Object result = null;
		String startKey = ZxFrameConfig.rKeyPrefix+getFnKeyStarts(pjd);
		Object args = Arrays.asList(pjd.getArgs());
		//执行目标方法
		try {
			//前置通知
			result = pjd.proceed();
			//后置通知
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
		return cls.getSimpleName()+"_"+cls.getName().hashCode()+"_"+joinPoint.getSignature().getName();
    }
}
