package com.ddmax.zjnucloud.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.ZJNUApplication;
import com.ddmax.zjnucloud.model.User;

import cn.bmob.v3.listener.SaveListener;

/**
 * @author ddMax
 * @since 2015/03/15 16:20
 * 说明：用户登录界面
 */
public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText mUsername;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化View
        initView();

        // 设置Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.loginToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_login);

        // 获得Application全局设置
        final ZJNUApplication application = ZJNUApplication.getInstance();

        // 设置注册新帐号链接
        TextView mRegisterLink = (TextView) findViewById(R.id.login_register);
        mRegisterLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        mRegisterLink.setLinkTextColor(getResources().getColor(R.color.blue));

        // 设置登录按钮动作
        final Button loginButton = (Button) findViewById(R.id.login_in_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog loginDialog = new ProgressDialog(LoginActivity.this);
                loginDialog.setMessage(getString(R.string.login_info));
                loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loginDialog.show();
                final User user = new User();
                user.setUsername(mUsername.getText().toString().trim());
                user.setPassword(mPassword.getText().toString().trim());
                user.login(LoginActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Log.i(TAG, getString(R.string.login_success, user.getUsername()));
                        loginDialog.dismiss();
                        Toast.makeText(LoginActivity.this, getString(R.string.login_success, user.getUsername()), Toast.LENGTH_LONG).show();
                        application.getLoginHandler().sendEmptyMessage(Constants.MSG_LOGIN_SUCCESS);
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        loginDialog.dismiss();
                        Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "登陆失败：" + s);
                    }
                });
            }
        });

    }

    private void initView() {
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
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
