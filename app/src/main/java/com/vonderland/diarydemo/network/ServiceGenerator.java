package com.vonderland.diarydemo.network;

import android.text.TextUtils;

import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.utils.SharedPrefUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vonderland on 2017/2/1.
 */

public class ServiceGenerator {
    public static <S> S createService(Class<S> serviceClass) {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        SharedPrefUtil sharedPrefUtil = SharedPrefUtil.getInstance();
                        final String token = (String) sharedPrefUtil.get(Constant.SP_KEY_TOKEN, "");

                        Request originalRequest = chain.request();
                        Request.Builder requestBuilder;
                        // 当用户已登录时,在 request header 中添加 Authorization 字段
                        if (TextUtils.isEmpty(token)) {
                            requestBuilder = originalRequest.newBuilder()
                                    .method(originalRequest.method(), originalRequest.body());
                        } else {
                            requestBuilder = originalRequest.newBuilder()
                                    .header("Authorization", token)
                                    .method(originalRequest.method(), originalRequest.body());
                        }
                        Request request = requestBuilder.build();

                        Response response = chain.proceed(request);
                        return response.newBuilder()
                                .build();
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constant.HOST)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}
