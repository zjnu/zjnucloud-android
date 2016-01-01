package com.ddmax.zjnucloud.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ddmax.zjnucloud.R;
import com.ddmax.zjnucloud.base.BaseActivity;
import com.ddmax.zjnucloud.model.calendar.Event;
import com.ddmax.zjnucloud.model.calendar.SchoolCalendar;
import com.samsistemas.calendarview.widget.CalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CalendarActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.calendar) CalendarView mCalendar;
    @Bind(R.id.event_list) RecyclerView mEventList;
    @Bind(R.id.event_none) TextView mEventNoneView;

    private List<SchoolCalendar> calendars = SchoolCalendar.ALL_CALENDARS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        // 初始化View
        initView();
    }

    private void initView() {
        mToolbar.setTitle(getString(R.string.title_activity_calendar));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_close_white_24dp));
        // 设置校历
        mCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        mCalendar.setIsOverflowDateVisible(true);
        mCalendar.setOnMonthChangedListener(new CalendarView.OnMonthChangedListener() {

            @Override
            public void onMonthChanged(@NonNull Date monthDate) {
                initEventList(SchoolCalendar.get(monthDate));
            }
        });
        // 设置当月校历事件
        Calendar cal = Calendar.getInstance();
        initEventList(SchoolCalendar.get(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1
        ));
    }

    /**
     * 初始化校历事件列表
     * @param schoolCalendar
     */
    private void initEventList(SchoolCalendar schoolCalendar) {
        mEventList.setLayoutManager(new LinearLayoutManager(this));
        if (schoolCalendar == null) {
            mEventNoneView.setVisibility(View.VISIBLE);
            mEventList.setVisibility(View.GONE);
        } else {
            mEventNoneView.setVisibility(View.GONE);
            mEventList.setVisibility(View.VISIBLE);
            mEventList.setAdapter(new CalendarEventAdapter(this,schoolCalendar.events));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.ViewHolder> {

        private Context mContext;
        private List<Event> events;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.event_time) TextView mTimeView;
            @Bind(R.id.event_name) TextView mNameView;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public CalendarEventAdapter(Context context, List<Event> events) {
            this.mContext = context;
            this.events = events;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_eventlist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if (events != null) {
                Event event = events.get(position);
                holder.mTimeView.setText(event.duration);
                holder.mNameView.setText(event.name);
            }
        }

        @Override
        public int getItemCount() {
            return events == null ? 0 : events.size();
        }


    }
}
