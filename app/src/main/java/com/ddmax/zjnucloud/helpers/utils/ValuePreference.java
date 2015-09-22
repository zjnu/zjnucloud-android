package com.ddmax.zjnucloud.helpers.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author ddMax
 * 
 */
public class ValuePreference {

	SharedPreferences sharedPreferences;

	public ValuePreference(Context context) {
		sharedPreferences = context.getSharedPreferences("value_preference",
				Context.MODE_PRIVATE);
	}


     //保存应用退出时的引导界面的状态控制
	public void saveGuidePosition(Context context, boolean pos) {
		sharedPreferences.edit().putBoolean("isStart", pos).commit();
	}

	public boolean getGuidePosition(Context context) {
		return sharedPreferences.getBoolean("isStart", true);
	}

}
