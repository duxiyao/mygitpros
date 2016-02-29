package com.kjstudy.frag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.SupportMapFragment;
import com.imbase.R;
import com.kjstudy.bars.BarDefault;
import com.kjstudy.frag.SupportMapFragment1.OnMapCreated;
import com.kjstudy.maputil.MapLocation.LocationListener;

public class SearchFrag extends BFrag {

	private NearByListFrag mNearByListFrag;
	private MapFrag mMapFrag;
	private Fragment mCurFrag;
	private FragmentManager mManager;
	private FragmentTransaction mTransaction;
	private BarDefault mBar;
	private static int mIndex = -1;

	@Override
	protected int getLayoutId() {
		return R.layout.frag_layout_search;
	}

	@Override
	protected void initWidget() {
		super.initWidget();

		mNearByListFrag = new NearByListFrag();
		mMapFrag = new MapFrag();
		if (mIndex == -1 || mIndex == 1)
			showNearByFrag();
		else
			showMapFrag();
		mBar = new BarDefault();
		mBar.setTxt(1, "列表");
		mBar.setTxt(2, "地图");
		mBar.setOnClickLis(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tv1:
					showNearByFrag();
					break;
				case R.id.tv2:
					showMapFrag();
					break;
				default:
					break;
				}

			}
		});
		setCustomBar(mBar.getBarView());
	}

	private void showFragment() {
		if (mManager == null)
			mManager = getChildFragmentManager();
		mTransaction = mManager.beginTransaction();
		if (mCurFrag != null) {
			mTransaction.replace(R.id.search_container, mCurFrag);
		}

		mTransaction.commitAllowingStateLoss();
	}

	public void showNearByFrag() {
		mIndex = 1;
		mCurFrag = mNearByListFrag;
		showFragment();
	}

	public void showMapFrag() {
		mIndex = 2;
		mCurFrag = mMapFrag;
		showFragment();
	}

}
