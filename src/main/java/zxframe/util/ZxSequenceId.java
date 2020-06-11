package zxframe.util;

/**
 * 获得顺序ID，0~Integer.MAX_VALUE循环
 * 使用时要注意递增完一圈后从0开始重复id问题，否则使用ZxObjectId
 * @author zx
 *
 */
public class ZxSequenceId {
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
