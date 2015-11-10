package com.ddmax.zjnucloud.model.score;

import java.io.Serializable;
import java.util.List;

/**
 * @author ddMax
 * @since 2015/11/4 20:44.
 */
public class Score implements Serializable{

    private int status;
    private String message;
    private String name;
    private String credits;
    private String gpa;
    private int count;
    private List<Semester> scores;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Semester> getScores() {
        return scores;
    }

    public void setScores(List<Semester> scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "Score{" +
                "status=" + status +
                ", name='" + name + '\'' +
                ", credits='" + credits + '\'' +
                ", gpa='" + gpa + '\'' +
                ", count=" + count +
                ", scores=" + scores +
                '}';
    }
}
