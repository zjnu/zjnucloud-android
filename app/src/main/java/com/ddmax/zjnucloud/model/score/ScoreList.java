package com.ddmax.zjnucloud.model.score;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddMax
 * @since 2015/11/4 20:44.
 */
@Table(name = "score_list")
public class ScoreList extends Model implements Serializable{

    @Column(name = "status")
    public int status;
    @Column(name = "message")
    public String message;
    @Column(name = "name")
    public String name;
    @Column(name = "credits")
    public String credits;
    @Column(name = "gpa")
    public String gpa;
    @Column(name = "count")
    public int count;
    @Column(name = "scores")
    public List<Semester> scores;

    @Column(name = "detail")
    public String detail; // 错误详细信息，如"Invalid Token"

    public List<Semester> scores() {
        return getMany(Semester.class, "Score");
    }

}
