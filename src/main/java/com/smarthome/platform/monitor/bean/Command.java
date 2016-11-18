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
	 *操作类型  1 关   0 开 2 分发  3 获取
	 */
	private int operation;
	
	private int board_id;
	
	private int key_id;
	
	private String content;
	
	public Command() {
		super();
	}
	public Command(int cid, String device_id, int operation) {
		super();
		this.cid = cid;
		this.device_id = device_id;
		this.operation = operation;
	}
	
	public Command(int cid, String device_id, int operation, int board_id,
			int key_id, String content) {
		super();
		this.cid = cid;
		this.device_id = device_id;
		this.operation = operation;
		this.board_id = board_id;
		this.key_id = key_id;
		this.content = content;
	}
	public int getBoard_id() {
		return board_id;
	}
	public void setBoard_id(int board_id) {
		this.board_id = board_id;
	}
	public int getKey_id() {
		return key_id;
	}
	public void setKey_id(int key_id) {
		this.key_id = key_id;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
