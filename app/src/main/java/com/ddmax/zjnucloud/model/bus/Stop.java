package com.ddmax.zjnucloud.model.bus;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ddMax
 * @since 2015/10/12 20:49.
 * 说明：百度地图站点类
 */
public class Stop implements Serializable {

    public static final Object[][][] STOP_DATA = {
            {
                    {"content", "00始发站"},
                    {"title", "桃园超市（始发站）"},
                    {"imageOffset", new ImageOffset(-46, 3)},
                    {"position", new LatLng(29.13997, 119.642781)}
            },
            {
                    {"content", "01"},
                    {"title", "商业街口"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.140924, 119.643841)}
            },
            {
                    {"content", "02"},
                    {"title", "杏园公寓"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.13982, 119.646095)}
            },
            {
                    {"content", "03"},
                    {"title", "大学生活动中心"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.14023, 119.647991)}
            },
            {
                    {"content", "04"},
                    {"title", "外语楼路口"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.139457, 119.649401)}
            },
            {
                    {"content", "05"},
                    {"title", "音乐楼"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.138605, 119.647335)}
            },
            {
                    {"content", "06"},
                    {"title", "行政中心西"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.1365, 119.644676)}
            },
            {
                    {"content", "07行知门"},
                    {"title", "校西南门（行知门）"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.135096, 119.64517)}
            },
            {
                    {"content", "08"},
                    {"title", "工行师大支行"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.119387, 119.64296)}
            },
            {
                    {"content", "09"},
                    {"title", "骆家塘"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.11504, 119.653515)}
            },
            {
                    {"content", "10"},
                    {"title", "二中路口"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.128494, 119.650879)}
            },
            {
                    {"content", "11"},
                    {"title", "火车西站"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.119422, 119.642893)}
            },
            {
                    {"content", "12解放西路市广电中心对面"},
                    {"title", "浙师大城区培训中心（解放西路市广电中心对面）"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.115174, 119.652851)}
            },
            {
                    {"content", "13终点站"},
                    {"title", "市中心（终点站）"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.108619, 119.655806)}
            },
            {
                    {"content", "14"},
                    {"title", "人民广场"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.108402, 119.657216)}
            },
            {
                    {"content", "15"},
                    {"title", "工行总行"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.115068, 119.658505)}
            },
            {
                    {"content", "16"},
                    {"title", "北苑"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.122132, 119.659646)}
            },
            {
                    {"content", "17"},
                    {"title", "流湖花园南门"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.129145, 119.655258)}
            },
            {
                    {"content", "18"},
                    {"title", "师大附中"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.129598, 119.653277)}
            },
            {
                    {"content", "19"},
                    {"title", "校东南大门"},
                    {"imageOffset", new ImageOffset(0, 3)},
                    {"position", new LatLng(29.13788, 119.65256)}
            },

    };
    public static final Map<String, Stop> ALL_STOPS;

    static {
        ALL_STOPS = new LinkedHashMap<>();
        for (int i = 0; i < STOP_DATA.length; i++) {
            Stop stop = new Stop(
                    (String) STOP_DATA[i][0][1],
                    (String) STOP_DATA[i][1][1],
                    (ImageOffset) STOP_DATA[i][2][1],
                    (LatLng) STOP_DATA[i][3][1]
            );
            ALL_STOPS.put(String.valueOf(i), stop);
        }
    }

    private String content; // 简介
    private String title; // 站名
    private ImageOffset imageOffset; // ??
    private LatLng position; // 坐标

    public Stop() {
    }

    public Stop(String content, String title, ImageOffset imageOffset, LatLng position) {
        this.content = content;
        this.title = title;
        this.imageOffset = imageOffset;
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImageOffset getImageOffset() {
        return imageOffset;
    }

    public void setImageOffset(ImageOffset imageOffset) {
        this.imageOffset = imageOffset;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "content: " + content + ", title: " + title
                + ", imageOffset: " + imageOffset + ", position: " + position;
    }

    public static class ImageOffset {
        private int width;
        private int height;

        public ImageOffset(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return "ImageOffset{" +
                    "width=" + width +
                    ", height=" + height +
                    '}';
        }
    }
}
