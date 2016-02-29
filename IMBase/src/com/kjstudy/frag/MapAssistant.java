package com.kjstudy.frag;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.kjstudy.maputil.MapLocation;
import com.kjstudy.maputil.MapOverlay;
import com.kjstudy.maputil.MapLocation.LocationListener;

public class MapAssistant {

	public static void startLocation(LocationListener lis) {
		MapLocation.getInstance().setLLis(lis).startLocation();
	}

	public static void addOverlay(BaiduMap baiduMap, BDLocation location) {
		MapOverlay mapOverlay = new MapOverlay(baiduMap);
		mapOverlay.locationMe(location);
//		mapOverlay.randomAroundMe(location);
	}
}
