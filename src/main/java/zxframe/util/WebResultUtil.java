package zxframe.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zxframe.config.ZxFrameConfig;

public class WebResultUtil {
	private static Logger logger = LoggerFactory.getLogger(WebResultUtil.class);
	public static ThreadLocal<HttpServletRequest> currentRequest = new ThreadLocal<HttpServletRequest>();
	public static ThreadLocal<HttpServletResponse> currentResponse = new ThreadLocal<HttpServletResponse>();
	public static void print(HttpServletRequest request,HttpServletResponse response, Object obj) throws IOException {
		 if(request==null) {
		 	request= currentRequest.get();
		 }
		 if(response==null) {
			response=currentResponse.get();
		 }
		 response.setContentType("text/html; charset=utf-8");
		 PrintWriter writer = response.getWriter();
		 String r=JsonUtil.obj2Json(obj);
		 if(request.getParameter("callback")!=null){
			 r=request.getParameter("callback")+"("+r+");";
		 }
		 if(ZxFrameConfig.showlog) {
			 logger.info("web result : "+r);
		 }
		 writer.print(r);
		 writer.flush();
		 writer.close();
	}
}

