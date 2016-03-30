package com.smarthome.core.util;

import java.util.UUID;
/**
 * <p>Title:uuID生成器</p>
 * <p>Description:UUID 标示符生成策略</p>
 * @author 杨伟俊
 * @Date:2012-03-19 20:57(PM)
 *@version 1.0
 */
public class UUIDGenerator {
	
	/**
	 * 得到一个字符的UUID。
	 * 标准UUID：b8154343-9b22-4a05-a7c1-01ac9e6eaf3e
	 * 
	 * @return
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();

//		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
//				+ s.substring(19, 23) + s.substring(24);
		
		return s;
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
			ss[i] = getUUID();
		}
		return ss;
	}

	public static void main(String[] args) {
		String[] vars = UUID.randomUUID().toString().split("-");
//		for (int i = 0; i < vars.length; i++) {
//			System.out.println("ok:" + vars[i]);
//			long var = Long.valueOf(vars[i], 16).longValue();
//			System.out.println("ok:===" + var);
//		}

	}
}