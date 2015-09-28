package com.ddmax.zjnucloud.utils;

import android.text.TextUtils;

import com.ddmax.zjnucloud.model.NewsDetailModel;
import com.ddmax.zjnucloud.model.NewsListModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author ddMax
 * @since 2014-01-20
 * 说明：JSON解析工具类
 */
public class GsonUtils {

    public static NewsListModel getNewsList(String content) {

        if (TextUtils.isEmpty(content)) return null;

        Gson gson = new GsonBuilder().create();
        NewsListModel newsListModel = gson.fromJson(content, NewsListModel.class);

        return newsListModel != null ? newsListModel : null;
    }

    public static NewsDetailModel getNewsDetail(String content) {

        if (TextUtils.isEmpty(content)) return null;

        Gson gson = new GsonBuilder().create();
	    NewsDetailModel newsDetail = gson.fromJson(content, NewsDetailModel.class);

        return newsDetail != null ? newsDetail : null;
    }
}
