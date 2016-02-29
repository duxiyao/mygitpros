package com.kjstudy.core.thread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {

	private static ThreadManager instance;
	private ThreadPoolExecutor executor;

	private static final int corePoolSize = 8;
	private static final int maximumPoolSize = 32;
	private static final int keepAliveTime = 1 * 10;// 10 seconds
	private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(
			100);

	private ThreadManager() {
		executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
				keepAliveTime, TimeUnit.SECONDS, workQueue,
				new ThreadPoolExecutor.DiscardPolicy());
	}

	public static ThreadManager getInstance() {
		if (instance == null) {
			instance = new ThreadManager();
		}

		return instance;
	}

	public void exeRunnable(Runnable run) {
		if (run != null) {
			executor.execute(run);
		}
	}

	public long getTaskCount() {
		return executor.getTaskCount();
	}

	public int getPoolSize() {
		return executor.getPoolSize();
	}

	public void close() {
		executor.shutdown();
	}

	/**
	 * execute a temp or local task
	 * 
	 * @param task
	 */
	public void executeTask(Runnable task) {
		if (task != null)
			executor.execute(task);
	}

}
