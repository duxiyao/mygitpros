package com.kjstudy.frag;

import android.support.v4.app.FragmentManager;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.SupportMapFragment;
import com.imbase.R;
import com.kjstudy.frag.SupportMapFragment1.OnMapCreated;
import com.kjstudy.maputil.MapLocation.LocationListener;

public class MapFrag extends BFrag implements OnMapCreated {

	private SupportMapFragment mMap;
	private BaiduMap mBaiduMap;

	@Override
	protected int getLayoutId() {
		return R.layout.frag_layout_map;
	}

	@Override
	protected void initWidget() {
		super.initWidget();
		FragmentManager fm = getChildFragmentManager();
		mMap = SupportMapFragment1.newInstance(this);
		fm.beginTransaction().replace(R.id.map, mMap).commitAllowingStateLoss();
	}

	@Override
	public void onMapCreated() {

		getActivity().getWindow().getDecorView().postDelayed(new Runnable() {

			@Override
			public void run() {
				mBaiduMap = mMap.getBaiduMap();
				if(mBaiduMap==null)
					return;
				// 普通地图
				mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

				MapAssistant.startLocation(new LocationListener() {

					@Override
					public void onReceiveLocation(BDLocation location) {
						MapAssistant.addOverlay(mBaiduMap, location);
					}
				});
			}
		}, 500);
	}

}
