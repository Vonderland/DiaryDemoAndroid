package com.vonderland.diarydemo.bean;

import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.network.DiaryDemoService;
import com.vonderland.diarydemo.network.ServiceGenerator;

import java.util.Map;

import retrofit2.Call;

/**
 * Created by Vonderland on 2017/2/2.
 */

public class MomentCallModel {
    private DiaryDemoService apiService;

    public MomentCallModel() {
        apiService = ServiceGenerator.createService(DiaryDemoService.class);
    }

    public void addMoment(Map<String, String> options, BaseResponseHandler handler) {
        Call<ListResponse<Moment>> call;
        call = apiService.addMoment(options);
        executeCall(call, handler);
    }

    public void updateMoment(Map<String, String> options, BaseResponseHandler handler) {
        Call<ListResponse<Moment>> call;
        call = apiService.uppdateMoment(options);
        executeCall(call, handler);
    }

    public void loadMoment(Map<String, String> options, BaseResponseHandler handler) {
        Call<ListResponse<Moment>> call = apiService.loadMoment(options);
        executeCall(call, handler);
    }

    public void loadAllMoment(BaseResponseHandler handler) {
        Call<ListResponse<Moment>> call = apiService.loadAllMoment();
        executeCall(call, handler);
    }

    public void deleteMoment(long id, BaseResponseHandler handler) {
        Call<ListResponse<Moment>> call = apiService.deleteMoment(id);
        executeCall(call, handler);
    }

    private void executeCall(Call call, BaseResponseHandler handler) {
        call.enqueue(handler);
    }
}
