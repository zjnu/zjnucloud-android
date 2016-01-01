package com.ddmax.zjnucloud.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.base.BaseActivity;
import com.ddmax.zjnucloud.base.BaseWebActivity;
import com.ddmax.zjnucloud.model.speech.Speech;
import com.ddmax.zjnucloud.model.speech.SpeechList;
import com.ddmax.zjnucloud.task.BaseGetDataTask;
import com.ddmax.zjnucloud.task.ResponseListener;
import com.ddmax.zjnucloud.ui.fragment.SpeechDetailFragment;
import com.ddmax.zjnucloud.ui.view.SwipeRefreshLayout;
import com.ddmax.zjnucloud.util.GsonUtils;
import com.ddmax.zjnucloud.util.RequestUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author ddMax
 * @since 2015/12/19 20:06
 * 说明：讲座预告列表Activity
 */
public class SpeechActivity extends BaseActivity implements ResponseListener<List<Speech>>{

    public static final String TAG = SpeechActivity.class.getSimpleName();

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.speech_list) RecyclerView mSpeechList;
    @Bind(R.id.progressView) RelativeLayout mLoadingProgress;
    @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout mRefreshLayout;

    private final static int REFRESH_DOWN = 0;
    private final static int REFRESH_UP = 1;

    private SpeechList speechListModel;
    private List<Speech> speechData;
    private SpeechAdapter mAdapter;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        ButterKnife.bind(this);
        // 初始化View
        initView();
        // 初始化Recycler
        initRecycler();
        // 请求讲座列表
        loadData(REFRESH_DOWN);
    }

    private void initView() {
        mToolbar.setTitle(getString(R.string.title_activity_speech));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 设置下拉主题
        mRefreshLayout.setColor(R.color.holo_blue_bright,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(REFRESH_DOWN);
            }
        });
        mRefreshLayout.setOnLoadListener(new SwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                loadData(REFRESH_UP);
            }
        });
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecycler() {
        mAdapter = new SpeechAdapter(this, speechData);
        mSpeechList.setLayoutManager(new LinearLayoutManager(this));
        mSpeechList.setAdapter(mAdapter);
    }

    /**
     * 加载讲座列表
     */
    private void loadData(int status) {

        setSpeechListShown(speechData == null || speechData.isEmpty() ? false : true);

        if (status == REFRESH_DOWN) {
            // 下拉
            mRefreshLayout.setRefreshing(true);
            new GetSpeechTask(this, this).execute(Constants.URL.SPEECH);
        } else if (status == REFRESH_UP) {
            // 上拉
            mRefreshLayout.setLoading(true);
            new GetMoreSpeechTask(this, this).execute(Constants.URL.SPEECH + "&page=" + page);
        }
    }

    // 显示/隐藏进度框
    private void setSpeechListShown(boolean finishLoading) {
        mSpeechList.setVisibility(finishLoading ? View.VISIBLE : View.GONE);
        mLoadingProgress.setVisibility(finishLoading ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class GetSpeechTask extends BaseGetDataTask<List<Speech>> {

        public GetSpeechTask(Context mContext, ResponseListener mResponseListener) {
            super(mContext, mResponseListener);
        }

        @Override
        protected List<Speech> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String newContent;
            SpeechList speechList = null;
            try {
                newContent = RequestUtils.get(params[0]);
                speechList = GsonUtils.getSpeechList(newContent);

                // 判断获取新闻列表是否成功
                isRefreshSuccess = speechList == null ? false : true;

            } catch (IOException e) {
                e.printStackTrace();
                this.isRefreshSuccess = false;
                this.e = e;
            }

//		isContentSame = checkIsContentSame(oldContent, newContent);

            if (isRefreshSuccess && !isContentSame) {
                // TODO

            }

            return speechList == null ? null : speechList.result;
        }
    }

    public class GetMoreSpeechTask extends GetSpeechTask {

        public GetMoreSpeechTask(Context mContext, ResponseListener mResponseListener) {
            super(mContext, mResponseListener);
        }

        @Override
        protected void onPostExecute(List<Speech> result) {
            // 取消刷新动画
            mRefreshLayout.setLoading(false);
            setSpeechListShown(true);
            page++;
            if (result != null) {
                if (result.isEmpty()) {
                    Toast.makeText(SpeechActivity.this, getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();
                } else {
                    speechData.addAll(result);
                    // 更新Adapter数据
                    mAdapter.updateData(speechData);
                }
            }
        }
    }
    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(List<Speech> result, boolean isRefreshSuccess, boolean isContentSame) {
        this.speechData = result;
        if (this.speechData != null) {
            page++;
            // 取消刷新动画
            mRefreshLayout.setRefreshing(false);
            setSpeechListShown(true);
            initRecycler();
        }
    }

    @Override
    public void onProgressUpdate(Long value) {

    }

    @Override
    public void onFail(Exception e) {

    }

    public static class SpeechAdapter extends RecyclerView.Adapter<SpeechAdapter.ViewHolder> {

        private Context mContext;
        private List<Speech> speechData;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            final View mView;
            @Bind(R.id.image) ImageView mImageView;
            @Bind(R.id.title) TextView mTitleView;
            @Bind(R.id.overview) TextView mInfoView;
            @Bind(R.id.date) TextView mDateView;

            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                ButterKnife.bind(this, itemView);
            }
        }

        public SpeechAdapter(Context mContext, List<Speech> speechData) {
            this.mContext = mContext;
            this.speechData = speechData;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_speech, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (speechData != null) {
                final Speech speech = speechData.get(position);
                holder.mTitleView.setText(speech.title);
                holder.mInfoView.setText(speech.overview);
                holder.mDateView.setText(speech.date);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        BaseWebActivity.actionStart(context,
                                speech.title,
                                SpeechDetailFragment.newInstance(
                                    Constants.URL.BASE_URL + "/speech/" + speech.id + "/detail/?format=json"
                                )
                        );
                    }
                });
                Picasso.with(mContext).load(speech.pic).into(holder.mImageView);

            }
        }

        @Override
        public int getItemCount() {
            return speechData == null ? 0 : speechData.size();
        }

        public void updateData(List<Speech> data) {
            this.speechData = data;
            this.notifyDataSetChanged();
        }
    }
}
