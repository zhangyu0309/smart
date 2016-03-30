package com.smarthome.core.base.action;

import java.util.zip.CRC32;

public class TestCrc {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CRC32 crc32 = new CRC32();
		byte[] data = new byte[128];
		for(int i=0;i<data.length;i++) {
		    data[i] = (byte) i;
		}
		crc32.update(data);
		System.out.println(Long.bitCount(crc32.getValue()));

	}
	/**
	 * int转byte[]
	 * 
	 * @param i
	 * @return
	 */
	private byte[] int2bytes(int i) {
		byte[] b = new byte[4];
		b[3] = (byte) (0xff & i);
		b[2] = (byte) ((0xff00 & i) >> 8);
		b[1] = (byte) ((0xff0000 & i) >> 16);
		b[0] = (byte) ((0xff000000 & i) >> 24);
		return b;
	}
}
