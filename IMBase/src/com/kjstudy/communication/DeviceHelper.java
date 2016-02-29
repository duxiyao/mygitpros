//package com.kjstudy.communication;
//
//import org.kymjs.kjframe.ui.ViewInject;
//import org.kymjs.kjframe.utils.StringUtils;
//
//import android.bluetooth.BluetoothClass.Device;
//import android.os.Process;
//
//import com.imbase.R;
//import com.kjstudy.bean.ImMessage;
//import com.kjstudy.core.thread.ThreadManager;
//import com.kjstudy.core.util.BroadCastUtil;
//import com.kjstudy.core.util.DBUtil;
//import com.kjstudy.core.util.DateUtil;
//import com.kjstudy.core.util.IntentNameUtil;
//import com.kjstudy.core.util.LogUtil;
//import com.kjstudy.im.views.IMTextView;
//
//public class DeviceHelper {
//	private static DeviceHelper instance;
//
//	private DeviceHelper() {
//
//	}
//
//	public static DeviceHelper getInstance() {
//		if (instance == null) {
//			synchronized (DeviceHelper.class) {
//				if (instance == null)
//					instance = new DeviceHelper();
//			}
//		}
//		return instance;
//	}
//
//	public void sendText(final String voipId, final String text,
//			UserData userData) {
//		if (StringUtils.strIsEmpty(voipId))
//			return;
//		if (StringUtils.strIsEmpty(text)) {
//			ViewInject.toast(StringUtils.getResString(R.string.none_string));
//			return;
//		}
//		final Device device = CCPHelper.getInstance().getDevice();
//		if (device == null)
//			return;
//		final ImMessage msg = new ImMessage();
//		msg.setDateCreate(DateUtil.getCommonDate(System.currentTimeMillis()));
//		msg.setDateSend(msg.getDateCreate());
//		msg.setMsgBody(text);
//		msg.setMsgLocationType(2);
//		msg.setMsgViewClass(IMTextView.class.getName());
//		msg.setSendState(1);
//		msg.setSenderName("me");
//		msg.setVoipAccount(voipId);
//		if (userData != null)
//			msg.setUserData(userData.getUserData());
//		
//		LogUtil.e(Thread.currentThread().getName());
//		ThreadManager.getInstance().executeTask(new Runnable() {
//			@Override
//			public void run() {
//				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
//				LogUtil.e(Thread.currentThread().getName());
//				if (DBUtil.saveBindId(msg)) {
//					BroadCastUtil.sendBroadCast(IntentNameUtil.IM_SEND_IN_DB);
//					String msgId = device.sendInstanceMessage(voipId, text,
//							null, msg.getUserData());
//					msg.setMsgId(msgId);
////					msg.setSendState(2);
//					DBUtil.update(msg);
//					BroadCastUtil.sendBroadCast(IntentNameUtil.IM_SEND_IN_DB);
//				} else {
//					LogUtil.e("kjdb saveBindId error");
//				}
//
//			}
//		});
//	}
//	
////	public boolean reSendText() {
////		return true;
////	}
//
////	public boolean isOnline() {
////		Device device = CCPHelper.getInstance().getDevice();
////		if (device == null)
////			return false;
////		switch (device.isOnline()) {
////		case NOTEXIST:
////			break;
////		case OFFLINE:
////			break;
////		case ONLINE:
////			return true;
////		case TIMEOUT:
////			break;
////		default:
////			break;
////		}
////		return false;
////	}
//}
