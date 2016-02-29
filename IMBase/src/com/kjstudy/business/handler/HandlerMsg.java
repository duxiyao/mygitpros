package com.kjstudy.business.handler;

import android.content.Context;

import com.imbase.MyApplication;
import com.yuntongxun.ecsdk.ECMessage;

public abstract class HandlerMsg {

	private HandlerMsg mNextHandler;
	protected Context mContext;

	public HandlerMsg() {
		mContext = MyApplication.getInstance().getApplicationContext();
	}

	public void handleMsg(ECMessage msg) {
		dealNext(msg);
	}

	private void dealNext(ECMessage msg) {
		if (mNextHandler != null) {
			mNextHandler.handleMsg(msg);
		}
	}

	public void setNextHandler(HandlerMsg next) {
		this.mNextHandler = next;
	}
}
