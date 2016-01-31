package com.ddmax.zjnucloud.model;

import cn.bmob.v3.BmobObject;

/**
 * @author ddMax
 * @since 2016/1/12 20:52.
 */
public class FeedBack extends BmobObject {
    //反馈内容
    private String message;
    //用户名
    private String username;
    //联系方式
    private String contact;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
