package zxframe.util;

/**
 * zxframe数据动态加密 
 * @author zhouxuan
 * @preserve
 */
public final class ZData {
	private static String key="zdk0101";
	/**
	 * 加密解密
	 * @param key 秘钥，长度10
	 * @param data 要加密的数据
	 * @return
	 */
	public final static byte[] getZDataBytes(int[] key,byte[] data){
		short count =(short) data.length;//减少资源开销，数据长度别超过32767
		short serialNumber=count;
		for (short i=0;i < count; i++){
			switch (serialNumber++%9) {
			case 0:
				data[i]=(byte)(data[i]^key[0]);
				break;
			case 1:
				data[i]=(byte)(data[i]^key[1]);
				break;
			case 2:
				data[i]=(byte)(data[i]^key[2]);
				break;
			case 3:
				data[i]=(byte)(data[i]^key[3]);
				break;
			case 4:
				data[i]=(byte)(data[i]^key[4]);
				break;
			case 5:
				data[i]=(byte)(data[i]^key[5]);
				break;
			case 6:
				data[i]=(byte)(data[i]^key[6]);
				break;
			case 7:
				data[i]=(byte)(data[i]^key[7]);
				break;
			case 8:
				data[i]=(byte)(data[i]^key[8]);
				break;
			default:
				data[i]=(byte)(data[i]^key[9]);
				break;
			}
		}
		return data;
	}
	/**
	 * crc验证
	 * @preserve
	 */
	public static byte crc(byte[] data)
	{
		int count = data.length;
		byte b=0;
		for (int i=0;i < count; i++){
			b=(byte) (data[i]^b);
		}
		return b;
	}
	public static String encode(String str){
		return Base64.encode(RC4.RunRC4(str, key));
	}
	public static String decode(String str){
		return RC4.RunRC4(Base64.decode(str),key);
	}
	
	public static void main(String[] args) {
		String v=",1,2,3,4,5";
		System.out.println(v.split(",").length);
	}
}
