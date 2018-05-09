package zxframe.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 时间处理工具
 *
 * @author 周璇  wing - V 2.0
 * @preserve
 */
public final class DateUtil {
	public  SimpleDateFormat sdf;
	private Calendar cal;
	
	public DateUtil(){
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	public DateUtil(String pattern){
		sdf = new SimpleDateFormat(pattern);
	}
	/**
	 * 根据间隔时间获得日期
	 * @preserve
	 * @param day 间隔时间 如 2(获得后天的日期)
	 * @return date
	 */
	public  Date getDate(int day) {
		cal  = Calendar.getInstance();    
		cal.add(cal.DAY_OF_YEAR, day);
		return cal.getTime();
	}

	/**
	 * 获得已经格式化的当前日期
	 * @preserve
	 * @return string date
	 */
	public  String getDate() {
		cal  = Calendar.getInstance();   
		cal.add(cal.DAY_OF_YEAR, 0);
		return formatString(cal.getTime());
	}

	/**
	 * 格式化时间
	 * @preserve
	 * @param date  需要格式化的日期对象
	 * @return string date
	 */
	public  String formatString(Date date) {
		return sdf.format(date);
	}

	/**
	 * 格式化时间
	 * @preserve
	 * @param date 需要格式化的String日期
	 * @return string date
	 */
	public  String formatString(String date) {
		date = replace(date);
		return sdf.format(new Date(date));
	}
	/**
	 * 将String类型的时间转换为Date类型
	 * @param date 时间
	 * @return Date时间
	 * @preserve
	 */
	public  Date formatDate(String date) {
		date = replace(date);
		try {
			return sdf.parse(sdf.format(new Date(date)));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/***********************
	 * 日差    如果第一个时间：当前时间  第二个时间：昨天  |得1
	 * @param sFrom
	 * @param sTo
	 * @return
	 * @preserve
	 */
	public static int getDiffOfDay(Date sFrom, Date sTo) {
		int nDay = 0;
		nDay = (int)((sFrom.getTime() - sTo.getTime()) / 60 / 1000/60/24);
		return nDay;
	}
	/**
	 * 时差 
	 * @param sFrom
	 * @param sTo
	 * @return
	 * @preserve
	 */
	public static int getDiffOfHour(Date sFrom, Date sTo) {
		int nDay = 0;
		nDay = (int)((sFrom.getTime() - sTo.getTime()) / 60 / 1000/60);
		return nDay;
	}
	
	/****************
	 *秒差 
	 *@preserve
	 */
	public  static int getDiffOfSecond(Date sFrom, Date sTo) {
		int nDay = 0;
		nDay = (int)((sFrom.getTime() - sTo.getTime()) / 1000);
		return nDay;
	}
	/**
	 * 获得某点的秒差，比如到0:0:0点还差多少秒
	 * @param hour 时
	 * @param minute 分
	 * @param second 秒
	 * @return
	 * @preserve
	 */
	public static int getDiffOfSecond(int hour,int minute,int second)
	{
		Calendar cal = Calendar.getInstance();
		//每天定点执行
	    cal.set(Calendar.HOUR_OF_DAY,hour);
	    cal.set(Calendar.MINUTE,minute);
	    cal.set(Calendar.SECOND,second);
	    int time=DateUtil.getDiffOfSecond(cal.getTime(),Calendar.getInstance().getTime());
	    if(time<0)//今天已经过了这个时间
	    {
	    	return 60*60*24+time;
	    }else//今天这个时间还未到
	    {
	    	return time;
	    }
	}
	/**
	 * 获得星期几的某点的秒差，比如到星期一的0:0:0点还差多少秒
	 * @param week 星期几
	 * @param hour 时
	 * @param minute 分
	 * @param second 秒
	 * @return
	 * @preserve
	 */
	public static int getDiffOfSecond(int week,int hour,int minute,int second)
	{
		int day = getDay();
		int nday=0;//还需要多少天
		if(week<day)//本周已经过去了这一天，只有等待下一周的那天
		{
			nday=7-day+week;
		}else
		{
			nday=week-day;
		}
		Date date = Calendar.getInstance().getTime();
		date = addHour(date, 24*nday);
		date.setHours(hour);
		date.setMinutes(minute);
		date.setSeconds(second);
		int time = getDiffOfSecond(date,Calendar.getInstance().getTime());
		if(time<0)//今天已经过了这个时间
	    {
	    	return 60*60*24*7+time;
	    }else//今天这个时间还未到
	    {
	    	return time;
	    }
	}
	/**
	 * 获得某天是星期几
	 * @param date
	 * @return
	 * @preserve
	 */
	public static int getDay(Date date) {
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  int day = cal.get(Calendar.DAY_OF_WEEK);
		  day=day-1;
		  if(day==0)
		  {
			  day=7;
		  }
		  return day;
	}
	/**
	 * 获得今天是星期几
	 * @param date
	 * @return
	 * @preserve
	 */
	public static int getDay() {
		  Calendar cal = Calendar.getInstance();
		  return getDay(cal.getTime());
	}
	/**
	 * 添加 count 小时  
	 * @param d 
	 * @param count 添加小时 
	 * @return
	 * @preserve
	 */
	public static Date  addHour(Date d,int count)
	{
		Calendar ins = Calendar.getInstance();
		ins.setTime(d);
		ins.add(Calendar.HOUR, count);
		return ins.getTime();
	}
	/**
	 * 添加 count月
	 * @param d 
	 * @param count 添加月 
	 * @return
	 * @preserve
	 */
	public static Date addMonth(Date d,int count)
	{
		Calendar ins = Calendar.getInstance();
		ins.setTime(d);
		ins.add(Calendar.MONTH, count);
		return ins.getTime();
	}
	/**
	 * 添加 count秒
	 * @param d 
	 * @param count 添加秒
	 * @return
	 * @preserve
	 */
	public static Date addSecond(Date d,int count)
	{
		Calendar ins = Calendar.getInstance();
		ins.setTime(d);
		ins.add(Calendar.SECOND, count);
		return ins.getTime();
	}
	private static String replace(String date) {
		date = date.replace("-", "/");
		date = date.replace("年", "/");
		date = date.replace("月", "/");
		date = date.replace("日", "");
		return date;
	}
}
