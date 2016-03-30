package com.smarthome.core.util;

import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UniqueIdUtil {

	private static long uid = 0L;

	private static Lock lock = new ReentrantLock();

	/**
	 * 根据系统当前时间毫秒数返回id值
	 * 
	 * @return
	 * @throws Exception
	 */
	public static long genId() throws Exception {
		lock.lock();
		try {
			long id = 0L;
			do
				id = System.currentTimeMillis();
			while (id == uid);
			uid = id;
			long l1 = id;
			return l1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return -1L;
	}

	/**
	 * 得到一个UUID值
	 * @return
	 */
	public static final String getGuid() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	/**
	 * 得到一个32位UUID值
	 * @return
	 */
	public static final String getGuid32() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}
	 
	/**
	 * 得到number个uuid组成的数组
	 * @param number
	 * @return
	 */
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < number; i++) {
			ss[i] = getGuid();
		}
		return ss;
	}

	
	public static void main(String[] args) throws InterruptedException {
	}

}
