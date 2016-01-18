package com.ddmax.zjnucloud;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.activeandroid.ActiveAndroid;
import com.ddmax.zjnucloud.db.NewsDbHelper;
import com.ddmax.zjnucloud.ui.activity.MainActivity;

/**
 * @author ddMax
 * @since 2015/03/05 19:34.
 * 说明：APP全局辅助类
 */
public class ZJNUApplication extends Application {

    private static final String TAG = "ZJNUApplication";

    private static ZJNUApplication mApplication;
    private static NewsDbHelper mNewsDbHelper;
    // Android SDK Version Code
    private int sdkVersion = Build.VERSION.SDK_INT;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

        mApplication = this;
        mNewsDbHelper = new NewsDbHelper(getApplicationContext());

    }

    private MainActivity.LoginHandler loginHandler;

    public MainActivity.LoginHandler getLoginHandler() {
        return loginHandler;
    }

    public void setLoginHandler(MainActivity.LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }


    public static ZJNUApplication getInstance() {
        if (mApplication == null) {
            mApplication = new ZJNUApplication();
        }
        return mApplication;
    }

    public static NewsDbHelper getNewsDbHelper() {
        return mNewsDbHelper;
    }

    public int getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(int sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
