package com.kjstudy.frag;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.ActUtil;
import org.kymjs.kjframe.utils.ServiceUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.guard.Watcher;
import com.imbase.LoginAct;
import com.imbase.R;
import com.kjstudy.act.StuPreInfoEditAct;
import com.kjstudy.act.UploadImageActivity;
import com.kjstudy.act.base.WebViewAct;
import com.kjstudy.bean.data.TSUserInfo;
import com.kjstudy.core.util.BroadCastUtil;
import com.kjstudy.core.util.DBUtil;
import com.kjstudy.core.util.Global;
import com.kjstudy.core.util.IntentNameUtil;
import com.kjstudy.core.util.cache.CacheManager;
import com.kjstudy.dialog.DialogAssistant;
import com.kjstudy.plugin.AverageView;
import com.kjstudy.plugin.CircleImageView;
import com.kjstudy.service.ServiceMainData;
import com.umeng.socialize.media.GooglePlusShareContent;

public class StudentFrag extends BFrag {

    @BindView(id = R.id.iv_head, click = true)
    private CircleImageView mCIVHead;

    @BindView(id = R.id.tv_name, click = true)
    private TextView mTvName;

    @BindView(id = R.id.cb)
    private CheckBox mCB;

    private final int LOGINSUCCESS = 1;

    private final int REGISTERSUCCESS = 2;

    private final int ONLASTUSERLOGIN = 3;

    private final int ONINITDATA = 4;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_student_me;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (Global.getCURUSER() != null
                        && Global.getCURUSER().getId() != -1)
                    if (isChecked) {
                        BroadCastUtil
                                .sendBroadCast(IntentNameUtil.SERVICE_ACTION_ON_UP_REAL_TIME_POS);
                    } else {
                        BroadCastUtil
                                .sendBroadCast(IntentNameUtil.SERVICE_ACTION_ON_STOP_REAL_TIME_POS);
                    }
            }
        });
        setFilters(IntentNameUtil.LOGIN_SUCCESS,
                IntentNameUtil.REGISTER_SUCCESS,
                IntentNameUtil.ON_UPLOAD_HEAD_IMG_SUCCESS,
                IntentNameUtil.ON_LAST_USER_LOGIN);
        Message msg = getOsEmptyMsg();
        msg.what = ONINITDATA;
        sendMsg(msg);
    }

    @Override
    protected void dealBroadcase(Intent intent) {
        super.dealBroadcase(intent);
        String action = intent.getAction();
        if (IntentNameUtil.LOGIN_SUCCESS.equals(action)) {
            Message msg = getOsEmptyMsg();
            msg.what = LOGINSUCCESS;
            sendMsg(msg);
        } else if (IntentNameUtil.REGISTER_SUCCESS.equals(action)) {
            Message msg = getOsEmptyMsg();
            msg.what = REGISTERSUCCESS;
            sendMsg(msg);
        } else if (IntentNameUtil.ON_LAST_USER_LOGIN.equals(action)) {
            Message msg = getOsEmptyMsg();
            msg.what = ONLASTUSERLOGIN;
            sendMsg(msg);
        } else if (IntentNameUtil.ON_UPLOAD_HEAD_IMG_SUCCESS.equals(action)) {
            Message msg = getOsEmptyMsg();
            msg.what = ONINITDATA;
            sendMsg(msg);
        }
    }

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
        switch (msg.what) {
        case LOGINSUCCESS:
        case REGISTERSUCCESS:
        case ONLASTUSERLOGIN:
        case ONINITDATA:
            // ServiceUtil.startService(ServiceMainData.class);
            TSUserInfo info = Global.getCURUSER();
            if (info != null) {
                String name = info.getName();
                if (TextUtils.isEmpty(name))
                    name = "完善资料";
                mTvName.setText(name);
                String url = info.getPhotoUrl();
                CacheManager.inflateHeadFront(url, mCIVHead,
                        R.drawable.default_nor_avatar);
            }
            break;

        default:
            break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.iv_head:
//            ActUtil.startAct(UploadImageActivity.class);
            Bundle p=new Bundle();
            p.putString(WebViewAct.EXTRA_RAWURL, "file:///android_asset/ichartjs-master/samples/index.html");
            ActUtil.startAct(WebViewAct.class, p);
            break;
        case R.id.tv_name:
            if (!Global.isLogining()) {
                ActUtil.startAct(LoginAct.class);
                getActivity().overridePendingTransition(
                        R.anim.sideslip_in_from_right,
                        R.anim.sideslip_out_from_left);
            } else {
                ActUtil.startAct(StuPreInfoEditAct.class);
            }
            break;
        default:
            break;
        }
    }
}
