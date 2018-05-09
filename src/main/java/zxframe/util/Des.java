package zxframe.util;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

public class Des {
	private Key key;
	// DES/ECB/PKCS5Padding DES/ECB/NoPadding DES
	public final String CIPHER_ALGORITHM = "DES/ECB/NoPadding";
	static {
		// DesEncrypt.getKey("*ljmnbsew54521231@#$%");//生成密匙
	}

	/**
	 * 根据参数生成KEY
	 * 
	 * @param strKey
	 */
	public void setKey(byte[] strKey) {
		try {
			// 解决多程序间的key补位不同===============================================================
			byte[] arrBTmp = strKey;
			// 创建一个空的8位字节数组（默认值为0）
			byte[] arrB = new byte[8];

			// 将原始字节数组转换为8位
			for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
				arrB[i] = arrBTmp[i];
			}
			// 生成密钥
			key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加密String明文输入,String密文输出
	 * 
	 * @param strMing
	 * @return
	 */
	public String getEncString(String strMing) {
		byte[] byteMi = null;
		byte[] byteMing = null;
		String strMi = "";
		try {
			return byte2hex(getEncCode(strMing.getBytes()));

			// byteMing = strMing.getBytes("UTF8");
			// byteMi = this.getEncCode(byteMing);
			// strMi = new String( byteMi,"UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			byteMing = null;
			byteMi = null;
		}
		return strMi;
	}

	/**
	 * * 解密 以String密文输入,String明文输出
	 * 
	 * @param strMi
	 * @return
	 */
	public String getDesString(String strMi) {
		byte[] byteMing = null;
		byte[] byteMi = null;
		String strMing = "";
		try {
			return new String(getDesCode(hex2byte(strMi.getBytes())));

			// byteMing = this.getDesCode(byteMi);
			// strMing = new String(byteMing,"UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			byteMing = null;
			byteMi = null;
		}
		return strMing;
	}

	/**
	 * 加密以byte[]明文输入,byte[]密文输出
	 * 
	 * @param byteS
	 * @return
	 */
	public byte[] getEncCode(byte[] byteS) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 解密以byte[]密文输入,以byte[]明文输出
	 * 
	 * @param byteD
	 * @return
	 */
	public byte[] getDesCode(byte[] byteD) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 二行制转字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2hex(byte[] b) { // 一个字节的数，
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

	private static byte[] hex2byte(byte[] b) {
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

	public byte[] JiaMi3Des(byte[] key, byte[] context) {
		byte[] data = context;
		byte[] b = key;
		byte[] k1 = new byte[8];// key前8字节
		byte[] k2 = new byte[8];// key后8字节
		// 保存key
		for (int i = 0; i < 8; i++) {
			k1[i] = b[i];
		}
		for (int i = 8; i < 16; i++) {
			k2[i - 8] = (byte) b[i];
		}
		Des d1 = new Des();
		d1.setKey(k1);
		Des d2 = new Des();
		d2.setKey(k2);
		data = d1.getEncCode(data);
		data = d2.getDesCode(data);
		data = d1.getEncCode(data);
		return data;
	}

	public byte[] JieMi3Des(byte[] key, byte[] context) {
		byte[] data = context;
		byte[] b = key;
		byte[] k1 = new byte[8];// key前8字节
		byte[] k2 = new byte[8];// key后8字节
		// 保存key
		for (int i = 0; i < 8; i++) {
			k1[i] = b[i];
		}
		for (int i = 8; i < 16; i++) {
			k2[i - 8] = (byte) b[i];
		}
		Des d1 = new Des();
		d1.setKey(k1);
		Des d2 = new Des();
		d2.setKey(k2);
		data = d1.getDesCode(data);
		data = d2.getEncCode(data);
		data = d1.getDesCode(data);
		return data;
	}

	/***
	 * 线路加密
	 * 
	 * @param key
	 *            久key
	 * @param mw
	 *            LD+明文
	 * @return
	 */
	public static byte[] XLJM(byte[] key, byte[] mw) {
		List<Byte> list = new ArrayList<Byte>();
		for (int i = 0; i < mw.length; i++) {
			list.add(mw[i]);
		}
		for (int i = 0; i < 8; i++) {
			if (i == 0) {
				byte b = (byte) 0x80;
				list.add(b);
			} else {
				byte b = (byte) 0x00;
				list.add(b);
			}
		}
		// 数据加密
		// 1.用1字节LD表示明文数据的长度，在明文数据前加上LD产生新的数据块。
		// 2.将第一步产生的数据块以8字节为单位进行分组，得到D1、D2、D3、D4…
		// 数据块
		// 3.判断最后一个数据块的长度，如果不足8字节，则补“8000…（00重复n次，直到不足8个字节）”
		int count = list.size() / 8;// 多少个块
		if (list.size() % 8 == 0) {
			count = count - 1;
		}
		byte[][] data = new byte[count][8];
		int number = 0;
		for (int i = 0; i < count; i++) {
			for (int j = 0; j < 8; j++) {
				data[i][j] = list.get(number++);
			}
		}
		// 4.对每一个数据块使用相应密钥进行加密，得到结果D1、D2、D3、D4…
		// 5.拼接在一起
		byte[] jieguo = new byte[count * 8];
		Des d = new Des();
		for (int i = 0; i < count; i++) {
			byte[] jiaMi3Des = d.JiaMi3Des(key, data[i]);
			for (int j = 0; j < jiaMi3Des.length; j++) {
				jieguo[i * 8 + j] = jiaMi3Des[j];
			}
		}
		return jieguo;
	}

	/**
	 * 
	 * @param context
	 *            mac内容
	 * @param init
	 *            初始化参数 小于8个 补00到8字节
	 * @param key
	 * @return
	 */
	public static byte[] mac(byte[] context, byte[] init, byte[] key) {
		List<Byte> list = new ArrayList<Byte>();
		for (int i = 0; i < context.length; i++) {
			list.add(context[i]);
		}
		for (int i = 0; i < 8; i++) {
			if (i == 0) {
				byte b = (byte) 0x80;
				list.add(b);
			} else {
				byte b = (byte) 0x00;
				list.add(b);
			}
		}
		// 1.初始数据
		byte[] sj = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		for (int i = 0; i < init.length; i++) {
			sj[i] = init[i];
		}
		// 3.分数据块 D1 D2 D3 D4 4.补位
		int count = list.size() / 8;
		byte[][] wdata=new byte[count][8];
		int number=0;
		for (int i = 0; i < wdata.length; i++) {
			for (int j = 0; j < 8; j++) {
				wdata[i][j]=list.get(number++);
				//System.out.print(Integer.toHexString(wdata[i][j] &0xff)+" ");
			}
		}
		//5.加密
		byte[] zkey=new byte[8];// 久key左半边key
		for (int i = 0; i < zkey.length; i++) {
			zkey[i]=key[i];
		}
		Des d=new Des();
		d.setKey(zkey);
		for (int i = 0; i <count; i++) {
			if(i==0)
			{
				for (int j = 0; j < 8; j++) {
					wdata[0][j]=(byte)(wdata[0][j]^sj[j]);//异或
				}
			}else
			{
				wdata[0]=d.getEncCode(wdata[0]);//单des
				for (int j = 0; j < 8; j++) {
					wdata[0][j]=(byte)(wdata[0][j]^wdata[i][j]);//异或
				}
			}
		}
		wdata[0]=d.JiaMi3Des(key, wdata[0]);//3des
//		//6.得到mac值
//		byte[] mac=new byte[4];
//		for (int i = 0; i < mac.length; i++) {
//			mac[i]=wdata[0][i];
//		}
		return wdata[0];
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		// 5.mac
		byte[] key={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
		byte[] init={0x31,0x32,0x33,0x34};
		byte[] context=EncodingUtil.hex2byte("84 d4 00 00 1c AA 89 7B AF DF AB 87 DE E0 C8 73 B1 74 EA 19 26 6F 35 8B 86 8D 09 26 41");
		byte[] mac = Des.mac(context, init, key);
		 for (int i = 0; i < mac.length; i++) {
		 System.out.println(Integer.toHexString(mac[i] & 0xff));
		 }
		// 4.线路加密
		// byte[]
		// jk={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
		// byte[] mw={0x15,(byte) 0xf9,(byte) 0xf0,(byte) 0xf0,0x07,(byte)
		// 0xff,0x30,0x30,0x30,0x30,0x30,0x30,0x30,0x30,(byte) 0xcf,(byte)
		// 0xcf,(byte) 0xcf,(byte) 0xcf,(byte) 0xcf,(byte) 0xcf,(byte)
		// 0xcf,(byte) 0xcf};
		// byte[] xljm = Des.XLJM(jk,mw);
		// for (int i = 0; i < xljm.length; i++) {
		// System.out.println(Integer.toHexString(xljm[i] & 0xff));
		// }
		// 3.3DES
		// byte[] context = { (byte) 0x30, (byte) 0x31, (byte) 0x32, (byte)
		// 0x33,
		// (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37 };
		// byte[] key={(byte)0x4D,(byte)0x39,(byte)
		// 0x95,(byte)0x72,(byte)0xEC,(byte)0x52,(byte)0xE3,(byte)0xA7,(byte)0xDD,(byte)0xCE,(byte)0x92,(byte)0xD7,(byte)0x95,(byte)0xFF,(byte)0x64,(byte)0xA1};
		// Des d=new Des();
		// context=d.JiaMi3Des(key, context);
		// for(int i=0;i<context.length;i++){
		// System.out.print(Integer.toHexString(context[i] & 0xFF)+" ");
		// }
		// System.out.println();
		// context=d.JieMi3Des(key, context);
		// for(int i=0;i<context.length;i++){
		// System.out.print(Integer.toHexString(context[i] & 0xFF)+" ");
		// }
		// 2.单DES
		// byte[] data = { (byte) 0xa1, (byte) 0xff, (byte) 0xd7, (byte) 0x33,
		// (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37 };
		// byte[] k={ 0x30, (byte) 0xa1, 0x30,0x30,0x30,0x30,0x30,0x30};
		// Des d1=new Des();
		// d1.setKey(k);
		// data=d1.getEncCode(data);
		// for(int i=0;i<data.length;i++){
		// System.out.print(Integer.toHexString(data[i] & 0xFF)+" ");
		// }
		// System.out.println();
		// data=d1.getDesCode(data);
		// for(int i=0;i<data.length;i++){
		// System.out.print(Integer.toHexString(data[i] & 0xFF)+" ");
		// }
		// 1.其他
		// String value="select n";
		// Des DesEncrypt=new Des();
		// DesEncrypt.setKey("123".getBytes());
		// String strEnc = DesEncrypt.getEncString(value);//加密字符串,返回String的密文
		// System.out.println(strEnc);
		// String strDes = DesEncrypt.getDesString(strEnc);//把String 类型的密文解密
		// System.out.println(strDes);
		//            
		//            
		// byte[] data
		// ={(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05,(byte)0x06,(byte)0x07,(byte)0x08};
		// byte[] encCode = DesEncrypt.getEncCode(data);
		// System.out.println(new String(encCode));
		// byte[] desCode = DesEncrypt.getDesCode(encCode);
		// System.out.println(new String(desCode));

	}
}