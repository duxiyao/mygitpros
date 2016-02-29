package com.kjstudy.test.view;

import com.imbase.MyApplication;
import com.imbase.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View.OnClickListener;

public class MapInfo {
	private String name;
	private int resIdDef;
	private int resIdPress = -1;
	private OnClickListener clickListener;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getResIdDef() {
		return resIdDef;
	}

	public void setResIdDef(int resIdDef) {
		this.resIdDef = resIdDef;
		// bmpDef = getBmp(resIdDef);
		// drDef = getDrawable(resIdDef);
	}

	public int getResIdPress() {
		return resIdPress;
	}

	public void setResIdPress(int resIdPress) {
		this.resIdPress = resIdPress;
		// bmpPress = getBmp(resIdPress);
		// drPress = getDrawable(resIdPress);
	}

	public OnClickListener getClickListener() {
		return clickListener;
	}

	public void setClickListener(OnClickListener clickListener) {
		this.clickListener = clickListener;
	}
//
//	private Bitmap getBmp(int resId) {
//		// return BitmapFactory.decodeResource(MyApplication.getInstance()
//		// .getResources(), resId);
//		return getbmp(resId);
//	}
//
//	private Drawable getDrawable(int resId) {
//		return MyApplication.getInstance().getResources().getDrawable(resId);
//	}
//
//	Bitmap getbmp(int resId) {
//		BitmapFactory.Options opt = new BitmapFactory.Options();
//		opt.inPreferredConfig = Bitmap.Config.RGB_565;
//		opt.inPurgeable = true;
//		opt.inInputShareable = true;
//		return BitmapFactory.decodeResource(MyApplication.getInstance()
//				.getResources(), R.drawable.man_front, opt);
//	}
}
