package com.ddmax.zjnucloud.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.model.NewsDetailModel;
import com.ddmax.zjnucloud.ui.fragments.NewsDetailFragment;

/**
 * @author ddMax
 * @since 2015/02/13
 * 说明：新闻详情页Activity
 */
public class NewsDetailActivity extends ActionBarActivity {

	private static final String NEWS_ID = "com.ddmax.zjnunews.activities.NewsDetailActivity.news_id";
	private static final String NEWS_DETAIL_MODEL = "com.ddmax.zjnunews.activities.NewsDetailActivity.news_detail_model";

	private long mNewsId = 0;
	private NewsDetailModel mNewsDetailModel = null;

	// Toolbar
	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);

		// 设置Toolbar标题，居中，返回
		mToolbar = (Toolbar) findViewById(R.id.mDetailToolbar);
		mToolbar.setTitle(R.string.detail);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {

			mNewsId = getIntent().getLongExtra("id", 0);
			mNewsDetailModel = (NewsDetailModel) getIntent().getSerializableExtra("newsDetailModel");

		} else {
			mNewsId = savedInstanceState.getLong(NEWS_ID);
			mNewsDetailModel = (NewsDetailModel) savedInstanceState.getSerializable(NEWS_DETAIL_MODEL);
		}

		// 设置Bundle将新闻ID传给NewsDetailFragment
		Bundle bundle = new Bundle();
		bundle.putLong("id", mNewsId);

		Fragment mFragment = getFragment();
		mFragment.setArguments(bundle);

		getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, mFragment).commit();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putLong(NEWS_ID, mNewsId);
		outState.putSerializable(NEWS_DETAIL_MODEL, mNewsDetailModel);
	}

	protected Fragment getFragment() {
		return new NewsDetailFragment();
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
}
