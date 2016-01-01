package com.ddmax.zjnucloud.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

/**
 * @author ddMax
 * @since 2015/01/26 13:24.
 * 说明：异步载入请求数据基类
 */
public abstract class BaseGetDataTask<T> extends AsyncTask<String, Long, T> {

	protected Context mContext = null;
	protected ResponseListener mResponseListener = null;
	protected Exception e = null;
	protected boolean isRefreshSuccess = true;
	protected boolean isContentSame = false;


	public BaseGetDataTask(Context mContext, ResponseListener mResponseListener) {
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

}
