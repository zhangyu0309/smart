package com.smarthome.core.util;

import com.smarthome.platform.authority.bean.Sms;

/**
 * 日期处理通用类
 * 
 * @author RM
 * @version 1.0
 */
public class SmsUtil {

	public static Sms send(String phone, String userId) {
		Sms sms = new Sms();
		sms.setPhone(phone);
		sms.setUserId(userId);
		
		String validCode = "123456";
		String authkey = UniqueIdUtil.getGuid32();
		
		sms.setValidcode(validCode);
		sms.setAuthkey(authkey);
		return sms;
	}

}
