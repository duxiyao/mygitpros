package com.kjstudy.core.util;

import java.text.SimpleDateFormat;
import java.util.TimerTask;

public class DateUtil {
	static final SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static String getCommonDate(long millis) {
		return format.format(millis);
	}

	/**
	 * @date 2015年11月9日
	 * @author duxiyao
	 * @description 描述
	 * @param strTime
	 *            ex:2015年11月09日 12:23:12
	 * @return ex:11月09日 凌晨04:29
	 */
	public static String getShortTime(String strTime) {
		String st = strTime.substring(5, strTime.length());
		String st1 = st.substring(0, st.length() - 3);

		String h = st1.substring(7, 9);
		int ih = Integer.parseInt(h);
		StringBuilder sb = new StringBuilder();
		sb.append(st1.substring(0, 7));
		sb.append(getOneWord(ih));
		sb.append(st1.substring(7, st1.length()));
		return sb.toString();
	}

	private static String getOneWord(int ih) {
		if (ih > 3 && ih < 6)
			return "凌晨";
		else if (ih >= 6 && ih < 8)
			return "早";
		else if (ih >= 8 && ih < 12)
			return "上午";
		else if (ih >= 12 && ih < 13)
			return "中午";
		else if (ih >= 13 && ih < 18)
			return "下午";
		else if (ih >= 18 && ih < 19)
			return "傍晚";
		else if (ih >= 19 && ih <= 23)
			return "晚";
		else if (ih >= 0 && ih <= 3)
			return "深夜";
		return "";
	} 
}
