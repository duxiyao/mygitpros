package com.kjstudy.act;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.ActUtil;
import org.kymjs.kjframe.utils.StringUtils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imbase.R;
import com.kjstudy.bars.BarDefault2;
import com.kjstudy.bean.Entity;
import com.kjstudy.bean.data.TSStudentInfo;
import com.kjstudy.bean.data.TSUserInfo;
import com.kjstudy.core.net.Req;
import com.kjstudy.core.util.BroadCastUtil;
import com.kjstudy.core.util.DBUtil;
import com.kjstudy.core.util.GUtil;
import com.kjstudy.core.util.Global;
import com.kjstudy.core.util.IntentNameUtil;
import com.kjstudy.core.util.JsonUtil;
import com.kjstudy.core.util.cache.CacheManager;
import com.kjstudy.dialog.DialogAssistant;
import com.kjstudy.plugin.AverageView;
import com.kjstudy.plugin.CircleImageView;

public class StuPreInfoEditAct extends KJActivity {

    @BindView(id = R.id.iv_head)
    private CircleImageView mIvHead;

    @BindView(id = R.id.tv_name)
    private TextView mTvName;

    @BindView(id = R.id.tv_sex)
    private TextView mTvSex;

    @BindView(id = R.id.tv_age)
    private TextView mTvAge;

    @BindView(id = R.id.tv_resident)
    private TextView mTvResident;

    @BindView(id = R.id.tv_grade)
    private TextView mTvGrade;

    @BindView(id = R.id.tv_subject)
    private TextView mTvSubject;

    @BindView(id = R.id.tv_personal_signature)
    private TextView mTvPersonalSig;

    @BindView(id = R.id.ll_content)
    private LinearLayout mLlContent;

    private final int INITDATA = 1;

    @Override
    public void setRootView() {
        setContentView(R.layout.layout_personal_info);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setFilters(IntentNameUtil.ON_UPLOAD_HEAD_IMG_SUCCESS,
                IntentNameUtil.ON_ALTER_PERSONAL_INFO_SUCCESS);
        BarDefault2 bar = new BarDefault2();
        setCustomBar(bar.getBarView());
        int len = mLlContent.getChildCount();
        for (int i = 0; i < len; i++) {
            View v = mLlContent.getChildAt(i);
            if (v instanceof RelativeLayout) {
                v.setOnClickListener(this);
            }
        }
        init();
    }

    @Override
    protected void dealBroadcase(Intent intent) {
        super.dealBroadcase(intent);
        if (IntentNameUtil.ON_UPLOAD_HEAD_IMG_SUCCESS
                .equals(intent.getAction())
                || IntentNameUtil.ON_ALTER_PERSONAL_INFO_SUCCESS.equals(intent
                        .getAction())) {
            Message msg = getOsEmptyMsg();
            msg.what = INITDATA;
            sendMsg(msg);
        }
    }

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
        if (INITDATA == msg.what) {
            init();
        }
    }

    private void init() {
        TSUserInfo m = Global.getCURUSER();
        if (m != null) {
            mTvName.setText(m.getName());
            if (m.getSex() != -1)
                mTvSex.setText(m.getSex() == 0 ? "男" : "女");
            if (m.getAge() != -1)
                mTvAge.setText(String.valueOf(m.getAge()));
            CacheManager.inflateHeadFront(m.getPhotoUrl(), mIvHead,
                    R.drawable.default_nor_avatar);

            TSStudentInfo tsInfo = DBUtil.findOne(TSStudentInfo.class, "ubId="
                    + m.getId());
            if (tsInfo != null && tsInfo.getId() != -1) {
                String grade = tsInfo.getGrade();
                String subject = tsInfo.getSubject();
                String latlng = tsInfo.getLatlng();
                grade = GUtil.binary2Ch(R.array.grade_info, grade);
                subject = GUtil.binary2Ch(R.array.subject_info, subject);
                mTvGrade.setText(grade);
                mTvSubject.setText(subject);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        String key = "";
        String strHint = "";
        switch (v.getId()) {
        case R.id.rl_head:
            ActUtil.startAct(UploadImageActivity.class);
            break;
        case R.id.rl_name:
            key = "a.name";
            strHint = "改名字！";
            break;
        case R.id.rl_sex:
            // key = "a.sex";
            // strHint = "";
            choiceSex();
            break;
        case R.id.rl_age:
            key = "a.age";
            strHint = "";
            break;
        default:
            break;
        }
        if (!StringUtils.isEmpty(key)) {
            Bundle b = new Bundle();
            b.putString(InfoEditAct.KEY, key);
            b.putString(InfoEditAct.HINTVALUE, strHint);
            b.putInt(InfoEditAct.INTTYPE, 0);
            ActUtil.startAct(InfoEditAct.class, b);
            return;
        }

        switch (v.getId()) {
        case R.id.rl_resident:
            Bundle p = new Bundle();
            p.putString(LocMapAct.IDENTITY, "student");
            ActUtil.startAct(LocMapAct.class, p);
            break;
        case R.id.rl_grade:
            choiceGrade();
            break;
        case R.id.rl_subject:
            choiceSubject();
            break;
        case R.id.rl_personal_signature:

            break;
        default:
            break;
        }
        if (!StringUtils.isEmpty(key)) {
            Bundle b = new Bundle();
            b.putString(InfoEditAct.KEY, key);
            b.putString(InfoEditAct.HINTVALUE, strHint);
            b.putInt(InfoEditAct.INTTYPE, 1);
            ActUtil.startAct(InfoEditAct.class, b);
            return;
        }
    }

    private void choiceSubject() {
        showChoice(R.layout.layout_dialog_choice_grade, R.array.subject_info);
    }

    private void choiceGrade() {
        showChoice(R.layout.layout_dialog_choice_grade, R.array.grade_info);
    }

    @SuppressLint("InflateParams")
    private void showChoice(int layId, final int arrId) {
        LayoutInflater lif = LayoutInflater.from(getApplicationContext());
        View v = lif.inflate(layId, null);
        AverageView av = (AverageView) v.findViewById(R.id.av_grade_subject);
        final Dialog d = DialogAssistant.getCustomDialog(v);
        OnClickListener lis = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (R.id.tv_cancle == v.getId())
                    d.dismiss();
                else {
                    try {
                        int p = Integer.parseInt(String.valueOf(v.getTag()));
                        String value = Integer.toBinaryString(p);
                        ViewInject.toast(value);
                        if (R.array.subject_info == arrId) {
                            reqUpdate(1, "b.subject", value);
                            d.dismiss();
                        } else if (R.array.grade_info == arrId) {
                            reqUpdate(1, "b.grade", value);
                            d.dismiss();
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Resources res = getResources();
        String[] data = res.getStringArray(arrId);
        List<TextView> list = new ArrayList<TextView>();
        for (String s : data) {
            TextView tv = (TextView) lif.inflate(R.layout.tv_grade_subject,
                    null);
            String[] str = s.split(":");
            tv.setText(str[0]);
            tv.setTag(str[1]);
            tv.setOnClickListener(lis);
            list.add(tv);
        }
        av.setViews(list, 4, false);
        v.findViewById(R.id.tv_cancle).setOnClickListener(lis);
        d.show();

    }

    private void choiceSex() {
        View v = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.layout_dialog_choice_sex, null);

        final Dialog d = DialogAssistant.getCustomDialog(v);
        OnClickListener lis = new OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                case R.id.tv_man:
                    reqUpdate(0, "a.sex", "0");
                    break;
                case R.id.tv_woman:
                    reqUpdate(0, "a.sex", "1");
                    break;
                case R.id.tv_cancle:
                    break;

                default:
                    break;
                }
                d.dismiss();
            }
        };
        v.findViewById(R.id.tv_man).setOnClickListener(lis);
        v.findViewById(R.id.tv_woman).setOnClickListener(lis);
        v.findViewById(R.id.tv_cancle).setOnClickListener(lis);
        d.show();
    }

    private void reqUpdate(int type, String key, String v) {
        Req.updateUserInfo(type, key, v, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    Entity en = JsonUtil.json2Obj(t, Entity.class);
                    if (0 == en.getCode()) {
                        BroadCastUtil
                                .sendBroadCast(IntentNameUtil.SERVICE_ACTION_ON_REQ_STU_TEA_DATA);
                    } else
                        ViewInject.toast("修改失败！");
                } catch(Exception e) {
                    ViewInject.toast("修改失败！");
                }
            }
        });
    }
}
