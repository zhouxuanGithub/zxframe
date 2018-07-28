package zxframe.util;

/**
 * 
 * @author zx
 *
 */
public class ArrayUtil {
	/**
	 * 获得数组长度
	 * @param array
	 * @return
	 */
	public static int getArraySize(Object[] array) {
		int size=0;
		for (int i = 0; i < array.length; i++) {
			if(array[i]!=null) {
				size++;
			}
		}
		return size;
	}
}
