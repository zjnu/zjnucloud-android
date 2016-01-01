package com.ddmax.zjnucloud.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * @author ddMax
 * @since 2015/12/5 15:26.
 */
@Table(name = "emis_user")
public class EmisUser extends Model implements Serializable{

    @Column(name = "username")
    public String username;
    @Column(name = "password")
    public String password;
    @Column(name = "token")
    public String token;
    @Column(name = "bmob")
    public String bmob;

    public EmisUser() {
        super();
    }

    public EmisUser(String username, String password, String token, String bmob) {
        super();
        this.username = username;
        this.password = password;
        this.token = token;
        this.bmob = bmob;
    }

}
