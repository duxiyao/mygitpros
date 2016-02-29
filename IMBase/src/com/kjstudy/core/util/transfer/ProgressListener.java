package com.kjstudy.core.util.transfer;

public interface ProgressListener {
	void transferred(long percent);
	void onResponse(boolean isOk,String ret,Exception e);
}
