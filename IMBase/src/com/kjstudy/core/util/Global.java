package com.kjstudy.core.util;

import java.util.Map;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.PreferenceHelper;

import android.text.TextUtils;

import com.imbase.MyApplication;
import com.kjstudy.bean.ETSUserInfo;
import com.kjstudy.bean.data.TSUserInfo;
import com.kjstudy.communication.CCPConfig;
import com.kjstudy.communication.SDKHelper;
import com.kjstudy.core.io.FileUtil;
import com.kjstudy.core.net.Req;

public class Global {

    private static boolean ISLOGING = false;

    public static boolean isLogining() {
        return ISLOGING;
    }

    private static TSUserInfo CURUSER;

    final static String KEY = "id"; // phone or qqOpenId or wxOpenId

    /**
     * @date 2015年11月3日
     * @author duxiyao
     * @description phone , qqOpenId , wxOpenId
     */
    final static String LOGIN_TYPE = "login_type";

    public static TSUserInfo getCURUSER() {
        return CURUSER;
    }

    /**
     * @date 2015年11月3日
     * @author duxiyao
     * @description 上次登录的用户
     */
    public static void lastLoginUser() {
        String id = PreferenceHelper.readString(MyApplication.getInstance()
                .getApplicationContext(), FileUtil.FN_CURUSERKEY, KEY);
        if (TextUtils.isEmpty(id))
            return;
        final String loginType = PreferenceHelper.readString(MyApplication
                .getInstance().getApplicationContext(), FileUtil.FN_CURUSERKEY,
                LOGIN_TYPE);
        if (DBUtil.getDB().isExists(TSUserInfo.class,
                loginType + "='" + id + "'")) {
            TSUserInfo u = DBUtil.findOne(TSUserInfo.class, loginType + "='"
                    + id + "'");
            if (u != null) {
                Req.loginTS(u.getPhone(), u.getPwd(), new HttpCallBack() {
                    @Override
                    public void onSuccess(Map<String, String> headers, byte[] t) {
                        String ret = new String(t);
                        ViewInject.toast(String.valueOf(ret));
                        ETSUserInfo user = JsonUtil.json2Obj(ret,
                                ETSUserInfo.class);
                        if (user != null && user.getCode() == 0
                                && user.getData() != null) {
                            TSUserInfo u = user.getData();
                            setCURUSER(u, loginType);
                            BroadCastUtil
                                    .sendBroadCast(IntentNameUtil.ON_LAST_USER_LOGIN);
                            notifyServiceDownDatas();
                        } else {
                            if (user != null)
                                ViewInject.toast(user.getMsg());
                        }
                    }

                });
            }
        }
    }

    public static void setCURUSER(String id) {
        CURUSER = DBUtil.findOne(TSUserInfo.class, id + "=" + id);
    }

    public static void setCURUSER(TSUserInfo u) {
        setCURUSER(u, "phone");
    }

    public static void setCURUSER(TSUserInfo u, String loginType) {
        if (u == null || u.getId() == -1)
            return;
        if (!GUtil.isEmpty(loginType)) {
            if ("phone".equals(loginType)) {
                PreferenceHelper.write(MyApplication.getInstance()
                        .getApplicationContext(), FileUtil.FN_CURUSERKEY,
                        LOGIN_TYPE, loginType);
                PreferenceHelper.write(MyApplication.getInstance()
                        .getApplicationContext(), FileUtil.FN_CURUSERKEY, KEY,
                        u.getPhone());
            } else if ("qqOpenId".equals(loginType)) {
                PreferenceHelper.write(MyApplication.getInstance()
                        .getApplicationContext(), FileUtil.FN_CURUSERKEY,
                        LOGIN_TYPE, loginType);
                PreferenceHelper.write(MyApplication.getInstance()
                        .getApplicationContext(), FileUtil.FN_CURUSERKEY, KEY,
                        u.getQqOpenId());
            } else if ("wxOpenId".equals(loginType)) {
                PreferenceHelper.write(MyApplication.getInstance()
                        .getApplicationContext(), FileUtil.FN_CURUSERKEY,
                        LOGIN_TYPE, loginType);
                PreferenceHelper.write(MyApplication.getInstance()
                        .getApplicationContext(), FileUtil.FN_CURUSERKEY, KEY,
                        u.getQqOpenId());
            }
            ISLOGING = true;
            BroadCastUtil.sendBroadCast(IntentNameUtil.LOGIN_SUCCESS);
            notifyServiceDownDatas();
        }
        CURUSER = u;
        PreferenceHelper.write(MyApplication.getInstance()
                .getApplicationContext(), FileUtil.FN_CURUSERKEY, KEY, u
                .getPhone());
        CCPConfig.VoIP_ID = u.getVoipAccount();
        CCPConfig.VoIP_PWD = u.getVoipPwd();
        CCPConfig.Sub_Account = u.getSubAccountSid();
        CCPConfig.Sub_Token = u.getSubToken();
        // if (TextUtils.isEmpty(CCPConfig.VoIP_ID)
        // || TextUtils.isEmpty(CCPConfig.VoIP_PWD)
        // || TextUtils.isEmpty(CCPConfig.Sub_Account)
        // || TextUtils.isEmpty(CCPConfig.Sub_Token))
        // return;
        SDKHelper.getInstance().init();
    }

    public static void notifyServiceDownDatas() {
        BroadCastUtil
                .sendBroadCast(IntentNameUtil.SERVICE_ACTION_ON_REQ_STU_TEA_DATA);
    }
}
