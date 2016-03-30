package com.smarthome.platform.monitor.dao;

import java.util.List;

import org.apache.log4j.Logger;

import com.smarthome.platform.monitor.bean.Device;
import com.smarthome.platform.monitor.bean.GpsInfo;
import com.smarthome.platform.monitor.bean.PositionInfo;

/**
 * 
 * @author zy
 * 
 */
public interface HostDao {
	
	public static Logger logger = Logger.getLogger(HostDao.class);

	public List<Device> getDevice(String deviceid);

	public List<GpsInfo> getPositionData(PositionInfo queryPi);

}
