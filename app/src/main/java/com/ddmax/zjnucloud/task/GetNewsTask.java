package com.ddmax.zjnucloud.task;

import android.content.Context;

import com.ddmax.zjnucloud.utils.GsonUtils;
import com.ddmax.zjnucloud.model.NewsListModel;
import com.ddmax.zjnucloud.model.NewsModel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * @author ddMax
 * @since 2015/01/31 21:13.
 * 说明：后台获取新闻列表
 */
public class GetNewsTask extends BaseGetNewsTask<LinkedList<NewsModel>> {

	public GetNewsTask(Context mContext, ResponseListener mResponseListener) {
		super(mContext, mResponseListener);
	}

	@Override
	protected LinkedList<NewsModel> doInBackground(String... params) {

		if (params.length == 0) {
			return null;
		}

		String newContent = null;
		LinkedList<NewsModel> newsList = new LinkedList<>();

		try {
			newContent = getUrl(params[0]);
			NewsListModel mNewsListModel = GsonUtils.getNewsList(newContent);

			// 判断获取新闻列表是否成功
			isRefreshSuccess = mNewsListModel == null ? false : true;

			/**
			 * 将获取到的数据封装成新闻对象的LinkedList
			 * 注：NewsListModel为从学工网上获取下来的原始对象，
			 *     NewsModel为对NewsListModel再次封装后的对象
			 */
			for (int i = 0; i < mNewsListModel.getCount(); i++) {

				NewsModel mNewsModel = new NewsModel();
				mNewsModel.setTitle(mNewsListModel.getTitiles().get(i));
				mNewsModel.setAuthor(mNewsListModel.getUsernames().get(i));
				mNewsModel.setId(mNewsListModel.getIds().get(i));
				try {
					mNewsModel.setDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(mNewsListModel.getDates().get(i)));
				} catch (ParseException e) {
					mNewsModel.setDate(null);
				}

				newsList.add(mNewsModel);
			}

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
