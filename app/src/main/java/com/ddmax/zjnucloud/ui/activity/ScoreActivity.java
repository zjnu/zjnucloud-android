package com.ddmax.zjnucloud.ui.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.adapter.ScoreListAdapter;
import com.ddmax.zjnucloud.base.BaseEmisActivity;
import com.ddmax.zjnucloud.model.EmisUser;
import com.ddmax.zjnucloud.model.score.ScoreList;
import com.ddmax.zjnucloud.task.BaseGetDataTask;
import com.ddmax.zjnucloud.task.ResponseListener;
import com.ddmax.zjnucloud.util.GsonUtils;
import com.ddmax.zjnucloud.util.RequestUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * @author ddMax
 * @since 2015/11/13 14:15
 * 说明：成绩查询Activity
 */
public class ScoreActivity extends BaseEmisActivity implements ResponseListener<ScoreList> {

    private static final String TAG = "ScoreActivity";

    @Bind(R.id.score_layout) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.app_bar) AppBarLayout mAppBarLayout;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.item_credit) LinearLayout mCreditBar;
    @Bind(R.id.item_gpa) LinearLayout mGpaBar;
    @Bind(R.id.score_subtitle) TextView mSubtitleView;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.fab) FloatingActionButton mFab;
    @Bind(R.id.score_list) RecyclerView mRecyclerView;
    @Bind(R.id.error_message) LinearLayout mErrorView;
    @Bind(R.id.error_text) TextView mErrorText;

    private ScoreList scoreList;
    private ScoreListAdapter mAdapter;

    private Snackbar refreshSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);
        /**
         * 设置CollapsingToolbarLayout折叠变化监听器，
         * 只有当折叠时显示Toolbar标题
         */
//        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = false;
//            int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if (scrollRange + verticalOffset == 0) {
//                    findViewById(R.id.score_bar).setVisibility(View.GONE);
//                    isShow = true;
//                } else if (isShow) {
//                    findViewById(R.id.score_bar).setVisibility(View.VISIBLE);
//                    isShow = false;
//                }
//            }
//        });

        mToolbar.setTitle(getString(R.string.title_activity_score));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 设置浮动按钮监听器
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRefresh();
            }
        });
        // 初始化RecyclerView
        initRecyclerView();
    }

    @Override
    public void restoreData() {
        if (new Select().from(ScoreList.class).exists()) {
            ScoreList scoreList = new Select().from(ScoreList.class).executeSingle();
            // 设置第3个参数isContentSame为true来区分是否从本地读取
            this.onPostExecute(scoreList, true, true);
        } else {
            doRefresh();
        }
    }

    /**
     * 刷新成绩数据
     */
    @Override
    public void doRefresh() {
        if (new Select().from(EmisUser.class).exists()) {
            EmisUser emisUser = new Select().from(EmisUser.class).executeSingle();
            String url = Constants.URL.EMIS.SCORE;
            refreshSnackbar = Snackbar.make(mCoordinatorLayout, R.string.score_refreshing_score, Snackbar.LENGTH_LONG)
                    .setAction("Action", null);
            refreshSnackbar.show();
            new GetScoreTask(this, this).execute(
                    url, emisUser.username, emisUser.password, emisUser.token
            );
        }
    }

    private void initRecyclerView() {
        mAdapter = new ScoreListAdapter(scoreList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        // 设置RecyclerView Animator
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(mAdapter);
        alphaAdapter.setDuration(600);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        // TODO: 解决Recycler Animator卡顿
//        mRecyclerView.addItemDecoration(new MyItemDecoration(1));
//        mRecyclerView.setAdapter(alphaAdapter);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void startRefreshStatus() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        animation.setInterpolator(new AccelerateInterpolator());
        this.mFab.startAnimation(animation);
    }

    private void stopRefreshStatus() {
        this.mFab.clearAnimation();
        // 取消Snackbar显示
        if (refreshSnackbar != null) {
            refreshSnackbar.dismiss();
        }
    }

    /**
     * 当请求数据后，刷新CollapsingToolbarLayout的信息
     * @param subTitle 二级标题信息
     * @param credit
     * @param gpa
     */
    private void refreshCollapsingInfo(String subTitle, String credit, String gpa) {
        mCollapsingToolbarLayout.setTitle(subTitle);
        ((TextView) mCreditBar.findViewById(R.id.item_name)).setText(getString(R.string.score_credit));
        ((TextView) mCreditBar.findViewById(R.id.item_value)).setText(credit);
        ((TextView) mGpaBar.findViewById(R.id.item_name)).setText(getString(R.string.score_gpa));
        ((TextView) mGpaBar.findViewById(R.id.item_value)).setText(gpa);
        if (mSubtitleView.getVisibility() == View.GONE) {
            mSubtitleView.setVisibility(View.VISIBLE);
        }
        if (mCreditBar.getVisibility() == View.GONE) {
            mCreditBar.setVisibility(View.VISIBLE);
        }
        if (mGpaBar.getVisibility() == View.GONE) {
            mGpaBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * RecyclerView自定义列表项间隔类
     */
    public class MyItemDecoration extends RecyclerView.ItemDecoration {
        private int mVerticalSpaceHeight;

        public MyItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;

            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = mVerticalSpaceHeight;
            }
        }
    }

    /**
     * 异步任务载入成绩数据
     */
    public static class GetScoreTask extends BaseGetDataTask<ScoreList> {

        public GetScoreTask(Context mContext, ResponseListener mResponseListener) {
            super(mContext, mResponseListener);
        }

        @Override
        protected ScoreList doInBackground(String... params) {
            String content = null;
            try {
                HashMap<String, String> data = new HashMap<>();
                data.put("username", params[1]);
                data.put("password", params[2]);
                content = RequestUtils.post(params[0], data, params[3]);
                ScoreList scoreList = GsonUtils.getScoreList(content);
                return scoreList;
            } catch (IOException e) {
                mResponseListener.onFail(e);
            }
            return null;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_emis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                break;
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // 以下为ResponseListener回调接口实现
    @Override
    public void onPreExecute() {
        this.startRefreshStatus();
    }

    @Override
    public void onPostExecute(ScoreList scoreList, boolean isRefreshSuccess, boolean isContentSame) {
        // 停止刷新状态
        this.stopRefreshStatus();
        if (scoreList == null) {
            return;
        }
        switch (scoreList.status) {
            case 0:
                // 请求成功
                Log.d(TAG, "成绩获取成功！");
                if (mErrorView.getVisibility() == View.VISIBLE) {
                    mErrorView.setVisibility(View.GONE);
                }
                // 非法的Token，提示重新绑定
                if (scoreList.detail != null && !scoreList.detail.isEmpty()) {
                    alertInvalidToken();
                    return;
                }
                this.scoreList = scoreList;
                // 保存到数据库
                if (!isContentSame) {
                    this.scoreList.save();
                }
                // 设置标题文字
                refreshCollapsingInfo(scoreList.name, scoreList.credits, scoreList.gpa);
                // 更新Adapter
                mAdapter.updateData(scoreList);
                break;
            case 3:
                // 未能请求到数据
                mErrorText.setText(getString(R.string.emis_server_error));
                if (mErrorView.getVisibility() == View.GONE) {
                    mErrorView.setVisibility(View.VISIBLE);
                }
                break;
            case -1:
                mErrorText.setText(getString(R.string.emis_error));
                if (mErrorView.getVisibility() == View.GONE) {
                    mErrorView.setVisibility(View.VISIBLE);
                }
                break;
            default:
                Snackbar.make(mCoordinatorLayout, scoreList.message, Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onProgressUpdate(Long value) {}

    @Override
    public void onFail(Exception e) {
        Log.d(TAG, "成绩刷新失败！");
        if (e instanceof SocketTimeoutException) {
            Snackbar.make(mCoordinatorLayout, getString(R.string.emis_request_timeout), Snackbar.LENGTH_LONG).show();
        }
    }
}
