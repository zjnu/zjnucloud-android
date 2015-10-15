package com.ddmax.zjnucloud.model.bus;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ddMax
 * @since 2015/10/14 11:27.
 * 说明：百度地图路径折线图类
 */
public class Path implements Serializable {

    // 线路属性
    public static final String[][][] PL_OPTIONS_DATA = {
            {
                    {"strokeColor", "#f00"},
                    {"strokeWeight", "4"},
                    {"strokeOpacity", "0.6"}
            },
            {
                    {"strokeColor", "#00f"},
                    {"strokeWeight", "4"},
                    {"strokeOpacity", "0.6"}
            },
    };
    // 始发线路数据
    public static final LatLng[] PL_PATHS_START_DATA = {
            new LatLng(29.13995, 119.642664),
            new LatLng(29.140885, 119.64265),
            new LatLng(29.14094, 119.643881),
            new LatLng(29.139706, 119.644106),
            new LatLng(29.139859, 119.647007),
            new LatLng(29.140112, 119.647465),
            new LatLng(29.14027, 119.649082),
            new LatLng(29.139323, 119.649477),
            new LatLng(29.138968, 119.647178),
            new LatLng(29.13743, 119.647447),
            new LatLng(29.137052, 119.644591),
            new LatLng(29.13493, 119.645103),
            new LatLng(29.133715, 119.641231),
            new LatLng(29.133613, 119.641294),
            new LatLng(29.134268, 119.643737),
            new LatLng(29.134307, 119.643836),
            new LatLng(29.133384, 119.64433),
            new LatLng(29.132477, 119.644636),
            new LatLng(29.130726, 119.645166),
            new LatLng(29.13004, 119.645327),
            new LatLng(29.128257, 119.645246),
            new LatLng(29.127681, 119.645148),
            new LatLng(29.126309, 119.646351),
            new LatLng(29.12679, 119.647043),
            new LatLng(29.127342, 119.648148),
            new LatLng(29.128391, 119.651112),
            new LatLng(29.128612, 119.651795),
            new LatLng(29.128758, 119.652464),
            new LatLng(29.129058, 119.655078),
            new LatLng(29.129476, 119.659426),
            new LatLng(29.126628, 119.659309),
            new LatLng(29.126565, 119.659309),
            new LatLng(29.126498, 119.659166),
            new LatLng(29.126273, 119.658999),
            new LatLng(29.126214, 119.658622),
            new LatLng(29.126009, 119.656835),
            new LatLng(29.125441, 119.652613),
            new LatLng(29.12481, 119.650852),
            new LatLng(29.124337, 119.650043),
            new LatLng(29.124305, 119.649765),
            new LatLng(29.124345, 119.649424),
            new LatLng(29.124226, 119.649217),
            new LatLng(29.124045, 119.649109),
            new LatLng(29.123808, 119.649118),
            new LatLng(29.123548, 119.648867),
            new LatLng(29.11928, 119.642327),
            new LatLng(29.119178, 119.642417),
            new LatLng(29.123619, 119.649271),
            new LatLng(29.123398, 119.649379),
            new LatLng(29.119643, 119.649055),
            new LatLng(29.115305, 119.650457),
            new LatLng(29.115431, 119.651876),
            new LatLng(29.114216, 119.657194),
            new LatLng(29.112567, 119.656906),
            new LatLng(29.110145, 119.656349),
            new LatLng(29.108047, 119.655622),
    };
    // 回程线路数据
    public static final LatLng[] PL_PATHS_RETURN_DATA = {
            new LatLng(29.108575, 119.655806),
            new LatLng(29.108039, 119.655613),
            new LatLng(29.107913, 119.655644),
            new LatLng(29.107372, 119.656767),
            new LatLng(29.107522, 119.656906),
            new LatLng(29.109877, 119.657612),
            new LatLng(29.111127, 119.658002),
            new LatLng(29.114129, 119.658227),
            new LatLng(29.115628, 119.658568),
            new LatLng(29.116421, 119.658793),
            new LatLng(29.117655, 119.659494),
            new LatLng(29.118074, 119.659565),
            new LatLng(29.126238, 119.659601),
            new LatLng(29.129496, 119.659664),
            new LatLng(29.128833, 119.653232),
            new LatLng(29.133108, 119.653035),
            new LatLng(29.134047, 119.652927),
            new LatLng(29.136295, 119.652433),
            new LatLng(29.137296, 119.652047),
            new LatLng(29.137478, 119.651939),
            new LatLng(29.137769, 119.652648),
            new LatLng(29.138377, 119.652693),
            new LatLng(29.138266, 119.651481),
            new LatLng(29.138463, 119.650609),
            new LatLng(29.138732, 119.650088),
            new LatLng(29.139299, 119.649531),
            new LatLng(29.140309, 119.649046),
            new LatLng(29.140096, 119.64742),
            new LatLng(29.139875, 119.647025),
            new LatLng(29.139623, 119.642668),
            new LatLng(29.139946, 119.642668)
    };

    public static final List<LatLng> ALL_START_PATH;
    public static final List<LatLng> ALL_RETURN_PATH;

    static {
        ALL_START_PATH = new ArrayList<>();
        ALL_RETURN_PATH = new ArrayList<>();
        ALL_START_PATH.addAll(Arrays.asList(PL_PATHS_START_DATA));
        ALL_RETURN_PATH.addAll(Arrays.asList(PL_PATHS_RETURN_DATA));
    }

    private LatLng point;

    public Path() {
    }

    public Path(LatLng point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "Path{" +
                "point=" + point +
                '}';
    }

    public LatLng getPoint() {
        return point;
    }

    public void setPoint(LatLng point) {
        this.point = point;
    }
}
