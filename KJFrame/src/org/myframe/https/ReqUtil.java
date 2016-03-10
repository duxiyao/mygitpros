package org.myframe.https;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ReqUtil {
	private Object mContext;

	public ReqUtil(Object context) {
		mContext = context;
	}

	public static ReqUtil create(Object context) {
		ReqUtil ru = new ReqUtil(context);
		return ru;
	}

	public void reqProxy(String httpCbName, HashMap<String, String> params) {
		try {
			Field field = mContext.getClass().getDeclaredField(httpCbName);
			if (field != null) {
				// 返回BindView类型的注解内容
				ReqConf conf = field.getAnnotation(ReqConf.class);
				if (conf != null) {
					String url = conf.serverHost() + conf.surffix();
					boolean isPost = conf.isPost();
					try {
						field.setAccessible(true);
						RequestBean rb = new RequestBean(
								(HttpsCb) field.get(mContext));
						rb.setServerAddr(url);
						rb.setPost(isPost);
						if (params == null)
							params = new HashMap<String, String>();
						rb.setParams(params);
						HttpsDispatch.getInstance().addTask(rb);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
