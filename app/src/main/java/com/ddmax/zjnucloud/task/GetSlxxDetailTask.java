package com.ddmax.zjnucloud.task;

import android.content.Context;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.util.GsonUtils;
import com.ddmax.zjnucloud.model.news.SlxxDetail;
import com.ddmax.zjnucloud.util.RequestUtil;

import java.io.IOException;

/**
 * @author ddMax
 * @since 2015/02/13
 * 说明：由于数理信息学工网已有返回JSON数据新闻，
 * 		因此单独写成异步任务
 */
public class GetSlxxDetailTask extends BaseGetDataTask<SlxxDetail> {

	public GetSlxxDetailTask(Context mContext, ResponseListener mResponseListener) {
		super(mContext, mResponseListener);
	}

	@Override
	protected SlxxDetail doInBackground(String... params) {

		if (params.length == 0) {
			return null;
		}

		String detailContent;
		SlxxDetail mSlxxDetailModel = null;

		try {
			detailContent = RequestUtil.getString(Constants.URL.NEWS.SLXX_DETAIL + params[0]);
			mSlxxDetailModel = GsonUtils.getSlxxDetail(detailContent);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return mSlxxDetailModel;
	}
}
