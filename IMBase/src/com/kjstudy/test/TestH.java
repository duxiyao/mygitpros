package com.kjstudy.test;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class TestH {
	
	public static Handler handler;
	
	public static void init(){
		HandlerThread thread = new HandlerThread("CCPServiceArguments", 10);
		thread.start();
		Looper looper=thread.getLooper();
		handler=new Handler(looper){
			@Override
			public void handleMessage(Message msg) {
				boolean isMain=Looper.myLooper() == Looper.getMainLooper();
				isMain=Thread.currentThread() == Looper.getMainLooper().getThread();
				String name1=Thread.currentThread().getName();
				String name2=Looper.getMainLooper().getThread().getName();
				String str="";
			}
		}; 
	}
	
	public static void post(){

		handler.post(new Runnable() {
			
			@Override
			public void run() {
				boolean isMain=Looper.myLooper() == Looper.getMainLooper();
				isMain=Thread.currentThread() == Looper.getMainLooper().getThread();
				String name1=Thread.currentThread().getName();
				String name2=Looper.getMainLooper().getThread().getName();
				String str="";
			}
		});
	}
	
//	main(){
//
//		boolean isMain=Looper.myLooper() == Looper.getMainLooper();
//		isMain=Thread.currentThread() == Looper.getMainLooper().getThread();
//		String name1=Thread.currentThread().getName();
//		String name2=Looper.getMainLooper().getThread().getName();
//		
//		final Intent service1=new Intent(this,MyService.class);
//		ServiceConnection conn=new ServiceConnection() {
//			
//			@Override
//			public void onServiceDisconnected(ComponentName name) {
//				
//			}
//			
//			@Override
//			public void onServiceConnected(ComponentName name, IBinder service) {
//				MainActivity.this.startService(service1);
//				MyBinder mb=(MyBinder) service;
//				mb.getHandler().post(new Runnable() {
//					
//					@Override
//					public void run() {
//						boolean isMain=Looper.myLooper() == Looper.getMainLooper();
//						isMain=Thread.currentThread() == Looper.getMainLooper().getThread();
//						String name1=Thread.currentThread().getName();
//						String name2=Looper.getMainLooper().getThread().getName();
//						String str="";	
//					}
//				});
//			}
//		};
//		bindService(service1, conn, 1);
//	}
}
