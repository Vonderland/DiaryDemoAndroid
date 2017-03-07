package com.vonderland.diarydemo.bean;

import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.network.DiaryDemoService;
import com.vonderland.diarydemo.network.ServiceGenerator;

import java.util.List;
import java.util.Map;

import io.realm.Realm;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Vonderland on 2017/2/2.
 */

public class DiaryModel {
    private DiaryDemoService apiService;
    private Realm realm;

    public DiaryModel() {
        apiService = ServiceGenerator.createService(DiaryDemoService.class);
        realm = Realm.getDefaultInstance();
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
        deleteDiaryInRealm(id);
        Call<ListResponse<Diary>> call = apiService.deleteDiary(id);
        executeCall(call, handler);
    }

    private void executeCall(Call call, BaseResponseHandler handler) {
        call.enqueue(handler);
    }

    public List<Diary> getAllDiariesFromRealm() {
        List<Diary> result;
        result = realm.copyFromRealm(realm.where(Diary.class).findAll());
        return result;
    }

    public void insertDiariesToRealm(List<Diary> data) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(data);
        realm.commitTransaction();
    }

    public void deleteDiariesInRealm() {
        realm.beginTransaction();
        realm.where(Diary.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    public void deleteDiaryInRealm(long id) {
        realm.beginTransaction();
        realm.where(Diary.class)
                .equalTo("id", id)
                .findAll()
                .deleteAllFromRealm();
        realm.commitTransaction();
    }
}
