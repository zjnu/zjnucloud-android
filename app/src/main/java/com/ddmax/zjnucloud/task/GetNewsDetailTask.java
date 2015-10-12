package com.ddmax.zjnucloud.task;

import android.content.Context;
import android.util.Log;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.model.news.NewsDetail;
import com.ddmax.zjnucloud.model.news.SlxxDetail;
import com.ddmax.zjnucloud.util.GsonUtils;

import java.io.IOException;

/**
 * @author ddMax
 * @since 2015/10/11
 * 说明：从API服务端获取新闻详情
 */
public class GetNewsDetailTask extends BaseGetNewsTask<NewsDetail> {
    public static final String TAG = "GetNewsDetailTask";

    public GetNewsDetailTask(Context mContext, ResponseListener mResponseListener) {
        super(mContext, mResponseListener);
    }

    @Override
    protected NewsDetail doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        // 去掉地址中的?format=json
        String baseUrl = params[0].substring(0, params[0].indexOf('?'));
        String url = new StringBuilder(baseUrl)
                .append(params[1]).append("/detail/?format=json")
                .toString();
        String detailContent;
        NewsDetail mNewsDetailModel = null;

        try {
            detailContent = getUrl(url);
            mNewsDetailModel = GsonUtils.getNewsDetail(detailContent);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mNewsDetailModel;
    }
}
