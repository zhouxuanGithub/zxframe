package zxframe.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;




/**
 * mysql数据库  兼容windows linux的备份还原 V1.0
 * @author 周璇 Wing
 *
 */
public class MySqlUtil {
	
	
	public static void main(String[] args) {
		//BackData("d:\\1.sql","root","test","kcdb");
		//ImportData("d:\\1","root","test","kcdb");
	}
	/**
	 * 备份数据库
	 * @param path 存放的地址
	 * @param loginName 数据库账号
	 * @param password 密码
	 * @param dataName 数据库名称
	 */
	public static void backData(String path,String loginName,String password,String dataName)
	{
			path=path.toLowerCase();
			//存储路径和文件名称
			if(!path.endsWith(".sql")){
				path+=".sql";
			}
			String outStr = "";   
			 try {
				Runtime rt = Runtime.getRuntime();   
				//如果windows没设环境变量 就 需要手动指定一下
				//String window="C://Program Files//MySQL//MySQL Server 5.1//bin//mysqldump  -u"+loginName+" -p"+password+" "+dataName;
				String window="mysqldump  -u"+loginName+" -p"+password+" "+dataName;
				String[] arr=new String[] { "sh", "-c", "mysqldump  -u"+loginName+" -p"+password+" -t "+dataName};
				Process child=null;
				if(File.separator.equals("\\"))
					child = rt.exec(window); 
				else
					child = rt.exec(arr);  
				// 设置导出编码为utf8。这里必须是utf8在此要注意，有时会发生一个mysqldump:    
				//Got error: 1045的错误，此时mysqldump必须加上你要备份的数据库的IP地址，即   
				//mysqldump -h192.168.0.1 -uroot -pmysql dbname，今天我就遇到了这样的问题，呵呵                
				// 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。   
				//注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行    
				InputStream in = child.getInputStream();// 控制台的输出信息作为输入流    
				InputStreamReader xx = new InputStreamReader(in, "utf8");// 设置输出流编码为utf8。这里必须是utf8，否则从流中读入的是乱码    
				String inStr;   
				StringBuffer sb = new StringBuffer("");   
				// 组合控制台输出信息字符串    
				BufferedReader br = new BufferedReader(xx);   
				while ((inStr = br.readLine()) != null) {   
					sb.append(inStr + "\r\n");   
				}   
				outStr = sb.toString();   
				in.close();   
				xx.close();   
				br.close();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			saveToChooseFile(path, outStr);
	}

	/**
	 * 还原数据库
	 * @param path 存放的地址
	 * @param loginName 数据库账号
	 * @param password 密码
	 * @param dataName 数据库名称
	 */
	public static void importData(String path,String loginName,String password,String dataName)
	{
		path=path.toLowerCase();
		//存储路径和文件名称
		if(!path.endsWith(".sql")){
			path+=".sql";
		}
		try {
			Runtime rt = Runtime.getRuntime();
			Process child = null;
			if (File.separator.equals("\\")) {
				// 需要配置环境变量
				String windows = "mysql -u"+loginName+" -p"+password+" "+dataName;
				child = rt.exec(windows);
				OutputStream out = child.getOutputStream();// 控制台的输入信息作为输出流
				String inStr;
				StringBuffer sb = new StringBuffer("");
				String outStr;
				BufferedReader br = new BufferedReader(
						new InputStreamReader(new FileInputStream(path), "utf8"));
				while ((inStr = br.readLine()) != null) {
					sb.append(inStr + "\r\n");
				}
				outStr=sb.toString();
				OutputStreamWriter writer=new OutputStreamWriter(out,"utf8");
				writer.write(outStr);
				//注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免
				writer.flush();
				//别忘记关闭输入输出流
				out.close();
				br.close();
				writer.close();

			} else {
				String[] arr = new String[] {
						"sh",
						"-c",
						"mysql  -u"+loginName+" -p"+password+" -f "+dataName+" < "
								+ path };
				child = rt.exec(arr);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void saveToChooseFile(String name,String content){
		try {
			File f=new File(name);
			FileOutputStream fos=null;
			f.createNewFile();
			fos=new FileOutputStream(f);
			fos.write(stringToByteOfUTF8(content));
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static byte[] stringToByteOfUTF8(String value) {
		try {
			if (removeNull(value).equals("")) {
				return null;
			} else {
				return value.getBytes("utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	private static String removeNull(String value) {
		if (value == null) {
			value = "";
		} else {
			value = value.trim();
		}
		if (value.equals("null")) {
			value = "";
		}
		return value;
	}
}
