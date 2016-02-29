package com.kjstudy.maputil;

import org.kymjs.kjframe.ui.ViewInject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.imbase.MyApplication;
import com.imbase.R;

public class MapOverlay {

	private BaiduMap mBaiduMap;
	private Context mContext;

	public MapOverlay(BaiduMap baiduMap) {
		mContext = MyApplication.getInstance().getApplicationContext();
		mBaiduMap = baiduMap;
		mBaiduMap.setOnMarkerClickListener(listener);
	}

	public void locationMe(BDLocation location) {

		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 构造定位数据
		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(location.getRadius()).latitude(latitude)
				.longitude(longitude).build();
		// 设置定位数据
		mBaiduMap.setMyLocationData(locData);

		// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
		BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.ic_launcher);
		MyLocationConfiguration config = new MyLocationConfiguration(
				com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.FOLLOWING,
				true, null);
		mBaiduMap.setMyLocationConfigeration(config);
		// 当不需要定位图层时关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		addOverlay(latitude, longitude, -1, true, MarkerAnimateType.grow);
	}

	/**
	 * @date 2015年10月22日
	 * @author duxiyao
	 * @description 描述
	 * @param latitude
	 * @param longitude
	 * @param icon
	 *            -1默认图标
	 * @param hasAnim
	 *            true 有动画
	 * @return
	 */
	public Marker addOverlay(double latitude, double longitude, int icon,
			boolean hasAnim, MarkerAnimateType animType) {
		// 定义Maker坐标点
		LatLng point = new LatLng(latitude, longitude);

		// 构建Marker图标
		BitmapDescriptor bitmap = null;
		if (icon != -1)
			bitmap = BitmapDescriptorFactory.fromResource(icon);
		else
			bitmap = BitmapDescriptorFactory.fromResource(R.drawable.i1);

		if (hasAnim) {
			MarkerOptions ooA = new MarkerOptions().position(point)
					.icon(bitmap).zIndex(9);
			ooA.animateType(animType);
			Marker marker = (Marker) mBaiduMap.addOverlay(ooA);
			return marker;
		} else {
			// 构建MarkerOption，用于在地图上添加Marker
			OverlayOptions option = new MarkerOptions().position(point).icon(
					bitmap);
			// 在地图上添加Marker，并显示
			Marker marker = (Marker) mBaiduMap.addOverlay(option);
			return marker;
		}
	}

	public void randomAroundMe(BDLocation location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();

		for (int i = 0; i < 5; i++) {
			latitude += 0.01 * i;
			longitude += 0.01 * i;
			Marker marker = addOverlay(latitude, longitude, R.drawable.i2,
					true, MarkerAnimateType.drop);
			marker.setTitle("I am teacher");
		}

		for (int i = 0; i < 5; i++) {
			latitude += 0.01 * i;
			Marker marker = addOverlay(latitude, longitude, R.drawable.i3,
					true, MarkerAnimateType.drop);
			marker.setTitle("I am student");
		}
	}

	OnMarkerClickListener listener = new OnMarkerClickListener() {
		/**
		 * 地图 Marker 覆盖物点击事件监听函数
		 * 
		 * @param marker
		 *            被点击的 marker
		 */
		public boolean onMarkerClick(Marker marker) {
			ViewInject.toast(marker.getTitle());
			View v = LayoutInflater.from(mContext).inflate(
					R.layout.map_pop_win_stu, null);
			MapPopWindow.getInstance(mBaiduMap).showPopWin(v,
					marker.getPosition());
			return true;
		}
	};
}
