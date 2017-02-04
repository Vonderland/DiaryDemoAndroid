package com.vonderland.diarydemo.bean;

import android.support.annotation.Nullable;

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

    public void addDiary(Map<String, String> options, @Nullable RequestBody picture,
                         BaseResponseHandler handler) {
        Call<ListResponse<Diary>> call;
        if (picture == null) {
            call = apiService.addDiaryWithoutPicture(options);
        } else {
            call = apiService.addDiaryWithPicture(options, picture);
        }
        executeCall(call, handler);
    }

    public void updateDiary(Map<String, String> options, @Nullable RequestBody picture,
    BaseResponseHandler handler) {
        Call<ListResponse<Diary>> call;
        if (picture == null) {
            call = apiService.updateDiaryWithoutPicture(options);
        } else {
            call = apiService.updateDiaryWithPicture(options, picture);
        }
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
