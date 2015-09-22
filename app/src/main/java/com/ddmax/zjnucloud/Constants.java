package com.ddmax.zjnucloud;

/**
 * @author ddMax
 * @since 2015/02/17 10:55
 * 说明：全局常量
 */
public final class Constants {

	// 各模块代号定义

	public static final class MODULE {
		public static final int NEWS = 0;
		public static final int CALENDAR = 1;
		public static final int BUS = 2;
		public static final int SPEECH = 3;
		public static final int RESOURCES = 4;
	}



	// 数信学工办新闻列表地址
    public static final class URL {

		// 学工新闻
		public static final String NEWS_LIVES = "http://slxx.zjnu.edu.cn/xgb/listIndexInfo.action?index=1&pageSize=10&pageNo=";

		// 信息通告
		public static final String NEWS_NOTIFICATION = "http://slxx.zjnu.edu.cn/xgb/listIndexInfo.action?index=2&pageSize=10&pageNo=";

        // 新闻内容地址
        public static final String NEWSDETAIL = "http://slxx.zjnu.edu.cn/xgb/showInfo.action?id=";
    }

	// 浙师大坐标
	public static final double ZJNU_LONGITUDE = 119.64751;
	public static final double ZJNU_LATITUDE = 29.13876;

	public static final String TEMPLATE_NEWS_DETAIL = "news-templates/template.html";

	// Bmob Application ID
	public static final String BMOB_APPID = "68b1340c9b81500e3fd31f1d14586dc7";

	// 数据库：NewsList
	public static final String DB_NAME = "zjnu_news.db";
	public static final int DB_VERSION = 5;

	public static final String TABLE_NAME = "news_list";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_AUTHOR = "author";
	public static final String COLUMN_DATE = "date";

}