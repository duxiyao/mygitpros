package com.kjstudy.frag;

import org.kymjs.kjframe.ui.AnnotateUtil;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class BFrag extends Fragment implements OnClickListener {

    View mView = null;

    protected int getLayoutId() {
        return -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        int resId = getLayoutId();

        if (resId != -1)
            mView = inflater.inflate(resId, container, false);
        if (mView == null)
            mView = super.onCreateView(inflater, container, savedInstanceState);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AnnotateUtil.initBindView(this);
        try {
            if (mView != null) {
                ObjectAnimator an = ObjectAnimator.ofFloat(mView, "alpha", 0f,
                        1f);
                an.setDuration(100);
                an.start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        initWidget();
    }

    protected void setCustomBar(View v) {
        try {
            ActionBar bar = getActivity().getActionBar();
            if (bar != null) {
                bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                bar.setCustomView(v);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected void initWidget() {
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            unRegisterReceiver();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private InternalReceiver internalReceiver;

    protected final void setFilters(String... actions) {
        try {
            if (actions != null && actions.length > 0) {
                IntentFilter intentfilter = new IntentFilter();
                for (String action : actions) {
                    intentfilter.addAction(action);
                }
                if (internalReceiver == null) {
                    internalReceiver = new InternalReceiver();
                }
                getActivity().registerReceiver(internalReceiver, intentfilter);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected final void unRegisterReceiver() {
        if (internalReceiver != null) {
            getActivity().unregisterReceiver(internalReceiver);
        }
    }

    private class InternalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            dealBroadcase(intent);
        }
    }

    protected void dealBroadcase(Intent intent) {

    }

    protected <T> T findView(int vId) {
        return (T) mView.findViewById(vId);
    }

    // 当线程中初始化的数据初始化完成后，调用回调方法
    private Handler threadHandle = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            handleMsg(msg);
        };
    };

    protected void sendMsg(Message msg) {
        threadHandle.sendMessage(msg);
    }

    protected void handleMsg(Message msg) {
    }

    protected Message getOsEmptyMsg(int what) {
        return threadHandle.obtainMessage(what);
    }

    protected Message getOsEmptyMsg() {
        return threadHandle.obtainMessage();
    }

    @Override
    public void onClick(View v) {
    }
}
