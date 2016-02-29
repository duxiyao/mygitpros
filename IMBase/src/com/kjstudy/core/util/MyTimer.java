package com.kjstudy.core.util;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {
    private Timer mT;

    private int mInterval;

    public void start(int seconds, TimerTask tt) {
        mInterval = seconds * 1000;
        mT = new Timer();
        if (tt != null)
            mT.schedule(tt, 0, mInterval);
    }

    public void stop() {
        try {
            mT.cancel();
            mT = null;
        } catch(Exception e) {
        }
    }
}
