package zxframe.sys.webhandle;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import zxframe.sys.webhandle.result.BaseResult;
import zxframe.sys.webhandle.result.ResultCode;
import zxframe.sys.webhandle.result.SimpleResult;

@Aspect
@Component
@Order(2)
public class RestControllerAop {
		
	@Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
	public void getAopPointcut() {
		
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Around(value="getAopPointcut()")
	public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable{
		
		Object result = null;
		try {
			result = pjd.proceed();
			if(result!=null) {
				if(result instanceof BaseResult) {
					return result;
				}
				else {
					return new SimpleResult(ResultCode.Success,"",result);
				}
			}
			else {
				return new BaseResult(ResultCode.Success);
			}
		} 
		catch (Throwable e) {			
			return new BaseResult(ResultCode.Failed,e.getMessage());
		}
		
		
	}

}
