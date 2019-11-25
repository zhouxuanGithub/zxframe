package zxframe.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 服务器系统工具
 * 
 * @author 周璇
 * 
 */
public final class SystemUtil {
	/**
	 * 判断系统  true:windows   false:linux  
	 * @return
	 */
	public static boolean isWindows()
	{
		//windows:\\  linux/
		if(File.separator.equals("\\"))
		{
			return true;
		}else
		{
			return false;
		}
	}
	/**
	 * 执行系统sh cmd 指令
	 * 
	 * @param cmd
	 */
	public final static String exec(String cmd) {
		Process process;
		// String cmd = "ifconfig";//ok
		// String cmd = "sar -u 1 1| awk 'NR==4 {print $8}'";//空白。管道不支持
		StringBuffer sb=new StringBuffer();
		InputStream is = null;
		InputStreamReader isr= null;
		BufferedReader br= null;
		try {
			// 使用Runtime来执行command，生成Process对象
			Runtime runtime = Runtime.getRuntime();
			process = runtime.exec(cmd);
			// 取得命令结果的输出流
			is = process.getInputStream();
			// 用一个读输出流类去读
			isr = new InputStreamReader(is);
			// 用缓冲器读行
			br = new BufferedReader(isr);
			String line = null;
			String separator =System.getProperty("line.separator");
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append(separator);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(is!=null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(isr!=null) {
					isr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(br!=null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
