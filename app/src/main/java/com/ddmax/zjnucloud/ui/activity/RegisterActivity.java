package com.ddmax.zjnucloud.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.base.BaseActivity;
import com.ddmax.zjnucloud.model.User;
import com.ddmax.zjnucloud.util.RegexUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author ddMax
 * @since 2015/03/15 18:30
 * 说明：用户注册界面
 */
public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.username) EditText mUsername;
    @Bind(R.id.password) EditText mPassword;
    @Bind(R.id.email) EditText mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

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

    private void performRegister(final String username, final String email, final String password) {
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, R.string.error_invalid_username, Toast.LENGTH_LONG).show();
            return;
        }
        if (!RegexUtils.matchEmail(email)) {
            Toast.makeText(this, R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, R.string.error_incorrect_password, Toast.LENGTH_LONG).show();
            return;
        }
        final User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setIdentify(0);
        // 设置用户默认头像
//        Bitmap avatarBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_default);
        user.signUp(this, new SaveListener() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this, R.string.register_success, Toast.LENGTH_LONG).show();
                // 自动登录
                user.login(RegisterActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, String errorMsg) {
                        Log.d(TAG, statusCode + "自动登录失败：" + errorMsg);
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                Log.e(TAG, statusCode + "注册失败：" + errorMsg);
                if (statusCode == 202) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.register_fail_username, username), Toast.LENGTH_SHORT).show();
                } else if (statusCode == 203) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.register_fail_email, email), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, getString(R.string.register_fail), Toast.LENGTH_SHORT).show();
                }
            }
        });

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
