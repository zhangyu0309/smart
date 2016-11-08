package com.smarthome.platform.monitor.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.smarthome.core.util.ByteUtil;
import com.smarthome.platform.monitor.bean.Command;
import com.smarthome.platform.monitor.bean.DeviceBoard;
import com.smarthome.platform.monitor.bean.DeviceBoardData;
import com.smarthome.platform.monitor.bean.SensorData;
import com.smarthome.platform.monitor.common.Constant;
import com.smarthome.platform.monitor.dao.HostJDBCDao;

public class Server implements Runnable {
	private static Logger logger = Logger.getLogger(Server.class.getName());
	private final Socket _s;
	Timer timer = new Timer();
	byte[] reqDate = null;
	long lastReceiveTime;// 上次收到数据包的时间
	InputStream in = null;
	OutputStream os = null;
	private static String device_id="";
	private static byte[] tempdata = new byte[]{};
	private static Map<String, Long> subMap = new HashMap<String, Long>(); 
	public Server(Socket s) {
		_s = s;
		try {
			_s.setSoTimeout(180000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		lastReceiveTime = System.currentTimeMillis();
	}
	
	public void run() {
		try {
			in = _s.getInputStream();
			os = _s.getOutputStream();
			logger.info("get connect from " + _s.getRemoteSocketAddress());
			sendMessage("TCP_CONNECTED".getBytes());
			while (true) {
				if (Constant.SOCKET_WAIT_TIME > 0 && 
						(System.currentTimeMillis() - lastReceiveTime > Constant.SOCKET_WAIT_TIME)) {
					logger.info("time out");
					break;
				}
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				if (in.available() > 0) {
					lastReceiveTime = System.currentTimeMillis();
					// 处理客户端发来的数据
					byte[] bytes = new byte[in.available()];
					in.read(bytes);
					logger.info("receice:" + ByteUtil.ListBytes(bytes));
//					for (byte b : bytes) {
//						System.out.print(ByteUtil.toHex(b));
//						System.out.print("|");
//					}
					MessageHandle(bytes);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			try {
				if (!_s.isClosed()) {
					_s.close();
				}
				if (timer != null){
					timer.cancel();
				}
				HostJDBCDao.online(0, device_id);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 对socket收到的报文进行处理
	 * 
	 * @param msg
	 *            收到报文
	 * @return 响应报文
	 */
	private byte[] MessageHandle(byte[] msg) {
		logger.info("HANDLE MESSAGE:" + new String(msg));
		if (new String(msg).contains("Hello from wifi board")){
			//Hello from wifi board My ID is: 10005600!
			device_id = new String(msg).split("My ID is: ")[1].replace("!", "").replace("\n", "").trim();
			sendMessage(Constant.START);
			HostJDBCDao.online(1, device_id);
		}else if (new String(msg).startsWith("Ok Fd:")){
			//定期获取传感器数据
			TimerTask task = new TimerTask() {  
		        @Override  
		        public void run() {
		        	sendMessage(Constant.GET_DATA);
		        	for (Map.Entry<String, Long> entry : subMap.entrySet()) {
		        		if (System.currentTimeMillis() - entry.getValue() > 120000){
		        			HostJDBCDao.online(0, entry.getKey());
		        		}
		        	}  
		        }  
		    };  
		  //定期查询开关设备信息 并发送到主控板
			TimerTask task1 = new TimerTask() {  
		        @Override  
		        public void run() {
		        	//sendMessage(Constant.GET_DATA);
//		        	logger.info("get command");
		        	Command command = HostJDBCDao.getCommand(device_id); 
//		        	logger.info(JsonUtils.getJAVABeanJSON(command));
		        	if (command != null){
		        		//开关设备
		        		if(command.getOperation() == 0 || command.getOperation() == 1){
		        			byte[] remsg = new byte[7];
			        		remsg[0] = 0x3a;
			        		if (command.getDevice_id().contains("-") && !command.getDevice_id().endsWith("-")){
			        			logger.info("sub device:" + command.getDevice_id());
			        			//子设备关闭
			        			String subid = command.getDevice_id().split("-")[1];
			    		        remsg[1] = Byte.parseByte(subid.substring(0, 2), 16);//Byte.parseByte("0x" + subid.substring(0, 2));
			    		        remsg[2] = Byte.parseByte(subid.substring(2, 4), 16);//Byte.parseByte("0x" + subid.substring(2, 2));
			    		        remsg[3] = 0x0a;
			    		        remsg[4] = (byte) command.getOperation();
			    		        remsg[5] = (byte) (remsg[0] ^ remsg[1] ^ remsg[2] ^ remsg[3] ^ remsg[4]);
			    		        remsg[6] = 0x23;
			    		        if (sendMessage(remsg)){
			    		        	HostJDBCDao.deleteCommand(command.getCid());
			    		        }else{
			    		        	logger.info("send command fail");
			    		        }
			        		}else{
			        			//主控板关闭
			        			logger.info("main device : " + device_id);
			        			if (sendMessage(Constant.CLOSE)){
			    		        	HostJDBCDao.deleteCommand(command.getCid());
			    		        }else{
			    		        	logger.info("send command fail1");
			    		        }
			        		}
		        		}else if(command.getOperation() == 2){
		        			//2分发
		        			
		        		}else if(command.getOperation() == 3){
		        			//3获取
		        			if (sendMessage(Constant.GETNV)){
		    		        	HostJDBCDao.deleteCommand(command.getCid());
		    		        }else{
		    		        	logger.info("send get key command fail1");
		    		        }
		        		}
		        	}
		        }  
		    }; 
		    timer.scheduleAtFixedRate(task, 0, 60000);
		    timer.scheduleAtFixedRate(task1, 0, 1000);
		}else if (new String(msg).startsWith("local_client_fd is not connect to")){
			sendMessage(Constant.START);
		}else if (new String(msg).startsWith("KEYCONF:DONE")){
			//下发确认
			return null;
		}else if (new String(msg).startsWith("CHANLIST")){
			//获取配置回复
			String currentBoard = "";
			String currentKey = "";
			for (String templine : new String(msg).split("\n")){
				logger.info(templine);
				if (templine.startsWith("Key Board")){
					//Key Board 1 .
					currentBoard = templine.replace("Key Board", "").replace(".", "").trim();
				}
				if (templine.startsWith("Key:")){
					//Key:1-0x0, 0x1f.
					currentKey = templine.split("-")[0].split(":")[1].trim();
					HostJDBCDao.updateDeviceKey(new DeviceBoard(device_id, currentBoard, currentKey, 
							templine.split("-")[1].split(",")[0].trim(), 
							templine.split("-")[1].split(",")[1].trim()));
				}
			}
			return null;
		}else if (msg[0] == 0x3a){
			tempdata = new byte[]{};
			//msg = new byte[]{0x3A,0x00,0x01,0x04,0x3F,0x23,
			//0x3A,0x00,0x01,0x02,0x13,0x21,0x12,(byte) 0xA5,(byte) 0xBC,0x23,0x3A,0x00,0x02,0x02,0x13,0x21,0x12,(byte) 0xA5,(byte) 0xBC,0x23};
			//数据
			if (msg.length == 6 && msg[msg.length - 1] == 0x23){
				//底层确认数据帧，无需处理
				return null;
			}
			if (msg.length % Constant.data_length == 6){
				//6字节起始位 + n个数据帧
				int sensors = (msg.length - 6) / Constant.data_length;
				//传感器个数
				Map<String, String> checkMap = new HashMap<String, String>();
				//
				for (int i=0; i < sensors; i++){
					String sub_id = ByteUtil.toHex(msg[5 + i*Constant.data_length + 2]).toLowerCase() +
							ByteUtil.toHex(msg[5 + i*Constant.data_length + 3]).toLowerCase();
					if (checkMap.containsKey(sub_id)){
						continue;
					}
					checkMap.put(sub_id, "");
					String temp1 = Integer.toString(ByteUtil.bytes2int(new byte[]{0x00,0x00,0x00,msg[5 + i*Constant.data_length + 5]}));
					String wet1 = Integer.toString(ByteUtil.bytes2int(new byte[]{0x00,0x00,0x00,msg[5 + i*Constant.data_length + 6]}));
					String temp2 = Integer.toString(ByteUtil.bytes2int(new byte[]{0x00,0x00,0x00,msg[5 + i*Constant.data_length + 7]}));
					String wet2 = Integer.toString(ByteUtil.bytes2int(new byte[]{0x00,0x00,msg[5 + i*Constant.data_length + 8],
							msg[5 + i*Constant.data_length + 9]}));
					String light = Integer.toString(ByteUtil.bytes2int(new byte[]{msg[5 + i*Constant.data_length + 10],
							msg[5 + i*Constant.data_length + 11],
							msg[5 + i*Constant.data_length + 12],
							msg[5 + i*Constant.data_length + 13]}));
					subMap.put(device_id + "-" + sub_id, System.currentTimeMillis());
					HostJDBCDao.saveData(new SensorData(device_id + "-" + sub_id, temp1, wet1, temp2, wet2, light));
				}
				return null;
			}
			if (msg.length % Constant.data_length == 0){
				//n个数据帧
				int sensors = msg.length / Constant.data_length;
				//传感器个数
				Map<String, String> checkMap = new HashMap<String, String>();
				//
				for (int i=0; i < sensors; i++){
					String sub_id = ByteUtil.toHex(msg[i*Constant.data_length + 1]).toLowerCase() +
							ByteUtil.toHex(msg[i*Constant.data_length + 2]).toLowerCase();
					if (checkMap.containsKey(sub_id)){
						continue;
					}
					checkMap.put(sub_id, "");
					String temp1 = Integer.toString(ByteUtil.bytes2int(new byte[]{0x00,0x00,0x00,msg[i*Constant.data_length + 4]}));
					String wet1 = Integer.toString(ByteUtil.bytes2int(new byte[]{0x00,0x00,0x00,msg[i*Constant.data_length + 5]}));
					String temp2 = Integer.toString(ByteUtil.bytes2int(new byte[]{0x00,0x00,0x00,msg[i*Constant.data_length + 6]}));
					String wet2 = Integer.toString(ByteUtil.bytes2int(new byte[]{0x00,0x00,msg[i*Constant.data_length + 7],msg[i*Constant.data_length + 8]}));
					String light = Integer.toString(ByteUtil.bytes2int(new byte[]{msg[i*Constant.data_length + 9],
							msg[i*Constant.data_length + 10],
							msg[i*Constant.data_length + 11],
							msg[i*Constant.data_length + 12]}));
					subMap.put(device_id + "-" + sub_id, System.currentTimeMillis());
					HostJDBCDao.saveData(new SensorData(device_id + "-" + sub_id, temp1, wet1, temp2, wet2, light));
				}
				return null;
			}
			//零碎数据
			byte[] datanow = new byte[tempdata.length + msg.length];
			System.arraycopy(tempdata, 0, datanow, 0, tempdata.length);
			System.arraycopy(msg, 0, datanow, tempdata.length, msg.length);
			if ((datanow.length % Constant.data_length == 6 || datanow.length % Constant.data_length == 0)
					 && datanow[datanow.length - 1] == 0x23){
				//零碎数据已经组合完整，开始处理
				MessageHandle(datanow);
			}else{
				//未组合完，继续等待
				tempdata = datanow;
			}
		}else{
			//零碎数据
			byte[] datanow = new byte[tempdata.length + msg.length];
			System.arraycopy(tempdata, 0, datanow, 0, tempdata.length);
			System.arraycopy(msg, 0, datanow, tempdata.length, msg.length);
			if ((datanow.length % Constant.data_length == 6 || datanow.length % Constant.data_length == 0)
					 && datanow[datanow.length - 1] == 0x23){
				//零碎数据已经组合完整，开始处理
				MessageHandle(datanow);
			}else{
				//未组合完，继续等待
				tempdata = datanow;
			}
		}
		return null;
	}
	
	/**
	 * 回复信息
	 * @param response
	 */
	private boolean sendMessage(byte[] response){
		boolean send = false;
		if (os != null && response != null) {
			int trytime = 0;
			do {// 为防止发送失败，最多重试6次
				try {
					trytime++;
					os.write(response);
					logger.info("response:" + ByteUtil.ListBytes(response));//new String(response));
					send = true;
					break;
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					e.printStackTrace();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			} while (trytime < 6);
		}
		return send;
	}

}
