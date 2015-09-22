package com.ddmax.zjnucloud;

import android.app.Application;

import com.ddmax.zjnucloud.db.NewsDataSource;

/**
 * @author ddMax
 * @since 2015/03/05 19:34.
 * 说明：APP全局辅助类
 */
public class ZJNUApplication extends Application {

	private static ZJNUApplication mApplication;
	private static NewsDataSource mNewsDataSource;

	@Override
	public void onCreate() {
		super.onCreate();

		mApplication = this;
		mNewsDataSource = new NewsDataSource(getApplicationContext());

	}

	public static ZJNUApplication getInstance() {
		return mApplication;
	}

	public static NewsDataSource getDataSource() {
		return mNewsDataSource;
	}
}
