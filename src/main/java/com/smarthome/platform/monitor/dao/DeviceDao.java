package com.smarthome.platform.monitor.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.smarthome.platform.monitor.bean.Device;
import com.smarthome.platform.monitor.bean.SensorData;

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

}
