package com.vending.machines.vendingservice;

import java.util.List;

import com.dwin.dwinapi.MySerialPort;
import com.dwin.dwinapi.OnReceSerialPortData;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

public class CoreService extends Service implements OnReceSerialPortData {
	SerialPortDealBroadcast mSerialPortDealBroadcast;
	MySerialPort mSport;

	@Override
	public void onCreate() {
		super.onCreate();
		mSerialPortDealBroadcast = new SerialPortDealBroadcast();
		IntentFilter inf = new IntentFilter();
		inf.addAction("");
		registerReceiver(mSerialPortDealBroadcast, inf);
		mSport = MySerialPort.getDefault();
		mSport.setOnRecePortData(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * @author Administrator
	 * 
	 *         接收处理与设备间的交互所发出的消息
	 */
	private class SerialPortDealBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

		}

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

		};
	};

	@Override
	public void onReceive(String datas) {
		
	}

	@Override
	public void onError(String ermsg, String datas) {
		
	}
}
