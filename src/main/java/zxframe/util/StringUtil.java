package zxframe.util;


import java.util.List;

/**
 * 字符串处理工具类
 * 
 * @author 周璇 wing - V 1.0.4
 * 
 */
public final class StringUtil {
	private static final String submoneyCN[] = { "", "拾", "佰", "仟" };
	private static final String submoneyCNN[] = { "零", "壹", "贰", "叁", "肆", "伍",
			"陆", "柒", "捌", "玖" };

	/**
	 * 检测字符串是否包含非法字符
	 * 
	 * @param str
	 *            需要被检测的字符串
	 * @param list
	 *            过滤字典
	 * @return true表示通过 检测 false表示不通过检测
	 */
	public static boolean cheackStr(String str, List<String> list) {
		int strLength = str.length();
		int count = list.size();
		int listCL = 0;
		for (int i = 0; i < count; i++) {
			listCL = list.get(i).length();
			for (int j = listCL, jj = 0; j <= strLength; j++, jj++) {
				if (str.substring(jj, j).equalsIgnoreCase(list.get(i))) {
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * 小写金额转换为大写金额  格式888.00
	 * @param money 需要转换的小写金额
	 * @return  转换后的大写金额
	 */
	public static String changMoney(String money) {
		String changeMoney = "";
		int point = money.indexOf(".");
		if (point != -1) {
			String money1 = money.substring(0, point);
			String money1_1 = (new StringBuffer(money1).reverse()).toString();
			String money2 = money.substring(point + 1);
			if (money2.length() < 2) {
				if (money2.length() == 0)
					money2 = "00";
				else
					money2 += "0";
			} else
				money2 = money.substring(point + 1, point + 3);
			int len = money1_1.length();
			int pos = len - 1;
			String sigle = "";
			boolean allhavenum = false;
			boolean havenum = false;
			boolean mark = false; // 设置一个开关变量，若当前数为"0"，将该值设为true；不为"0"时设为false
			// 以下代码为读出小数点左面的部分
			while (pos >= 0) {
				sigle = money1_1.substring(pos, pos + 1);
				// 读取“亿单元”的代码
				if (pos >= 8 && pos < 12) { // 假设读取10024531042.34。小数点左面的部分反转后为：24013542001；pos的初始值为10；mark的初始值为false；havenum的初始值为false
					if (!sigle.equals("0")) { // 如果当前值不为"0"
						if (!mark) { // 如果当前值的前一位数不为"0"
							changeMoney += submoneyCNN[Integer.parseInt(sigle)]
									+ submoneyCN[pos % 4];
						} else { // 如果当前值不为"0"，但该值的前一位数为"0"
							if (allhavenum) { // 如果在当前值之前有不为"0"的数字出现。该条件用来处理用户输入的如：0012.34的数值
								changeMoney += "零";
							}
							changeMoney += submoneyCNN[Integer.parseInt(sigle)]
									+ submoneyCN[pos % 4];
							mark = false;
						}
						havenum = true;
						allhavenum = true; // 变量allhavenum表示小数点左面的数中是否有不为"0"的数字；true表示有，false表示无
					} else { // 如果当前值为"0"
						mark = true;
					}
					if (pos % 4 == 0 && havenum) { // 如果当前数字为该单元的最后一位，并且该单元中有不为"0"的数字出现
						changeMoney += "亿";
						havenum = false;
					}
				}
				// 读取“万单元”的代码
				if (pos >= 4 && pos < 8) {
					if (!sigle.equals("0")) {
						// allhavenum=true;
						if (!mark)
							changeMoney += submoneyCNN[Integer.parseInt(sigle)]
									+ submoneyCN[pos % 4];
						else {
							if (allhavenum) {
								changeMoney += "零";
							}
							changeMoney += submoneyCNN[Integer.parseInt(sigle)]
									+ submoneyCN[pos % 4];
							mark = false;
						}
						havenum = true;
						allhavenum = true;
					} else {
						mark = true;
					}
					if (pos % 4 == 0 && havenum) {
						changeMoney += "万";
						havenum = false;
					}
				}
				// 读取“个、十、百、千”的代码
				if (pos >= 0 && pos < 4) {
					if (!sigle.equals("0")) {
						// allhavenum=true;
						if (!mark)
							changeMoney += submoneyCNN[Integer.parseInt(sigle)]
									+ submoneyCN[pos % 4];
						else {
							if (allhavenum) {
								changeMoney += "零";
							}
							changeMoney += submoneyCNN[Integer.parseInt(sigle)]
									+ submoneyCN[pos % 4];
							mark = false;
						}
						havenum = true;
						allhavenum = true;
					} else {
						mark = true;
					}
				}
				pos--;
			}
			// 碰到小数点时的读法
			if (allhavenum) // 如：00.34就不能读为:元3角4分.变量allhavenum表示小数点左面的内容中是否有数字出现
				changeMoney += "元";
			else
				// 如果小数点左面的部分都为0如：00.34应读为：零元3角4分
				changeMoney = "零元";
			// 以下代码为读出小数点右面的部分
			if (money2.equals("00"))
				changeMoney += "整";
			else {
				// 读出角
				if (money2.startsWith("0")
						|| (allhavenum && money1.endsWith("0"))) { // 如120.34读为：1佰2拾元零3角4分；123.04读为：1佰2拾3元零4分
					changeMoney += "零";
				}
				if (!money2.startsWith("0")) {
					changeMoney += submoneyCNN[Integer.parseInt(money2
							.substring(0, 1))]
							+ "角";
				}
				// 读出分，如：12.30读1拾2元3角零分
				changeMoney += submoneyCNN[Integer
						.parseInt(money2.substring(1))]
						+ "分";
			}
		} else {
			changeMoney = "输入的格式不正确！格式：888.00";
		}
		return changeMoney;

	}
	/**
	 * 防XSS攻击
	 * @param str 需要处理的字符串
	 * @return
	 */
	public static String cleanXSS(String s)
	{
		StringBuilder sb = new StringBuilder(s.length() + 16);  
        for (int i = 0; i < s.length(); i++) {  
            char c = s.charAt(i);  
            switch (c) {  
            case '>':  
                sb.append("＞");// 转义大于号  
                break;  
            case '<':  
                sb.append("＜");// 转义小于号  
                break;  
            case 10:
            case 13:
                break;
            case '\'':  
                sb.append("＇");// 转义单引号  
                break;  
            case '\"':  
                sb.append("＂");// 转义双引号  
                break;  
            case '&':  
                sb.append("＆");// 转义&  
                break;  
            case '#':  
                sb.append("＃");// 转义#  
                break; 
            default:  
                sb.append(c);  
                break;  
            }  
        }
        return sb.toString();
	}
	/**
	 * 清除 String的NULL异常和左右空格
	 * @param value
	 * @return
	 */
	public static String removeNull(String value) {
		if (value == null) {
			value = "";
		} else {
			value = value.trim();
		}
		if (value.equals("null")) {
			value = "";
		}
		return value;
	}
	/**
	 * 判断字段真实长度的实例(中文2个字符,英文1个字符)
	 * @param value
	 * @return
	 */
	public static int strLength(String value) {
		 int valueLength = 0;
		 String chinese = "[\u4e00-\u9fa5]";
		 for (int i = 0; i < value.length(); i++) {
			  String temp = value.substring(i, i + 1);
			  if (temp.matches(chinese)) {
				  valueLength += 2;
			  } else {
				  valueLength += 1;
			  }
		 }
		 return valueLength;
	}
	public static void main(String[] args) {
		System.out.println(StringUtil.changMoney("111.11"));
	}
}
