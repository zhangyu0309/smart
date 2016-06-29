package com.smarthome.platform.monitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.smarthome.core.base.action.BaseAction;
import com.smarthome.core.common.AuthorityCommon;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.platform.authority.bean.Admin;
import com.smarthome.platform.monitor.bean.Device;
import com.smarthome.platform.monitor.bean.SensorData;
import com.smarthome.platform.monitor.service.DeviceService;

/**
 * 设备信息前台接口类
 *
 */
public class DeviceAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	@Resource
	private DeviceService deviceService ;
	private static Logger logger = Logger.getLogger(DeviceAction.class.getName());
	
	/**
	 * 用于查询的
	 */
	private Device device;
	
	/**
	 * 编辑的类型 0 代表添加 1 代表更新  2:管理用户设备
	 */
	private int editType;
	
	/**
	 * 管理设备对应的user_id
	 */
	private String user_id;
	
	/**
	 * 管理类型  1：可以增删改  2：只能查询
	 */
	private int manage_type;
	
	/**
	 * 
	 */
	private String device_id;
	
	private int page;
	
	private int rows;
	
	/**
	 * 操作类型  1：关设备  2：开设备
	 */
	private int option;
	
	
	/**
	 * 查询设备信息
	 * @return
	 */
	public String getAllData(){
		List<Device> resultList = this.deviceService.getDevice(this.device);
		this.jsonString = JsonUtils.getJAVABeanJSON(resultList);
		try {
			this.responseWriter(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 查询当前登录用户的设备信息
	 * @return
	 */
	public String getAllMyData(){
		Admin sessionAdmin = (Admin) this.getSession(AuthorityCommon.ADMIN_SESSION);
		if (sessionAdmin.getUserId() != null && !sessionAdmin.getUserId().equals("")){
			if (this.device == null){
				this.device = new Device();
			}
			this.device.setUser_id(sessionAdmin.getUserId());
			List<Device> resultList = this.deviceService.getDevice(this.device);
			this.jsonString = JsonUtils.getJAVABeanJSON(resultList);
			try {
				this.responseWriter(jsonString);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * 开始添加或者更新功能菜单
	 * @return
	 */
	public String beginAddOrUpdate(){
		
		if(this.editType==0){
			return "add_method";
		}else if(this.editType==1){
			if (this.device == null || this.device.getDevice_id() == null || this.device.getDevice_id().equals("")){
				return null;
			}
			//根据ID号获取对象值
			this.device = this.deviceService.getDeviceById(this.device.getDevice_id());
			if (!this.device.getParent_id().equals("0")){
				this.device.setReal_name(this.device.getDevice_id().split("-")[1]);
			}
			return "update_method";
		}else if(this.editType==2){
			return "manage_method";
		}
		return null;
	}
	
	/**
	 * 获取所有父节点
	 * @return
	 */
	public String getAllParent(){
		this.jsonString = JsonUtils.getJAVABeanJSON(this.deviceService.getAllParentDevice(this.user_id));
		try {
			this.responseWriter(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 添加设备
	 * @return
	 */
	public String add(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.device!=null){
			if (this.device.getOnline().equals("1")){
				//主控板
				this.device.setParent_id("0");
				this.device.setIconCls("ext-icon-computer_add");
			}else if (this.device.getOnline().equals("2")){
				//子设备
				if (this.device.getParent_id() == null || this.device.getParent_id().equals("") || this.device.getParent_id().equals("0")){
					map.put("flag", false);
					map.put("msg", "请选择对应的上级设备！");
					this.jsonString = JsonUtils.getJAVABeanJSON(map);
					try {
						this.responseWriter(this.jsonString);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				this.device.setDevice_id(this.device.getParent_id() + "-" + this.device.getDevice_id());
				this.device.setDevice_id(this.device.getDevice_id().toLowerCase());
				this.device.setIconCls("ext-icon-computer");
			} 
			map = this.deviceService.addDevice(this.device);
		}else{
			map.put("flag", false);
			map.put("msg", "传递的参数为空");
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(map);
		try {
			this.responseWriter(this.jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 编辑
	 * @return
	 */
	public String edit(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.device!=null&&this.device.getDevice_id()!=null){
			if (this.device.getOnline().equals("1")){
				//主控板
				this.device.setParent_id("0");
			}else if (this.device.getOnline().equals("2")){
				//子设备
				if (this.device.getParent_id() == null || this.device.getParent_id().equals("") || this.device.getParent_id().equals("0")){
					map.put("flag", false);
					map.put("msg", "请选择对应的上级设备！");
					this.jsonString = JsonUtils.getJAVABeanJSON(map);
					try {
						this.responseWriter(this.jsonString);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				//new device_id
				this.device.setUser_id(this.device.getParent_id() + "-" + this.device.getDevice_id().split("-")[1]);
				this.device.setUser_id(this.device.getUser_id().toLowerCase());
			} 
			//开启编辑
			map = this.deviceService.updateDevice(this.device);
		}else{
			map.put("flag", false);
			map.put("msg", "传递的参数为空");
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(map);
		try {
			this.responseWriter(this.jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询个人设备数据信息
	 * @return
	 */
	public String getAllSensorData(){
		//先判断当前是否已经登录
		Admin sessionAdmin = (Admin) this.getSession(AuthorityCommon.ADMIN_SESSION);
		if (sessionAdmin == null || sessionAdmin.getUserId() == null){
			return null;
		}
		int start  = rows * (page - 1);
		
		List<SensorData> resultList = this.deviceService.getSensorData(sessionAdmin.getUserId(), start, rows);
		this.jsonString = JsonUtils.getJAVABeanJSON(resultList);
		try {
			this.responseWriter(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 查询指定设备的数据
	 * @return
	 */
	public String getLatestData(){
		//先判断当前是否已经登录
		Admin sessionAdmin = (Admin) this.getSession(AuthorityCommon.ADMIN_SESSION);
		if (sessionAdmin == null || sessionAdmin.getUserId() == null || this.device_id == null || this.device_id.equals("")){
			return null;
		}
		SensorData result = this.deviceService.getLatestData(this.device_id, sessionAdmin.getUserId());
		if (result.getLight() == null || result.getLight().equals("")){
			result.setLight("1");
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(result);
		try {
			this.responseWriter(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
	}
	/**
	 * 删除功能菜单选项
	 * @return
	 */
	public String delete(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.device!=null&&this.device.getDevice_id()!=null){
			//开启删除操作
			map = this.deviceService.delDeviceById(this.device);
		}else{
			map.put("flag", false);
			map.put("msg", "传递的参数为空");
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(map);
		try {
			this.responseWriter(this.jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 开关设备
	 * @return
	 */
	public String onoff(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.device_id!=null && !this.device_id.equals("")){
			map = this.deviceService.onoffDvice(this.device_id, option);
		}else{
			map.put("flag", false);
			map.put("msg", "传递的参数为空");
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(map);
		try {
			this.responseWriter(this.jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 以下方法struts2使用 
	 */

	public DeviceService getDeviceService() {
		return deviceService;
	}

	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Device getDevice() {
		return device;
	}

	public int getEditType() {
		return editType;
	}

	public void setEditType(int editType) {
		this.editType = editType;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getManage_type() {
		return manage_type;
	}

	public void setManage_type(int manage_type) {
		this.manage_type = manage_type;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setOption(int option) {
		this.option = option;
	}

}
