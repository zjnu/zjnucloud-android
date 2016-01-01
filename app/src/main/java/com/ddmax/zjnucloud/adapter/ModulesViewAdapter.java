package com.ddmax.zjnucloud.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.util.DensityUtils;

/**
 * @author ddMax
 * @since 2015/02/19 20:15.
 * 说明：首页GridView适配器，计算图标大小请看下面的链接
 * @link http://blog.csdn.net/baiyuliang2013/article/details/40181367
 *
 */
public class ModulesViewAdapter extends BaseAdapter {
    private Context mContext;
    // 每个图标的宽度和高度相等
    private int width;

    private String[] mModulesTitles = null;

    private Integer[] mModulesImages = {
            R.drawable.module_news, R.drawable.module_calendar,
            R.drawable.module_bus, R.drawable.module_speech,
            R.drawable.module_resources, R.drawable.module_exam,
            R.drawable.module_coursetable, R.drawable.module_library,
            R.drawable.module_logistics
    };

    public ModulesViewAdapter(Context mContext) {
        this.mContext = mContext;
        mModulesTitles = mContext.getResources().getStringArray(R.array.module_titles);
        // 获得宽度，并计算每个item的宽高
        this.width = DensityUtils.getWidth((Activity) mContext);
        // Item宽度 = (屏幕宽度 - (每行有几个间距 * 间距宽度)) / 每行有几个Item
        width = (width - (5 * DensityUtils.dip2px(mContext, 20))) / 4;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_module, parent, false);
        }

        ImageView mImageView = (ImageView) convertView.findViewById(R.id.module_image);
        TextView mTextView = (TextView) convertView.findViewById(R.id.module_title);

        mImageView.setLayoutParams(new LinearLayout.LayoutParams(width, width));
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setImageResource(mModulesImages[position]);
        mTextView.setText(mModulesTitles[position]);

        return convertView;
    }

}
