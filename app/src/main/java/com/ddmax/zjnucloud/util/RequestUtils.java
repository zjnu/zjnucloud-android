package com.ddmax.zjnucloud.util;

import android.support.annotation.Nullable;

import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.model.speech.SpeechList;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
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
    }

    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    static SpeechService speechService = retrofit.create(SpeechService.class);

    @Nullable
    public static String get(String url) throws IOException {

        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Nullable
    public static String post(String url, Map data) throws IOException {
        OkHttpClient client = new OkHttpClient();
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
        OkHttpClient client = new OkHttpClient();
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

    public interface SpeechService {
        @GET(Constants.URL.SPEECH)
        Call<SpeechList> getAll();
        @GET(Constants.URL.SPEECH)
        Call<SpeechList> getMore(@Query("page") int page);
    }

    public static void getAllSpeech(Callback<SpeechList> callback) {
        Call<SpeechList> call = speechService.getAll();
        call.enqueue(callback);
    }

    public static void getMoreSpeech(int page, Callback<SpeechList> callback) {
        Call<SpeechList> call = speechService.getMore(page);
        call.enqueue(callback);
    }

}
