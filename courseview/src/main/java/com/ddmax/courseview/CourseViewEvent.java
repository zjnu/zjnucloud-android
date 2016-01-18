package com.ddmax.courseview;

import java.util.List;

/**
 * Created by Raquib-ul-Alam Kanak on 7/21/2014.
 * Website: http://april-shower.com
 */
public class CourseViewEvent {
    private long mId;
    private String mName;
    private String mPlace;
    private String weekDay;
    private int start;
    private int end;
    private int mColor;

    public CourseViewEvent(){

    }

    /**
     * Initializes the course for week view.
     * @param id The id of the course.
     * @param name Name of the course.
     * @param place The location of the course.
     * @param weekDay Week day of course, format: 一|二|三|四|五|六|七.
     * @param start The start time of the course. <strong>1-based</strong>
     * @param end The end time of the course.
     */
    public CourseViewEvent(long id, String name, String place, String weekDay, int start, int end) {
        this.mId = id;
        this.mName = name;
        this.mPlace = place;
        this.weekDay = weekDay;
        this.start = start - 1;
        this.end = end;
    }

    /**
     * Initializes the course for week view.
     * @param id The id of the course.
     * @param name Name of the course.
     * @param weekDay Week day of course, format: 一|二|三|四|五|六|七.
     * @param start The start time of the course.
     * @param end The end time of the course.
     */
    public CourseViewEvent(long id, String name, String weekDay, int start, int end) {
        this(id, name, null, weekDay, start, end);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String location) {
        this.mPlace = location;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public int getStartTime() {
        return start;
    }

    public void setStartTime(int start) {
        this.start = start;
    }

    public int getEndTime() {
        return end;
    }

    public void setEndTime(int end) {
        this.end = end;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseViewEvent that = (CourseViewEvent) o;

        return mId == that.mId;

    }

    @Override
    public int hashCode() {
        return (int) (mId ^ (mId >>> 32));
    }

    @Override
    public String toString() {
        return this.mName;
    }
}
