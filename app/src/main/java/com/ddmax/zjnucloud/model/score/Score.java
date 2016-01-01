package com.ddmax.zjnucloud.model.score;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * @author ddMax
 * @since 2015/11/4 20:47.
 */
@Table(name = "score_score")
public class Score extends Model implements Serializable {

    @Column(name = "cid")
    public String id;
    @Column(name = "name")
    public String name;
    @Column(name = "credit")
    public String credit;
    @Column(name = "mark")
    public String mark;
    @Column(name = "makeupmark")
    public String makeupmark;
    @Column(name = "retakemark")
    public String retakemark;
    @Column(name = "gradepoint")
    public String gradepoint;

}
