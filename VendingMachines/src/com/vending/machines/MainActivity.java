package com.vending.machines;

import org.myframe.MActivity;

import com.baidu.location.BDLocation;
import com.vending.machines.act.ChoiceAct;
import com.vending.machines.act.OuttingRiceAct;
import com.vending.machines.act.PayAct;
import com.vending.machines.util.maputil.MapLocation;
import com.vending.machines.util.maputil.MapLocation.LocationListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends MActivity {

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
//		startActivity(new Intent(this,ChoiceAct.class));
//		startActivity(new Intent(this,OuttingRiceAct.class));
		startActivity(new Intent(this,PayAct.class));
//		MapLocation.getInstance().setLLis(new LocationListener() {
//
//			@Override
//			public void onReceiveLocation(BDLocation location) {
//				String s="";
//			}
//		}).startLocation();
		// Dwin.getInstance().hideNavigation();
		// Dwin.getInstance().showNavigation();
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// HttpsClient cl = HttpsClient.getInstance();
		// cl.c("https://icall.ieasycall.com:8964/icallserver/app/user/getOtherUserInfo")
		// .addParam("voipId", "80000300016330").exec(new HttpsCb() {
		// @Override
		// public void onResponse(String data) {
		// String str = data;
		// String s = "";
		// }
		// });
		// }
		// }).start();
		// getWindow().getDecorView().postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// SpeechUtil.getInstance().speak("钟瑞林1");
		// SpeechUtil.getInstance().speak("钟瑞林2");
		// SpeechUtil.getInstance().speak("钟瑞林3");
		// }
		// }, 2000);

	}

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_main);

	}
}
