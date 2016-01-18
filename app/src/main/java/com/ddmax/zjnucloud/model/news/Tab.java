package com.ddmax.zjnucloud.model.news;

import android.os.Parcel;
import android.util.Log;

import com.ddmax.zjnucloud.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tab extends Page {
    public static final String TAG = "Tab";
    private static final String[][] TAB_DATA = {
            {"浙师新闻", Constants.URL.NEWS.ZSXW},
            {"学术动态", Constants.URL.NEWS.XSDT},
            {"通知公告", Constants.URL.NEWS.TZGG},
            {"数理信息学工新闻", Constants.URL.NEWS.SLXX_LIVES},
            {"数理信息通知公告", Constants.URL.NEWS.SLXX_NOTIFICATION},
    };
    public static final Map<String, Tab> ALL_TABS;
    private static final String SEPARATOR = ",";

    static {
        // 获得所有新闻板块Map
        ALL_TABS = new LinkedHashMap<>();
        for (String[] data : TAB_DATA) {
            final String title = data[0];
            final String key = data[1];
            final Tab tab = new Tab(title, key);
            ALL_TABS.put(key, tab);
        }
    }

    private final String mTitle;
    private final String mKey;

    public Tab(String title, String key) {
        mTitle = title;
        mKey = key;
    }

    private Tab(Parcel in) {
        mTitle = in.readString();
        mKey = in.readString();
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getUrl() {
        return mKey;
    }

    public String getKey() {
        return mKey;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mKey);
    }

    public static final Creator<Tab> CREATOR = new Creator<Tab>() {
        public Tab createFromParcel(Parcel source) {
            return new Tab(source);
        }

        public Tab[] newArray(int size) {
            return new Tab[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tab)) return false;
        Tab tab = (Tab) o;
        return (mTitle == null) ? (tab.mTitle == null) : mTitle.equals(tab.mTitle) &&
                (mKey == null) ? (tab.mKey == null) : mKey.equals(tab.mKey);
    }

    @Override
    public int hashCode() {
        return mKey.hashCode();
    }

    /**
     * 获取所有新闻站点
     */
    public static List<Tab> getAllTabs() {

        final ArrayList<Tab> result = new ArrayList<>();
        Iterator it = ALL_TABS.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Tab tab = (Tab) entry.getValue();
            result.add(tab);
        }
        Log.d(TAG, Arrays.toString(result.toArray()));
        return result;
    }

}
