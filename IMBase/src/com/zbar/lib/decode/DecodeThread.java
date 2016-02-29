package com.zbar.lib.decode;

import java.util.concurrent.CountDownLatch;

import android.os.Handler;
import android.os.Looper;

import com.zbar.lib.CaptureActivity;

/**
 * 描述: 解码线程
 */
final class DecodeThread extends Thread {
	/** 扫描界面 */
	CaptureActivity activity;
	private Handler handler;
	/** 处理程序初始化锁 */
	private final CountDownLatch handlerInitLatch;// CountDownLatch:计数锁

	DecodeThread(CaptureActivity activity) {
		this.activity = activity;
		handlerInitLatch = new CountDownLatch(1);
	}

	/***
	 * 创建Handler对象
	 * 
	 * @return
	 */
	Handler getHandler() {
		try {
			// 线程等待
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		// 初始化当前线程作为一个活套。这给你一个机会，然后创建处理程序参考这套，然后再开始循环。一定要在调用此方法后调用loop()，结束它通过调用quit()。
		// 准备
		Looper.prepare();
		handler = new DecodeHandler(activity);
		/*
		 * 减少锁的数量，释放所有等待的线程，如果计数达到零。
		 * 如果当前计数大于零则递减。如果新的计数为零，那么所有等待的线程，线程调度的目的是重新启用。 如果当前计数等于零，然后什么都没发生。
		 */
		// 倒计时
		handlerInitLatch.countDown();
		//运行在该线程的消息队列。一定要打电话给quit()来结束循环。//quit()退出
		Looper.loop();
	}

}
