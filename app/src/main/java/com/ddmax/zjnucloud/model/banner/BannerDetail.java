package com.ddmax.zjnucloud.model.banner;

/**
 * @author ddMax
 * @since 2016/1/9 15:12.
 */
public class BannerDetail {

    public String status;
    public Page page;

    public static class Page {
        public int id;
        public String url;
        public String image_url;
        public String title;
        public String title_plain;
        public String content;
        public String date;
    }
}
