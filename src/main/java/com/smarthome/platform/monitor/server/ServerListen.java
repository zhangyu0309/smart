package com.smarthome.platform.monitor.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.smarthome.platform.monitor.common.Constant;

public class ServerListen extends Thread {
	private ServerSocket serverSocket;
	private boolean flag = Constant.IS_RUN_SOCKET;

	public ServerListen(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void run() {
		while (flag) {
			Socket s = null;
			try {
				s = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			Server cs = new Server(s);
			new Thread(cs).start();
		}
	}
}