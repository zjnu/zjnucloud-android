package com.ddmax.zjnucloud.model.course;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * @author ddMax
 * @since 2015/12/21 21:05.
 */
@Table(name = "course_course")
public class Course extends Model implements Serializable {

    @Column(name = "cid")
    public String id;
    @Column(name = "no")
    public String no;
    @Column(name = "name")
    public String name;
    @Column(name = "teacher")
    public String teacher;
    @Column(name = "time")
    public String[] time;
    @Column(name = "classroom")
    public String classroom;
    @Column(name = "total")
    public String total;
    @Column(name = "elected")
    public String elected;
    @Column(name = "credit")
    public String credit;
    @Column(name = "property")
    public String property;
    @Column(name = "remarks")
    public String remarks;

}
