package com.ddmax.zjnucloud.helpers.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author ddMax
 * @since  2015/01/23 23:36.
 * 说明：用于绑定和处理Fragment与ViewPager之间的逻辑关系的适配器
 */
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

	private List<Fragment> fragments; // Fragment总数
	private ViewPager mViewPager;
	private List<String> mTabTitles; // Tab标题

	public BaseFragmentPagerAdapter(FragmentManager fm, ViewPager mViewPager, List<Fragment> fragments, List<String> mTabTitles) {
		super(fm);
		this.fragments = fragments;
		this.mTabTitles = mTabTitles;
		this.mViewPager = mViewPager;
		this.mViewPager.setAdapter(this);
		this.mViewPager.setOnPageChangeListener(this);
		this.mViewPager.setOffscreenPageLimit(getCount());
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTabTitles.get(position);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		if (mViewPager != null) {
			mViewPager.invalidate();
		}
	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

}
