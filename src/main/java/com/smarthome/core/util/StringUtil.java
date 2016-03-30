package com.smarthome.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * String处理类
 * 
 * @author sunny
 * 
 */
public class StringUtil {
	
	public static String classPath = "" ;

	/**
	 * 将参数字符串中的特殊字符进行编码 例如 "Hello world!" 转换后为 "Hello%20world%21"
	 * 
	 * @param src
	 *            源字符串
	 * @return 编码后的字符串
	 */
	public static String escape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);

		for (int i = 0; i < src.length(); i++) {
			char j = src.charAt(i);

			if ((Character.isDigit(j)) || (Character.isLowerCase(j))
					|| (Character.isUpperCase(j))) {
				tmp.append(j);
			} else if (j < 'Ā') {
				tmp.append("%");
				if (j < '\020')
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * escape()方法的逆运算
	 * 
	 * @param src
	 *            源字符串
	 * @return 解码后的字符串
	 */
	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0;
		int pos = 0;

		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					char ch = (char) Integer.parseInt(
							src.substring(pos + 2, pos + 6), 16);

					tmp.append(ch);
					lastPos = pos + 6;
					continue;
				}
				char ch = (char) Integer.parseInt(
						src.substring(pos + 1, pos + 3), 16);

				tmp.append(ch);
				lastPos = pos + 3;
				continue;
			}

			if (pos == -1) {
				tmp.append(src.substring(lastPos));
				lastPos = src.length();
				continue;
			}
			tmp.append(src.substring(lastPos, pos));
			lastPos = pos;
		}

		return tmp.toString();
	}

	/**
	 * toTrim循环去掉以trimStr开头的字符 例如 String s = "123123Hello123world123123";
	 * trimPrefix(s,"123") - 输出Hello123world123123
	 * 
	 * @param toTrim
	 * @param trimStr
	 * @return
	 */
	public static String trimPrefix(String toTrim, String trimStr) {
		while (toTrim.startsWith(trimStr)) {
			toTrim = toTrim.substring(trimStr.length());
		}
		return toTrim;
	}

	/**
	 * toTrim循环去掉以trimStr结尾的字符 例如 String s = "123123Hello123world123123";
	 * trimPrefix(s,"123") - 输出123123Hello123world
	 * 
	 * 
	 * @param toTrim
	 * @param trimStr
	 * @return
	 */
	public static String trimSufffix(String toTrim, String trimStr) {
		while (toTrim.endsWith(trimStr)) {
			toTrim = toTrim.substring(0, toTrim.length() - trimStr.length());
		}
		return toTrim;
	}


	/**
	 * 判断输入字符串是否是空串
	 * 
	 * @param str
	 * @return boolean true：输入字符串是空串 false：输入字符串不是空串
	 */
	public static boolean isEmpty(String str) {
		if (str != null&&!str.equals("")&&str.length()!=0&&!str.equals("null")) {
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 判断输入字符串是否是空串
	 * 
	 * @param str
	 * @return boolean true：输入字符串不是空串 false：输入字符串是空串
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String subString(String str, int len) {
		int strLen = str.length();
		if (strLen < len)
			return str;
		char[] chars = str.toCharArray();
		int cnLen = len * 2;
		String tmp = "";
		int iLen = 0;
		for (int i = 0; i < chars.length; i++) {
			int iChar = chars[i];
			if (iChar <= 128)
				iLen += 1;
			else
				iLen += 2;
			if (iLen >= cnLen)
				break;
			tmp = new StringBuilder().append(tmp)
					.append(String.valueOf(chars[i])).toString();
		}
		return tmp;
	}


	/**
	 * 判断输入参数是否可转换为整数
	 * 
	 * @param s
	 * @return boolean true：可以 false：不可以
	 */
	public static boolean isInteger(String s) {
		boolean rtn = validByRegex("^[-+]{0,1}\\d*$", s);
		return rtn;
	}

	/**
	 * 判断输入参数是否可转换为邮件地址
	 * 
	 * @param s
	 * @return boolean true：可以 false：不可以
	 */
	public static boolean isEmail(String s) {
		boolean rtn = validByRegex(
				"(\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)*", s);
		return rtn;
	}

	/**
	 * 判断输入参数是否可转换为手机号码
	 * 
	 * @param s
	 * @return boolean true：可以 false：不可以
	 */
	public static boolean isMobile(String s) {
		boolean rtn = validByRegex(
				"^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", s);
		return rtn;
	}

	/**
	 * 判断输入参数是否可转换为固定号码
	 * 
	 * @param s
	 * @return boolean true：可以 false：不可以
	 */
	public static boolean isPhone(String s) {
		boolean rtn = validByRegex(
				"(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?", s);
		return rtn;
	}

	/**
	 * 判断输入参数是否可转换为邮政编码
	 * 
	 * @param s
	 * @return boolean true：可以 false：不可以
	 */
	public static boolean isZip(String s) {
		boolean rtn = validByRegex("^[0-9]{6}$", s);
		return rtn;
	}

	/**
	 * 判断输入参数是否可转换为qq号
	 * 
	 * @param s
	 * @return boolean true：可以 false：不可以
	 */
	public static boolean isQq(String s) {
		boolean rtn = validByRegex("^[1-9]\\d{4,9}$", s);
		return rtn;
	}

	/**
	 * 判断输入参数是否可转换为IP
	 * 
	 * @param s
	 * @return boolean true：可以 false：不可以
	 */
	public static boolean isIp(String s) {
		boolean rtn = validByRegex(
				"^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
				s);
		return rtn;
	}

	/**
	 * 判断输入参数是否可转换为中文
	 * 
	 * @param s
	 * @return boolean true：可以 false：不可以
	 */
	public static boolean isChinese(String s) {
		boolean rtn = validByRegex("^[一-龥]+$", s);
		return rtn;
	}

	/**
	 * 判断输入参数是否只包含数字和字母
	 * 
	 * @param s
	 * @return boolean true：是 false：否
	 */
	public static boolean isChrNum(String s) {
		boolean rtn = validByRegex("^([a-zA-Z0-9]+)$", s);
		return rtn;
	}

	/**
	 * 判断input是否符合regex规范
	 * 
	 * @param regex
	 *            正则
	 * @param input
	 *            输入字符串
	 * @return boolean
	 */
	public static boolean validByRegex(String regex, String input) {
		Pattern p = Pattern.compile(regex, 2);
		Matcher regexMatcher = p.matcher(input);
		return regexMatcher.find();
	}

	/**
	 * 判断输入字符串中每一个字符都是数字
	 * 
	 * @param str
	 * @return boolean true:是 false:不是
	 */
	public static boolean isNumeric(String str) {
		int i = str.length();
		if (i == 0) {
			return false;
		}
		while (true) {
			i--;
			if (i < 0)
				break;
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 首字母转换为大写
	 * 
	 * @param newStr
	 * @return
	 */
	public static String makeFirstLetterUpperCase(String newStr) {
		if (newStr.length() == 0) {
			return newStr;
		}
		char[] oneChar = new char[1];
		oneChar[0] = newStr.charAt(0);
		String firstChar = new String(oneChar);
		return new StringBuilder().append(firstChar.toUpperCase())
				.append(newStr.substring(1)).toString();
	}

	/**
	 * 首字母转换为小写
	 * 
	 * @param newStr
	 * @return
	 */
	public static String makeFirstLetterLowerCase(String newStr) {
		if (newStr.length() == 0) {
			return newStr;
		}
		char[] oneChar = new char[1];
		oneChar[0] = newStr.charAt(0);
		String firstChar = new String(oneChar);
		return new StringBuilder().append(firstChar.toLowerCase())
				.append(newStr.substring(1)).toString();
	}

	/**
	 * 格式化对象数组信息
	 * 
	 * @param message
	 * @param args
	 * @return
	 */
	public static String formatParamMsg(String message, Object[] args) {
		for (int i = 0; i < args.length; i++) {
			message = message.replace(new StringBuilder().append("{").append(i)
					.append("}").toString(), args[i].toString());
		}
		return message;
	}

	/**
	 * 格式化Map对象信息
	 * 
	 * @param message
	 * @param args
	 * @return
	 */
	public static String formatParamMsg(String message, Map params) {
		if (params == null)
			return message;
		Iterator keyIts = params.keySet().iterator();
		while (keyIts.hasNext()) {
			String key = (String) keyIts.next();
			Object val = params.get(key);
			if (val != null) {
				message = message.replace(new StringBuilder().append("${")
						.append(key).append("}").toString(), val.toString());
			}
		}
		return message;
	}

	public static StringBuilder formatMsg(CharSequence msgWithFormat,
			boolean autoQuote, Object[] args) {
		int argsLen = args.length;
		boolean markFound = false;

		StringBuilder sb = new StringBuilder(msgWithFormat);

		if (argsLen > 0) {
			for (int i = 0; i < argsLen; i++) {
				String flag = new StringBuilder().append("%").append(i + 1)
						.toString();
				int idx = sb.indexOf(flag);

				while (idx >= 0) {
					markFound = true;
					sb.replace(idx, idx + 2, toString(args[i], autoQuote));
					idx = sb.indexOf(flag);
				}
			}

			if ((args[(argsLen - 1)] instanceof Throwable)) {
				StringWriter sw = new StringWriter();
				((Throwable) args[(argsLen - 1)])
						.printStackTrace(new PrintWriter(sw));
				sb.append("\n").append(sw.toString());
			} else if ((argsLen == 1) && (!markFound)) {
				sb.append(args[(argsLen - 1)].toString());
			}
		}
		return sb;
	}

	public static StringBuilder formatMsg(String msgWithFormat, Object[] args) {
		return formatMsg(new StringBuilder(msgWithFormat), true, args);
	}

	public static String toString(Object obj, boolean autoQuote) {
		StringBuilder sb = new StringBuilder();
		if (obj == null) {
			sb.append("NULL");
		} else if ((obj instanceof Object[])) {
			for (int i = 0; i < ((Object[]) (Object[]) obj).length; i++) {
				sb.append(((Object[]) (Object[]) obj)[i]).append(", ");
			}
			if (sb.length() > 0)
				sb.delete(sb.length() - 2, sb.length());
		} else {
			sb.append(obj.toString());
		}

		if ((autoQuote)
				&& (sb.length() > 0)
				&& ((sb.charAt(0) != '[') || (sb.charAt(sb.length() - 1) != ']'))
				&& ((sb.charAt(0) != '{') || (sb.charAt(sb.length() - 1) != '}'))) {
			sb.insert(0, "[").append("]");
		}
		return sb.toString();
	}

	public static String returnSpace(String str) {
		String space = "";
		if (!str.isEmpty()) {
			String[] path = str.split("\\.");
			for (int i = 0; i < path.length - 1; i++) {
				space = new StringBuilder().append(space)
						.append("&nbsp;&emsp;").toString();
			}
		}
		return space;
	}

	public static String getArrayAsString(List<String> arr) {
		if ((arr == null) || (arr.size() == 0))
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.size(); i++) {
			if (i > 0)
				sb.append(",");
			sb.append((String) arr.get(i));
		}
		return sb.toString();
	}

	public static String getArrayAsString(String[] arr) {
		if ((arr == null) || (arr.length == 0))
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0)
				sb.append(",");
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	public static String getSetAsString(Set set) {
		if ((set == null) || (set.size() == 0))
			return "";
		StringBuffer sb = new StringBuffer();
		int i = 0;
		Iterator it = set.iterator();
		while (it.hasNext()) {
			if (i++ > 0)
				sb.append(",");
			sb.append(it.next().toString());
		}
		return sb.toString();
	}

	public static String htmlEntityToString(String dataStr) {
		dataStr = dataStr.replace("&apos;", "'").replace("&quot;", "\"")
				.replace("&gt;", ">").replace("&lt;", "<")
				.replace("&amp;", "&");

		int start = 0;
		int end = 0;
		StringBuffer buffer = new StringBuffer();

		while (start > -1) {
			int system = 10;
			if (start == 0) {
				int t = dataStr.indexOf("&#");
				if (start != t) {
					start = t;
				}
				if (start > 0) {
					buffer.append(dataStr.substring(0, start));
				}
			}
			end = dataStr.indexOf(";", start + 2);
			String charStr = "";
			if (end != -1) {
				charStr = dataStr.substring(start + 2, end);

				char s = charStr.charAt(0);
				if ((s == 'x') || (s == 'X')) {
					system = 16;
					charStr = charStr.substring(1);
				}
			}
			try {
				char letter = (char) Integer.parseInt(charStr, system);
				buffer.append(new Character(letter).toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			start = dataStr.indexOf("&#", end);
			if (start - end > 1) {
				buffer.append(dataStr.substring(end + 1, start));
			}

			if (start == -1) {
				int length = dataStr.length();
				if (end + 1 != length) {
					buffer.append(dataStr.substring(end + 1, length));
				}
			}
		}
		return buffer.toString();
	}

	public static String stringToHtmlEntity(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			switch (c) {
			case '\n':
				sb.append(c);
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '\'':
				sb.append("&apos;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			default:
				if ((c < ' ') || (c > '~')) {
					sb.append("&#x");
					sb.append(Integer.toString(c, 16));
					sb.append(';');
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 处理database使用的特殊字符
	 */
	public static String convertSpecialCharacterforDB(String str) {
		if (null != str && !"".equals(str)) {
			str = str.replaceAll("'", "''");
			str = str.replaceAll("\\\\", "\\\\\\\\");
			str = str.replaceAll("%", "\\\\%");
			str = str.replaceAll("_", "\\\\_");
		}
		return str;
	}

	public static void main(String[] args) {
		// String str =
		// "${scriptImpl.getStartUserPos(startUser)==\"&#37096;&#38376;&#32463;&#29702;\"}";
		// System.out.println(htmlEntityToString(str));

		String s = "123\\.456\\.789";
		System.out.println(returnSpace(s));

	}

	/***
	 * MD5加码 生成32位md5码
	 */
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */
	public static String convertMD5(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}
	
	

}
