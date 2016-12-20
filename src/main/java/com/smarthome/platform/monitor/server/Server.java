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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.smarthome.core.util.ByteUtil;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.core.util.StringUtil;
import com.smarthome.platform.monitor.bean.Command;
import com.smarthome.platform.monitor.bean.DeviceBoard;
import com.smarthome.platform.monitor.common.Constant;
import com.smarthome.platform.monitor.dao.HostJDBCDao;

public class Server implements Runnable {
	private Logger logger = Logger.getLogger("server");
	private final Socket _s;
	Timer timer = new Timer();
	byte[] reqDate = null;
	long lastReceiveTime;// 上次收到数据包的时间
	InputStream in = null;
	OutputStream os = null;
	private String device_id="";
	private StringBuilder tempdata = new StringBuilder("");
	private Map<String, Long> subMap = new HashMap<String, Long>(); 
	private Command appGet = null;
	Calendar timerCal = Calendar.getInstance();
	boolean socketLive = true;
	
	/**
	 * 发送锁
	 */
	boolean sendLocked = false;
	
//	boolean sending = false;
	/**
	 * 连续超时的次数
	 */
	int timeoutCount = 0;
	/**
	 * 待处理命令队列
	 */
	private BlockingQueue<String> queue = null;
	
	/**
	 * 接收到的心跳数量
	 */
	private long heartBeats = 0;
	public Server(Socket s) {
		_s = s;
		queue = new ArrayBlockingQueue<String>(500);
		try {
			_s.setSoTimeout(180000);
		} catch (SocketException e) {
			logger.error(e.getMessage(), e);
		}
		lastReceiveTime = System.currentTimeMillis();
	}
	
	public void run() {
		try {
			in = _s.getInputStream();
			os = _s.getOutputStream();
			logger.info("get connect from " + _s.getRemoteSocketAddress());
			sendMessage("TCP_CONNECTED".getBytes());
			
			//队列处理
			new Thread(new Runnable() {
				public void run() {
					while (socketLive) {
						logger.debug("get command from queue");
						String message = null;
						try {
							message = queue.take();
							logger.debug("get command:" + message);
							if (message != null && !message.equals("") && !message.equals("\n")){
								MessageHandle(message);
							}
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
					}
				}
			}).start();
			
			while (true) {
				if (Constant.SOCKET_WAIT_TIME > 0 && 
						(System.currentTimeMillis() - lastReceiveTime > Constant.SOCKET_WAIT_TIME)) {
					logger.info("time out");
					break;
				}
				if (timeoutCount > 10){
					logger.info("timeoutCount upto 10, force disconnect!");
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
					String receive = new String(bytes);
					logger.info("<--:" + receive);
//					for (byte b : bytes) {
//						System.out.print(ByteUtil.toHex(b));
//						System.out.print("|");
//					}
					tempdata.append(receive);
//					logger.debug("now temp data:" + tempdata.toString());
//					logger.info("n index:" + tempdata.indexOf("\n"));
					while (tempdata.indexOf("\n") >= 0){
						String firstLine = tempdata.substring(0, tempdata.indexOf("\n"));
						logger.info("add command to queue:" + firstLine);
						queue.put(firstLine);
						tempdata.delete(0, firstLine.length() + 1);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			socketLive = false;
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
	private byte[] MessageHandle(String message) {
		logger.info("HANDLE MESSAGE:" + message);
		if (message == null || message.equals("") || message.equals("\n")){
			return null;
		}
		if (message.startsWith("DBG")) {
			return null;
		}
		String[] commands = message.split("\n");
		if (commands.length > 1){
			for (int i = 0; i < commands.length; i++) {
				MessageHandle(commands[i]);
			}
			return null;
		}
		if ((device_id == null || device_id.equals("")) && !message.contains("Hello from wifi board")){
			logger.info("no deviceid, ignore command:" + message);
			return null;
		}
		if (message.contains("Set Time Done")){
			sendLocked = false;
			return null;
		}
		if (message.trim().startsWith("RequestSchemaPolicy:")) {
			//RequestSchemaPolicy:DEVID: 0x1,ShortAddr: 0xbf6b.
			String subidString = StringUtil.replaceBlank(message).split("DEVID:")[1].split(",")[0].replace("0x", "");
//			while (device_id == null || device_id.equals("")) {
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
			if (device_id != null && !device_id.equals("")) {
				sendTimer(device_id + "-" + StringUtil.addZeroForString(subidString, 4));
			}else {
				logger.info("no deviceid, ignore timer reauest");
			}
			return null;
		}
		if (message.startsWith("KEYCONF:")){
			sendLocked = false;
			return null;
		}
		if (message.startsWith("SCENERY: Key From Server")){
			sendLocked = false;
			return null;
		}
		if (message.startsWith("SENDCMD")){
			sendLocked = false;
			return null;
		}
		
		if (message.startsWith("heart beats")) {
			//第一次心跳下发定时策略
			if(heartBeats == 0){
//				sendTimer(null);
			}
			heartBeats ++;
			//TIMING:21:28:16,08/17/2017.
			//每60次心跳对时一次
			if (heartBeats % 60 == 0){
				sendMessage(("TIMING:" + Constant.sdFormat.format(new Date()) + ".").getBytes());
			}
			HostJDBCDao.online(1, device_id);
			sendLocked = false;
			return null;
		}
		if (message.contains("Hello from wifi board")){
			//Hello from wifi board My ID is: 1988606!
			device_id = message.split("My ID is: ")[1].replace("!", "").replace("\n", "").trim();
			sendMessage(Constant.START);
			HostJDBCDao.online(1, device_id);
		}else if (message.contains("Ok Fd:")){
			sendMessage(("TIMING:" + Constant.sdFormat.format(new Date()) + ".").getBytes());
			//定期获取传感器数据
			TimerTask task = new TimerTask() {  
		        @Override  
		        public void run() {
		        	sendMessage(Constant.GET_DATA);
		        	logger.info("refresh device status start");
		        	for (Map.Entry<String, Long> entry : subMap.entrySet()) {
		        		if (System.currentTimeMillis() - entry.getValue() > 120000){
		        			HostJDBCDao.online(0, entry.getKey());
		        		}else {
		        			HostJDBCDao.online(1, entry.getKey());
						}
		        	}
		        	logger.info("refresh device status end");
		        }  
		    }; 
		  //0点下发定时策略
			TimerTask task2 = new TimerTask() {  
		        @Override  
		        public void run() {
		        	timerCal.setTime(new Date());
		        	if (timerCal.get(Calendar.HOUR_OF_DAY) == 0 && 
		        			timerCal.get(Calendar.MINUTE)  == 0){
		        		try {
		        			sendTimer(null);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
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
			    		        remsg[1] = (byte)Integer.parseInt(subid.substring(0, 2), 16);//Byte.parseByte("0x" + subid.substring(0, 2));
			    		        remsg[2] = (byte)Integer.parseInt(subid.substring(2, 4), 16);//Byte.parseByte("0x" + subid.substring(2, 2));
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
		        			try {
		        				sendTimer(command.getDevice_id());
			        			HostJDBCDao.deleteCommand(command.getCid());
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
		        		}else if(command.getOperation() == 9){
		        			//dimmer
		        			if (command.getContent() == null || command.getContent().equals("")){
		        				command.setContent("0");
		        			}
		        			if (Integer.parseInt(command.getContent()) < 0){
		        				command.setContent("0");
		        			}
		        			if (Integer.parseInt(command.getContent()) > 100){
		        				command.setContent("100");
		        			}
		        			byte[] remsg = new byte[7];
			        		remsg[0] = 0x3a;
			        		logger.info("dimmer device:" + command.getDevice_id() + "-" + Integer.parseInt(command.getContent()));
			        		String subid = command.getDevice_id().split("-")[1];
			    		    remsg[1] = (byte)Integer.parseInt(subid.substring(0, 2), 16);//Byte.parseByte("0x" + subid.substring(0, 2));
			    		    remsg[2] = (byte)Integer.parseInt(subid.substring(2, 4), 16);//Byte.parseByte("0x" + subid.substring(2, 2));
			    		    remsg[3] = 0x0a;
			    		    remsg[4] = (byte) Integer.parseInt(command.getContent());
			    		    remsg[5] = (byte) (remsg[0] ^ remsg[1] ^ remsg[2] ^ remsg[3] ^ remsg[4]);
			    		    remsg[6] = 0x23;
			    		    if (sendMessage(remsg)){
			    		    	HostJDBCDao.deleteCommand(command.getCid());
			    		        }else{
			    		        	logger.info("send command fail");
			    		        }
		        		}
		        	}
		        }  
		    }; 
		    timer.schedule(task, 0, 60000);
		    timer.schedule(task1, 0, 1000);
		    timer.schedule(task2, 0, 50000);
		}else if (message.startsWith("local_client_fd is not connect to")){
			sendMessage(Constant.START);
		}else if (message.startsWith("Key Board")) {
			sendLocked = false;
			//Key Board 1 .
			//Key:5-0xffffffff, 0xffffffff.
			String currentBoard = "";
			String currentKey = "";
			for (String templine : message.split("\n")){
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
		else if (StringUtil.replaceBlank(message).startsWith("CURRVALUE:")){
			sendLocked = false;
			message = StringUtil.replaceBlank(message);
//			CURRVALUE:3A00010200000000000000000000000000003923.
			//数据
			if (message.endsWith(".")){
				logger.info("full data");
				//解决重复数据
				Map<String, String> checkMap = new HashMap<String, String>();
				String[] datas = message.replace("CURRVALUE:", "").split("\\.");
				for (int i = 0; i < datas.length; i++){
					String tempData = datas[i];
					if (tempData == null || tempData.equals("")){
						continue;
					}
					logger.debug("data : " + tempData);
					//子id
					String subid = tempData.substring(2, 6);
					if (checkMap.containsKey(subid)){
						continue;
					}
					logger.debug("data for : " + device_id + "-" + subid);
					checkMap.put(subid, "");
					int onoff = Integer.parseInt(tempData.substring(9, 10));
					subMap.put(device_id + "-" + subid, System.currentTimeMillis());
					HostJDBCDao.online(1, device_id + "-" + subid);
					HostJDBCDao.updateSubDeviceStatus(device_id + "-" + subid, onoff);
				}
			}
//			else if (message.contains(".")){
//				logger.info("full data, with half");
//				//结尾有零碎数据
//				MessageHandle(message.substring(0, message.lastIndexOf(".") + 1));
//				tempdata = new StringBuilder(message.substring(message.lastIndexOf(".") + 1));
//			}else {
//				logger.info("other data");
//				tempdata.append(StringUtil.replaceBlank(message));
//			}
		}else{
			logger.info("unknown command ,abandon : " + message);
//			message = StringUtil.replaceBlank(message);
//			logger.info("other data 1");
//			//零碎数据
//			tempdata.append(message);
//			logger.info("tempdata:" + tempdata);
//			if (tempdata.toString().startsWith("CURRVALUE:") && 
//					tempdata.toString().contains(".")){
//				//零碎数据已经组合完整，开始处理
//				logger.info("combine done!");
//				if (tempdata.toString().endsWith(".")){
//					logger.info("-->full");
//					MessageHandle(tempdata.toString());
//					tempdata = new StringBuilder("");
//				}else {
//					if (tempdata.toString().contains(".")){
//						logger.info("-->full with half");
//						MessageHandle(tempdata.toString().substring(0, tempdata.toString().lastIndexOf(".") + 1));
//						tempdata = new StringBuilder(tempdata.toString().substring(tempdata.toString().lastIndexOf(".") + 1));
//					}else {
//						logger.info("-->half");
//						tempdata.append(message);
//					}
//				}
//			}
		}
		return null;
	}
	/**
	 * 下发定时策略
	 * targetid 需要定时的完整设备id
	 */
	private void sendTimer(String targetid) {
		logger.info("send timer for:" + targetid);
		List<com.smarthome.platform.monitor.bean.Timer> timerList ;
		if (targetid == null || targetid.equals("")) {
			timerList = HostJDBCDao.getTimerList(device_id);
		}else if (targetid.endsWith("0000") || !targetid.contains("-")) {
			targetid = device_id + "-0000";
			timerList = HostJDBCDao.getSceneTimerList(targetid);
		}else{
			timerList = HostJDBCDao.getTimerList(targetid);
		}
		if (timerList != null && timerList.size() > 0){
			//定时策略中所有的子设备列表
			Set<String> deviceidsList = new HashSet<String>();
			for (com.smarthome.platform.monitor.bean.Timer timer : timerList){
				if (!timer.getDevice_id().contains("-")){
					timer.setDevice_id(timer.getDevice_id() + "-0000");
				}
				deviceidsList.add(timer.getDevice_id());
			}
			for(String did : deviceidsList){
				String subid = did.split("-")[1];
				if (targetid != null && !targetid.equals("") 
						&& !targetid.equals(did)){
					continue;
				}
				byte[] timerBytes = new byte[27];
				timerBytes[0] = 0x3a;
				timerBytes[1] = (byte)Integer.parseInt(subid.substring(0, 2), 16);
				timerBytes[2] = (byte)Integer.parseInt(subid.substring(2, 4), 16);
				timerBytes[3] = 0x4b;
				int setCount = 0;
				for(com.smarthome.platform.monitor.bean.Timer timer : timerList){
					if (timer.getDevice_id().equals(did)){
						logger.info("timer type:" + timer.getType());
						if (timer.getType().equals("0")){
//							2016-12-03 14:31:38
							Date adtionDate = null;
							try {
								adtionDate = Constant.sdc.parse(timer.getAction_time());
							} catch (ParseException e) {
								logger.error(e.getMessage(), e);
							}
							Calendar cal = Calendar.getInstance();
							cal.setTime(adtionDate);
							timerBytes[3 + setCount * 3 + 1] = Byte.parseByte(Integer.toString(cal.get(Calendar.HOUR_OF_DAY), 16), 16);
							timerBytes[3 + setCount * 3 + 2] = Byte.parseByte(Integer.toString(cal.get(Calendar.MINUTE), 16), 16);
							if (targetid.endsWith("0000") || !targetid.contains("-")) {
								timerBytes[3 + setCount * 3 + 3] = mergeMiniInt(timer.getBoard_id(), timer.getKey_id());
							}else {
								timerBytes[3 + setCount * 3 + 3] = (byte) Integer.parseInt(timer.getAction());
							}
						}
						else if (timer.getType().equals("1")){
//							 14:31:38
							timerBytes[3 + setCount * 3 + 1] = Byte.parseByte(Integer.toString(Integer.parseInt(timer.getWeek_time().split(":")[0]), 16), 16);
							timerBytes[3 + setCount * 3 + 2] = Byte.parseByte(Integer.toString(Integer.parseInt(timer.getWeek_time().split(":")[1]), 16), 16);
							if (targetid.endsWith("0000") || !targetid.contains("-")) {
								timerBytes[3 + setCount * 3 + 3] = mergeMiniInt(timer.getBoard_id(), timer.getKey_id());
							}else {
								timerBytes[3 + setCount * 3 + 3] = (byte) Integer.parseInt(timer.getAction());
							}
						}
						setCount ++;
					}
				}
				logger.info("timers count:" + setCount);
				if (setCount < 7){
					for (; setCount < 7; setCount ++){
						logger.info("0xff---:" + setCount);
						timerBytes[3 + setCount * 3 + 1] = (byte) 0xff;
						timerBytes[3 + setCount * 3 + 2] = (byte) 0xff;
						timerBytes[3 + setCount * 3 + 3] = (byte) 0xff;
					}
				}
				logger.info("byte[25]---:" + setCount);
		        timerBytes[25] = (byte) (timerBytes[0] ^ timerBytes[1] ^ timerBytes[2] ^ timerBytes[3] ^ timerBytes[4]
		        		^ timerBytes[5] ^ timerBytes[6] ^ timerBytes[7] ^ timerBytes[8] ^ timerBytes[9]
		        		^ timerBytes[10] ^ timerBytes[11] ^ timerBytes[12] ^ timerBytes[13] ^ timerBytes[14]
		        		^ timerBytes[15] ^ timerBytes[16] ^ timerBytes[17] ^ timerBytes[18] ^ timerBytes[19] 
		        		^ timerBytes[20] ^ timerBytes[21] ^ timerBytes[22] ^ timerBytes[23] ^ timerBytes[24]);
		        timerBytes[26] = 0x23;
		       sendMessage(timerBytes);
			}

//			3A 00 01 4B 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 70 23
		}else {
			logger.info("empty timer");
			if (targetid != null && !targetid.equals("") ){
				String subid = targetid.split("-")[1];
				byte[] timerBytes = new byte[27];
				timerBytes[0] = 0x3a;
				timerBytes[1] = (byte)Integer.parseInt(subid.substring(0, 2), 16);
				timerBytes[2] = (byte)Integer.parseInt(subid.substring(2, 4), 16);
				timerBytes[3] = 0x4b;
				timerBytes[4] = (byte) 0xfe;
				for (int i=5; i <= 24; i++){
					timerBytes[i] = (byte) 0xff;
				}
				timerBytes[25] = (byte) (timerBytes[0] ^ timerBytes[1] ^ timerBytes[2] ^ timerBytes[3] ^ timerBytes[4]
		        		^ timerBytes[5] ^ timerBytes[6] ^ timerBytes[7] ^ timerBytes[8] ^ timerBytes[9]
		        		^ timerBytes[10] ^ timerBytes[11] ^ timerBytes[12] ^ timerBytes[13] ^ timerBytes[14]
		        		^ timerBytes[15] ^ timerBytes[16] ^ timerBytes[17] ^ timerBytes[18] ^ timerBytes[19] 
		        		^ timerBytes[20] ^ timerBytes[21] ^ timerBytes[22] ^ timerBytes[23] ^ timerBytes[24]);
		        timerBytes[26] = 0x23;
		        sendMessage(timerBytes);
			}
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
		long startTimestamp = System.currentTimeMillis();
		//正常发送 还是等待锁超时发送
		boolean timeout = false;
		while (sendLocked){
			if (System.currentTimeMillis() - startTimestamp > 3000){
				timeoutCount ++;
				timeout = true;
				logger.info("wait response timeout,timeoutCount:" + timeoutCount);
				break;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		sendLocked = true;
		//本次未超时 连续超时次数归零
		if (timeout){
			//
		}else {
			logger.info("timeoutCount reset to 0");
			timeoutCount = 0;
		}
		boolean send = false;
		logger.info("response:" + ByteUtil.ListBytes(response));//new String(response));
		logger.info("-->:" + new String(response));//;
		if (os != null && response != null) {
			int trytime = 0;
			do {// 为防止发送失败，最多重试6次
				try {
					trytime++;
					os.write(response);
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
			} while (trytime < 1);
		}else {}
		logger.info("send result:" + send);
//		sendLocked = false;
		return send;
	}
	/**
	 * 两个小int组合成一个byte
	 * @param i
	 * @param j
	 * @return
	 */
	private byte mergeMiniInt(int i, int j){
		String h = Integer.toBinaryString(i);
		String l = Integer.toBinaryString(j);
		System.out.println(h + "--" + l);
		int hneed = 4 - h.length();
		for (int m = 0; m < hneed; m++){
			h = "0" + h;
		}
		int lneed = 4 - l.length();
		for (int m = 0; m < lneed; m++){
			l = "0" + l;
		}	
		System.out.println(h + "--" + l);
		byte b = ByteUtil.bitToByte(h + l);
		return b;
	}
}
