package com.ddmax.zjnucloud.model.news;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ddMax
 * @since 2015/10/6 18:34.
 * 说明：基本每条新闻对象模型：
 * 		包含：标题、作者、日期、Id
 */
public class BaseNews implements Serializable {

	protected String title;
	protected String author;
	protected Date date;
	protected long articleId;


	public BaseNews() {
	}

	public BaseNews(String title, String author, Date date, long id) {
		this.title = title;
		this.author = author;
		this.date = date;
		this.articleId = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

}
