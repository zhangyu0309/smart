package com.smarthome.platform.monitor.bean;

/**
 * 设备信息
 * 
 * @author yu zhang
 * 
 */
public class Device {
	/**
	 *设备编号
	 */
	private String device_id;
	/**
	 * 设备名称
	 */
	private String device_name;

	/**
	 * 对应wifi board 的id。如果本身就是wifi board，则为0
	 */
	private String parent_id;
	
	/**
	 * 是否在线 0 or 1
	 */
	private String online;
	
	/**
	 * 设备描述
	 */
	private String description;
	
	/**
	 * 设备创建时间
	 */
	private String create_time;
	
	/**
	 * 查询开始时间
	 */
	private String start_time;
	
	/**
	 * 查询结束时间
	 */
	private String end_time;
	
	/**
	 * 设备图标
	 */
	private String iconCls;
	
	/**
	 * 设备图标
	 */
	private String user_id;
	
	/**
	 * 设备图标
	 */
	private String real_name;
	
	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	
}
