package com.smarthome.platform.monitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.smarthome.core.base.action.BaseAction;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.platform.monitor.bean.Device;
import com.smarthome.platform.monitor.bean.GpsInfo;
import com.smarthome.platform.monitor.bean.PositionInfo;
import com.smarthome.platform.monitor.service.HostService;
/**
 * 主机前台接口类
 * @author RM 
 *
 */
public class HostAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	@Resource
	private HostService hostService ;
	private static Logger logger = Logger.getLogger(HostAction.class.getName());
	/**
	 * 设备ID
	 */
	private String deviceid;
	
	/**
	 * 查询位置信息的开始时间
	 */
	private String stime;
	
	/**
	 * 查询位置信息的结束时间
	 */
	private String etime;
	
	private String editType;
	/**
	 * 查询主机信息
	 * @return
	 */
	public String getAll(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Device> resultList = this.hostService.getDevice(this.deviceid);
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
	 * 查询历史未知信息
	 * @return
	 */
	public String getPositionData() {
		if (this.deviceid != null && !this.deviceid.equalsIgnoreCase("")) {
			JSONObject jsonObject = new JSONObject();
			for (String tempid : this.deviceid.split(",")) {
				if (tempid != null && !tempid.equalsIgnoreCase("")) {
					PositionInfo queryPi = new PositionInfo(tempid, this.stime, this.etime);
					System.out.println(JsonUtils.getJAVABeanJSON(queryPi));
					List<GpsInfo> tmpList = hostService.getPositionData(queryPi);
					jsonObject.put(tempid, JsonUtils.getGenericList(tmpList));
				}
			}
			this.jsonString = jsonObject.toString();
			try {
				this.responseWriter(this.jsonString);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}
	/**
	 * 查看轨迹
	 * @return
	 */
	public String beginAddOrUpdate(){
		if (this.editType.equals("0"))
			return "add_method";
		return null;
	}
	/**
	 * 以下方法struts2使用 
	 */

	public HostService getHostService() {
		return hostService;
	}

	public void setHostService(HostService hostService) {
		this.hostService = hostService;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}

}
