package com.kjstudy.communication;

import java.util.List;

import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

import android.os.Process;

import com.imbase.R;
import com.kjstudy.bean.ImMessage;
import com.kjstudy.business.handler.IMHandlerManager;
import com.kjstudy.core.thread.ThreadManager;
import com.kjstudy.core.util.BroadCastUtil;
import com.kjstudy.core.util.DBUtil;
import com.kjstudy.core.util.DateUtil;
import com.kjstudy.core.util.IntentNameUtil;
import com.kjstudy.core.util.LogUtil;
import com.kjstudy.im.views.IMTextView;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECChatManager.OnSendMessageListener;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

public class IMHelper implements OnChatReceiveListener{
	private static volatile IMHelper mInstance;

	public static IMHelper getInstance() {
		if (mInstance == null) {
			synchronized (IMHelper.class) {
				if (mInstance == null)
					mInstance = new IMHelper();
			}
		}
		return mInstance;
	}

	private IMHelper() {

	}

	@Override
	public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage arg0) {
		
	}

	@Override
	public void OnReceivedMessage(ECMessage msg) {
		if(msg==null)
			return;
		IMHandlerManager.handleMsg(msg);
		BroadCastUtil.sendBroadCast(IntentNameUtil.IM_RECE_IN_DB);
		CCPVibrateUtil.getInstace().doVibrate();
	}

	@Override
	public int onGetOfflineMessage() {
		return ECDevice.SYNC_OFFLINE_MSG_ALL;
	}

	@Override
	public void onOfflineMessageCount(int arg0) {
		
	}

	@Override
	public void onReceiveDeskMessage(ECMessage arg0) {
		
	}

	@Override
	public void onReceiveOfflineMessage(List<ECMessage> msgs) {
		for(ECMessage msg:msgs){
			IMHandlerManager.handleMsg(msg);
		}
		BroadCastUtil.sendBroadCast(IntentNameUtil.IM_RECE_IN_DB);
		CCPVibrateUtil.getInstace().doVibrate();
	}

	@Override
	public void onReceiveOfflineMessageCompletion() {
		
	}

	@Override
	public void onServicePersonVersion(int arg0) {
		
	}

	@Override
	public void onSoftVersion(String arg0, int arg1) {
		
	}
	
	public void sendText(final String voipId, final String text,
			UserData userData) {
		if(!ECDevice.isInitialized()){
			ViewInject.toast("not login");
			return;
		}
		if (StringUtils.strIsEmpty(voipId))
			return;
		if (StringUtils.strIsEmpty(text)) {
			ViewInject.toast(StringUtils.getResString(R.string.none_string));
			return;
		}
		final ImMessage msg = new ImMessage();
		msg.setDateCreate(DateUtil.getCommonDate(System.currentTimeMillis()));
		msg.setDateSend(msg.getDateCreate());
		msg.setMsgBody(text);
		msg.setMsgLocationType(2);
		msg.setMsgViewClass(IMTextView.class.getName());
		msg.setSendState(1);
		msg.setSenderName("me");
		msg.setVoipAccount(voipId);
		if (userData != null)
			msg.setUserData(userData.getUserData());
		
		LogUtil.e(Thread.currentThread().getName());
		ThreadManager.getInstance().executeTask(new Runnable() {
			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				LogUtil.e(Thread.currentThread().getName());
				if (DBUtil.saveBindId(msg)) {
					BroadCastUtil.sendBroadCast(IntentNameUtil.IM_SEND_IN_DB);
					ECMessage ecMsg=ECMessage.createECMessage(ECMessage.Type.TXT);
					ecMsg.setForm(CCPConfig.VoIP_ID);
					ecMsg.setMsgTime(System.currentTimeMillis());
					ecMsg.setTo(voipId);
					ecMsg.setSessionId(voipId);
					ecMsg.setDirection(ECMessage.Direction.SEND);
					ECTextMessageBody msgBody=new ECTextMessageBody(text);
					ecMsg.setBody(msgBody);
					sendMsg(ecMsg,msg);
				} else {
					LogUtil.e("kjdb saveBindId error");
				}

			}
		});
	}
	
	private void sendMsg(ECMessage msg,final ImMessage imMsg){
		ECChatManager manager=ECDevice.getECChatManager();
		manager.sendMessage(msg, new OnSendMessageListener() {
			
			@Override
			public void onProgress(String arg0, int arg1, int arg2) {
				
			}
			
			@Override
			public void onSendMessageComplete(ECError arg0, ECMessage msg) {
				if(msg==null)
					return;
				String msgId = msg.getMsgId();
				imMsg.setMsgId(msgId);
				imMsg.setSendState(2);
				DBUtil.update(imMsg);
				BroadCastUtil.sendBroadCast(IntentNameUtil.IM_SEND_IN_DB);
			}
		});
	}

    @Override
    public void onReceiveMessageNotify(ECMessageNotify arg0) {
    }
}
