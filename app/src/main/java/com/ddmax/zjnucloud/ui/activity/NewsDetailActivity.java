package com.ddmax.zjnucloud.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.base.BaseActivity;
import com.ddmax.zjnucloud.model.news.BaseNewsDetail;
import com.ddmax.zjnucloud.model.news.News;
import com.ddmax.zjnucloud.model.news.NewsDetail;
import com.ddmax.zjnucloud.model.news.SlxxDetail;
import com.ddmax.zjnucloud.ui.fragment.NewsDetailFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author ddMax
 * @since 2015/02/13
 * 说明：新闻详情页Activity
 */
public class NewsDetailActivity extends BaseActivity {

    private static final String NEWS_ID = "com.ddmax.zjnunews.activities.NewsDetailActivity.news_id";
    private static final String NEWS_DETAIL_MODEL = "com.ddmax.zjnunews.activities.NewsDetailActivity.news_detail_model";

    private long mArticleId = 0; // 文章id
    private BaseNewsDetail mBaseDetailModel = null; // 新闻详情模型
    private News mNewsModel = null; // 新闻模型
    private String mUrl; // 新闻详情连接
    private boolean isSlxx; // 是否是数理信息新闻

    // Toolbar
    @Bind(R.id.mDetailToolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        // 设置Toolbar标题，居中，返回
        mToolbar.setTitle(R.string.detail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 是否是数理信息新闻
        isSlxx = getIntent().getBooleanExtra("isSlxx", false);
        if (savedInstanceState == null) {

            mArticleId = getIntent().getLongExtra("id", 0);
            if (!isSlxx) {
                mBaseDetailModel = (NewsDetail) getIntent().getSerializableExtra("newsDetailModel");
            } else {
                mBaseDetailModel = (SlxxDetail) getIntent().getSerializableExtra("newsDetailModel");
            }

        } else {
            mArticleId = savedInstanceState.getLong(NEWS_ID);
            if (!isSlxx) {
                mBaseDetailModel = (NewsDetail) savedInstanceState.getSerializable(NEWS_DETAIL_MODEL);
            } else {
                mBaseDetailModel = (SlxxDetail) savedInstanceState.getSerializable(NEWS_DETAIL_MODEL);
            }
        }

        // 设置新闻对象模型
        mNewsModel = (News) getIntent().getSerializableExtra("newsModel");

        // 设置Bundle将新闻ID传给NewsDetailFragment
        Bundle bundle = new Bundle();
        bundle.putLong("id", mArticleId);
        bundle.putBoolean("isSlxx", isSlxx);
        bundle.putInt("hits", mNewsModel.getHits());

        Fragment mFragment = getFragment();
        mFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, mFragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(NEWS_ID, mArticleId);
        outState.putSerializable(NEWS_DETAIL_MODEL, mBaseDetailModel);
    }

    protected Fragment getFragment() {
        return new NewsDetailFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_share:
                shareBySystem();
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: ShareSDK分享模块 - 新闻
    private void shareBySystem() {
//        ShareSDK.initSDK(this);
//        OnekeyShare oks = new OnekeyShare();
//        oks.disableSSOWhenAuthorize();
//        oks.setTitle(mNewsModel.getTitle());
//        oks.setText(mNewsModel.getTitle());
//
//        oks.show(this);
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
//        //设置分享的内容
//        intent.putExtra(Intent.EXTRA_TEXT, mNewsModel.getTitle());
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(Intent.createChooser(intent, getTitle()));
    }
}
