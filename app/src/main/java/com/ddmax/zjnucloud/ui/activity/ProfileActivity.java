package com.ddmax.zjnucloud.ui.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.model.User;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private ImageView mAvatarView;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 初始化View
        initView();
    }

    private void initView() {
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAvatarView = (ImageView) findViewById(R.id.avatar);

        /**
         * 设置CollapsingToolbarLayout折叠变化监听器，
         * 只有当折叠时显示Toolbar标题
         */
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
                } else if(isShow) {
                    mCollapsingToolbarLayout.setTitle(getString(R.string.title_activity_profile, currentUser.getUsername()));
                    isShow = false;
                }
            }
        });
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
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

    public static class ProfileRecyclerViewAdapter
            extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder> {

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);

            }
        }

        @Override
        public ProfileRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ProfileRecyclerViewAdapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }


}
