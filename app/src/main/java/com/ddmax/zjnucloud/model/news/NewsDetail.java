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
    private String videocover;

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

    public String getVideocover() {
        return videocover;
    }

    public void setVideocover(String videocover) {
        this.videocover = videocover;
    }

    @Override
    public String toString() {
        return "NewsDetail{" +
                "articleId=" + articleId +
                ", date=" + date +
                ", author='" + author + '\'' +
                ", videolink='" + videolink + '\'' +
                ", videocover='" + videocover + '\'' +
                '}';
    }
}
