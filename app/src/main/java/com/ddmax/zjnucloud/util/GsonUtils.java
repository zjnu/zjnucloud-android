package com.ddmax.zjnucloud.util;

import android.text.TextUtils;

import com.ddmax.zjnucloud.model.news.News;
import com.ddmax.zjnucloud.model.news.NewsDetail;
import com.ddmax.zjnucloud.model.news.NewsList;
import com.ddmax.zjnucloud.model.news.SlxxDetail;
import com.ddmax.zjnucloud.model.news.SlxxList;
import com.ddmax.zjnucloud.model.score.Score;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * @author ddMax
 * @since 2014-01-20
 * 说明：使用Gson解析JSON工具类
 */
public class GsonUtils {

    public static Score getScore(String content){
        if (TextUtils.isEmpty(content)) return null;

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();
        Score score = gson.fromJson(content, Score.class);

        return score != null ? score : null;
    }

    // 返回新闻列表
    public static NewsList getNewsList(String content) {
        if (TextUtils.isEmpty(content)) return null;

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy年MM月dd日")
                .create();
        NewsList newsModel = gson.fromJson(content, NewsList.class);

        return newsModel != null ? newsModel : null;
    }

    // 返回新闻详情对象
    public static NewsDetail getNewsDetail(String content) {
        if (TextUtils.isEmpty(content)) return null;

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy年MM月dd日")
                .create();
        NewsDetail newsDetail = gson.fromJson(content, NewsDetail.class);

        return newsDetail != null ? newsDetail : null;
    }

    // 返回数理信息新闻列表对象
    public static SlxxList getSlxxList(String content) {
        if (TextUtils.isEmpty(content)) return null;

        Gson gson = new GsonBuilder().create();
        SlxxList newsListModel = gson.fromJson(content, SlxxList.class);

        return newsListModel != null ? newsListModel : null;
    }

    // 返回数理信息新闻详情对象
    public static SlxxDetail getSlxxDetail(String content) {
        if (TextUtils.isEmpty(content)) return null;

        Gson gson = new GsonBuilder().create();
	    SlxxDetail newsDetail = gson.fromJson(content, SlxxDetail.class);

        return newsDetail != null ? newsDetail : null;
    }
}
