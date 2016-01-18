package com.ddmax.zjnucloud.model.banner;

import java.util.List;

/**
 * @author ddMax
 * @since 2016/1/9 15:12.
 */
public class Banner {

    public int count;
    public int total;
    public String previous;
    public String next;
    public List<Image> result;

    public static class Image {
        public int id;
        public String image;
        public String href;
    }
}
