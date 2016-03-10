package org.myframe.https;

import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;

import android.os.Process;
import android.text.TextUtils;

public class HttpsDispatch extends Thread {

	private static HttpsDispatch mInstance = new HttpsDispatch();

	private HttpsDispatch() {

	}

	private final PriorityBlockingQueue<RequestBean> mNetworkQueue = new PriorityBlockingQueue<RequestBean>();

	public synchronized void addTask(RequestBean run) {
		mNetworkQueue.add(run);
	}

	public static HttpsDispatch getInstance() {
		return mInstance;
	}

	@Override
	public void run() {

		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		while (true) {
			HttpsCb tmpCb=null;
			RequestBean tmpReq=null;
			try {
				final RequestBean req = mNetworkQueue.take();
				if (req == null)
					continue;
				if (req != null) {
					final HttpsCb cb = req.getCb();
					tmpCb=cb;
					tmpReq=req;
					String url = req.getServerAddr();
					HashMap<String, String> params = req.getParams();
					if (params == null)
						params = new HashMap<String, String>();
					HttpsClient hc = HttpsClient.getInstance().c(url)
							.setParams(params);
					if (!req.isPost())
						hc.setMethodGet();
					hc.exec(new HttpsCb() {

						@Override
						public void onResponse(String data, RequestBean rb) {
							if (cb != null)
								cb.onResponse(data, req);
						}

						@Override
						public void onError(String error, RequestBean rb) {
							if (cb != null)
								cb.onError(error, req);
						}
					});
				}
			} catch (Exception e) {
				if (tmpCb != null&&tmpReq!=null)
					tmpCb.onError("", tmpReq);
			}
		}
	}
}
