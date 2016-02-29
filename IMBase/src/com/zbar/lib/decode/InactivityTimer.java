package com.zbar.lib.decode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import android.app.Activity;

/**
 * 描述: 不活动计时器
 */
public final class InactivityTimer {

	/**在活动延迟的秒数*/
	private static final int INACTIVITY_DELAY_SECONDS = 5 * 60;
	//newSingleThreadScheduledExecutor 新的单线程的执行
	/**创建一个单线程的执行，可以调度命令后运行一个给定的延迟，或定期执行*/
	private final ScheduledExecutorService inactivityTimer = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
	private final Activity activity;
	/**在未来的活动*/
	private ScheduledFuture<?> inactivityFuture = null;

	public InactivityTimer(Activity activity) {
		this.activity = activity;
		onActivity();
	}

	/**
	 * 创建和启动
	 */
	public void onActivity() {
		cancel();
		//创建和执行一一拍的动作变成了延迟后启用。
		inactivityFuture = inactivityTimer.schedule(new FinishListener(activity), INACTIVITY_DELAY_SECONDS, TimeUnit.SECONDS);
	}

	/***
	 * 取消不活动计时器
	 */
	private void cancel() {
		if (inactivityFuture != null) {
			//尝试取消此任务的执行。
			inactivityFuture.cancel(true);
			inactivityFuture = null;
		}
	}

	/***
	 * 关机
	 */
	public void shutdown() {
		cancel();
		/*修先前提交的任务执行有序关闭，但没有新的任务将被接受。如果已经关闭调用没有额外的影响。
			此方法不会等待先前提交的任务完成执行。使用awaittermination做。*/
		//关机
		inactivityTimer.shutdown();
	}

	/***
	 * 守护线程工厂
	 * @author Administrator
	 *
	 */
	private static final class DaemonThreadFactory implements ThreadFactory {
		/**
		 * 构建了一种新的线程。实现也可以初始化优先，名字，守护状态，可以，等。
		 */
		public Thread newThread(Runnable runnable) {
			Thread thread = new Thread(runnable);
			//如果接收器是一个守护线程或不。这只能是线程开始运行前做的。
			//指示线程应该守护或不
			thread.setDaemon(true);
			return thread;
		}
	}

}
