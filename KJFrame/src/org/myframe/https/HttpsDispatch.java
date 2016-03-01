package org.myframe.https;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.myframe.http.Request;

import android.os.Process;

public class HttpsDispatch extends Thread {

	private static HttpsDispatch mInstance = new HttpsDispatch();

	private HttpsDispatch() {

	}

	private final PriorityBlockingQueue<Runnable> mNetworkQueue = new PriorityBlockingQueue<Runnable>();

	public synchronized void addTask(Runnable run) {
		mNetworkQueue.add(run);
	}

	public static HttpsDispatch getInstance() {
		return mInstance;
	}

	@Override
	public void run() {

		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		while (true) {
			try {
//				mNetworkQueue.poll()
			} catch (Exception e) {
			}
		}
	}
}
