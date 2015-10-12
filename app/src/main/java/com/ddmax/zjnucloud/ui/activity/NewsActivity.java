package com.ddmax.zjnucloud.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.adapter.BaseFragmentPagerAdapter;
import com.ddmax.zjnucloud.model.Page;
import com.ddmax.zjnucloud.ui.fragment.NewsFragment;

/**
 * @author ddMax
 * @since 2015/02/19
 * 说明：新闻展示界面
 */

public class NewsActivity extends AppCompatActivity {
    // Toolbar, ActionBar, TabLayout
    private ActionBar mActionBar;
    private Toolbar mToolbar;
    private AppBarLayout mAppBar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter;

    public NewsActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

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

        this.setAppBarShadow(true);
        invalidateOptionsMenu();
    }

    private void setAppBarShadow(boolean isShown) {
        final int elevation = isShown ?
                getResources().getDimensionPixelSize(R.dimen.appbar_elevation) : 0;
        ViewCompat.setElevation(mAppBar, elevation);
    }

    private Fragment getFragmentToShow(Intent intent) {
        return NewsFragment.newInstance((Page)intent.getExtras().getParcelable("page"));
    }

    // 初始化ViewPager
    private void initViewPager() {
        // ViewPager
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        // ViewPager属性设置
        mFragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentPagerAdapter);
    }

    /**
     * 初始化界面
     */
    private void initView() {

        // 初始化Toolbar为ActionBar，设置ActionBar菜单，返回
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mToolbar.setTitle(R.string.title_activity_news);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mAppBar = (AppBarLayout) findViewById(R.id.appbar);

        // 设置TabLayout
        mTabLayout = (TabLayout) findViewById(R.id.top_tabs);
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
