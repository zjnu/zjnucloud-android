package com.ddmax.zjnucloud.model;

import java.io.Serializable;

/**
 * @author ddMax
 * @since 2015/01/20 23:23.
 * 说明：新闻内容对象模型
 */
public class NewsDetailModel implements Serializable{

    private String content;
    private long id;
    private int infoGeneralId;
    private String infoGeneralName;
    private int infoTypeId;
    private String infoTypeName;
    private String time;
    private String title;
    private String username;
    private long visit;

    public NewsDetailModel() {
    }

    public NewsDetailModel(String content, long id, int infoGeneralId, String infoGeneralName, int infoTypeId, String infoTypeName, String time, String title, String username, long visit) {
        this.content = content;
        this.id = id;
        this.infoGeneralId = infoGeneralId;
        this.infoGeneralName = infoGeneralName;
        this.infoTypeId = infoTypeId;
        this.infoTypeName = infoTypeName;
        this.time = time;
        this.title = title;
        this.username = username;
        this.visit = visit;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getInfoGeneralId() {
        return infoGeneralId;
    }

    public void setInfoGeneralId(int infoGeneralId) {
        this.infoGeneralId = infoGeneralId;
    }

    public String getInfoGeneralName() {
        return infoGeneralName;
    }

    public void setInfoGeneralName(String infoGeneralName) {
        this.infoGeneralName = infoGeneralName;
    }

    public int getInfoTypeId() {
        return infoTypeId;
    }

    public void setInfoTypeId(int infoTypeId) {
        this.infoTypeId = infoTypeId;
    }

    public String getInfoTypeName() {
        return infoTypeName;
    }

    public void setInfoTypeName(String infoTypeName) {
        this.infoTypeName = infoTypeName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getVisit() {
        return visit;
    }

    public void setVisit(long visit) {
        this.visit = visit;
    }
}
