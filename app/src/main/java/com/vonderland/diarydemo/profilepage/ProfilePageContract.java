package com.vonderland.diarydemo.profilepage;

import android.content.Context;

import com.vonderland.diarydemo.BasePresenter;
import com.vonderland.diarydemo.BaseView;
import com.vonderland.diarydemo.bean.User;

/**
 * Created by Vonderland on 2017/3/13.
 */

public interface ProfilePageContract {

    interface View extends BaseView<Presenter> {
        void showData(User user, boolean isLover);
        void showSuccess(ProfileFragment.SuccessType type);
        void showError(int code);
    }

    interface Presenter extends BasePresenter {
        void updateAvatar(String filePath);
        void updateNickName(String nickName);
        void startLoverPage(Context context);
        void setBlackHouse(boolean inBlackHouse);
        void startBreakUp(Context context);
    }
}
