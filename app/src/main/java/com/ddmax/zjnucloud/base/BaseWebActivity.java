package com.ddmax.zjnucloud.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.ui.view.MarqueeToolbar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author ddMax
 * @since 2015/12/19 20:06
 * 说明：基本WebView Activity，
 * 传入toolbarTitle, url
 */
public class BaseWebActivity extends BaseActivity {

    public static final String TAG = BaseWebActivity.class.getSimpleName();

    public static Fragment mFragment;

    @Bind(R.id.toolbar) MarqueeToolbar mToolbar;
    private String url;
    private boolean isPadding;

    /**
     * 使用此方法启动BaseWebActivity
     * @param context Context
     * @param toolbarTitle Toolbar标题字符串
     * @param url 链接地址
     */
    public static void actionStart(Context context, String toolbarTitle, String url, boolean isPadding) {
        Intent intent = new Intent(context, BaseWebActivity.class);
        intent.putExtra("toolbarTitle", toolbarTitle);
        intent.putExtra("url", url);
        intent.putExtra("isPadding", isPadding);
        context.startActivity(intent);
    }

    /**
     * 带BaseWebFragment启动Activity
     * @param context Context
     * @param toolbarTitle Toolbar标题字符串
     * @param fragment ? extends BaseWebFragment
     */
    public static void actionStart(Context context, String toolbarTitle, Fragment fragment) {
        Intent intent = new Intent(context, BaseWebActivity.class);
        intent.putExtra("toolbarTitle", toolbarTitle);
        mFragment = fragment;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        // 设置Toolbar
        setupToolbar(getIntent().getStringExtra("toolbarTitle"));

        if (savedInstanceState == null) {
            url = getIntent().getStringExtra("url");
            isPadding = getIntent().getBooleanExtra("isPadding", true);
        } else {
            url = savedInstanceState.getString("url");
            isPadding = savedInstanceState.getBoolean("isPadding", true);
        }

        // 初始化Fragment
        initFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("url", url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFragment = null;
    }

    protected void setupToolbar(String title) {
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_close_white_24dp));
    }

    protected Fragment getFragment() {
        if (mFragment != null) {
            return mFragment;
        }
        return BaseWebFragment.newInstance(url, false);
    }

    protected void initFragment() {
        Fragment fragment = getFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
