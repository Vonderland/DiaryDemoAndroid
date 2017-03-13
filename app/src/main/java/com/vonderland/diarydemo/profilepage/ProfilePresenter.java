package com.vonderland.diarydemo.profilepage;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.vonderland.diarydemo.bean.User;
import com.vonderland.diarydemo.bean.UserModel;
import com.vonderland.diarydemo.bean.UserResponse;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.event.RefreshNavEvent;
import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.utils.L;
import com.vonderland.diarydemo.utils.PictureUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Vonderland on 2017/3/13.
 */

public class ProfilePresenter implements ProfilePageContract.Presenter {

    private boolean isLover;
    private UserModel userModel;
    private ProfilePageContract.View view;

    public ProfilePresenter(ProfilePageContract.View view, boolean isLover) {
        this.view = view;
        view.setPresenter(this);

        this.isLover = isLover;
        userModel = new UserModel();
    }

    @Override
    public void updateAvatar(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            File picture = new File(filePath);
            builder.addFormDataPart(Constant.KEY_AVATAR, picture.getName(),
                    RequestBody.create(MediaType.parse(PictureUtil.getMimeType(picture)), picture));
            RequestBody requestBody = builder.build();
            userModel.updateAvatar(requestBody, new BaseResponseHandler<UserResponse>() {
                @Override
                public void onSuccess(UserResponse body) {
                    if (body != null) {
                        User refreshUser = body.getData();
                        if (refreshUser != null) {
                            userModel.updateProfileInRealm(refreshUser);
                            view.showSuccess(ProfileFragment.SuccessType.UPDATE_AVATAR);
                            EventBus.getDefault().
                                    postSticky(new RefreshNavEvent(refreshUser.getAvatar(),
                                            refreshUser.getNickName()));
                        }
                    }
                }

                @Override
                public void onError(int statusCode) {
                    view.showError(statusCode);
                }
            });
        }
    }

    @Override
    public void updateNickName(final String nickName) {
        if (nickName != null) {
            userModel.updateNickName(nickName, new BaseResponseHandler<UserResponse>() {
                @Override
                public void onSuccess(UserResponse body) {
                    if (body != null) {
                        User refreshUser = body.getData();
                        if (refreshUser != null) {
                            userModel.updateProfileInRealm(refreshUser);
                            view.showSuccess(ProfileFragment.SuccessType.UPDATE_NICK_NAME);
                            EventBus.getDefault().
                                    postSticky(new RefreshNavEvent(refreshUser.getAvatar(),
                                            nickName));
                        }
                    }
                }

                @Override
                public void onError(int statusCode) {
                    view.showError(statusCode);
                }
            });
        }
    }

    @Override
    public void startLoverPage(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(Constant.KEY_IS_LOVER, true);
        context.startActivity(intent);
    }

    @Override
    public void setBlackHouse(boolean inBlackHouse) {

    }

    @Override
    public void startBreakUp() {

    }

    @Override
    public void start() {
        loadCachedData();
        refreshData();
    }

    private void loadCachedData() {
        User user;
        L.d("profileTest", "cachedata");
        if (!isLover) {
            user = userModel.getUserProfileFromRealm();
        } else {
            user = null;
        }
        view.showData(user, isLover);
    }

    private void refreshData() {
        if (!isLover) {
            userModel.getUserProfile(new BaseResponseHandler<UserResponse>() {
                @Override
                public void onSuccess(UserResponse body) {
                    L.d("profileTestonSuccess", "----");
                    if (body != null) {
                        User refreshUser = body.getData();
                        if (refreshUser != null) {
                            userModel.updateProfileInRealm(refreshUser);
                            view.showData(refreshUser, false);
                            L.d("profileTestonSuccess", "haha");
                            EventBus.getDefault().
                                    postSticky(new RefreshNavEvent(refreshUser.getAvatar(),
                                            refreshUser.getNickName()));
                        }
                    }
                }

                @Override
                public void onError(int statusCode) {
                    L.d("profileTestError", "code = " + statusCode);
                    view.showError(statusCode);
                }
            });
        } else {
            view.showData(null, isLover);
        }
    }
}
