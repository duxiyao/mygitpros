package com.zbar.lib.camera;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 描述: 相机自动对焦
 */
final class AutoFocusCallback implements Camera.AutoFocusCallback {//回调接口用于通知在相机自动对焦完成。

	private static final String TAG = AutoFocusCallback.class.getSimpleName();
	/**自动对焦区间的时间**/
	private static final long AUTOFOCUS_INTERVAL_MS = 1500L;

	private Handler autoFocusHandler;
	private int autoFocusMessage;

	void setHandler(Handler autoFocusHandler, int autoFocusMessage) {
		this.autoFocusHandler = autoFocusHandler;
		this.autoFocusMessage = autoFocusMessage;
	}

	public void onAutoFocus(boolean success, Camera camera) {
		if (autoFocusHandler != null) {
			Message message = autoFocusHandler.obtainMessage(autoFocusMessage, success);
			autoFocusHandler.sendMessageDelayed(message, AUTOFOCUS_INTERVAL_MS);
			autoFocusHandler = null;
		} else {
			Log.d(TAG, "Got auto-focus callback, but no handler for it");
		}
	}

}
