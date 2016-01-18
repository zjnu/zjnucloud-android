package com.ddmax.courseview;

import java.util.Calendar;
import java.util.List;

public interface CourseViewLoader {
    /**
     * Load the events in specific day of week
     * @return A list with the events of this day
     */
    List<CourseViewEvent> onLoad(int dayOfWeek);
}
