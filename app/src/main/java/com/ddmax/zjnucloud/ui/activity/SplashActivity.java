package com.ddmax.zjnucloud.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ddmax.zjnucloud.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Will on 15-9-18.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String TAG = "SplashActivity";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Log.i(TAG, "onCreate");

        final Intent it = new Intent(this, MainActivity.class); //需要转向的Activity
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it); //执行
                finish();
            }
        };
        timer.schedule(task, 1000 * 2); //2秒后

    }
}


