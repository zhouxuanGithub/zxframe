package zxframe.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 计算util
 * @author 周璇
 *
 */
public class MathUtil {
	/**
	 * 用于计算成功几率
	 * @param v "30%"
	 * @return
	 * @throws ParseException
	 */
	public static boolean isSuccess(String v) throws ParseException {
		if(v==null) {
			return false;
		}
		return isSuccess((double)NumberFormat.getPercentInstance().parse(v));
	}
	/**
	 * 用于计算成功几率
	 * @param v double0-1
	 * @return
	 */
	public static boolean isSuccess(double v) {
		if (Math.random() < v) {
			return true;
		}
		return false;
	}
	/**
	 * 获得范围内的随机数，0>=值>n
	 * @param n
	 * @return
	 */
	public static int nextInt(int n) {
		return ThreadLocalRandom.current().nextInt(n);
	}
	/**
	 * 获得范围内的随机数，0>=值>n
	 * @param n
	 * @return
	 */
	public static double nextDouble(double n) {
		return ThreadLocalRandom.current().nextDouble(n);
	}
}
