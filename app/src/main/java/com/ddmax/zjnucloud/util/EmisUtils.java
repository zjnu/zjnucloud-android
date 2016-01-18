package com.ddmax.zjnucloud.util;

import android.content.Context;

import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.model.EmisUser;
import com.ddmax.zjnucloud.model.course.Course;
import com.ddmax.zjnucloud.model.course.CourseList;
import com.ddmax.zjnucloud.model.exam.Exam;
import com.ddmax.zjnucloud.model.exam.ExamList;
import com.ddmax.zjnucloud.model.score.Score;
import com.ddmax.zjnucloud.model.score.ScoreList;
import com.ddmax.zjnucloud.model.score.Semester;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.POST;
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
            Exam.class, ExamList.class,
            Course.class, CourseList.class,
            EmisUser.class
    };

    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.URL.BASE_INTRA_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    static EmisService service = retrofit.create(EmisService.class);

    public interface EmisService {
        @POST(Constants.URL.EMIS.SCORE)
        Call<String> getScore(@Field("username") String username, @Field("password") String password);
        @POST(Constants.URL.EMIS.EXAM)
        Call<String> getExam(@Field("username") String username, @Field("password") String password);
        @POST(Constants.URL.EMIS.COURSE)
        Call<String> getCourse(@Field("username") String username, @Field("password") String password);
        @POST(Constants.URL.EMIS.BIND)
        Call<String> bind(@Field("username") String username, @Field("password") String password, @Field("bmob") String bmob);
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
     * 获取所有教务信息
     */
    public static void getAllEmis(String username, String password) {

    }

    /**
     * 执行教务解绑
     */
    public static void unbind(String username, String bmob, Callback<String> callback) {
        Call<String> call = service.unbind(username, bmob);
        call.enqueue(callback);
    }

    /**
     * 清除所有教务系统相关缓存数据
     */
    public static void clean(Context context) {
        valuePreference = new ValuePreference(context);
        valuePreference.saveEmisBind(false);
        // 清除所有相关对象数据
        for (Class each : allModels) {
            new Delete().from(each).execute();
        }
    }

    /**
     * 清除指定对象数据
     * @param models AA Model子类
     */
    @SuppressWarnings({"unchecked", "varargs"})
    public static void clean(Class<? extends Model>... models) {
        for (Class<? extends Model> model : models) {
            if (new Select().from(model).exists()) {
                new Delete().from(model).execute();
            }
        }
    }
}
