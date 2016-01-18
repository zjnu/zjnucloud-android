package com.ddmax.zjnucloud.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.ZJNUApplication;
import com.ddmax.zjnucloud.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {

    @Bind(R.id.bottom_version) TextView mBottomVersion;
    @Bind(R.id.toolbar_back) ImageView mToolbarImage;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 设置显示App版本号
        mBottomVersion.setText(getString(R.string.app_version, ZJNUApplication.getVersionName(this)));
        // TODO: 加载gif
        Glide.with(this).load(R.drawable.about).into(mToolbarImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_contact:
                showContacts();
            default:
                break;
        }
        return true;
    }

    private void showContacts() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.about_contact))
                .setItems(R.array.contacts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        Uri uri;
                        switch (which) {
                            case 0:
                                uri = Uri.parse("mailto:ddmax@zjnucloud.com");
                                intent = new Intent(Intent.ACTION_SENDTO, uri);
                                startActivity(intent);
                                break;
                            case 1:
                            uri = Uri.parse("mailto:leo@zjnucloud.com");
                            intent = new Intent(Intent.ACTION_SENDTO, uri);
                            startActivity(intent);
                            break;
                            case 2:
                            uri = Uri.parse("mailto:michaelzhu@zjnucloud.com");
                            intent = new Intent(Intent.ACTION_SENDTO, uri);
                            startActivity(intent);
                            break;
                            default:
                                break;
                        }
                    }
                }).create().show();
    }
}
