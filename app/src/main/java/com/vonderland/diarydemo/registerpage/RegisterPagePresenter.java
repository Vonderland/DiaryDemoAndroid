package com.vonderland.diarydemo.registerpage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.vonderland.diarydemo.bean.AuthModel;
import com.vonderland.diarydemo.bean.AuthResponse;
import com.vonderland.diarydemo.bean.Authorization;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.event.RegisterFinishEvent;
import com.vonderland.diarydemo.homepage.MainActivity;
import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.utils.CipherUtil;
import com.vonderland.diarydemo.utils.L;
import com.vonderland.diarydemo.utils.PictureUtil;
import com.vonderland.diarydemo.utils.SharedPrefUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Vonderland on 2017/3/12.
 */

public class RegisterPagePresenter implements RegisterPageContract.Presenter {

    private RegisterPageContract.View view;
    private AuthModel authModel;
    private SharedPrefUtil spUtil;

    public RegisterPagePresenter(RegisterPageContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        authModel = new AuthModel();
        spUtil = SharedPrefUtil.getInstance();
    }
    @Override
    public void register(final Context context, String email, String password, String nickName, boolean gender, String filePath) {
        view.showProgressBar();
        String encodedEmail = CipherUtil.encodeDataBase64(email);
        String encodedPassword = CipherUtil.encodeData(password);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart(Constant.KEY_EMAIL, encodedEmail)
                .addFormDataPart(Constant.KEY_PASSWORD, encodedPassword)
                .addFormDataPart(Constant.KEY_NICK_NAME, nickName);
        if (gender) {
            builder.addFormDataPart(Constant.KEY_GENDER, "1");
        } else {
            builder.addFormDataPart(Constant.KEY_GENDER, "0");
        }
        if (!TextUtils.isEmpty(filePath)) {
            File picture = new File(filePath);
            builder.addFormDataPart(Constant.KEY_AVATAR, picture.getName(),
                    RequestBody.create(MediaType.parse(PictureUtil.getMimeType(picture)), picture));
        }

        RequestBody requestBody = builder.build();
        authModel.register(requestBody, new BaseResponseHandler<AuthResponse>() {
            @Override
            public void onSuccess(AuthResponse body) {
                if (body != null) {
                    Authorization authorization = body.getData();
                    if (authorization != null) {
                        spUtil.put(Constant.SP_KEY_TOKEN, authorization.getToken());
                        spUtil.put(Constant.SP_KEY_UID, authorization.getUid());
                        L.d("registerResponse", "token = " + authorization.getToken() + " uid = " + authorization.getUid());
                        view.dismissProgressBar();

                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                        EventBus.getDefault().post(new RegisterFinishEvent());
                    }
                }
            }

            @Override
            public void onError(int statusCode) {
                view.dismissProgressBar();
                view.showError(statusCode);
            }
        });
    }

    @Override
    public void start() {

    }
}
