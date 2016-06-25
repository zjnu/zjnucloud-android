package com.ddmax.zjnucloud.model.calendar;

import com.activeandroid.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author ddMax
 * @since 2015/12/17 21:07.
 */
public class SchoolCalendar implements Serializable{

    public static final Object[][] CALENDAR_DATA = {
            {2015, 8, new Event("教师、学生报到", "8月31日")},
            {2015, 9, new Event("开始上课", "9月1日")},
            {2015, 9, new Event("抗日战争胜利70周年纪念日", "9月3日")},
            {2015, 9, new Event("本专科生实践教学周", "9月1-7日")},
            {2015, 9, new Event("新生报到", "9月5-6日")},
            {2015, 9, new Event("新生始业教育、军训、选课", "9月7-23日")},
            {2015, 9, new Event("研究生新生开始上课", "9月9日")},
            {2015, 9, new Event("教师节", "9月10日")},
            {2015, 9, new Event("本专科新生开始上课", "9月27日")},
            {2015, 10, new Event("国庆节", "10月1-7日")},
            {2015, 10, new Event("校运动会", "10月23-24日")},
            {2015, 11, new Event("前半学期短课程考试", "11月2-8日")},
            {2015, 11, new Event("后半学期短课程开始上课", "11月9日")},
            {2016, 1, new Event("元旦", "1月1-3日")},
            {2016, 1, new Event("期末考试", "1月12-18日")},
            {2016, 1, new Event("寒假", "1月19日", true)},
            {2016, 2, new Event("春节", "2月8日")},
            {2016, 2, new Event("元宵节", "2月22日")},
            {2016, 2, new Event("教师、学生报到", "2月24日")},
            {2016, 2, new Event("开始上课", "2月25日")},
            {2016, 2, new Event("上周三课程", "2月27日")},
            {2016, 4, new Event("清明节", "4月4日")},
            {2016, 4, new Event("校庆日", "4月16日")},
            {2016, 4, new Event("前半学期短课程考试", "4月18-24日")},
            {2016, 4, new Event("后半学期短课程开始上课", "4月25日")},
            {2016, 5, new Event("国际劳动节", "5月1日")},
            {2016, 5, new Event("青年节", "5月4日")},
            {2016, 6, new Event("端午节", "6月9日")},
            {2016, 6, new Event("毕业生离校", "6月16日")},
            {2016, 6, new Event("期末考试", "6月20-26日")},
            {2016, 6, new Event("本专科生实践教学周", "6月27日-7月1日")},
            {2016, 7, new Event("暑假", "7月2日", true)},
    };

    public static final List<SchoolCalendar> ALL_CALENDARS;

    static {
        ALL_CALENDARS = new ArrayList<>();
        // 遍历数组，同年同月的校历事件添加到集合中
        ArrayList<Event> events = null;
        for (int i = 0; i < CALENDAR_DATA.length; i++) {
            int year = Integer.valueOf(CALENDAR_DATA[i][0].toString());
            int month = Integer.valueOf(CALENDAR_DATA[i][1].toString());
            if (i == 0) {
                events = new ArrayList<>();
                events.add((Event) CALENDAR_DATA[i][2]);
            } else {
                int yearLast = Integer.valueOf(CALENDAR_DATA[i - 1][0].toString());
                int monthLast = Integer.valueOf(CALENDAR_DATA[i - 1][1].toString());
                if (year == yearLast && month == monthLast) {
                    events.add((Event) CALENDAR_DATA[i][2]);
                } else {
                    ALL_CALENDARS.add(new SchoolCalendar(
                            yearLast, monthLast, events
                    ));
                    events = new ArrayList<>();
                    events.add((Event) CALENDAR_DATA[i][2]);
                }

            }
        }
    }

    public int year;
    public int month;
    public List<Event> events;

    public SchoolCalendar() {}

    public SchoolCalendar(int year, int month, List<Event> events) {
        this.year = year;
        this.month = month;
        this.events = events;
    }

    public static SchoolCalendar get(int year, int month) {
        for (SchoolCalendar calendar : ALL_CALENDARS) {
            if (year == calendar.year && month == calendar.month) {
                return calendar;
            }
        }
        return null;
    }

    public static SchoolCalendar get(Date date) {
        int year = Integer.valueOf(
                new SimpleDateFormat("yyyy", Locale.getDefault()).format(date));
        int month = Integer.valueOf(
                new SimpleDateFormat("MM", Locale.getDefault()).format(date));
        return get(year, month);
    }

    /**
     * @return 日期字符串，格式为"yyyy.MM"
     */
    @Override
    public String toString() {
        return String.valueOf(this.year) + "." + String.valueOf(this.month);
    }
}
