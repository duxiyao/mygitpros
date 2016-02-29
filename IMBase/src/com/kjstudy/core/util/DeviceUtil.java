package com.kjstudy.core.util;

import org.kymjs.kjframe.ui.ViewInject;

import com.imbase.MyApplication;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class DeviceUtil {

	public static String IMEI = "";

	public static String getImei() {
		if (TextUtils.isEmpty(IMEI)) {
			TelephonyManager tm = (TelephonyManager) MyApplication
					.getInstance().getApplicationContext()
					.getSystemService(Context.TELEPHONY_SERVICE);
			IMEI = tm.getDeviceId();
			if (TextUtils.isEmpty(IMEI)) {
				IMEI = "";
				ViewInject.toast("没有权利获得imei，此操作无法执行。获得imei是数据安全方面的考虑，为必选参数");
			}
		}
		return IMEI;
	}

	public static String getCurTime() {
		return String.valueOf(System.currentTimeMillis());
	}

	public static String getAesCode(String time) throws Exception {
		return MM.aesEncrypt(getImei() + time, "sfgnuiqnyzjy1314");
	}
}
