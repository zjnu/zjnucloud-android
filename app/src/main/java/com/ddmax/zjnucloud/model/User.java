package com.ddmax.zjnucloud.model;

import cn.bmob.v3.BmobUser;

/**
 * @author ddMax
 * @since 2015/03/15 18:48.
 * 说明：学生用户模型
 */
public class User extends BmobUser {
	private int identify;

	public User() {}

	public int getIdentify() {
		return identify;
	}

	public void setIdentify(int identify) {
		this.identify = identify;
	}
}
