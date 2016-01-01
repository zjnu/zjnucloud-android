package com.ddmax.zjnucloud.model.score;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddMax
 * @since 2015/11/4 20:46.
 */
@Table(name = "score_semester")
public class Semester extends Model implements Serializable {

    @Column(name = "semester")
    public String semester;
    @Column(name = "gpa")
    public String gpa;
    @Column(name = "vals")
    public List<Score> values;

    public List<Score> courses() {
        return getMany(Score.class, "Semester");
    }

}
