package com.smarthome.platform.monitor.bean;

/**
 * 数据信息
 * 
 * @author yu zhang
 * 
 */
public class SensorData {
	/**
	 *
	 */
	private String device_id;
	/**
	 *
	 */
	private String device_name;
	/**
	 * 
	 */
	private String temp1;
	
	/**
	 * 
	 */
	private String wet1;
	
	/**
	 * 
	 */
	private String temp2;
	
	/**
	 * 
	 */
	private String wet2;
	
	/**
	 * 
	 */
	private String light;

	/**
	 * 
	 */
	private String data_time;
	
	/**
	 * 
	 */
	private String online;
	
	public SensorData() {
		super();
	}

	public SensorData(String device_id, String temp1, String wet1,
			String temp2, String wet2, String light) {
		super();
		this.device_id = device_id;
		this.temp1 = temp1;
		this.wet1 = wet1;
		this.temp2 = temp2;
		this.wet2 = wet2;
		this.light = light;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	public String getWet1() {
		return wet1;
	}

	public void setWet1(String wet1) {
		this.wet1 = wet1;
	}

	public String getTemp2() {
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	public String getWet2() {
		return wet2;
	}

	public void setWet2(String wet2) {
		this.wet2 = wet2;
	}

	public String getLight() {
		return light;
	}

	public void setLight(String light) {
		this.light = light;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public String getData_time() {
		return data_time;
	}

	public void setData_time(String data_time) {
		this.data_time = data_time;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

}
