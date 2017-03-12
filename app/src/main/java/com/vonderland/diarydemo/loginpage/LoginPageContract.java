package com.vonderland.diarydemo.loginpage;

import android.content.Context;

import com.vonderland.diarydemo.BasePresenter;
import com.vonderland.diarydemo.BaseView;

/**
 * Created by Vonderland on 2017/3/12.
 */

public interface LoginPageContract {
    interface View extends BaseView<Presenter> {

        void showStatus(boolean remember);

        void showProgressBar();

        void dismissProgressBar();

        void showError(int code);
    }

    interface Presenter extends BasePresenter {

        void login(Context context, String email, String password);

        void setStatus(boolean remember);

        void startRegister(Context context);

        void startForget(Context context);
    }
}
