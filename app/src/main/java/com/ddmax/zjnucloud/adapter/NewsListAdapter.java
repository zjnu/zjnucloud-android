package com.ddmax.zjnucloud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.model.NewsModel;

import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * @author ddMax
 * @since 2015/02/02 20:35.
 * 说明：新闻列表 Adapter
 */
public class NewsListAdapter extends BaseAdapter {

	private Context mContext;
	private LinkedList<NewsModel> newsList;

	public NewsListAdapter(Context mContext, LinkedList<NewsModel> newsList) {
		this.mContext = mContext;
		this.newsList = newsList == null ? new LinkedList<NewsModel>() : new LinkedList<>(newsList);
	}

	@Override
	public int getCount() {
		return newsList != null ? newsList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return position >= newsList.size() ? null : newsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.newslist_item, parent, false);
		}

		TextView newsTitleView = (TextView) convertView.findViewById(R.id.titleView);
		TextView newsAuthorView = (TextView) convertView.findViewById(R.id.authorView);
		TextView newsDateView = (TextView) convertView.findViewById(R.id.dateView);

		final NewsModel mNewsModel = newsList.get(position);
		newsTitleView.setText(mNewsModel.getTitle());
		newsAuthorView.setText("发布人：" + mNewsModel.getAuthor());
		newsDateView.setText("发布日期：" + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(mNewsModel.getDate()));

		return convertView;
	}

	public void updateData(LinkedList<NewsModel> dataList) {
		this.newsList = dataList;
		this.notifyDataSetChanged();
	}
}
