package com.ddmax.zjnucloud.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.base.BaseWebActivity;
import com.ddmax.zjnucloud.model.speech.Speech;

/**
 * @author ddMax
 * @since 2015/12/19 22:50
 * 说明：讲座预告详情Activity，继承自BaseWebActivity
 *      需传入url, Speech对象
 */
public class SpeechDetailActivity extends BaseWebActivity {

    private Speech speechModel;

    /**
     * 调用此方法启动该Activity
     * @param context Context
     * @param url 讲座预告文章详情页的地址
     * @param speechModel Speech对象
     */
    public static void actionStart(Context context, String url, Speech speechModel) {
        Intent intent = new Intent(context, SpeechDetailActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("speechModel", speechModel);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            speechModel = (Speech) getIntent().getSerializableExtra("speechModel");
        } else {
            speechModel = (Speech) savedInstanceState.getSerializable("speechModel");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initFragment() {
        Fragment fragment = getFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("speechModel", speechModel);
    }
}
