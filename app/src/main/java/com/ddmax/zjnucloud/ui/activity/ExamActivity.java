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
import com.ddmax.zjnucloud.adapter.ExamListAdapter;
import com.ddmax.zjnucloud.base.BaseEmisActivity;
import com.ddmax.zjnucloud.model.EmisUser;
import com.ddmax.zjnucloud.model.exam.Exam;
import com.ddmax.zjnucloud.model.exam.ExamList;
import com.ddmax.zjnucloud.task.BaseGetDataTask;
import com.ddmax.zjnucloud.task.ResponseListener;
import com.ddmax.zjnucloud.util.EmisUtils;
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
public class ExamActivity extends BaseEmisActivity implements ResponseListener<ExamList>{

    private static final String TAG = ScoreActivity.class.getSimpleName();

    @Bind(R.id.exam_layout) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.app_bar) AppBarLayout mAppBarLayout;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.exam_subtitle) TextView mSubtitleView;
    @Bind(R.id.fab) FloatingActionButton mFab;
    @Bind(R.id.exam_list) RecyclerView mRecyclerView;
    @Bind(R.id.error_message) LinearLayout mErrorView;
    @Bind(R.id.error_text) TextView mErrorText;

    private ExamList examList;
    private ExamListAdapter mAdapter;

    private Snackbar refreshSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_exam);
        ButterKnife.bind(this);

        mToolbar.setTitle(getString(R.string.title_activity_exam));
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
        if (new Select().from(ExamList.class).exists()) {
            ExamList examList = new Select().from(ExamList.class).executeSingle();
            // 设置第3个参数isContentSame为true来区分是否从本地读取
            this.onPostExecute(examList, true, true);
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
            String url = Constants.URL.EMIS.EXAM;
            refreshSnackbar = Snackbar.make(mCoordinatorLayout, R.string.exam_refreshing_exam, Snackbar.LENGTH_LONG)
                    .setAction("Action", null);
            refreshSnackbar.show();
            new GetExamTask(this, this).execute(
                    url, emisUser.username, emisUser.password, emisUser.token
            );
        }
    }

    private void initRecyclerView() {
        mAdapter = new ExamListAdapter(examList, this);
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
     * @param info 二级标题信息
     */
    private void refreshCollapsingInfo(String info) {
        String title = getString(R.string.exam_empty_title);
        try {
            title = info.substring(0, info.indexOf(','));
        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "info格式错误");
        }
        mCollapsingToolbarLayout.setTitle(title);
        if (mSubtitleView.getVisibility() == View.GONE) {
            mSubtitleView.setVisibility(View.VISIBLE);
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
    public static class GetExamTask extends BaseGetDataTask<ExamList> {

        public GetExamTask(Context mContext, ResponseListener mResponseListener) {
            super(mContext, mResponseListener);
        }

        @Override
        protected ExamList doInBackground(String... params) {
            String content = null;
            try {
                HashMap<String, String> data = new HashMap<>();
                data.put("username", params[1]);
                data.put("password", params[2]);
                content = RequestUtils.post(params[0], data, params[3]);
                ExamList examList = GsonUtils.getExamList(content);
                return examList;
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
    public void onPostExecute(ExamList examList, boolean isRefreshSuccess, boolean isContentSame) {
        // 停止刷新状态
        this.stopRefreshStatus();
        if (examList == null) {
            return;
        }
        switch (examList.status) {
            case 0:
                // 请求成功
                Log.d(TAG, "考试安排请求成功！");
                if (mErrorView.getVisibility() == View.VISIBLE) {
                    mErrorView.setVisibility(View.GONE);
                }
                // 非法的Token，提示重新绑定
                if (examList.detail != null && !examList.detail.isEmpty()) {
                    alertInvalidToken();
                    return;
                }
                this.examList = examList;
                // 若为刷新数据，则清空后保存到数据库
                if (!isContentSame) {
                    EmisUtils.clean(Exam.class, ExamList.class);
                    this.examList.save();
                }
                // 设置标题文字
                refreshCollapsingInfo(examList.info);
                // 更新Adapter
                mAdapter.updateData(examList);
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
                Snackbar.make(mCoordinatorLayout, examList.message, Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onProgressUpdate(Long value) {}

    @Override
    public void onFail(Exception e) {
        Log.d(TAG, "考试数据刷新失败！");
        if (e instanceof SocketTimeoutException) {
            Snackbar.make(mCoordinatorLayout, getString(R.string.emis_request_timeout), Snackbar.LENGTH_LONG).show();
        }
    }
}
