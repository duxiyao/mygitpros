package com.kjstudy.maputil;

import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.model.LatLng;

public class MapPopWindow {

	private static MapPopWindow mInstance;
	private BaiduMap mBaiduMap;
	private InfoWindow mInfoWindow;
	private boolean isShowing = false;

	private MapPopWindow(BaiduMap baiduMap) {
		mBaiduMap = baiduMap;
	}

	public static MapPopWindow getInstance(BaiduMap baiduMap) {
		if (mInstance == null) {
			synchronized (MapPopWindow.class) {
				if (mInstance == null)
					mInstance = new MapPopWindow(baiduMap);
			}
		}
		return mInstance;
	}

	public void showPopWin(View v, LatLng pt) {
		mBaiduMap.hideInfoWindow();
		mInfoWindow = new InfoWindow(v, pt, -47);
		isShowing = true;
		mBaiduMap.showInfoWindow(mInfoWindow);
	}
}
