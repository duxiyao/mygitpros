package com.vending.machines.vendingservice;

import java.util.HashMap;

import org.myframe.https.HttpsCb;
import org.myframe.https.HttpsDispatch;
import org.myframe.https.ReqConf;
import org.myframe.https.ReqUtil;
import org.myframe.https.RequestBean;
import org.myframe.utils.SystemTool;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;

import com.dwin.dwinapi.MySerialPort;
import com.dwin.dwinapi.OnReceSerialPortData;
import com.vending.machines.util.ActionName;

public class CoreService extends Service implements OnReceSerialPortData {
	SerialPortDealBroadcast mSerialPortDealBroadcast;
	MySerialPort mSport;
	private final int WHAT_REQ_REGISTER = 10;

	@Override
	public void onCreate() {
		super.onCreate();
		HttpsDispatch.getInstance().start();
		mSerialPortDealBroadcast = new SerialPortDealBroadcast();
		IntentFilter inf = new IntentFilter();
		inf.addAction(ActionName.REQUEST_REGISTER);
		registerReceiver(mSerialPortDealBroadcast, inf);
		sendHMsg(WHAT_REQ_REGISTER);
		// mSport = MySerialPort.getDefault();
		// mSport.setOnRecePortData(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * @author Administrator
	 * 
	 *         接收处理与设备间的交互所发出的消息
	 */
	private class SerialPortDealBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null)
				return;
			try {
				String action = intent.getAction();
				if (ActionName.REQUEST_REGISTER.equals(action)) {
					sendHMsg(WHAT_REQ_REGISTER);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void sendHMsg(int what) {
		Message msg = mHandler.obtainMessage(what);
		msg.sendToTarget();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_REQ_REGISTER:
				String imei = SystemTool.getPhoneIMEI(CoreService.this);
				String registerId = JPushInterface
						.getRegistrationID(CoreService.this);
				if (!TextUtils.isEmpty(imei) && !TextUtils.isEmpty(registerId)) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("imei", imei);
					params.put("registrationID", registerId);
					ReqUtil.create(CoreService.this).reqProxy("registerCb",
							params);
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onReceive(String datas) {

	}

	@Override
	public void onError(String ermsg, String datas) {

	}

	@ReqConf(surffix = "/api/device/register",reReqInterval=300)
	private HttpsCb registerCb = new HttpsCb() {

		@Override
		public void onResponse(String data, RequestBean rb) {
			System.out.println("registerCb-->onResponse");
		}

		@Override
		public void onError(String error, RequestBean rb) {
			System.out.println("registerCb-->onError");
		}
	};

}
