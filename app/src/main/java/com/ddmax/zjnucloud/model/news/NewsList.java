package com.ddmax.zjnucloud.model.news;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ddMax
 * @since 2015/10/8 20:36.
 * 说明：从API服务端获取到的新闻列表JSON数据
 * http://api.ddmax.gq/news/zsxw/?format=json
 */
public class NewsList implements Serializable{

	private int count; // 当前页的新闻数量，默认为10
	private long total; // 新闻总数
	private String previous; // 前一页新闻地址
	private String next; // 后一页新闻地址
	private LinkedList<News> result; // 新闻对象集合

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public LinkedList<News> getResult() {
		return result;
	}

	public void setResult(LinkedList<News> result) {
		this.result = result;
	}
}
