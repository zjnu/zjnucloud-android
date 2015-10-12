package com.ddmax.zjnucloud.task;

import android.content.Context;

import com.ddmax.zjnucloud.model.news.News;
import com.ddmax.zjnucloud.model.news.NewsList;
import com.ddmax.zjnucloud.model.news.SlxxList;
import com.ddmax.zjnucloud.util.GsonUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * @author ddMax
 * @since 2015/01/31 21:13.
 * 说明：后台获取浙师新闻列表
 */
public class GetNewsTask extends BaseGetNewsTask<LinkedList<News>> {

    public GetNewsTask(Context mContext, ResponseListener mResponseListener) {
        super(mContext, mResponseListener);
    }

    @Override
    protected LinkedList<News> doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        String newContent;
        LinkedList<News> newsList = null;

        try {
            newContent = getUrl(params[0]);
            NewsList newsListModel = GsonUtils.getNewsList(newContent);
            // 真正返回的新闻集合
            newsList = newsListModel.getResult();

            // 判断获取新闻列表是否成功
            isRefreshSuccess = newsListModel == null ? false : true;

        } catch (IOException e) {
            e.printStackTrace();

            this.isRefreshSuccess = false;
            this.e = e;
        }

//		isContentSame = checkIsContentSame(oldContent, newContent);

        if (isRefreshSuccess && !isContentSame) {
            // TODO
        }

        return newsList;
    }

}
