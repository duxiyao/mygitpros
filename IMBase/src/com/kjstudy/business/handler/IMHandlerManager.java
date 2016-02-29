package com.kjstudy.business.handler;

import com.yuntongxun.ecsdk.ECMessage;

public class IMHandlerManager {
	public static void handleMsg(ECMessage msg) {
		HandlerMsg hmsg=new HandlerIMTextMsg();
		hmsg.handleMsg(msg);
	}
}
