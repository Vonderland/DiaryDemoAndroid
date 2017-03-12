package com.vonderland.diarydemo.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Vonderland on 2017/3/11.
 */

public class User extends RealmObject {
    @PrimaryKey
    private long uid;
    private String email;
    private String nickName;
    private boolean gender;
    private String avatar;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
