package com.ddmax.zjnucloud;

import android.app.Application;
import android.os.Build;

import com.ddmax.zjnucloud.db.NewsDataSource;
import com.ddmax.zjnucloud.ui.activity.MainActivity;

/**
 * @author ddMax
 * @since 2015/03/05 19:34.
 * 说明：APP全局辅助类
 */
public class ZJNUApplication extends Application {

    private static final String TAG = "ZJNUApplication";

    private static ZJNUApplication mApplication;
    private static NewsDataSource mNewsDataSource;
    // Android SDK Version Code
    private int sdkVersion = Build.VERSION.SDK_INT;

    private MainActivity.LoginHandler loginHandler;

    public MainActivity.LoginHandler getLoginHandler() {
        return loginHandler;
    }

    public void setLoginHandler(MainActivity.LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;
        mNewsDataSource = new NewsDataSource(getApplicationContext());

    }

    public static ZJNUApplication getInstance() {
        if (mApplication == null) {
            mApplication = new ZJNUApplication();
        }
        return mApplication;
    }

    public static NewsDataSource getDataSource() {
        return mNewsDataSource;
    }

    public int getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(int sdkVersion) {
        this.sdkVersion = sdkVersion;
    }
}
