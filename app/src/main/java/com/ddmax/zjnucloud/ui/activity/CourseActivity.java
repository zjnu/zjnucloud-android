package com.ddmax.zjnucloud.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.ddmax.courseview.MonthLoader;
import com.ddmax.courseview.WeekView;
import com.ddmax.courseview.WeekViewEvent;
import com.ddmax.courseview.WeekViewLoader;
import com.ddmax.zjnucloud.Constants;
import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.base.BaseEmisActivity;
import com.ddmax.zjnucloud.model.EmisUser;
import com.ddmax.zjnucloud.model.course.Course;
import com.ddmax.zjnucloud.model.course.CourseList;
import com.ddmax.zjnucloud.task.BaseGetDataTask;
import com.ddmax.zjnucloud.task.ResponseListener;
import com.ddmax.zjnucloud.util.GsonUtils;
import com.ddmax.zjnucloud.util.RequestUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author ddMax
 * @since 2015/11/13 14:15
 * 说明：成绩查询Activity
 */
public class CourseActivity extends BaseEmisActivity implements ResponseListener<CourseList>{

    private static final String TAG = CourseActivity.class.getSimpleName();

    @Bind(R.id.course_layout) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.app_bar) AppBarLayout mAppBarLayout;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.course_view) WeekView mCourseView;
    @Bind(R.id.fab) FloatingActionButton mFab;
    @Bind(R.id.error_message) LinearLayout mErrorView;
    @Bind(R.id.error_text) TextView mErrorText;

    private CourseList courseList;

    private Snackbar refreshSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
                doRefresh();
            }
        });
        // 初始化CourseView
        initCourseView();
    }

    @Override
    public void restoreData() {
        if (new Select().from(CourseList.class).exists()) {
            CourseList courseList = new Select().from(CourseList.class).executeSingle();
            // 设置第3个参数isContentSame为true来区分是否从本地读取
            this.onPostExecute(courseList, true, true);
        } else {
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
            refreshSnackbar = Snackbar.make(mCoordinatorLayout, R.string.course_refreshing_course, Snackbar.LENGTH_LONG)
                    .setAction("Action", null);
            refreshSnackbar.show();
            new GetCourseTask(this, this).execute(
                    url, emisUser.username, emisUser.password, emisUser.token
            );
        }
    }

    /**
     * 初始化CourseView
     */
    private void initCourseView() {
        mCourseView.setWeekViewLoader(new WeekViewLoader() {
            @Override
            public double toWeekViewPeriodIndex(Calendar instance) {
                return 0;
            }

            @Override
            public List<WeekViewEvent> onLoad(int periodIndex) {
                List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
//                Log.d(TAG, String.valueOf(newYear) + " " + String.valueOf(newMonth));
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 3);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.MONTH, 11);
                startTime.set(Calendar.YEAR, 2015);
                Calendar endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR, 2);
                endTime.set(Calendar.MONTH, 11);
                WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
                event.setColor(ContextCompat.getColor(CourseActivity.this, R.color.material_blue));
                events.add(event);

                return events;
            }
        });
        mCourseView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
//                Log.d(TAG, String.valueOf(newYear) + " " + String.valueOf(newMonth));
//                Calendar startTime = Calendar.getInstance();
//                startTime.set(Calendar.HOUR_OF_DAY, 3);
//                startTime.set(Calendar.MINUTE, 0);
//                startTime.set(Calendar.MONTH, newMonth-1);
//                startTime.set(Calendar.YEAR, newYear);
//                Calendar endTime = (Calendar) startTime.clone();
//                endTime.add(Calendar.HOUR, 2);
//                endTime.set(Calendar.MONTH, newMonth-1);
//                WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
//                event.setColor(ContextCompat.getColor(CourseActivity.this, R.color.material_blue));
//                events.add(event);
//                Calendar startTime = Calendar.getInstance();
//                startTime.set(Calendar.HOUR_OF_DAY, 3);
//                startTime.set(Calendar.MINUTE, 0);
//                startTime.set(Calendar.MONTH, newMonth-1);
//                startTime.set(Calendar.YEAR, newYear);
//                Calendar endTime = (Calendar) startTime.clone();
//                endTime.add(Calendar.HOUR, 1);
//                endTime.set(Calendar.MONTH, newMonth-1);
//                WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
//                event.setColor(getResources().getColor(R.color.material_blue));
//                events.add(event);
//
//                startTime = Calendar.getInstance();
//                startTime.set(Calendar.HOUR_OF_DAY, 3);
//                startTime.set(Calendar.MINUTE, 30);
//                startTime.set(Calendar.MONTH, newMonth-1);
//                startTime.set(Calendar.YEAR, newYear);
//                endTime = (Calendar) startTime.clone();
//                endTime.set(Calendar.HOUR_OF_DAY, 4);
//                endTime.set(Calendar.MINUTE, 30);
//                endTime.set(Calendar.MONTH, newMonth-1);
//                event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
//                event.setColor(getResources().getColor(R.color.material_green));
//                events.add(event);
//
//                startTime = Calendar.getInstance();
//                startTime.set(Calendar.HOUR_OF_DAY, 4);
//                startTime.set(Calendar.MINUTE, 20);
//                startTime.set(Calendar.MONTH, newMonth-1);
//                startTime.set(Calendar.YEAR, newYear);
//                endTime = (Calendar) startTime.clone();
//                endTime.set(Calendar.HOUR_OF_DAY, 5);
//                endTime.set(Calendar.MINUTE, 0);
//                event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
//                event.setColor(getResources().getColor(R.color.material_red));
//                events.add(event);
//
//                startTime = Calendar.getInstance();
//                startTime.set(Calendar.HOUR_OF_DAY, 5);
//                startTime.set(Calendar.MINUTE, 30);
//                startTime.set(Calendar.MONTH, newMonth-1);
//                startTime.set(Calendar.YEAR, newYear);
//                endTime = (Calendar) startTime.clone();
//                endTime.add(Calendar.HOUR_OF_DAY, 2);
//                endTime.set(Calendar.MONTH, newMonth-1);
//                event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
//                event.setColor(getResources().getColor(R.color.material_orange));
//                events.add(event);
//
//                startTime = Calendar.getInstance();
//                startTime.set(Calendar.HOUR_OF_DAY, 5);
//                startTime.set(Calendar.MINUTE, 0);
//                startTime.set(Calendar.MONTH, newMonth-1);
//                startTime.set(Calendar.YEAR, newYear);
//                startTime.add(Calendar.DATE, 1);
//                endTime = (Calendar) startTime.clone();
//                endTime.add(Calendar.HOUR_OF_DAY, 3);
//                endTime.set(Calendar.MONTH, newMonth - 1);
//                event = new WeekViewEvent(3, getEventTitle(startTime), startTime, endTime);
//                event.setColor(getResources().getColor(R.color.material_teal_dark));
//                events.add(event);
//
//                startTime = Calendar.getInstance();
//                startTime.set(Calendar.DAY_OF_MONTH, 15);
//                startTime.set(Calendar.HOUR_OF_DAY, 3);
//                startTime.set(Calendar.MINUTE, 0);
//                startTime.set(Calendar.MONTH, newMonth-1);
//                startTime.set(Calendar.YEAR, newYear);
//                endTime = (Calendar) startTime.clone();
//                endTime.add(Calendar.HOUR_OF_DAY, 3);
//                event = new WeekViewEvent(4, getEventTitle(startTime), startTime, endTime);
//                event.setColor(getResources().getColor(R.color.material_blue_dark));
//                events.add(event);
//
//                startTime = Calendar.getInstance();
//                startTime.set(Calendar.DAY_OF_MONTH, 1);
//                startTime.set(Calendar.HOUR_OF_DAY, 3);
//                startTime.set(Calendar.MINUTE, 0);
//                startTime.set(Calendar.MONTH, newMonth-1);
//                startTime.set(Calendar.YEAR, newYear);
//                endTime = (Calendar) startTime.clone();
//                endTime.add(Calendar.HOUR_OF_DAY, 3);
//                event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
//                event.setColor(getResources().getColor(R.color.material_amber));
//                events.add(event);
//
//                startTime = Calendar.getInstance();
//                startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
//                startTime.set(Calendar.HOUR_OF_DAY, 15);
//                startTime.set(Calendar.MINUTE, 0);
//                startTime.set(Calendar.MONTH, newMonth-1);
//                startTime.set(Calendar.YEAR, newYear);
//                endTime = (Calendar) startTime.clone();
//                endTime.add(Calendar.HOUR_OF_DAY, 3);
//                event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
//                event.setColor(getResources().getColor(R.color.material_pink));
//                events.add(event);

                return events;
            }
        });
    }

    private String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    // TODO: getCourses
    private List<WeekViewEvent> getCourses(int year, int month) {
//        List<WeekViewEvent> events = new ArrayList<>();
//        for (Course course : courseList.courses) {
//            for (int i = 0; i < course.time.length; i++) {
//                // 解析课程时间
//                String day = course.time[i].substring(0, 1);
//                int dayNumber = day.indexOf("一二三四五") + 1;
//                SimpleDateFormat sdf = new SimpleDateFormat("u");
//                try {
//                    sdf.parse(String.valueOf(dayNumber));
//                    events.add(new WeekViewEvent(
//                            Long.valueOf(course.id), course.name,
//                            Calendar.getInstance().setTime();, Calendar.getInstance()
//                    ))
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        return null;
    }

    private void startRefreshStatus() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        animation.setInterpolator(new AccelerateInterpolator());
        this.mFab.startAnimation(animation);
    }

    private void stopRefreshStatus() {
        this.mFab.clearAnimation();
        // 取消Snackbar显示
        if (refreshSnackbar != null) {
            refreshSnackbar.dismiss();
        }
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
            String content = null;
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

    // 以下为ResponseListener回调接口实现
    @Override
    public void onPreExecute() {
        this.startRefreshStatus();
    }

    @Override
    public void onPostExecute(CourseList courseList, boolean isRefreshSuccess, boolean isContentSame) {
        // 停止刷新状态
        this.stopRefreshStatus();
        if (courseList == null) {
            return;
        }
        switch (courseList.status) {
            case 0:
                // 请求成功
                Log.d(TAG, "课表请求成功！");
                setVisibility(true);
                // 非法的Token，提示重新绑定
                if (courseList.detail != null && !courseList.detail.isEmpty()) {
                    alertInvalidToken();
                    return;
                }
                this.courseList = courseList;
                // 保存到数据库
                if (!isContentSame) {
                    this.courseList.save();
                }
                // 初始化CourseView
                initCourseView();
                break;
            case 3:
                // 未能请求到数据
                setVisibility(false, getString(R.string.emis_server_error));
                break;
            case -1:
                setVisibility(false, getString(R.string.emis_error));
                break;
            default:
                Snackbar.make(mCoordinatorLayout, courseList.message, Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onProgressUpdate(Long value) {}

    @Override
    public void onFail(Exception e) {
        Log.d(TAG, "考试数据刷新失败！");
        if (e instanceof SocketTimeoutException) {
            Snackbar.make(mCoordinatorLayout, getString(R.string.emis_request_timeout), Snackbar.LENGTH_LONG).show();
        }
    }
}
