package com.ddmax.zjnucloud.helpers.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author ddMax
 * @since 2015/02/23 13:27.
 * 说明：欢迎界面和图片展览的ViewPager Adapter
 */
public class BasePagerAdapter extends PagerAdapter{

	private Context mContext;
	private List<? extends View> mViews;

	public BasePagerAdapter(Context mContext, List<? extends View> mViews) {
		this.mContext = mContext;
		this.mViews = mViews;
	}

	@Override
	public int getCount() {
		return mViews.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mViews.get(position));
		return mViews.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mViews.get(position));
	}
}
