package com.ddmax.zjnucloud.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.base.BaseActivity;
import com.ddmax.zjnucloud.model.FeedBack;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.et_contact) MaterialEditText mContact;
    @Bind(R.id.et_content) MaterialEditText mContent;
    @Bind(R.id.fab) FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mToolbar.setTitle(R.string.title_activity_feedback);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 设置焦点
        mContact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        mContact.requestFocus();
        mContact.requestFocusFromTouch();
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

    @Override
    public void onBackPressed() {
        String contact = mContact.getText().toString().trim();
        String content = mContent.getText().toString().trim();
        if (!TextUtils.isEmpty(contact) || !TextUtils.isEmpty(content)) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.feedback_not_sent))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
            dialog.show();
        } else {
            finish();
        }
    }

    @OnClick({R.id.fab})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                prepareFeedback();
                break;
        }
    }

    /**
     * 检查反馈内容
     */
    private void prepareFeedback() {
        final String contact = mContact.getText().toString().trim();
        final String content = mContent.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            if (!TextUtils.isEmpty(contact)) {
                sendFeedback(contact, content);
            } else {
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.feedback_no_contact))
                        .setPositiveButton(getString(R.string.feedback_action_send_direct), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                sendFeedback(contact, content);
                            }
                        })
                        .setNegativeButton(getString(R.string.feedback_action_rewrite_contact), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create().show();
            }
        } else {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.feedback_no_content))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create().show();
        }
    }


    private void sendFeedback(String contact, String content) {
        FeedBack feedback = new FeedBack();
        feedback.setContact(contact);
        feedback.setMessage(content);
        BmobUser user = BmobUser.getCurrentUser(this);
        if (user != null) {
            feedback.setUsername(user.getUsername());
        }
        feedback.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                Log.i("bmob", "反馈信息已保存到服务器");
                new AlertDialog.Builder(FeedbackActivity.this)
                        .setMessage(getString(R.string.feedback_success))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish();
                            }
                        })
                        .create().show();
            }

            @Override
            public void onFailure(int code, String arg0) {
                Log.e("bmob", "保存反馈信息失败：" + arg0);
                Toast.makeText(FeedbackActivity.this, getString(R.string.feedback_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
