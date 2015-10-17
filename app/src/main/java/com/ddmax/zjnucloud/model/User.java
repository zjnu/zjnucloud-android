package com.ddmax.zjnucloud.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * @author ddMax
 * @since 2015/03/15 18:48.
 * 说明：学生用户模型
 */
public class User extends BmobUser {
    private int identify;
    private BmobFile avatar;

    public User() {
    }

    public int getIdentify() {
        return identify;
    }

    public void setIdentify(int identify) {
        this.identify = identify;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "用户信息：objectId = " + getObjectId()
                + ", name = " + getUsername()
                + ", email = " + getEmail();
    }
}
