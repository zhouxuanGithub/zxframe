package zxframe.sys.security.seesion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import zxframe.sys.webhandle.result.BaseResult;
import zxframe.sys.webhandle.result.ResultCode;
import zxframe.util.WebResultUtil;


public class WebSessionInterceptor implements HandlerInterceptor{
	/**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
    	return true;
    	//根据实际情况进行会话校验
    	//登录成功使用WebSession.createSession注册会话到cookie
    	//访问action时使用WebSession.validitySession进行会话检验
//    	if(request.getRequestURI().equals("/user/login") || request.getRequestURI().startsWith("/zxframe")) {
//    		return true;
//    	}else {
//    		if(WebSession.validitySession(request)) {
//    			return true;
//    		}else {
//    			WebResultUtil.print(request, response, new BaseResult(ResultCode.Failed,"会话异常，请重新登录 - Invalid session"));
//    			return false;
//    		}
//    	}
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
