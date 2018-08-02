package zxframe.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Random;

/**
 * 计算util
 * @author 周璇
 *
 */
public class MathUtil {
	/**
	 * 用于计算成功几率
	 * @param v double0-1
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
		if(new Random().nextInt(100)+1<=v*100) {
			return true;
		}
		return false;
	}
}
