package com.ddmax.zjnucloud.ui.fragment;

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

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.model.news.BaseNewsDetail;
import com.ddmax.zjnucloud.model.news.NewsDetail;
import com.ddmax.zjnucloud.model.news.SlxxDetail;
import com.ddmax.zjnucloud.task.GetNewsDetailTask;
import com.ddmax.zjnucloud.task.GetSlxxDetailTask;
import com.ddmax.zjnucloud.task.ResponseListener;
import com.ddmax.zjnucloud.util.AssetsUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author ddMax
 * @since 2015/02/13
 * 说明：新闻详情页Fragment
 */
public class NewsDetailFragment extends Fragment implements ResponseListener<BaseNewsDetail> {

    public static final String ID = "com.ddmax.zjnunews.ui.fragments.NewsDetailFragment.id";
    public static final String TAG = "NewsDetailFragment";

    @Bind(R.id.webView) WebView mWebView;
    @Bind(R.id.progress_wheel) ProgressWheel mProgressWheel;

    // 从Activity接收过来的新闻ID, isSlxx
    private long mArticleId = 0;
    private boolean isSlxx = false;

    public NewsDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获得NewsDetailActivity传递过来的新闻ID值
        Bundle bundle = getArguments();
        if (savedInstanceState == null) {
            mArticleId = bundle != null ? bundle.getLong("id") : 0;
        } else {
            mArticleId = savedInstanceState.getLong(ID);
        }
        isSlxx = bundle.getBoolean("isSlxx");

        // TODO: 新闻详情缓存
        // TODO: 无图模式

        // 后台获取新闻详情
        if (!getActivity().getIntent().getBooleanExtra("isSlxx", false)) {
            new GetNewsDetailTask(getActivity(), this).execute(
                getActivity().getIntent().getStringExtra("url"),
                String.valueOf(mArticleId)
            );
        } else {
            new GetSlxxDetailTask(getActivity(), this).execute(String.valueOf(mArticleId));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 填充Fragment布局
        View rootView = inflater.inflate(R.layout.fragment_news_detail, container, false);

        // 设置WebView，进度条
        ButterKnife.bind(this, rootView);

        setupWebViewDefaults(mWebView);

        return rootView;
    }

    private void setupWebViewDefaults(WebView mWebView) {

//        mWebView.addJavascriptInterface(new JavaScriptObject(getActivity()), "injectedObject");

        // 设置缓存模式
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);

        // 支持通过js打开新的窗口
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
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
        });
    }

    /**
     * 设置WebView内容
     *
     * @param baseNewsDetail 新闻详情对象
     */
    private void setWebView(BaseNewsDetail baseNewsDetail) {
        if (!isAdded()) {
            return;
        }

        // 解析HTML，替换模板中的{title},{author}等，生成最终网页内容
        // 其中发布时间date重新格式化成yyyy.MM.dd HH:mm:ss形式
        String finalHTML = AssetsUtils.loadText(getActivity(), Constants.TEMPLATE_NEWS_DETAIL);
        if (finalHTML != null) {
            try {
                // 从API服务端获取的新闻详情
                if (!isSlxx) {
                    NewsDetail finalNewsDetail = (NewsDetail) baseNewsDetail;
                    Log.d(TAG, finalNewsDetail.toString());
                    finalHTML = finalHTML.replace("{title}", finalNewsDetail.getTitle())
                            .replace("{author}", finalNewsDetail.getAuthor())
                            .replace("{clickrate}", String.valueOf(getArguments().getInt("hits")))
                            .replace("{date}", new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINESE).format(finalNewsDetail.getDate()))
                            .replace("{content}", finalNewsDetail.getContent());
                } else {
                    SlxxDetail finalNewsDetail = (SlxxDetail) baseNewsDetail;
                    finalHTML = finalHTML.replace("{title}", finalNewsDetail.getTitle())
                            .replace("{author}", finalNewsDetail.getUsername())
                            .replace("{clickrate}", String.valueOf(finalNewsDetail.getVisit()))
                            .replace("{date}", new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.CHINESE).format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(finalNewsDetail.getTime())))
                            .replace("{content}", finalNewsDetail.getContent());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // 加载最终网页
            mWebView.loadDataWithBaseURL(null, finalHTML, "text/html", "UTF-8", null);
        } else {
            Log.e(TAG, "finalHTML is null");
        }

    }

    private void setWebViewShown(boolean shown) {
        mWebView.setVisibility(shown ? View.VISIBLE : View.INVISIBLE);
        mProgressWheel.setVisibility(shown ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 保存新闻ID
        outState.putLong(ID, mArticleId);
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public void onPostExecute(BaseNewsDetail result, boolean isRefreshSuccess, boolean isContentSame) {
        if (isAdded()) {
            if (result != null) {
                setWebView(result);
            }
        }
        setWebViewShown(true);
    }

    @Override
    public void onProgressUpdate(Long value) {

    }

    @Override
    public void onFail(Exception e) {
        setWebViewShown(true);
    }

}
