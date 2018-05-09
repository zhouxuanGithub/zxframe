package zxframe.util;


/**
 * 已优化
 *
 */
public final class RC4
{
	
	private RC4(){}
	public final static String RunRC4(final String aInput,final String aKey)
	{
		int[] iS = new int[256];
		byte[] iK = new byte[256];
		
		for (int i=0;i<256;i++)iS[i]=i;
			
		int j = 1;
		
		for (short i= 0;i<256;i++)iK[i]=(byte)aKey.charAt((i % aKey.length()));
		
		j=0;
		int temp;
		for (int i=0;i<255;i++)
		{
			j=(j+iS[i]+iK[i]) % 256;
			temp = iS[i];
			iS[i]=iS[j];
			iS[j]=temp;
		}
	
	
		int i=0;
		j=0;
		char[] iInputChar = aInput.toCharArray();
		char[] iOutputChar = new char[iInputChar.length];
		for(short x = 0;x<iInputChar.length;x++)
		{
			i = (i+1) % 256;
			j = (j+iS[i]) % 256;
			temp = iS[i];
			iS[i]=iS[j];
			iS[j]=temp;
			temp = (iS[i]+(iS[j] % 256)) % 256;
			temp = iS[temp];
			iOutputChar[x] =(char)( iInputChar[x]^temp) ;	
		}
		return new String(iOutputChar);
	}
	public static void main(String[] args)
	{
		long a=System.currentTimeMillis();
		String aInput = "23232sfuck";
		String aKey = "wdkpkey";
		//System.out.println(new Integer(aKey));
		System.out.println(java.net.URLEncoder.encode(RunRC4(aInput,aKey)));
		System.out.println(RunRC4(RunRC4(aInput,aKey),aKey));
		System.out.println(System.currentTimeMillis()-a);
	}
}