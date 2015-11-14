package com.ddmax.zjnucloud.ui.activity;

import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.adapter.ScoreListAdapter;
import com.ddmax.zjnucloud.model.score.Score;
import com.ddmax.zjnucloud.util.GsonUtils;
import com.ddmax.zjnucloud.util.RequestUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * @author ddMax
 * @since 2015/11/13 14:15
 * 说明：成绩查询Activity
 */
public class ScoreActivity extends AppCompatActivity {

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
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;

    private Score score;
    private String mUsername = "13211115";
    private String mPassword = "13211115";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);

        // 初始化View
        initView();
        // 刷新数据
        doRefresh();

    }

    private void initView() {

        /**
         * 设置CollapsingToolbarLayout折叠变化监听器，
         * 只有当折叠时显示Toolbar标题
         */
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    findViewById(R.id.score_bar).setVisibility(View.GONE);
                    isShow = true;
                } else if (isShow) {
                    findViewById(R.id.score_bar).setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });

        mToolbar.setTitle(getString(R.string.score_title));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRefresh();
            }
        });
    }

    /**
     * 刷新成绩数据
     */
    private void doRefresh() {
        String url = Constants.URL.SCORE + "?username=" + mUsername + "&password=" + mPassword + "&format=json";
        Snackbar.make(mCoordinatorLayout, R.string.score_refreshing_score, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        new GetScoreTask(this).execute(url);
    }

    private void initRecyclerView() {
        mAdapter = new ScoreListAdapter(score, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        // 设置RecyclerView Animator
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(mAdapter);
        alphaAdapter.setDuration(1000);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        // 设置ItemDecoration
//        mRecyclerView.addItemDecoration(new MyItemDecoration(1));
        mRecyclerView.setAdapter(alphaAdapter);
    }

    private void startFabRotate() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        animation.setInterpolator(new AccelerateInterpolator());
        this.mFab.startAnimation(animation);
    }

    private void stopFabRotate() {
        this.mFab.clearAnimation();
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
    public static class GetScoreTask extends AsyncTask<String, Void, Score> {
        WeakReference<ScoreActivity> scoreActivity;

        public GetScoreTask(ScoreActivity scoreActivity) {
            this.scoreActivity = new WeakReference<>(scoreActivity);
        }

        @Override
        protected void onPreExecute() {
            ScoreActivity activity = scoreActivity.get();
            // FAB旋转动画开始
            activity.startFabRotate();
        }

        @Override
        protected Score doInBackground(String... params) {
            String content = null;
            try {
                content = RequestUtil.getString(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Score score = GsonUtils.getScore(content);
//            Score score = GsonUtils.getScore("{\"status\":0,\"name\":\"吴逸川\",\"credits\":\"111.5\",\"gpa\":\"3.56\",\"count\":51,\"scores\":[{\"semester\":\"2014学年第2学期\",\"values\":[{\"id\":\"2210000004\",\"name\":\"大学体育(四)\",\"credit\":\"1\",\"mark\":\"50\",\"makeupmark\":\"40\",\"retakemark\":\"80\",\"gradepoint\":\"3\"},{\"id\":\"2220000291\",\"name\":\"Android移动媒体应用开发\",\"credit\":\"1\",\"mark\":\"87\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3.5\"},{\"id\":\"0260100068\",\"name\":\"专业见习\",\"credit\":\"2\",\"mark\":\"A\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4.5\"},{\"id\":\"0210000003\",\"name\":\"毛泽东思想和中国特色社会主义理论体系概论（二）\",\"credit\":\"3\",\"mark\":\"82\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3\"},{\"id\":\"0080600396\",\"name\":\"操作系统(Linux)\",\"credit\":\"4\",\"mark\":\"89.5\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3.5\"},{\"id\":\"0080600395\",\"name\":\"Web Authoring & Design\",\"credit\":\"3\",\"mark\":\"94\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4\"},{\"id\":\"0080600391\",\"name\":\"System Analysis & Design\",\"credit\":\"3\",\"mark\":\"87\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3.5\"},{\"id\":\"0080600387\",\"name\":\"英语泛读与写作（二）\",\"credit\":\"2\",\"mark\":\"81\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3\"},{\"id\":\"0080600187\",\"name\":\"软件设计综合训练\",\"credit\":\"3\",\"mark\":\"100\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"5\"},{\"id\":\"0070100028\",\"name\":\"概率统计\",\"credit\":\"2\",\"mark\":\"80\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3\"}]},{\"semester\":\"2014学年第1学期\",\"values\":[{\"id\":\"2210000003\",\"name\":\"大学体育(三)\",\"credit\":\"1\",\"mark\":\"76\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"2.5\"},{\"id\":\"2220000072\",\"name\":\"物理学与人类文明\",\"credit\":\"1\",\"mark\":\"91\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4\"},{\"id\":\"0260100047\",\"name\":\"思想政治理论课社会实践\",\"credit\":\"2\",\"mark\":\"B\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3.5\"},{\"id\":\"0210000096\",\"name\":\"大学生职业生涯规划与就业指导(二)\",\"credit\":\".5\",\"mark\":\"A\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4.5\"},{\"id\":\"0210000017\",\"name\":\"大学英语听力(三)\",\"credit\":\"1\",\"mark\":\"81\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3\"},{\"id\":\"0210000013\",\"name\":\"大学英语(三)\",\"credit\":\"3\",\"mark\":\"84\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3\"},{\"id\":\"0210000002\",\"name\":\"毛泽东思想和中国特色社会主义理论体系概论（一）\",\"credit\":\"3\",\"mark\":\"68\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"1.5\"},{\"id\":\"0210000001\",\"name\":\"马克思主义基本原理\",\"credit\":\"3\",\"mark\":\"83\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3\"},{\"id\":\"0080600393\",\"name\":\"Networks in Organisations\",\"credit\":\"2\",\"mark\":\"84\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3\"},{\"id\":\"0080600390\",\"name\":\"Database Application\",\"credit\":\"4\",\"mark\":\"95\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4.5\"},{\"id\":\"0080600386\",\"name\":\"英语泛读与写作（一）\",\"credit\":\"2\",\"mark\":\"90\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4\"},{\"id\":\"0080600156\",\"name\":\"面向对象程序设计Java(A)\",\"credit\":\"4\",\"mark\":\"95\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4.5\"},{\"id\":\"0080600041\",\"name\":\"程序设计综合训练\",\"credit\":\"3\",\"mark\":\"95\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4.5\"},{\"id\":\"0070100045\",\"name\":\"离散数学A\",\"credit\":\"4\",\"mark\":\"88\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3.5\"}]},{\"semester\":\"2013学年第2学期\",\"values\":[{\"id\":\"2210000002\",\"name\":\"大学体育(二)\",\"credit\":\"1\",\"mark\":\"78\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"2.5\"},{\"id\":\"2220000224\",\"name\":\"食品安全与健康\",\"credit\":\"1\",\"mark\":\"90\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4\"},{\"id\":\"0210000095\",\"name\":\"大学生职业生涯规划与就业指导(一)\",\"credit\":\".5\",\"mark\":\"B\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3.5\"},{\"id\":\"0210000016\",\"name\":\"大学英语听力(二)\",\"credit\":\"1\",\"mark\":\"80\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3\"},{\"id\":\"0210000012\",\"name\":\"大学英语(二)\",\"credit\":\"3\",\"mark\":\"82\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3\"},{\"id\":\"0210000033\",\"name\":\"高等数学D(二)\",\"credit\":\"4\",\"mark\":\"77\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"2.5\"},{\"id\":\"0210000004\",\"name\":\"思想道德修养与法律基础\",\"credit\":\"3\",\"mark\":\"72\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"2\"},{\"id\":\"0080600389\",\"name\":\"Creative Computing\",\"credit\":\"3\",\"mark\":\"97\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4.5\"},{\"id\":\"0080600385\",\"name\":\"英语会话（二）\",\"credit\":\"1\",\"mark\":\"98\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4.5\"},{\"id\":\"0080600384\",\"name\":\"The Computing Professional\",\"credit\":\"2\",\"mark\":\"97\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4.5\"},{\"id\":\"0080600206\",\"name\":\"数据结构与算法A\",\"credit\":\"6\",\"mark\":\"90\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4\"},{\"id\":\"0070200018\",\"name\":\"大学物理实验D\",\"credit\":\".5\",\"mark\":\"92\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4\"},{\"id\":\"0070200017\",\"name\":\"大学物理D\",\"credit\":\"3\",\"mark\":\"90\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4\"}]},{\"semester\":\"2013学年第1学期\",\"values\":[{\"id\":\"2210000001\",\"name\":\"大学体育(一)\",\"credit\":\"1\",\"mark\":\"60\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"1\"},{\"id\":\"0260100036\",\"name\":\"军事训练\",\"credit\":\"1\",\"mark\":\"82\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3\"},{\"id\":\"0210000015\",\"name\":\"大学英语听力(一)\",\"credit\":\"1\",\"mark\":\"87\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3.5\"},{\"id\":\"0210000011\",\"name\":\"大学英语(一)\",\"credit\":\"3\",\"mark\":\"93\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4\"},{\"id\":\"0210000038\",\"name\":\"军事理论\",\"credit\":\"1\",\"mark\":\"79\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"2.5\"},{\"id\":\"0210000032\",\"name\":\"高等数学D(一)\",\"credit\":\"4\",\"mark\":\"92\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4\"},{\"id\":\"0210000022\",\"name\":\"大学生心理调适与发展\",\"credit\":\"1\",\"mark\":\"82\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3\"},{\"id\":\"0210000006\",\"name\":\"形势与政策（一）\",\"credit\":\"1\",\"mark\":\"B\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3.5\"},{\"id\":\"0210000005\",\"name\":\"中国近现代史纲要\",\"credit\":\"2\",\"mark\":\"77\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"2.5\"},{\"id\":\"0080600381\",\"name\":\"高级语言程序设计实验A\",\"credit\":\"2\",\"mark\":\"97\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4.5\"},{\"id\":\"0080600380\",\"name\":\"英语会话（一）\",\"credit\":\"1\",\"mark\":\"86\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3.5\"},{\"id\":\"0080600379\",\"name\":\"Digital Infrastructure\",\"credit\":\"2\",\"mark\":\"96\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4.5\"},{\"id\":\"0080600109\",\"name\":\"高级语言程序设计A\",\"credit\":\"3\",\"mark\":\"93\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"4\"},{\"id\":\"0070100083\",\"name\":\"线性代数B\",\"credit\":\"2\",\"mark\":\"83\",\"makeupmark\":\"\",\"retakemark\":\"\",\"gradepoint\":\"3\"}]}]}");
            return score;
        }

        @Override
        protected void onPostExecute(Score score) {
            ScoreActivity activity = scoreActivity.get();
            if (activity == null) return;
            // 停止FAB动画
            activity.stopFabRotate();
            // 未能请求到数据
            if (score == null) {
                activity.mErrorText.setText(activity.getString(R.string.score_server_error));
                if (activity.mErrorView.getVisibility() == View.GONE) {
                    activity.mErrorView.setVisibility(View.VISIBLE);
                }
                return;
            }
            // 请求成功
            if (score.getStatus() == 0) {
                if (activity.mErrorView.getVisibility() == View.VISIBLE) {
                    activity.mErrorView.setVisibility(View.GONE);
                }
                activity.score = score;
                // 设置标题文字
                activity.refreshCollapsingInfo(score.getName(), score.getCredits(), score.getGpa());
                // 初始化RecyclerView
                activity.initRecyclerView();
                activity.mAdapter.notifyDataSetChanged();
            } else if (score.getStatus() == -1) {
                activity.mErrorText.setText(activity.getString(R.string.score_emis_error));
                if (activity.mErrorView.getVisibility() == View.GONE) {
                    activity.mErrorView.setVisibility(View.VISIBLE);
                }
            } else {
                Snackbar.make(activity.mCoordinatorLayout, score.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_score, menu);
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
}
