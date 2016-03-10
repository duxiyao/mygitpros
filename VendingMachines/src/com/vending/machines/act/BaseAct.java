package com.vending.machines.act;

import org.myframe.MActivity;

import cn.jpush.android.api.JPushInterface;

public class BaseAct extends MActivity {
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}
}
