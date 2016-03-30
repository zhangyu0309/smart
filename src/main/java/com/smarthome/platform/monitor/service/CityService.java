package com.smarthome.platform.monitor.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.smarthome.platform.monitor.bean.Area;
import com.smarthome.platform.monitor.dao.CityDao;

/**
 * 主机分组业务逻辑层的处理
 * @author zy
 *
 */
@Service
public class CityService {

	@Resource
	private CityDao dao;

	/**
	 * 查询省份信息
	 * @return
	 */
	public List<Area> getPro(){
		return dao.getPro();
	}
	/**
	 * 
	 * @param pro
	 * @return
	 */
	public List<Area> getCity(String pro){
		return dao.getCity(pro);
	}
	/**
	 * 
	 * @param city
	 * @return
	 */
	public List<Area> getCountry(String city){
		return dao.getCountry(city);
	}
	
}
