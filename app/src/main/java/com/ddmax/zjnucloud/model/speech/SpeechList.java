package com.ddmax.zjnucloud.model.speech;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddMax
 * @since 2015/12/18 21:07.
 */
public class SpeechList implements Serializable {

    public int count; // 当前页的讲座数量，默认为10
    public long total; // 讲座总数
    public String previous; // 前一页讲座地址
    public String next; // 后一页讲座地址
    public List<Speech> result; // 讲座对象集合

    public SpeechList() {}

    public SpeechList(int count, long total, String previous, String next, List<Speech> result) {
        this.count = count;
        this.total = total;
        this.previous = previous;
        this.next = next;
        this.result = result;
    }

    @Override
    public String toString() {
        return "SpeechList{" +
                "count=" + count +
                ", total=" + total +
                ", previous='" + previous + '\'' +
                ", next='" + next + '\'' +
                ", result=" + result +
                '}';
    }
}
