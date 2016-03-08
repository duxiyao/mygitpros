package com.vending.machines;

import org.myframe.MActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.vending.machines.act.AdminLoginAct;
import com.vending.machines.act.ConfirmFillRiceAct;
import com.vending.machines.act.OrderPwdObtainRiceAct;

public class MainActivity extends MActivity {

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		startActivity(new Intent(this, ConfirmFillRiceAct.class));
		// startActivity(new Intent(this,AdminLoginAct.class));
		// startActivity(new Intent(this,OrderPwdObtainRiceAct.class));
		// startActivity(new Intent(this,ChoiceAct.class));
		// startActivity(new Intent(this,OuttingRiceAct.class));
		// startActivity(new Intent(this,PayAct.class));
		// MapLocation.getInstance().setLLis(new LocationListener() {
		//
		// @Override
		// public void onReceiveLocation(BDLocation location) {
		// String s="";
		// }
		// }).startLocation();
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
