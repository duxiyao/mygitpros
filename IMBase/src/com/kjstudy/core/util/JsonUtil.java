package com.kjstudy.core.util;

import java.io.IOException;
import java.io.StringReader;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.kjstudy.bean.EntityS;
import com.kjstudy.bean.EntityT;

import android.util.JsonReader;

public class JsonUtil {

    public static <T> EntityT<T> json2ET(String json, Class cls) {
        if (GUtil.isEmpty(json))
            return null;
        try {
            EntityS es = json2Obj(json, EntityS.class);
            String data = es.getData();
            T t = (T) JSON.parseObject(data, cls);
            EntityT<T> et = new EntityT<T>();
            et.setCode(es.getCode());
            et.setMsg(es.getMsg());
            et.setData(t);
            return et;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T json2Obj(String json, Class cls) {
        if (GUtil.isEmpty(json))
            return null;
        try {
            return (T) JSON.parseObject(json, cls);
        } catch(Exception e) {
            return null;
        }
    }

    public static String obj2Json(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static String getString(JSONObject jo, String key) {
        try {
            return jo.getString(key);
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getString(String json, String key) {
        JsonReader jr = new JsonReader(new StringReader(json));
        try {
            jr.beginObject();
            while (jr.hasNext()) {
                String k = jr.nextName();
                String v = jr.nextString();
                if (key.equals(k.toLowerCase())) {
                    return v;
                }
            }
            jr.endObject();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
