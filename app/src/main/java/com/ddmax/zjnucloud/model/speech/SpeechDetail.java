package com.ddmax.zjnucloud.model.speech;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ddMax
 * @since 2015/12/24 20:23.
 */
public class SpeechDetail implements Serializable {

    public long id;
    public String title;
    public String date;
    public String content;

    public SpeechDetail(long id, String title, String date, String content) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.content = content;
    }

    @Override
    public String toString() {
        return "SpeechDetail{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
