package org.myframe.https;

import java.util.HashMap;

public class RequestBean implements Comparable<RequestBean> {
	private String serverAddr;
	private HashMap<String, String> params;
	private HttpsCb cb;
	private boolean isPost = true;

	public RequestBean(HttpsCb cb) {
		this.cb = cb;
	}

	public String getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

	public HttpsCb getCb() {
		return cb;
	}

	public boolean isPost() {
		return isPost;
	}

	public void setPost(boolean isPost) {
		this.isPost = isPost;
	}

	@Override
	public int compareTo(RequestBean another) {
		return 0;
	}
}
