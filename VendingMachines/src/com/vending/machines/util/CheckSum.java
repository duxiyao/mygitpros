package com.vending.machines.util;

import java.util.List;

import org.myframe.utils.MLoger;

public class CheckSum {

	public static String makeChecksum(String data) {
		if (isEmpty(data)) {
			return "";
		}
		int total = 0;
		int len = data.length();
		int num = 0;
		while (num < len) {
			String s = data.substring(num, num + 2);
			System.out.println(s);
			total += Integer.parseInt(s, 16);
			num = num + 2;
		}
		int mod = total % 256;
		String hex = Integer.toHexString(mod);
		len = hex.length();
		if (len < 2) {
			hex = "0" + hex;
		}
		return hex;
	}

	public static boolean checkChecksum(String data, String sign) {
		// String
		// sourceData="0100150aa303b101b201b301b404b504b601b904ba03be0140";
		// data="0100150aa303b101b201b301b404b504b601b904ba03be01";
		// sign="40";
		if (isEmpty(data) || isEmpty(sign)) {
			return false;
		}
		String checksum = makeChecksum(data);
		System.out.println("=============" + checksum);
		if (checksum.equals(sign)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkPkg(String[] datas) {
		if (datas == null || datas.length == 0 || datas.length < 3)
			return false;
		if (!"AA55".equals(datas[0] + datas[1]))
			return false;
		int len = datas.length;
		if (!"55AA".equals(datas[len - 2] + datas[len - 1]))
			return false;
		String s = "";
		for (int i = 0; i < datas.length - 3; i++) {
			s += datas[i];
		}
		String sign = datas[datas.length - 3];
		return checkChecksum(s, sign);
	}

	static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static void main(String[] args) {
		String data = "0102030405060708";
		System.out.println(makeChecksum(data));
		System.out.println(checkChecksum(data, "24"));
	}
}
