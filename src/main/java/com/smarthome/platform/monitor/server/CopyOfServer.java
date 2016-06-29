package com.smarthome.platform.monitor.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.smarthome.core.util.ByteUtil;
import com.smarthome.platform.monitor.common.Constant;

public class CopyOfServer implements Runnable {
	private static Logger logger = Logger.getLogger(CopyOfServer.class.getName());
	private static String id = "";
	private final Socket _s;

	long lastReceiveTime;// 上次收到数据包的时间

	public CopyOfServer(Socket s) {
		_s = s;
		lastReceiveTime = System.currentTimeMillis();
	}

	public void run() {
		try {
			InputStream in = _s.getInputStream();
			OutputStream os = _s.getOutputStream();
			while (true) {
				if (System.currentTimeMillis() - lastReceiveTime > Constant.SOCKET_WAIT_TIME) {
					logger.info("超时断开:" + (id == null ? "" : id));
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (in.available() > 0) {
					lastReceiveTime = System.currentTimeMillis();
					// 处理客户端发来的数据
					byte[] bytes = new byte[in.available()];
					in.read(bytes);
					logger.info("receice:" + new String(bytes).trim());
					logger.info("receice length:" + bytes.length);
					StringBuilder sb = new StringBuilder();
					for (byte b : bytes) {
						sb.append(ByteUtil.toHex(b));
						sb.append("|");
					}
					logger.info(sb.toString());
					byte[] response = MessageHandle(bytes);
					if (response != null) {
						String reString = bytesToHexString(response);
						int trytime = 0;
						do {// 为防止发送失败，最多重试6次
							try {
								trytime++;
								os.write(reString.getBytes());
								logger.info("response:" + reString);
								break;
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
								e.printStackTrace();
								Thread.sleep(1000);
							}
						} while (trytime < 6);
					}
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
		//logger.info("HANDLE MESSAGE:" + new String(msg));
		return null;
	}

	/**
	 * 把16进制字符串 转换成字节数组 只转前88字节
	 * 
	 * @param bArray
	 * @return
	 */
	private static byte[] HexToByte(String hexString)// 字串符转换成16进制byte[]
	{
		int len = (hexString.length() - 88);// 前88字节转换
		byte[] result = new byte[len];
		char[] achar = hexString.substring(0, 176).toCharArray();
		System.out.println(achar.length);
		for (int i = 0; i < 88; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		if (len > 88) {
			System.arraycopy(hexString.substring(88 * 2).getBytes(), 0, result,
					88, len - 88);
		}

		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 把字节数组转换成16进制字符串
	 * 
	 * @param bArray
	 * @return
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}

		return sb.toString();
	}
}
