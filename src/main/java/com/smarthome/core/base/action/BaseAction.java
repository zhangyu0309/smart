package com.smarthome.core.base.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	public String jsonString;

	protected final transient Log logger = LogFactory.getLog(BaseAction.class);
	

	/**
	 * 获取本次会话 Request 对象
	 * 
	 * @return 本次会话 Request对象
	 */
	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 获取本次会话 Response 对象
	 * 
	 * @return 本次会话 Response 对象
	 */
	protected HttpServletResponse getResponse() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
	    response.setContentType("application/json");
		return response;
	}

	/**
	 * 获取本次会话 Session 对象
	 * 
	 * @return 本次会话 Session 对象
	 */
	protected HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * Response 写数据
	 * 
	 * @param jsonString
	 * @throws IOException
	 */
	public void responseWriter(String jsonString) throws Exception {
		PrintWriter out = getResponse().getWriter();
		try {
			out.write(jsonString);
		} catch (Exception e) {
			StringWriter trace = new StringWriter();
			e.printStackTrace(new PrintWriter(trace));
			logger.error(trace);
		} finally {
			out.flush();
			out.close();
		}

	}
	/**
	 *  检查Session对象是否存在---未验证
	 *  
	 * @param key
	 * @return
	 */
	protected boolean isExistSession(String key) {
		if (ActionContext.getContext().getSession().get(key) != null) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 *  保存Session对象---未验证
	 * @param key
	 * @param obj
	 */
	protected void setSession(String key, Object obj) {
		ActionContext.getContext().getSession().put(key, obj);
	}

	/**
	 *  取得Session对象---未验证
	 * @param key
	 * @return
	 */
	protected Object getSession(String key) {
		return ActionContext.getContext().getSession().get(key);
	}
	
	/**
	 *  取得查询的URL    
	 * @return
	 */
    protected String getRequestPath() {   
        return (String)ServletActionContext.getRequest().getServletPath();   
    }  


}
