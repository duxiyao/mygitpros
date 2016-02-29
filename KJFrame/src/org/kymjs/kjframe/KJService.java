package org.kymjs.kjframe;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class KJService extends Service {

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

    class InternalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            dealBroadcase(intent);
        }

    }

    private InternalReceiver mInternalReceiver;

    protected final void setFilters(String... actions) {
        try {
            if (actions != null && actions.length > 0) {
                IntentFilter intentfilter = new IntentFilter();
                for (String action : actions) {
                    intentfilter.addAction(action);
                }
                if (mInternalReceiver == null) {
                    mInternalReceiver = new InternalReceiver();
                }
                registerReceiver(mInternalReceiver, intentfilter);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void dealBroadcase(Intent intent) {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mInternalReceiver);
    }

}
