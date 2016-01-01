package com.ddmax.zjnucloud.model.calendar;

import com.activeandroid.Model;

import java.io.Serializable;

/**
 * @author ddMax
 * @since 2015/12/17 23:42.
 */
public class Event implements Serializable {

    public String name;
    public String duration;
    public boolean isAccent;

    public Event(String name, String duration) {
        this(name, duration, false);
    }

    public Event(String name, String duration, boolean isAccent) {
        this.name = name;
        this.duration = duration;
        this.isAccent = isAccent;
    }
}
