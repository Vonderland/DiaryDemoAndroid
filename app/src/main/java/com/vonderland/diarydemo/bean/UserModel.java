package com.vonderland.diarydemo.bean;

import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.network.DiaryDemoService;
import com.vonderland.diarydemo.network.ServiceGenerator;
import com.vonderland.diarydemo.utils.SharedPrefUtil;

import io.realm.Realm;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Vonderland on 2017/3/12.
 */

public class UserModel {
    private DiaryDemoService apiService;
    private Realm realm;

    public UserModel() {
        apiService = ServiceGenerator.createService(DiaryDemoService.class);
        realm = Realm.getDefaultInstance();
    }

    public void getUserProfile(BaseResponseHandler handler) {
        Call<UserResponse> call = apiService.getUserProfile();
        executeCall(call, handler);
    }

    public void updateAvatar(RequestBody body, BaseResponseHandler handler) {
        Call<UserResponse> call = apiService.updateAvatar(body);
        executeCall(call, handler);
    }

    public void updateNickName(String nickName, BaseResponseHandler handler) {
        Call<UserResponse> call = apiService.updateNickName(nickName);
        executeCall(call, handler);
    }

    private void executeCall(Call call, BaseResponseHandler handler) {
        call.enqueue(handler);
    }

    public void updateProfileInRealm(User user) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
    }

    public User getUserProfileFromRealm() {
        long uid = (long)SharedPrefUtil.getInstance().get(Constant.SP_KEY_UID, 0L);
        User result = realm.where(User.class).equalTo("uid", uid).findFirst();
        if (result != null) {
            result = realm.copyFromRealm(result);
        }
        return result;
    }

//    public User getLoverProfileFromRealm() {
//        long uid = (long)SharedPrefUtil.getInstance().get(Constant.SP_KEY_UID, 0);
//        User result = realm.copyFromRealm(realm.where(User.class).equalTo("uid", uid).findFirst());
//        return result;
//    }
}
