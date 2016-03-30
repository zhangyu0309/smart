package com.smarthome.core.util;


import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * @data: 2015-7-8上午10:03:09
 * @author: CR
 * @description: httpclient发送post请求
 */
public class UrlRequest {
	private static Logger logger = Logger.getLogger(UrlRequest.class.getName());
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
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * httpClient的get请求方式
	 * 返回字符串
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url) {
		logger.info("请求：" + url);
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 50000);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("请求出错: " + getMethod.getStatusLine());
			}
			byte[] responseBody = getMethod.getResponseBody();
			String response = new String(responseBody, "UTF-8");
			logger.info("回应：" + response);
			return response;
		} catch (HttpException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return null;
	}
	
	/**
	 * httpClient的get请求方式
	 * 返回json
	 * @return
	 * @throws Exception
	 */
	public static JSONObject doGetJson(String url) {
		logger.info("请求：" + url);
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("请求出错: " + getMethod.getStatusLine());
			}
			byte[] responseBody = getMethod.getResponseBody();
			String response = new String(responseBody, "UTF-8");
			logger.info("回应：" + response);
			JSONObject jsonObject = new JSONObject(response);
			return jsonObject;
		} catch (HttpException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return null;
	}
}
