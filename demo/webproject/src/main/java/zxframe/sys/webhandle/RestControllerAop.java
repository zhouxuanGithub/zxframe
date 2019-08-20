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
import zxframe.util.WebResultUtil;

@Aspect
@Component
@Order(0)
public class RestControllerAop {
		
	@Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
	public void getAopPointcut() {}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Around(value="getAopPointcut()")
	public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable{
		Object result = null;
		try {
			result = pjd.proceed();
			if(result!=null) {
				if(!(result instanceof BaseResult)) {
					result= new SimpleResult(ResultCode.Success,"",result);
				}
			}
			else {
				result= new BaseResult(ResultCode.Success);
			}
		} 
		catch (Throwable e) {			
			result= new BaseResult(ResultCode.Failed,e.getMessage());
		}
		WebResultUtil.print(null,null, result);
		return null;
	}
}
