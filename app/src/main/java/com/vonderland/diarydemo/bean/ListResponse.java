package com.vonderland.diarydemo.bean;

import java.util.List;

/**
 * Created by Vonderland on 2017/2/1.
 */

public class ListResponse<T> extends BaseResponse {
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
