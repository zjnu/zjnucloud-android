package com.ddmax.zjnucloud.ui.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.ZJNUApplication;
import com.ddmax.zjnucloud.model.User;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

/**
 * @author ddMax
 * @since 2015/11/13 14:16
 * 说明：个人资料Activity
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.app_bar) AppBarLayout mAppBarLayout;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.avatar) ImageView mAvatarView;
    @Bind(R.id.item_bar_dynamics) LinearLayout mItemBarDynamics;
    @Bind(R.id.item_bar_explore) LinearLayout mItemBarExplore;
    @Bind(R.id.item_bar_notification) LinearLayout mItemBarNotification;
    @Bind(R.id.item_bind_emis) LinearLayout mItemBindEmis;
    @Bind(R.id.item_avatar) LinearLayout mItemChangeAvatar;
    @Bind(R.id.item_password) LinearLayout mItemChangePassword;
    @Bind(R.id.item_logout) LinearLayout mItemLogout;

    private ZJNUApplication application = ZJNUApplication.getInstance();
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        // 初始化View
        initView();
        // 设置CollapsingToolbarLayout位置变化监听器
        setUpCollapsingListener();
    }

    private void initView() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 获得当前用户头像和用户名
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        if (currentUser != null) {
            if (currentUser.getAvatar() == null) {
                mAvatarView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.avatar_default));
            } else {
                Picasso.with(this).load(Constants.BMOB_FILE_LINK + currentUser.getAvatar().getUrl())
                        .into(mAvatarView);
            }
            mCollapsingToolbarLayout.setTitle(getString(R.string.title_activity_profile, currentUser.getUsername()));
        }

        // 设置信息栏的动态、发现、通知及对应数目
        ((TextView) mItemBarDynamics.findViewById(R.id.item_name)).setText(getString(R.string.profile_item_bar_dynamics));
        ((TextView) mItemBarExplore.findViewById(R.id.item_name)).setText(getString(R.string.profile_item_bar_explore));
        ((TextView) mItemBarNotification.findViewById(R.id.item_name)).setText(getString(R.string.profile_item_bar_notification));
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
    @OnClick({R.id.item_bind_emis, R.id.item_avatar, R.id.item_password, R.id.item_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_bind_emis:
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

    private void performLogout() {
        BmobUser.logOut(this);
        finish();
        application.getLoginHandler().sendEmptyMessage(Constants.MSG_LOGOUT_SUCCESS);
    }
}
