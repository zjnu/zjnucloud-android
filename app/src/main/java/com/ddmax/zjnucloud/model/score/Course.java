package com.ddmax.zjnucloud.model.score;

import java.io.Serializable;

/**
 * @author ddMax
 * @since 2015/11/4 20:47.
 */
public class Course implements Serializable {

    private String id;
    private String name;
    private String credit;
    private String mark;
    private String makeupmark;
    private String retakemark;
    private String gradepoint;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMakeupmark() {
        return makeupmark;
    }

    public void setMakeupmark(String makeupmark) {
        this.makeupmark = makeupmark;
    }

    public String getRetakemark() {
        return retakemark;
    }

    public void setRetakemark(String retakemark) {
        this.retakemark = retakemark;
    }

    public String getGradepoint() {
        return gradepoint;
    }

    public void setGradepoint(String gradepoint) {
        this.gradepoint = gradepoint;
    }
}
