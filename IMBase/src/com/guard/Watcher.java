package com.guard;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.util.Log;

/**
 * 监视器类，构造时将会在Native创建子进程来监视当前进程，
 * 
 * @author wangqiang
 * @date 2014-04-24
 */
public class Watcher {
    private static final String PACKAGE = "com.imbase/";

    private String mMonitoredService = "com.kjstudy.service.ServiceMainData";

    private volatile boolean bHeartBreak = false;

    private Context mContext;

    private boolean mRunning = true;

    public void createAppMonitor(String userId) {
        if (!createWatcher(userId)) {
            Log.e("Watcher", "<<Monitor created failed>>");
        }
    }

    public Watcher(Context context) {
        mContext = context;
    }

    private int isServiceRunning() {
        ActivityManager am = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) am
                .getRunningServices(1024);
        for (int i = 0; i < runningService.size(); ++i) {
            if (mMonitoredService.equals(runningService.get(i).service
                    .getClassName().toString())) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Native方法，创建一个监视子进程.
     * 
     * @param userId
     *            当前进程的用户ID,子进程重启当前进程时需要用到当前进程的用户ID.
     * @return 如果子进程创建成功返回true，否则返回false
     */
    private native boolean createWatcher(String userId);

    /**
     * Native方法，让当前进程连接到监视进程.
     * 
     * @return 连接成功返回true，否则返回false
     */
    private native boolean connectToMonitor();

    /**
     * Native方法，向监视进程发送任意信息
     * 
     * @param 发给monitor的信息
     * @return 实际发送的字节
     */
    private native int sendMsgToMonitor(String msg);

    public native void startGuardProcess();

    static {
        System.loadLibrary("mysafes");
    }
}