package com.ddmax.zjnucloud.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.utils.AssetsUtils;
import com.ddmax.zjnucloud.model.NewsDetailModel;
import com.ddmax.zjnucloud.task.GetNewsDetailTask;
import com.ddmax.zjnucloud.task.ResponseListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author ddMax
 * @since 2015/02/13
 * 说明：新闻详情页Fragment
 */
public class NewsDetailFragment extends Fragment implements ResponseListener<NewsDetailModel>{

	public static final String ID = "com.ddmax.zjnunews.ui.fragments.NewsDetailFragment.id";
	public static final String TAG = "NewsDetailFragment";

	private WebView mWebView;
	private ProgressBar mProgressBar;

	// 从Activity接收过来的新闻ID
	private long mNewsId = 0;
	private ArrayList<String> mDetailImageList = new ArrayList<>();

	public NewsDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 获得NewsDetailActivity传递过来的新闻ID值
		if (savedInstanceState == null) {
			Bundle bundle = getArguments();
			mNewsId = bundle != null ? bundle.getLong("id") : 0;
		} else {
			mNewsId = savedInstanceState.getLong(ID);
		}

		// TODO: 新闻详情缓存

		// TODO: 无图模式

		// 后台获取新闻详情
		new GetNewsDetailTask(getActivity(), this).execute(String.valueOf(mNewsId));


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// 填充Fragment布局
		View rootView =  inflater.inflate(R.layout.fragment_news_detail, container, false);

		// 设置WebView，进度条
		mWebView = (WebView) rootView.findViewById(R.id.webView);
		mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

		setupWebViewDefaults(mWebView);

		return rootView;
	}

	private void setupWebViewDefaults(WebView mWebView) {

		mWebView.addJavascriptInterface(new JavaScriptObject(getActivity()), "injectedObject");

		// 设置缓存模式
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		mWebView.getSettings().setJavaScriptEnabled(true);

		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);

		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);

		// 支持通过js打开新的窗口
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
			                         final JsResult result) {
				result.cancel();
				return true;
			}

			@Override
			public boolean onJsConfirm(WebView view, String url,
			                           String message, final JsResult result) {

				return true;
			}
		});
	}

	public static class JavaScriptObject {

		private Activity mInstance;

		public JavaScriptObject(Activity instance) {
			mInstance = instance;
		}

	}

	/**
	 * 设置WebView内容
	 * @param finalNewsDetail
	 */
	private void setWebView(NewsDetailModel finalNewsDetail) {

		if (!isAdded()) {
			return;
		}

		// 解析HTML，替换模板中的{title},{author}等，生成最终网页内容
		// 其中发布时间date重新格式化成yyyy.MM.dd HH:mm:ss形式
		String finalHTML = AssetsUtils.loadText(getActivity(), Constants.TEMPLATE_NEWS_DETAIL);
		if (finalHTML != null) {
			try {
				finalHTML = finalHTML.replace("{title}", finalNewsDetail.getTitle())
						.replace("{author}", finalNewsDetail.getUsername())
						.replace("{clickrate}", String.valueOf(finalNewsDetail.getVisit()))
						.replace("{date}", new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(finalNewsDetail.getTime())))
						.replace("{content}", finalNewsDetail.getContent());
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
		mWebView.setVisibility(shown ? View.VISIBLE : View.GONE);
		mProgressBar.setVisibility(shown ? View.GONE : View.VISIBLE);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// 保存新闻ID
		outState.putLong(ID, mNewsId);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPreExecute() {

	}

	@Override
	public void onPostExecute(NewsDetailModel result, boolean isRefreshSuccess, boolean isContentSame) {
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
