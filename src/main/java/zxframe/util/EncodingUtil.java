package zxframe.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 编码处理类
 * 
 * @author 周璇 wing V 2.0
 * 
 */
public final class EncodingUtil {

	/************
	 * 将十进制整数转为byte数组（双字节）
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] intToTwoByte(int value) {
		String str = Integer.toHexString(value);

		String s = "";
		if (str.length() < 4) {
			for (int i = 0; i < 4 - str.length(); i++) {
				s += "0";
			}
			str = s + str;
		}

		String s1 = str.substring(0, 2);
		String s2 = str.substring(2, 4);

		int i1 = hexStringToInt(s1);
		int i2 = hexStringToInt(s2);
		byte b[] = { (byte) i1, (byte) i2 };
		return b;
	}

	/************
	 * 将十进制整数转为byte数组（三字节）
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] intToThreeByte(int value) {
		String str = Integer.toHexString(value);

		String s = "";
		if (str.length() < 6) {
			for (int i = 0; i < 6 - str.length(); i++) {
				s += "0";
			}
			str = s + str;
		}

		String s1 = str.substring(0, 2);
		String s2 = str.substring(2, 4);
		String s3 = str.substring(4, 6);

		int i1 = hexStringToInt(s1);
		int i2 = hexStringToInt(s2);
		int i3 = hexStringToInt(s3);
		byte b[] = { (byte) i1, (byte) i2, (byte) i3 };
		return b;
	}

	/******************
	 * 将十进制整数转为byte数组（四字节）
	 */
	public static byte[] intToFourByte(int value) {
		String str = Integer.toHexString(value).toUpperCase();
		String s = "";
		if (str.length() < 4) {
			for (int i = 0; i < 4 - str.length(); i++) {
				s += "0";
			}
			str = s + str;
		}
		return stringToByte(str);
	}

	/******************
	 * 将多个byte值转为int
	 */
	public static int byteToInt(byte[] b) {
		if (b != null) {
			if (b.length > 0) {
				String s = "";
				for (int i = 0; i < b.length; i++) {
					String v = Integer.toHexString(b[i] & 0xFF);
					if (v.length() == 1) {
						v = "0" + v;
					}
					s += v;
				}
				return hexStringToInt(s);
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	/******************
	 * 将两个byte值转为int
	 */
	public int byteToInt(byte b1, byte b2) {
		String s1 = Integer.toHexString(b1 & 0xFF);
		String s2 = Integer.toHexString(b2 & 0xFF);
		if (s1.length() == 1) {
			s1 = "0" + s1;
		}
		if (s2.length() == 1) {
			s2 = "0" + s2;
		}
		return hexStringToInt(s1 + s2);
	}

	

	/*********
	 * 每个以空格分割
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] hex2byte(String value) {
		String[] str = value.split(" ");
		byte[] b = new byte[str.length];
		for (int i = 0; i < str.length; i++) {
			b[i] = (byte) hexStringToInt(str[i]);
		}
		return b;
	}
	/**
     * 连续的16进制串.getBytes()  转换成 byte (非0隔开)
     * @param b
     * @return
     */
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }

        return b2;
    }
    /**
	 * 将byte数组转换为 十六进制 string
	 * 
	 * @param bArray
	 * @return
	 */
	public static String byte2hex1(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase());
		}

		return sb.toString();
	}
	 /**
     * 转成16进制字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex2(byte[] b) { // 一个字节的数，
    	// 转成16进制字符串
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            // 整数转成十六进制表示
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase(); // 转成大写
    }
    
	/****************
	 * 
	 * 16进制字符转为字符串 如 将02转为十进制的2(单个) 
	 */
	public static String hexStringToString(byte b) {
		return Integer.toHexString(0xFF & b);
	}
	/****************
	 * 16进制字符串转为十进制整数 如 将0E转为十进制的14 （单个）
	 */
	public static int hexStringToInt(String value) {
		return Integer.valueOf(value, 16);
	}
	/********************
	 * 将CRC十六进制字符转为byte数组
	 */
	public byte[] crcToByte(String crc) {
		if (crc.equals("")) {
			return this.intToTwoByte(0);
		} else {
			if (crc.length() < 4) {
				String s = "";
				for (int i = 0; i < 4 - crc.length(); i++) {
					s += "0";
				}
				crc = s + crc;
			}
			String s1 = crc.substring(0, 2);
			String s2 = crc.substring(2, 4);

			int i1 = hexStringToInt(s1);
			int i2 = hexStringToInt(s2);

			byte b[] = { (byte) i1, (byte) i2 };
			return b;
		}
	}

	/**
	 * 将byte数组转换为十进制 string
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesToString(byte[] bytes) {
		try {
			return new String(bytes, "gb2312");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	

	/**
	 * 把字符串转化成byte数组
	 * 
	 * @param value
	 *            需要转化的值
	 * @param encoding
	 *            编码格式
	 * @return
	 */
	public static byte[] stringToByte(String value) {
		try {
			if (StringUtil.removeNull(value).equals("")) {
				return null;
			} else {
				return value.getBytes("gb2312");
			}
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/************
	 * hex字符串转对应的char字符
	 */
	public static char hexStringToChar(String hex) {
		int v = hexStringToInt(hex);
		return (char) v;
	}

	/*******************
	 * 把时间转成四字节 yyyy-MM-dd HH:mm:ss 08:30:05 (8*60+30)*60+5=30605 0x0000778D
	 */
	public static byte[] timeToByte(String time) {
		// System.out.println(time);
		Date date = stringToDate(time, "yyyy-MM-dd HH:mm:ss");
		int as = (date.getHours() * 60 + date.getMinutes()) * 60
				+ date.getSeconds();
		String hex = Integer.toHexString(as);
		String h = "";
		if (hex.length() < 8) {
			for (int i = 0; i < 8 - hex.length(); i++) {
				h += "0";
			}
		}
		hex = h + hex;
		int j = 0;
		byte b[] = new byte[4];
		for (int i = 0; i <= 6; i = i + 2) {
			b[j] = (byte) hexStringToInt(hex.substring(i, i + 2));
			j++;
		}
		return b;
	}

	/*****************
	 * 日期转三字节 yyyy-MM-dd HH:mm:ss
	 */
	public static byte[] dateToByte(String d) {
		String sYear = d.substring(0, d.indexOf("-"));
		sYear = sYear.substring(2, 4);
		int year = Integer.parseInt(sYear);
		int month = Integer.parseInt(d.substring(d.indexOf("-") + 1, d
				.lastIndexOf("-")));
		int day = Integer.parseInt(d.substring(d.lastIndexOf("-") + 1, d
				.length()));
		byte[] b = new byte[3];
		b[0] = (byte) (year);
		b[1] = (byte) (month);
		b[2] = (byte) (day);
		return b;
	}

	/*****************
	 * 日期转三字节 yyyy-MM-dd HH:mm:ss
	 */
	public static byte[] dateToByte(Date d) {
		int year = d.getYear() % 100;
		int month = d.getMonth() + 1;
		int day = d.getDate();
		byte[] b = new byte[3];
		b[0] = (byte) (year);
		b[1] = (byte) (month);
		b[2] = (byte) (day);
		return b;
	}

	private static Date stringToDate(String dateStr, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 处理Get请求产生的乱码
	 * 
	 * @param temp
	 *            乱码
	 * @return 正确编码的字符串
	 */
	public static String convertEncoding(String temp) {
		try {
			//return new String(temp.getBytes("iso-8859-1"),"UTF-8");
			byte s[] = temp.getBytes("iso-8859-1");
			return new String(s,"GBK");

		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * @函数功能: BCD码转为10进制串(阿拉伯数据)
	 * @输入参数: BCD码
	 * @输出结果: 10进制串
	 */
	public static String bcdToStr(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
				.toString().substring(1) : temp.toString();
	}

	/** */
	/**
	 * @函数功能: 10进制串转为BCD码
	 * @输入参数: 10进制串
	 * @输出结果: BCD码
	 */
	public static byte[] strToBcd(String asc) {
		int len = asc.length();
		int mod = len % 2;

		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}

		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}

		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;

		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}

			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}

			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (charToByte(achar[pos]) << 4 | charToByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte charToByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/*
	 * 把char转成HexString
	 */
	public static final String charArrayToHexString(char[] c) {
		String resultStr = "";
		String tempStr = "";
		for (int i = 0; i < c.length; i++) {
			tempStr = Integer.toHexString((int) c[i]).toUpperCase();
			if ((int) c[i] < 16) {
				tempStr = "0" + tempStr;
			}
			resultStr = resultStr + tempStr + " ";
		}

		return resultStr;
	}

	public static char OneHexStrToOnechar(String hex) {
		byte[] ByteArray = new byte[2];
		ByteArray[0] = charToByte(hex.charAt(0));
		ByteArray[1] = charToByte(hex.charAt(1));

		char resultchar = (char) (ByteArray[0] << 4 | ByteArray[1]);
		return resultchar;
	}

	public static final String objectToHexString(Serializable s)
			throws IOException {
		return byte2hex1(objectToBytes(s));
	}

	public static final Object hexStringToObject(String hex)
			throws IOException, ClassNotFoundException {
		return bytesToObject(hexStringToByte(hex));
	}
	/**
	 * 把可序列化对象转换成字节数组
	 * 
	 * @param s
	 * @return
	 * @throws IOException
	 */
	public static final byte[] objectToBytes(Serializable s) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream ot = new ObjectOutputStream(out);
		ot.writeObject(s);
		ot.flush();
		ot.close();
		return out.toByteArray();
	}
	/**
	 * 把字节数组转换为对象
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static final Object bytesToObject(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		ObjectInputStream oi = new ObjectInputStream(in);
		Object o = oi.readObject();
		oi.close();
		return o;
	}
}
