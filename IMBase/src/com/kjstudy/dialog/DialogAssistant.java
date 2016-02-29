package com.kjstudy.dialog;

import org.kymjs.kjframe.ui.KJActivityStack;
import org.kymjs.kjframe.utils.DensityUtils;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.imbase.R;

public class DialogAssistant {

	public static Dialog getProgressingDialog() {
		int h = (int) (DensityUtils.getScreenH() * 0.5); // 高度设置为屏幕的0.6
		int w = (int) (DensityUtils.getScreenW() * 0.8); // 宽度设置为屏幕的0.65

		Activity act = KJActivityStack.create().topActivity();
		View v = LayoutInflater.from(act).inflate(
				R.layout.custom_progress_dialog, null);

		Dialog d = new Dialog(act, R.style.CustomProgressDialog);
		d.setContentView(v);
		Window win = d.getWindow();
		WindowManager.LayoutParams lp = win.getAttributes();
		win.setGravity(Gravity.CENTER);

		ImageView loadingProfress = (ImageView) v
				.findViewById(R.id.loading_progress);
		Animation animation = AnimationUtils.loadAnimation(act,
				R.anim.loading_progress_animation);
		loadingProfress.setAnimation(animation);

		// lp.width = h;
		// lp.height = w;
		// lp.alpha = 0.7f;
		win.setAttributes(lp);
		d.setCanceledOnTouchOutside(false);
		return d;
	}

    public static Dialog getCustomDialog(View v) {

//		int h = (int) (DensityUtils.getScreenH() * 0.5); // 高度设置为屏幕的0.6
//		int w = (int) (DensityUtils.getDialogW() * 0.8); // 宽度设置为屏幕的0.65

		// Activity act = KJActivityStack.create().topActivity();
		//
		// Dialog d = new Dialog(act, R.style.ccpalertdialog);
		// d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// d.setContentView(v);
		// Window win = d.getWindow();
		// WindowManager.LayoutParams lp = win.getAttributes();
		// win.setGravity(Gravity.CENTER);
		// lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
		// lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// // lp.alpha = 0.7f;
		// win.setAttributes(lp);
		int w = WindowManager.LayoutParams.WRAP_CONTENT;
        int h = WindowManager.LayoutParams.WRAP_CONTENT;
        
		Dialog d = getDialog(R.style.ccpalertdialog, v, w, h, Gravity.CENTER,
				-1, -1, 1f);
		d.setCanceledOnTouchOutside(false);
		d.setCancelable(true);
		return d;
	}

	public static Dialog getPwdDialog(View v) {

		int h = (int) (DensityUtils.getAllScreenH());
		int w = (int) (DensityUtils.getScreenW());

		Dialog d = getDialog(R.style.gesture_pwd_dialog, v, w, h,
				Gravity.CENTER, -1, -1, 1f);
		return d;
	}

	public static Dialog getDialog(int style, View v, int w, int h,
			int gravity, int x, int y, float alpha) {

		Activity act = KJActivityStack.create().topActivity();

		Dialog d = new Dialog(act, style);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.setContentView(v);
		Window win = d.getWindow();
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = w;
		lp.height = h;
		if (x != -1)
			lp.x = x;
		if (y != -1)
			lp.y = y;
		lp.alpha = alpha;
		win.setAttributes(lp);
		win.setGravity(gravity);
		d.setCanceledOnTouchOutside(false);
		d.setCancelable(false);
		return d;
	}
}
