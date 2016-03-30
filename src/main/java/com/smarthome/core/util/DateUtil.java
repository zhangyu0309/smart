package com.smarthome.core.util;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理通用类
 * @author RM
 * @version 1.0
 */
public class DateUtil {
	
	
	 private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	 private static SimpleDateFormat dateFormatByLongTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 /**
	  * 将时间转换成"yyyy-MM-dd HH:mm:ss"
	  * @param date
	  * @return
	  */
	 
	 public static String getDateString2ByDate(Date date){
	    	if(date==null){ 
	    		return "";
	    	}else{
	    		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	    	}
	    }
	 /**
	  * 初始化时间
	  * @param date
	  * @param dateFormat
	  * @return
	  */
	 public static String getDateStringByFormatString(Date date,String dateFormat){
	    	if(date==null){ 
	    		return "";
	    	}else{
	    		return new SimpleDateFormat(dateFormat).format(date);
	    	}
	    }
	 /**
	  * 根据时间长度获取规范的时间格式 "yyyy-MM-dd HH:mm:ss"
	  * @param secondTime 毫秒
	  * @return
	  */
	 public static String getDateStringBySecondTime(Long secondTime){
		 if(secondTime!=null){
			 Date nowDate = new Date(secondTime);
			 return DateUtil.dateFormatByLongTime.format(nowDate);
		 }else{
			 return "";
		 }
	 }
	 /**
		 * 根据当前时间前多少的时间
		 * @param preNum 前几个小时
		 * @return
		 */
		public static Date getHoursAgoTime(Date nowDae,int preNum){
			Calendar calendar = Calendar.getInstance(); //得到日历
			
			calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - preNum) ; //把时间设置为当前时间-1小时，同理，也可以设置其他时间
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH").format(calendar.getTime()));
			String tempDate = new SimpleDateFormat("yyyy-MM-dd HH").format(calendar.getTime());
			try {
				 return new SimpleDateFormat("yyyy-MM-dd HH").parse(tempDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new Date();
			}
		}
/**
 * 根据时间获取年份的字符串
 * @param date
 * @return
 */
	    public static String getYearStringByDate(Date date){
	    	if(date==null){ 
	    		return "";
	    	}else{
	    		return new SimpleDateFormat("yyyy").format(date);
	    	}
	    }
	    /**
	     * 根据时间获取月份的字符串
	     * @param date
	     * @return
	     */
	    public static String getMonthStringByDate(Date date){
	    	if(date==null){ 
	    		return "";
	    	}else{
	    		return new SimpleDateFormat("MM").format(date);
	    	}
	    }
	    /**
	     * 获得当前月--开始日期
	     * @param date 当前时间
	     * @return
	     */
	    public static Date getMinMonthDate(Date date) {   
	             Calendar calendar = Calendar.getInstance();   
	              try {
	                 calendar.setTime(date);
	                 calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH)); 
	                 return dateFormat.parse(dateFormat.format(calendar.getTime()));
	               } catch (java.text.ParseException e) {
	               e.printStackTrace();
	              }
	            return null;
	    }
        /**
         * 获得当前月--结束日期
         * @param date  当前时间
         * @return
         */
	    public static Date getMaxMonthDate(Date date){   
	         Calendar calendar = Calendar.getInstance();   
	         try {
	                calendar.setTime(date);
	                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	                return dateFormat.parse(dateFormat.format(calendar.getTime()));
	         }  catch (java.text.ParseException e) {
	                e.printStackTrace();
	          }
	        return null;
	}
	/**
	 * 将时间转换成"yyyyMMdd"形式的字符串
	 * @param date
	 * @return
	 */
	public static String getDateStringByDate(Date date){
		if(date == null) return "";
		return new SimpleDateFormat("yyyyMMdd").format(date); 
	}
	public static String getDateStringYYYYMMDDHHByDate(Date date){
		if(date == null){ return "";}
		String tempDate ="";
		try {
			
			tempDate = new SimpleDateFormat("yyyy-MM-dd HH").format(date);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "未知";
		}
		return tempDate; 
	}
	/**
	 * 根据当前时间前几天的时间
	 * @param preNum 提前的天数
	 * @return
	 */
	public static Date getPreDayByNowDayAndPreNum(int preNum){
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dBefore);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH,preNum);//设置为前preNum天
		dBefore = calendar.getTime();   //得到前一天的时间
		return dBefore;
	}
	/**
	 * 获取所给时间的位移时间
	 * @param tempDate
	 * @param preNum
	 * @return
	 */
	public static Date getPreDayByDateAndPreNum(Date tempDate,int preNum){
		Date dBefore = tempDate;
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dBefore);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH,preNum);//设置为前preNum天
		dBefore = calendar.getTime();   //得到前一天的时间
		return dBefore;
	}
	/**
	 * 获取时间日期
	 * @param tempDate 日期
	 * @param num      
	 * @return
	 */
	public static Date getDayStringByNowDayAndPreNum(Date tempDate,int num){
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(tempDate);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH,num);//设置为前preNum天
		tempDate = calendar.getTime();   //得到前一天的时间
		return tempDate;
	}
	/**
	 * 比较两个时间相差的天数
	 * @param fDate 开始时间
	 * @param oDate 截止时间
	 * @return
	 */
	public static int getIntervalDays(Date fDate, Date oDate) {
	       if (null == fDate || null == oDate) {
	           return -1;
	       }
	       long intervalMilli = oDate.getTime()-fDate.getTime();
	       System.out.println(intervalMilli);
	       return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	 }
	/**
	 * 返回date1 - date2 的分钟数
	 * @param date1  当前时间
	 * @param date2  
	 * @return
	 */
	public static int getMinites(Date date1,Date date2) { 
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		
		int hour = cal1.get(Calendar.HOUR_OF_DAY) - cal2.get(Calendar.HOUR_OF_DAY);
		int minite = cal1.get(Calendar.MINUTE) - cal2.get(Calendar.MINUTE);
		
		return hour * 60 + minite;
	}
	
	/**
	 * 格式化日期 ： yyyy-MM-dd HH:mm:ss
	 * @param date 日期
	 * @return 日期字符串，如果date为空返回空串("")
	 */
	public static String getFormatDateString(Date date) {
		if(date == null) return "";
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
	
	/**
	 * 格式化日期 ： yyyy-MM-dd HH:mm
	 * @param date 日期
	 * @return 日期字符串，如果date为空返回空串("")
	 */
	public static String getFormatDateTimeString(Date date) {
		if(date == null) return "";
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
	}
	/**
	 * 格式化日期 ： yyyy-MM-dd HH:mm
	 * @param date 日期
	 * @return 日期字符串，如果date为空返回空串("")
	 */
	public static Date getFormatDateTimeStringForDate(String date) {
		if(date == null) return null;
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 格式化日期 ： yyyy-MM-dd HH:mm
	 * @param date 日期
	 * @return 日期字符串，如果date为空返回空串("")
	 */
	public static Date getFormatDateTimeStringForDate2(String date) {
		if(date == null) return null;
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 格式化日期 ： yyyy-MM-dd
	 * @param date 日期
	 * @return 日期字符串，如果为空返回空串("")
	 */
	public static String getDateString(Date date) {
		if(date == null) return "";
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
	/**
	 * 格式化日期：yyyy-MM
	 * @param date
	 * @return
	 */
	public static String getDateForMonthString(Date date){
		if(date == null) return "";
		return new SimpleDateFormat("yyyy-MM").format(date);
	}
	
	/**
	 * 格式化时间 ： HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String getTimeString(Date date) {
		if(date == null) return "";
		return new SimpleDateFormat("HH:mm:ss").format(date);
	}
	
	/**
	 * 由字符串转换为日期类型
	 * @param date
	 * @return
	 */
	public static Date getDate(String str, String format) {
		try {
			return new SimpleDateFormat(format).parse(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取日期对应的时间，其中年月日为当前的年月日，时间为参数中的时间
	 * @param time 时间
	 * @return 日期
	 */
	public static Date getDateFromTime(Time time){
		Calendar c = Calendar.getInstance(); 
		try {
			c.setTime(new SimpleDateFormat("HH:mm:ss").parse(time.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		Calendar cal = Calendar.getInstance();
		c.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
		return c.getTime();
	}
	/**
	 * 返回毫秒级别数据
	 * @param date 日期对象 
	 * @return mmssSSS 格式时间
	 */
	public static String getmmssSSS(Date date) {
		return new SimpleDateFormat("mmssSSS").format(date);
	}
	/**
	 * 将时间格式转换成 2015-09-28 12:00:00
	 * @param tempDate 传过来的时间
	 * @return
	 */
	public static Date getDateByDate(Date tempDate){
		SimpleDateFormat simple1 = new SimpleDateFormat("yyyy-MM-dd HH");
		SimpleDateFormat simple2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String tempDateString = simple1.format(tempDate)+":00:00";
		try {
			tempDate = simple2.parse(tempDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempDate;
	}
	
	
}
