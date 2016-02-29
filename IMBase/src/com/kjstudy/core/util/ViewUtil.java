package com.kjstudy.core.util;

import android.view.View;

public class ViewUtil {
	public static <T> T findView(View v,int vId){
		return (T) v.findViewById(vId);
	}
}
