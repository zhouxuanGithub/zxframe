package zxframe.controller;

import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zxframe.util.DateUtil;
import zxframe.util.SystemUtil;

@RestController
@RequestMapping("zxframe")
public class ZxframeController {
	@Value("${server.tomcat.basedir}")
	private String basedir;
	private static long ctime=0;
	//查看错误日志
	@RequestMapping("error")
	public synchronized String error() {
		if(!checkRunTime()) {
			return "访问频率太快，请稍等一下！";
		}
		return "<pre>"+SystemUtil.exec("tail -n 500 "+basedir+"log/error."+new DateUtil("yyyy-MM-dd").getDate()+".log")+"</pre>";
	}
	//查看运行状态
	@RequestMapping("status")
	public synchronized String status() {
		if(!checkRunTime()) {
			return "访问频率太快，请稍等一下！";
		}
		StringBuffer sb=new StringBuffer();
	    sb.append("<br><br>");  
	    long maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024; //java虚拟机能取得的最大内存  
	    long totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024; //java虚拟机当前取得的内存大小  
	    long freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024; //java虚拟机所占用的内存中的空闲部分  
	    long usedMemory = totalMemory - freeMemory; //java虚拟机当前实际使用的内存大小  
	    sb.append("<br>虚拟机能取得的最大内存 : " + maxMemory + "M");  
	    sb.append("<br>虚拟机当前取得的内存大小: " + totalMemory + "M");  
	    sb.append("<br>虚拟机当前实际使用的内存大小  : " + usedMemory + "M");  
	    sb.append("<br>虚拟机所占用的内存中的空闲部分 : " + freeMemory + "M");  
	    sb.append("<br><br>");  
	  
	    if (usedMemory < maxMemory) {  
	        sb.append("内存使用状况正常"); //标记字符!  
	    } else {  
	        sb.append("内存溢出"); //标记字符!  
	    }  
	    sb.append("<br><br>");  
	    sb.append(new java.util.Date());  
	    sb.append("<br>");  
	    sb.append(TimeZone.getDefault().getDisplayName());  
		return sb.toString();
	}
	private boolean checkRunTime() {
		boolean r=false;
		if(ctime+2500<System.currentTimeMillis()) {
			ctime=System.currentTimeMillis();
			r=true;
		}
		return r;
	}
}
