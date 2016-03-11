package org.myframe.https;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;

import org.json.JSONException;
import org.json.JSONObject;

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

	public void cancle(String suffix) {
		if (TextUtils.isEmpty(suffix))
			return;
		try {
			Iterator<RequestBean> it = mNetworkQueue.iterator();
			while (it.hasNext()) {
				RequestBean rb = it.next();
				if (rb == null || TextUtils.isEmpty(rb.getServerAddr()))
					continue;
				String url = rb.getServerAddr();
				if (url.contains(suffix))
					rb.setCancle(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		while (true) {
			HttpsCb tmpCb = null;
			RequestBean tmpReq = null;
			int tmpFlag = -10;
			try {
				final RequestBean req = mNetworkQueue.take();
				if (req == null)
					continue;
				if (req.isCancle())
					continue;
				final int flag = req.isReq();
				tmpFlag = flag;
				if (req != null && flag > -1 && !req.isCancle()) {
					final HttpsCb cb = req.getCb();
					tmpCb = cb;
					tmpReq = req;
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
							if (!TextUtils.isEmpty(data)) {
								if (cb != null && !req.isCancle())
									cb.onResponse(data, req);
								try {
									JSONObject jo = new JSONObject(data);
									if (!(jo.has("code") && "0".equals(jo
											.optString("code")))) {
										if (flag == 1 && !req.isCancle()) {
											addTask(req);
										}
									}
								} catch (JSONException e) {
									e.printStackTrace();
									if (flag == 1 && !req.isCancle()) {
										addTask(req);
									}
								}
							} else {
								if (flag == 1 && !req.isCancle()) {
									addTask(req);
								}
							}
						}

						@Override
						public void onError(String error, RequestBean rb) {
							if (cb != null && !req.isCancle())
								cb.onError(error, req);
							if (flag == 1 && !req.isCancle()) {
								addTask(req);
							}
						}
					});
				} else if (flag == -1 && !req.isCancle()) {
					addTask(req);
				}
			} catch (Exception e) {
				if (tmpCb != null && tmpReq != null) {
					tmpCb.onError("", tmpReq);
					if (tmpFlag == 1 && !tmpReq.isCancle()) {
						addTask(tmpReq);
					}
				}
			}
		}
	}
}
