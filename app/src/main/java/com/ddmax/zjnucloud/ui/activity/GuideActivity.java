package com.ddmax.zjnucloud.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.adapter.BasePagerAdapter;
import com.ddmax.zjnucloud.utils.ValuePreference;

/**
 * @author ddMax
 * @since 2014/12/05
 * 说明：APP第一次运行欢迎界面
 */
public class GuideActivity extends Activity {
	 // ViewPager展示引导页内容
	private ViewPager mPager;
	 // 引导页的跳转按钮
	private Button mButton;
	 // 引导页显示内容的View
	private View mPage1, mPage2, mPage3;
	 // 存放显示内容的View
	private List<View> mViews = new ArrayList<View>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ValuePreference value_on = new ValuePreference(this);

        // 判断向导是否已经运行过
		if (value_on.getGuidePosition(this) == false) {
			startActivity(new Intent(this, SplashActivity.class));
			finish();
		} else {
			setContentView(R.layout.activity_guide);
			 // 获取要显示的引导页内容
			mPage1 = LayoutInflater.from(this).inflate(
					R.layout.guide_activity_page1, null);
			mPage2 = LayoutInflater.from(this).inflate(
					R.layout.guide_activity_page2, null);
			mPage3 = LayoutInflater.from(this).inflate(
					R.layout.guide_activity_page3, null);
			findViewById();
			setListener();
			 // 添加View
			mViews.add(mPage1);
			mViews.add(mPage2);
			mViews.add(mPage3);
			 // ViewPager设置适配器
			mPager.setAdapter(new BasePagerAdapter(this, mViews));
			 // 标记引导页已使用
			value_on.saveGuidePosition(this, false);
		}
	}

	/**
	 * 绑定界面UI
	 */
	private void findViewById() {
		mPager = (ViewPager) findViewById(R.id.guide_activity_viewpager);
		mButton = (Button) mPage3.findViewById(R.id.guide_activity_btn);
	}

	/**
	 * UI事件监听
	 */
	private void setListener() {
		 // 跳转按钮监听
		mButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 跳转到主界面
				startActivity(new Intent(GuideActivity.this, MainActivity.class));
				finish();
			}
		});
	}

}
