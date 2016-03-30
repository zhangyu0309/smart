package com.smarthome.platform.monitor.dao;

import java.util.List;

import org.apache.log4j.Logger;

import com.smarthome.platform.monitor.bean.Area;

/**
 * 
 * @author zy
 * 
 */
public interface CityDao {
	
	public static Logger logger = Logger.getLogger(CityDao.class);

	public List<Area> getPro();

	public List<Area> getCity(String pro);
	
	public List<Area> getCountry(String city);

}
