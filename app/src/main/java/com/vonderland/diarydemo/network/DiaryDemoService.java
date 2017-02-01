package com.vonderland.diarydemo.network;

import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.bean.ListResponse;
import com.vonderland.diarydemo.bean.Moment;
import com.vonderland.diarydemo.constant.Constant;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * Created by Vonderland on 2017/2/1.
 */

public interface DiaryDemoService {
    @GET("allDiaries")
    Call<ListResponse<Diary>> loadAllDiaries();

    @GET("diaries")
    Call<ListResponse<Diary>> loadDiaries(@QueryMap Map<String, String> options);

    @FormUrlEncoded
    @POST("addDiary")
    Call<ListResponse<Diary>> addDiaryWithoutPicture(@FieldMap Map<String, String> options);

    @FormUrlEncoded
    @Multipart
    @POST("addDiary")
    Call<ListResponse<Diary>> addDiaryWithPicture(@FieldMap Map<String, String> options,
                                                  @Part(Constant.KEY_PICTURE) RequestBody picture);
    @FormUrlEncoded
    @POST("updateDiary")
    Call<ListResponse<Diary>> uppdateDiaryWithoutPicture(@FieldMap Map<String, String> options);

    @FormUrlEncoded
    @Multipart
    @POST("updateDiary")
    Call<ListResponse<Diary>> updateDiaryWithPicture(@FieldMap Map<String, String> options,
                                                  @Part(Constant.KEY_PICTURE) RequestBody picture);
    @FormUrlEncoded
    @POST("deleteDiary")
    Call<ListResponse<Diary>> deleteDiary(@Field(Constant.KEY_ID) long id);


    @GET("allMoment")
    Call<ListResponse<Moment>> loadAllMoment();

    @GET("moment")
    Call<ListResponse<Moment>> loadMoment(@QueryMap Map<String, String> options);

    @FormUrlEncoded
    @Multipart
    @POST("addMoment")
    Call<ListResponse<Moment>> addMoment(@FieldMap Map<String, String> options);

    @FormUrlEncoded
    @POST("updateMoment")
    Call<ListResponse<Moment>> uppdateMoment(@FieldMap Map<String, String> options);

    @FormUrlEncoded
    @POST("deleteMoment")
    Call<ListResponse<Moment>> deleteMoment(@Field(Constant.KEY_ID) long id);
}
