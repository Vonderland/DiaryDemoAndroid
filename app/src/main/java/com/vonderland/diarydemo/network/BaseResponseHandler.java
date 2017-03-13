package com.vonderland.diarydemo.network;

import android.util.Log;

import com.vonderland.diarydemo.bean.BaseResponse;
import com.vonderland.diarydemo.utils.L;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Vonderland on 2017/2/2.
 */

/**
 * 对 Response 进行预处理：
 * 1. 统一处理网络异常
 * 2. 将正常的 http response 通过 onSuccess() 传给调用层
 * */
abstract public class BaseResponseHandler<T> implements Callback<T> {
    private static final String TAG = "DebugBaseResponseHandler";

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        L.d("profileTest", "haha");
        if (response.isSuccessful()) {
            L.d("profileTest", "response.isSuccessful, body = " + response.body());
            if (response.body() instanceof BaseResponse) {
                L.d("profileTest", "response.body() instanceof BaseResponse");
                BaseResponse resp = (BaseResponse) response.body();
                L.d("profileTest", "resp.getCode() = " + resp.getCode());
                if (resp.getCode() != 100) {
                    onError(resp.getCode());
                    return;
                }
            }
            onSuccess(response.body());
        } else {
            L.d(TAG, "网络异常 " + response.code());
            onError(0);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError(-1);
    }

    abstract public void onSuccess(T body);

    abstract public void onError(int statusCode);
}
