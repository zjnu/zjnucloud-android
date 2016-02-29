package com.ddmax.zjnucloud.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.ddmax.zjnucloud.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author ddMax
 * @since 2015/12/19 20:06
 * 说明：包含WebView的基本Fragment
 */
public class BaseWebFragment extends Fragment implements Serializable{
    protected static final String URL = "url";
    protected static final String IS_PADDING = "isPadding";

    protected String mUrl;
    protected boolean isPadding;

    @Bind(R.id.container) protected RelativeLayout mContainer;
    @Bind(R.id.webView) protected WebView mWebView;
    @Bind(R.id.progress_wheel) protected ProgressWheel mProgressWheel;

    public BaseWebFragment() {
        // 保留空的构造器
    }

    public static BaseWebFragment newInstance(String url, boolean isPadding) {
        BaseWebFragment fragment = new BaseWebFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        args.putBoolean(IS_PADDING, isPadding);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mUrl = savedInstanceState.getString(URL);
            isPadding = savedInstanceState.getBoolean("isPadding", true);
        } else {
            Bundle args = getArguments();
            if (args != null) {
                mUrl = args.getString(URL);
                isPadding = args.getBoolean("isPadding", true);
                Log.d("isPadding", String.valueOf(isPadding));
            }
        }

        // TODO: 缓存
        // TODO: 无图模式

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        ButterKnife.bind(this, view);
        // 设置WebView
        setupWebView();

        if (mUrl != null) {
            mWebView.loadUrl(mUrl);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(URL, mUrl);
    }

    /**
     * WebView参数设置
     */
    @SuppressLint("SetJavaScriptEnabled")
    protected void setupWebView() {
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);

        // 支持通过js打开新的窗口
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebViewClient(new MyWebViewClient());

        // 是否设置网页边距
        if (!isPadding) {
            mContainer.setPadding(0, 0, 0, 0);
        }
    }

    private void setWebViewShown(boolean shown) {
        mWebView.setVisibility(shown ? View.VISIBLE : View.INVISIBLE);
        mProgressWheel.setVisibility(shown ? View.GONE : View.VISIBLE);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if( url.startsWith("http:") || url.startsWith("https:") ) {
                return false;
            }

            // Otherwise allow the OS to handle things like tel, mailto, etc.
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            setWebViewShown(true);
        }
    }
}
