package com.smarthome.platform.monitor.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.smarthome.platform.monitor.bean.Device;
import com.smarthome.platform.monitor.bean.GpsInfo;
import com.smarthome.platform.monitor.bean.PositionInfo;
import com.smarthome.platform.monitor.dao.HostDao;

/**
 * 主机分组业务逻辑层的处理
 * @author zy
 *
 */
@Service
public class HostService {

	@Resource
	private HostDao dao;

	/**
	 * 查询设备信息
	 * @param deviceid 设备ID，如果为空则查询所有 的设备
	 * @return
	 */
	public List<Device> getDevice(String deviceid) {
		return dao.getDevice(deviceid);
	}

	/**
	 * 查询历史位置信息
	 * @param queryPi 包含设备ID，开始时间，结束时间   设备ID不允许为空
	 * @return
	 */
	public List<GpsInfo> getPositionData(PositionInfo queryPi) {
		return dao.getPositionData(queryPi);
	}
	
}
