package com.vonderland.diarydemo.bean;

/**
 * Created by Vonderland on 2017/2/2.
 */

public class BaseResponse {
    private int code;
    private int size;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
