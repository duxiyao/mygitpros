package com.dwin.dwinapi;

public class MySerialPort {
	private static MySerialPort mInstance = new MySerialPort();

	private String mDefaultSP = "S0";
	private int mDefaultBoundrate = 9600;
	SerialPort mSerialPort;

	private MySerialPort() {
		open();
	}

	private void open() {
		mSerialPort = new SerialPort(mDefaultSP, mDefaultBoundrate, 8, 1, 110);
	}

	public static MySerialPort getDefault() {
		return mInstance;
	}

}
