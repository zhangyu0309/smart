package com.smarthome.platform.monitor.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.smarthome.core.util.ByteUtil;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.platform.monitor.bean.Command;
import com.smarthome.platform.monitor.bean.Device;
import com.smarthome.platform.monitor.bean.DeviceBoard;
import com.smarthome.platform.monitor.bean.SensorData;
import com.smarthome.platform.monitor.bean.Timer;
import com.smarthome.platform.monitor.common.Constant;
import com.smarthome.platform.monitor.dao.DeviceDao;
import com.smarthome.platform.monitor.dao.DeviceJDBCDao;
import com.smarthome.platform.monitor.dao.HostJDBCDao;

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
	
	private static Logger logger = Logger.getLogger(DeviceService.class.getName());
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
	
	public Map<String, Object> onoffDvice(String device_id, int option, String content) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("device_id", device_id);
		map.put("option", option);
		map.put("content", content);
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
			String boardId, String keyId, String all, String userId) {
		if (deviceId == null && boardId == null && keyId == null && all == null){
			return new ArrayList<DeviceBoard>();
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("deviceId", (deviceId == null || deviceId.equals("")) ? null : deviceId);
		map.put("boardId", (boardId == null || boardId.equals("")) ? null : boardId);
		map.put("keyId", (keyId == null || keyId.equals("")) ? null : keyId);
		map.put("userId", userId);
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
			long sourceInt = Long.parseLong(sourceString.replace("0x", ""), 16);
			String sBString = Long.toBinaryString(sourceInt);
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
		logger.info("add command:" + JsonUtils.getJAVABeanJSON(map));
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
	
	public Map<String, Object> setConf(String device_id, String boardId,
			String keyId, String hvalue, String lvalue) {
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if (hvalue.length() != 10 || !hvalue.startsWith("0x")){
			map.put("flag", false);
			map.put("msg", "0010");
		}else if (lvalue.length() != 10 || !lvalue.startsWith("0x")){
			map.put("flag", false);
			map.put("msg", "0011");
		}else if (!(boardId.equals("1") || boardId.equals("2") || boardId.equals("3"))){
			map.put("flag", false);
			map.put("msg", "0012");
		}else if (!(keyId.equals("1") || keyId.equals("2") || keyId.equals("3")|| keyId.equals("4") 
				|| keyId.equals("5")|| keyId.equals("6")|| keyId.equals("7"))){
			map.put("flag", false);
			map.put("msg", "0013");
		}else{
			paramMap.put("device_id", device_id);
			paramMap.put("option", 4);
			paramMap.put("boardId", boardId);
			paramMap.put("keyId", keyId);
			paramMap.put("content", "KEYCONF:"+keyId+","+hvalue+","+lvalue+".BOARD:"+boardId+".");
			try{
				Device device = dao.getDeviceById(device_id);
				if (device.getOnline().equals("0")){
					map.put("flag", false);
					map.put("msg", "0014");
				}else {
					dao.onoffDvice(paramMap);
					map.put("flag", true);
					map.put("msg", "0001");
				}
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				map.put("flag", false);
				map.put("msg", "0000");
			}
		}
		return map;
	}
	
	public Map<String, Object> getConf(String device_id, String boardId,
			String keyId) {

		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if (!(boardId.equals("1") || boardId.equals("2") || boardId.equals("3"))){
			map.put("flag", false);
			map.put("msg", "0012");
		}else if (!(keyId.equals("1") || keyId.equals("2") || keyId.equals("3")|| keyId.equals("4") 
				|| keyId.equals("5")|| keyId.equals("6")|| keyId.equals("7"))){
			map.put("flag", false);
			map.put("msg", "0013");
		}else{
			paramMap.put("device_id", device_id);
			paramMap.put("option", 5);
			paramMap.put("boardId", boardId);
			paramMap.put("keyId", keyId);
			Device device = dao.getDeviceById(device_id);
			if (device.getOnline().equals("0")){
				map.put("flag", false);
				map.put("msg", "0014");
			}else {
				if(dao.onoffDvice(paramMap) > 0){
					int updated = dao.getUpdatedCount(paramMap);
					long start = System.currentTimeMillis();
					while (true && (System.currentTimeMillis() - start <= 60000)) {
						logger.info("wait for update...");
						if(updated == 1){
							DeviceBoard db = HostJDBCDao.getDeviceBoard(device_id, Integer.parseInt(boardId),
									Integer.parseInt(keyId));
							map.put("flag", true);
							map.put("msg", "0001");
							map.put("hvalue", db.getValue1());
							map.put("lvalue", db.getValue2());
							break;
						}
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						updated = dao.getUpdatedCount(paramMap);
					}
				}else {
					map.put("flag", false);
					map.put("msg", "0000");
				}
			}
		}
		HostJDBCDao.setKeyUpdated(new Command(0, device_id, 0, Integer.parseInt(boardId), Integer.parseInt(keyId), ""), 0);
		return map;
	
	}
	/**
	 * 根据id查询设备board信息
	 * @param id
	 * @return
	 */
	public DeviceBoard getDeviceBoardById(String id) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", id);
		return dao.getDeviceBoardById(paramMap);
	}
	/**
	 * 编辑场景
	 * @param id
	 * @param sceneName
	 * @param onoff
	 * @return
	 */
	public Map<String, Object> editScene(String id, String sceneName,
			String onoff) {
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", id);
		paramMap.put("sceneName", (sceneName == null || sceneName.equals("")) ? null : sceneName);
		paramMap.put("onoff", (sceneName == null || sceneName.equals("")) ? onoff : null);
		
		if (onoff != null && onoff.equals("1")) {
			DeviceBoard db = dao.getDeviceBoardById(paramMap);
			Device device = dao.getDeviceById(db.getDeviceId());
			if (device.getOnline().equals("0")){
				map.put("flag", false);
				map.put("msg", "设备当前不在线！");
				return map;
			}
			paramMap.put("deviceId", db.getDeviceId());
			dao.closeAllScene(paramMap);
			dao.deleteSceneCommand(paramMap);
			dao.insertSceneCommand(paramMap);
		}
		try {
			dao.editScene(paramMap);
			map.put("flag", true);
			map.put("msg",  (sceneName == null || sceneName.equals("")) ? "开启场景成功" : "修改场景成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			map.put("flag", false);
			map.put("msg", "修改场景失败");
		}
		return map;
	}
	/**
	 * 重命名设备
	 * @param user_idString
	 * @param device_id
	 * @param device_name
	 * @return
	 */
	public Map<String, Object> renameDevice(String user_idString,
			String device_id, String device_name) {
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("userId", user_idString);
		paramMap.put("deviceId", device_id);
		paramMap.put("deviceName", device_name);
		try{
			int count = dao.getUserDeviceCount(paramMap);
			if (count == 1){
				dao.renameUserDeviceCount(paramMap);
				map.put("flag", true);
				map.put("msg", "0001");
			}else {
				map.put("flag", false);
				map.put("msg", "0008");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			map.put("flag", false);
			map.put("msg", "0000");
		}
		return map;
	}
	/**
	 * 应用添加子设备
	 * @param user_idString
	 * @param device_id
	 * @param device_name
	 * @param device_type
	 * @param parent_id
	 * @return
	 */
	public Map<String, Object> addSubDevice(String user_idString,
			String device_id, String device_name, String device_type, String parent_id) {
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("userId", user_idString);
		paramMap.put("deviceId", parent_id);
		try{
			int count = dao.getUserDeviceCount(paramMap);
			if (count == 1){
				paramMap.put("deviceName", device_name);
				paramMap.put("deviceType", device_type);
				paramMap.put("parentId", parent_id);
				paramMap.put("deviceId", parent_id + "-" + device_id);
				int deviceCount = dao.getUserDeviceCount(paramMap);
				if(deviceCount == 0){
					dao.addSubDevice(paramMap);
					map.put("flag", true);
					map.put("msg", "0001");
				}else {
					map.put("flag", false);
					map.put("msg", "0007");
				}
			}else {
				map.put("flag", false);
				map.put("msg", "0008");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			map.put("flag", false);
			map.put("msg", "0000");
		}
		return map;
	}
	/**
	 * 获取场景列表
	 * @param user_idString
	 * @param device_id
	 * @return
	 */
	public List<DeviceBoard> getSceneList(String user_idString, String device_id) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("userId", user_idString);
		paramMap.put("deviceId", device_id);
		try{
			int count = dao.getUserDeviceCount(paramMap);
			if (count == 1){
				return dao.getSceneList(paramMap);
			}else {
				return null;
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	public Map<String, Object> updateScene(String user_idString,
			String device_id, String boardId, String keyId, String sceneName, String status) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		Map<String,Object> map = new HashMap<String,Object>();
		paramMap.put("userId", user_idString);
		paramMap.put("deviceId", device_id);
		try{
			int count = dao.getUserDeviceCount(paramMap);
			if (count == 1){
				paramMap.put("boardId", boardId);
				paramMap.put("keyId", keyId);
				paramMap.put("sceneName", (sceneName == null || sceneName.equals("")) ? null : sceneName);
				paramMap.put("status", null);
				
//				if (status != null && status.equals("1")) {
//					dao.closeAllScene(paramMap);
//				}
				dao.updateScene(paramMap);
				map.put("flag", true);
				map.put("msg", "0001");
			}else {
				map.put("flag", false);
				map.put("msg", "0008");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			map.put("flag", false);
			map.put("msg", "0000");
		}
		return map;
	}
	
	public Map<String, Object> openScene(String user_idString,
			String device_id, String boardId, String keyId) {

		Map<String,Object> paramMap = new HashMap<String,Object>();
		Map<String,Object> map = new HashMap<String,Object>();
		paramMap.put("userId", user_idString);
		paramMap.put("deviceId", device_id);
		try{
			int count = dao.getUserDeviceCount(paramMap);
			if (count == 1){
				paramMap.put("boardId", boardId);
				paramMap.put("keyId", keyId);
				
				Device device = dao.getDeviceById(device_id);
				if (device.getOnline().equals("0")){
					map.put("flag", false);
					map.put("msg", "0014");
					return map;
				}
				paramMap.put("deviceId", device_id);
				dao.closeAllScene(paramMap);
				dao.deleteSceneCommand(paramMap);
				dao.addSceneCommand(paramMap);
				map.put("flag", true);
				map.put("msg", "0001");
			}else {
				map.put("flag", false);
				map.put("msg", "0008");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			map.put("flag", false);
			map.put("msg", "0000");
		}
		return map;
	
	}
	/**
	 * 根据查询条件查询所有定时任务
	 * @param deviceId
	 * @param userId
	 * @return
	 */
	public List<Timer> getAllTimersByDevice(String deviceId, String userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("deviceId", (deviceId == null || deviceId.equals("")) ? null : deviceId);
		map.put("userId", userId);
		return dao.getAllTimersByDevice(map);
	}
	
	/**
	 * 添加定时任务
	 * @param timer
	 * @return
	 */
	public Map<String, Object> addDeviceTimer(Timer timer) {
		Map<String,Object> result = new HashMap<String,Object>();
		int count = timer.getType().equals("0") ? dao.getSingleExistsTimer(timer) : dao.getReExistsTimer(timer);
		if (count <= 0){
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("deviceId", timer.getDevice_id());
			if (timer.getType().equals("0")){
				try {
					param.put("week", getWeek(Constant.sdc.parse(timer.getAction_time())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				param.put("day", timer.getAction_time());
				logger.info(JsonUtils.getJAVABeanJSON(param));
				count = dao.getDeviceSingleTimerCount(param);
				logger.info("111=-" + count);
				if (count >= 7){
					result.put("flag", false);
					result.put("msg", "定时任务已达到最大数量");
					return result;
				}
			}
			if (timer.getType().equals("0")){
				dao.addSingleTimer(timer);
			}else {
				dao.addReTimer(timer);
			}
			dao.deleteTimerCommand(param);
			dao.insertTimerCommand(param);
			
			result.put("flag", true);
			result.put("msg", "添加定时任务成功");
		}else {
			result.put("flag", false);
			result.put("msg", "定时任务有冲突或重复，请检查");
		}
		return result;
	}
	/**
	 * 当前周几
	 * @return
	 */
	private static String getWeek(Date date){
		String[] weekDays = {"7", "1", "2", "3", "4", "5", "6"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
	    int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
//	    if (week < 0){
//	    	week = 0;
//	    }
//	    week ++;
	    return weekDays[week];
	}
	
	public Map<String, Object> updateTimer(String id, int op, String deviceId) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("id", id);
		if (op == 100){
			dao.deleteTimer(param);
		}else if (op == 1){
			dao.startTimer(param);
		}else if (op == 0){
			dao.stopTimer(param);
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("flag", true);
		result.put("msg", "0001");
		param.put("deviceId", deviceId);
		
		dao.deleteTimerCommand(param);
		dao.insertTimerCommand(param);
		return result;
	}
	
	/**
	 * 添加定时任务
	 * @param timer
	 * @param user_idString 
	 * @return
	 */
	public Map<String, Object> addDeviceTimerFromApp(Timer timer, String user_idString) {
		Map<String,Object> result = new HashMap<String,Object>();
		int count = timer.getType().equals("0") ? dao.getSingleExistsTimer(timer) : dao.getReExistsTimer(timer);
		if (count <= 0){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("userId", user_idString);
			paramMap.put("deviceId", timer.getDevice_id());
			count = dao.getUserDeviceCount(paramMap);
			if (count == 1){
				if (timer.getType().equals("0")){
					try {
						paramMap.put("week", getWeek(Constant.sdc.parse(timer.getAction_time())));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					paramMap.put("day", timer.getAction_time());
					count = dao.getDeviceSingleTimerCount(paramMap);
					if (count >= 7){
						result.put("flag", false);
						result.put("msg", "0007");
						return result;
					}
				}
				if (timer.getType().equals("0")){
					dao.addSingleTimer(timer);
				}else {
					dao.addReTimer(timer);
				}
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("deviceId", timer.getDevice_id());
				
				dao.deleteTimerCommand(param);
				dao.insertTimerCommand(param);
				result.put("flag", true);
				result.put("msg", "0001");
			}else {
				result.put("flag", false);
				result.put("msg", "0008");
			}
		}else {
			result.put("flag", false);
			result.put("msg", "0004");
		}
		return result;
	}
	
	public Map<String, Object> updateTimerFromApp(String id, int op, String user_idString) {
		Map<String,Object> param = new HashMap<String,Object>();
		Map<String,Object> result = new HashMap<String,Object>();
		param.put("id", id);
		Timer timer = dao.getTimerById(param);
		if (timer != null){
			String deviceId = timer.getDevice_id();
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("userId", user_idString);
			paramMap.put("deviceId", deviceId);
			int count = dao.getUserDeviceCount(paramMap);
			if (count == 1){
				if (op == 100){
					dao.deleteTimer(param);
				}else if (op == 1){
					dao.startTimer(param);
				}else if (op == 0){
					dao.stopTimer(param);
				}
				result.put("flag", true);
				result.put("msg", "0001");
//				param.put("deviceId", deviceId.split("-")[0]);
				param.put("deviceId", deviceId);
				dao.deleteTimerCommand(param);
				dao.insertTimerCommand(param);
			}else {
				result.put("flag", false);
				result.put("msg", "0008");
			}
		}else {
			result.put("flag", false);
			result.put("msg", "0002");
		}
		return result;
	}
	public List<Timer> getAllSceneTimersByDevice(String deviceId, String userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("deviceId", (deviceId == null || deviceId.equals("")) ? null : deviceId);
		map.put("userId", userId);
		return dao.getAllSceneTimersByDevice(map);
	}
	
	/**
	 * 添加 场景定时任务
	 * @param timer
	 * @return
	 */
	public Map<String, Object> addDeviceSceneTimer(Timer timer, String user_id) {
		Map<String,Object> result = new HashMap<String,Object>();
		int count = timer.getType().equals("0") ? dao.getSingleExistsTimer(timer) : dao.getReExistsTimer(timer);
		if (count <= 0){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("userId", user_id);
			paramMap.put("deviceId", timer.getDevice_id());
			count = dao.getUserDeviceCount(paramMap);
			if (count == 1){
				if (timer.getType().equals("0")){
					try {
						paramMap.put("week", getWeek(Constant.sdc.parse(timer.getAction_time())));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					paramMap.put("day", timer.getAction_time());
					count = dao.getDeviceSingleTimerCount(paramMap);
					if (count >= 7){
						result.put("flag", false);
						result.put("msg", "0007");
						return result;
					}
				}
				if (timer.getType().equals("0")){
					dao.addSingleTimer(timer);
				}else {
					dao.addReTimer(timer);
				}
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("deviceId", timer.getDevice_id());
				
				dao.deleteTimerCommand(param);
				dao.insertTimerCommand(param);
				result.put("flag", true);
				result.put("msg", "0001");
			}else {
				result.put("flag", false);
				result.put("msg", "0008");
			}
		}else {
			result.put("flag", false);
			result.put("msg", "0004");
		}
		return result;
	}
}


