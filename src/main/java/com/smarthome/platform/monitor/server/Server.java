package com.smarthome.platform.monitor.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.smarthome.core.util.ByteUtil;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.platform.monitor.bean.Command;
import com.smarthome.platform.monitor.bean.DeviceBoard;
import com.smarthome.platform.monitor.common.Constant;
import com.smarthome.platform.monitor.dao.HostJDBCDao;

public class Server implements Runnable {
	private Logger logger = Logger.getLogger(Server.class.getName());
	private final Socket _s;
	Timer timer = new Timer();
	byte[] reqDate = null;
	long lastReceiveTime;// 上次收到数据包的时间
	InputStream in = null;
	OutputStream os = null;
	private String device_id="";
	private byte[] tempdata = new byte[]{};
	private Map<String, Long> subMap = new HashMap<String, Long>(); 
	private boolean timerHandled = true;
	private Command appGet = null;
	Calendar timerCal = Calendar.getInstance();
	/**
	 * 接收到的心跳数量
	 */
	private long heartBeats = 0;
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
		if (new String(msg).startsWith("DBG")) {
			return null;
		}
		if (new String(msg).startsWith("get_timer")) {
			sendTimer();
			return null;
		}
		//heart beats
		if (new String(msg).startsWith("heart beats")) {
			//第一次心跳下发定时策略
			if(heartBeats == 0){
				sendTimer();
			}
			heartBeats ++;
			//TIMING:21:28:16,08/17/2017.
			//每60次心跳对时一次
			if (heartBeats % 60 == 0){
				sendMessage(("TIMING:" + Constant.sdFormat.format(new Date()) + ".").getBytes());
			}
			return null;
		}
		if (new String(msg).contains("Hello from wifi board")){
			//Hello from wifi board My ID is: 1988606!
			device_id = new String(msg).split("My ID is: ")[1].replace("!", "").replace("\n", "").trim();
			sendMessage(Constant.START);
			HostJDBCDao.online(1, device_id);
		}else if (new String(msg).contains("Ok Fd:")){
			sendMessage(("TIMING:" + Constant.sdFormat.format(new Date()) + ".").getBytes());
			//定期获取传感器数据
			TimerTask task = new TimerTask() {  
		        @Override  
		        public void run() {
		        	sendMessage(Constant.GET_DATA);
		        	for (Map.Entry<String, Long> entry : subMap.entrySet()) {
		        		if (System.currentTimeMillis() - entry.getValue() > 120000){
		        			HostJDBCDao.online(0, entry.getKey());
		        		}else {
		        			HostJDBCDao.online(1, entry.getKey());
						}
		        	}
		        }  
		    }; 
		  //0点下发定时策略
			TimerTask task2 = new TimerTask() {  
		        @Override  
		        public void run() {
		        	timerCal.setTime(new Date());
		        	if (timerCal.get(Calendar.HOUR_OF_DAY) == 0 && 
		        			timerCal.get(Calendar.MINUTE)  == 0){
		        		sendTimer();
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
		        	if (command != null){
		        		logger.info("get command:" + JsonUtils.getJAVABeanJSON(command));
		        		//开关设备
		        		if(command.getOperation() == 0 || command.getOperation() == 1 || command.getOperation() == 7){
		        			//电机停的动作在数据库中保存为7 底层通信实际为2
		        			if (command.getOperation() == 7){
		        				command.setOperation(2);
		        			}
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
		        			DeviceBoard db = HostJDBCDao.getDeviceBoard(device_id, command.getBoard_id(), command.getKey_id());
		        			//KEYCONF:1,0x00000000,0x0000001f.BOARD:1.
		        			String distributeString = "KEYCONF:"+command.getKey_id() + 
		        					","+db.getValue1()+","+db.getValue2()+".BOARD:"+command.getBoard_id()+".";
		        			sendMessage(distributeString.getBytes());
		        			HostJDBCDao.deleteCommand(command.getCid());
		        		}else if(command.getOperation() == 3){
		        			//3获取
		        			//SERGETKEY:BOARD:1,KEY:5.
		        			if (sendMessage(("SERGETKEY:BOARD:"+command.getBoard_id()+",KEY:"+command.getKey_id()+"." ).getBytes())){
		    		        	HostJDBCDao.deleteCommand(command.getCid());
		    		        }else{
		    		        	logger.info("send get key command fail1");
		    		        }
		        		}else if(command.getOperation() == 4){
		        			//4直接发送
		        			if (command.getContent() != null && !command.getContent().equals("")){
		        				if (sendMessage(command.getContent().getBytes())){
			    		        	HostJDBCDao.deleteCommand(command.getCid());
			    		        }else{
			    		        	logger.info("send content command fail1");
			    		        }
		        			}
		        		}else if(command.getOperation() == 5){
		        			//5应用get
		        			appGet = command;
		        			HostJDBCDao.setKeyUpdated(command, 0);
		        			if (sendMessage(("SERGETKEY:BOARD:"+command.getBoard_id()+",KEY:"+command.getKey_id()+"." ).getBytes())){
		    		        	HostJDBCDao.deleteCommand(command.getCid());
		    		        }else{
		    		        	logger.info("send get key command fail1");
		    		        }
		        		}else if(command.getOperation() == 6){
		        			//6开启场景
		        			byte[] remsg = new byte[7];
			        		remsg[0] = 0x3a;
			        		logger.info("open scene:" + command.getDevice_id() + "," + command.getBoard_id()
			        				 + "," + command.getKey_id());
		    		        remsg[1] = 0x01;
		    		        remsg[2] = (byte) command.getBoard_id();
		    		        remsg[3] = 0x4a;
		    		        remsg[4] = (byte) command.getKey_id();
		    		        remsg[5] = (byte) (remsg[0] ^ remsg[1] ^ remsg[2] ^ remsg[3] ^ remsg[4]);
		    		        remsg[6] = 0x23;
		    		        if (sendMessage(remsg)){
		    		        	HostJDBCDao.deleteCommand(command.getCid());
		    		        }else{
		    		        	logger.info("send command fail");
		    		        }
		    		        //下发定时策略
		        		}else if(command.getOperation() == 8){
		        			sendTimer();
		        			HostJDBCDao.deleteCommand(command.getCid());
		        		}
		        	}
		        }  
		    }; 
		    timer.scheduleAtFixedRate(task, 0, 60000);
		    timer.scheduleAtFixedRate(task1, 0, 1000);
		    timer.scheduleAtFixedRate(task2, 0, 50000);
		}else if (new String(msg).startsWith("local_client_fd is not connect to")){
			sendMessage(Constant.START);
		}else if (new String(msg).startsWith("KEYCONF:DONE")){
			//下发确认
			return null;
		}else if (new String(msg).startsWith("Key Board")) {
			//Key Board 1 .
			//Key:5-0xffffffff, 0xffffffff.
			String currentBoard = "";
			String currentKey = "";
			for (String templine : new String(msg).split("\n")){
				if (templine.startsWith("Key Board")){
					//Key Board 1 .
					//Key Board 1 .Key:2-0x0, 0x1.Scop:0x1-0x40.
					currentBoard = templine.replace("Key Board", "").replace(".", "").trim();
				}
				if (templine.startsWith("Key:")){
					currentKey = templine.split("-")[0].split(":")[1].trim();
					String value1 = templine.split("-")[1].split(",")[0].trim();
					String value2 = templine.split("-")[1].split(",")[1].replace(".", "").trim();
					if (templine.contains("Scop")){
						value2 = templine.split("-")[1].split(",")[1].split("\\.")[0].trim();
					}
					if(value1.equalsIgnoreCase("0xffffffff")){
						value1 = "0x00000000";
					}
					if(value2.equalsIgnoreCase("0xffffffff")){
						value2 = "0x00000000";
					}
					if (value1.length() < 10){
						value1 = formatValue(value1);
					}
					if (value2.length() < 10){
						value2 = formatValue(value2);
					}
					DeviceBoard db = new DeviceBoard(device_id, currentBoard, currentKey, 
							value1, value2
							);
					HostJDBCDao.updateDeviceKey(db);
					logger.info("update local key:" + JsonUtils.getJAVABeanJSON(db));
				}
			}
			if (appGet != null){
				HostJDBCDao.setKeyUpdated(appGet, 1);
				appGet = null;
			}
			return null;
		}
		
//		else if (new String(msg).startsWith("CHANLIST")){
//			//获取配置回复
//			String currentBoard = "";
//			String currentKey = "";
//			for (String templine : new String(msg).split("\n")){
//				logger.info(templine);
//				if (templine.startsWith("Key Board")){
//					//Key Board 1 .
//					currentBoard = templine.replace("Key Board", "").replace(".", "").trim();
//				}
//				if (templine.startsWith("Key:")){
//					//Key:1-0x0, 0x1f.
//					currentKey = templine.split("-")[0].split(":")[1].trim();
//					HostJDBCDao.updateDeviceKey(new DeviceBoard(device_id, currentBoard, currentKey, 
//							templine.split("-")[1].split(",")[0].trim(), 
//							templine.split("-")[1].split(",")[1].trim()));
//				}
//			}
//			return null;
//		}
		else if (msg[0] == 0x3a){
			tempdata = new byte[]{};
			//msg = new byte[]{0x3A,0x00,0x01,0x04,0x3F,0x23,
			//0x3A,0x00,0x01,0x02,0x13,0x21,0x12,(byte) 0xA5,(byte) 0xBC,0x23,0x3A,0x00,0x02,0x02,0x13,0x21,0x12,(byte) 0xA5,(byte) 0xBC,0x23};
			//数据
			if (msg.length == 6 && msg[msg.length - 1] == 0x23){
				//底层确认数据帧，无需处理
				return null;
			}
			if (msg.length == 27 && msg[msg.length - 1] == 0x23 && msg[3] == 0x4b){
				//定时策略底层确认数据帧，无需处理
				timerHandled = true;
				return null;
			}
			if (msg.length % Constant.data_length == 0){
				//n个数据帧
				int sensors = msg.length / Constant.data_length;
				//解决重复数据
				Map<String, String> checkMap = new HashMap<String, String>();
//				SendBuf[0] = 0x3A;                          
//				  SendBuf[1] = HI_UINT16( EndDeviceID );
//				  SendBuf[2] = LO_UINT16( EndDeviceID );
//				  SendBuf[3] = 0x02;                       //FC
//				  SendBuf[4] = dev_on_off_status;//开关设备，这一位表示当前状态  
//				  SendBuf[5] = 0;
//				  SendBuf[6] = 0;//GetGas();  //获取气体传感器的状态  
//				  SendBuf[7] = 0;//HI_UINT16(SoilHumValue);//GetLamp(); //获得灯的状态
//				  SendBuf[8] = 0;//LO_UINT16(SoilHumValue);
//				  SendBuf[9] = 0;
//				  SendBuf[10] = 0;
//				  SendBuf[11] = 0;
//				  SendBuf[12] = 0;
//				  SendBuf[18] = XorCheckSum(SendBuf, 18);
//				  SendBuf[19] = 0x23;
				for (int i=0; i < sensors; i++){
					String sub_id = ByteUtil.toHex(msg[i*Constant.data_length + 1]).toLowerCase() +
							ByteUtil.toHex(msg[i*Constant.data_length + 2]).toLowerCase();
					if (checkMap.containsKey(sub_id)){
						continue;
					}
					checkMap.put(sub_id, "");
					int onoff = ByteUtil.bytes2int(new byte[]{0x00,0x00,0x00,msg[i*Constant.data_length + 4]});
					subMap.put(device_id + "-" + sub_id, System.currentTimeMillis());
					HostJDBCDao.online(1, device_id + "-" + sub_id);
					HostJDBCDao.updateSubDeviceStatus(device_id + "-" + sub_id, onoff);
				}
				return null;
			}
			//零碎数据
			byte[] datanow = new byte[tempdata.length + msg.length];
			System.arraycopy(tempdata, 0, datanow, 0, tempdata.length);
			System.arraycopy(msg, 0, datanow, tempdata.length, msg.length);
			if (datanow.length % Constant.data_length == 0
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
			if (datanow.length % Constant.data_length == 0
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
	 * 下发定时策略
	 */
	private void sendTimer() {
		List<com.smarthome.platform.monitor.bean.Timer> timerList = HostJDBCDao.getTimerList(device_id);
		if (timerList != null && timerList.size() > 0){
			//定时策略中所有的子设备列表
			Set<String> deviceidsList = new HashSet<String>();
			for (com.smarthome.platform.monitor.bean.Timer timer : timerList){
				deviceidsList.add(timer.getDevice_id());
			}
			for(String did : deviceidsList){
				byte[] timerBytes = new byte[27];
				timerBytes[0] = 0x3a;
				String subid = did.split("-")[1];
				timerBytes[1] = Byte.parseByte(subid.substring(0, 2), 16);
				timerBytes[2] = Byte.parseByte(subid.substring(2, 4), 16);
				timerBytes[3] = 0x4b;
				int setCount = 0;
				for(com.smarthome.platform.monitor.bean.Timer timer : timerList){
					if (timer.getDevice_id().equals(did)){
						if (timer.getType().equals("0")){
//							2016-12-03 14:31:38
							Date adtionDate = null;
							try {
								adtionDate = Constant.sdc.parse(timer.getAction_time());
							} catch (ParseException e) {
								e.printStackTrace();
							}
							Calendar cal = Calendar.getInstance();
							cal.setTime(adtionDate);
							timerBytes[3 + setCount * 3 + 1] = Byte.parseByte(Integer.toString(cal.get(Calendar.HOUR_OF_DAY), 16), 16);
							timerBytes[3 + setCount * 3 + 2] = Byte.parseByte(Integer.toString(cal.get(Calendar.MINUTE), 16), 16);
							timerBytes[3 + setCount * 3 + 3] = (byte) Integer.parseInt(timer.getAction());
						}
						else if (timer.getType().equals("1")){
//							 14:31:38
							timerBytes[3 + setCount * 3 + 1] = Byte.parseByte(Integer.toString(Integer.parseInt(timer.getWeek_time().split(":")[0]), 16), 16);
							timerBytes[3 + setCount * 3 + 2] = Byte.parseByte(Integer.toString(Integer.parseInt(timer.getWeek_time().split(":")[1]), 16), 16);
							timerBytes[3 + setCount * 3 + 3] = (byte) Integer.parseInt(timer.getAction());
						}
						setCount ++;
					}
				}
				if (setCount < 7){
					for (; setCount < 7; setCount ++){
						timerBytes[3 + setCount * 3 + 1] = (byte) 0xff;
						timerBytes[3 + setCount * 3 + 2] = (byte) 0xff;
						timerBytes[3 + setCount * 3 + 3] = (byte) 0xff;
					}
				}
		        
		        timerBytes[25] = (byte) (timerBytes[0] ^ timerBytes[1] ^ timerBytes[2] ^ timerBytes[3] ^ timerBytes[4]
		        		^ timerBytes[5] ^ timerBytes[6] ^ timerBytes[7] ^ timerBytes[8] ^ timerBytes[9]
		        		^ timerBytes[10] ^ timerBytes[11] ^ timerBytes[12] ^ timerBytes[13] ^ timerBytes[14]
		        		^ timerBytes[15] ^ timerBytes[16] ^ timerBytes[17] ^ timerBytes[18] ^ timerBytes[19] 
		        		^ timerBytes[20] ^ timerBytes[21] ^ timerBytes[22] ^ timerBytes[23] ^ timerBytes[24]);
		        timerBytes[26] = 0x23;
		       sendMessage(timerBytes);
			}

//			3A 00 01 4B 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 70 23
		}
	}

	private String formatValue(String value) {
		int length = value.length();
		String added = "";
		for(int i = 0; i < 10 - length; i++){
			added = added + "0";
		}
		return "0x" + added + value.replace("0x", "");
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
					logger.info("-->:" + new String(response));//;
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
