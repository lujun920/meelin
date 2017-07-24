package com.meelin.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 字符串的帮助类，提供静态方法，不可以实例化
 * @类功能说明：
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @作者：lujun
 * @创建时间：2015年11月19日 下午1:51:47
 * @版本：V1.0
 */
public class StrUtils {
	/**
	 * 禁止实例化
	 */
	private StrUtils() {
	}

	/**
	 * 
	 * @方法功能说明：分割并且去除空格
	 * @参数：@param str		待分割字符串
	 * @参数：@param sep		分割符
	 * @参数：@param sep2	第二个分隔符
	 * @参数：@return		如果str为空，则返回null。
	 * @修改者：
	 * @修改日期：
	 * @修改说明：
	 * @作者：lujun
	 * @创建时间：2015年11月19日 下午1:52:05
	 * @返回值：String[]
	 * @throws
	 */
	public static String[] splitAndTrim(String str, String sep, String sep2) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		if (!StringUtils.isBlank(sep2)) {
			str = StringUtils.replace(str, sep2, sep);
		}
		String[] arr = StringUtils.split(str, sep);
		// trim
		for (int i = 0, len = arr.length; i < len; i++) {
			arr[i] = arr[i].trim();
		}
		return arr;
	}

	/**
	 * 
	 * @方法功能说明：文本转html
	 * @参数：@param txt		文本
	 * @参数：@return
	 * @修改者：
	 * @修改日期：
	 * @修改说明：
	 * @作者：lujun
	 * @创建时间：2015年11月19日 下午1:52:59
	 * @返回值：String
	 * @throws
	 */
	public static String txt2htm(String txt) {
		if (StringUtils.isBlank(txt)) {
			return txt;
		}
		StringBuilder sb = new StringBuilder((int) (txt.length() * 1.2));
		char c;
		boolean doub = false;
		for (int i = 0; i < txt.length(); i++) {
			c = txt.charAt(i);
			if (c == ' ') {
				if (doub) {
					sb.append(' ');
					doub = false;
				} else {
					sb.append("&nbsp;");
					doub = true;
				}
			} else {
				doub = false;
				switch (c) {
				case '&':
					sb.append("&amp;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\n':
					sb.append("<br/>");
					break;
				default:
					sb.append(c);
					break;
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @方法功能说明：剪切文本。如果进行了剪切，则在文本后加上"...",(用作中文处理,字母和数字以2个作为一个字符)
	 * @参数：@param s		剪切对象
	 * @参数：@param len		编码小于256的作为一个字符，大于256的作为两个字符
	 * @参数：@param append	文本后追加内容
	 * @参数：@return
	 * @修改者：
	 * @修改日期：
	 * @修改说明：
	 * @作者：lujun
	 * @创建时间：2015年11月19日 下午1:53:28
	 * @返回值：String
	 * @throws
	 */
	public static String textCut(String s, int len, String append) {
		if (s == null) {
			return null;
		}
		int slen = s.length();
		if (slen <= len) {
			return s;
		}
		// 最大计数（如果全是英文）
		int maxCount = len * 2;
		int count = 0;
		int i = 0;
		for (; count < maxCount && i < slen; i++) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
		}
		if (i < slen) {
			if (count > maxCount) {
				i--;
			}
			if (!StringUtils.isBlank(append)) {
				if (s.codePointAt(i - 1) < 256) {
					i -= 2;
				} else {
					i--;
				}
				return s.substring(0, i) + append;
			} else {
				return s.substring(0, i);
			}
		} else {
			return s;
		}
	}

	/**
	 * 
	 * @方法功能说明：检查字符串中是否包含被搜索的字符串。被搜索的字符串可以使用通配符'*'。
	 * @参数：@param str		被搜索字符串
	 * @参数：@param search	搜索关键字
	 * @参数：@return
	 * @修改者：
	 * @修改日期：
	 * @修改说明：
	 * @作者：lujun
	 * @创建时间：2015年11月19日 下午1:55:18
	 * @返回值：boolean
	 * @throws
	 */
	public static boolean contains(String str, String search) {
		if (StringUtils.isBlank(str) || StringUtils.isBlank(search)) {
			return false;
		}
		String reg = StringUtils.replace(search, "*", ".*");
		Pattern p = Pattern.compile(reg);
		return p.matcher(str).matches();
	}
	
	/**
	 * 判断是否为空
	 * 
	 * @param value
	 * @return
	 */
	public static final boolean isNullOrEmpty(String value) {
		return (value == null) || (value.length() == 0);
	}
	
	/**
	 * 获取文件后缀
	 * @方法功能说明：
	 * @参数：@param str
	 * @参数：@return
	 * @修改者：
	 * @修改日期：
	 * @修改说明：
	 * @作者：lujun
	 * @创建时间：2015年11月19日 下午1:56:26
	 * @返回值：String
	 * @throws
	 */
	public static String getSuffix(String str) {
		int splitIndex = str.lastIndexOf(".");
		return str.substring(splitIndex + 1);
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	public static void main(String args[]) {
		System.out.println(contains("622", "6222308703845319"));
		
		System.out.println(textCut("6222308703845319", 6, ""));
	}

}
