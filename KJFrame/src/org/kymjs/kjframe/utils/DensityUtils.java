/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
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
package org.kymjs.kjframe.utils;

import java.lang.reflect.Field;

import org.kymjs.kjframe.ui.KJActivityStack;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Window;

/**
 * 系统屏幕的一些操作<br>
 * 
 * <b>创建时间</b> 2014-8-14
 * 
 * @author kymjs (https://github.com/kymjs)
 * @version 1.1
 */
public final class DensityUtils {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(float dpValue) {
		Context context = KJActivityStack.create().topActivity();
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
	 */
	public static int px2sp(float pxValue) {
		Context context = KJActivityStack.create().topActivity();
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 sp 的单位 转成为 px
	 */
	public static int sp2px(float spValue) {
		Context context = KJActivityStack.create().topActivity();
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 获取dialog宽度
	 */
	public static int getDialogW() {
		Context aty = KJActivityStack.create().topActivity();
		DisplayMetrics dm = new DisplayMetrics();
		dm = aty.getResources().getDisplayMetrics();
		int w = dm.widthPixels - 100;
		// int w = aty.getWindowManager().getDefaultDisplay().getWidth() - 100;
		return w;
	}

	/**
	 * 获取屏幕宽度
	 */
	public static int getScreenW() {
		Context aty = KJActivityStack.create().topActivity();
		DisplayMetrics dm = new DisplayMetrics();
		dm = aty.getResources().getDisplayMetrics();
		int w = dm.widthPixels;
		// int w = aty.getWindowManager().getDefaultDisplay().getWidth();
		return w;
	}

	/**
	 * 获取屏幕高度
	 */
	public static int getScreenH() {
		Context aty = KJActivityStack.create().topActivity();
		DisplayMetrics dm = new DisplayMetrics();
		dm = aty.getResources().getDisplayMetrics();
		int h = dm.heightPixels;
		// int h = aty.getWindowManager().getDefaultDisplay().getHeight();
		return h;
	}

	public static int getAllScreenH() {
		return getScreenH() + getStatusBarHeight();
	}

	public static int getSysStatusBarHeight() {
		Rect frame = new Rect();
		KJActivityStack.create().topActivity().getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		return statusBarHeight;
	}

	public static int getStatusBarHeightByDecor() {
		Window window = KJActivityStack.create().topActivity().getWindow();
		Rect outRect = new Rect();
		window.getDecorView().getWindowVisibleDisplayFrame(outRect);
		return outRect.top;
	}

	public static int getStatusBarHeight() {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			return KJActivityStack.create().topActivity().getResources()
					.getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
			return 75;
		}
	}
}