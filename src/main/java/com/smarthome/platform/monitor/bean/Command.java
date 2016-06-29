package com.smarthome.platform.monitor.bean;

/**
 * 开关设备指令信息
 * 
 * @author yu zhang
 * 
 */
public class Command {
	
	/**
	 *指令ID
	 */
	private int cid;
	
	/**
	 *设备ID
	 */
	private String device_id;
	/**
	 *操作类型  1 关   0 开
	 */
	private int operation;
	
	public Command() {
		super();
	}
	public Command(int cid, String device_id, int operation) {
		super();
		this.cid = cid;
		this.device_id = device_id;
		this.operation = operation;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public int getOperation() {
		return operation;
	}
	public void setOperation(int operation) {
		this.operation = operation;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	
}
