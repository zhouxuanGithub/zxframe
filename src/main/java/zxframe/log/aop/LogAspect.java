/**
 * ZxFrame Java Library
 * https://github.com/zhouxuanGithub/zxframe
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
package zxframe.log.aop;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import zxframe.http.Context;
import zxframe.util.JsonUtil;

@Aspect
@Component
public class LogAspect {
	private static Logger logger = LoggerFactory.getLogger(LogAspect.class); 
	@Value("${logging.level.root}")
	private String logLevel;
	private int inLogType=-1;
	@Pointcut("@annotation(zxframe.log.annotation.FnLog)")
	public void getAopPointcut1() {
	}
	@Pointcut("@within(zxframe.log.annotation.FnLog)")
	public void getAopPointcut2() {
	}
	@Around(value="getAopPointcut1()")
	public Object aroundMethod1(ProceedingJoinPoint pjd) throws Throwable{
		return exec(pjd);
	}
	@Around(value="getAopPointcut2()")
	public Object aroundMethod2(ProceedingJoinPoint pjd) throws Throwable{
		return exec(pjd);
	}
	public Object exec(ProceedingJoinPoint pjd) throws Throwable{
		if(inLogType==-1) {
			if(logLevel.toUpperCase().equals("TRACE") || logLevel.toUpperCase().equals("DEBUG") ||logLevel.toUpperCase().equals("INFO")) {
				inLogType=1;
			}else {
				inLogType=0;
			}
		}
		String id=null;
		if(inLogType==1) {
			try {
				Signature signature = pjd.getSignature();
				MethodSignature methodSignature = (MethodSignature) signature;
				String[] ps = methodSignature.getParameterNames();
				Object[] args = pjd.getArgs();
				//[uuid][execute]-[class]-[fn]-[pm]-[http uri]-[cookie]
				id=UUID.randomUUID().toString();
				StringBuffer sb=new StringBuffer();
				sb.append("["+id+"]");
				sb.append("[execute:"+pjd.getSignature().getDeclaringType().getName()+"]");
				sb.append("[function:"+pjd.getSignature().getName()+"]");
				sb.append("[param:{");
				for (int i = 0; i < args.length; i++) {
					Object arg = args[i];
					if(arg instanceof HttpServletRequest || arg instanceof HttpServletResponse || arg instanceof ServerHttpRequest || arg instanceof HttpServletResponse ) {
						continue;
					}
					sb.append(ps[i]);
					sb.append(":");
					sb.append(JsonUtil.obj2Json(arg));
					if(i!=args.length-1) {
						sb.append(",");
					}
				}
				sb.append("}]");
				HttpServletRequest request = Context.currentRequest.get();
				if(request!=null) {
					sb.append("[url:"+request.getRequestURL()+(request.getQueryString()==null?"":("?"+request.getQueryString()))+"]");
					sb.append("[cookie:{");
					Cookie[] cookies = request.getCookies();
					if(cookies!=null) {
						for(Cookie cookie : cookies){
					    	sb.append(cookie.getName());
							sb.append(":");
							sb.append(cookie.getValue());
							sb.append(",");
					    } 
					}
				    sb.append("}]");
				}
				logger.info(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Object result = null;
		try {
			result = pjd.proceed();
		} catch (Throwable e) {
			if(inLogType==1) {
				StringBuffer sb=new StringBuffer();
				sb.append("["+id+"]");
				sb.append("[error:"+e.getMessage()+"]");
				logger.info(sb.toString());
			}
			throw e;
		}
		if(inLogType==1) {
			try {
				StringBuffer sb=new StringBuffer();
				sb.append("["+id+"]");
				sb.append("[result:"+JsonUtil.obj2Json(result)+"]");
				logger.info(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
