package com.ddmax.zjnucloud.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.ZJNUApplication;
import com.ddmax.zjnucloud.base.BaseActivity;
import com.ddmax.zjnucloud.model.EmisUser;
import com.ddmax.zjnucloud.model.User;
import com.ddmax.zjnucloud.util.EmisUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * @author ddMax
 * @since 2015/11/13 14:16
 * 说明：个人资料Activity
 */
public class ProfileActivity extends BaseActivity implements
        View.OnClickListener, Callback<String> {
    public static final String TAG = "ProfileActivity";
    public static final String CURRENT_USER = "currentUser";

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.avatar)
    ImageView mAvatarView;
    @Bind(R.id.item_bar_dynamics)
    LinearLayout mItemBarDynamics;
    @Bind(R.id.item_bar_explore)
    LinearLayout mItemBarExplore;
    @Bind(R.id.item_bar_notification)
    LinearLayout mItemBarNotification;
    @Bind(R.id.item_bind_emis)
    LinearLayout mItemBindEmis;
    @Bind(R.id.item_unbind_emis)
    LinearLayout mItemUnbindEmis;
    @Bind(R.id.item_avatar)
    LinearLayout mItemChangeAvatar;
    @Bind(R.id.item_password)
    LinearLayout mItemChangePassword;
    @Bind(R.id.item_logout)
    LinearLayout mItemLogout;

    private ZJNUApplication application = ZJNUApplication.getInstance();
    private User currentUser;
    private EmisUser currentEmisUser;
    private ProgressDialog unbindingDialog;

    public static final int REQUEST_FROM_PROFILE = 1; // ProfileActivity请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        // 获得当前用户头像和用户名
        currentUser = (User) getIntent().getSerializableExtra(CURRENT_USER);
        // 获得当前绑定的教务账号
        currentEmisUser = EmisUtils.getCurrentEmisUser(this);
        // 初始化View
        initView();
        // 设置CollapsingToolbarLayout位置变化监听器
        setUpCollapsingListener();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (currentUser != null) {
            if (currentUser.getAvatar() == null) {
                mAvatarView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.avatar_default));
            } else {
                Picasso.with(this).load(Constants.BMOB_FILE_LINK + currentUser.getAvatar().getUrl())
                        .into(mAvatarView);
            }
            mCollapsingToolbarLayout.setTitle(getString(R.string.title_activity_profile, currentUser.getUsername()));
        }
        // 设置顶栏视图
        setItemBar();
        // 设置选项视图
        setItemEmis();
    }

    /**
     * 刷新视图
     */
    private void refreshView() {
        setItemEmis();
    }

    /**
     * TODO: 设置信息栏的动态、发现、通知及对应数目
     */
    private void setItemBar() {
        ((TextView) mItemBarDynamics.findViewById(R.id.item_name)).setText(getString(R.string.profile_item_bar_dynamics));
        ((TextView) mItemBarDynamics.findViewById(R.id.item_value)).setText("0");
        ((TextView) mItemBarExplore.findViewById(R.id.item_name)).setText(getString(R.string.profile_item_bar_explore));
        ((TextView) mItemBarExplore.findViewById(R.id.item_value)).setText("0");
        ((TextView) mItemBarNotification.findViewById(R.id.item_name)).setText(getString(R.string.profile_item_bar_notification));
        ((TextView) mItemBarNotification.findViewById(R.id.item_value)).setText("0");
    }

    /**
     * 动态设置教务账号绑定状态
     */
    private void setItemEmis() {
        // 获取当前绑定用户
        currentEmisUser = EmisUtils.getCurrentEmisUser(this);
        if (currentEmisUser != null) {
            mItemBindEmis.setVisibility(View.GONE);
            ((TextView) mItemUnbindEmis.findViewById(R.id.tv_unbind_emis)).setText(
                    String.format(getString(R.string.profile_item_unbind_emis), currentEmisUser.username)
            );
            mItemUnbindEmis.setVisibility(View.VISIBLE);
        } else {
            mItemUnbindEmis.setVisibility(View.GONE);
            mItemBindEmis.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置CollapsingToolbarLayout折叠变化监听器，
     * 只有当折叠时显示Toolbar标题
     */
    private void setUpCollapsingListener() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mCollapsingToolbarLayout.setTitle(getString(R.string.title_activity_profile, currentUser.getUsername())
                            + getString(R.string.title_activity_profile_additional));
                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbarLayout.setTitle(getString(R.string.title_activity_profile, currentUser.getUsername()));
                    isShow = false;
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

    @Override
    @OnClick({R.id.item_bind_emis, R.id.item_unbind_emis, R.id.item_avatar
            , R.id.item_password, R.id.item_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_bind_emis:
                startActivityForResult(new Intent(this, BindActivity.class), REQUEST_FROM_PROFILE);
                break;
            case R.id.item_unbind_emis:
                confirmUnbind();
                break;
            case R.id.item_avatar:
                break;
            case R.id.item_password:
                break;
            case R.id.item_logout:
                performLogout();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FROM_PROFILE) {
            refreshView();
        }
    }

    public void performLogout() {
        BmobUser.logOut(this);
        finish();
        application.getLoginHandler().sendEmptyMessage(Constants.MSG_LOGOUT_SUCCESS);
    }

    public void confirmUnbind() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_hint))
                .setMessage(getString(R.string.unbind_warning_info))
                .setPositiveButton(getString(R.string.unbind_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executeUnbind();
                    }
                })
                .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        dialog.show();
    }

    public void executeUnbind() {
        // 弹出进度框
        unbindingDialog = new ProgressDialog(ProfileActivity.this);
        unbindingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        unbindingDialog.setMessage(getString(R.string.unbinding_info));
        unbindingDialog.show();
        EmisUtils.unbind(currentEmisUser.username, currentUser.getUsername(), ProfileActivity.this);
    }

    @Override
    public void onResponse(Response<String> response, Retrofit retrofit) {
        Log.d(TAG, "Unbind request success");
        Log.d(TAG, "Code：" + String.valueOf(response.code()));
        unbindingDialog.cancel();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;
        if (response.code() == 204 || response.code() == 404) {
            // 解绑成功
            dialog = builder.setTitle(getString(R.string.dialog_hint))
                    .setMessage(getString(R.string.unbind_success))
                    .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();

        } else {
            // 请求异常
            dialog = builder.setTitle(getString(R.string.dialog_hint))
                    .setMessage(getString(R.string.unbind_fail))
                    .setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirmUnbind();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
        }
        dialog.show();
        // 清除教务数据库
        EmisUtils.clean(this);
        // 设置列表视图
        setItemEmis();
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(TAG, "Unbind request failed!");
        unbindingDialog.cancel();
        Toast.makeText(ProfileActivity.this, getString(R.string.request_fail), Toast.LENGTH_LONG).show();
    }
}
