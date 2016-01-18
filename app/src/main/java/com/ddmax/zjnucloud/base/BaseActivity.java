package com.ddmax.zjnucloud.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * @author ddMax
 * @since 2015/12/17 16:08.
 */
public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 友盟session统计
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟session统计
        MobclickAgent.onResume(this);
    }
}
