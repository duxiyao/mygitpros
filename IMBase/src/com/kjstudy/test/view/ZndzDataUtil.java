package com.kjstudy.test.view;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

import com.imbase.R;

public class ZndzDataUtil {

//	static {
//		getManFront();
//		getManBack();
//		getWoManFront();
//		getWoManBack();
//	}
	
	public static void test(){
		
	}

//	private static List<MapInfo> mManFront, mManBack, mWoManFront, mWoManBack;

	public static List<MapInfo> getManFront() {
//		if (mManFront == null) {
			List<MapInfo> datas = new ArrayList<MapInfo>();
			MapInfo mi = new MapInfo();
			mi.setName("bg");
			mi.setResIdDef(R.drawable.man_front);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("head");
			mi.setResIdDef(R.drawable.man_head);
			mi.setResIdPress(R.drawable.man_head_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("neck");
			mi.setResIdDef(R.drawable.man_neck);
			mi.setResIdPress(R.drawable.man_neck_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("chest");
			mi.setResIdDef(R.drawable.man_chest);
			mi.setResIdPress(R.drawable.man_chest_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("arm");
			mi.setResIdDef(R.drawable.man_arm);
			mi.setResIdPress(R.drawable.man_arm_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("hand");
			mi.setResIdDef(R.drawable.man_hand);
			mi.setResIdPress(R.drawable.man_hand_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("abdomen");
			mi.setResIdDef(R.drawable.man_abdomen);
			mi.setResIdPress(R.drawable.man_abdomen_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("genitals");
			mi.setResIdDef(R.drawable.man_genitals);
			mi.setResIdPress(R.drawable.man_genitals_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("leg");
			mi.setResIdDef(R.drawable.man_leg);
			mi.setResIdPress(R.drawable.man_leg_click);
			mi.setClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				}
			});
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("foot");
			mi.setResIdDef(R.drawable.man_foot);
			mi.setResIdPress(R.drawable.man_foot_click);
			datas.add(mi);
			return datas;
//			mManFront = datas;
//		}
//		return mManFront;
	}

	public static List<MapInfo> getManBack() {
//		if (mManBack == null) {
			List<MapInfo> datas = new ArrayList<MapInfo>();
			MapInfo mi = new MapInfo();
			mi.setName("bg");
			mi.setResIdDef(R.drawable.man_back);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("backside");
			mi.setResIdDef(R.drawable.man_backside);
			mi.setResIdPress(R.drawable.man_backside_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("hips");
			mi.setResIdDef(R.drawable.man_hips);
			mi.setResIdPress(R.drawable.man_hips_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("waist");
			mi.setResIdDef(R.drawable.man_waist);
			mi.setResIdPress(R.drawable.man_waist_click);
			datas.add(mi);
			return datas;
//			mManBack = datas;
//		}
//		return mManBack;
	}

	public static List<MapInfo> getWoManFront() {
//		if (mWoManFront == null) {
			List<MapInfo> datas = new ArrayList<MapInfo>();
			MapInfo mi = new MapInfo();
			mi.setName("bg");
			mi.setResIdDef(R.drawable.woman_front);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("head");
			mi.setResIdDef(R.drawable.woman_head);
			mi.setResIdPress(R.drawable.woman_head_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("neck");
			mi.setResIdDef(R.drawable.woman_neck);
			mi.setResIdPress(R.drawable.woman_neck_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("chest");
			mi.setResIdDef(R.drawable.woman_chest);
			mi.setResIdPress(R.drawable.woman_chest_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("arm");
			mi.setResIdDef(R.drawable.woman_arm);
			mi.setResIdPress(R.drawable.woman_arm_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("hand");
			mi.setResIdDef(R.drawable.woman_hand);
			mi.setResIdPress(R.drawable.woman_hand_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("abdomen");
			mi.setResIdDef(R.drawable.woman__abdomen);
			mi.setResIdPress(R.drawable.woman_abdomen_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("genitals");
			mi.setResIdDef(R.drawable.woman_genitals);
			mi.setResIdPress(R.drawable.woman_genitals_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("leg");
			mi.setResIdDef(R.drawable.woman_leg);
			mi.setResIdPress(R.drawable.woman_leg_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("foot");
			mi.setResIdDef(R.drawable.woman_foot);
			mi.setResIdPress(R.drawable.woman_foot_click);
			datas.add(mi);
			return datas;
//			mWoManFront = datas;
//		}
//		return mWoManFront;
	}

	public static List<MapInfo> getWoManBack() {
//		if (mWoManBack == null) {
			List<MapInfo> datas = new ArrayList<MapInfo>();
			MapInfo mi = new MapInfo();
			mi.setName("bg");
			mi.setResIdDef(R.drawable.woman_back);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("backside");
			mi.setResIdDef(R.drawable.woman_backside);
			mi.setResIdPress(R.drawable.woman_backside_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("hips");
			mi.setResIdDef(R.drawable.woman_hips);
			mi.setResIdPress(R.drawable.woman_hips_click);
			datas.add(mi);

			mi = new MapInfo();
			mi.setName("waist");
			mi.setResIdDef(R.drawable.woman_waist);
			mi.setResIdPress(R.drawable.woman_waist_click);
			datas.add(mi);
			return datas;
//			mWoManBack = datas;
//		}
//		return mWoManBack;
	}
}
