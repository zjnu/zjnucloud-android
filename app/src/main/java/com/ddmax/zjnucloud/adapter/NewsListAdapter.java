package com.ddmax.zjnucloud.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.model.NewsModel;
import com.ddmax.zjnucloud.ui.activity.NewsDetailActivity;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ddMax
 * @since 2015/02/02 20:35.
 * 说明：新闻列表 RecyclerView.Adapter
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

	private List<NewsModel> mNewsList;

	public NewsListAdapter(List<NewsModel> mNewsList) {
		this.mNewsList = mNewsList == null ? new LinkedList<NewsModel>() : mNewsList;
	}

	// ViewHolder内部类
	public static class ViewHolder extends RecyclerView.ViewHolder {

		public final View mView;
		public final TextView mNewsTitleView, mNewAuthorView, mNewsDateView;

		public ViewHolder(View itemView) {
			super(itemView);
			mView = itemView;
			mNewsTitleView = (TextView) itemView.findViewById(R.id.titleView);
			mNewAuthorView = (TextView) itemView.findViewById(R.id.authorView);
			mNewsDateView = (TextView) itemView.findViewById(R.id.dateView);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.newslist_item, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		final NewsModel newsModel = mNewsList.get(position);
		holder.mNewsTitleView.setText(newsModel.getTitle());
		holder.mNewAuthorView.setText("作者：" + newsModel.getAuthor());
		holder.mNewsDateView.setText("日期：" + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(newsModel.getDate()));

		holder.mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Context context = v.getContext();
				Intent intent = new Intent(context, NewsDetailActivity.class);
				intent.putExtra("id", newsModel.getId());
				intent.putExtra("newsModel", newsModel);

				context.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return mNewsList != null ? mNewsList.size() : 0;
	}

	public void updateData(LinkedList<NewsModel> dataList) {
		this.mNewsList = dataList;
		this.notifyDataSetChanged();
	}

//	@Override
//	public int getItemCount() {
//		return 0;
//	}

//	@Override
//	public Object getItem(int position) {
//		return position >= mNewsList.size() ? null : mNewsList.get(position);
//	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		if (convertView == null) {
//			convertView = LayoutInflater.from(mContext).inflate(R.layout.newslist_item, parent, false);
//		}
//
//		TextView newsTitleView = (TextView) convertView.findViewById(R.id.titleView);
//		TextView newsAuthorView = (TextView) convertView.findViewById(R.id.authorView);
//		TextView newsDateView = (TextView) convertView.findViewById(R.id.dateView);
//
//		final NewsModel mNewsModel = mNewsList.get(position);
//		newsTitleView.setText(mNewsModel.getTitle());
//		newsAuthorView.setText("发布人：" + mNewsModel.getAuthor());
//		newsDateView.setText("发布日期：" + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(mNewsModel.getDate()));
//
//		return convertView;
//	}

	// ViewHolder点击监听事件




}
