package org.kymjs.kjframe.utils;

import org.kymjs.kjframe.ui.KJActivityStack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActUtil {
	public static void startAct(Class cls) {
		Activity act = KJActivityStack.create().topActivity();
		Intent intent = new Intent(act, cls);
		act.startActivity(intent);
	}

	public static void startAct(Class cls, Bundle p) {
		Activity act = KJActivityStack.create().topActivity();
		Intent intent = new Intent(act, cls);
		if (p != null)
			intent.putExtras(p);
		act.startActivity(intent);
	}
}
