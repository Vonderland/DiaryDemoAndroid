package com.vonderland.diarydemo.bean;

import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.network.DiaryDemoService;
import com.vonderland.diarydemo.network.ServiceGenerator;

import java.util.List;
import java.util.Map;

import io.realm.Realm;
import retrofit2.Call;

/**
 * Created by Vonderland on 2017/2/2.
 */

public class MomentModel {
    private DiaryDemoService apiService;
    private Realm realm;

    public MomentModel() {
        apiService = ServiceGenerator.createService(DiaryDemoService.class);
        realm = Realm.getDefaultInstance();
    }

    public void addMoment(Map<String, String> options, BaseResponseHandler handler) {
        Call<ListResponse<Moment>> call;
        call = apiService.addMoment(options);
        executeCall(call, handler);
    }

    public void updateMoment(Map<String, String> options, BaseResponseHandler handler) {
        Call<ListResponse<Moment>> call;
        call = apiService.updateMoment(options);
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
        deleteMomentInRealm(id);
        Call<ListResponse<Moment>> call = apiService.deleteMoment(id);
        executeCall(call, handler);
    }

    private void executeCall(Call call, BaseResponseHandler handler) {
        call.enqueue(handler);
    }

    public List<Moment> getAllMomentFromRealm() {
        List<Moment> result;
        result = realm.copyFromRealm(realm.where(Moment.class).findAll());
        return result;
    }

    public void insertMomentToRealm(List<Moment> data) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(data);
        realm.commitTransaction();
    }

    public void deleteMomentInRealm() {
        realm.beginTransaction();
        realm.where(Moment.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    public void deleteMomentInRealm(long id) {
        realm.beginTransaction();
        realm.where(Moment.class)
                .equalTo("id", id)
                .findAll()
                .deleteAllFromRealm();
        realm.commitTransaction();
    }
}
