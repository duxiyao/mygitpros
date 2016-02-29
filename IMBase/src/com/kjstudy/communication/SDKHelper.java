package com.kjstudy.communication;

import android.text.TextUtils;

import com.imbase.MyApplication;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECDevice.OnLogoutListener;
import com.yuntongxun.ecsdk.ECInitParams;

public class SDKHelper {
    private static volatile SDKHelper mInstance;

    private ECInitParams mInitParams;

    public static SDKHelper getInstance() {
        if (mInstance == null) {
            synchronized (SDKHelper.class) {
                if (mInstance == null)
                    mInstance = new SDKHelper();
            }
        }
        return mInstance;
    }

    private SDKHelper() {

    }

    public void init() {
        logout();

        if (!ECDevice.isInitialized()) {
            ECDevice.initial(MyApplication.getInstance()
                    .getApplicationContext(), LoginHelper.getInstance());
            return;
        }
        // 已经初始化成功，直接进行注册
        LoginHelper.getInstance().onInitialized();
    }

    public void logout() {
        ECDevice.logout(new OnLogoutListener() {

            @Override
            public void onLogout() {

            }
        });
    }

    public ECInitParams getParams() {
        if (mInitParams == null || mInitParams.getInitParams() == null
                || mInitParams.getInitParams().isEmpty()) {
            mInitParams = new ECInitParams();
        }
        mInitParams.reset();
        // 如：VoIP账号/手机号码/..
        mInitParams.setUserid(CCPConfig.VoIP_ID);
        // appkey
        mInitParams.setAppKey(CCPConfig.App_ID);
        // mInitParams.setAppKey(/*clientUser.getAppKey()*/"ff8080813d823ee6013d856001000029");
        // appToken
        mInitParams.setToken(CCPConfig.App_TOKEN);
        // mInitParams.setToken(/*clientUser.getAppToken()*/"d459711cd14b443487c03b8cc072966e");
        // ECInitParams.LoginMode.FORCE_LOGIN
        mInitParams.setMode(ECInitParams.LoginMode.FORCE_LOGIN);

        // 如果有密码（VoIP密码，对应的登陆验证模式是）
        // ECInitParams.LoginAuthType.PASSWORD_AUTH
        if (!TextUtils.isEmpty(CCPConfig.VoIP_PWD)) {
            mInitParams.setPwd(CCPConfig.VoIP_PWD);
        }

        // 设置登陆验证模式（是否验证密码/如VoIP方式登陆）
        // if(clientUser.getLoginAuthType() != null) {
        mInitParams.setAuthType(ECInitParams.LoginAuthType.PASSWORD_AUTH);
        // }

        // 设置接收VoIP来电事件通知Intent
        // 呼入界面activity、开发者需修改该类
        // Intent intent = new Intent(getInstance().mContext,
        // SDKCoreHelper.class);
        // PendingIntent pendingIntent = PendingIntent.getActivity(
        // getInstance().mContext, 0, intent,
        // PendingIntent.FLAG_UPDATE_CURRENT);
        // mInitParams.setPendingIntent(pendingIntent);

        // 设置SDK注册结果回调通知，当第一次初始化注册成功或者失败会通过该引用回调
        // 通知应用SDK注册状态
        // 当网络断开导致SDK断开连接或者重连成功也会通过该设置回调
        mInitParams.setOnDeviceConnectListener(LoginHelper.getInstance());

        // if(ECDevice.getECMeetingManager() != null) {
        // ECDevice.getECMeetingManager().setOnMeetingListener(MeetingMsgReceiver.getInstance());
        // }
        mInitParams.setOnChatReceiveListener(IMHelper.getInstance());
        return mInitParams;
    }

    public boolean isOnline() {
        return ECDevice.isInitialized()
                && ECDevice.getECDeviceOnlineState() != null
                && ECDevice.getECDeviceOnlineState().ONLINE == ECDevice
                        .getECDeviceOnlineState();
    }
}
