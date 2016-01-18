package com.ddmax.zjnucloud.model.explore;

import java.io.Serializable;

/**
 * @author ddMax
 * @since 2016/1/13 12:37.
 */
public class Explore implements Serializable{

    public int id;
    public String url;
    public String image_url;
    public String title;
    public String title_plain;
    public String content;
    public String date;

    public Explore() {
    }

    public Explore(int id, String url, String image_url, String title, String title_plain, String content, String date) {
        this.id = id;
        this.url = url;
        this.image_url = image_url;
        this.title = title;
        this.title_plain = title_plain;
        this.content = content;
        this.date = date;
    }
}
