package com.ddmax.courseview;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CourseLoader implements CourseViewLoader {

    public static final String DAY_OF_WEEK = "一二三四五六七";

    private LoadListener mLoadListener;

    public CourseLoader(LoadListener listener){
        this.mLoadListener = listener;
    }

    @Override
    public List<CourseViewEvent> onLoad(int dayNumber){
        List<CourseViewEvent> courseViewEvents = mLoadListener.onCourseLoad();
        List<CourseViewEvent> targetEvents = new ArrayList<CourseViewEvent>();
        for (CourseViewEvent event : courseViewEvents) {
            String weekDay = event.getWeekDay();
            if (dayNumber == DAY_OF_WEEK.indexOf(weekDay) + 1) {
                targetEvents.add(event);
            }
        }
        return targetEvents;
    }

    public LoadListener getLoadListener() {
        return mLoadListener;
    }

    public void setLoadListener(LoadListener onLoadListener) {
        this.mLoadListener = onLoadListener;
    }

    public interface LoadListener {
        /**
         * Very important interface, it's the base to load courses in the view.
         * @return a list of the course events.
         */
        List<CourseViewEvent> onCourseLoad();
    }
}
