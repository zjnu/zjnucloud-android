package com.ddmax.zjnucloud.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.model.Page;
import com.ddmax.zjnucloud.model.news.News;
import com.ddmax.zjnucloud.ui.activity.NewsDetailActivity;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author ddMax
 * @since 2015/02/02 20:35.
 * 说明：新闻列表 RecyclerView.Adapter
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private List<News> mNewsList;
    private Page mPage;

    /**
     * @param mNewsList 新闻列表
     * @param mPage 是否是数理信息新闻
     */
    public NewsListAdapter(List<News> mNewsList, Page mPage) {
        this.mNewsList = mNewsList == null ? new LinkedList<News>() : mNewsList;
        this.mPage = mPage;
    }

    // ViewHolder内部类
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        @Bind(R.id.titleView) TextView mNewsTitleView;
        @Bind(R.id.authorView) TextView mNewAuthorView;
        @Bind(R.id.dateView) TextView mNewsDateView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, mView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_newslist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final News newsModel = mNewsList.get(position);
        holder.mNewsTitleView.setText(newsModel.getTitle());
        holder.mNewAuthorView.setText("作者：" + newsModel.getAuthor());
        if (!mPage.getTitle().startsWith("数理信息")) {
            holder.mNewsDateView.setText("日期：" + new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINESE).format(newsModel.getDate()));
        } else {
            holder.mNewsDateView.setText("日期：" + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.CHINESE).format(newsModel.getDate()));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("id", newsModel.getArticleId());
                intent.putExtra("isSlxx", mPage.getTitle().startsWith("数理信息"));
                intent.putExtra("url", mPage.getUrl());
                intent.putExtra("newsModel", newsModel);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsList != null ? mNewsList.size() : 0;
    }

    public void updateData(LinkedList<News> dataList) {
        this.mNewsList = dataList;
        this.notifyDataSetChanged();
    }

}
