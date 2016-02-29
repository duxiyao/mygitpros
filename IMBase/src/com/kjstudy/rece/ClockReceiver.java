package com.kjstudy.rece;

import com.kjstudy.service.ServiceMainData;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ClockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("hehe");
        Intent i = new Intent(context, ServiceMainData.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(i);
    }

}
