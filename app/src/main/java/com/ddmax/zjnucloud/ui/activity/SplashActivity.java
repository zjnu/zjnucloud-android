package com.ddmax.zjnucloud.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Will on 15-9-18.
 */
public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Intent intent = new Intent(this, MainActivity.class); //需要转向的Activity
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(intent); //执行
                finish();
            }
        };
        timer.schedule(task, 1000); //1秒后
    }
}


