package com.imbase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;

import com.baidu.mapapi.SDKInitializer;
import com.kjstudy.rece.ClockReceiver;
import com.kjstudy.service.ServiceMainData;

public class MyApplication extends Application {

    public static MyApplication mInstance;

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        SDKInitializer.initialize(this);

        startService(new Intent(this, ServiceMainData.class));
        // String executable = "libhelper.so";
        // String aliasfile = "helper";
        // String r = NativeRuntime.getInstance().RunExecutable(
        // getPackageName(),
        // executable,
        // aliasfile,
        // getPackageName()
        // + "/com.qigame.lock.helper.HostMonitor");
        // System.out.println(r);
        // try {
        // NativeRuntime
        // .getInstance()
        // .startService(
        // getPackageName()
        // + "/com.kjstudy.service.ServiceMainData");
        // } catch(Exception e) {
        // e.printStackTrace();
        // }

//        startClock();
        // Watcher w = new Watcher(this);
        // w.createAppMonitor(String.valueOf(android.os.Process.myUid()));
        // CrashHandler.getInstance().init(getApplicationContext());

        // ThreadManager.getInstance().exeRunnable(new Runnable() {
        //
        // @Override
        // public void run() {
        // try {
        // FileUtils.copyFromAssets(MyApplication.this.getAssets(),
        // "ANProcessGuard", "/data/data/com.imbase/ANProcessGuard");
        // // Watcher w = new Watcher(MyApplication.this);
        // // w.startGuardProcess();
        // // do_exec("ps | grep com.imbase >> /sdcard/a.txt");
        // // do_exec("/sdcard/ANProcessGuard");
        // } catch(Exception e) {
        // e.printStackTrace();
        // }
        // }
        // });
    }

    private void startClock() {
        Intent intent = new Intent(this, ClockReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        // We want the alarm to go off 10 seconds from now.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        // Schedule the alarm!
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                10 * 1000, sender);
    }

    private String do_exec(String cmd) {
        String s = "/n";
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                s += line + "/n";
            }
            System.out.println(s);
        } catch(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cmd;
    }
    
    @Override
    public void onTerminate() {
        super.onTerminate();
        System.out.println("onTerminate");
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        System.out.println("onConfigurationChanged");
    }
    
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.out.println("onLowMemory");
    }
    
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        System.out.println("onTrimMemory");
    }
}
