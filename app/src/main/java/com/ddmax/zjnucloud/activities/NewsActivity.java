package com.ddmax.zjnucloud.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.helpers.adapters.BaseFragmentPagerAdapter;
import com.ddmax.zjnucloud.ui.custom.SlidingTabLayout;
import com.ddmax.zjnucloud.ui.fragments.NewsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author ddMax
 * @since 2015/02/19
 * 说明：新闻展示界面
 */
public class NewsActivity extends AppCompatActivity {

	// Toolbar, ActionBar, SlidingTabLayout
	private ActionBar mActionBar;
	private Toolbar mToolbar;
	private SlidingTabLayout mSlidingTabLayout;
	// Fragments，及对应的新闻地址
	private List<Fragment> mFragments;
	private HashMap<String, String> mNewsInfo;
	private int mCurrentFragment = 0;
	// 中间内容
	private RelativeLayout mViewPagerContainer;
	private ViewPager mViewPager;
	private BaseFragmentPagerAdapter mFragmentPagerAdapter;
	// 具体内容，用于fragment之间切换
	private Fragment contentFragment;
	// 管理fragment
	private FragmentManager fragmentManager;

	public NewsActivity() {}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);

		// 初始化NewsURLs
		initNewsURLs();
		// 将Fragments放入List
		initFragments();
		// 初始化ViewPager
		initViewPager();
		// 初始化界面
		initUi();
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	private void initNewsURLs() {
		mNewsInfo = new HashMap<>();
		//TODO: Fetch from server
		mNewsInfo.put("信息通告", Constants.URL.NEWS_NOTIFICATION);
		mNewsInfo.put("学工新闻", Constants.URL.NEWS_LIVES);
	}

	private void initFragments() {
		mFragments = new ArrayList<>();
		Set set = mNewsInfo.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Fragment newsFragment = new NewsFragment();
			// 设置参数
			Bundle bundle = new Bundle();
			String key = it.next().toString();
			bundle.putString("URL", mNewsInfo.get(key));
			newsFragment.setArguments(bundle);
			// 添加Fragment
			mFragments.add(newsFragment);
		}
	}

	private void initViewPager() {
		// 中间内容
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mViewPagerContainer = (RelativeLayout) findViewById(R.id.news_container);
		// 得到TabTitles
		Set set = mNewsInfo.keySet();
		Iterator it = set.iterator();
		List<String> mTabTitles = new ArrayList<>();
		while (it.hasNext()) {
			mTabTitles.add(it.next().toString());
		}
		// ViewPager属性设置
		mFragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), mViewPager, mFragments, mTabTitles);
		mViewPager.setOnPageChangeListener(mPageChangeListener);
		mViewPagerContainer.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mViewPager.dispatchTouchEvent(event);
			}
		});
	}

	/**
	 * 初始化界面
	 */
	private void initUi() {

		// 初始化Toolbar为ActionBar，设置ActionBar菜单，返回
		mToolbar = (Toolbar) findViewById(R.id.mToolbar);
		mToolbar.setTitle(R.string.title_activity_news);
		setSupportActionBar(mToolbar);
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);

		// 设置SlidingTabLayout
		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.top_tabs);
		mSlidingTabLayout.setDistributeEvenly(true);
		mSlidingTabLayout.setViewPager(mViewPager);

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

	/**
	 * ViewPager滑动监听事件
	 */
	private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			if (mViewPager != null) {
				mViewPager.invalidate();
			}
		}

		@Override
		public void onPageSelected(int position) {
			mCurrentFragment = position;
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	};

}
