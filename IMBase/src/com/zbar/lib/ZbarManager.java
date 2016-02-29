package com.zbar.lib;

/**
 * 描述: zbar调用类
 */
public class ZbarManager {

	static {
		//加载库
		System.loadLibrary("zbar");
	}

	/***
	 * 解码
	 * @param data
	 * @param width
	 * @param height
	 * @param isCrop
	 * @param x
	 * @param y
	 * @param cwidth
	 * @param cheight
	 * @return
	 */
	public native String decode(byte[] data, int width, int height, boolean isCrop, int x, int y, int cwidth, int cheight);
}
