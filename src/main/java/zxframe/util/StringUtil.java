package zxframe.util;


import java.io.UnsupportedEncodingException;
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
		if(s==null) {
			return null;
		}
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

	private static final String GB_2312 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbp"
			+ "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbpbbbbbbbbbbbbbbbbbb"
			+ "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
			+ "pbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
			+ "bbbbbbbbbbbbbbbbbbbbcccccccccccccccccccccccccccccc"
			+ "ccccccccccccccccccccccccccccccccccczcccccccccccccc"
			+ "ccccccccccccccccccccccccccccccccccccsccccccccccccc"
			+ "cccccccccccccccccccccccccccccccccccccccccczccccccc"
			+ "cccccccccccccccccccccccccccccccccccccccccccccccccc"
			+ "cccddddddddddddddddddddddddddddddddddddddddddddddd"
			+ "dddddddddddddddddddddzdddddddddddddddddddddddddddd"
			+ "dddddddddddddddddddddddddddddddtdddddddddddddddddd"
			+ "dddddddddddddddddddddddddddddddddddddeeeeeeeeeeeee"
			+ "eeeeeeeeefffffffffffffffffffffffffffffffffffffffff"
			+ "ffffffffffffffffffffffffffffffffffffffffffffffffff"
			+ "fffffffffffffpffffffffffffffffffffgggggggggggggggg"
			+ "ggggggggggggggggggghggggggggggggghgggggggggggggggg"
			+ "gggggggggggggggggggggggggggggggggggggggggggggggggg"
			+ "ggggggggggggggggggggggggggggggggggggggghhhhhhhhhhh"
			+ "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhmhhhhhhhhhhh"
			+ "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"
			+ "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"
			+ "hhhhhhhhhhhhhhhhhhhhjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj"
			+ "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj"
			+ "jjjjjjjjjjjjjjkjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj"
			+ "jjjjjjjyjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj"
			+ "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj"
			+ "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj"
			+ "jjjjjjjjjjjjjjjkkkgkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkh"
			+ "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"
			+ "kkkkkkkkkkkkkkklllllllllllllllllllllllllllllllllll"
			+ "llllllllllllllllllllllllllllllllllllllllllllllllll"
			+ "llllllllllllllllllllllllllllllllllllllllllllllllll"
			+ "llllllllllllllllllllllllllllllllllllllllllllllllll"
			+ "llllllllllllllllllllllllllllllllllllllllllllllllll"
			+ "lllllllllllllmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm"
			+ "mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm"
			+ "mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm"
			+ "mmmmmmmmmmmmmmnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
			+ "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnooooo"
			+ "oooppppppppppppppppppppppppppppppppppppppppppppppp"
			+ "pppppppppppppppppppppppppppppppppppppppppppppppppp"
			+ "ppppppppppppppppppppppppbqqqqqqqqqqqqqqqqqqqqqqqqq"
			+ "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"
			+ "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"
			+ "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqrrrrrrrrrrrrrrrrrr"
			+ "rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrsssssssss"
			+ "ssssssssssssssssssssssssssssssssssssssssssssssssss"
			+ "ssssssssssssssssssssssssssssssssssssssssssssssssss"
			+ "ssssssssssssssssssssssssssssssssssssssssssssssssss"
			+ "ssssssssssssssssssssssssssssssssssssssssssssssssss"
			+ "sssssssssssssssssssssssssssssssssssssssssssssssssx"
			+ "sssssssssssssssssssssssssssttttttttttttttttttttttt"
			+ "tttttttttttttttttttttttttttttttttttttttttttttttttt"
			+ "tttttttttttttttttttttttttttttttttttttttttttttttttt"
			+ "tttttttttttttttttttttttttttttttttwwwwwwwwwwwwwwwww"
			+ "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww"
			+ "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww"
			+ "wwwxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxsx"
			+ "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
			+ "xxxxxxxxxxxxxxxxxxxxxjxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
			+ "xxxxxhxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxcxxxxxxxxx"
			+ "xxxxxxxxxxxxxxxxxxxxxxxxxxyyyyyyyyyyyyyyyyyyyyyyyy"
			+ "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"
			+ "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"
			+ "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"
			+ "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"
			+ "yyyyyyyyyyyyyyyyyyyyyyyyxyyyyyyyyyyyyyyyyyyyyyyyyy"
			+ "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyzzzzzzzzzzzzzzzzzz"
			+ "zzzzzzzzzzzzzzzzzzzzzczzzzzzzzzzzzzzzzzzzzzzzzzzzz"
			+ "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"
			+ "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"
			+ "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"
			+ "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"
			+ "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"
			+ "zzzzz     cjwgnspgcgnesypbtyyzdxykygtdjnnjqmbsjzsc"
			+ "yjsyyfpgkbzgylywjkgkljywkpjqhytwddzlsymrypywwcckzn"
			+ "kyygttngjnykkzytcjnmcylqlypysfqrpzslwbtgkjfyxjwzlt"
			+ "bncxjjjjtxdttsqzycdxxhgckbphffsswybgmxlpbylllhlxst"
			+ "zmyjhsojnghdzqyklgjhsgqzhxqgkxzzwyscscjxyeyxadzpmd"
			+ "ssmzjzqjyzcdjzwqjbyzbjgznzcpwhwxhqkmwfbpbydtjzzkxx"
			+ "ylygxfptyjyyzpszlfchmqshgmxxsxjyqdcsbbqbefsjyhxwgz"
			+ "kpylqbgldlcdtnmaeddkssngycsgxlyzaypnptsdkdylhgymyl"
			+ "cxpycjndqjwxqxfyyfjlejpzrxccqwqqsbzkymgplbmjrqcfln"
			+ "ymyqmtqyrbcjthztqfrxqhxmqjcjlyxgjmshzkbswyemyltxfs"
			+ "ydsglycjqxsjnqbsctyhbftdcyjdjyyghqfsxwckqkxebptlpx"
			+ "jzsrmebwhjlpjslyysmdxlclqkxlhxjrzjmfqhxhwywsbhtrxx"
			+ "glhqhfnmgykldyxzpylggtmtcfpnjjzyljtyanjgbjplqgszyq"
			+ "yaxbkysecjsznslyzhzxlzcghpxzhznytdsbcjkdlzayfmytle"
			+ "bbgqyzkggldndnyskjshdlyxbcgyxypkdjmmzngmmclgezszxz"
			+ "jfznmlzzthcsydbdllscddnlkjykjsycjlkwhqasdknhcsgaeh"
			+ "daashtcplcpqybsdmpjlpzjoqlcdhjxysprchnwjnlhlyyqyhw"
			+ "zptczgwwmzffjqqqqyxaclbhkdjxdgmmydqxzllsygxgkjrywz"
			+ "wyclzmssjzldbydcpcxyhlxchyzjqsfqagmnyxpfrkssbjlyxy"
			+ "syglnscmhcwwmnzjjlxxhchsyzsttxrycyxbyhcsmxjsznpwgp"
			+ "xxtaybgajcxlysdccwzocwkccsbnhcpdyznfcyytyckxkybsqk"
			+ "kytqqxfcwchcykelzqbsqyjqcclmthsywhmktlkjlycxwheqqh"
			+ "tqkjpqsqscfymmdmgbwhwlgsllystlmlxpthmjhwljzyhzjxht"
			+ "xjlhxrswlwzjcbxmhzqxsdzpsgfcsglsxymqshxpjxwmyqksmy"
			+ "plrthbxftpmhyxlchlhlzylxgsssstclsldclrpbhzhxyyfhbb"
			+ "gdmycnqqwlqhjjzywjzyejjdhpblqxtqkwhlchqxagtlxljxms"
			+ "ljhtzkzjecxjcjnmfbycsfywybjzgnysdzsqyrsljpclpwxsdw"
			+ "ejbjcbcnaytwgmpapclyqpclzxsbnmsggfnzjjbzsfzyndxhpl"
			+ "qkzczwalsbccjxjyzgwkypsgxfzfcdkhjgxtlqfsgdslqwzkxt"
			+ "mhsbgzmjzrglyjbpmlmsxlzjqzhzyjczydjwfmjklddpmjegxy"
			+ "hylxhlqyqhkycwcjmyyxnatjhyccxzpcqlbzwwytwsqcmlpmyr"
			+ "jcccxfpznzzljplxxyztzlgdltcklyrzzgqtkjhhgjljaxfgfj"
			+ "zslcfdqzlclgjdjcsnzlljpjqdcclcjxmyzftsxgcgsbrzxjqq"
			+ "ctzhgyqtjqqlzxjylylncyamcstylpdjbyregklzyzhlyszqlz"
			+ "nwczcllwjqjjjkdgjzolbbzppglghtgzxyjhzmycnqsycyhbhg"
			+ "xkamtxyxnbskyzzgjzlqjtfcjxdygjqjjpmgwgjjjpkqsbgbmm"
			+ "cjssclpqpdxcdyykyfcjddyygywrhjrtgznyqldkljszzgzqzj"
			+ "gdykshpzmtlcpwnjyfyzdjcnmwescyglbtzcgmssllyxqsxxbs"
			+ "jsbbsgghfjlypmzjnlyywdqshzxtyywhmcyhywdbxbtlmsyyyf"
			+ "sxjchtxxlhjhfssxzqhfzmzcztqcxzxrttdjhnnyzqqmtqdmmz"
			+ " ytxmjgdxcdyzbffallztdltfxmxqzdngwqdbdczjdxbzgsqqd"
			+ "djcmbkzffxmkdmdsyyszcmljdsynsprskmkmpcklgdbqtfzswt"
			+ "fgglyplljzhgjjgypzltcsmcnbtjbqfkdhpyzgkpbbymtdssxt"
			+ "bnpdkleycjnyddykzddhqhsdzsctarlltkzlgecllkjlqjaqnb"
			+ "dkkghpjxzqksecshalqfmmgjnlyjbbtmlyzxdxjpldlpcqdhzy"
			+ "cbzsczbzmsljflkrzjsnfrgjhxpdhyjybzgdlqcsezgxlblhyx"
			+ "twmabchecmwyjyzlljjyhlgbdjlslygkdzpzxjyyzlwcxszfgw"
			+ "yydlyhcljscmbjhblyzlycblydpdqysxqzbytdkyxlyycnrjmp"
			+ "dqgklcljbcxbjddbblblczqrppxjcjlzcshltoljnmdddlngka"
			+ "thqhjhykheznmshrphqqjchgmfprxhjgdychgklyrzqlcyqjnz"
			+ "sqtkqjymszxwlcfqqqxyfggyptqwlmcrnfkkfsyylybmqammmy"
			+ "xctpshcptxxzzsmphpshmclmldqfyqxszyjdjjzzhqpdszglst"
			+ "jbckbxyqzjsgpsxqzqzrqtbdkwxzkhhgflbcsmdldgdzdblzyy"
			+ "cxnncsybzbfglzzxswmsccmqnjqsbdqsjtxxmbltxcclzshzcx"
			+ "rqjgjylxzfjphymzqqydfqjqlzznzjcdgzygztxmzysctlkpht"
			+ "xhtlbjxjlxscdqxcbbtjfqzfsltjbtkqbxxjjljchczdbzjdcz"
			+ "jdcprnpqcjpfczlclzxzdmxmphjsgzgszzqlylwtjpfsyaxmcj"
			+ "btzyycwmytzsjjlqcqlwzmalbxyfbpnlsfhtgjwejjxxglljst"
			+ "gshjqlzfkcgnndszfdeqfhbsaqtgylbxmmygszldydqmjjrgbj"
			+ "tkgdhgkblqkbdmbylxwcxyttybkmrtjzxqjbhlmhmjjzmqasld" + "cyxyqdlqcafywyxqhz";

	private static final String GBK_3 = "ksxsm sdqlybjjjgczbjfya jhphsyzgj   sn      xy  ng"
			+ "    lggllyjds yssgyqyd xjyydldwjjwbbftbxthhbczcrfm"
			+ "qwyfcwdzpyddwyxjajpsfnzyjxxxcxnnxxzzbpysyzhmzbqbzc"
			+ "ycbxqsbhhxgfmbhhgqcxsthlygymxalelccxzrcsd njjtzzcl"
			+ "jdtstbnxtyxsgkwyflhjqspxmxxdc lshxjbcfybyxhczbjyzl"
			+ "wlcz gtsmtzxpqglsjfzzlslhdzbwjncjysnycqrzcwybtyftw"
			+ "ecskdcbxhyzqyyxzcffzmjyxxsdcztbzjwszsxyrnygmdthjxs"
			+ "qqccsbxrytsyfbjzgclyzzbszyzqscjhzqydxlbpjllmqxtydz"
			+ "sqjtzplcgqtzwjbhcjdyfxjelbgxxmyjjqfzasyjnsydk jcjs"
			+ "zcbatdclnjqmwnqncllkbybzzsyhjqltwlccxthllzntylnzxd"
			+ "dtcenjyskkfksdkghwnlsjt jymrymzjgjmzgxykymsmjklfxm"
			+ "tghpfmqjsmtgjqdgyalcmzcsdjlxdffjc f  ffkgpkhrcjqcj"
			+ "dwjlfqdmlzbjjscgckdejcjdlzyckscclfcq czgpdqzjj hdd"
			+ "wgsjdkccctllpskghzzljlgjgjjtjjjzczmlzyjkxzyzmljkyw"
			+ "xmkjlkjgmclykjqlblkmdxwyxysllpsjqjqxyqfjtjdmxxllcr"
			+ "qyjb xgg pjygegdjgnjyjkhqfqzkhyghdgllsdjjxkyoxnzsx"
			+ "wwxdcskxxjyqscsqkjexsyzhydz ptqyzmtstzfsyldqagylcq"
			+ "lyyyhlrq ldhsssadsjbrszxsjyrcgqc hmmxzdyohycqgphhy"
			+ "nxrhgjlgwqwjhcstwasjpmmrdsztxyqpzxyhyqxtpbfyhhdwzb"
			+ "txhqeexzxxkstexgltxydn  hyktmzhxlplbmlsfhyyggbhyqt"
			+ "xwlqczydqdq gd lls zwjqwqajnytlxanzdecxzwwsgqqdyzt"
			+ "chyqzlxygzglydqtjtadyzzcwyzymhyhyjzwsxhzylyskqysbc"
			+ "yw  xjzgtyxqsyhxmchrwjpwxzlwjs sgnqbalzzmtjcjktsax"
			+ "ljhhgoxzcpdmhgtysjxhmrlxjkxhmqxctxwzbkhzccdytxqhlx"
			+ "hyx syydz znhxqyaygypdhdd pyzndltwxydpzjjcxmtlhbyn"
			+ "yymhzllhnmylllmdcppxmxdkycydltxchhznaclcclylzsxzjn"
			+ "zln lhyntkyjpychegttgqrgtgyhhlgcwyqkpyyyttttlhylly"
			+ "ttplkyzqqzdq  nmjzxyqmktfbjdjjdxbtqzgtsyflqgxblzfh"
			+ " zadpmjhlccyhdzfgydgcyxs hd d axxbpbyyaxcqffqyjxdl"
			+ "jjzl bjydyqszwjlzkcdtctbkdyzdqjnkknjgyeglfykasntch"
			+ "blwzbymjnygzyheyfjmctyfzjjhgck lxhdwxxjkyykssmwctq"
			+ "zlpbzdtwzxzag kwxl lspbclloqmmzslbczzkdcz xgqqdcyt"
			+ "zqwzqssfpktfqdcdshdtdwfhtdy jaqqkybdjyxtlj drqxxxa"
			+ "ydrjlklytwhllrllcxylbw z  zzhkhxksmdsyyjpzbsqlcxxn"
			+ "xwmdq gqmmczjgttybhyjbetpjxdqhkzbhfdxkawtwajldyjsf"
			+ "hblddqjncxfjhdfjjwzpkzypcyzynxff ydbzznytxzembsehx"
			+ "fzmbflzrsymzjrdjgxhjgjjnzzxhgxhymlpeyyxtgqshxssxmf"
			+ "mkcctxnypszhzptxwywxyysljsqxzdleelmcpjclxsqhfwwtff"
			+ "tnqjjjdxhwlyznflnkyyjldx hdynrjtywtrmdrqhwqcmfjdyz"
			+ "hmyyxjwzqtxtlmrspwwchjb xygcyyrrlmpymkszyjrmysntpl"
			+ "nbpyyxmykyngjzznlzhhanmpgwjdzmxxmllhgdzxyhxkrycjmf"
			+ "fxyhjfssqlxxndyca nmtcjcyprrnytyqym sxndlylyljnlxy"
			+ "shqmllyzljzxstyzsmcqynzlxbnnylrqtryyjzzhsytxcqgxzs"
			+ "shmkczyqhzjnbh qsnjnzybknlqhznswxkhjyybqlbfl p bkq"
			+ "zxsddjmessmlxxkwnmwwwydkzggtggxbjtdszxnxwmlptfxlcx"
			+ "jjljzxnwxlyhhlrwhsc ybyawjjcwqqjzzyjgxpltzftpakqpt"
			+ "lc  xtx hklefdleegqymsawhmljtwyqlyjeybqfnlyxrdsctg"
			+ "gxyyn kyqctlhjlmkkcgygllldzydhzwpjzkdyzzhyyfqytyzs"
			+ "ezzlymhjhtwyzlkyywzcskqqtdxwctyjklwqbdqyncs szjlkc"
			+ "dcdtlzzacqqzzddxyplxzbqjylzllqdzqgyjyjsyxnyyynyjxk"
			+ "xdazwrdljyyynjlxllhxjcykynqcclddnyyykyhhjcl pb qzz"
			+ "yjxj fzdnfpzhddwfmyypqjrssqzsqdgpzjwdsjdhzxwybp gp"
			+ "tmjthzsbgzmbjczwbbzmqcfmbdmcjxljbgjtz mqdyxjzyctyz"
			+ "tzxtgkmybbcljssqymscx jeglxszbqjjlyxlyctsxmcwfa kb"
			+ "qllljyxtyltxdphnhfqyzyes sdhwdjbsztfd czyqsyjdzjqp"
			+ "bs j fbkjbxtkqhmkwjjlhhyyyyywyycdypczyjzwdlfwxwzzj"
			+ "cxcdjzczlxjjtxbfwpxzptdzbccyhmlxbqlrtgrhqtlf mwwjx"
			+ "jwcysctzqhxwxkjybmpkbnzhqcdtyfxbyxcbhxpsxt m sxlhk"
			+ "mzxydhwxxshqhcyxglcsqypdh my ypyyykzljqtbqxmyhcwll"
			+ "cyl ewcdcmlggqktlxkgndgzyjjlyhqdtnchxwszjydnytcqcb"
			+ "hztbxwgwbxhmyqsycmqkaqyncs qhysqyshjgjcnxkzycxsbxx"
			+ "hyylstyxtymgcpmgcccccmztasgqzjlosqylstmqsqdzljqqyp"
			+ "lcycztcqqpbqjclpkhz yyxxdtddsjcxffllxmlwcjcxtspyxn"
			+ "dtjsjwxqqjskyylsjhaykxcyydmamdqmlmczncybzkkyflmcsc"
			+ "lhxrcjjgslnmtjzzygjddzjzk qgjyyxzxxqhheytmdsyyyqlf"
			+ " zzdywhscyqwdrxqjyazzzdywbjwhyqszywnp  azjbznbyzzy"
			+ "hnscpjmqcy zpnqtbzjkqqhngccxchbzkddnzhjdrlzlsjljyx"
			+ "ytbgtcsqmnjpjsrxcfjqhtpzsyjwbzzzlstbwwqsmmfdwjyzct"
			+ "bwzwqcslqgdhqsqlyzlgyxydcbtzkpj gm pnjkyjynhpwsnsz"
			+ "zxybyhyzjqjtllcjthgdxxqcbywbwzggqrqzssnpkydznxqxjm"
			+ "y dstzplthzwxwqtzenqzw ksscsjccgptcslccgllzxczqthn"
			+ "jgyqznmckcstjskbjygqjpldxrgzyxcxhgdnlzwjjctsbcjxbf"
			+ "zzpqdhjtywjynlzzpcjdsqjkdxyajyemmjtdljyryynhjbngzj"
			+ "kmjxltbsllrzylcscnxjllhyllqqqlxymswcxsljmc zlnsdwt"
			+ "jllggjxkyhbpdkmmscsgxjcsdybxdndqykjjtxdygmzzdzslo "
			+ "yjsjzdlbtxxxqqjzlbylwsjjyjtdzqqzzzzjlzcdzjhpl qplf"
			+ "fjzysj zfpfzksyjjhxttdxcysmmzcwbbjshfjxfqhyzfsjybx"
			+ "pzlhmbxhzxfywdab lktshxkxjjzthgxh jxkzxszzwhwtzzzs"
			+ "nxqzyawlcwxfxyyhxmyyswqmnlycyspjkhwcqhyljmzxhmcnzh"
			+ "hxcltjplxyjhdyylttxfszhyxxsjbjyayrmlckd yhlrlllsty"
			+ "zyyhscszqxkyqfpflk ntljmmtqyzwtlll s rbdmlqjbcc qy"
			+ "wxfzrzdmcyggzjm  mxyfdxc shxncsyjjmpafyfnhyzxyezy "
			+ "sdl zztxgfmyyysnbdnlhpfzdcyfssssn zzdgpafbdbzszbsg"
			+ "cyjlm  z yxqcyxzlckbrbrbzcycjzeeyfgzlyzsfrtkqsxdcm"
			+ "z  jl xscbykjbbrxllfqwjhyqylpzdxczybdhzrbjhwnjtjxl"
			+ "kcfssdqyjkzcwjl b  tzlltlqblcqqccdfpphczlyygjdgwcf"
			+ "czqyyyqyrqzslszfcqnwlhjcjjczkypzzbpdc   jgx gdz  f"
			+ "gpsysdfwwjzjyxyyjyhwpbygxrylybhkjksftzmmkhtyysyyzp"
			+ "yqydywmtjjrhl   tw  bjycfnmgjtysyzmsjyjhhqmyrszwtr"
			+ "tzsskx gqgsptgcznjjcxmxgzt ydjz lsdglhyqgggthszpyj"
			+ "hhgnygkggmdzylczlxqstgzslllmlcskbljzzsmmytpzsqjcj "
			+ " zxzzcpshkzsxcdfmwrllqxrfzlysdctmxjthjntnrtzfqyhqg"
			+ "llg   sjdjj tqjlnyhszxcgjzypfhdjspcczhjjjzjqdyb ss"
			+ "lyttmqtbhjqnnygjyrqyqmzgcjkpd gmyzhqllsllclmholzgd"
			+ "yyfzsljc zlylzqjeshnylljxgjxlyjyyyxnbzljsszcqqzjyl"
			+ "lzldj llzllbnyl hxxccqkyjxxxklkseccqkkkcgyyxywtqoh"
			+ "thxpyxx hcyeychbbjqcs szs lzylgezwmysx jqqsqyyycmd"
			+ "zywctjsycjkcddjlbdjjzqysqqxxhqjohdyxgmajpchcpljsmt"
			+ "xerxjqd pjdbsmsstktssmmtrzszmldj rn sqxqydyyzbdsln"
			+ "fgpzmdycwfdtmypqwytjzzqjjrjhqbhzpjhnxxyydyhhnmfcpb"
			+ "zpzzlzfmztzmyftskyjyjzhbzzygh pzcscsjssxfjgdyzyhzc"
			+ "whcsexfqzywklytmlymqpxxskqjpxzhmhqyjs cjlqwhmybdhy"
			+ "ylhlglcfytlxcjscpjskphjrtxteylssls yhxscznwtdwjslh"
			+ "tqdjhgydphcqfzljlzptynlmjllqyshhylqqzypbywrfy js y"
			+ "p yrhjnqtfwtwrchygmm yyhsmzhngcelqqmtcwcmpxjjfyysx"
			+ "ztybmstsyjdtjqtlhynpyqzlcxznzmylflwby jgsylymzctdw"
			+ "gszslmwzwwqzsayysssapxwcmgxhxdzyjgsjhygscyyxhbbzjk"
			+ "ssmalxycfygmqyjycxjlljgczgqjcczotyxmtthlwtgfzkpzcx"
			+ "kjycxctjcyh xsgckxzpsjpxhjwpjgsqxxsdmrszzyzwsykyzs"
			+ "hbcsplwsscjhjlchhylhfhhxjsx lnylsdhzxysxlwzyhcldyh"
			+ "zmdyspjtqznwqpsswctst zlmssmnyymjqjzwtyydchqlxkwbg"
			+ "qybkfc jdlzllyylszydwhxpsbcmljscgbhxlqrljxysdwxzsl"
			+ "df hlslymjljylyjcdrjlfsyjfnllcqyqfjy szlylmstdjcyh"
			+ "zllnwlxxygyygxxhhzzxczqzfnwpypkpypmlgxgg dxzzkzfbx"
			+ "xlzptytswhzyxhqhxxxywzyswdmzkxhzphgchj lfjxptzthly"
			+ "xcrhxshxkjxxzqdcqyl jlkhtxcwhjfwcfpqryqxyqy gpggsc"
			+ "sxngkchkzxhflxjbyzwtsxxncyjjmwzjqrhfqsyljzgynslgtc"
			+ "ybyxxwyhhxynsqymlywgyqbbzljlpsytjzhyzwlrorjkczjxxy"
			+ "xchdyxyxxjddsqfxyltsfxlmtyjmjjyyxltcxqzqhzlyyxzh n"
			+ "lrhxjcdyhlbrlmrllaxksllljlxxxlycry lccgjcmtlzllyzz"
			+ "pcw jyzeckzdqyqpcjcyzmbbcydcnltrmfgyqbsygmdqqzmkql"
			+ "pgtbqcjfkjcxbljmswmdt  ldlppbxcwkcbjczhkphyyhzkzmp" + "jysylpnyyxdb";

	private static final String GBK_4 = "kxxmzjxsttdzxxbzyshjpfxpqbyljqkyzzzwl zgfwyctjxjpy"
			+ "yspmsmydyshqy zchmjmcagcfbbhplxtyqx djgxdhkxxnbhrm"
			+ "lnjsltsmrnlxqjyzlsqglbhdcgyqyyhwfjybbyjyjjdpqyapfx"
			+ "cgjscrssyz lbzjjjlgxzyxyxsqkxbxxgcxpld wetdwwcjmbt"
			+ "xchxyxxfxllj fwdpzsmylmwytcbcecblgdbqzqfjdjhymcxtx"
			+ "drmjwrh xcjzylqdyhlsrsywwzjymtllltqcjzbtckzcyqjzqa"
			+ "lmyhwwdxzxqdllqsgjfjljhjazdjgtkhsstcyjfpszlxzxrwgl"
			+ "dlzr lzqtgslllllyxxqgdzybphl x bpfd   hy jcc dmzpp"
			+ "z cyqxldozlwdwyythcqsccrsslfzfp qmbjxlmyfgjb m jwd"
			+ "n mmjtgbdzlp hsymjyl hdzjcctlcl ljcpddqdsznbgzxxcx"
			+ "qycbzxzfzfjsnttjyhtcmjxtmxspdsypzgmljtycbmdkycsz z"
			+ "yfyctgwhkyjxgyclndzscyzssdllqflqllxfdyhxggnywyllsd"
			+ "lbbjcyjzmlhl xyyytdlllb b bqjzmpclmjpgehbcqax hhhz"
			+ "chxyhjaxhlphjgpqqzgjjzzgzdqybzhhbwyffqdlzljxjpalxz"
			+ "daglgwqyxxxfmmsypfmxsyzyshdzkxsmmzzsdnzcfp ltzdnmx"
			+ "zymzmmxhhczjemxxksthwlsqlzllsjphlgzyhmxxhgzcjmhxtx"
			+ "fwkmwkdthmfzzydkmsclcmghsxpslcxyxmkxyah jzmcsnxyym"
			+ "mpmlgxmhlmlqmxtkzqyszjshyzjzybdqzwzqkdjlfmekzjpezs"
			+ "wjmzyltemznplplbpykkqzkeqlwayyplhhaq jkqclhyxxmlyc"
			+ "cyskg  lcnszkyzkcqzqljpmzhxlywqlnrydtykwszdxddntqd"
			+ "fqqmgseltthpwtxxlwydlzyzcqqpllkcc ylbqqczcljslzjxd"
			+ "dbzqdljxzqjyzqkzljcyqdypp pqykjyrpcbymxkllzllfqpyl"
			+ "llmsglcyrytmxyzfdzrysyztfmsmcl ywzgxzggsjsgkdtggzl"
			+ "ldzbzhyyzhzywxyzymsdbzyjgtsmtfxqyjssdgslnndlyzzlrx"
			+ "trznzxnqfmyzjzykbpnlypblnzz jhtzkgyzzrdznfgxskgjtt"
			+ "yllgzzbjzklplzylxyxbjfpnjzzxcdxzyxzggrs jksmzjlsjy"
			+ "wq yhqjxpjzt lsnshrnypzt wchklpszlcyysjylybbwzpdwg"
			+ "cyxckdzxsgzwwyqyytctdllxwkczkkcclgcqqdzlqcsfqchqhs"
			+ "fmqzlnbbshzdysjqplzcd cwjkjlpcmz jsqyzyhcpydsdzngq"
			+ "mbsflnffgfsm q lgqcyybkjsrjhzldcftlljgjhtxzcszztjg"
			+ "gkyoxblzppgtgyjdhz zzllqfzgqjzczbxbsxpxhyyclwdqjjx"
			+ "mfdfzhqqmqg yhtycrznqxgpdzcszcljbhbzcyzzppyzzsgyhc"
			+ "kpzjljnsc sllxb mstldfjmkdjslxlsz p pgjllydszgql l"
			+ "kyyhzttnt  tzzbsz ztlljtyyll llqyzqlbdzlslyyzyfszs"
			+ "nhnc   bbwsk rbc zm  gjmzlshtslzbl q xflyljqbzg st"
			+ "bmzjlxfnb xjztsfjmssnxlkbhsjxtnlzdntljjgzjyjczxygy"
			+ "hwrwqnztn fjszpzshzjfyrdjfcjzbfzqchzxfxsbzqlzsgyft"
			+ "zdcszxzjbqmszkjrhyjzckmjkhchgtxkjqalxbxfjtrtylxjhd"
			+ "tsjx j jjzmzlcqsbtxhqgxtxxhxftsdkfjhzxjfj  zcdlllt"
			+ "qsqzqwqxswtwgwbccgzllqzbclmqqtzhzxzxljfrmyzflxys x"
			+ "xjk xrmqdzdmmyxbsqbhgcmwfwtgmxlzpyytgzyccddyzxs g "
			+ "yjyznbgpzjcqswxcjrtfycgrhztxszzt cbfclsyxzlzqmzlmp"
			+ " lxzjxslbysmqhxxz rxsqzzzsslyflczjrcrxhhzxq dshjsj"
			+ "jhqcxjbcynsssrjbqlpxqpymlxzkyxlxcjlcycxxzzlxlll hr"
			+ "zzdxytyxcxff bpxdgygztcqwyltlswwsgzjmmgtjfsgzyafsm"
			+ "lpfcwbjcljmzlpjjlmdyyyfbygyzgyzyrqqhxy kxygy fsfsl"
			+ "nqhcfhccfxblplzyxxxkhhxshjzscxczwhhhplqalpqahxdlgg"
			+ "gdrndtpyqjjcljzljlhyhyqydhz zczywteyzxhsl jbdgwxpc"
			+ "  tjckllwkllcsstknzdnqnttlzsszyqkcgbhcrrychfpfyrwq"
			+ "pxxkdbbbqtzkznpcfxmqkcypzxehzkctcmxxmx nwwxjyhlstm"
			+ "csqdjcxctcnd p lccjlsblplqcdnndscjdpgwmrzclodansyz"
			+ "rdwjjdbcxwstszyljpxloclgpcjfzljyl c cnlckxtpzjwcyx"
			+ "wfzdknjcjlltqcbxnw xbxklylhzlqzllzxwjljjjgcmngjdzx"
			+ "txcxyxjjxsjtstp ghtxdfptffllxqpk fzflylybqjhzbmddb"
			+ "cycld tddqlyjjwqllcsjpyyclttjpycmgyxzhsztwqwrfzhjg"
			+ "azmrhcyy ptdlybyznbbxyxhzddnh msgbwfzzjcyxllrzcyxz"
			+ "lwjgcggnycpmzqzhfgtcjeaqcpjcs dczdwldfrypysccwbxgz"
			+ "mzztqscpxxjcjychcjwsnxxwjn mt mcdqdcllwnk zgglcczm"
			+ "lbqjqdsjzzghqywbzjlttdhhcchflsjyscgc zjbypbpdqkxwy"
			+ "yflxncwcxbmaykkjwzzzrxy yqjfljphhhytzqmhsgzqwbwjdy"
			+ "sqzxslzyymyszg x hysyscsyznlqyljxcxtlwdqzpcycyppnx"
			+ "fyrcmsmslxglgctlxzgz g tc dsllyxmtzalcpxjtjwtcyyjb"
			+ "lbzlqmylxpghdlssdhbdcsxhamlzpjmcnhjysygchskqmc lwj"
			+ "xsmocdrlyqzhjmyby lyetfjfrfksyxftwdsxxlysjslyxsnxy"
			+ "yxhahhjzxwmljcsqlkydztzsxfdxgzjksxybdpwnzwpczczeny"
			+ "cxqfjykbdmljqq lxslyxxylljdzbsmhpsttqqwlhogyblzzal"
			+ "xqlzerrqlstmypyxjjxqsjpbryxyjlxyqylthylymlkljt llh"
			+ "fzwkhljlhlj klj tlqxylmbtxchxcfxlhhhjbyzzkbxsdqc j"
			+ "zsyhzxfebcqwyyjqtzyqhqqzmwffhfrbntpcjlfzgppxdbbztg"
			+ " gchmfly xlxpqsywmngqlxjqjtcbhxspxlbyyjddhsjqyjxll"
			+ "dtkhhbfwdysqrnwldebzwcydljtmxmjsxyrwfymwrxxysztzzt"
			+ "ymldq xlyq jtscxwlprjwxhyphydnxhgmywytzcs tsdlwdcq"
			+ "pyclqyjwxwzzmylclmxcmzsqtzpjqblgxjzfljjytjnxmcxs c"
			+ "dl dyjdqcxsqyclzxzzxmxqrjhzjphfljlmlqnldxzlllfypny"
			+ "ysxcqqcmjzzhnpzmekmxkyqlxstxxhwdcwdzgyyfpjzdyzjzx "
			+ "rzjchrtlpyzbsjhxzypbdfgzzrytngxcqy b cckrjjbjerzgy"
			+ "  xknsjkljsjzljybzsqlbcktylccclpfyadzyqgk tsfc xdk"
			+ "dyxyfttyh  wtghrynjsbsnyjhkllslydxxwbcjsbbpjzjcjdz"
			+ "bfxxbrjlaygcsndcdszblpz dwsbxbcllxxlzdjzsjy lyxfff"
			+ "bhjjxgbygjpmmmpssdzjmtlyzjxswxtyledqpjmygqzjgdblqj"
			+ "wjqllsdgytqjczcjdzxqgsgjhqxnqlzbxsgzhcxy ljxyxydfq"
			+ "qjjfxdhctxjyrxysqtjxyebyyssyxjxncyzxfxmsyszxy schs"
			+ "hxzzzgzcgfjdltynpzgyjyztyqzpbxcbdztzc zyxxyhhsqxsh"
			+ "dhgqhjhgxwsztmmlhyxgcbtclzkkwjzrclekxtdbcykqqsayxc"
			+ "jxwwgsbhjyzs  csjkqcxswxfltynytpzc czjqtzwjqdzzzqz"
			+ "ljjxlsbhpyxxpsxshheztxfptjqyzzxhyaxncfzyyhxgnxmywx"
			+ "tcspdhhgymxmxqcxtsbcqsjyxxtyyly pclmmszmjzzllcogxz"
			+ "aajzyhjmzxhdxzsxzdzxleyjjzjbhzmzzzqtzpsxztdsxjjlny"
			+ "azhhyysrnqdthzhayjyjhdzjzlsw cltbzyecwcycrylcxnhzy"
			+ "dzydtrxxbzsxqhxjhhlxxlhdlqfdbsxfzzyychtyyjbhecjkgj"
			+ "fxhzjfxhwhdzfyapnpgnymshk mamnbyjtmxyjcthjbzyfcgty"
			+ "hwphftwzzezsbzegpbmtskftycmhbllhgpzjxzjgzjyxzsbbqs"
			+ "czzlzccstpgxmjsftcczjz djxcybzlfcjsyzfgszlybcwzzby"
			+ "zdzypswyjgxzbdsysxlgzybzfyxxxccxtzlsqyxzjqdcztdxzj"
			+ "jqcgxtdgscxzsyjjqcc ldqztqchqqjzyezwkjcfypqtynlmkc"
			+ "qzqzbqnyjddzqzxdpzjcdjstcjnxbcmsjqmjqwwjqnjnlllwqc"
			+ "qqdzpzydcydzcttf znztqzdtjlzbclltdsxkjzqdpzlzntjxz"
			+ "bcjltqjldgdbbjqdcjwynzyzcdwllxwlrxntqqczxkjld tdgl"
			+ " lajjkly kqll dz td ycggjyxdxfrskstqdenqmrkq  hgkd"
			+ "ldazfkypbggpzrebzzykyqspegjjglkqzzzslysywqzwfqzylz"
			+ "zlzhwcgkyp qgnpgblplrrjyxcccyyhsbzfybnyytgzxylxczw"
			+ "h zjzblfflgskhyjzeyjhlplllldzlyczblcybbxbcbpnnzc r"
			+ " sycgyy qzwtzdxtedcnzzzty hdynyjlxdjyqdjszwlsh lbc"
			+ "zpyzjyctdyntsyctszyyegdw ycxtscysmgzsccsdslccrqxyy"
			+ "elsm xztebblyylltqsyrxfkbxsychbjbwkgskhhjh xgnlycd"
			+ "lfyljgbxqxqqzzplnypxjyqymrbsyyhkxxstmxrczzywxyhymc"
			+ "l lzhqwqxdbxbzwzmldmyskfmklzcyqyczqxzlyyzmddz ftqp"
			+ "czcyypzhzllytztzxdtqcy ksccyyazjpcylzyjtfnyyynrs y"
			+ "lmmnxjsmyb sljqyldzdpqbzzblfndsqkczfywhgqmrdsxycyt"
			+ "xnq jpyjbfcjdyzfbrxejdgyqbsrmnfyyqpghyjdyzxgr htk "
			+ "leq zntsmpklbsgbpyszbydjzsstjzytxzphsszsbzczptqfzm"
			+ "yflypybbjgxzmxxdjmtsyskkbzxhjcelbsmjyjzcxt mljshrz"
			+ "zslxjqpyzxmkygxxjcljprmyygadyskqs dhrzkqxzyztcghyt"
			+ "lmljxybsyctbhjhjfcwzsxwwtkzlxqshlyjzjxe mplprcglt "
			+ "zztlnjcyjgdtclklpllqpjmzbapxyzlkktgdwczzbnzdtdyqzj"
			+ "yjgmctxltgcszlmlhbglk  njhdxphlfmkyd lgxdtwzfrjejz"
			+ "tzhydxykshwfzcqshknqqhtzhxmjdjskhxzjzbzzxympagjmst"
			+ "bxlskyynwrtsqlscbpspsgzwyhtlksssw hzzlyytnxjgmjszs"
			+ "xfwnlsoztxgxlsmmlbwldszylkqcqctmycfjbslxclzzclxxks"
			+ "bjqclhjpsqplsxxckslnhpsfqqytxy jzlqldtzqjzdyydjnzp"
			+ "d cdskjfsljhylzsqzlbtxxdgtqbdyazxdzhzjnhhqbyknxjjq"
			+ "czmlljzkspldsclbblzkleljlbq ycxjxgcnlcqplzlznjtzlx"
			+ "yxpxmyzxwyczyhzbtrblxlcczjadjlmmmsssmybhb kkbhrsxx"
			+ "jmxsdynzpelbbrhwghfchgm  klltsjyycqltskywyyhywxbxq"
			+ "ywbawykqldq tmtkhqcgdqktgpkxhcpthtwthkshthlxyzyyda"
			+ "spkyzpceqdltbdssegyjq xcwxssbz dfydlyjcls yzyexcyy"
			+ "sdwnzajgyhywtjdaxysrltdpsyxfnejdy lxllqzyqqhgjhzyc"
			+ "shwshczyjxllnxzjjn fxmfpycyawddhdmczlqzhzyztldywll"
			+ "hymmylmbwwkxydtyldjpyw xjwmllsafdllyflb   bqtzcqlj"
			+ "tfmbthydcqrddwr qnysnmzbyytbjhp ygtjahg tbstxkbtzb"
			+ "kldbeqqhqmjdyttxpgbktlgqxjjjcthxqdwjlwrfwqgwqhckry"
			+ "swgftgygbxsd wdfjxxxjzlpyyypayxhydqkxsaxyxgskqhykf"
			+ "dddpplcjlhqeewxksyykdbplfjtpkjltcyyhhjttpltzzcdlsh"
			+ "qkzjqyste eywyyzy xyysttjkllpwmcyhqgxyhcrmbxpllnqt"
			+ "jhyylfd fxzpsftljxxjbswyysksflxlpplbbblbsfxyzsylff"
			+ "fscjds tztryysyffsyzszbjtbctsbsdhrtjjbytcxyje xbne"
			+ "bjdsysykgsjzbxbytfzwgenhhhhzhhtfwgzstbgxklsty mtmb"
			+ "yxj skzscdyjrcwxzfhmymcxlzndtdh xdjggybfbnbpthfjaa"
			+ "xwfpxmyphdttcxzzpxrsywzdlybbjd qwqjpzypzjznjpzjlzt"
			+ " fysbttslmptzrtdxqsjehbzyj dhljsqmlhtxtjecxslzzspk"
			+ "tlzkqqyfs gywpcpqfhqhytqxzkrsg gsjczlptxcdyyzss qz"
			+ "slxlzmycbcqbzyxhbsxlzdltcdjtylzjyyzpzylltxjsjxhlbr"
			+ "ypxqzskswwwygyabbztqktgpyspxbjcmllxztbklgqkq lsktf"
			+ "xrdkbfpftbbrfeeqgypzsstlbtpszzsjdhlqlzpmsmmsxlqqnk"
			+ "nbrddnxxdhddjyyyfqgzlxsmjqgxytqlgpbqxcyzy drj gtdj"
			+ "yhqshtmjsbwplwhlzffny  gxqhpltbqpfbcwqdbygpnztbfzj"
			+ "gsdctjshxeawzzylltyybwjkxxghlfk djtmsz sqynzggswqs"
			+ "phtlsskmcl  yszqqxncjdqgzdlfnykljcjllzlmzjn   scht"
			+ "hxzlzjbbhqzwwycrdhlyqqjbeyfsjxwhsr  wjhwpslmssgztt"
			+ "ygyqqwr lalhmjtqjcmxqbjjzjxtyzkxbyqxbjxshzssfjlxmx"
			+ "  fghkzszggylcls rjyhslllmzxelgl xdjtbgyzbpktzhkzj"
			+ "yqsbctwwqjpqwxhgzgdyfljbyfdjf hsfmbyzhqgfwqsyfyjgp"
			+ "hzbyyzffwodjrlmftwlbzgycqxcdj ygzyyyyhy xdwegazyhx"
			+ "jlzythlrmgrxxzcl   ljjtjtbwjybjjbxjjtjteekhwslj lp"
			+ "sfyzpqqbdlqjjtyyqlyzkdksqj yyqzldqtgjj  js cmraqth"
			+ "tejmfctyhypkmhycwj cfhyyxwshctxrljhjshccyyyjltktty"
			+ "tmxgtcjtzaxyoczlylbszyw jytsjyhbyshfjlygjxxtmzyylt"
			+ "xxypzlxyjzyzyybnhmymdyylblhlsyygqllscxlxhdwkqgyshq"
			+ "ywljyyhzmsljljxcjjyy cbcpzjmylcqlnjqjlxyjmlzjqlycm"
			+ "hcfmmfpqqmfxlmcfqmm znfhjgtthkhchydxtmqzymyytyyyzz"
			+ "dcymzydlfmycqzwzz mabtbcmzzgdfycgcytt fwfdtzqssstx"
			+ "jhxytsxlywwkxexwznnqzjzjjccchyyxbzxzcyjtllcqxynjyc"
			+ "yycynzzqyyyewy czdcjyhympwpymlgkdldqqbchjxy       "
			+ "                                                  " + "                 sypszsjczc     cqytsjljjt   ";

	public static String getGBKpy(String hzString) throws UnsupportedEncodingException {
//		if (hzString.equals("不详")) {
//			return "";
//		}
		/*
		 * 效率:处理大字符串(字符串有132055个byte,即70577个char)1000次，消耗时间44.474S.
		 */
		if (hzString == null || hzString.length() == 0)
			return "";
		int pyi, len, no;
		int ch1code = 0, ch2code = 0;
		char ch1, ch2;

		StringBuffer pyBuffer = new StringBuffer();
		byte eB[] = hzString.getBytes("GBK");
		len = eB.length;

		// 开始计算
		pyi = 0;
		while (pyi < len) {
			ch1 = (char) eB[pyi];
			pyi = pyi + 1;
			ch1code = ch1;
			if (ch1code > 0 && ch1code < 129) {
				// 普通的acsii
				pyBuffer.append(ch1);
				continue;
			} else {
				// GBK字符
				ch1 = (char) (256 + (int) ch1);
				if (eB[pyi] < 0) {
					ch2 = (char) (256 + (int) eB[pyi]);
				} else {
					ch2 = (char) eB[pyi];
				}
				pyi = pyi + 1;
				if (pyi > len)
					break;
			}
			ch1code = ch1;
			ch2code = ch2;
			if (ch1code <= 254 && ch1code >= 170) {
				// 优先处理GB-2312汉字.
				if (ch2code > 160) {
					// 查找GB-2312
					no = (ch1code - 176) * 94 + (ch2code - 160);
					pyBuffer.append(GB_2312.charAt(no - 1));
				} else {
					// 查找GBK_4
					no = (ch1code - 170) * 97 + (ch2code - 63);
					pyBuffer.append(GBK_4.charAt(no - 1));
				}
			} else if (ch1code <= 160 && ch1code >= 129) {
				// 查找GBK_3
				no = (ch1code - 129) * 191 + (ch2code - 63);
				pyBuffer.append(GBK_3.charAt(no - 1));
			} else {
				// 不是GBK汉字
				continue;
			}
		}
		return pyBuffer.toString().trim().toLowerCase();
	}
	/**
	 * 判断是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
	public static void main(String[] args) throws UnsupportedEncodingException {
		String readFile = FileUtil.readFile("d:\\single.txt", "@@@@@","GBK");
		String[] split = readFile.split("@@@@@");
		for (int i = 0; i < split.length; i++) {
			String v = split[i];
			String[] vs = v.split("	");
			String actorNames = vs[3];//导演
			if(actorNames.indexOf("|")>-1) {
				actorNames=actorNames.substring(0,actorNames.indexOf("|"));
			}
			String actorNamesPY=getGBKpy(actorNames);
			String actorSearchNamesPY = getGBKpy(vs[5]).replaceAll("\\|", ",");
			if(actorSearchNamesPY.length()>0) {
				actorSearchNamesPY=","+actorSearchNamesPY+",";
			}
			String r=vs[0]+"	"+vs[1]+"	"+vs[2]+"	"+actorNames+"	"+actorNamesPY+"	"+vs[5]+"	"+actorSearchNamesPY;
			FileUtil.inContext("d:\\single-n2.txt","\n",r,true,"UTF-8");
		}
		
	}
}
