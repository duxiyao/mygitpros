package com.vending.machines.myrece;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyJpushRece extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("MyJpushRece.onReceive");
		try {
			if ("cn.jpush.android.intent.MESSAGE_RECEIVED".equals(intent
					.getAction())) {
				System.out.println("recemsg");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
