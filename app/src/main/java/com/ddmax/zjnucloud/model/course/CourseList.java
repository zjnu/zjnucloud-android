package com.ddmax.zjnucloud.model.course;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.ddmax.zjnucloud.model.exam.Exam;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddMax
 * @since 2015/12/21 21:05.
 */
@Table(name = "course_list")
public class CourseList extends Model implements Serializable {

    @Column(name = "status")
    public int status;
    @Column(name = "message")
    public String message;
    @Column(name = "name")
    public String name;
    @Column(name = "info")
    public String info;
    @Column(name = "courses")
    public List<Course> courses;

    @Column(name = "detail")
    public String detail; // 错误详细信息，如"Invalid Token"

    public List<Course> courses() {
        return getMany(Course.class, "Exam");
    }

    @Override
    public String toString() {
        return "CourseList{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", courses=" + courses +
                ", detail='" + detail + '\'' +
                '}';
    }
}
