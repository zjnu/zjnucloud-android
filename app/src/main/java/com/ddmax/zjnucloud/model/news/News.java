package com.ddmax.zjnucloud.model.news;

import java.io.Serializable;

/**
 * @author ddMax
 * @since 2015/02/12 21:04.
 * 说明：浙师新闻单条的对象模型，包含：
 *      点击量、概览
 */
public class News extends BaseNews implements Serializable{

	private int hits;
	private String overview;

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}
}
