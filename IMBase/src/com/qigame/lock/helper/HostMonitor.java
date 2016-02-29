package com.qigame.lock.helper;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

@SuppressLint("NewApi")
public class HostMonitor extends Service {
    
    
    

    @Override
    public void onCreate() {
        super.onCreate();
        super.onCreate();
        Log.i("hellofork",
        "HostMonitor: onCreate Came Back! I can not be Killed!!!");
        stopSelf();
    }
    
    

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.i("hellofork","HostMonitor: onStartCommand!!!");
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
