package com.ddmax.zjnucloud.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.adapter.NewsListAdapter;
import com.ddmax.zjnucloud.common.exception.FatalException;
import com.ddmax.zjnucloud.model.Page;
import com.ddmax.zjnucloud.model.news.News;
import com.ddmax.zjnucloud.task.GetNewsTask;
import com.ddmax.zjnucloud.task.GetSlxxTask;
import com.ddmax.zjnucloud.task.ResponseListener;
import com.ddmax.zjnucloud.ui.view.SwipeRefreshLayout;

import java.util.LinkedList;

/**
 * @author ddMax
 * @since 2014/12/31
 * 说明：新闻列表Fragment
 */
public class NewsFragment extends Fragment implements ResponseListener<LinkedList<News>> {

    public static final String TAG = "NewsFragment";
    public static final String ARG_PAGE = "page";
    private final static int REFRESH_DOWN = 0;
    private final static int REFRESH_UP = 1;

    // 记录当前新闻加载的次数
    private static int mListCount = 1;

    private Page mPage;

    // 进度条
    private RelativeLayout mLoadingProgress;
    private RecyclerView mList;
    private SwipeRefreshLayout mRefreshLayout;

    // 新闻列表
    private LinkedList<News> mNewsList = null;
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

    public static NewsFragment newInstance(Page page) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    public NewsFragment() {
        // 空的构造器
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle arguments = getArguments();
        if (arguments != null) {
            mPage = arguments.getParcelable(ARG_PAGE);
            Log.d(TAG, mPage.getTitle() + ":" + mPage.getUrl());
        }
        if (mPage == null) {
            throw new FatalException("Tab can't be null");
        }
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
        mList = (RecyclerView) mViewGroup.findViewById(R.id.newsList);
        // 初始化新闻列表
        initRecycler(mList);
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

    private void initRecycler(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new NewsListAdapter(mNewsList, mPage));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //加载新闻
        loadNewsList(REFRESH_DOWN);
    }

    @Override
    public void onStart() {
        super.onStart();

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
            if (mPage.getTitle().startsWith("数理信息")) {
                new GetSlxxTask(getActivity(), this).execute(mPage.getUrl());
            } else {
                new GetNewsTask(getActivity(), this).execute(mPage.getUrl());
            }
        } else {
            // 若是上拉
            mRefreshLayout.setLoading(true);
            /**
             * 新闻页数计数
             * 说明：默认每次加载10条新闻
             */
            mListCount = mNewsList == null ? 1 : mNewsList.size() / 10 + 1;
            if (mPage.getTitle().startsWith("数理信息")) {
                new GetMoreSlxxTask(getActivity(), this).execute(mPage.getUrl() + mListCount);
            } else {
                new GetMoreNewsTask(getActivity(), this).execute(mPage.getUrl() + "&page=" + mListCount);
            }
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
        protected void onPostExecute(LinkedList<News> result) {
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

    // 加载更多新闻GetMoreSlxxTask
    private class GetMoreSlxxTask extends GetSlxxTask {

        public GetMoreSlxxTask(Context mContext, ResponseListener mResponseListener) {
            super(mContext, mResponseListener);
        }

        @Override
        protected void onPostExecute(LinkedList<News> result) {
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
    public void onPostExecute(LinkedList<News> result, boolean isRefreshSuccess, boolean isContentSame) {
        if (isAdded()) {
            // 刷新结束，取消刷新状态
            mRefreshLayout.setRefreshing(false);
            mRefreshLayout.setLoading(false);

            if (isRefreshSuccess && !isContentSame) {
                mNewsList = result;

                if (mAdapter == null) {
                    // 此时为第一次获取新闻
                    mAdapter = new NewsListAdapter(result, mPage);
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

}
