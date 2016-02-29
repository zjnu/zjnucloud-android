package com.ddmax.zjnucloud.util;

import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.Model;
import com.ddmax.zjnucloud.database.aa.ActiveAndroidExclusionStrategy;
import com.ddmax.zjnucloud.model.banner.BannerDetail;
import com.ddmax.zjnucloud.model.course.CourseList;
import com.ddmax.zjnucloud.model.exam.ExamList;
import com.ddmax.zjnucloud.model.news.NewsDetail;
import com.ddmax.zjnucloud.model.news.NewsList;
import com.ddmax.zjnucloud.model.news.SlxxDetail;
import com.ddmax.zjnucloud.model.news.SlxxList;
import com.ddmax.zjnucloud.model.score.ScoreList;
import com.ddmax.zjnucloud.model.speech.SpeechDetail;
import com.ddmax.zjnucloud.model.speech.SpeechList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * @author ddMax
 * @since 2014-01-20
 * 说明：使用Gson解析JSON工具类
 */
public class GsonUtils {

    private static final String TAG = "GsonUtils";

    public static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy年MM月dd日")
            .setExclusionStrategies(new ActiveAndroidExclusionStrategy(null, Model.class))
            .serializeNulls()
            .create();

    // 返回新闻列表
    public static NewsList getNewsList(String content) {
        if (TextUtils.isEmpty(content)) return null;

        try {
            NewsList newsModel = gson.fromJson(content, NewsList.class);
            return newsModel != null ? newsModel : null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, "Gson解析错误，未能获取到正确内容?");
            return null;
        }

    }

    // 返回新闻详情对象
    public static NewsDetail getNewsDetail(String content) {
        if (TextUtils.isEmpty(content)) return null;

        try {
            NewsDetail newsDetail = gson.fromJson(content, NewsDetail.class);
            return newsDetail != null ? newsDetail : null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, "Gson解析错误，未能获取到正确内容?");
            return null;
        }

    }

    // 返回数理信息新闻列表对象
    public static SlxxList getSlxxList(String content) {
        if (TextUtils.isEmpty(content)) return null;

        try {
            SlxxList newsListModel = gson.fromJson(content, SlxxList.class);
            return newsListModel != null ? newsListModel : null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, "Gson解析错误，未能获取到正确内容?");
            return null;
        }

    }

    // 返回数理信息新闻详情对象
    public static SlxxDetail getSlxxDetail(String content) {
        if (TextUtils.isEmpty(content)) return null;

        try {
            SlxxDetail newsDetail = gson.fromJson(content, SlxxDetail.class);
            return newsDetail != null ? newsDetail : null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, "Gson解析错误，未能获取到正确内容?");
            return null;
        }

    }

    // 返回讲座列表
    public static SpeechList getSpeechList(String content) {
        if (TextUtils.isEmpty(content)) return null;

        try {
            SpeechList speechListModel = gson.fromJson(content, SpeechList.class);
            return speechListModel != null ? speechListModel : null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, "Gson解析错误，未能获取到正确内容?");
            return null;
        }
    }

    // 返回讲座详情
    public static SpeechDetail getSpeechDetail(String content) {
        if (TextUtils.isEmpty(content)) return null;

        try {
            SpeechDetail speechDetailModel = gson.fromJson(content, SpeechDetail.class);
            return speechDetailModel != null ? speechDetailModel : null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, "Gson解析错误，未能获取到正确内容?");
            return null;
        }
    }

    // 返回成绩对象ScoreList
    public static ScoreList getScoreList(String content) {
        if (TextUtils.isEmpty(content)) return null;

        try {
            ScoreList scoreList = gson.fromJson(content, ScoreList.class);
            return scoreList != null ? scoreList : null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, "Gson解析错误，未能获取到正确内容?");
            return null;
        }
    }

    // 返回考试安排对象ExamList
    public static ExamList getExamList(String content) {
        if (TextUtils.isEmpty(content)) return null;

        try {
            ExamList examList = gson.fromJson(content, ExamList.class);
            return examList != null ? examList : null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, "Gson解析错误，未能获取到正确内容?");
            return null;
        }
    }

    // 返回考试安排对象CourseList
    public static CourseList getCourseList(String content) {
        if (TextUtils.isEmpty(content)) return null;

        try {
            CourseList courseList = gson.fromJson(content, CourseList.class);
            return courseList != null ? courseList : null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, "Gson解析错误，未能获取到正确内容?");
            return null;
        }
    }

    // 返回轮播图详情对象BannerDetail
    public static BannerDetail getBannerDetail(String content) {
        if (TextUtils.isEmpty(content)) return null;

        try {
            BannerDetail bannerDetail = gson.fromJson(content, BannerDetail.class);
            return bannerDetail != null ? bannerDetail : null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, "Gson解析错误，未能获取到正确内容?");
            return null;
        }
    }

    // 返回绑定成功后的status, message, token
    public static String[] getBindInfo(String content) {
        if (TextUtils.isEmpty(content)) return null;

        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        try {
            HashMap<String, String> body = gson.fromJson(content, type);
            String[] bindInfo = {
                    body.get("status"),
                    body.get("message"),
                    body.get("token"),
            };
            return bindInfo;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
