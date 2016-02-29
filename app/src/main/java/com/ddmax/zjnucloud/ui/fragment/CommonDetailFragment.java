package com.ddmax.zjnucloud.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.base.BaseWebFragment;
import com.ddmax.zjnucloud.task.BaseGetDataTask;
import com.ddmax.zjnucloud.task.ResponseListener;
import com.ddmax.zjnucloud.util.AssetsUtils;
import com.ddmax.zjnucloud.util.RequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author ddMax
 * @since 2016/2/5 23:38
 * 说明：公共详情页Fragment，接受展示html内容或请求json响应的详情
 */
public class CommonDetailFragment extends BaseWebFragment {
    public static final String TAG = CommonDetailFragment.class.getSimpleName();
    public static final String CONTENT = "content";
    public static final String CONTENT_KEY = "key";

    // For initWithContent
    private String mContent;
    // For initWithUrl
    private String[] mContentKeys;

    public CommonDetailFragment() {
        // 保留空的构造器
    }

    public static CommonDetailFragment initWithContent(String content) {
        CommonDetailFragment fragment = new CommonDetailFragment();
        Bundle args = new Bundle();
        args.putString(CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    public static CommonDetailFragment initWithUrl(String url, String... contentKeys) {
        CommonDetailFragment fragment = new CommonDetailFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        args.putStringArray(CONTENT_KEY, contentKeys);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mContent = args.getString(CONTENT);
            mUrl = args.getString(URL);
            mContentKeys = args.getStringArray(CONTENT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (mContent != null) {
            setupContent();
        } else if (mUrl != null && !TextUtils.isEmpty(mUrl)) {
            // 合并参数为一个数组
            String[] taskArgs = new String[1 + mContentKeys.length];
            taskArgs[0] = mUrl;
            System.arraycopy(mContentKeys, 0, taskArgs, 1, mContentKeys.length);
            new GetDetailTask(getActivity(), new ResponseListener<String>() {
                @Override
                public void onPreExecute() {
                }

                @Override
                public void onPostExecute(String result, boolean isRefreshSuccess, boolean isContentSame) {
                    mContent = result;
                    setupContent();
                }

                @Override
                public void onProgressUpdate(Long value) {
                }

                @Override
                public void onFail(Exception e) {
                }
            }).execute(taskArgs);
        }
        return view;
    }

    private void setupContent() {
        if (!isAdded()) {
            return;
        }
        String finalHTML = AssetsUtils.loadText(getActivity(), Constants.TEMPLATE_COMMON_DETAIL);
        if (finalHTML != null) {
            finalHTML = finalHTML.replace("{content}", mContent);
        }
        // 加载最终网页
        mWebView.loadDataWithBaseURL(null, finalHTML, "text/html", "UTF-8", null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {}

    /**
     * 获取详情内容异步任务
     * 注：doInBackground中传入2个参数：
     * --> url        目标地址
     * --> contentKeys... 返回JSON字符串中含有目标内容的键名，有嵌套需逐层传入
     */
    private static class GetDetailTask extends BaseGetDataTask<String> {

        public GetDetailTask(Context mContext, ResponseListener mResponseListener) {
            super(mContext, mResponseListener);
        }

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String responseJson;
            String result = "";
            JSONObject key = null;
            try {
                responseJson = RequestUtils.get(params[0]);
                key = new JSONObject(responseJson);
            } catch (IOException e) {
                e.printStackTrace();
                this.isRefreshSuccess = false;
                this.e = e;
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            // 遍历键名找到content
            if (key != null) {
                for (int i = 1; i < params.length; i++) {
                    try {
                        Object value = key.get(params[i]);
                        if (i == params.length - 1) {
                            result = value.toString();
                        } else {
                            key = (JSONObject) value;
                        }
                    } catch (JSONException jsonException) {
                        if (i == params.length - 1) {
                            Log.e(TAG, "在JSON字符串中无法找到内容");
                            e.printStackTrace();
                        }
                    }
                }
            }
            return result;
        }
    }
}
