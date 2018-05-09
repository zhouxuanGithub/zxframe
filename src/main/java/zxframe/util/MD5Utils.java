package zxframe.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	protected static MessageDigest messagedigest = null;
	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			nsaex.printStackTrace();
		}
	}

	public static String getFileMD5String(File file) throws IOException {
		FileInputStream in = null;
		int len;
		try {
			in = new FileInputStream(file);
			byte buffer[] = new byte[1024];
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				messagedigest.update(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return bufferToHex(messagedigest.digest());
	}

	public static String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	public static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		messagedigest.reset();
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static boolean checkPassword(String password, String md5PwdStr) {
		String s = getMD5String(password);
		return s.equals(md5PwdStr);
	}

	public static void main(String[] args) {
		//30 30 31 35 00 00 00 00 00 01 00 E1 80 39 38
		String a = getMD5String("我是好人").toUpperCase();
//		System.out.println(EncodingUtil.change(a));
		System.out.println(a);
	}

	/**
	 * 校验 mac
	 * @param message
	 * @return
	 */
	public static boolean validateMssageMac(String message) {
		if (!message.equals("") && message.length() > 12) {
			String mac = message.substring(message.length() - 8, message
					.length());
			String reportData = message.substring(0, message.length() - 8);
			String reportDataMd5 = MD5Utils.getMD5String(reportData)
					.toUpperCase();
			if (reportDataMd5.startsWith(mac)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}