package zxframe.util;

import java.io.BufferedReader;
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
	 * 执行系统sh cmd 指令
	 * 
	 * @param cmd
	 */
	public final static void exec(String cmd) {
		Process process;
		// String cmd = "ifconfig";//ok
		// String cmd = "sar -u 1 1| awk 'NR==4 {print $8}'";//空白。管道不支持

		try {
			// 使用Runtime来执行command，生成Process对象
			Runtime runtime = Runtime.getRuntime();
			process = runtime.exec(cmd);
			// 取得命令结果的输出流
			InputStream is = process.getInputStream();
			// 用一个读输出流类去读
			InputStreamReader isr = new InputStreamReader(is);
			// 用缓冲器读行
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			is.close();
			isr.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	}

	
}
