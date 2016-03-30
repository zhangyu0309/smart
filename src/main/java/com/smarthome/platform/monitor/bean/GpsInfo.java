package com.smarthome.platform.monitor.bean;
/**
 * GPS信息
 * 
 * @author yu zhang
 *
 */
public class GpsInfo extends PositionInfo {
	
	/**
	 * UTC时间，hhmmss(时分秒)格式
	 */
	private String time;
	/**
	 * 定位状态，A=有效定位，V=无效定位
	 */
	private String state;
	
	/**
	 *  地面速率(000.0~999.9节，前面的0也将被传输)
	 */
	private String speed;
	
	/**
	 * 地面航向(000.0~359.9度，以真北为参考基准，前面的0也将被传输)
	 */
	private String direction;
	/**
	 * UTC日期，ddmmyy(日月年)格式
	 */
	private String date;
	
	/**
	 * 磁偏角(000.0~180.0度，前面的0也将被传输)
	 */
	private String angle;
	
	/**
	 * 磁偏角方向，E(东)或W(西)
	 */
	private String angledirection;
	
	/**
	 *  使用卫星数量，从00到12(第一个零也将传送)
	 */
	private String satellites;
	
	/**
	 * 天线离海平面的高度，-9999.9到9999.9米  M  指单位米
	 */
	private String elevation;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAngle() {
		return angle;
	}

	public void setAngle(String angle) {
		this.angle = angle;
	}

	public String getAngledirection() {
		return angledirection;
	}

	public void setAngledirection(String angledirection) {
		this.angledirection = angledirection;
	}

	public String getSatellites() {
		return satellites;
	}

	public void setSatellites(String satellites) {
		this.satellites = satellites;
	}

	public String getElevation() {
		return elevation;
	}

	public void setElevation(String elevation) {
		this.elevation = elevation;
	}

}
