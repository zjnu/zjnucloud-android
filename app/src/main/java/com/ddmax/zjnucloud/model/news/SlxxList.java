package com.ddmax.zjnucloud.model.news;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddMax
 * @since 2015-01-20
 * 说明：这个是从数理信息学工网上获取下来的原始对象，
 *      为了和JSON数据格式字段一一对应
 *      Slxx -> 数理信息
 */
public class SlxxList implements Serializable{

    private int count;
    private List<String> dates;
    private List<Long> ids;
    private List<String> infoGeneralNames;
    private List<String> titiles;
    private long total;
    private long totalPage;
    private List<Integer> typeIds;
    private List<String> usernames;

    public SlxxList() {
    }

    public SlxxList(int count, List<String> dates, List<Long> ids, List<String> infoGeneralNames,
                    List<String> titiles, long total, long totalPage, List<Integer> typeIds,
                    List<String> usernames) {
        this.count = count;
        this.dates = dates;
        this.ids = ids;
        this.infoGeneralNames = infoGeneralNames;
        this.titiles = titiles;
        this.total = total;
        this.totalPage = totalPage;
        this.typeIds = typeIds;
        this.usernames = usernames;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public List<String> getInfoGeneralNames() {
        return infoGeneralNames;
    }

    public void setInfoGeneralNames(List<String> infoGeneralNames) {
        this.infoGeneralNames = infoGeneralNames;
    }

    public List<String> getTitiles() {
        return titiles;
    }

    public void setTitiles(List<String> titiles) {
        this.titiles = titiles;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public List<Integer> getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(List<Integer> typeIds) {
        this.typeIds = typeIds;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

}