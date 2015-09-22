package com.ddmax.zjnucloud.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;

public class BusActivity extends ActionBarActivity {

	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private Toolbar mToolbar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_bus);

		// Views injection
		findViewById();

		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.title_activity_bus);

		// 定位到浙师大
		initLocation();

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

	private void findViewById() {
		mMapView = (MapView) findViewById(R.id.bus_map);
		mBaiduMap = mMapView.getMap();
		mToolbar = (Toolbar) findViewById(R.id.mToolbar);
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
			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}

}
