package com.vonderland.diarydemo.bean;

import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.network.DiaryDemoService;
import com.vonderland.diarydemo.network.ServiceGenerator;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Vonderland on 2017/2/2.
 */

public class DiaryCallModel {
    private DiaryDemoService apiService;

    public DiaryCallModel() {
        apiService = ServiceGenerator.createService(DiaryDemoService.class);
    }

    public void addDiary(RequestBody body, BaseResponseHandler handler) {
        Call<ListResponse<Diary>> call;
        call = apiService.addDiary(body);
        executeCall(call, handler);
    }

    public void updateDiary(RequestBody body, BaseResponseHandler handler) {
        Call<ListResponse<Diary>> call;
        call = apiService.updateDiary(body);
        executeCall(call, handler);
    }

    public void loadDiaries(Map<String, String> options, BaseResponseHandler handler) {
        Call<ListResponse<Diary>> call = apiService.loadDiaries(options);
        executeCall(call, handler);
    }

    public void loadAllDiaries(BaseResponseHandler handler) {
        Call<ListResponse<Diary>> call = apiService.loadAllDiaries();
        executeCall(call, handler);
    }

    public void deleteDiary(long id, BaseResponseHandler handler) {
        Call<ListResponse<Diary>> call = apiService.deleteDiary(id);
        executeCall(call, handler);
    }

    private void executeCall(Call call, BaseResponseHandler handler) {
        call.enqueue(handler);
    }
}
