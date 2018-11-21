package zxframe.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zxframe.util.JsonUtil;

public class JsonpResultUtil {
	public static void print(HttpServletRequest request,HttpServletResponse response, Object obj) throws IOException {
		 response.setContentType("text/html; charset=utf-8");
		 PrintWriter writer = response.getWriter();
		 if(request.getParameter("callback")!=null){
			 writer.print(request.getParameter("callback")+"("+JsonUtil.obj2Json(obj)+");");
		 }else {
			 writer.print(JsonUtil.obj2Json(obj));
		 }
		 writer.flush();
		 writer.close();
	}
}

