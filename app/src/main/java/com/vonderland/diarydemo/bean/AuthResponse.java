package com.vonderland.diarydemo.bean;

/**
 * Created by Vonderland on 2017/3/11.
 */

public class AuthResponse extends BaseResponse {
    private Authorization data;

    public Authorization getData() {
        return data;
    }

    public void setData(Authorization data) {
        this.data = data;
    }
}
