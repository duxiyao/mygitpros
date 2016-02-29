package com.kjstudy.frag;

import android.os.Bundle;

import com.baidu.mapapi.map.SupportMapFragment;

public class SupportMapFragment1 extends SupportMapFragment {

	public interface OnMapCreated {
		void onMapCreated();
	}

	private OnMapCreated mLis;

	public static SupportMapFragment1 newInstance(OnMapCreated lis) {
		SupportMapFragment1 smf = new SupportMapFragment1();
		smf.mLis = lis;
		return smf;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		if (mLis != null)
			mLis.onMapCreated();
	}
}
