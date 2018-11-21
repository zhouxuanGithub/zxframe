package zxframe.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zxframe.config.ZxFrameConfig;

public class JsonpResultUtil {
	private static Logger logger = LoggerFactory.getLogger(JsonpResultUtil.class);
	public static void print(HttpServletRequest request,HttpServletResponse response, Object obj) throws IOException {
		 response.setContentType("text/html; charset=utf-8");
		 PrintWriter writer = response.getWriter();
		 String r=null;
		 if(request.getParameter("callback")!=null){
			 r=request.getParameter("callback")+"("+JsonUtil.obj2Json(obj)+");";
		 }else {
			 r=JsonUtil.obj2Json(obj);
		 }
		 if(ZxFrameConfig.showlog) {
			 logger.info("JsonpResult print : "+r);
		 }
		 writer.print(r);
		 writer.flush();
		 writer.close();
	}
}

