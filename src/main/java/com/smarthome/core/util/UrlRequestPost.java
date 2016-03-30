package com.smarthome.core.util;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONObject;

/**
 * @data: 2015-7-8上午10:03:09
 * @author: CR
 * @description: httpclient发送post请求
 */
public class UrlRequestPost {
	
	/**
	 * 模拟发送http post请求
	 * @param url:要请求的url路径
	 * @param params:要发送的参数(json结构的字符串)
	 * @return
	 */
	public static JSONObject urlPost(String url,String params) {
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		RequestEntity requestEntity = null;
		try {
			//设置超时时间
			client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
			//设置请求体内容(包括参数和请求头)
			requestEntity = new StringRequestEntity(params,"application/json","UTF-8");
			postMethod.setRequestEntity(requestEntity);
			client.executeMethod(postMethod);
			if(postMethod.getStatusLine().getStatusCode() == 200) {
				String result = postMethod.getResponseBodyAsString();
				JSONObject jsonObject = new JSONObject(result);
				return jsonObject;
			}else {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
