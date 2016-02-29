package com.kjstudy.act;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import android.view.Window;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.imbase.R;
import com.kjstudy.communication.SDKHelper;
import com.kjstudy.frag.MapAssistant;
import com.kjstudy.maputil.MapLocation.LocationListener;

public class MapAct extends KJActivity {

    @BindView(id = R.id.bmapView)
    private MapView mMapView;

    private BaiduMap mBaiduMap;

    @Override
    public void setRootView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_map);
    }

    @Override
    public void initWidget() {
        super.initWidget();

        mBaiduMap = mMapView.getMap();
        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
