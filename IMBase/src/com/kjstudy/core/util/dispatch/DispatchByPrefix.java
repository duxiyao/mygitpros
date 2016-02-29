package com.kjstudy.core.util.dispatch;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description 两个类进行匹配处理 一个类只写条件，一个类只写处理函数
 * @author duxiyao
 * @date 2016年1月9日 上午10:41:20
 * 
 */
public class DispatchByPrefix {
    LinkedHashMap<String, LinkedList<Method>> lhmPrefix;

    LinkedHashMap<String, LinkedList<Method>> lhmProxy;

    LinkedHashMap<String, LinkedList<AnoPrefix>> lhmAno;

    LinkedList<String> llAno;

    public synchronized void dispatch(Object prefix, Object target,
            Object... args) {

        if (lhmPrefix == null) {
            lhmPrefix = new LinkedHashMap<String, LinkedList<Method>>();
            lhmProxy = new LinkedHashMap<String, LinkedList<Method>>();
            lhmAno = new LinkedHashMap<String, LinkedList<AnoPrefix>>();
            llAno = new LinkedList<String>();

            for (Method m : prefix.getClass().getMethods()) {
                AnoPrefix ano = m.getAnnotation(AnoPrefix.class);
                if (ano != null) {
                    String pre = ano.pre();
                    if (pre == null || pre.trim().length() == 0)
                        continue;
                    if (!llAno.contains(pre)) {
                        llAno.add(pre);
                        LinkedList ll = new LinkedList<Method>();
                        ll.add(m);
                        lhmPrefix.put(pre, ll);
                    } else {
                        lhmPrefix.get(pre).add(m);
                    }
                }
            }
            for (Method m : getMethods(target)) {
                AnoPrefix ano = m.getAnnotation(AnoPrefix.class);
                if (ano != null) {
                    String pre = ano.pre();
                    if (pre == null || pre.trim().length() == 0)
                        continue;
                    if (llAno.contains(pre)) {
                        if (lhmProxy.containsKey(pre)) {
                            lhmProxy.get(pre).add(m);
                        } else {
                            LinkedList ll = new LinkedList<Method>();
                            ll.add(m);
                            lhmProxy.put(pre, ll);
                        }
                        if (lhmAno.containsKey(pre)) {
                            lhmAno.get(pre).add(ano);
                        } else {
                            LinkedList<AnoPrefix> ll = new LinkedList<AnoPrefix>();
                            ll.add(ano);
                            lhmAno.put(pre, ll);
                        }
                    }
                }
            }
        }

        for (String pre : lhmProxy.keySet()) {
            try {
                LinkedList<Method> methods = lhmPrefix.get(pre);
                boolean isRun = true;
                for (Method m : methods) {
                    if (isRun) {
                        Class<?>[] pt = m.getParameterTypes();
                        if (args == null || pt != null
                                && pt.length == args.length)
                            isRun = isRun && (Boolean) m.invoke(prefix, args);
                    } else
                        break;
                }

                methods = lhmProxy.get(pre);
                for (int i = 0; i < methods.size(); i++) {
                    Method m = methods.get(i);
                    AnoPrefix ano = lhmAno.get(pre).get(i);
                    if (isRun || (!isRun && ano.un())) {
                        Class<?>[] pt = m.getParameterTypes();
                        if (args == null || pt != null
                                && pt.length == args.length)
                            m.invoke(target, args);
                        if (ano.finish()) {
                            return;
                        }
                    }
                }
            } catch(Exception e) {
            }
        }
    }

    private List<Method> getMethods(Object obj) {
        HashMap<Integer, Method> hm = new HashMap<Integer, Method>();
        LinkedList<Method> target = new LinkedList<Method>();
        List<Integer> l = new ArrayList<Integer>();
        try {
            for (Method m : obj.getClass().getMethods()) {
                AnoPrefix ano = m.getAnnotation(AnoPrefix.class);
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
