package com.ddmax.zjnucloud.util;

import android.support.annotation.Nullable;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * @author ddMax
 * @since 2015/11/4 22:46.
 */
public class RequestUtil {

    @Nullable
    public static String getString(String url) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
