package com.kjstudy.test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class MyService extends Service {
	
	private int lastStartId=-1;
	private MyBinder binder=new MyBinder();

	@Override
	public void onCreate() {
		super.onCreate(); 
		TestH.init();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return this.binder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			return super.onStartCommand(intent, flags, startId);
		}
 
		if (this.lastStartId != -1) {
			stopSelfResult(this.lastStartId);
		}

		this.lastStartId = startId;

		return 3;
	}
	
	public class MyBinder extends Binder{
		public Handler getHandler(){
			return TestH.handler;
		}
		
		public void post(){
			TestH.post();
		}
	}
	
}
