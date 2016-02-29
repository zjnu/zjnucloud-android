package com.ddmax.zjnucloud.ui.activity;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.ddmax.courseview.CourseLoader;
import com.ddmax.courseview.CourseView;
import com.ddmax.courseview.CourseViewEvent;
import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.base.BaseEmisActivity;
import com.ddmax.zjnucloud.model.EmisUser;
import com.ddmax.zjnucloud.model.course.Course;
import com.ddmax.zjnucloud.model.course.CourseList;
import com.ddmax.zjnucloud.task.BaseGetDataTask;
import com.ddmax.zjnucloud.task.ResponseListener;
import com.ddmax.zjnucloud.util.EmisUtils;
import com.ddmax.zjnucloud.util.GsonUtils;
import com.ddmax.zjnucloud.util.RequestUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author ddMax
 * @since 2015/11/13 14:15
 * 说明：课表Activity
 */
public class CourseActivity extends BaseEmisActivity implements ResponseListener<CourseList> {

    private static final String TAG = CourseActivity.class.getSimpleName();

    @Bind(R.id.course_view) CourseView mCourseView;
    @Bind(R.id.error_message) LinearLayout mErrorView;
    @Bind(R.id.error_text) TextView mErrorText;

    private CourseList courseList;
    private GetCourseTask getCourseTask;
//    private static final int[] courseColors = {
//        R.color.material_blue, R.color.material_pink,
//        R.color.material_green, R.color.material_orange,
//        R.color.material_red, R.color.material_amber,
//        R.color.material_teal_dark, R.color.material_deep_orange
//    };
    private static final int[] courseColors = {
        R.color.course_cadetblue, R.color.course_chocolate,
        R.color.course_cornflowerblue, R.color.course_forestgreen,
        R.color.course_goldenrod, R.color.course_indianred,
        R.color.course_mediumrosybrown, R.color.course_mediumturquoise,
        R.color.course_mediumorange, R.color.course_purple,
        R.color.course_salmon, R.color.course_yellow
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);

        mToolbar.setTitle(getString(R.string.title_activity_course));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 设置浮动按钮监听器
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFabProgressCircle.show();
                doRefresh();
            }
        });
        // 初始化CourseView
        initCourseView();
    }

    @Override
    public void restoreData() {
        if (new Select().from(CourseList.class).exists()) {
            // 本地数据库中已有数据
            CourseList courseList = new Select().from(CourseList.class).executeSingle();
            // 设置第3个参数isContentSame为true来区分是否从本地读取
            this.onPostExecute(courseList, true, true);
        } else {
            // 此时为第一次加载数据
            setInitAnimationShown(true);
            doRefresh();
        }
    }

    /**
     * 刷新成绩数据
     */
    @Override
    public void doRefresh() {
        if (new Select().from(EmisUser.class).exists()) {
            EmisUser emisUser = new Select().from(EmisUser.class).executeSingle();
            String url = Constants.URL.EMIS.COURSE;

            getCourseTask = new GetCourseTask(this, this);
            getCourseTask.execute(url, emisUser.username, emisUser.password, emisUser.token);
        }
    }

    /**
     * 初始化CourseView
     */
    private void initCourseView() {
        mCourseView.setLoadListener(new CourseLoader.LoadListener() {
            @Override
            public List<CourseViewEvent> onCourseLoad() {
                List<CourseViewEvent> events = new ArrayList<CourseViewEvent>();
                if (courseList != null) {
                    int i = 0;
                    for (Course course : courseList.courses) {
                        for (String time : course.time) {
                            try {
                                // example: "一10,11,12,"
                                String[] dd = time.split(",");
                                CourseViewEvent event = new CourseViewEvent(
                                        i, course.name, course.classroom,
                                        dd[0].substring(0, 1), // 周几
                                        Integer.valueOf(dd[0].substring(1, dd[0].length())), // 开始节数
                                        Integer.valueOf(dd[dd.length - 1]) //结束节数
                                );
                                event.setColor(ContextCompat.getColor(CourseActivity.this, courseColors[i % courseColors.length]));

                                events.add(event);
                            } catch (NumberFormatException e) {
                                Log.d(TAG, "时间格式错误");
                            }
                            i++;
                        }
                    }
                }
                return events;
            }
        });
        mCourseView.invalidate();
    }

    private String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    private void setVisibility(boolean isSuccess) {
        this.setVisibility(isSuccess, null);
    }

    private void setVisibility(boolean isSuccess, String errMsg) {
        if (errMsg != null) {
            mErrorText.setText(errMsg);
        }
        mCourseView.setVisibility(isSuccess ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(isSuccess ? View.GONE : View.VISIBLE);
        setInitAnimationShown(!isSuccess);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_emis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                break;
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 异步任务载入成绩数据
     */
    public static class GetCourseTask extends BaseGetDataTask<CourseList> {

        public GetCourseTask(Context mContext, ResponseListener mResponseListener) {
            super(mContext, mResponseListener);
        }

        @Override
        protected CourseList doInBackground(String... params) {
            String content;
            try {
                HashMap<String, String> data = new HashMap<>();
                data.put("username", params[1]);
                data.put("password", params[2]);
                content = RequestUtils.post(params[0], data, params[3]);
                CourseList courseList = GsonUtils.getCourseList(content);
                return courseList;
            } catch (IOException e) {
                mResponseListener.onFail(e);
            }
            return null;
        }
    }

    // 以下为ResponseListener回调接口实现
    @Override
    public void onPreExecute() {
        startRefreshStatus(getCourseTask, R.string.course_refreshing_course);
    }

    @Override
    public void onPostExecute(CourseList courseList, boolean isRefreshSuccess, boolean isContentSame) {
        // 未能返回数据
        if (courseList == null) {
            // 取消刷新状态
            cancelRefreshStatus(getCourseTask, R.string.emis_offline);
            return;
        }
        switch (courseList.status) {
            // 请求成功
            case 0:
                Log.d(TAG, "课表请求成功！");
                setVisibility(true);
                // 非法的Token，提示重新绑定
                if (courseList.detail != null && !courseList.detail.isEmpty()) {
                    alertInvalidToken();
                    return;
                }
                this.courseList = courseList;
                // 若为刷新数据，则清空后保存到数据库
                if (!isContentSame) {
                    // 完成刷新状态
                    finishRefresh();
                    EmisUtils.clean(Course.class, CourseList.class);
                    this.courseList.save();
                }
                // 初始化CourseView
                initCourseView();
                break;
            default:
                if (this.courseList.courses == null || this.courseList.courses.size() == 0) {
                    mErrorText.setText(courseList.message);
                    if (mErrorView.getVisibility() != View.VISIBLE) {
                        mErrorView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Snackbar.make(mCoordinatorLayout, courseList.message, Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onProgressUpdate(Long value) {}

    @Override
    public void onFail(Exception e) {
        Log.d(TAG, "课表数据刷新失败！");
        stopRefreshStatus(getCourseTask);
        if (e instanceof SocketTimeoutException) {
            Snackbar.make(mCoordinatorLayout, getString(R.string.emis_request_timeout), Snackbar.LENGTH_LONG).show();
        }
    }
}
