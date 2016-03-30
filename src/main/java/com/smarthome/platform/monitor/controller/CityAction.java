package com.smarthome.platform.monitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.smarthome.core.base.action.BaseAction;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.platform.monitor.bean.Area;
import com.smarthome.platform.monitor.service.CityService;
/**
 * 区域信息前台接口类
 *
 */
public class CityAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	@Resource
	private CityService cityService ;
	private static Logger logger = Logger.getLogger(CityAction.class.getName());
	/**
	 * 所选省份
	 */
	private String pro;
	
	/**
	 * 所选成熟
	 */
	private String city;
	
	/**
	 * 查询省份信息
	 * @return
	 */
	public String getAllPro(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Area> resultList = this.cityService.getPro();
		if(resultList!=null){
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
	 * 查询区县信息
	 * @return
	 */
	public String getAllCountry(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Area> resultList = null;
		if (this.city != null && !this.city.equals("")){
			resultList = this.cityService.getCountry(this.city);	
		}
		if(resultList!=null){
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
	 * 查询城市信息
	 * @return
	 */
	public String getAll(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Area> resultList = null;
		if (this.pro != null && !this.pro.equals("")){
			resultList = this.cityService.getCity(this.pro);	
		}
		if(resultList!=null){
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
	 * 以下方法struts2使用 
	 */

	public void setPro(String pro) {
		this.pro = pro;
	}

	public CityService getCityService() {
		return cityService;
	}

	public void setCityService(CityService cityService) {
		this.cityService = cityService;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
