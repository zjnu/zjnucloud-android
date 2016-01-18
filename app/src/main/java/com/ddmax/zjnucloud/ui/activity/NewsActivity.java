package com.ddmax.zjnucloud.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.adapter.BaseFragmentPagerAdapter;
import com.ddmax.zjnucloud.base.BaseActivity;
import com.ddmax.zjnucloud.model.news.Page;
import com.ddmax.zjnucloud.ui.fragment.NewsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author ddMax
 * @since 2015/02/19
 * 说明：新闻展示界面
 */

public class NewsActivity extends BaseActivity {

    @Bind(R.id.mToolbar) Toolbar mToolbar;
//    @Bind(R.id.app_bar) AppBarLayout mAppBar;
    @Bind(R.id.top_tabs) TabLayout mTabLayout;
    @Bind(R.id.view_pager) ViewPager mViewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        // 初始化ViewPager
        initViewPager();
        // 初始化界面
        initView();

        // 将Fragments放入List
//        switchFragment(getFragmentToShow(getIntent()), false);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    // 初始化新闻Fragments
    private void switchFragment(Fragment fragment, boolean addToBackStack) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out,
                R.anim.abc_fade_in, R.anim.abc_fade_out)
                .replace(R.id.view_pager, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

//        this.setAppBarShadow(true);
        invalidateOptionsMenu();
    }

//    private void setAppBarShadow(boolean isShown) {
//        final int elevation = isShown ?
//                getResources().getDimensionPixelSize(R.dimen.appbar_elevation) : 0;
//        ViewCompat.setElevation(mAppBar, elevation);
//    }

    private Fragment getFragmentToShow(Intent intent) {
        return NewsFragment.newInstance((Page)intent.getExtras().getParcelable("page"));
    }

    // 初始化ViewPager
    private void initViewPager() {
        // ViewPager属性设置
        FragmentPagerAdapter fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(fragmentPagerAdapter);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        // 初始化Toolbar为ActionBar，设置ActionBar菜单，返回
        mToolbar.setTitle(R.string.title_activity_news);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 设置TabLayout
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        final Fragment fragment = getFragmentToShow(intent);
        switchFragment(fragment, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 填充Toolbar上的菜单项
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
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
