package com.kjstudy.communication;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.KJActivityStack;
import org.kymjs.kjframe.ui.ViewInject;

import android.content.Intent;

import com.kjstudy.act.ChattingUI;
import com.kjstudy.act.MyFriendsUI;
import com.kjstudy.core.util.ParamsUtil;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECDevice.ECConnectState;
import com.yuntongxun.ecsdk.ECDevice.InitListener;
import com.yuntongxun.ecsdk.ECDevice.OnECDeviceConnectListener;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.SdkErrorCode;

public class LoginHelper implements InitListener, OnECDeviceConnectListener {

	private static volatile LoginHelper mInstance;

	public static LoginHelper getInstance() {
		if (mInstance == null) {
			synchronized (LoginHelper.class) {
				if (mInstance == null)
					mInstance = new LoginHelper();
			}
		}
		return mInstance;
	}

	private LoginHelper() {

	}

	@Override
	public void onError(Exception arg0) {

	}

	@Override
	public void onInitialized() {
		ECInitParams params = SDKHelper.getInstance().getParams();

		if (!params.validate()) {
			ViewInject.toast("error");
			// Intent intent = new Intent(ACTION_SDK_CONNECT);
			// intent.putExtra("error", -1);
			// mContext.sendBroadcast(intent);
			return;
		}

		if (params.validate()) {
			ECDevice.login(params);
		}
	}

	@Override
	public void onConnect() {
	}

	@Override
	public void onConnectState(ECConnectState state, ECError error) {
		if (state == ECDevice.ECConnectState.CONNECT_FAILED
				&& error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {

		} else if (ECConnectState.CONNECT_SUCCESS.equals(state)) {
			ViewInject.toast("connect success");
//			Object obj = KJActivityStack.create().topActivity();
//			if (obj instanceof KJActivity) {
//				KJActivity act = (KJActivity) obj;
//				act.skipActivity(act, MyFriendsUI.class);
//			}
		}
	}

	@Override
	public void onDisconnect(ECError error) {
		if(100==error.errorCode)
		ViewInject.toast("connecting communication server");
//		ViewInject.toast(error.toString());
	}
}
