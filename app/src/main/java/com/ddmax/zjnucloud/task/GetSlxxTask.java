package com.ddmax.zjnucloud.task;

import android.content.Context;

import com.ddmax.zjnucloud.util.GsonUtils;
import com.ddmax.zjnucloud.model.news.SlxxList;
import com.ddmax.zjnucloud.model.news.News;
import com.ddmax.zjnucloud.util.RequestUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * @author ddMax
 * @since 2015/01/31 21:13.
 * 说明：由于数理信息学工网已有返回JSON数据新闻，
 * 		因此单独写成异步任务
 */
public class GetSlxxTask extends BaseGetDataTask<LinkedList<News>> {

	public GetSlxxTask(Context mContext, ResponseListener mResponseListener) {
		super(mContext, mResponseListener);
	}

	@Override
	protected LinkedList<News> doInBackground(String... params) {

		if (params.length == 0) {
			return null;
		}

		String newContent;
		LinkedList<News> newsList = new LinkedList<>();

		try {
			newContent = RequestUtil.getString(params[0]);
			SlxxList slxxListModel = GsonUtils.getSlxxList(newContent);

			// 判断获取新闻列表是否成功
			isRefreshSuccess = slxxListModel == null ? false : true;

			/**
			 * 将获取到的数据封装成新闻对象的LinkedList
			 * 注：NewsListModel为从学工网上获取下来的原始对象，
			 *     NewsModel为对NewsListModel再次封装后的对象
			 */
			for (int i = 0; i < slxxListModel.getCount(); i++) {
				News news = new News();
				news.setTitle(slxxListModel.getTitiles().get(i));
				news.setAuthor(slxxListModel.getUsernames().get(i));
				news.setArticleId(slxxListModel.getIds().get(i));
				try {
					news.setDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(slxxListModel.getDates().get(i)));
				} catch (ParseException e) {
					news.setDate(null);
				}

				newsList.add(news);
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
