package com.smarthome.platform.monitor.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.smarthome.core.base.action.BaseAction;
import com.smarthome.core.common.AuthorityCommon;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.core.util.WeatherTool;
import com.smarthome.platform.authority.bean.Admin;
import com.smarthome.platform.monitor.bean.DayWeather;
import com.smarthome.platform.monitor.bean.Weather;
import com.smarthome.platform.monitor.common.Constant;
/**
 * 获取weather
 * 信息
 *
 */
public class WeatherAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(WeatherAction.class.getName());
	
	/**
	 * 
	 * @return
	 */
	public String get(){
		Admin admin = (Admin) this.getSession().getAttribute(AuthorityCommon.ADMIN_SESSION);
		String citycode = admin.getCitycode();
		if (citycode.startsWith("01") || citycode.startsWith("02") || citycode.startsWith("03") || citycode.startsWith("04")){
			citycode = citycode.substring(0,2) + citycode.substring(4,6) + "00";
		}
		citycode = "101" + citycode;
		String weatherString = WeatherTool.getweather(citycode);
		if (weatherString == null || weatherString.equals("")){
			this.jsonString = "";
			try {
				this.responseWriter(this.jsonString);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage() , e);
			}
			return null;
		}
		JSONObject weatherObject = new JSONObject(weatherString);
		/*{"c":{"c1":"101010200","c2":"haidian","c3":"海淀","c4":"beijing",
		 * "c5":"北京","c6":"beijing","c7":"北京","c8":"china","c9":"中国","c10":"3",
		 * "c11":"010","c12":"100080","c13":116.170000,"c14":39.590000,"c15":"47","c16":"AZ9010","c17":"+8"},
		 * "f":{
		 * "f1":[{"fa":"00","fb":"01","fc":"7","fd":"-6","fe":"0","ff":"0","fg":"0","fh":"0","fi":"07:21|17:36"},
		 * {"fa":"01","fb":"01","fc":"5","fd":"-7","fe":"0","ff":"0","fg":"0","fh":"0","fi":"07:20|17:37"},
		 * {"fa":"00","fb":"00","fc":"4","fd":"-7","fe":"8","ff":"0","fg":"1","fh":"0","fi":"07:19|17:38"}],
		 * 
		 * "f0":"201602030800"}}
		 * 
		 * http://www.baidu.com/aladdin/img/new_weath/icon/3.png
		 * */
		Weather weather = new Weather();
		
		JSONObject cityObject = weatherObject.getJSONObject("c");
		if (cityObject.getString("c1").startsWith("10101") || cityObject.getString("c1").startsWith("10102") || cityObject.getString("c1").startsWith("10103") || cityObject.getString("c1").startsWith("10104")){
			weather.setCity( cityObject.getString("c5") + cityObject.getString("c3"));
		}else{
			weather.setCity(cityObject.getString("c7") + cityObject.getString("c5") + cityObject.getString("c3"));	
		}
		
		JSONObject daysObject = weatherObject.getJSONObject("f");
		weather.setPtime(daysObject.getString("f0"));
		weather.setPtime(weather.getPtime().substring(0, 4) + "年" + weather.getPtime().substring(4, 6) + "月" + weather.getPtime().substring(6, 8) + "日");
		JSONArray daysArray = daysObject.getJSONArray("f1");
		for (int i = 0; i < daysArray.length(); i ++){
			DayWeather dw = new DayWeather();
			JSONObject dayObject = daysArray.getJSONObject(i);
			dw.setWeathercode1(dayObject.getString("fa"));
			dw.setWeathercode2(dayObject.getString("fb"));
			if (dw.getWeathercode1() != null && !dw.getWeathercode1().equals("")){
				dw.setWeather1(Constant.weatherMap.get(dw.getWeathercode1()));
			}else{
				dw.setWeather1("");
			}
			if (dw.getWeathercode2() != null && !dw.getWeathercode2().equals("")){
				dw.setWeather2(Constant.weatherMap.get(dw.getWeathercode2()));
			}else{
				dw.setWeather2("");
			}
			dw.setTemp1(dayObject.getString("fc"));
			dw.setTemp2(dayObject.getString("fd"));
			dw.setWinddirection1(Constant.windDirectionMap.get(dayObject.getString("fe")));
			dw.setWinddirection2(Constant.windDirectionMap.get(dayObject.getString("ff")));
			dw.setWindstrength1(Constant.windStrengthMap.get(dayObject.getString("fg")));
			dw.setWindstrength2(Constant.windStrengthMap.get(dayObject.getString("fh")));
			
			if (dw.getWinddirection1() == null)
				dw.setWinddirection1("");
			if (dw.getWindstrength1() == null)
				dw.setWindstrength1("");
			String sunString = dayObject.getString("fi");
			dw.setSunrise(sunString.split("\\|")[0]);
			dw.setSunset(sunString.split("\\|")[1]);
			
			if (dw.getWeathercode1() == null || dw.getWeathercode1().equals("")){
				String icons = Constant.iconMap.get(dw.getWeathercode2());
				if (icons.contains(",")){
					dw.setIcon(icons.split(",")[1]);
				}else{
					dw.setIcon(icons);
				}
			}else{
				String icons = Constant.iconMap.get(dw.getWeathercode1());
				if (icons.contains(",")){
					dw.setIcon(icons.split(",")[0]);
				}else{
					dw.setIcon(icons);
				}
			}
			
			switch (i) {
			case 0:
				weather.setDay1(dw);
				break;
			case 1:
				weather.setDay2(dw);
				break;
			case 2:
				weather.setDay3(dw);
				break;
			default:
				break;
			}
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(weather);
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
}
