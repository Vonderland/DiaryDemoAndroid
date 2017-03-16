package com.vonderland.diarydemo.bean;

import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.network.DiaryDemoService;
import com.vonderland.diarydemo.network.ServiceGenerator;

import retrofit2.Call;

/**
 * Created by Vonderland on 2017/3/16.
 */

public class CoupleModel {
    private DiaryDemoService apiService;

    public CoupleModel() {
        apiService = ServiceGenerator.createService(DiaryDemoService.class);
    }

    public void sendRequest(String email, BaseResponseHandler handler) {
        Call<BaseResponse> call = apiService.sendRequest(email);
        executeCall(call, handler);
    }

    public void acceptRequest(long id, BaseResponseHandler handler) {
        Call<BaseResponse> call = apiService.acceptRequest(id);
        executeCall(call, handler);
    }

    public void rejectRequest(long id, BaseResponseHandler handler) {
        Call<BaseResponse> call = apiService.rejectRequest(id);
        executeCall(call, handler);
    }

    public void checkRequest(BaseResponseHandler handler) {
        Call<RequestResponse> call = apiService.checkRequest();
        executeCall(call,handler);
    }

    public void hasLover(BaseResponseHandler handler) {
        Call<BooleanResponse> call = apiService.hasLover();
        executeCall(call, handler);
    }

    public void isBlack(BaseResponseHandler handler) {
        Call<BooleanResponse> call = apiService.isBlack();
        executeCall(call, handler);
    }

    public void setIsBlack(boolean isBlack, BaseResponseHandler handler) {
        Call<BaseResponse> call = apiService.setIsBlack(isBlack);
        executeCall(call, handler);
    }

    public void breakUp(BaseResponseHandler handler) {
        Call<BaseResponse> call = apiService.breakUp();
        executeCall(call, handler);
    }

    private void executeCall(Call call, BaseResponseHandler handler) {
        call.enqueue(handler);
    }
}
