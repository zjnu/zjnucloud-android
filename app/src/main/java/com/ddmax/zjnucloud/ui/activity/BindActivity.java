package com.ddmax.zjnucloud.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.ddmax.zjnucloud.base.BaseActivity;
import com.ddmax.zjnucloud.model.EmisUser;
import com.ddmax.zjnucloud.model.User;
import com.ddmax.zjnucloud.util.GsonUtils;
import com.ddmax.zjnucloud.util.RequestUtils;
import com.ddmax.zjnucloud.util.ValuePreference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

public class BindActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.emis_username) EditText mEmisUsername;
    @Bind(R.id.emis_password) EditText mEmisPassword;
    @Bind(R.id.confirm_bind_button) Button mBindButton;

    private ProgressDialog bindingDialog;
    private ValuePreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        ButterKnife.bind(this);
        // 获取SharedPreference
        preference = new ValuePreference(this);

        initView();

        mBindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBind();
            }
        });
    }

    /**
     * 进行绑定
     */
    private void doBind() {
        // 检查用户输入信息
        String username = mEmisUsername.getText().toString().trim();
        String password = mEmisPassword.getText().toString().trim();
        String bmob = (String) BmobUser.getObjectByKey(this, "username");
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            Map<String, String> data = new HashMap<>();
            data.put("username", username);
            data.put("password", password);
            data.put("bmob", bmob);
            new BindTask().execute(Constants.URL.EMIS.BIND, data);
        }
    }

    /**
     * 绑定成功后续操作
     */
    private void finishBind() {
        Toast.makeText(this, getString(R.string.bind_success), Toast.LENGTH_SHORT).show();
        // 保存绑定状态
        preference.saveEmisBind(true);
        setResult(RESULT_OK);
        finish();
    }

    private void initView() {
        mToolbar.setTitle(R.string.title_activity_bind);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
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

    /**
     * 绑定教务账号异步任务
     */
    public class BindTask extends AsyncTask<Object, Void, String[]> {

        @Override
        protected void onPreExecute() {
            bindingDialog = new ProgressDialog(BindActivity.this);
            bindingDialog.setMessage(getString(R.string.binding_info));
            bindingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            bindingDialog.show();
        }

        @Override
        protected String[] doInBackground(Object... params) {
            String content;
            String[] bindInfo = null;
            try {
                content = RequestUtils.post((String) params[0], (Map<String, String>) params[1]);
                bindInfo = GsonUtils.getBindInfo(content);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bindInfo;
        }

        @Override
        protected void onPostExecute(String[] bindInfo) {
            if (bindInfo != null) {
                bindingDialog.cancel();
                int status = Integer.valueOf(bindInfo[0]);
                if (status == 0) {
                    String bmobUsername = BmobUser.getCurrentUser(BindActivity.this, User.class).getUsername();
                    EmisUser emisUser = new EmisUser(
                            mEmisUsername.getText().toString().trim(),
                            mEmisPassword.getText().toString().trim(),
                            bindInfo[2],
                            bmobUsername
                    );
                    // 调用ActiveAndroid的save()方法保存对象
                    emisUser.save();
                    finishBind();
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(BindActivity.this)
                            .setMessage(bindInfo[1])
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .create();
                    dialog.show();
                }
            } else {
                // bindInfo为空，请求超时
                bindingDialog.cancel();
                Toast.makeText(BindActivity.this, getString(R.string.emis_request_timeout), Toast.LENGTH_LONG).show();
            }
        }
    }
}
