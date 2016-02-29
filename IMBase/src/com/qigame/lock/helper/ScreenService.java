package com.qigame.lock.helper;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ScreenService extends Service{
    

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        
        @Override
        public void onReceive(Context context, Intent intent) {
            //Intent ii = new Intent(context, MyActivity.class);
            //ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(ii);
            NativeRuntime.getInstance().startActivity(getPackageName()+"/com.qigame.lock.helper.MyActivity");
        }
    };
    
    @Override
    public void onCreate() {
        super.onCreate();
        super.onCreate();
        Log.i("hellofork", "ScreenService: onCreate .....!");
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(mReceiver, filter);
        } catch (Exception e) {
        }
    }
    
    

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.i("hellofork","HostMonitor: onStartCommand!!!");
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        Log.i("hellofork", "ScreenService: onDestroy .....!");
        super.onDestroy();
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
        }
    }
    
    

}
