package com.ddmax.zjnucloud.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.base.BaseWebFragment;
import com.ddmax.zjnucloud.model.speech.SpeechDetail;
import com.ddmax.zjnucloud.task.BaseGetDataTask;
import com.ddmax.zjnucloud.task.ResponseListener;
import com.ddmax.zjnucloud.util.AssetsUtils;
import com.ddmax.zjnucloud.util.GsonUtils;
import com.ddmax.zjnucloud.util.RequestUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.ButterKnife;

/**
 * @author ddMax
 * @since 2015/02/13
 * 说明：新闻详情页Fragment
 */
public class SpeechDetailFragment extends BaseWebFragment implements ResponseListener<SpeechDetail> {

    public static final String TAG = SpeechDetailFragment.class.getSimpleName();

    public SpeechDetailFragment() {
        // 保留空的构造器
    }

    public static SpeechDetailFragment newInstance(String url) {
        SpeechDetailFragment fragment = new SpeechDetailFragment();
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
                mUrl = args.getString(URL);
            }
        }

        // 后台获取新闻详情
        new GetSpeechDetailTask(getActivity(), this).execute(mUrl);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        ButterKnife.bind(this, view);
        // 设置WebView
        setupWebView();
        return view;
    }

    public static class GetSpeechDetailTask extends BaseGetDataTask<SpeechDetail> {

        public GetSpeechDetailTask(Context mContext, ResponseListener mResponseListener) {
            super(mContext, mResponseListener);
        }

        @Override
        protected SpeechDetail doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String detailContent;
            SpeechDetail speechDetailModel = null;

            try {
                detailContent = RequestUtils.get(params[0]);
                speechDetailModel = GsonUtils.getSpeechDetail(detailContent);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return speechDetailModel;
        }
    }

    /**
     * 设置WebView内容
     *
     * @param speechDetail 讲座详情模型类
     */
    private void setWebView(SpeechDetail speechDetail) {
        if (!isAdded()) {
            return;
        }
        String finalHTML = AssetsUtils.loadText(getActivity(), Constants.TEMPLATE_SPEECH_DETAIL);
        if (finalHTML != null) {
            finalHTML = finalHTML.replace("{content}", speechDetail.content);
        }
        // 加载最终网页
        mWebView.loadDataWithBaseURL(null, finalHTML, "text/html", "UTF-8", null);
    }

    private void setWebViewShown(boolean shown) {
        mWebView.setVisibility(shown ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(shown ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 以下为ResponseListener回调方法
     */
    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(SpeechDetail result, boolean isRefreshSuccess, boolean isContentSame) {
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
