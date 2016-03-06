package com.dwin.dwinapi;

import java.util.List;

public interface OnReceSerialPortData {
	void onReceive(String datas);
	void onError(String ermsg,String datas);
}
