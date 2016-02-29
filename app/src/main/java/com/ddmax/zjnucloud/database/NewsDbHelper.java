package com.ddmax.zjnucloud.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.model.news.News;
import com.lidroid.xutils.DbUtils;

import java.util.LinkedList;

/**
 * @author ddMax
 * @since 2015/03/05 18:02.
 * 说明：存储新闻列表到数据库
 * 		数据库帮助类
 */
public final class NewsDbHelper {

	private SQLiteDatabase mDatabase;
	private DbUtils mDbUtil;
	private String[] newsColumns = {
			Constants.COLUMN_ID,
			Constants.COLUMN_TITLE,
			Constants.COLUMN_AUTHOR,
			Constants.COLUMN_DATE
	};

	public NewsDbHelper(Context context) {
		mDbUtil = DbUtils.create(context);
		mDatabase = mDbUtil.getDatabase();
	}

	public LinkedList<News> insertNewsList(String id, String title, String author, String date) {
		ContentValues values = new ContentValues();
		values.put(Constants.COLUMN_ID, id);
		values.put(Constants.COLUMN_TITLE, title);
		values.put(Constants.COLUMN_AUTHOR, author);
		values.put(Constants.COLUMN_DATE, date);

		mDatabase.insert(Constants.TABLE_NAME, null, values);
		// TODO: db

		return null;
	}
}
