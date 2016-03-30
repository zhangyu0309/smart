package com.smarthome.platform.monitor.server;

import java.io.IOException;
import java.net.ServerSocket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.smarthome.platform.monitor.common.Constant;

/**
 * 启动socket监听端口
 * 
 * @author zy
 * 
 */
public class ServerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private ServerListen lt = null;
	private ServerSocket serverSockt = null;
	
	private static final Log logger = LogFactory.getLog(ServerServlet.class);

	public ServerServlet() {
	}

	public void init() {
		logger.info("socket init--" + Constant.IS_RUN_SOCKET);
		if (Constant.IS_RUN_SOCKET) {
			try {
				serverSockt = new ServerSocket(Constant.LISTEN_PORT);
				lt = new ServerListen(serverSockt);
				lt.start();
			} catch (Exception e) {
				logger.info(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void doGet(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) throws ServletException,
			IOException {}

	public void doPost(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) throws ServletException,
			IOException {}
}
