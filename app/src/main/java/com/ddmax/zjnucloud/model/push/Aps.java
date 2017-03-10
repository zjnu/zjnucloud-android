package com.ddmax.zjnucloud.model.push;

import java.io.Serializable;

/**
 * @author ddMax
 * @since 2016/4/25 20:58.
 * 说明：消息标题实体，跟iOS一致
 */
public class Aps implements Serializable {

    private String alert;
    private int badge;
    private String sound;

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    @Override
    public String toString() {
        return "Aps [alert=" + alert + ", badge=" + badge + ", sound=" + sound
                + "]";
    }
}
