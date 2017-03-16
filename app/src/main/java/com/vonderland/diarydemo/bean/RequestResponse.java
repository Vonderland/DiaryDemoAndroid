package com.vonderland.diarydemo.bean;

/**
 * Created by Vonderland on 2017/3/16.
 */

public class RequestResponse extends BaseResponse {
    private Request data;

    public Request getData() {
        return data;
    }

    public void setData(Request data) {
        this.data = data;
    }
}
