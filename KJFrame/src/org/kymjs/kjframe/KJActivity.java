/*
 * Copyright (c) 2014, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kymjs.kjframe;

import org.kymjs.kjframe.ui.FrameActivity;
import org.kymjs.kjframe.ui.KJActivityStack;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.KJLoger;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

/**
 * @author kymjs (https://github.com/kymjs)
 */
public abstract class KJActivity extends FrameActivity {

	/**
	 * 当前Activity状态
	 */
	public static enum ActivityState {
		RESUME, PAUSE, STOP, DESTROY
	}

	public Activity aty;
	/** Activity状态 */
	public ActivityState activityState = ActivityState.DESTROY;

	private IntentFilter mIntentFilter;

	/***************************************************************************
	 * 
	 * print Activity callback methods
	 * 
	 ***************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		aty = this;
		KJActivityStack.create().addActivity(this);
		KJLoger.state(this.getClass().getName(), "---------onCreat ");
		super.onCreate(savedInstanceState);

	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	@Override
	protected void onStart() {
		super.onStart();
		KJLoger.state(this.getClass().getName(), "---------onStart ");
	}

	@Override
	protected void onResume() {
		super.onResume();
		activityState = ActivityState.RESUME;
		KJLoger.state(this.getClass().getName(), "---------onResume ");
		MobclickAgent.onPageStart(this.getClass().getName()); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		activityState = ActivityState.PAUSE;
		KJLoger.state(this.getClass().getName(), "---------onPause ");
		MobclickAgent.onPageEnd(this.getClass().getName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		// onPageEnd 在onPause
		// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		activityState = ActivityState.STOP;
		KJLoger.state(this.getClass().getName(), "---------onStop ");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		KJLoger.state(this.getClass().getName(), "---------onRestart ");
	}

	@Override
	protected void onDestroy() {
		activityState = ActivityState.DESTROY;
		KJLoger.state(this.getClass().getName(), "---------onDestroy ");
		try {
			ViewInject.hideSoftInputFromWindow(getWindow().getDecorView());
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
		KJActivityStack.create().finishActivity(this);
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Class<?> cls) {
		showActivity(aty, cls);
		aty.finish();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Intent it) {
		showActivity(aty, it);
		aty.finish();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
		showActivity(aty, cls, extras);
		aty.finish();
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Intent it) {
		aty.startActivity(it);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
		Intent intent = new Intent();
		intent.putExtras(extras);
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}

	private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null)
				return;
			if (mIntentFilter != null
					&& mIntentFilter.hasAction(intent.getAction()))
				dealBroadcase(intent);
		}
	};

	protected void dealBroadcase(Intent intent) {

	}

	protected void setFilters(String... actions) {
		if (actions != null && actions.length > 0) {
			mIntentFilter = new IntentFilter();
			for (String action : actions) {
				mIntentFilter.addAction(action);
			}
			registerBroadcast();
		} else {
			mIntentFilter = null;
		}
	}

	@Override
	public void registerBroadcast() {
		if (mIntentFilter != null)
			registerReceiver(myBroadcastReceiver, mIntentFilter);
	}

	@Override
	public void unRegisterBroadcast() {
		if (mIntentFilter != null)
			unregisterReceiver(myBroadcastReceiver);
	}

	protected void setCustomBar(View v) {
		try {
			ActionBar bar = getActionBar();
			if (bar != null) {
				bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
				ActionBar.LayoutParams p = new ActionBar.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				bar.setCustomView(v, p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setCustomBarBg(int resId) {
		if (resId == -1)
			return;
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				setTranslucentStatus(true);
			}

			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			// tintManager.setNavigationBarTintEnabled(true);
			tintManager.setStatusBarTintResource(resId);
			// tintManager.setTintColor(getResources().getColor(
			// R.color.color_0698F6));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
