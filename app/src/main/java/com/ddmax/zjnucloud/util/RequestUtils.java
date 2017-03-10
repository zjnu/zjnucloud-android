package com.ddmax.zjnucloud.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.model.banner.Banner;
import com.ddmax.zjnucloud.model.banner.BannerDetail;
import com.ddmax.zjnucloud.model.calendar.KeyDates;
import com.ddmax.zjnucloud.model.explore.ExploreList;
import com.ddmax.zjnucloud.model.speech.SpeechList;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * @author ddMax
 * @since 2015/11/4 22:46.
 */
public class RequestUtils {

    private static OkHttpClient client;

    static {
        client = new OkHttpClient();
        client.setReadTimeout(30, TimeUnit.SECONDS);
    }

    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Nullable
    public static String get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Nullable
    public static String post(String url, Map data) throws IOException {
//        OkHttpClient client = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 遍历data
        Set keys = data.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            String value = data.get(key).toString();
            builder.add(key, value);
        }
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (SocketTimeoutException e) {
            return null;
        }
    }

    @Nullable
    public static String post(String url, Map data, String token) throws IOException {
//        OkHttpClient client = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        // 遍历data
        Set keys = data.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            String value = data.get(key).toString();
            builder.add(key, value);
        }
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Token " + token)
                .post(builder.build())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 设置OkHTTP网络缓存拦截器
     * @param context Context
     * @param day 缓存天数
     */
    public static void setCacheInterceptor(final Context context, final int day) {
        final Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (isNetworkAvailable(context)) {
                    int maxAge = 60; // read from cache for 1 minute
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * day; // tolerate 1-week stale
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };
        client.networkInterceptors().add(cacheInterceptor);
        // 设置缓存
        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10MB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        // 为client添加缓存
        client.setCache(cache);
        // 将client设置到Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 获取校历服务
     */
    public interface KeyDatesService {
        @GET(Constants.URL.KEYDATES)
        Call<KeyDates> getAll();
        @GET(Constants.URL.KEYDATES_VERSION)
        Call<KeyDates> getVersion();
    }

    /**
     * 获取讲座信息服务
     */
    public interface SpeechService {
        @GET(Constants.URL.SPEECH)
        Call<SpeechList> getAll();
        @GET(Constants.URL.SPEECH)
        Call<SpeechList> getMore(@Query("page") int page);
    }

    static SpeechService speechService = retrofit.create(SpeechService.class);

    public static void getAllSpeech(Callback<SpeechList> callback) {
        Call<SpeechList> call = speechService.getAll();
        call.enqueue(callback);
    }

    public static void getMoreSpeech(int page, Callback<SpeechList> callback) {
        Call<SpeechList> call = speechService.getMore(page);
        call.enqueue(callback);
    }

    /**
     * 获取首页轮播图服务
     */
    public interface BannerService {
        @GET(Constants.URL.BANNER)
        Call<Banner> get();
        @GET(Constants.URL.BANNER_DETAIL)
        Call<BannerDetail> getDetail(@Path("prefix") String rawUrl);
    }

    static BannerService bannerService = retrofit.create(BannerService.class);

    public static void getBanner(Callback<Banner> callback) {
        Call<Banner> call = bannerService.get();
        call.enqueue(callback);
    }

    public static void getBannerDetail(String url, Callback<BannerDetail> callback) {
        Log.d("RequestUtils", url);
        Call<BannerDetail> call = bannerService.getDetail(url);
        call.enqueue(callback);
    }

    /**
     * 获取发现内容服务
     */
    public interface ExploreService {
        @GET(Constants.URL.EXPLORE)
        Call<ExploreList> get(@Query("page") int page);
    }

    static ExploreService exploreService = retrofit.create(ExploreService.class);

    public static void getExplore(int page, Callback<ExploreList> callback) {
        Call<ExploreList> call = exploreService.get(page);
        call.enqueue(callback);
    }
    /**
     * 检查网络是否可用
     * @param context Context
     * @return boolean
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
