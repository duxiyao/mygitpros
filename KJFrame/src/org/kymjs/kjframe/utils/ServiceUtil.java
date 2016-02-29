package org.kymjs.kjframe.utils;

import org.kymjs.kjframe.ui.KJActivityStack;

import android.app.Activity;
import android.content.Intent;

public class ServiceUtil {
    public static void startService(Class cls){
        Activity act=KJActivityStack.create().topActivity();
        Intent intent=new Intent(act,cls);
        act.startService(intent);
    }
}
