package zxframe.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zxframe.http.Context;

public class WebResultUtil {
	public static void print(HttpServletRequest request,HttpServletResponse response, Object obj) throws IOException {
		 if(request==null) {
		 	request= Context.currentRequest.get();
		 }
		 if(response==null) {
			response=Context.currentResponse.get();
		 }
		 response.setContentType("text/html; charset=utf-8");
		 PrintWriter writer = response.getWriter();
		 String r=JsonUtil.obj2Json(obj);
		 if(request.getParameter("callback")!=null){
			 r=request.getParameter("callback")+"("+r+");";
		 }
		 writer.print(r);
		 writer.flush();
		 writer.close();
	}
}

