package com.ddmax.zjnucloud.model.score;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddMax
 * @since 2015/11/4 20:46.
 */
public class Semester implements Serializable {

    private String semester;
    private List<Course> values;

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public List<Course> getValues() {
        return values;
    }

    public void setValues(List<Course> values) {
        this.values = values;
    }
}
