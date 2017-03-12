package com.vonderland.diarydemo.event;

/**
 * Created by Vonderland on 2017/3/12.
 */

public class RefreshNavEvent {
    public final String avatar;
    public final String nickName;

    public RefreshNavEvent(String avatar, String nickName) {
        this.avatar = avatar;
        this.nickName = nickName;
    }
}
