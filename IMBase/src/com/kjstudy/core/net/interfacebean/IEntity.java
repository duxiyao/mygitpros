package com.kjstudy.core.net.interfacebean;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.kymjs.kjframe.http.HttpParams;

import android.text.TextUtils;

public class IEntity {

    private HashMap<String, String> m = new HashMap<String, String>();

    public HttpParams getP() {

        Field[] fields = getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    String name = field.getName();
                    Object obj = field.get(this);
                    if (!m.containsKey(name) && !TextUtils.isEmpty(name)
                            && !"m".equals(name) && obj != null)
                        m.put(name, String.valueOf(obj));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        HttpParams hp = new HttpParams();
        for (String key : m.keySet())
            hp.put(key, m.get(key));

        return hp;
    }
}
