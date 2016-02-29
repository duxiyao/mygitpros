package com.kjstudy.a.im;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.imbase.MyApplication;
import com.kjstudy.bean.ImMessage;
import com.kjstudy.core.util.ViewUtil;
import com.kjstudy.i.im.IMsgView;
import com.kjstudy.plugin.ChattingItemView;

public abstract class AMsgView implements IMsgView {
	
	protected Context mContext;
	
	public AMsgView(){
		mContext=MyApplication.getInstance().getApplicationContext();
	} 
	
	protected <T> T findView(View v,int vId) {
		return ViewUtil.findView(v, vId);
	}
}
