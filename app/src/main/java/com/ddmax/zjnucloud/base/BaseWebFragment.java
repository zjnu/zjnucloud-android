package com.ddmax.zjnucloud.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ddmax.zjnucloud.R;

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

    protected String mUrl;

    @Bind(R.id.webView) protected WebView mWebView;
    @Bind(R.id.progressBar) protected ProgressBar mProgressBar;

    private OnFragmentInteractionListener mListener;

    public BaseWebFragment() {
        // 保留空的构造器
    }

    public static BaseWebFragment newInstance(String url) {
        BaseWebFragment fragment = new BaseWebFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mUrl = savedInstanceState.getString(URL);
        } else {
            Bundle args = getArguments();
            if (args != null) {
                mUrl = getArguments().getString(URL);
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
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mUrl = url;
            view.loadUrl(url);
            return true;
        }
    }
}
