package com.ddmax.zjnucloud.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.model.bus.Path;
import com.ddmax.zjnucloud.model.bus.Stop;

import java.util.Map;

import cn.sharesdk.framework.ShareSDK;

public class BusActivity extends AppCompatActivity implements BaiduMap.OnMarkerClickListener {
    public static final String TAG = "BusActivity";

    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private Toolbar mToolbar = null;

    private Map<String, Stop> mAllStops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化View
        initView();
        // 定位到浙师大
        initLocation();
        // 添加校车站点覆盖物
        addBusStopMarkers();
        // 添加校车路线折线图
        addBusPolyline();
        // 添加Marker点击事件
        mBaiduMap.setOnMarkerClickListener(this);
    }

    private void initView() {
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_bus);

        findViewById();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_bus);
    }

    private void findViewById() {
        mMapView = (MapView) findViewById(R.id.bus_map);
        mBaiduMap = mMapView.getMap();
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
    }

    private void initLocation() {
        mBaiduMap.setMyLocationEnabled(true);
        // 定位数据
        LatLng loc = new LatLng(Constants.ZJNU_LATITUDE, Constants.ZJNU_LONGITUDE);
        MapStatusUpdate locationInfo = MapStatusUpdateFactory.newLatLng(loc);
        MapStatusUpdate zoomInfo = MapStatusUpdateFactory.zoomTo(16);
        mBaiduMap.animateMapStatus(locationInfo);
        mBaiduMap.animateMapStatus(zoomInfo);
    }

    private void addBusStopMarkers() {
        mAllStops = Stop.ALL_STOPS;
        LatLng point = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        BitmapDescriptor bitmap = null;
        for (int i = 0; i < mAllStops.size(); i++) {
            Stop stop = mAllStops.get(String.valueOf(i));
            //定义Maker坐标点
            point = stop.getPosition();
            //构建Marker图标
            if (i == 0) {
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marker_start);
            } else if (i == mAllStops.size() - 1) {
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marker_end);
            } else {
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marker);
            }
            //构建MarkerOption，用于在地图上添加Marker
            overlayOptions = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            marker = (Marker) mBaiduMap.addOverlay(overlayOptions);
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", stop);
            marker.setExtraInfo(bundle);
        }

    }

    private void addBusPolyline() {
        OverlayOptions polylineStart = new PolylineOptions()
                .width(13).color(0xAA0099ff).points(Path.ALL_START_PATH);
        OverlayOptions polylineReturn = new PolylineOptions()
                .width(13).color(0xAA92C87E).points(Path.ALL_RETURN_PATH);
        mBaiduMap.addOverlay(polylineStart);
        mBaiduMap.addOverlay(polylineReturn);
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_share:
                shareBySystem();
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareBySystem() {
        ShareSDK.initSDK(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // 获得marker中的数据
        Stop stop = (Stop) marker.getExtraInfo().get("info");
        // 建立一个TextView用于显示InfoWindow
        View infoView = getLayoutInflater().inflate(R.layout.map_infowindow, null);
        if (infoView == null) {
            Log.d(TAG, "infoView is null!");
            return false;
        }
        TextView textView = (TextView) infoView.findViewById(R.id.map_info_content);
        textView.setPadding(30, 20, 30, 30);
        textView.setText(stop.getTitle());
        // 将marker所在的经纬度信息转化成屏幕上的坐标
        final LatLng markerPoint = marker.getPosition();
        // 建立InfoWindow，添加点击事件
        InfoWindow infoWindow = new InfoWindow(
                BitmapDescriptorFactory.fromView(infoView),
                markerPoint, -100, new InfoWindow.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick() {
                mBaiduMap.hideInfoWindow();
            }
        });
        // 显示InfoWindow
        mBaiduMap.showInfoWindow(infoWindow);
        // 设置地图点击事件，使点击任意位置能隐藏InfoWindow
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();
                mBaiduMap.setOnMapClickListener(null);
            }
            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        return true;
    }
}
