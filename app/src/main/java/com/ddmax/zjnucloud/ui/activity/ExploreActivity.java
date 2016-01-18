package com.ddmax.zjnucloud.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.base.BaseActivity;
import com.ddmax.zjnucloud.base.BaseWebActivity;
import com.ddmax.zjnucloud.model.explore.Explore;
import com.ddmax.zjnucloud.model.explore.ExploreList;
import com.ddmax.zjnucloud.ui.fragment.CommonDetailFragment;
import com.ddmax.zjnucloud.ui.view.SwipeRefreshLayout;
import com.ddmax.zjnucloud.util.RequestUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ExploreActivity extends BaseActivity {
    public static final String TAG = ExploreActivity.class.getSimpleName();

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.explore_list) RecyclerView mExploreList;
    @Bind(R.id.progress_view) RelativeLayout mLoadingProgress;

    private final static int REFRESH_DOWN = 0;
    private final static int REFRESH_UP = 1;

    private ExploreList exploreListModel;
    private List<Explore> exploreData;
    private ExploreAdapter mAdapter;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        ButterKnife.bind(this);
        initView();
        initRecycler();
        loadData(REFRESH_DOWN);
    }

    private void initView() {
        mToolbar.setTitle(getString(R.string.title_activity_explore));
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

    private void initRecycler() {
        mExploreList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ExploreAdapter(this, exploreData);
        mExploreList.setAdapter(mAdapter);
    }

    /**
     * 加载发现列表
     */
    private void loadData(int status) {
        setExploreListShown(exploreData == null || exploreData.isEmpty() ? false : true);

        if (status == REFRESH_DOWN) {
            // 下拉
            mRefreshLayout.setRefreshing(true);
            RequestUtils.getExplore(1, new Callback<ExploreList>() {
                @Override
                public void onResponse(Response<ExploreList> response, Retrofit retrofit) {
                    exploreListModel = response.body();
                    if (exploreListModel != null) {
                        exploreData = exploreListModel.posts;
                        page++;
                        // 取消刷新动画
                        mRefreshLayout.setRefreshing(false);
                        setExploreListShown(true);
                        mAdapter.updateData(exploreData);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(ExploreActivity.this, getString(R.string.network_fail), Toast.LENGTH_SHORT).show();
                    // 取消刷新动画
                    mRefreshLayout.setRefreshing(false);
                }
            });
        } else if (status == REFRESH_UP) {
            // 上拉，默认当发现列表数量大于10时加载更多
            Log.d(TAG, "count: " + exploreListModel.count);
            if (exploreListModel != null && exploreListModel.count >= 10) {
                mRefreshLayout.setLoading(true);
                RequestUtils.getExplore(page, new Callback<ExploreList>() {
                    @Override
                    public void onResponse(Response<ExploreList> response, Retrofit retrofit) {
                        exploreListModel = response.body();
                        if (exploreListModel != null) {
                            exploreData.addAll(exploreListModel.posts);
                            page++;
                            // 取消刷新动画
                            mRefreshLayout.setRefreshing(false);
                            setExploreListShown(true);
                            mAdapter.updateData(exploreData);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(ExploreActivity.this, getString(R.string.network_fail), Toast.LENGTH_SHORT).show();
                        // 取消刷新动画
                        mRefreshLayout.setRefreshing(false);
                    }
                });
            } else {
                mRefreshLayout.setLoading(false);
            }
        }
    }

    // 显示/隐藏进度框
    private void setExploreListShown(boolean finishLoading) {
        mExploreList.setVisibility(finishLoading ? View.VISIBLE : View.GONE);
        mLoadingProgress.setVisibility(finishLoading ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ViewHolder> {

        private Context context;
        private List<Explore> data;

        public ExploreAdapter(Context context, List<Explore> data) {
            this.context = context;
            this.data = data;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.card_view) CardView mCardView;
            @Bind(R.id.explore_image) ImageView mExploreImage;
            @Bind(R.id.explore_title) TextView mExploreTitle;
            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_explore_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Explore explore = data.get(position);
            holder.mExploreTitle.setText(explore.title_plain);
            Picasso.with(context).load(explore.image_url).fit().centerInside().into(holder.mExploreImage);
            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseWebActivity.actionStart(
                            context, explore.title_plain,
                            CommonDetailFragment.newInstance(explore.content)
                    );
                }
            });
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }

        private void updateData(List<Explore> data) {
            this.data = data;
            this.notifyDataSetChanged();
        }
    }
}
