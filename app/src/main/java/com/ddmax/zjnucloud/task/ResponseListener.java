package com.ddmax.zjnucloud.task;

/**
 * @author ddMax
 * @since 2015/02/14 15:24.
 * 说明：Task回调接口
 */
public interface ResponseListener<T> {

	public void onPreExecute();

	// TODO: More args
	public void onPostExecute(T result, boolean isRefreshSuccess, boolean isContentSame);

	public void onProgressUpdate(Long value);

	public void onFail(Exception e);

}
