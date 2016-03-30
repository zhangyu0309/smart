package com.smarthome.platform.monitor.bean;

/**
 * 设备信息
 * 
 * @author yu zhang
 * 
 */
public class Device {
	/**
	 * 设备id
	 */
	private String deviceid;
	/**
	 * 是否在线，0=离线，1=在线
	 */
	private int online;

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

}
