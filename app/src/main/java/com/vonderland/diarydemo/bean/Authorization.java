package com.vonderland.diarydemo.bean;

/**
 * Created by Vonderland on 2017/3/11.
 */

public class Authorization {

    private long uid;
    private String token;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
