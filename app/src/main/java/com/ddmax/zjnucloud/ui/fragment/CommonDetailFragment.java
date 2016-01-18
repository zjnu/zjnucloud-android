package com.ddmax.zjnucloud.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.base.BaseWebFragment;
import com.ddmax.zjnucloud.util.AssetsUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommonDetailFragment extends BaseWebFragment {
    public static final String CONTENT = "content";

    private String content;

    public CommonDetailFragment() {
        // 保留空的构造器
    }

    public static CommonDetailFragment newInstance(String content) {
        CommonDetailFragment fragment = new CommonDetailFragment();
        Bundle args = new Bundle();
        args.putString(CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            content = args.getString("content");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setupContent();
        return view;
    }

    private void setupContent() {
        if (!isAdded()) {
            return;
        }
        String finalHTML = AssetsUtils.loadText(getActivity(), Constants.TEMPLATE_SPEECH_DETAIL);
        if (finalHTML != null) {
            finalHTML = finalHTML.replace("{content}", content);
        }
        // 加载最终网页
        mWebView.loadDataWithBaseURL(null, finalHTML, "text/html", "UTF-8", null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}
