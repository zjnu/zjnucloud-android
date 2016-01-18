package com.ddmax.zjnucloud.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.ZJNUApplication;
import com.ddmax.zjnucloud.base.BaseActivity;
import com.ddmax.zjnucloud.model.User;
import com.ddmax.zjnucloud.util.RegexUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author ddMax
 * @since 2015/03/15 16:20
 * 说明：用户登录界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener{
    public static final String TAG = "LoginActivity";
    // 请求码
    public static final int REGISTER_FROM_LOGIN = 0;

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.login_view) LinearLayout mLoginView;
    @Bind(R.id.username) EditText mUsername;
    @Bind(R.id.password) EditText mPassword;
//    @Bind(R.id.login_register) EditText mRegisterLink;
    @Bind(R.id.reset_view) LinearLayout mResetView;
    @Bind(R.id.reset_email) EditText mResetEmail;
    @Bind(R.id.reset_button) Button mResetButton;
    @Bind(R.id.reset_back_login) TextView mResetBackLogin;
//    @Bind(R.id.reset_login_register) TextView mResetRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();

        // 获得Application全局设置
        final ZJNUApplication application = ZJNUApplication.getInstance();

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
                        loginDialog.cancel();
                        Toast.makeText(LoginActivity.this, getString(R.string.login_success, user.getUsername()), Toast.LENGTH_LONG).show();
                        application.getLoginHandler().sendEmptyMessage(Constants.MSG_LOGIN_SUCCESS);
                        // 设置结果码
                        setResult(RESULT_OK);
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void onFailure(int statusCode, String errorMsg) {
                        loginDialog.cancel();
                        Log.d(TAG, statusCode + "登录失败：" + errorMsg);
                        switch (statusCode) {
                            case 101:
                                Toast.makeText(LoginActivity.this, getString(R.string.login_fail_101), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }
        });

    }

    private void initView() {
        // 设置Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_login);
    }

    private void resetPassword() {
        String email = mResetEmail.getText().toString().trim();
        if (RegexUtils.matchEmail(email)) {
            BmobUser.resetPasswordByEmail(this, email, new ResetPasswordByEmailListener() {
                @Override
                public void onSuccess() {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(getString(R.string.success))
                            .setMessage(getString(R.string.reset_success))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setResetViewShown(false);
                                    dialog.cancel();
                                }
                            }).create().show();
                }

                @Override
                public void onFailure(int statusCode, String errorMsg) {
                    Log.d(TAG, statusCode + "找回密码失败：" + errorMsg);
                    Toast.makeText(LoginActivity.this, getString(R.string.reset_fail), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置找回密码视图是否可见
     * @param isShow
     */
    private void setResetViewShown(boolean isShow) {
        mLoginView.setVisibility(isShow ? View.GONE : View.VISIBLE);
        mResetView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mToolbar.setTitle(isShow ? getString(R.string.reset_password) : getString(R.string.title_activity_login));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    @OnClick({R.id.login_register, R.id.reset_login_register,
              R.id.forget_pwd, R.id.reset_back_login, R.id.reset_button})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register:
            case R.id.reset_login_register:
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), REGISTER_FROM_LOGIN);
                break;
            case R.id.forget_pwd:
                setResetViewShown(true);
                break;
            case R.id.reset_back_login:
                setResetViewShown(false);
                break;
            case R.id.reset_button:
                // 找回密码按钮监听
                resetPassword();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REGISTER_FROM_LOGIN:
                    setResult(RESULT_OK);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
