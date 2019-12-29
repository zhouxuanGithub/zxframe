package zxframe.sys.security.referer;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.WebUtils;

import zxframe.sys.webhandle.result.BaseResult;
import zxframe.sys.webhandle.result.ResultCode;
import zxframe.util.WebResultUtil;

@WebFilter(asyncSupported = true, filterName = "/RefererFilter", urlPatterns = { "/*" })
public class RefererFilter implements Filter {
	//允许访问的集合,自行配置在配置文件
	@Value("${sys.security.allowAccessReferer}")
	private String[] allowAccessReferer;
	Logger logger = LoggerFactory.getLogger(RefererFilter.class);
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("{} init...", this.getClass().getSimpleName());
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		//允许跨域访问
//		response.setHeader("Access-Control-Allow-Origin", "*");  
//      response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
//      response.setHeader("Access-Control-Max-Age", "3600");  
//      response.setHeader("Access-Control-Allow-Headers", "x-requested-with");  
		String referer = request.getHeader("referer");
		if(request.getRequestURI().equals("/") || request.getRequestURI().equals("/index.html") || request.getRequestURI().startsWith("/zxframe/") || request.getRequestURI().startsWith("/druid/") || request.getServerName().equals("127.0.0.1") || request.getServerName().equals("localhost")) {
			chain.doFilter(request, response);
		}else  {
			boolean allowAccess = false;
			for(int i=0;i<allowAccessReferer.length;i++){
				String filter = allowAccessReferer[i];
				if(filter.equals("*") || (referer!=null && referer.startsWith(filter))){
					allowAccess = true;
					break;
				}
			}
			if(allowAccess==false){
				badRequest(request,response);
			}else{
				chain.doFilter(request, response);
			}
			
		}
	}
	
	
	private void badRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setStatus(500);
	    request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, 500);
		WebResultUtil.print(request, response, new BaseResult(ResultCode.Failed,"Referer of request is unsafe"));
	}

	@Override
	public void destroy() {
	}

}
