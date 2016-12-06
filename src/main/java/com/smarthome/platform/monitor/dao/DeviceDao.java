package com.smarthome.platform.monitor.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.smarthome.platform.monitor.bean.Device;
import com.smarthome.platform.monitor.bean.DeviceBoard;
import com.smarthome.platform.monitor.bean.SensorData;
import com.smarthome.platform.monitor.bean.Timer;

/**
 * 
 * @author zy
 * 
 */
public interface DeviceDao {
	
	public static Logger logger = Logger.getLogger(DeviceDao.class);

	public List<Device> getDevice(Device device);

	public List<Device> getAllParentDevice(String user_id);

	public Device getDeviceById(String device_id);

	public int addDevice(Device device);

	public int updateDevice(Device device);

	public int delDeviceById(Device device);

	public List<SensorData> getSensorData(Map<String, Object> map);

	public SensorData getLatestData(String device_id);

	public int onoffDvice(Map<String, Object> map);

	public void deleteCommand(String device_id);

	public List<Device> getDeviceList(String string);

	/**
	 * 添加主设备七键配置参数
	 * @param device
	 */
	public void addBoardKey(Device device);

	public void delDeviceBoardInfoById(Device device);

	public List<DeviceBoard> getAllKeysByDevice(Map<String, Object> map);

	public DeviceBoard getDeviceBoard(Map<String, Object> paramMap);

	public void updateKey(DeviceBoard db);

	public void deleteDisOrGetCommand(Map<String, Object> map);

	public int getUpdatedCount(Map<String, Object> paramMap);

	public DeviceBoard getDeviceBoardById(Map<String, Object> paramMap);

	public void editScene(Map<String, Object> paramMap);

	public int getUserDeviceCount(Map<String, Object> paramMap);

	public void renameUserDeviceCount(Map<String, Object> paramMap);

	public void addSubDevice(Map<String, Object> paramMap);

	public List<DeviceBoard> getSceneList(Map<String, Object> paramMap);

	public void updateScene(Map<String, Object> paramMap);

	public void closeAllScene(Map<String, Object> paramMap);

	public void deleteSceneCommand(Map<String, Object> paramMap);

	public void insertSceneCommand(Map<String, Object> paramMap);

	public void addSceneCommand(Map<String, Object> paramMap);

	public List<Timer> getAllTimersByDevice(Map<String, Object> map);

	public int getSingleExistsTimer(Timer timer);

	public int getReExistsTimer(Timer timer);

	public void addSingleTimer(Timer timer);

	public void addReTimer(Timer timer);

	public void deleteTimer(Map<String, Object> param);

	public void startTimer(Map<String, Object> param);

	public void stopTimer(Map<String, Object> param);

	public void insertTimerCommand(Map<String, Object> param);

	public Timer getTimerById(Map<String, Object> param);

}
