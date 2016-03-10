package com.vending.machines;

import org.myframe.https.HttpsDispatch;
import org.myframe.utils.MLoger;

import android.app.Application;
import android.content.res.Configuration;
import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

	public static MyApplication mInstance;

	public static MyApplication getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		MLoger.DEBUG_LOG = true;
		MLoger.debug("start"); 
		mInstance = this;
		JPushInterface.setDebugMode(false);
		JPushInterface.init(this);
		
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		System.out.println("onTerminate");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		System.out.println("onConfigurationChanged");
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.out.println("onLowMemory");
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		System.out.println("onTrimMemory");
	}

}
