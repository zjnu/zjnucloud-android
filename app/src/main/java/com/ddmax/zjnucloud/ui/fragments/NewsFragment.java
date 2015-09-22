package com.ddmax.zjnucloud.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.activities.NewsDetailActivity;
import com.ddmax.zjnucloud.helpers.adapters.NewsListAdapter;
import com.ddmax.zjnucloud.model.NewsModel;
import com.ddmax.zjnucloud.task.GetNewsTask;
import com.ddmax.zjnucloud.task.ResponseListener;
import com.ddmax.zjnucloud.ui.custom.SwipeRefreshLayout;

import java.util.LinkedList;

/**
 * @author ddMax
 * @since 2014/12/31
 * 说明：新闻列表Fragment
 */
public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener, ResponseListener<LinkedList<NewsModel>> {
	private final static int REFRESH_DOWN = 0;
	private final static int REFRESH_UP = 1;

	// 记录当前新闻加载的次数
	private static int mListCount = 1;

	// 进度条
	private RelativeLayout mLoadingProgress;
	private ListView mList;
	private SwipeRefreshLayout mRefreshLayout;

	// 新闻列表
	private LinkedList<NewsModel> mNewsList = null;
	private NewsListAdapter mAdapter = null;

	private Handler refreshHandler = new Handler() {
		public void handleMessage(Message msg) {
		switch (msg.what) {
			case REFRESH_DOWN:
				if (mNewsList != null) {
					loadNewsList(REFRESH_DOWN);
				}
				break;
			case REFRESH_UP:
				loadNewsList(REFRESH_UP);
				break;
			default:
				break;
		}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO: 新闻列表缓存

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// 填充Fragment布局
		ViewGroup mViewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_news, container, false);
		// 设置进度条
		mLoadingProgress = (RelativeLayout) mViewGroup.findViewById(R.id.progressView);
		// 设置新闻
		mList = (ListView) mViewGroup.findViewById(R.id.newsList);
		// 设置新闻列表点击监听事件
		mList.setOnItemClickListener(this);

		// 初始化SwipeRefreshLayout，设置下拉主题
		mRefreshLayout = (SwipeRefreshLayout) mViewGroup.findViewById(R.id.swipeRefreshLayout);
		mRefreshLayout.setColor(R.color.holo_blue_bright,
				R.color.holo_green_light,
				R.color.holo_orange_light,
				R.color.holo_red_light);
		// 设置SwipeRefreshLayout下拉监听事件
		mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshHandler.sendEmptyMessage(REFRESH_DOWN);
			}
		});
		// 设置SwipeRefreshLayout上拉加载事件
		mRefreshLayout.setOnLoadListener(new SwipeRefreshLayout.OnLoadListener() {
			@Override
			public void onLoad() {
				refreshHandler.sendEmptyMessage(REFRESH_UP);
			}
		});
		return mViewGroup;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//加载新闻
		loadNewsList(REFRESH_DOWN);

	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	// 加载新闻列表
	private void loadNewsList(int status) {

		setNewsListShown(mNewsList == null || mNewsList.isEmpty() ? false : true);

		if (status == REFRESH_DOWN) {
			// 若是下拉
			mRefreshLayout.setRefreshing(true);
			new GetNewsTask(getActivity(), this).execute(getArguments().getString("URL"));
		} else {
			// 若是上拉
			mRefreshLayout.setLoading(true);
			/**
			 * 新闻页数计数
			 * 说明：默认每次加载10条新闻
			 */
			mListCount = mNewsList == null ? 1 : mNewsList.size() / 10 + 1;
			new GetMoreNewsTask(getActivity(), this).execute(getArguments().getString("URL") + mListCount);
		}
	}

	// 显示/隐藏进度框
	private void setNewsListShown(boolean finishLoading) {
		mList.setVisibility(finishLoading ? View.VISIBLE : View.GONE);
		mLoadingProgress.setVisibility(finishLoading ? View.GONE : View.VISIBLE);
	}

	// 加载更多新闻GetMoreNewsTask
	private class GetMoreNewsTask extends GetNewsTask {

		public GetMoreNewsTask(Context mContext, ResponseListener mResponseListener) {
			super(mContext, mResponseListener);
		}

		@Override
		protected void onPostExecute(LinkedList<NewsModel> result) {
			if (isAdded()) {
				// 刷新结束，取消刷新状态
				mRefreshLayout.setRefreshing(false);
				mRefreshLayout.setLoading(false);

				if (mNewsList != null) {
					mNewsList.addAll(result);
					mAdapter.updateData(mNewsList);
				}

				setNewsListShown(true);
			}
		}
	}

	// 以下为GetNewsTask的ResponseListener接口方法实现
	@Override
	public void onPreExecute() {

	}

	@Override
	public void onPostExecute(LinkedList<NewsModel> result, boolean isRefreshSuccess, boolean isContentSame) {
		if (isAdded()) {
			// 刷新结束，取消刷新状态
			mRefreshLayout.setRefreshing(false);
			mRefreshLayout.setLoading(false);

			if (isRefreshSuccess && !isContentSame) {
				mNewsList = result;

				if (mAdapter == null) {
					// 此时为第一次获取新闻
					mAdapter = new NewsListAdapter(getActivity(), result);
					mList.setAdapter(mAdapter);
				} else {
					// 此时为刷新新闻
					mAdapter.updateData(mNewsList);
				}
			}
			setNewsListShown(true);
		}
	}

	@Override
	public void onProgressUpdate(Long value) {

	}

	@Override
	public void onFail(Exception e) {

	}

	// ListView点击监听事件
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		NewsModel mNewsModel = mNewsList != null ? mNewsList.get(position) : null;

		if (mNewsModel == null) {
			return;
		}

		Intent intent = new Intent();
		intent.putExtra("id", mNewsModel.getId());
		intent.putExtra("newsModel", mNewsModel);

		// 跳转到NewsDetailActivity
		intent.setClass(getActivity(), NewsDetailActivity.class);
		getActivity().startActivity(intent);
	}
}
