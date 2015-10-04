package com.ddmax.zjnucloud.task;

import android.content.Context;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.utils.GsonUtils;
import com.ddmax.zjnucloud.model.NewsDetailModel;

import java.io.IOException;

/**
 * @author ddMax
 * @since 2015/02/13
 * 说明：后台获取新闻详情
 */
public class GetNewsDetailTask extends BaseGetNewsTask<NewsDetailModel> {

	public GetNewsDetailTask(Context mContext, ResponseListener mResponseListener) {
		super(mContext, mResponseListener);
	}

	@Override
	protected NewsDetailModel doInBackground(String... params) {

		if (params.length == 0) {
			return null;
		}

		String detailContent;
		NewsDetailModel mNewsDetailModel = null;

		try {
			detailContent = getUrl(Constants.URL.NEWS.SLXX_DETAIL + params[0]);
			mNewsDetailModel = GsonUtils.getNewsDetail(detailContent);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return mNewsDetailModel;
	}
}
