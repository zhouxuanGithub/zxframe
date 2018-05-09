package zxframe.util;

/**
 * 当前单一服务器环境下的UUID生成，为了避免使用较长的32位UUID
 * 使用时要避免递增完一圈后从0开始重复id问题
 * @author zx
 *
 */
public class CServerUUID {
	private static int sequenceId=0;//生成id使用，递增
	/**
	 * @preserve
	 * @return
	 */
	public synchronized static int getSequenceId()
	{
		sequenceId++;
		if(sequenceId==Integer.MAX_VALUE)
		{
			sequenceId=0;
		}
		return sequenceId;
	}
}
