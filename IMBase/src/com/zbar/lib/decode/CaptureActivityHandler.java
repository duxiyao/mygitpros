package com.zbar.lib.decode;

import android.os.Handler;
import android.os.Message;

import com.imbase.R;
import com.zbar.lib.CaptureActivity;
import com.zbar.lib.camera.CameraManager;

/**
 * 描述: 扫描消息转发
 */
public final class CaptureActivityHandler extends Handler {
	/** 解码线程 */
	DecodeThread decodeThread = null;
	/** 扫描界面 */
	CaptureActivity activity = null;
	/** 状态 */
	private State state;

	private enum State {
		/** 预览 */
		PREVIEW,
		/** 成功 */
		SUCCESS,
		/** 干 */
		DONE
	}

	/***
	 *  扫描消息转发
	 * @param activity
	 */
	public CaptureActivityHandler(CaptureActivity activity) {
		this.activity = activity;
		decodeThread = new DecodeThread(activity);
		decodeThread.start();
		state = State.SUCCESS;
		CameraManager.get().startPreview();
		restartPreviewAndDecode();
	}

	@Override
	public void handleMessage(Message message) {

		switch (message.what) {
		// 自动对焦
		case R.id.auto_focus:
			if (state == State.PREVIEW) {
				CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			}
			break;
		// 启动预览
		case R.id.restart_preview:
			restartPreviewAndDecode();
			break;
		// 解码成功
		case R.id.decode_succeeded:
			state = State.SUCCESS;
			activity.handleDecode((String) message.obj);// 解析成功，回调
			break;
		// 解码失败
		case R.id.decode_failed:
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);
			break;
		}

	}

	/**
	 * 退出同步*
	 * 
	 */
	public void quitSynchronously() {
		state = State.DONE;
		CameraManager.get().stopPreview();
		// 移除任何即将发布的消息代码“什么”的在消息队列。
		removeMessages(R.id.decode_succeeded);
		removeMessages(R.id.decode_failed);
		removeMessages(R.id.decode);
		removeMessages(R.id.auto_focus);
	}

	/***
	 * 启动预览和解码
	 */
	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);
			CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
		}
	}

}
