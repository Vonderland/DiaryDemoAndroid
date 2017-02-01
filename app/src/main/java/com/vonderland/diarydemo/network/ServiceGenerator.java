package com.vonderland.diarydemo.network;

import android.text.TextUtils;

import com.vonderland.diarydemo.constant.Constant;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vonderland on 2017/2/1.
 */

public class ServiceGenerator {
    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constant.HOST)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
        return retrofit.create(serviceClass);
    }
}
