package com.vonderland.diarydemo.registerpage;

import android.content.Context;

import com.vonderland.diarydemo.BasePresenter;
import com.vonderland.diarydemo.BaseView;

/**
 * Created by Vonderland on 2017/3/12.
 */

public interface RegisterPageContract {

    interface View extends BaseView<Presenter> {

        void showError(int code);

        void showProgressBar();

        void dismissProgressBar();
    }

    interface Presenter extends BasePresenter {

        void register(Context context, String email, String password, String nickName, boolean gender, String filePath);
    }
}
