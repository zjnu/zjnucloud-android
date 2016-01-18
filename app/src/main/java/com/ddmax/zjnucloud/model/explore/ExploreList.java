package com.ddmax.zjnucloud.model.explore;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddMax
 * @since 2016/1/13 12:37.
 */
public class ExploreList implements Serializable{

    public String status;
    public int count;
    public int count_total;
    public int pages;
    public List<Explore> posts;

    public ExploreList() {
    }

    public ExploreList(String status, int count, int count_total, int pages, List<Explore> posts) {
        this.status = status;
        this.count = count;
        this.count_total = count_total;
        this.pages = pages;
        this.posts = posts;
    }
}
