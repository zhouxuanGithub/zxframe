package zxframe.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * ZLib压缩工具
 */
public final class ZLibUtil {
	/**
	 * 压缩
	 * 
	 * @param data
	 *            待压缩数据
	 * @return byte[] 压缩后的数据
	 */
	public final static byte[] compress(byte[] data) {
		byte[] output = new byte[0];

		Deflater compresser = new Deflater();

		compresser.reset();
		compresser.setInput(data);
		compresser.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		compresser.end();
		return output;
	}
	/**
	 * 压缩
	 * 
	 * @param data
	 *            待压缩数据
	 * 
	 * @param os
	 *            输出流
	 */
	public final static void compress(byte[] data, OutputStream os) {
		DeflaterOutputStream dos = new DeflaterOutputStream(os);

		try {
			dos.write(data, 0, data.length);

			dos.finish();

			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解压缩
	 * 
	 * @param data
	 *            待压缩的数据
	 * @return byte[] 解压缩后的数据
	 */
	public final static byte[] decompress(byte[] data) {
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		decompresser.end();
		return output;
	}

	/**
	 * 解压缩
	 * 
	 * @param is
	 *            输入流
	 * @return byte[] 解压缩后的数据
	 */
	public final static byte[] decompress(InputStream is) {
		InflaterInputStream iis = new InflaterInputStream(is);
		ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
		try {
			int i = 1024;
			byte[] buf = new byte[i];

			while ((i = iis.read(buf, 0, i)) > 0) {
				o.write(buf, 0, i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o.toByteArray();
	}
	public  static void main(String[] args) {
		long a=System.currentTimeMillis();
		String str="asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十"
				+ "分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asda"
				+ "dsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf"
				+ "十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分"
				+ "的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf"
				+ "十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsd"
				+ "f十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asda"
				+ "dsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdads"
				+ "df十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf"
				+ "十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的as"
				+ "dadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的"
				+ "asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分"
				+ "的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分"
				+ "的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十"
				+ "分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asda"
				+ "dsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的as"
				+ "dadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的"
				+ "asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十"
				+ "分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的a"
				+ "sdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf"
				+ "十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asd"
				+ "adsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的as"
				+ "dadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的a"
				+ "sdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的"
				+ "asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分"
				+ "的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十分的asdadsdf十";
				
		int l=0;
		for (int i = 0; i < 10000; i++){
			String cstr=str+Math.random();
			byte[] by=compress(cstr.getBytes());
			//System.out.println(new String(by));
			if(l==0) {
				l=by.length;
			}
			by=decompress(by);
			String v=new String(by);
//			System.out.println(v);
		}
		System.out.println("压缩前的长度："+str.getBytes().length);
		System.out.println("压缩后的长度："+l);
		System.out.println("耗时："+(System.currentTimeMillis()-a));
	}
}

//一个典型应用中，使用delphi作为客户端，J2EE服务端，两者之间用XML作为数据交换，为了提高效率，对XML数据进行压缩，为此需要找到一种压缩/解压算法能够两个平台之间交互处理，使用ZLIB算法就是一个不错的解决方案。
//
//1、JAVA实现
//
//　在JDK中，在java.util.zip包中已经内置了ZLIB的实现，示例代码如下：
//
//
//1//解压
//2 public String decompressData(String encdata) {
//3 try {
//4 ByteArrayOutputStream bos = new ByteArrayOutputStream();
//5 InflaterOutputStream zos = new InflaterOutputStream(bos);
//6 zos.write(convertFromBase64(encdata));
//7 zos.close();
//8 return new String(bos.toByteArray());
//9 } catch (Exception ex) {
//10 ex.printStackTrace();
//11 return "UNZIP_ERR";
//12 }
//13 }
//14
//15 //压缩
//16 public String compressData(String data) {
//17 try {
//18 ByteArrayOutputStream bos = new ByteArrayOutputStream();
//19 DeflaterOutputStream zos = new DeflaterOutputStream(bos);
//20 zos.write(data.getBytes());
//21 zos.close();
//22 return new String(convertToBase64(bos.toByteArray()));
//23 } catch (Exception ex) {
//24 ex.printStackTrace();
//25 return "ZIP_ERR";
//26 }
//27 }
//
//　　2、DELPHI中的实现
//
//　　在DELPHI中，有第3方的控件可以利用来实现压缩/解压，这里我们选择VCLZIP V3.04，可以从这里下载http://www.vclzip.net。为了提高通用性，我们可以编写一个标准的DLL，就可以在Win32平台随意调用了，关键代码如下：
//
//
//function Cmip_CompressStr(txt: PChar): pchar; stdcall;
//var
//zip: TVclZip;
//compr: string;
//data: PChar;
//begin
//zip := TVclZip.Create(nil);
//compr := zip.ZLibCompressString(txt);
//data := pchar(Base64EncodeStr(compr));
//Result := StrNew(data);
//zip.Free
//end;
//
//function Cmip_DeCompressStr(txt: PChar): pchar; stdcall;
//var
//zip: TVCLUnZip;
//compr: string;
//data: PChar;
//begin
//zip := TVCLUnZip.Create(nil);
//compr := zip.ZLibDecompressString(Base64DecodeStr(txt));
//data := StrNew(pchar(compr));
//Result := data;
//zip.Free
//end;