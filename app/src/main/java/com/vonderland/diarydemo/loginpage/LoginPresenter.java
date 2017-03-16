package com.vonderland.diarydemo.loginpage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.AuthModel;
import com.vonderland.diarydemo.bean.AuthResponse;
import com.vonderland.diarydemo.bean.Authorization;
import com.vonderland.diarydemo.bean.User;
import com.vonderland.diarydemo.bean.UserModel;
import com.vonderland.diarydemo.bean.UserResponse;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.couplepages.BlackHouseActivity;
import com.vonderland.diarydemo.couplepages.SingleActivity;
import com.vonderland.diarydemo.event.RefreshNavEvent;
import com.vonderland.diarydemo.homepage.MainActivity;
import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.registerpage.RegisterActivity;
import com.vonderland.diarydemo.utils.CipherUtil;
import com.vonderland.diarydemo.utils.L;
import com.vonderland.diarydemo.utils.SharedPrefUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vonderland on 2017/3/12.
 */

public class LoginPresenter implements LoginPageContract.Presenter{

    private LoginPageContract.View view;
    private AuthModel authModel;
    private UserModel userModel;
    private SharedPrefUtil spUtil;
    private long lastForget;

    public LoginPresenter(LoginPageContract.View view) {
        this.view = view;
        this.view.setPresenter(this);

        authModel = new AuthModel();
        spUtil = SharedPrefUtil.getInstance();
    }

    @Override
    public void login(final Context context, String email, String password) {
        String encodedEmail = CipherUtil.encodeDataBase64(email);
        String encodedPassword = CipherUtil.encodeData(password);
        L.d("ciperTest", "email = " + encodedEmail + " password = " + encodedPassword);
        view.showProgressBar();

        Map<String, String> options = new HashMap<String, String>();
        options.put(Constant.KEY_EMAIL, encodedEmail);
        options.put(Constant.KEY_PASSWORD, encodedPassword);
        authModel.login(options, new BaseResponseHandler<AuthResponse>() {
            @Override
            public void onSuccess(AuthResponse body) {
                if (body != null) {
                    Authorization authorization = body.getData();
                    if (authorization != null) {
                        spUtil.put(Constant.SP_KEY_TOKEN, authorization.getToken());
                        spUtil.put(Constant.SP_KEY_UID, authorization.getUid());
                        L.d("loginResponse", "token = " + authorization.getToken() + " uid = " + authorization.getUid());
                        startNextActivity(context);
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
    public void setStatus(boolean remember) {
        spUtil.put(Constant.SP_KEY_REMEMBER_STATUS, remember);
    }

    @Override
    public void startRegister(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void startForget(Context context) {

    }

    @Override
    public void start() {
        boolean remember = (boolean)spUtil.get(Constant.SP_KEY_REMEMBER_STATUS, false);
        view.showStatus(remember);
    }

    @Override
    public void forgetPassword(String email) {
        if (java.util.Calendar.getInstance().getTimeInMillis() - lastForget > 60 * 1000) {
            lastForget = java.util.Calendar.getInstance().getTimeInMillis();
            String encodedEmail = CipherUtil.encodeDataBase64(email);
            authModel.forgetPassword(encodedEmail, new BaseResponseHandler() {
                @Override
                public void onSuccess(Object body) {
                    view.showEmailSuccess();
                    lastForget = 0;
                }

                @Override
                public void onError(int statusCode) {
                    view.showError(statusCode);
                    lastForget = 0;
                }
            });
        } else {
            view.showWaitMessage();
        }
    }

    @Override
    public void setHostAddress(String host) {
        spUtil.put(Constant.SP_KEY_HOST, host);
    }

    @Override
    public String getHostAddress() {
        return (String)spUtil.get(Constant.SP_KEY_HOST, Constant.HOST);
    }

    private void startNextActivity(final Context context) {
        // auth 返回后才有 token 信息，因此在这里才进行初始化
        userModel = new UserModel();
        userModel.getUserProfile(new BaseResponseHandler<UserResponse>() {

            @Override
            public void onSuccess(UserResponse body) {
                if (body != null) {
                    User refreshUser = body.getData();
                    if (refreshUser != null) {
                        userModel.updateProfileInRealm(refreshUser);
                        spUtil.put(Constant.SP_KEY_IS_BLACK, refreshUser.isBlack());
                        spUtil.put(Constant.SP_KEY_LOVER_ID, refreshUser.getLoverId());
                        Intent intent;
                        if (refreshUser.isBlack()) {
                            intent = new Intent(context, BlackHouseActivity.class);
                        } else if (refreshUser.getLoverId() == -1) {
                            intent = new Intent(context, SingleActivity.class);
                        } else {
                            intent = new Intent(context, MainActivity.class);
                        }
                        view.dismissProgressBar();
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                }
            }

            @Override
            public void onError(int statusCode) {

            }
        });
    }
}
