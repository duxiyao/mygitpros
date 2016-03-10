package com.vending.machines.myrece;

import org.myframe.utils.BroadCastUtil;

import com.vending.machines.util.ActionName;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyJpushRece extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("MyJpushRece.onReceive");
		if (intent == null)
			return;
		try {
			if ("cn.jpush.android.intent.MESSAGE_RECEIVED".equals(intent
					.getAction())) {
				System.out.println("recemsg");
			} else if ("cn.jpush.android.intent.REGISTRATION".equals(intent
					.getAction())) {
				BroadCastUtil.sendBroadCast(ActionName.REQUEST_REGISTER);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
