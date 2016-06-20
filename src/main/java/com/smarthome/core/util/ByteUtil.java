package com.smarthome.core.util;

/**
 * 日期处理通用类
 * 
 * @author RM
 * @version 1.0
 */
public class ByteUtil {

	/**
	 * int转byte[]
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] int2bytes(int i) {
		byte[] b = new byte[4];
		b[3] = (byte) (0xff & i);
		b[2] = (byte) ((0xff00 & i) >> 8);
		b[1] = (byte) ((0xff0000 & i) >> 16);
		b[0] = (byte) ((0xff000000 & i) >> 24);
		return b;
	}

	/**
	 * byte[] 转int
	 * 
	 * @param src
	 * @return
	 */
	public static int bytes2int(byte[] src) {
		if (src.length == 2) {
			byte[] temp = new byte[4];
			temp[0] = 0x00;
			temp[1] = 0x00;
			temp[2] = src[0];
			temp[3] = src[1];
			src = temp;
		}
		int value;
		value = (int) (((src[0] & 0xFF) << 24) | ((src[1] & 0xFF) << 16)
				| ((src[2] & 0xFF) << 8) | (src[3] & 0xFF));
		return value;
	}

	/**
	 * 根据指定长度返回byte[]
	 * 
	 * @param op
	 *            OP域，第一个byte
	 * @param str
	 *            需要转换的字符串
	 * @param len
	 *            需要byte[]的长度
	 * @return
	 */
	public static byte[] getBytes(byte op, String str, int len) {
		byte[] result = new byte[len];
		result[0] = op;
		byte[] strBytes = str.getBytes();
		if (strBytes.length >= len - 1) {
			for (int i = 1; i < len; i++) {
				result[i] = strBytes[i - 1];
			}
		} else {
			System.arraycopy(strBytes, 0, result, 1, strBytes.length);
			for (int i = strBytes.length + 1; i < len; i++) {
				result[i] = (byte) ' ';
			}
		}
		return result;
	}

	/**
	 * byte转十六进制
	 * 
	 * @param b
	 * @return
	 */
	public static String toHex(byte b) {
		String hex = "00" + Integer.toHexString((int) b).toUpperCase();
		hex = hex.substring(hex.length() - 2);
		return hex;
	}

	/**
	 * long转换 byte[]
	 * 
	 * @param l
	 * @return
	 */
	public static byte[] long2Byte(long x) {
		byte[] bb = new byte[8];
		bb[0] = (byte) (x >> 56);
		bb[1] = (byte) (x >> 48);
		bb[2] = (byte) (x >> 40);
		bb[3] = (byte) (x >> 32);
		bb[4] = (byte) (x >> 24);
		bb[5] = (byte) (x >> 16);
		bb[6] = (byte) (x >> 8);
		bb[7] = (byte) (x >> 0);
		return bb;
	}

	/**
	 * byte[] 转换long
	 * 
	 * @param b
	 * @return
	 */

	public static long byte2Long(byte[] bb) {
		return ((((long) bb[0] & 0xff) << 56) | (((long) bb[1] & 0xff) << 48)
				| (((long) bb[2] & 0xff) << 40) | (((long) bb[3] & 0xff) << 32)
				| (((long) bb[4] & 0xff) << 24) | (((long) bb[5] & 0xff) << 16)
				| (((long) bb[6] & 0xff) << 8) | (((long) bb[7] & 0xff) << 0));
	}

	public static void main(String[] args) {
		byte[] b = new byte[] { (byte) 0xe2, (byte) 0xe3, (byte) 0xe4,
				(byte) 0xea, (byte) 0xca, (byte) 0xda, (byte) 0xea, (byte) 0xfa };
		System.out.println(byte2Long(b));
	}

	/**
	 * 字节数组转为十六进制打印
	 * @param bytes
	 * @return
	 */
	public static String ListBytes(byte[] bytes) {
		if (bytes == null || bytes.length == 0){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(ByteUtil.toHex(b)).append("|");
		}
		return sb.toString();
	}
}
