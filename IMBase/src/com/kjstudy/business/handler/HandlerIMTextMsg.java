package com.kjstudy.business.handler;

import com.kjstudy.bean.ImMessage;
import com.kjstudy.core.util.DBUtil;
import com.kjstudy.core.util.DateUtil;
import com.kjstudy.im.views.IMTextView;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

public class HandlerIMTextMsg extends HandlerMsg {
	@Override
	public void handleMsg(ECMessage msg) {
		ECMessage.Type type=msg.getType();
		if(ECMessage.Type.TXT==type){
			ECTextMessageBody txtMsg=(ECTextMessageBody) msg.getBody();
			String msgBody = txtMsg.getMessage();
			String sender = msg.getForm();
			String dateCreate = DateUtil.getCommonDate(msg.getMsgTime());
			String userData = msg.getUserData();
			String msgId = msg.getMsgId();
			String dateRece = DateUtil
					.getCommonDate(System.currentTimeMillis());

			ImMessage imsg = new ImMessage();
			imsg.setMsgBody(msgBody);
			imsg.setVoipAccount(sender);
			imsg.setDateCreate(dateCreate);
			imsg.setUserData(userData);
			imsg.setMsgId(msgId);
			imsg.setDateRece(dateRece);
			imsg.setMsgViewClass(IMTextView.class.getName());
			imsg.setMsgLocationType(1);

			DBUtil.save(imsg);
		}
		super.handleMsg(msg);
	}
}
