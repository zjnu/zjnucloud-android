package com.ddmax.zjnucloud.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.ui.activity.BindActivity;
import com.ddmax.zjnucloud.ui.activity.LoginActivity;
import com.ddmax.zjnucloud.ui.activity.ProfileActivity;
import com.ddmax.zjnucloud.util.EmisUtils;

import cn.bmob.v3.BmobUser;

/**
 * @author ddMax
 * @since 2015/12/5 17:30.
 * 说明：与教务系统有关的Activity都继承此类
 */
public abstract class BaseEmisActivity extends BaseActivity {

    public static final int INTENT_LOGIN = 0x01;
    public static final int INTENT_BIND = 0x02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkPrerequisites()) {
            initView();
            restoreData();
        }
    }

    protected boolean checkPrerequisites() {
        return ensureBmobUserExists() && ensureEmisBinded();
    }

    /**
     * 判断是否已登录Bmob账号
     */
    protected boolean ensureBmobUserExists() {
        if (BmobUser.getCurrentUser(this) == null) {
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.bmob_not_login)
                    .setPositiveButton(R.string.action_to_bmob_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(BaseEmisActivity.this, LoginActivity.class), INTENT_LOGIN);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create();
            dialog.show();
            return false;
        }
        return true;
    }

    /**
     * 判断是否已绑定教务账号
     */
    protected boolean ensureEmisBinded() {
        if (!EmisUtils.isEmisUserBinded(this)) {
            // 数据不存在，未绑定，弹出对话框
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.emis_not_bind))
                    .setPositiveButton(R.string.action_to_emis_bind, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(BaseEmisActivity.this, BindActivity.class), INTENT_BIND);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create();
            dialog.show();
            return false;
        }
        return true;
    }

    /**
     * 当Token不符合时，弹出提示框
     */
    protected void alertInvalidToken() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.emis_token_invalid))
                .setPositiveButton(R.string.action_confirm_rebind, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(BaseEmisActivity.this, ProfileActivity.class);
                        intent.putExtra(ProfileActivity.CURRENT_USER, BmobUser.getCurrentUser(BaseEmisActivity.this));
                        startActivityForResult(intent, INTENT_BIND);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INTENT_LOGIN:
                if (resultCode == RESULT_OK) {
                    ensureEmisBinded();
                } else {
                    finish();
                }
                break;
            case INTENT_BIND:
                if (resultCode == RESULT_OK) {
                    initView();
                    restoreData();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    public abstract void initView();

    /**
     * 从本地数据库中读取数据
     * 若不存在，则执行doRefresh()
     */
    public abstract void restoreData();

    /**
     * 从网络请求数据
     */
    public abstract void doRefresh();
}
