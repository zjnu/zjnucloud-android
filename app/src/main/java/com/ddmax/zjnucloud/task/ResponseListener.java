package com.ddmax.zjnucloud.task;

/**
 * @author ddMax
 * @since 2015/02/14 15:24.
 * 说明：Task回调接口
 */
public interface ResponseListener<T> {

	void onPreExecute();

	// TODO: More args
	void onPostExecute(T result, boolean isRefreshSuccess, boolean isContentSame);

	void onProgressUpdate(Long value);

	void onFail(Exception e);

}
