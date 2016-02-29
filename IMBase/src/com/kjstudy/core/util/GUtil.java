package com.kjstudy.core.util;

import java.util.HashMap;

import com.imbase.MyApplication;
import com.imbase.R;

import android.text.TextUtils;

public class GUtil {

    public static boolean isEmpty(String s) {
        return TextUtils.isEmpty(s) || TextUtils.isEmpty(s.trim());

    }

    public static String binary2Ch(int arrId, String value) {
        if(isEmpty(value))
            return "";
        try {
            String[] arr = MyApplication.getInstance().getResources()
                    .getStringArray(arrId);
            HashMap<String, String> hm = new HashMap<String, String>();
            for (String s : arr) {
                String[] tmp = s.split(":");
                hm.put(tmp[1], tmp[0]);
            }
            return hm.get(String.valueOf(Integer.parseInt(value, 2)));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
