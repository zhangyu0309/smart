package com.smarthome.platform.monitor.bean;

/**
 * 位置（经纬度）信息
 * 
 * @author yu zhang
 * 
 */
public class PositionInfo {
	
	/**
	 * 设备id
	 */
	private String deviceid;
	
	/**
	 * 纬度，如果需要把它转换成度分秒的格式，计算方法： 如接收到的纬度是：4546.40891 4546.40891 / 100 =
	 * 45.4640891 可以直接读出45度 4546.40891–45 * 100 = 46.40891 可以直接读出46分 46.40891–46
	 * = 0.40891 * 60 = 24.5346 读出24秒 所以纬度是：45度46分24秒
	 */
	private String latitude;

	/**
	 * 南北纬，这个位有两种值‘N’（北纬）和‘S’（南纬）
	 */

	private String ns;
	/**
	 * 经度.计算方法和纬度的计算方法一样
	 */
	private String longitude;

	/**
	 * 东西经，这个位有两种值‘E’（东经）和‘W’（西经）
	 */
	private String ew;

	/**
	 * 查询开始时间
	 */
	private String stime;

	/**
	 * 查询结束时间
	 */
	private String etime;

	/**
	 * 初始化查询，set开始和结束时间
	 * @param stime
	 * @param etime
	 */
	public PositionInfo(String deviceid, String stime, String etime) {
		super();
		this.deviceid = deviceid;
		this.stime = stime;
		this.etime = etime;
	}

	/**
	 * 默认构造方法
	 */
	public PositionInfo() {
		super();
	}
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getEw() {
		return ew;
	}

	public void setEw(String ew) {
		this.ew = ew;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

}
