package com.kjstudy.business.builder;

import android.view.View;

import com.kjstudy.a.im.AMsgView;
import com.kjstudy.bean.ImMessage;

public class ImMsgViewBuilder {
	public static View getView(ImMessage msg) {
		Class<? extends AMsgView> cls = msg.getMsgType();
		try {
			AMsgView aMsgView = cls.newInstance();
			return aMsgView.getView(msg);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	} 
}
