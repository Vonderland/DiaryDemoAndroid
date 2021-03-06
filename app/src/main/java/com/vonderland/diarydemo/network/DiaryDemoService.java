package com.vonderland.diarydemo.network;

import com.vonderland.diarydemo.bean.AuthResponse;
import com.vonderland.diarydemo.bean.BaseResponse;
import com.vonderland.diarydemo.bean.BooleanResponse;
import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.bean.ListResponse;
import com.vonderland.diarydemo.bean.Moment;
import com.vonderland.diarydemo.bean.RequestResponse;
import com.vonderland.diarydemo.bean.UserResponse;
import com.vonderland.diarydemo.constant.Constant;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

/**
 * Created by Vonderland on 2017/2/1.
 */

public interface DiaryDemoService {

    @GET("allDiaries")
    Call<ListResponse<Diary>> loadAllDiaries();

    @GET("diaries")
    Call<ListResponse<Diary>> loadDiaries(@QueryMap Map<String, String> options);

    @POST("addDiary")
    Call<ListResponse<Diary>> addDiary(@Body RequestBody body);

    @POST("updateDiary")
    Call<ListResponse<Diary>> updateDiary(@Body RequestBody body);

    @FormUrlEncoded
    @POST("deleteDiary")
    Call<ListResponse<Diary>> deleteDiary(@Field(Constant.KEY_ID) long id);

    @GET("allMoment")
    Call<ListResponse<Moment>> loadAllMoment();

    @GET("moment")
    Call<ListResponse<Moment>> loadMoment(@QueryMap Map<String, String> options);

    @FormUrlEncoded
    @POST("addMoment")
    Call<ListResponse<Moment>> addMoment(@FieldMap Map<String, String> options);

    @FormUrlEncoded
    @POST("updateMoment")
    Call<ListResponse<Moment>> updateMoment(@FieldMap Map<String, String> options);

    @FormUrlEncoded
    @POST("deleteMoment")
    Call<ListResponse<Moment>> deleteMoment(@Field(Constant.KEY_ID) long id);

    @FormUrlEncoded
    @POST("login")
    Call<AuthResponse> login(@FieldMap Map<String, String> options);

    @POST("register")
    Call<AuthResponse> register(@Body RequestBody body);

    @GET("userProfile")
    Call<UserResponse> getUserProfile();

    @POST("updateAvatar")
    Call<UserResponse> updateAvatar(@Body RequestBody body);

    @FormUrlEncoded
    @POST("updateNickName")
    Call<UserResponse> updateNickName(@Field(Constant.KEY_NICK_NAME) String nickName);

    @FormUrlEncoded
    @POST("resetPassword")
    Call<AuthResponse> resetPassword(@Field(Constant.KEY_PASSWORD) String password,
                                     @Field(Constant.KEY_NEW_PASSWORD) String newPassword);

    @FormUrlEncoded
    @POST("forgetPassword")
    Call<BaseResponse> forgetPassword(@Field(Constant.KEY_EMAIL) String email);

    @FormUrlEncoded
    @POST("sendRequest")
    Call<BaseResponse> sendRequest(@Field(Constant.KEY_EMAIL) String email);

    @FormUrlEncoded
    @POST("acceptRequest")
    Call<BaseResponse> acceptRequest(@Field(Constant.KEY_ID) long id);

    @FormUrlEncoded
    @POST("rejectRequest")
    Call<BaseResponse> rejectRequest(@Field(Constant.KEY_ID) long id);

    @GET("checkRequest")
    Call<RequestResponse> checkRequest();

    @GET("hasLover")
    Call<BooleanResponse> hasLover();

    @GET("isBlack")
    Call<BooleanResponse> isBlack();

    @FormUrlEncoded
    @POST("setIsBlack")
    Call<BaseResponse> setIsBlack(@Field(Constant.KEY_IS_BLACK) boolean isBlack);

    @POST("breakUp")
    Call<BaseResponse> breakUp();

    @GET("loverProfile")
    Call<UserResponse> getLoverProfile();
}
