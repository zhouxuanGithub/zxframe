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
package zxframe.http;

import java.io.IOException;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import zxframe.data.mgr.ZXDataTemplate;
import zxframe.data.model.ZXData;
import zxframe.util.DateUtil;
import zxframe.util.SystemUtil;
import zxframe.util.WebResultUtil;

@Controller
@RequestMapping("zxframe")
public class ZxframeController {
	@Value("${server.tomcat.basedir}")
	private String basedir;
	private static long ctime=0;
	@Resource
	private ZXDataTemplate zds;
	//查看运行状态
	@RequestMapping("error")
	private synchronized void error(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String r="";
		if(!checkRunTime()) {
			r= "访问频率太快，请稍等一下！";
		}else{
			String top="<meta charset=\"utf-8\"/><body onLoad='window.document.body.scrollTop = document.body.scrollHeight;'><pre><xmp>";
			String content=SystemUtil.exec("tail -n 500 "+basedir+"log/error."+new DateUtil("yyyy-MM-dd").getDate()+".log");
			String end="</xmp></pre><hr/>"+getStatus()+"</body>";
			r= top+content+end;
		}
		WebResultUtil.print(request,response, r);
	}
	//查看运行状态
	@RequestMapping("status")
	private synchronized void status(HttpServletRequest request,HttpServletResponse response) throws IOException {
		WebResultUtil.print(request,response, getStatus());
	}
	@RequestMapping("zxdata")
	private synchronized void zxdata(HttpServletRequest request,HttpServletResponse response,String key) throws IOException {
		String r="";
		if(!checkRunTime()) {
			r= "访问频率太快，请稍等一下！";
		}else{
			ZXData zxData = zds.getZXData(key);
			if(zxData==null) {
				r="null";
			}else {
				r=zxData.toString();
			}
		}
		WebResultUtil.print(request,response, r);
	}
	private String getStatus() {
		StringBuffer sb=new StringBuffer();
		sb.append("<meta charset=\"utf-8\"/>");
	    long maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024; //java虚拟机能取得的最大内存  
	    long totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024; //java虚拟机当前取得的内存大小  
	    long freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024; //java虚拟机所占用的内存中的空闲部分  
	    long usedMemory = totalMemory - freeMemory; //java虚拟机当前实际使用的内存大小  
	    if (usedMemory < maxMemory) {  
	        sb.append("内存使用状况：正常"); //标记字符!  
	    } else {  
	        sb.append("内存使用状况：溢出"); //标记字符!  
	    }  
	    sb.append("<br>虚拟机能取得的最大内存 : " + maxMemory + "M");  
	    sb.append("<br>虚拟机当前取得的内存大小: " + totalMemory + "M");  
	    sb.append("<br>虚拟机当前实际使用的内存大小  : " + usedMemory + "M");  
	    sb.append("<br>虚拟机所占用的内存中的空闲部分 : " + freeMemory + "M");  
	    sb.append("<br>");  
	    sb.append(new java.util.Date());  
	    sb.append("<br>");
	    sb.append(TimeZone.getDefault().getDisplayName());
	    sb.append("<br>");
		return sb.toString();
	}
	private boolean checkRunTime() {
		boolean r=false;
		if(ctime+2000<System.currentTimeMillis()) {
			ctime=System.currentTimeMillis();
			r=true;
		}
		return r;
	}
}
