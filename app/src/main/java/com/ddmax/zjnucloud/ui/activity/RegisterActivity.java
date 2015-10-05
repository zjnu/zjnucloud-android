package com.ddmax.zjnucloud.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.model.User;
import com.ddmax.zjnucloud.utils.RegexUtils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author ddMax
 * @since 2015/03/15 18:30
 * 说明：用户注册界面
 */
public class RegisterActivity extends AppCompatActivity {

	private Toolbar mToolbar;
	private EditText mUsername;
	private EditText mPassword;
	private EditText mEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		// 初始化 Bmob SDK
		Bmob.initialize(this, Constants.BMOB_APPID);

		// 初始化各组件
		findViewById();

		// 设置Toolbar
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(R.string.title_activity_register);

		// 设置注册按钮监听事件
		Button regButton = (Button) findViewById(R.id.register_new_user);
		regButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				performRegister(mUsername.getText().toString().trim(),
								mEmail.getText().toString().trim(),
								mPassword.getText().toString().trim());
			}
		});
	}

	private void performRegister(String username, String email, String password) {
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, R.string.error_invalid_username, Toast.LENGTH_LONG).show();
			return;
		}
		if (RegexUtils.matchEmail(email)) {
			Toast.makeText(this, R.string.error_invalid_email, Toast.LENGTH_LONG).show();
			return;
		}
		if (password.length() < 6) {
			Toast.makeText(this, R.string.error_incorrect_password, Toast.LENGTH_LONG).show();
			return;
		}
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password);
		user.setIdentify(0);
		user.signUp(this, new SaveListener() {
//			DialogFragment startDialog = new DialogFragment();

			@Override
			public void onStart() {
				super.onStart();
//				startDialog.show(getSupportFragmentManager(), "注册中，请稍候...");
			}

			@Override
			public void onSuccess() {
				Toast.makeText(RegisterActivity.this, R.string.register_success, Toast.LENGTH_LONG).show();
//				new DialogFragment().show(getSupportFragmentManager(), "注册成功！");
			}

			@Override
			public void onFailure(int i, String s) {
				Toast.makeText(RegisterActivity.this, R.string.register_fail, Toast.LENGTH_LONG).show();
//				new DialogFragment().show(getSupportFragmentManager(), "注册失败！");
			}
		});

	}

	private void findViewById() {
		mUsername = (EditText) findViewById(R.id.username);
		mPassword = (EditText) findViewById(R.id.password);
		mEmail = (EditText) findViewById(R.id.email);
		mToolbar = (Toolbar) findViewById(R.id.registerToolbar);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}
}