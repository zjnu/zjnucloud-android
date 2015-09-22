package com.ddmax.zjnucloud.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * @author ddMax
 * @since 2015/01/26 13:24.
 * 说明：HttpGet获取新闻列表BaseTask
 */
public abstract class BaseGetNewsTask<T> extends AsyncTask<String, Long, T> {

	protected Context mContext = null;
	protected ResponseListener mResponseListener = null;
	protected Exception e = null;
	protected boolean isRefreshSuccess = true;
	protected boolean isContentSame = false;


	public BaseGetNewsTask(Context mContext, ResponseListener mResponseListener) {
		super();
		this.mContext = mContext;
		this.mResponseListener = mResponseListener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (mResponseListener != null) {
			mResponseListener.onPreExecute();
		}
	}

	@Override
	protected void onPostExecute(T response) {
		super.onPostExecute(response);

		if (mResponseListener != null) {
			if (isRefreshSuccess) {
				mResponseListener.onPostExecute(response, isRefreshSuccess, isContentSame);
			} else {
				mResponseListener.onFail(e);
			}
		}
	}

	@Override
	protected void onProgressUpdate(Long... values) {
		super.onProgressUpdate(values);

		if (mResponseListener != null) {
			mResponseListener.onProgressUpdate(values[0]);
		}
	}

	protected boolean checkIsContentSame(String oldContent, String newContent) {

		if (TextUtils.isEmpty(oldContent) || TextUtils.isEmpty(newContent)) {
			return false;
		}

		return oldContent.equals(newContent);
	}

	protected String getUrl(String url) throws IOException {

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		return response.body().string();

	}

}
