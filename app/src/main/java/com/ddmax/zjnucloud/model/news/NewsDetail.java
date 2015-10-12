package com.ddmax.zjnucloud.model.news;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ddMax
 * @since 2015/01/20 23:23.
 * 说明：新闻内容对象模型
 */
public class NewsDetail extends BaseNewsDetail implements Serializable{

    private long articleId;
    private Date date;
    private String author;
    private String videolink;

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVideolink() {
        return videolink;
    }

    public void setVideolink(String videolink) {
        this.videolink = videolink;
    }

    @Override
    public String toString() {
        return new StringBuilder("Id:").append(getArticleId())
                .append("\nDate:").append(getDate())
                .append("\nAuthor:").append(getAuthor())
                .append("\nTitle:").append(getTitle())
                .append("\nContent:").append(getContent()).toString();
    }
}
