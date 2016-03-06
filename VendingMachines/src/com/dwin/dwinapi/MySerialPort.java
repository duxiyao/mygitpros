package com.dwin.dwinapi;

import java.util.ArrayList;
import java.util.List;

import org.myframe.utils.MLoger;

import com.vending.machines.exceptions.SerialPortException;
import com.vending.machines.util.CheckSum;

public class MySerialPort {
	private static MySerialPort mInstance = new MySerialPort();
	private OnReceSerialPortData mOnReceSerialPortData;
	private String mDefaultSP = "S0";
	private int mDefaultBoundrate = 9600;
	SerialPort mSerialPort;
	private boolean isReceDatas = false;// 是否接收数据
	private int mTimeOut = 15;// 超时时间
	private ReadThread mReadThread;
	/**
	 * 发送命令时记录的时间
	 */
	private int mSendCMDTime = -1;

	private MySerialPort() {
		mReadThread = new ReadThread();
		startReceThread();
		open();
	}

	private void startReceThread() {
		Thread thread = new Thread(mReadThread);
		thread.start();
	}

	private void open() {
		mSerialPort = new SerialPort(mDefaultSP, mDefaultBoundrate, 8, 1, 110);

	}

	public static MySerialPort getDefault() {
		return mInstance;
	}

	class ReadThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					if (isReceDatas && mSerialPort != null
							&& mSerialPort.isOpen) {
						String hex = mSerialPort.receiveData("HEX");
						if (hex!=null&&mOnReceSerialPortData != null&&hex.contains(" ")) {

							if (CheckSum.checkPkg(hex.split(" ")))
								mOnReceSerialPortData.onReceive(hex);
							else
								mOnReceSerialPortData.onError("pkg error", hex);
						}
					}
				} catch (Exception e) {
				}

				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
		}
	}

	public void setOnRecePortData(OnReceSerialPortData lis) {
		if (lis != null)
			mOnReceSerialPortData = lis;
	}

	public void sendData(String hexdata) throws SerialPortException {
		if (mSerialPort == null || !mSerialPort.isOpen)
			throw new SerialPortException("serial port is null or not open");
		isReceDatas = true;
		mSerialPort.sendData(hexdata, "HEX");
	}

	public void queryRiceLeft() {
		String cmd = "AA5501000055AA";
		try {
			sendData(cmd);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}
}
