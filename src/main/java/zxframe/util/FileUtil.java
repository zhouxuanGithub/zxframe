package zxframe.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * 文件处理类
 * 
 * @author 周璇 wing V 1.0.3
 * 
 */
public final class FileUtil {
	/**
	 * wingdows/linux操作文件夹
	 * 
	 * @param path
	 *            路径和名称
	 * @param command
	 *            指令 1：创建 其他：删除
	 */
	public static void controlDirectory(String path, int command) {
		File myFile = new File(path);
		if (command == 1) {
			if (!myFile.exists()) {
				myFile.mkdirs();
			}
		} else {
			if (myFile.isDirectory()) {
				myFile.delete();
			}
		}
	}

	/**
	 * wingdows/linux操作文件
	 * 
	 * @param path
	 *            路径和名称
	 * @param command
	 *            指令 1：创建 其他：删除
	 */
	public static void controlFile(String path, int command) {
		File myFile = new File(path);
		if (command == 1) {
			if (!myFile.exists()) {
				try {
					myFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			myFile.delete();
		}
	}

	/**
	 * wingdows/linux追加内容，向路径文件里写内容,覆盖以前的内容
	 * 无文件会创建文件，无目录会报错
	 * 注：多个服务对一个文件操作会有并发问题
	 * @param path 路径
	 * @param split 分割符
	 * @param content 内容
	 * @param append 是否追加到文件末尾
	 */
	public static void inContext(String path, String split, String content,boolean append) {
		synchronized (LockStringUtil.getLock(path)) {
			FileOutputStream fos = null;
			try {
				File f = new File(path);
				fos = new FileOutputStream(f,append);
				fos.write(content.getBytes("UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(fos!=null){
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * windows/linux读取文件信息
	 * 
	 * @param path
	 *            文件路径包括文件名
	 * @param split
	 *            分隔符
	 * @return
	 */
	public static String readFile(String path, String split) {
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line + split);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * windows/Linux获得项目同级路径
	 * 
	 * @return 路径
	 */
	public static String getPath() {
		return System.getProperty("user.dir");
	}
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

}
