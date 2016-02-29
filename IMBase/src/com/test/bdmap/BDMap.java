package com.test.bdmap;

import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.KJLoger;

import android.content.Intent;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.imbase.R;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class BDMap extends KJActivity {

	@BindView(id = R.id.bmapView)
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	@Override
	public void setRootView() {
		setContentView(R.layout.layout_bdmap_test);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		initLocation();
		mLocationClient.start();

		// Tencent tencent = Tencent.createInstance("1104917096",
		// this.getApplicationContext());
		//
		// if (!tencent.isSessionValid()) {
		// tencent.login(this, "all", lis);
		// }

		// UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1104917096",
		// "cTbIQKW7HxmZlSne");
		// qqSsoHandler.addToSocialSDK();

		// final UMSocialService mController = UMServiceFactory
		// .getUMSocialService("com.umeng.login");

		// mController.doOauthVerify(this, SHARE_MEDIA.QQ, new UMAuthListener()
		// {
		//
		// @Override
		// public void onCancel(SHARE_MEDIA arg0) {
		// }
		//
		// @Override
		// public void onComplete(Bundle value, SHARE_MEDIA platform) {
		//
		// if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
		// ViewInject.toast("授权成功.");
		// getData(mController);
		// } else {
		// ViewInject.toast("授权失败");
		// }
		// }
		//
		// @Override
		// public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
		// }
		//
		// @Override
		// public void onStart(SHARE_MEDIA arg0) {
		// }
		// });

		mBaiduMap = mMapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// mBaiduMap.setTrafficEnabled(true);
		// 开启交通图
		mBaiduMap.setBaiduHeatMapEnabled(true);
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

	// private void deleteData(UMSocialService controller ){
	// controller.deleteOauth(BDMap.this, SHARE_MEDIA.QQ,
	// new SocializeClientListener() {
	//
	// @Override
	// public void onStart() {
	// }
	//
	// @Override
	// public void onComplete(int status, SocializeEntity arg1) {
	// if (status == 200) {
	// ViewInject.toast("删除成功.");
	// } else {
	// ViewInject.toast("删除失败");
	// }
	// }
	// });
	// }

	// private void getData(final UMSocialService controller ){
	// controller.getPlatformInfo(BDMap.this, SHARE_MEDIA.QQ,
	// new UMDataListener() {
	//
	// @Override
	// public void onStart() {
	// ViewInject.toast("获取平台数据开始...");
	// }
	//
	// @Override
	// public void onComplete(int status, Map<String, Object> info) {
	// if (status == 200 && info != null) {
	// StringBuilder sb = new StringBuilder();
	// Set<String> keys = info.keySet();
	// for (String key : keys) {
	// sb.append(key + "=" + info.get(key).toString()
	// + "\r\n");
	// }
	// KJLoger.debug("TestData-->" + sb.toString());
	// deleteData(controller);
	// } else {
	// KJLoger.debug("TestData-->" + "发生错误：" + status);
	// }
	// }
	// });
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		// Tencent.handleResultData(arg0, arg1);onActivityResultData(arg0, arg1,
		// arg2, lis);
	}

	IUiListener lis = new IUiListener() {

		@Override
		public void onError(UiError arg0) {
			String str = "";
		}

		@Override
		public void onComplete(Object arg0) {
			String str = "";
		}

		@Override
		public void onCancel() {
			String str = "";
		}
	};

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 单位：公里每小时
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// 单位：米
				sb.append("\ndirection : ");
				sb.append(location.getDirection());// 单位度
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps定位成功");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
				sb.append("\ndescribe : ");
				sb.append("网络定位成功");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				sb.append("\ndescribe : ");
				sb.append("离线定位成功，离线定位结果也是有效的");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				sb.append("\ndescribe : ");
				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				sb.append("\ndescribe : ");
				sb.append("网络不同导致定位失败，请检查网络是否通畅");
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				sb.append("\ndescribe : ");
				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
			}
			sb.append("\nlocationdescribe : ");
			sb.append(location.getLocationDescribe());// 位置语义化信息
			List<Poi> list = location.getPoiList();// POI数据
			if (list != null) {
				sb.append("\npoilist size = : ");
				sb.append(list.size());
				for (Poi p : list) {
					sb.append("\npoi= : ");
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}
			}
			KJLoger.debug("BaiduLocationApiDem" + sb.toString());
		}
	}
}
