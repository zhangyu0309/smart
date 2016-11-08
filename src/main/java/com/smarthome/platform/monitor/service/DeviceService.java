package com.smarthome.platform.monitor.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.smarthome.core.util.ByteUtil;
import com.smarthome.platform.monitor.bean.Device;
import com.smarthome.platform.monitor.bean.DeviceBoard;
import com.smarthome.platform.monitor.bean.SensorData;
import com.smarthome.platform.monitor.dao.DeviceDao;
import com.smarthome.platform.monitor.dao.DeviceJDBCDao;

/**
 * 设备管理业务逻辑层的处理
 * @author zy
 *
 */
@Service
public class DeviceService {

	@Resource
	private DeviceDao dao;
	@Resource
	private DeviceJDBCDao jdbcDao;
	/**
	 * 查询设备信息
	 * @return
	 */
	public List<Device> getDevice(Device device) {
		return dao.getDevice(device);
	}
	/**
	 * 获取所有父节点(wifi board)
	 * @return
	 */
	public List<Device> getAllParentDevice(String user_id) {
		if (user_id == null || user_id.equals("")){
			return null;
		}
		return dao.getAllParentDevice(user_id);
	}
	/**
	 * 根据ID查询设备
	 * @param device_id
	 * @return
	 */
	public Device getDeviceById(String device_id) {
		return dao.getDeviceById(device_id);
	}
	/**
	 * 添加
	 * @param device
	 * @return
	 */
	public Map<String, Object> addDevice(Device device) {
		Map<String,Object> map = new HashMap<String,Object>();
		if (jdbcDao.getCountByDeviceId(device.getDevice_id()) > 0){
			map.put("flag", false);
			map.put("msg", "设备ID已存在，不允许重复！");
			return map;
		}
		if (dao.addDevice(device) > 0){
			map.put("flag", true);
			map.put("msg", "添加设备成功！");
			if(device.getParent_id().equals("0")){
				dao.addBoardKey(device);
			}
		}else{
			map.put("flag", false);
			map.put("msg", "添加设备失败，数据库连接异常");
		}
		return map;
	}
	/**
	 * 编辑
	 * @param device
	 * @return
	 */
	public Map<String, Object> updateDevice(Device device) {
		Map<String,Object> map = new HashMap<String,Object>();
//		if (device.getUser_id() != null && !device.getUser_id().equals("") 
//				&& jdbcDao.getCountByDeviceId(device.getUser_id()) > 0){
//			map.put("flag", false);
//			map.put("msg", "新的上级设备下该子设备ID已存在，不允许重复！");
//			return map;
//		}
		if(dao.updateDevice(device) > 0){
			map.put("flag", true);
			map.put("msg", "更新设备成功！");
		}else{
			map.put("flag", false);
			map.put("msg", "更新设备失败，数据库连接异常");
		}
		return map;
	}
	/**
	 * 删除
	 * @param device
	 * @return
	 */
	public Map<String, Object> delDeviceById(Device device) {
		Map<String,Object> map = new HashMap<String,Object>();
		if(dao.delDeviceById(device) > 0){
			map.put("flag", true);
			map.put("msg", "删除设备成功！");
			dao.delDeviceBoardInfoById(device);
		}else{
			map.put("flag", false);
			map.put("msg", "删除设备失败，数据库连接异常！");
		}
		return map;
	}
	
	/**
	 * 获取子设备的传感器数据
	 * @param user_id 
	 * @param page 
	 * @param start 
	 * @return
	 */
	public List<SensorData> getSensorData(String user_id, int start, int page) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		map.put("start", start);
		map.put("limit", page);
		return dao.getSensorData(map);
	}
	
	public SensorData getLatestData(String device_id, String user_id) {
		if (jdbcDao.getCountByDeviceIdAndUserId(device_id, user_id) == 0){
			return null;
		}
		return dao.getLatestData(device_id);
	}
	
	public Map<String, Object> onoffDvice(String device_id, int option) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("device_id", device_id);
		map.put("option", option);
		dao.deleteCommand(device_id);
		if(dao.onoffDvice(map) > 0){
			map.put("flag", true);
			map.put("msg", "发送指令成功！");
		}else{
			map.put("flag", false);
			map.put("msg", "发送指令失败，数据库连接异常！");
		}
		return map;
	}
	
	public List<Device> getDeviceList(String string) {
		if (string == null || string.equals("")){
			return new ArrayList<Device>();
		}
		return dao.getDeviceList(string);
	}
	
	public List<Device> getAllMainDevice(String userId) {
		return dao.getAllParentDevice(userId);
	}
	public List<DeviceBoard> getAllKeysByDevice(String deviceId,
			String boardId, String keyId) {
		if (deviceId == null && boardId == null && keyId == null){
			return new ArrayList<DeviceBoard>();
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("deviceId", (deviceId == null || deviceId.equals("")) ? null : deviceId);
		map.put("boardId", (boardId == null || boardId.equals("")) ? null : boardId);
		map.put("keyId", (keyId == null || keyId.equals("")) ? null : keyId);
		return dao.getAllKeysByDevice(map);
	}
	
	/**
	 * 
	 * @param deviceId
	 * @param boardId
	 * @param keyId
	 * @param positionId
	 * @param i
	 * @return
	 */
	public Map<String, Object> startShutSubDvice(String deviceId,
			String boardId, String keyId, String positionId, int i) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("deviceId", deviceId);
		paramMap.put("boardId", boardId);
		paramMap.put("keyId", keyId);
		DeviceBoard db = dao.getDeviceBoard(paramMap);
		if(db != null){
			//高位
			if (Integer.parseInt(positionId) > 31){
				if (db.getValue1().equals("0xffffffff")){
					db.setValue1(i == 0 ? "0x00000000" : "0x00ffffff");
				}else {
					String bitString = get32bitStringFromHex(db.getValue1()) + get32bitStringFromHex(db.getValue2());
					int changeIndex = 63 - Integer.parseInt(positionId);
					if (changeIndex == 63){
						bitString = bitString.substring(0, changeIndex) + i;
					}else{
						bitString = bitString.substring(0, changeIndex) + i + bitString.substring(changeIndex + 1);
					}
					db.setValue1(gethexStringFromBits(bitString.substring(0, 32)));
				}
			}
			if (Integer.parseInt(positionId) <= 31){
				if (db.getValue2().equals("0xffffffff")) {
					db.setValue2(i == 0 ? "0x00000000" : "0x00ffffff");
				}else {
					String bitString = get32bitStringFromHex(db.getValue1()) + get32bitStringFromHex(db.getValue2());
					int changeIndex = 63 - Integer.parseInt(positionId);
					if (changeIndex == 63){
						bitString = bitString.substring(0, changeIndex) + i;
					}else{
						bitString = bitString.substring(0, changeIndex) + i + bitString.substring(changeIndex + 1);
					}
					db.setValue2(gethexStringFromBits(bitString.substring(32)));
				}
			}
			dao.updateKey(db);
			map.put("flag", true);
			map.put("msg", "操作成功！");
		}else {
			map.put("flag", false);
			map.put("msg", "操作异常，该设备不存在！");
		}
//		if(dao.delDeviceById(device) > 0){
//			map.put("flag", true);
//			map.put("msg", "删除设备成功！");
//			dao.delDeviceBoardInfoById(device);
//		}else{
//			map.put("flag", false);
//			map.put("msg", "删除设备失败，数据库连接异常！");
//		}
		return map;
	}
	/**
	 * 
	 * @param sourceString
	 * @return
	 */
	private String gethexStringFromBits(String sBString){
		if (sBString == null || sBString.length() != 32){
			return "";
		}
		if (sBString.equals("99999999999999999999999999999999")){
			return "0xffffffff";
		}
		try{
			byte[] bytes = new byte[]{ByteUtil.bitToByte(sBString.substring(0, 8)),ByteUtil.bitToByte(sBString.substring(8, 16)),ByteUtil.bitToByte(sBString.substring(16, 24)),ByteUtil.bitToByte(sBString.substring(24))};
			 String getString = Integer.toHexString(ByteUtil.bytes2int(bytes));
			 int getlength = getString.length();
			 for (int i = 1; i <= 8-getlength; i++){
				 getString = "0" + getString;
			 }
			return ("0x" + getString);
		}catch(Exception e){
			return "";
		}
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
	 * 
	 * @param deviceId
	 * @param keyId 
	 * @param boardId 
	 * @param i  2分发3获取
	 * @return
	 */
	public Map<String, Object> distributeOrGetKey(String deviceId, String boardId, String keyId, int i) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("deviceId", deviceId);
		map.put("device_id", deviceId);
		map.put("boardId", boardId);
		map.put("keyId", keyId);
		map.put("option", i);
		dao.deleteDisOrGetCommand(map);
		if(dao.onoffDvice(map) > 0){
			Device device = dao.getDeviceById(deviceId);
			if (device.getOnline().equals("0")){
				map.put("flag", true);
				map.put("msg", "发送指令成功！但设备未在线，指令将在设备下次上线时执行");
			}else {
				map.put("flag", true);
				map.put("msg", "发送指令成功！");
			}
		}else{
			map.put("flag", false);
			map.put("msg", "发送指令失败，数据库连接异常！");
		}
		return map;
	}
}
