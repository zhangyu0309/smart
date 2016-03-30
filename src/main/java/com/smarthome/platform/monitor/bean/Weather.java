package com.smarthome.platform.monitor.bean;

/**
 * 
 * 中央气象局API返回的天气信息
 * 完整天气信息
 * @author yu zhang
 * 
 */
public class Weather {
	/**
	 *
	 */
	private String city;

	/**
	 * 
	 */
	private DayWeather day1;
	
	/**
	 * 
	 */
	private DayWeather day2;
	
	/**
	 * 
	 */
	private DayWeather day3;
	
	/**
	 * 
	 */
	private String ptime;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public DayWeather getDay1() {
		return day1;
	}

	public void setDay1(DayWeather day1) {
		this.day1 = day1;
	}

	public DayWeather getDay2() {
		return day2;
	}

	public void setDay2(DayWeather day2) {
		this.day2 = day2;
	}

	public DayWeather getDay3() {
		return day3;
	}

	public void setDay3(DayWeather day3) {
		this.day3 = day3;
	}

	public String getPtime() {
		return ptime;
	}

	public void setPtime(String ptime) {
		this.ptime = ptime;
	}

}
