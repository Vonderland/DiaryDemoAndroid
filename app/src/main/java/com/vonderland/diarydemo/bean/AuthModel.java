package com.vonderland.diarydemo.bean;

import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.network.DiaryDemoService;
import com.vonderland.diarydemo.network.ServiceGenerator;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Vonderland on 2017/3/12.
 */

public class AuthModel {
    private DiaryDemoService apiService;

    public AuthModel() {
        apiService = ServiceGenerator.createService(DiaryDemoService.class);
    }

    public void login(Map<String, String> options, BaseResponseHandler handler) {
        Call<AuthResponse> call = apiService.login(options);
        executeCall(call, handler);
    }

    public void register(RequestBody body, BaseResponseHandler handler) {
        Call<AuthResponse> call = apiService.register(body);
        executeCall(call, handler);
    }

    private void executeCall(Call call, BaseResponseHandler handler) {
        call.enqueue(handler);
    }
}
