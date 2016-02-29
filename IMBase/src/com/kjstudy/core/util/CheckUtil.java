/**
 *
 */
package com.kjstudy.core.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * simple string validate methods
 * 
 * @author chao
 * @version 1.0.0
 */
public class CheckUtil {

	public static final String[] PHONE_PREFIX = new String[] { "130", "131",
			"132", "133", "134", "135", "136", "137", "138", "139", "150",
			"151", "152", "153", "154", "155", "156", "157", "158", "159",
			"170", "180", "181", "182", "183", "184", "185", "186", "187",
			"188", "189" };

	public static boolean checkLocation(String mdn) {
		return checkMDN(mdn, false);
	}

	/**
	 * 验证手机号码是否标准
	 * 
	 * @param mdn
	 * @return
	 */
	public static boolean checkMDN(String mdn) {
		return checkMDN(mdn, true);
	}

	/**
	 * 检查手机号码合法性
	 * 
	 * @param mdn
	 * @return
	 */
	public static boolean checkMDN(String mdn, boolean checkLen) {
		if (mdn == null || mdn.equals("")) {
			return false;
		}
		// modify by zhangyp 去掉号码前边的+86
		if (mdn.startsWith("+86")) {
			mdn = mdn.substring(3);
		}

		if (checkLen && mdn.length() != 11) {
			return false;
		}

		// 手机号的正则表达式
		Pattern p = Pattern.compile("1(3|4|5|7|8){1}\\d{9}");
		Matcher m = p.matcher(mdn);
		boolean result = m.find();
		// boolean flag = false;
		// String p = mdn.length() > 3 ? mdn.substring(0, 3) : mdn;
		// for (int i = 0; i < PHONE_PREFIX.length; i++) {
		// if (p.equals(PHONE_PREFIX[i])) {
		// flag = true;
		// break;
		// }
		// }
		// if (!flag) {
		// return false;
		// }
		return result;
	}

	public static final char[] INVALID_CH_CN = { 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '.', ',', ';', ':', '!', '@', '/', '(', ')',
			'[', ']', '{', '}', '|', '#', '$', '%', '^', '&', '<', '>', '?',
			'\'', '+', '-', '*', '\\', '\"' };

	public static boolean checkCN(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		char[] cArray = str.toCharArray();
		for (int i = 0; i < cArray.length; i++) {
			for (int j = 0; j < INVALID_CH_CN.length; j++) {
				if (cArray[i] == INVALID_CH_CN[j]) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 是否为新版本, true 为有新版本， 否则；
	 * 
	 * @param mdn
	 * @return
	 */
	public static boolean versionCompare(String oldversion, String newversion) {
		boolean updater = false;
		if (oldversion == null || newversion == null) {
			return updater;
		}
		String clientVersion = oldversion.replace(".", "");
		String webVersion = newversion.replace(".", "");

		try {
			int lastVersion = Integer.valueOf(clientVersion);
			int newVersion = Integer.valueOf(webVersion);
			if (newVersion > lastVersion) {
				updater = true;
			}
		} catch (Exception e) {
			// 上传版本格式错误(不是纯数字的格式)
			int minCount = Math
					.min(webVersion.length(), clientVersion.length());
			int maxCount = Math
					.max(webVersion.length(), clientVersion.length());
			for (int i = 0; i < minCount; i++) {
				if (webVersion.charAt(i) > clientVersion.charAt(i)) {
					updater = true;
					break;
				}
			}
			if (!updater && (minCount != maxCount)) {
				if (webVersion.length() == maxCount) {
					updater = true;
				}
			}
		}
		return updater;
	}

	/**
	 * 检测邮箱合法性
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmailValid(String email) {
		if ((email == null) || (email.trim().length() == 0)) {
			return false;
		}
		String regEx = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(email.trim().toLowerCase(Locale.getDefault()));

		return m.find();
	}

	/**
	 * 
	 * @param mobile
	 * @return
	 */
	public static String formatMobileNumber(String mobile) {
		if (TextUtils.isEmpty(mobile)) {
			return "";
		}
		return mobile.replaceAll("[\\.\\-\\ ]", "").trim();
	}

	public static boolean checkUrl(String url) {
		if (TextUtils.isEmpty(url))
			return false;
		return url.startsWith("http");

		// String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
		// Pattern pattern = Pattern.compile(regex);
		// Matcher matcher = pattern.matcher(url);
		// return matcher.matches();
	}
}
