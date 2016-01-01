package com.ddmax.zjnucloud.util;

import android.content.Context;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.model.EmisUser;
import com.ddmax.zjnucloud.model.score.Score;
import com.ddmax.zjnucloud.model.score.ScoreList;
import com.ddmax.zjnucloud.model.score.Semester;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.DELETE;
import retrofit.http.Query;

/**
 * @author ddMax
 * @since 2015/12/15 8:59.
 */
public class EmisUtils {

    private static ValuePreference valuePreference;
    private static EmisUser currentEmisUser;

    private static Class[] allModels = {
            Semester.class, Score.class, ScoreList.class,
            EmisUser.class
    };

    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.URL.BASE_INTRA_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    static EmisService service = retrofit.create(EmisService.class);

    public interface EmisService {
        @DELETE(Constants.URL.EMIS.BIND)
        Call<String> unbind(@Query("username") String username, @Query("bmob") String bmob);
    }

    /**
     * 判断是否已绑定教务账号
     * @param context
     * @return
     */
    public static boolean isEmisUserBinded(Context context) {
        valuePreference = new ValuePreference(context);
        if (valuePreference.getEmisBind()) {
            if (new Select().from(EmisUser.class).exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前绑定的教务账号
     * @param context
     * @return EmisUser
     */
    public static EmisUser getCurrentEmisUser(Context context) {
        if (isEmisUserBinded(context)) {
            if (currentEmisUser == null) {
                currentEmisUser = new Select().from(EmisUser.class).executeSingle();
            }
            return currentEmisUser;
        }
        return null;
    }

    /**
     * 执行教务解绑
     */
    public static void unbind(String username, String bmob, Callback<String> callback) {
        Call<String> call = service.unbind(username, bmob);
        call.enqueue(callback);
    }

    /**
     * 清除缓存数据
     */
    public static void clean(Context context) {
        valuePreference = new ValuePreference(context);
        valuePreference.saveEmisBind(false);
        // 清除所有教务相关数据
        for (Class each : allModels) {
            new Delete().from(each).execute();
        }
    }
}
