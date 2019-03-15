package zxframe.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
	 * wingdows/linux 向文件写入内容
	 * 注：多个服务对一个文件操作会有并发问题
	 * @param path 路径
	 * @param split 分割符
	 * @param content 内容
	 * @param append 是否追加到文件末尾
	 */
	public static void inContext(String path, String split, String content,boolean append) {
		inContext(path,split,content,append,"UTF-8");
	}
	public static void inContext(String path, String split, String content,boolean append,String charsetName) {
		synchronized (LockStringUtil.getLock(path)) {
			//目录创建
            File dc=new File(path.substring(0, path.lastIndexOf(File.separator)));
            if(!dc.exists()) {
            	dc.mkdirs();
            }
            //写入文件
			FileOutputStream fos = null;
			try {
				File f = new File(path);
				fos = new FileOutputStream(f,append);
				fos.write(content.getBytes(charsetName));
				if(split==null) {
					split=System.getProperty("line.separator");
				}
				fos.write(split.getBytes(charsetName));
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
		return readFile(path,split,"UTF-8");
	}
	public static String readFile(String path, String split,String charsetName) {
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = null;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(path), charsetName);
			reader = new BufferedReader(isr);
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
