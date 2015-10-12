package com.ddmax.zjnucloud.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ddmax.zjnucloud.model.Tab;
import com.ddmax.zjnucloud.ui.fragment.NewsFragment;

import java.util.List;

/**
 * @author ddMax
 * @since 2015/01/23 23:36.
 * 说明：用于绑定和处理Fragment与ViewPager之间的逻辑关系的适配器
 */
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter{

    private final List<Tab> mTabs;

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);

        mTabs = Tab.getAllTabs();
    }

    @Override
    public Fragment getItem(int position) {
        return NewsFragment.newInstance(mTabs.get(position));
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).getTitle();
    }

}
