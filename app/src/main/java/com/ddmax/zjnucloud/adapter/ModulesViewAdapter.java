package com.ddmax.zjnucloud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddmax.zjnucloud.R;

/**
 * @author ddMax
 * @since 2015/02/19 20:15.
 */
public class ModulesViewAdapter extends BaseAdapter {
	private Context mContext;

	private String[] mModulesTitles = null;

	private Integer[] mModulesImages = {
			R.drawable.module_news, R.drawable.module_calendar,
			R.drawable.module_bus, R.drawable.module_speech,
			R.drawable.module_resources
	};

	public ModulesViewAdapter(Context mContext) {
		this.mContext = mContext;
		mModulesTitles = mContext.getResources().getStringArray(R.array.module_titles);
	}

	@Override
	public int getCount() {
		return mModulesImages.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.module_item, parent, false);
		}

		ImageView mImageView = (ImageView) convertView.findViewById(R.id.module_image);
		TextView mTextView = (TextView) convertView.findViewById(R.id.module_title);

		mImageView.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
		mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		mImageView.setImageResource(mModulesImages[position]);
		mTextView.setText(mModulesTitles[position]);

		return convertView;
	}
}
