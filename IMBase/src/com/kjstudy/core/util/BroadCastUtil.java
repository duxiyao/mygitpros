package com.kjstudy.core.util;

import android.content.Intent;

import com.imbase.MyApplication;

public class BroadCastUtil {
    public static void sendBroadCast(String action) {
        MyApplication.getInstance().sendBroadcast(new Intent(action));
    }

}
