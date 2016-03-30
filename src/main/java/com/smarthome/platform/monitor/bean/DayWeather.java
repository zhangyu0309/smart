package com.smarthome.platform.monitor.bean;

/**
 * 
 * 中央气象局API返回的天气信息
 * 一天天气信息
 * @author yu zhang
 * 
 */
public class DayWeather {

	/**
	 * 
	 */
	private String temp1;
	
	/**
	 * 
	 */
	private String temp2;
	
	/**
	 * 
	 */
	private String weather1;
	
	/**
	 * 
	 */
	private String weather2;
	
	/**
	 * 
	 */
	private String weathercode1;
	
	/**
	 * 
	 */
	private String weathercode2;
	
	/**
	 * 
	 */
	private String winddirection1;
	
	/**
	 * 
	 */
	private String winddirection2;
	
	/**
	 * 
	 */
	private String windstrength1;
	
	/**
	 * 
	 */
	private String windstrength2;
	
	/**
	 * 
	 */
	private String sunrise;
	
	/**
	 * 
	 */
	private String sunset;

	private String icon;
	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	public String getTemp2() {
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	public String getWeather1() {
		return weather1;
	}

	public void setWeather1(String weather1) {
		this.weather1 = weather1;
	}

	public String getWeather2() {
		return weather2;
	}

	public void setWeather2(String weather2) {
		this.weather2 = weather2;
	}

	public String getWeathercode1() {
		return weathercode1;
	}

	public void setWeathercode1(String weathercode1) {
		this.weathercode1 = weathercode1;
	}

	public String getWeathercode2() {
		return weathercode2;
	}

	public void setWeathercode2(String weathercode2) {
		this.weathercode2 = weathercode2;
	}

	public String getWinddirection1() {
		return winddirection1;
	}

	public void setWinddirection1(String winddirection1) {
		this.winddirection1 = winddirection1;
	}

	public String getWinddirection2() {
		return winddirection2;
	}

	public void setWinddirection2(String winddirection2) {
		this.winddirection2 = winddirection2;
	}

	public String getWindstrength1() {
		return windstrength1;
	}

	public void setWindstrength1(String windstrength1) {
		this.windstrength1 = windstrength1;
	}

	public String getWindstrength2() {
		return windstrength2;
	}

	public void setWindstrength2(String windstrength2) {
		this.windstrength2 = windstrength2;
	}

	public String getSunrise() {
		return sunrise;
	}

	public void setSunrise(String sunrise) {
		this.sunrise = sunrise;
	}

	public String getSunset() {
		return sunset;
	}

	public void setSunset(String sunset) {
		this.sunset = sunset;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
