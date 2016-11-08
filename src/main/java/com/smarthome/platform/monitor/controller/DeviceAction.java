package com.smarthome.platform.monitor.controller;

import java.util.ArrayList;
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
import com.smarthome.platform.monitor.bean.DeviceBoard;
import com.smarthome.platform.monitor.bean.DeviceBoardData;
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
	 * 配置参数
	 */
	private String deviceId;
	private String boardId;
	private String keyId;
	private String positionId;
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
//				this.device.setDevice_id(this.device.getParent_id() + "-" + this.device.getDevice_id());
//				this.device.setDevice_id(this.device.getDevice_id().toLowerCase());
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
	 * 查询所有主设备
	 * @return
	 */
	public String getAllMain(){
		Map<String,Object> map = new HashMap<String,Object>();
		Admin sessionAdmin = (Admin) this.getSession(AuthorityCommon.ADMIN_SESSION);
		if (sessionAdmin.getUserId() != null && !sessionAdmin.getUserId().equals("")){
			List<Device> list = this.deviceService.getAllMainDevice(sessionAdmin.getUserId());
			if(list != null){
				map.put("rows", list);
				map.put("total", list.size());
			}
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(map);
		try {
			this.responseWriter(this.jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage() , e);
		}
		return null;
	}
	/**
	 * 根据查询条件查询所有参数配置
	 * @return
	 */
	public String getAllKeysBy(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<DeviceBoard> list = this.deviceService.getAllKeysByDevice(this.deviceId, this.boardId, this.keyId);
		List<DeviceBoardData> resultList = new ArrayList<DeviceBoardData>();
		if(list != null){
			/**
			 * boardId:"2"
			 * deviceId:"1988606"
			 * deviceName:"主板"
			 * keyId:"1"
			 * value1:"0x00000000"
			 * value2:"0x00000000"
			 */
			for(DeviceBoard boardData : list){
				//64位0 1
				String btString = get32bitStringFromHex(boardData.getValue1()) + get32bitStringFromHex(boardData.getValue2());
				for (int i = 63; i >= 0; i--) {
					resultList.add(new DeviceBoardData(boardData.getDeviceId(), 
							boardData.getDeviceName(),
							boardData.getBoardId(),
							boardData.getKeyId(),
							Integer.toString(63 - i),
							"子设备" + Integer.toString(64 - i),
							String.valueOf(btString.charAt(i))
							));
				}
			}
			map.put("rows", resultList);
			map.put("total", resultList.size());
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(map);
		try {
			this.responseWriter(this.jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage() , e);
		}
		return null;
	}
	
	/**
	 * 参数配置中 关子设备
	 * @return
	 */
	public String stopSub(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.deviceId!=null && this.boardId!=null&&
				this.keyId!=null && this.positionId!=null){
			map = this.deviceService.startShutSubDvice(this.deviceId, this.boardId, this.keyId, this.positionId, 0);
		}else{
			map.put("flag", false);
			map.put("msg", "请先选择要关闭的子设备");
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
	 * 分发
	 * @return
	 */
	public String distributeKey(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.deviceId!=null && !this.deviceId.trim().equals("") && !this.deviceId.trim().equals(",")){
			if(this.boardId!=null && !this.boardId.trim().equals("")){
				if(this.keyId!=null && !this.keyId.trim().equals("")){
					map = this.deviceService.distributeOrGetKey(this.deviceId,this.boardId, this.keyId, 2);
				}else{
					map.put("flag", false);
					map.put("msg", "请先选择要分发的按键");
				}
			}else{
				map.put("flag", false);
				map.put("msg", "请先选择要分发的组号");
			}
		}else{
			map.put("flag", false);
			map.put("msg", "请先选择要分发的设备");
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
	 * 获取
	 * @return
	 */
	public String getKey(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.deviceId!=null && !this.deviceId.trim().equals("") && !this.deviceId.trim().equals(",")){
			map = this.deviceService.distributeOrGetKey(this.deviceId, null, null, 3);
		}else{
			map.put("flag", false);
			map.put("msg", "请先选择要获取的设备");
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
	 * 参数配置中 开子设备
	 * @return
	 */
	public String startSub(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.deviceId!=null && this.boardId!=null&&
				this.keyId!=null && this.positionId!=null){
			map = this.deviceService.startShutSubDvice(this.deviceId, this.boardId, this.keyId, this.positionId, 1);
		}else{
			map.put("flag", false);
			map.put("msg", "请先选择要打开的子设备");
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
	 * 
	 * @param sourceString  0x0000001f
	 * @return  00000000000000000000000000011111
	 */
	private String get32bitStringFromHex(String sourceString){
		if (sourceString == null || sourceString.length() != 10){
			return "";
		}
		if (sourceString.equals("0xffffffff")){
			return "99999999999999999999999999999999";
		}
		try{
			int sourceInt = Integer.parseInt(sourceString.replace("0x", ""), 16);
			String sBString = Integer.toBinaryString(sourceInt);
			int length = sBString.length();
			for (int i = 1; i <= 32-length; i++){
				sBString = "0" + sBString;
			}
			return sBString;
		}catch(Exception e){
			return "";
		}
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

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	
}
