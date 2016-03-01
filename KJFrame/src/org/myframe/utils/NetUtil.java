package org.myframe.utils;

import org.myframe.MActivity;
import org.myframe.ui.ActivityStack;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * @author tianzc
 * @date 2015年11月13日
 * @description 网络工具集
 */
public class NetUtil {
	public static final int NETTYPE_WIFI = 0x01;// 无线wifi
	public static final int NETTYPE_CMWAP = 0x02;// wap网
	public static final int NETTYPE_CMNET = 0x03;// net网

	/**
	 * @return
	 * @date 2015年11月13日
	 * @author tianzc
	 * @description 网络是否连接
	 */
	public static boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) ActivityStack.create()
				.topActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null) {
				return ni != null && ni.isConnectedOrConnecting();
			}
		}
		return false;
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */

	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) ActivityStack
				.create().topActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return netType;
		}
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!TextUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}
}
