package com.ddmax.zjnucloud.model.speech;

import java.io.Serializable;

/**
 * @author ddMax
 * @since 2015/12/18 21:12.
 */
public class Speech implements Serializable {

    public int id;
    public String title;
    public String date;
    public String href;
    public String pic;
    public String overview;

    public Speech() {}

    public Speech(int id, String title, String date, String href, String pic, String overview) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.href = href;
        this.pic = pic;
        this.overview = overview;
    }

    @Override
    public String toString() {
        return "Speech{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", href='" + href + '\'' +
                ", pic='" + pic + '\'' +
                ", overview='" + overview + '\'' +
                '}';
    }
}
