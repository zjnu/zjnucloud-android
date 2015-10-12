package com.ddmax.zjnucloud.model.news;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ddMax
 * @since 2015/10/6 18:34.
 * 说明：基本新闻详情模型
 */
public class BaseNewsDetail implements Serializable {

    protected String title;
    protected String content;

    public BaseNewsDetail() {
    }

    public BaseNewsDetail(long articleId, String title, Date date, String author, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
