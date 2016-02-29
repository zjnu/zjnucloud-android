package com.ddmax.zjnucloud.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
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

    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.app_bar) AppBarLayout mAppBarLayout;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.avatar) CircleImageView mAvatarView;
//    @Bind(R.id.item_bar_dynamics) LinearLayout mItemBarDynamics;
//    @Bind(R.id.item_bar_explore) LinearLayout mItemBarExplore;
//    @Bind(R.id.item_bar_notification) LinearLayout mItemBarNotification;
    @Bind(R.id.item_bind_emis) LinearLayout mItemBindEmis;
    @Bind(R.id.item_unbind_emis) LinearLayout mItemUnbindEmis;
    @Bind(R.id.item_avatar) LinearLayout mItemChangeAvatar;
    @Bind(R.id.item_password) LinearLayout mItemChangePassword;
    @Bind(R.id.item_logout) LinearLayout mItemLogout;

    private ZJNUApplication application = ZJNUApplication.getInstance();
    private User currentUser;
    private EmisUser currentEmisUser;
    private Uri mAvatarImageUri; // 更新头像成功后设置的uri
    private ProgressDialog unbindingDialog;
    private AlertDialog updatePwdDialog;

    // 请求码
    public static final int EMIS_BIND = 1;
    public static final int GET_IMAGE_FROM_ALBUM = 2;
    public static final int PERFORM_ZOOM = 3;

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
        setAvatar();
        setItemEmis();
    }

    /**
     * 设置当前界面的头像
     */
    private void setAvatar() {
        if (mAvatarImageUri != null) {
            Picasso.with(this).load(mAvatarImageUri).fit().into(mAvatarView);
        }
    }

    /**
     * TODO: 设置信息栏的动态、发现、通知及对应数目
     */
    private void setItemBar() {
//        ((TextView) mItemBarDynamics.findViewById(R.id.item_name)).setText(getString(R.string.profile_item_bar_dynamics));
//        ((TextView) mItemBarDynamics.findViewById(R.id.item_value)).setText("0");
//        ((TextView) mItemBarExplore.findViewById(R.id.item_name)).setText(getString(R.string.profile_item_bar_explore));
//        ((TextView) mItemBarExplore.findViewById(R.id.item_value)).setText("0");
//        ((TextView) mItemBarNotification.findViewById(R.id.item_name)).setText(getString(R.string.profile_item_bar_notification));
//        ((TextView) mItemBarNotification.findViewById(R.id.item_value)).setText("0");
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
                startActivityForResult(new Intent(this, BindActivity.class), EMIS_BIND);
                break;
            case R.id.item_unbind_emis:
                confirmUnbind();
                break;
            case R.id.item_avatar:
                selectAvatar();
                break;
            case R.id.item_password:
                updatePassword();
                break;
            case R.id.item_logout:
                performLogout();
                break;
            default:
                break;
        }
    }

    private void selectAvatar() {
        // TODO: 拍照作为头像
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GET_IMAGE_FROM_ALBUM);
    }

    private void performAvatarZoom(Uri sourceUri) {
        Uri destUri = Uri.fromFile(new File(getCacheDir(), UUID.randomUUID() + ".jpg"));
        Crop.of(sourceUri, destUri).asSquare().start(this);
    }

    private void uploadAvatar(final Uri uri) {
        // 上传头像
        Log.d(TAG, uri.getPath());
        final BmobFile avatar = new BmobFile(new File(uri.getPath()));
        avatar.uploadblock(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                // 将新头像设置到当前用户
                currentUser.setAvatar(avatar);
                currentUser.update(ProfileActivity.this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ProfileActivity.this, getString(R.string.profile_avatar_upload_success), Toast.LENGTH_SHORT).show();
                        // 返回Uri
                        mAvatarImageUri = uri;
                        // 设置返回结果
                        setResult(RESULT_OK);
                        // 刷新视图
                        refreshView();
                    }

                    @Override
                    public void onFailure(int statusCode, String errorMsg) {
                        Log.d(TAG, "更新失败，错误码：" + statusCode + "，错误信息：" + errorMsg);
                        Toast.makeText(ProfileActivity.this, getString(R.string.profile_avatar_upload_failure), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
                Log.d(TAG, "上传失败，错误码：" + statusCode + "，错误信息：" + errorMsg);
                Toast.makeText(ProfileActivity.this, getString(R.string.profile_avatar_upload_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePassword() {
        View updateView = getLayoutInflater().inflate(R.layout.dialog_update_pwd, null, false);
        final EditText oldPassword = (EditText) updateView.findViewById(R.id.et_username);
        final EditText newPassword = (EditText) updateView.findViewById(R.id.et_password);
        final EditText repeatPassword = (EditText) updateView.findViewById(R.id.et_password_repeat);
        updatePwdDialog = new AlertDialog.Builder(this)
                .setView(updateView)
                .setTitle(R.string.profile_item_password)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String oldPwd = oldPassword.getText().toString();
                        String newPwd = newPassword.getText().toString();
                        String repeatPwd = repeatPassword.getText().toString();
                        if (newPwd.equals(repeatPwd)) {
                            BmobUser.updateCurrentUserPassword(ProfileActivity.this, oldPwd, newPwd, new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(ProfileActivity.this, getString(R.string.profile_update_password_success), Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }

                                @Override
                                public void onFailure(int statusCode, String errorMsg) {
                                    Log.d(TAG, "密码修改失败，错误码：" + statusCode + "，错误信息：" + errorMsg);
                                    Toast.makeText(ProfileActivity.this, getString(R.string.profile_update_password_failure), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(ProfileActivity.this, getString(R.string.profile_update_password_not_match), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        updatePwdDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case EMIS_BIND:
                    refreshView();
                    break;
                case GET_IMAGE_FROM_ALBUM:
                    performAvatarZoom(data.getData());
                    break;
                case Crop.REQUEST_CROP:
                    uploadAvatar(Crop.getOutput(data));
                default:
                    break;
            }
        }
    }

    public void performLogout() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.profile_logout_confirm)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BmobUser.logOut(ProfileActivity.this);
                        // 清除Emis数据
                        EmisUtils.clean(ProfileActivity.this);
                        application.getLoginHandler().sendEmptyMessage(Constants.MSG_LOGOUT_SUCCESS);
                        dialog.cancel();
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    public void confirmUnbind() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_hint))
                .setMessage(getString(R.string.unbind_warning_info))
                .setPositiveButton(getString(R.string.unbind_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        peformUnbind();
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

    public void peformUnbind() {
        // 弹出进度框
        unbindingDialog = new ProgressDialog(ProfileActivity.this);
        unbindingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        unbindingDialog.setMessage(getString(R.string.unbinding_info));
        unbindingDialog.show();
        EmisUtils.unbind(currentEmisUser.username, currentUser.getUsername(), ProfileActivity.this);
    }

    // 以下为Retrofit教务解绑回调实现
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
        Toast.makeText(ProfileActivity.this, getString(R.string.network_fail), Toast.LENGTH_LONG).show();
    }
}
