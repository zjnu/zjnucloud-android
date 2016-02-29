package com.ddmax.zjnucloud.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.task.BaseGetDataTask;
import com.ddmax.zjnucloud.ui.activity.BindActivity;
import com.ddmax.zjnucloud.ui.activity.LoginActivity;
import com.ddmax.zjnucloud.ui.activity.ProfileActivity;
import com.ddmax.zjnucloud.util.EmisUtils;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;

/**
 * @author ddMax
 * @since 2015/12/5 17:30.
 * 说明：与教务系统有关的Activity都继承此类
 */
public abstract class BaseEmisActivity extends BaseActivity {

    public static final int INTENT_LOGIN = 0x01;
    public static final int INTENT_BIND = 0x02;

    @Bind(R.id.toolbar) protected Toolbar mToolbar;
    @Bind(R.id.coordinator) protected CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.fab) protected FloatingActionButton mFab;
    @Bind(R.id.fabProgressCircle) protected FABProgressCircle mFabProgressCircle;
    @Bind(R.id.progress_wheel) ProgressWheel mInitProgress;
    protected Snackbar refreshSnackbar;

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
     * 第一次加载时的动画
     */
    protected void setInitAnimationShown(boolean isShown) {
        mInitProgress.setVisibility(isShown ? View.VISIBLE : View.GONE);
        mFabProgressCircle.setVisibility(isShown ? View.GONE : View.VISIBLE);
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

    /**
     * 开始刷新状态动作：
     * FAB动画 + Snackbar弹出消息
     *
     * @param task BaseGetDataTask子类
     * @param resId 弹出消息资源id
     */
    protected void startRefreshStatus(final BaseGetDataTask task, int resId) {
        // Android 5.0以后才使用FAB rotate animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        animation.setInterpolator(new AccelerateInterpolator());
        mFab.startAnimation(animation);

        // 显示Snackbar
        refreshSnackbar = Snackbar.make(mCoordinatorLayout, resId, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        refreshSnackbar.show();
        // 设置FAB点击事件为取消刷新
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFabProgressCircle.hide();
                cancelRefreshStatus(task, R.string.refresh_cancelled);
            }
        });
    }

    /**
     * 取消刷新状态动作
     * - 当用户手动取消刷新时调用
     * - 当未能请求到数据时调用
     * @param task BaseGetDataTask子类
     * @param resId 提示消息
     */
    protected void cancelRefreshStatus(final BaseGetDataTask task, int resId) {
        stopRefreshStatus(task);
        refreshSnackbar = Snackbar.make(mCoordinatorLayout, resId, Snackbar.LENGTH_SHORT)
                .setAction("Action", null);
        refreshSnackbar.show();
    }

    /**
     * 停止刷新状态动作
     * @param task BaseGetDataTask子类
     */
    protected void stopRefreshStatus(final BaseGetDataTask task) {
        // 取消原来的Snackbar消息
        if (refreshSnackbar != null) {
            refreshSnackbar.dismiss();
        }
        if (task != null) {
            task.cancel(true);
        }
        // 清除动画
        if (mFabProgressCircle.getVisibility() != View.GONE) {
            mFabProgressCircle.hide();
        } else {
            setInitAnimationShown(false);
        }
        mFab.clearAnimation();
        // 重新设置点击刷新事件
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFabProgressCircle.show();
                doRefresh();
            }
        });
    }

    protected void finishRefresh() {
        if (refreshSnackbar != null) {
            refreshSnackbar.dismiss();
        }
        // 判断是否第一次加载
        if (mFabProgressCircle.getVisibility() == View.GONE) {
            setInitAnimationShown(false);
        } else {
            mFabProgressCircle.beginFinalAnimation();
        }
        mFab.clearAnimation();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFabProgressCircle.show();
                doRefresh();
            }
        });
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
