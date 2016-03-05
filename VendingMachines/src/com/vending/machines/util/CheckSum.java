package com.vending.machines.util;

public class CheckSum {

	/**
	 * ����16�����ۼӺ�У����
	 * 
	 * @param data
	 *            ��ȥУ��λ������
	 * @return
	 */
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
		/**
		 * ��256���������255����16���Ƶ�FF
		 */
		int mod = total % 256;
		String hex = Integer.toHexString(mod);
		len = hex.length();
		// �������У��λ�ĳ��ȣ���0,�����õ�����λУ��
		if (len < 2) {
			hex = "0" + hex;
		}
		return hex;
	}

	/**
	 * 16�����ۼӺ�У��
	 * 
	 * @param data
	 *            ��ȥУ��λ������
	 * @param sign
	 *            У��λ������
	 * @return
	 */
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

	static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static void main(String[] args) {
		String data = "0102030405060708";
		System.out.println(makeChecksum(data));
		System.out.println(checkChecksum(data,"24"));
	}
}
