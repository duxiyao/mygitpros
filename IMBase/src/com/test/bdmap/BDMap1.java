package com.test.bdmap;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import android.graphics.Color;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine.TransitStep;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.imbase.R;

public class BDMap1 extends KJActivity {

	@BindView(id = R.id.bmapView)
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	public LocationClient mLocationClient = null;

	@Override
	public void setRootView() {
		
		setContentView(R.layout.layout_bdmap_test);
	}

	@Override
	public void initWidget() {
		super.initWidget();

		mBaiduMap = mMapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// mBaiduMap.setTrafficEnabled(true);
		// 开启交通图
		// mBaiduMap.setBaiduHeatMapEnabled(true);

		mMapView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// geoCoder();
				line();
			}
		}, 5000);

	}

	void linecolor() {
		// 构造折线点坐标
		List<LatLng> points = new ArrayList<LatLng>();
		points.add(new LatLng(39.965, 116.404));
		points.add(new LatLng(39.925, 116.454));
		points.add(new LatLng(39.955, 116.494));
		points.add(new LatLng(39.905, 116.554));
		points.add(new LatLng(39.965, 116.604));

		// 构建分段颜色索引数组
		List<Integer> colors = new ArrayList<Integer>();
		colors.add(Integer.valueOf(Color.BLUE));
		colors.add(Integer.valueOf(Color.RED));
		colors.add(Integer.valueOf(Color.YELLOW));
		colors.add(Integer.valueOf(Color.GREEN));

		OverlayOptions ooPolyline = new PolylineOptions().width(10)
				.points(points).color(Color.RED);
		// 添加在地图中
		Polyline mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);

	}

	void geoCoder() {
		GeoCoder mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(liss);
		mSearch.geocode(new GeoCodeOption().city("北京").address("海淀区上地十街10号"));
	}

	OnGetGeoCoderResultListener liss = new OnGetGeoCoderResultListener() {
		public void onGetGeoCodeResult(GeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				// 没有检索到结果
			}
			// 获取地理编码结果
		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				// 没有找到检索结果
			}
			// 获取反向地理编码结果
		}
	};

	void line() {
		RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(listener);
		PlanNode stNode = PlanNode.withLocation(new LatLng(39.915291,
				116.403857));// withCityNameAndPlaceName("北京", "龙泽地铁");
		PlanNode enNode = PlanNode.withLocation(new LatLng(40.056858,
				116.308194));// PlanNode.withCityNameAndPlaceName("北京",
								// "西二旗地铁");

		TransitRoutePlanOption p = new TransitRoutePlanOption();
		p.from(stNode).to(enNode);
		p.city("北京");
		mSearch.transitSearch(p);
		// mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(
		// enNode));
		// mSearch.destroy();
	}

	OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
		public void onGetWalkingRouteResult(WalkingRouteResult result) {
			//
		}

		public void onGetTransitRouteResult(TransitRouteResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				ViewInject.toast("抱歉，未找到结果");
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				// result.getSuggestAddrInfo()
				return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				// TransitRouteOverlay overlay = new
				// TransitRouteOverlay(mBaiduMap);
				// mBaiduMap.setOnMarkerClickLi stener(overlay);
				// overlay.setData(result.getRouteLines().get(0));
				// overlay.addToMap();
				// overlay.zoomToSpan();
				List<TransitRouteLine> ltr = result.getRouteLines();
				TransitRouteLine tr = ltr.get(0);
				List<LatLng> datas = new ArrayList<LatLng>();
				int len = tr.getAllStep().size();
				List<TransitStep> ds = tr.getAllStep();
				for (int i = 0; i < len; i++) {
					for (LatLng ll : ds.get(i).getWayPoints())
						datas.add(ll);
				}
				OverlayOptions ooPolyline = new PolylineOptions().width(10)
						.points(datas).color(Color.RED);
				// 添加在地图中
				Polyline mPolyline = (Polyline) mBaiduMap
						.addOverlay(ooPolyline);
				ViewInject.toast(String.valueOf(mBaiduMap
						.getMapStatus().zoom));
				MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(mBaiduMap
						.getMapStatus().zoom);
				mBaiduMap.animateMapStatus(u);
				
				// 开启定位图层
				mBaiduMap.setMyLocationEnabled(true);
				// 构造定位数据
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(40)
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(40.056858)
						.longitude(116.308194).build();
				// 设置定位数据
				mBaiduMap.setMyLocationData(locData);

				// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
				BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
						.fromResource(R.drawable.ic_launcher);
				MyLocationConfiguration config = new MyLocationConfiguration(
						com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.FOLLOWING,
						true, null);
				mBaiduMap.setMyLocationConfigeration(config);
//				 当不需要定位图层时关闭定位图层
				 mBaiduMap.setMyLocationEnabled(false);
			}
		}

		public void onGetDrivingRouteResult(DrivingRouteResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				ViewInject.toast("抱歉，未找到结果");
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				// result.getSuggestAddrInfo()
				return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//				DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
//				mBaiduMap.setOnMarkerClickListener(overlay);
//				overlay.setData(result.getRouteLines().get(0));
//				overlay.addToMap();
//				overlay.zoomToSpan();
			}
		}
	};

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

	private void addOverlay(double latitude, double longitude,
			BDLocation location) {
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		ViewInject.toast(String.valueOf(location.getRadius()));
		// 构造定位数据
		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(100).latitude(35.196443841152)
				.longitude(113.79197640538).build();
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
		// mBaiduMap.setMyLocationEnabled(false);

		// 定义Maker坐标点
		LatLng point = new LatLng(35.196443841152, 113.79197640538);
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.ic_launcher);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap);
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option);
	}
}
