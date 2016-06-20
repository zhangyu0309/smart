package com.smarthome.platform.monitor.common;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class Constant {
	public static Logger logger = Logger.getLogger(Constant.class);
	/**
	 * 服务启动时 是否允许socket监听
	 */
	public static final boolean IS_RUN_SOCKET = true;
	/**
	 * 监听端口
	 */
	public static final int LISTEN_PORT = 5001;
	
	/**
	 * socket超时时间
	 */
	public static final int SOCKET_WAIT_TIME = 18000;
	
	/**
	 * 气象局开放端口appid
	 */
	public static String appid="2af329b6baa66f96";
	
	/**
	 * 气象局开放端口密钥
	 */
	public static String appkey="fb2953_SmartWeatherAPI_b40aeb3";
	
	/**
	 * 气象编码
	 */
	public static HashMap<String, String> weatherMap = new HashMap<String, String>();
	
	/**
	 * 风向编码
	 */
	public static HashMap<String, String> windDirectionMap = new HashMap<String, String>();
	
	/**
	 * 风力编码
	 */
	public static HashMap<String, String> windStrengthMap = new HashMap<String, String>();
	
	/**
	 * 气象编码
	 */
	public static HashMap<String, String> iconMap = new HashMap<String, String>();
	
	static{
		weatherMap.put("00", "晴");
		weatherMap.put("01", "多云");
		weatherMap.put("02", "阴");
		weatherMap.put("03", "阵雨");
		weatherMap.put("04", "雷阵雨");
		weatherMap.put("05", "雷阵雨伴有冰雹");
		weatherMap.put("06", "雨夹雪");
		weatherMap.put("07", "小雨");
		weatherMap.put("08", "中雨");
		weatherMap.put("09", "大雨");
		weatherMap.put("10", "暴雨");
		weatherMap.put("11", "大暴雨");
		weatherMap.put("12", "特大暴雨");
		weatherMap.put("13", "阵雪");
		weatherMap.put("14", "小雪");
		weatherMap.put("15", "中雪");
		weatherMap.put("16", "大雪");
		weatherMap.put("17", "暴雪");
		weatherMap.put("18", "雾");
		weatherMap.put("19", "冻雨");
		weatherMap.put("20", "沙尘暴");
		weatherMap.put("21", "小到中雨");
		weatherMap.put("22", "中到大雨");
		weatherMap.put("23", "大到暴雨");
		weatherMap.put("24", "暴雨到大暴雨");
		weatherMap.put("25", "大暴雨到特大暴雨");
		weatherMap.put("26", "小到中雪");
		weatherMap.put("27", "中到大雪");
		weatherMap.put("28", "大到暴雪");
		weatherMap.put("29", "浮尘");
		weatherMap.put("30", "扬沙");
		weatherMap.put("31", "强沙尘暴");
		weatherMap.put("53", "霾");
		weatherMap.put("99", "无");
		
		windDirectionMap.put("0", "无持续风向");
		windDirectionMap.put("1", "东北风");
		windDirectionMap.put("2", "东风");
		windDirectionMap.put("3", "东南风");
		windDirectionMap.put("4", "南风");
		windDirectionMap.put("5", "西南风");
		windDirectionMap.put("6", "西风");
		windDirectionMap.put("7", "西北风");
		windDirectionMap.put("8", "北风");
		windDirectionMap.put("9", "旋转风");
		
		windStrengthMap.put("0", "微风");
		windStrengthMap.put("1", "3-4级");
		windStrengthMap.put("2", "4-5级");
		windStrengthMap.put("3", "5-6级");
		windStrengthMap.put("4", "6-7级");
		windStrengthMap.put("5", "7-8级");
		windStrengthMap.put("6", "8-9级");
		windStrengthMap.put("7", "9-10级");
		windStrengthMap.put("8", "10-11级");
		windStrengthMap.put("9", "11-12级");
		
		iconMap.put("00", "00,00-n");
		iconMap.put("01", "01,01-n");
		iconMap.put("02", "01,02-n");
		iconMap.put("03", "8");
		iconMap.put("04", "15");
		iconMap.put("05", "14");
		iconMap.put("06", "16");
		iconMap.put("07", "8");
		iconMap.put("08", "9");
		iconMap.put("09", "10");
		iconMap.put("10", "11");
		iconMap.put("11", "12");
		iconMap.put("12", "13");
		iconMap.put("13", "17");
		iconMap.put("14", "17");
		iconMap.put("15", "18");
		iconMap.put("16", "19");
		iconMap.put("17", "20");
		iconMap.put("18", "21");
		iconMap.put("19", "8");
		iconMap.put("20", "7");
		iconMap.put("21", "8");
		iconMap.put("22", "10");
		iconMap.put("23", "11");
		iconMap.put("24", "12");
		iconMap.put("25", "13");
		iconMap.put("26", "17");
		iconMap.put("27", "19");
		iconMap.put("28", "20");
		iconMap.put("29", "21");
		iconMap.put("30", "22");
		iconMap.put("31", "23");
		iconMap.put("53", "24");
		iconMap.put("99", "");
	}
	
	/**
	 * 开始标记
	 */
	public static final byte[] START = "START".getBytes();
	
	/**
	 * 结束标记
	 */
	public static final byte[] CLOSE = "CLOSE".getBytes();
	
	/**
	 * 查询所有传感器数据
	 */
	public static final byte[] GET_DATA = {0x3a,0x00,(byte) 0xff,0x04,(byte) 0xc1,0x23};
	
	/**
	 * 开设备
	 */
	public static final byte[] OPEN_DEVICE = {0x3a,0x00,0x01,0x0a,0x00,0x31,0x23};
	
	/**
	 * 关设备
	 */
	public static final byte[] CLOSE_DEVICE = {0x3a,0x00,0x01,0x0a,0x01,0x30,0x23};
	
	/**
	 * 每条子设备数据的长度
	 */
	public static int data_length = 20;
}
