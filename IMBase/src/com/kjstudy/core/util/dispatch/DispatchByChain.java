package com.kjstudy.core.util.dispatch;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description 一个接一个的处理逻辑函数
 * @author duxiyao
 * @date 2016年1月9日 上午10:41:20
 * 
 */
public class DispatchByChain {
    private List<Method> mHandlers;

    private boolean isOrder = true;

    public DispatchByChain(boolean isOrder) {
        this.isOrder = isOrder;
    }

    public synchronized void dispatch(Object obj, Object... args) {
        if (mHandlers == null) {
            if (isOrder)
                mHandlers = getOrderMethods(obj);
            else
                mHandlers = getMethod(obj);
        }

        for (Method m : mHandlers) {
            try {
                Class<?>[] pt = m.getParameterTypes();
                if (args == null || pt != null && pt.length == args.length){
                    Object ret = m.invoke(obj, args);
                    if(Boolean.TRUE.equals(ret)){
                        break;
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<Method> getMethod(Object obj) {
        LinkedList<Method> target = new LinkedList<Method>();
        try {
            for (Method m : obj.getClass().getMethods()) {
                AnoChainFun ano = m.getAnnotation(AnoChainFun.class);
                if (ano != null) {
                    target.add(m);
                }
            }
        } catch(Exception e) {
        }
        return target;
    }

    private List<Method> getOrderMethods(Object obj) {
        HashMap<Integer, Method> hm = new HashMap<Integer, Method>();
        LinkedList<Method> target = new LinkedList<Method>();
        List<Integer> l = new ArrayList<Integer>();
        try {
            for (Method m : obj.getClass().getMethods()) {
                AnoChainFun ano = m.getAnnotation(AnoChainFun.class);
                if (ano != null && ano.order() != -1) {
                    hm.put(ano.order(), m);
                    l.add(ano.order());
                }
            }
        } catch(Exception e) {
        }

        Collections.sort(l);
        for (int i : l) {
            target.add(hm.get(i));
        }
        return target;
    }
}
