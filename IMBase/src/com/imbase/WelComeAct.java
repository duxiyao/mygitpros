package com.imbase;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

import org.kymjs.kjframe.KJActivity;

import com.kjstudy.core.util.Global;
import com.kjstudy.service.ServiceMainData;

@SuppressLint("NewApi")
public class WelComeAct extends KJActivity {

	private int mSeconds = 0;
	private TimerTask mTask = new TimerTask() {

		@Override
		public void run() {
			if (mSeconds == 3) {
				skipActivity(WelComeAct.this, MainTSAct.class);
				finish();
			}
			mSeconds++;
		}
	};
	private Timer mTimer;

	@Override
	public void setRootView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_welcom);
	} 

	@Override
	public void initWidget() {
		ObjectAnimator anim = ObjectAnimator.ofFloat(
				getWindow().getDecorView(), "alpha", 0.8f, 1f);
		anim.setDuration(2000);
		anim.start();
		super.initWidget();
		mTimer = new Timer(true);
		mTimer.schedule(mTask, 100, 1000);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTimer.cancel();
		mTimer = null;
	}
}
