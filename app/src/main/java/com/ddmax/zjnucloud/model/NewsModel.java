package com.ddmax.zjnucloud.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ddMax
 * @since 2015/02/12 21:04.
 * 说明：对NewsListModel再次封装后的对象
 *      每条新闻的对象模型，包含：
 *      标题、发布人、发布时间
 */
public class NewsModel implements Serializable{

	public static final long serialVersionUID = 1L;

	private String title;
	private String author;
	private Date date;
	private long id;

	public NewsModel() {
	}

	public NewsModel(String title, String author, Date date, long id) {
		this.title = title;
		this.author = author;
		this.date = date;
		this.id = id;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
