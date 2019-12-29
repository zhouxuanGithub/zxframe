package zxframe.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
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
	public static String getCade() {
		StringBuilder builder = new StringBuilder();
		try {
			builder.append("{");
			builder.append("os.name:");
			builder.append(System.getProperty("os.name").toLowerCase());// 系统名
			builder.append(",os.arch:");
			builder.append(System.getProperty("os.arch").toLowerCase());// 系统的架构
			builder.append(",os.version:");
			builder.append(System.getProperty("os.version").toLowerCase());// 系统版本
			builder.append(",java.home:");
			builder.append(System.getProperty("java.home").toLowerCase());
			builder.append(",java.class.version:");
			builder.append(System.getProperty("java.class.version").toLowerCase());
			builder.append(",user.dir:");
			builder.append(System.getProperty("user.dir").toLowerCase());
			builder.append(",user.name:");
			builder.append(System.getProperty("user.name").toLowerCase());
			builder.append(",os.hostname:");
			builder.append(getHostName());// 用户名
			if(isWindows()) {
				builder.append(",disk:");
				builder.append(getDiskNumber());// C盘序列卷号
			}
			builder.append(",cpu:");
			builder.append(Runtime.getRuntime().availableProcessors());
			builder.append("}");
//			builder.append(getHostAddress());// ip
//			builder.append(getMAC());// mac地址
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(builder.toString());
		return Base64.encode(builder.toString().getBytes());
	}

	// 获得本机IP
	private static String getHostAddress() throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		return addr.getHostAddress();
	}

	// 获得计算机名
	private static String getHostName() throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		return addr.getHostName();
	}

	// 获得MAC地址
	private static String getMAC() throws IOException {

		try {
			Enumeration<NetworkInterface> el = NetworkInterface
					.getNetworkInterfaces();
			StringBuilder builder = new StringBuilder();

			while (el.hasMoreElements()) {
				NetworkInterface networkInterface = el.nextElement();
				builder.append("name:").append(networkInterface.getName())
						.append(",");
				builder.append("displayName:")
						.append(networkInterface.getDisplayName()).append(",");
				builder.append("mac:");
				byte[] mac = networkInterface.getHardwareAddress();
				if (mac == null) {
					builder.append(";");
					continue;
				}
				for (byte b : mac) {
					builder.append(Integer.toHexString(b & 0xff));
					builder.append("-");
				}
				builder.append(";");
			}
			return builder.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;

	}

	public static String getDiskNumber() {
		String line = "";
		String HdSerial = "";// 记录硬盘序列号
		try {
			Process proces = Runtime.getRuntime().exec("cmd /c dir c:");// 获取命令行参数
			BufferedReader buffreader = new BufferedReader(
					new InputStreamReader(proces.getInputStream(), "gbk"));
			while ((line = buffreader.readLine()) != null) {
				if (line.indexOf("卷的序列号是") != -1) { // 读取参数并获取硬盘序列号
					HdSerial = line.substring(
							line.indexOf("卷的序列号是") + "卷的序列号是".length(),
							line.length());
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return HdSerial.trim();
	}
	public static void main(String[] args) {
		System.out.println(getCade());
	}
}
