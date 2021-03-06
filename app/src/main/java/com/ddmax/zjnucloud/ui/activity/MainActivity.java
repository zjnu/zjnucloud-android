package com.ddmax.zjnucloud.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.ZJNUApplication;
import com.ddmax.zjnucloud.adapter.BasePagerAdapter;
import com.ddmax.zjnucloud.adapter.ModulesViewAdapter;
import com.ddmax.zjnucloud.base.BaseActivity;
import com.ddmax.zjnucloud.base.BaseWebActivity;
import com.ddmax.zjnucloud.model.User;
import com.ddmax.zjnucloud.model.banner.Banner;
import com.ddmax.zjnucloud.ui.fragment.CommonDetailFragment;
import com.ddmax.zjnucloud.util.DensityUtils;
import com.ddmax.zjnucloud.util.RequestUtils;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.update.BmobUpdateAgent;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * @author ddMax
 * @since 2014/12/05.
 * 说明：主界面
 */
public class MainActivity extends BaseActivity implements
        Toolbar.OnMenuItemClickListener, GridView.OnItemClickListener,
        View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "MainActivity";
    // 请求码
    public static final int REQUEST_PROFILE = 0;
    public static final int REQUEST_LOGIN = 1;

    @Bind(R.id.mToolbar) Toolbar mToolbar;
    @Bind(R.id.image_display) ViewPager mImageDisplay; // 主界面图片展示
    @Bind(R.id.gridView) GridView mGridView; // GridView展示模块
    @Bind(R.id.mDrawerLayout) DrawerLayout mDrawerLayout; // 导航抽屉
    @Bind(R.id.left_navdrawer) NavigationView mNavigationView; // 导航视图
    @Bind(R.id.footer_exit) LinearLayout mDrawerExitBtn;
    @Bind(R.id.footer_about) LinearLayout mDrawerAboutBtn;
    @Bind(R.id.footer_feedback) LinearLayout mDrawerFeedbackBtn;
    private BottomBar mBottomBar;

    // 轮播页视图集合
    private List<View> mDisplayViews = new ArrayList<>();
    // 轮播Handler
    private AutoRoundHandler mHandler = new AutoRoundHandler(new WeakReference<>(this));
    // 轮播图片链接地址
    private static List<Banner.Image> mBannerImgs;

    private ActionBarDrawerToggle mDrawerToggle;
    private View mNavigationHeader;
    private CircleImageView mAvatarView;
    // 用于返回键退出计时
    private long exitTime = 0;
    private Toast mToast;
    // 当前登录的用户
    private User currentUser = null;

    // 用户登录处理Handler
    public static class LoginHandler extends Handler {
        private WeakReference<MainActivity> mActivity;

        public LoginHandler(MainActivity mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MainActivity activity = mActivity.get();
            switch (msg.what) {
                case Constants.MSG_LOGIN_SUCCESS:
                    // 登录成功后，显示用户信息
                    activity.mDrawerLayout.openDrawer(GravityCompat.START);
                    activity.updateUsername();
                    break;
                case Constants.MSG_LOGOUT_SUCCESS:
                    activity.onLogout();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化 Bmob SDK
        Bmob.initialize(this, Constants.BMOB_APPID);
        Log.d(TAG, "Bmob SDK initialized!");
        // 初始化界面
        initView(savedInstanceState);
        // 设置登录消息处理Handler
        ZJNUApplication application = ZJNUApplication.getInstance();
        application.setLoginHandler(new LoginHandler(this));
        // 初始化更新代理
        initUpdate();
        // 初始化推送服务
        initPush();
    }

    // Bmob更新代理
    private void initUpdate() {
        BmobUpdateAgent.update(this);
        BmobUpdateAgent.setUpdateOnlyWifi(false);
//        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
//                @Override
//            public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {
//                if (updateStatus == UpdateStatus.Yes) {
//                    Toast.makeText(MainActivity.this, "有更新啦！！！", Toast.LENGTH_SHORT).show();
//                } else if (updateStatus == UpdateStatus.No) {
//                    Toast.makeText(MainActivity.this, "无更新", Toast.LENGTH_SHORT).show();
//                } else if (updateStatus == UpdateStatus.EmptyField) {
//                    Toast.makeText(MainActivity.this, "请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。", Toast.LENGTH_SHORT).show();
//                }else if(updateStatus==UpdateStatus.IGNORED){
//                    Toast.makeText(MainActivity.this, "该版本已被忽略更新", Toast.LENGTH_SHORT).show();
//                }else if(updateStatus==UpdateStatus.ErrorSizeFormat){
//                    Toast.makeText(MainActivity.this, "请检查target_size填写的格式，请使用file.length()方法获取apk大小。", Toast.LENGTH_SHORT).show();
//                }else if(updateStatus==UpdateStatus.TimeOut){
//                    Toast.makeText(MainActivity.this, "查询出错或查询超时", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    // Bmob推送服务
    private void initPush() {
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        Log.d(TAG, "_InstallationId: " + BmobInstallation.getInstallationId(this));
        BmobPush.startWork(this);
    }

    // 实现再按一次返回键退出的功能
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) >= 2000) {
            mToast = Toast.makeText(MainActivity.this, getResources().getString(R.string.exit_hint), Toast.LENGTH_SHORT);
            mToast.show();
            exitTime = System.currentTimeMillis();
        } else {
            // 取消Toast显示
            mToast.cancel();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        updateUsername();
        updateNavBackground();
        updateNotification();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PROFILE:
                    updateAvatar();
                    break;
                case REQUEST_LOGIN:
                    // do nothing
                default:
                    break;
            }
        }
    }

    /**
     * 初始化界面
     */
    private void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        // 初始化Toolbar，设置Toolbar菜单
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//		mToolbar.setNavigationIcon(R.drawable.ic_action_search);
        mToolbar.setOnMenuItemClickListener(this);

        // 从网络获取轮播图
        requestBanner();
        // 展示图片
        setUpBanner();

        // 初始化GridView
        mGridView.setAdapter(new ModulesViewAdapter(this));
        mGridView.setOnItemClickListener(this);

        // 初始化底部栏
        setUpBottomBar(savedInstanceState);

        // 设置左边抽屉图标
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.app_name, R.string.app_name);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.white));
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        // 绑定登录，注册按钮事件
        mNavigationHeader = mNavigationView.inflateHeaderView(R.layout.navigation_header);
        mNavigationHeader.findViewById(R.id.user_login_button).setOnClickListener(this);
        mNavigationHeader.findViewById(R.id.user_register_button).setOnClickListener(this);

        // 绑定NavigationView菜单项点击事件
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    // 从网络获取轮播图
    private void requestBanner() {
        // 设置缓存
//        RequestUtils.setCacheInterceptor(this, 1);
        // 请求数据
        RequestUtils.getBanner(new Callback<Banner>() {
            @Override
            public void onResponse(Response<Banner> response, Retrofit retrofit) {
                mBannerImgs = response.body().result;
                // 请求成功后重新设置轮播图
                setUpBanner();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    // 设置首页轮播图片
    private void setUpBanner() {
        // 设置自适应ViewPager高度
        int width = DensityUtils.getWidth(this);
        int height = (int) (width / Constants.DISPLAY_SCALE);
        mImageDisplay.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        // ViewPager添加图片资源
        if (mBannerImgs == null || mBannerImgs.size() == 0) {
            ImageView mImageView = new ImageView(this);
            mImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.display_banner_default));
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mDisplayViews.add(mImageView);
        } else {
            // 移除原有的轮播页
            mDisplayViews.clear();
            // 添加mBannerImgs到轮播页
            for (final Banner.Image bannerImg : mBannerImgs) {
                ImageView mImageView = new ImageView(this);
                mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseWebActivity.actionStart(MainActivity.this, getString(R.string.detail),
                                CommonDetailFragment.initWithUrl(bannerImg.href+"?json=1", "page", "content"));
                    }
                });
                Picasso.with(this).load(bannerImg.image).fit().centerInside().into(mImageView);
                mDisplayViews.add(mImageView);
            }
        }
        // 设置Adapter
        mImageDisplay.setAdapter(new BasePagerAdapter(this, mDisplayViews));

        // 设置ViewPager.OnPageChangeListener
        mImageDisplay.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            // 配合Adapter的currentItem字段进行设置
            @Override
            public void onPageSelected(int position) {
                mHandler.sendMessage(Message.obtain(mHandler, AutoRoundHandler.MSG_PAGE_CHANGED, position, 0));
            }

            // 重写该方法实现自动循环播放效果的暂停和恢复
            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        mHandler.sendEmptyMessage(AutoRoundHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        mHandler.sendEmptyMessageDelayed(AutoRoundHandler.MSG_UPDATE_IMAGE, AutoRoundHandler.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });
        // 开始自动循环播放
        mHandler.sendEmptyMessageDelayed(AutoRoundHandler.MSG_UPDATE_IMAGE, AutoRoundHandler.MSG_DELAY);

        // 设置ViewPager滑动动画效果

    }

    // 初始化BottomBar
    private void setUpBottomBar(Bundle savedInstanceState) {
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.menu_bottombar, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

    }

//    TODO/**
//     * 获取轮播图详情页任务
//     */
//    private static class GetBannerDetailTask extends BaseGetDataTask<BannerDetail> {
//
//        public GetBannerDetailTask(Context mContext, ResponseListener mResponseListener) {
//            super(mContext, mResponseListener);
//        }
//
//        @Override
//        protected BannerDetail doInBackground(String... params) {
//            if (params.length == 0) {
//                return null;
//            }
//            String newContent;
//            BannerDetail bannerDetail = null;
//            try {
//                newContent = RequestUtils.get(params[0]);
//                bannerDetail = GsonUtils.getBannerDetail(newContent);
//            } catch (IOException e) {
//                e.printStackTrace();
//                this.isRefreshSuccess = false;
//                this.e = e;
//            }
//            return bannerDetail == null ? null : bannerDetail;
//        }
//    }

    // TODO: onStart()时更新用户信息
    private void updateUsername() {
        currentUser = BmobUser.getCurrentUser(this, User.class);
        if (currentUser != null) {
            Log.d(TAG, currentUser.toString());
            onLogin();
        } else {
            Log.d(TAG, "本地用户为null!");
        }
    }

    // TODO: onStart()时更新抽屉背景
    private void updateNavBackground() {

    }

    // TODO: onStart()时更新通知
    private void updateNotification() {

    }

    // 用户登录成功后的动作
    private void onLogin() {
        // 隐藏按钮
        mNavigationHeader.findViewById(R.id.user_login_button).setVisibility(View.INVISIBLE);
        mNavigationHeader.findViewById(R.id.user_register_button).setVisibility(View.INVISIBLE);
        // 显示登陆信息
        TextView welcomeText = (TextView) mNavigationHeader.findViewById(R.id.welcome_message);
        welcomeText.setText(currentUser.getUsername());
        welcomeText.setVisibility(View.VISIBLE);
        // 更改Drawer头像
        mAvatarView = (CircleImageView) mNavigationHeader.findViewById(R.id.avatar);
        // 设置头像点击事件
        mAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivityForResult(intent, REQUEST_PROFILE);
            }
        });
        if (currentUser != null) {
            BmobFile avatarFile = currentUser.getAvatar();
            if (avatarFile == null) {
                mAvatarView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.avatar_default));
            } else {
                Picasso.with(this).load(Constants.BMOB_FILE_LINK + avatarFile.getUrl()).into(mAvatarView);
            }
            // 设置NavigationView header背景
            mNavigationHeader.setBackgroundResource(R.drawable.drawer_background);
        }
    }

    // 用户登出后的动作
    private void onLogout() {
        // 显示按钮
        mNavigationHeader.findViewById(R.id.user_login_button).setVisibility(View.VISIBLE);
        mNavigationHeader.findViewById(R.id.user_register_button).setVisibility(View.VISIBLE);
        // 设置空头像
        mAvatarView.setImageResource(R.drawable.avatar_empty);
//        Picasso.with(this).load(R.drawable.avatar_empty).into(mAvatarView);
        mAvatarView.setOnClickListener(null);
        // 隐藏登录信息文字，去除背景
        mNavigationHeader.findViewById(R.id.user_account_view).setBackgroundResource(R.color.material_blue);
        mNavigationHeader.findViewById(R.id.welcome_message).setVisibility(View.GONE);
        // 设置currentUser=null
        currentUser = null;
        // 弹出成功Toast
        Toast.makeText(MainActivity.this, getString(R.string.logout_success), Toast.LENGTH_LONG).show();
    }

    /**
     * 更新左侧抽屉用户头像
     */
    private void updateAvatar() {
        if (BmobUser.getCurrentUser(this) != null) {
            Picasso.with(this).load(Constants.BMOB_FILE_LINK + currentUser.getAvatar().getUrl()).into(mAvatarView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 填充Toolbar上的菜单项
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    /**
     * Toolbar菜单点击监听事件
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            default:
                break;
        }
        return false;
    }

    /**
     * Handler实现MainActivity中展示图片的自动循环播放
     */
    private static class AutoRoundHandler extends Handler {

        // 请求更新显示的View
        protected static final int MSG_UPDATE_IMAGE = 1;
        // 请求暂停自动循环播放
        protected static final int MSG_KEEP_SILENT = 2;
        // 请求恢复自动循环播放
        protected static final int MSG_BREAK_SILENT = 3;
        /**
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使循环播放的页面出错。
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
         */
        protected static final int MSG_PAGE_CHANGED = 4;
        // 轮播间隔时间
        protected static final int MSG_DELAY = 5000;

        // 使用弱引用避免Handler泄漏
        private WeakReference<MainActivity> mWeakReference;
        // 记录当前页面
        private int currentItem = 0;

        public AutoRoundHandler(WeakReference<MainActivity> mWeakReference) {
            this.mWeakReference = mWeakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MainActivity activity = mWeakReference.get();
            // Activity已经回收，无需更新UI
            if (activity == null) {
                return;
            }

            /**
             *  检查消息队列并移除未发送的信息
             *  主要是为了避免在复杂环境下消息出现重复等问题
             */
            if (activity.mHandler.hasMessages(MSG_UPDATE_IMAGE)) {
                activity.mHandler.removeMessages(MSG_UPDATE_IMAGE);
            }

            // 处理消息事件
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    currentItem = (++currentItem) % (mBannerImgs == null ? 1 : mBannerImgs.size());
                    activity.mImageDisplay.setCurrentItem(currentItem);
                    // 准备下次播放
                    activity.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    break;
                case MSG_BREAK_SILENT:
                    activity.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    currentItem = msg.arg1;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * DrawerLayout的HeaderView中登录注册及底部三个按钮点击事件
     * @param view
     */
    @Override
    @OnClick({R.id.footer_exit, R.id.footer_about, R.id.footer_feedback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_login_button:
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), REQUEST_LOGIN);
                break;
            case R.id.user_register_button:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            case R.id.footer_exit:
                finish();
                break;
            case R.id.footer_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.footer_feedback:
                startActivity(new Intent(MainActivity.this, FeedbackActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * GridView模块点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {

            case Constants.MODULE.NEWS:
                startActivity(new Intent(this, NewsActivity.class));
                break;
            case Constants.MODULE.CALENDAR:
                startActivity(new Intent(this, CalendarActivity.class));
                break;
            case Constants.MODULE.BUS:
                startActivity(new Intent(this, BusActivity.class));
                break;
            case Constants.MODULE.SPEECH:
                startActivity(new Intent(this, SpeechActivity.class));
                break;
            case Constants.MODULE.SCORE:
                startActivity(new Intent(this, ScoreActivity.class));
                break;
            case Constants.MODULE.COURSE:
                startActivity(new Intent(this, CourseActivity.class));
                break;
            case Constants.MODULE.EXAM:
                startActivity(new Intent(this, ExamActivity.class));
                break;
            case Constants.MODULE.LIBRARY:
                BaseWebActivity.actionStart(this,
                        getString(R.string.title_activity_library),
                        Constants.URL.LIBRARY,
                        false
                );
                break;
            case Constants.MODULE.LOGISTICS:
                BaseWebActivity.actionStart(this,
                        getString(R.string.title_activity_logistics),
                        Constants.URL.LOGISTICS,
                        false
                );
                break;
            default:
                break;
        }
    }

    /**
     * NavigationView中菜单项点击事件
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.explore:
                startActivity(new Intent(this, ExploreActivity.class));
                break;
            // TODO: 通知中心
//            case R.id.notification:
//
//                break;
            case R.id.setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
                break;
        }
        return true;
    }
}
