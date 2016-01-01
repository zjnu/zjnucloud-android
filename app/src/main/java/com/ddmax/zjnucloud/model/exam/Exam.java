package com.ddmax.zjnucloud.model.exam;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * @author ddMax
 * @since 2015/12/21 21:05.
 */
@Table(name = "exam_exam")
public class Exam extends Model implements Serializable {

    @Column(name = "cid")
    public String id;
    @Column(name = "name")
    public String name;
    @Column(name = "classno")
    public String classno;
    @Column(name = "teacher")
    public String teacher;
    @Column(name = "date")
    public String date;
    @Column(name = "time")
    public String time;
    @Column(name = "place")
    public String place;
    @Column(name = "studentno")
    public String studentno;

}
