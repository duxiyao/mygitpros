package org.myframe.utils;

import org.myframe.ui.ActivityStack;

import android.content.Intent;

public class BroadCastUtil {
	public static void sendBroadCast(String action) {
		ActivityStack.create().topActivity()
				.sendBroadcast(new Intent(action));
	}

}
