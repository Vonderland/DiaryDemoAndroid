package com.vonderland.diarydemo.homepage;

import android.content.Context;
import android.content.Intent;

import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.bean.DiaryModel;
import com.vonderland.diarydemo.bean.ListResponse;
import com.vonderland.diarydemo.bean.User;
import com.vonderland.diarydemo.bean.UserModel;
import com.vonderland.diarydemo.bean.UserResponse;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.detailpage.DetailActivity;
import com.vonderland.diarydemo.event.RefreshNavEvent;
import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.utils.L;
import com.vonderland.diarydemo.utils.SharedPrefUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vonderland on 2017/2/3.
 */

public class DiaryPresenter implements HomePageContract.Presenter {

    private Context context;
    private boolean isLoadingMore = false;
    private boolean hasMoreItems = true;
    private HomePageContract.View view;
    private DiaryModel diaryModel;
    private UserModel userModel;
    private List<Diary> data;
    private Diary footer;
    private Diary empty;
    private Diary noMore;
    private long timeCursor = 0;
    private SharedPrefUtil sharedPrefUtil;

    public DiaryPresenter (Context context, HomePageContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.context = context;
        sharedPrefUtil = SharedPrefUtil.getInstance();

        diaryModel = new DiaryModel();
        userModel = new UserModel();
        data = new ArrayList<>();

        footer = new Diary();
        footer.setFooter(true);

        empty = new Diary();
        empty.setEmptyView(true);

        noMore = new Diary();
        noMore.setNoMore(true);
    }

    @Override
    public void refresh() {
        hasMoreItems = true;
        view.showRefreshing();
        loadData(true);
    }

    @Override
    public void loadMore() {
        isLoadingMore = true;
        data.add(footer);
        view.showData(data);

        loadData(false);
    }

    @Override
    public void loadData(final boolean isRefreshing) {
        Map<String, String> options = new HashMap<>();
        if (!isRefreshing) {
            options.put(Constant.KEY_TIME_CURSOR, timeCursor + "");
        }
        if (isRefreshing && data.size() == 0) {
            data.addAll(diaryModel.getAllDiariesFromRealm());
            if (data.size() == 0) {
                data.add(empty);
            }
            view.showData(data);
        }
        diaryModel.loadDiaries(options, new BaseResponseHandler<ListResponse<Diary>>() {
            @Override
            public void onSuccess(ListResponse<Diary> body) {
                if (isRefreshing) {
                    data.clear();
                    data.addAll(body.getData());
                    diaryModel.deleteDiariesInRealm();
                    diaryModel.insertDiariesToRealm(body.getData());
                    int bodySize = body.getData().size();
                    if (data.size() == 0) {
                        data.add(empty);
                    } else if (bodySize < Constant.PAGE_SIZE) {
                        hasMoreItems = false;
                        data.add(noMore);
                    } else {
                        hasMoreItems = true;
                        timeCursor = body.getData().get(bodySize - 1).getEventTime();
                    }
                    view.showData(data);
                    view.stopRefreshing();
                } else {
                    if (data.get(data.size() - 1).isFooter()) {
                        data.remove(data.size() - 1);
                    }
                    data.addAll(body.getData());
                    diaryModel.insertDiariesToRealm(body.getData());
                    int bodySize = body.getData().size();
                    if (bodySize < Constant.PAGE_SIZE) {
                        hasMoreItems = false;
                        data.add(noMore);
                    } else {
                        timeCursor = body.getData().get(bodySize - 1).getEventTime();
                    }
                    view.showData(data);
                    isLoadingMore = false;
                }
            }

            @Override
            public void onError(int statusCode) {
                if (isRefreshing) {
                    view.stopRefreshing();
                } else {
                    if (data.get(data.size() - 1).isFooter()) {
                        data.remove(data.size() - 1);
                        view.showData(data);
                    }
                    isLoadingMore = false;
                }
                view.showError();
            }
        });
    }

    @Override
    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    @Override
    public boolean hasMoreItems() {
        return hasMoreItems;
    }

    @Override
    public void start() {
        refresh();
        refreshUserProfile();
    }

    @Override
    public void deleteData(int position) {
        diaryModel.deleteDiary(data.get(position).getId(), new BaseResponseHandler<ListResponse<Diary>>() {

            @Override
            public void onSuccess(ListResponse<Diary> body) {
                view.showDeleteSuccessfully();
            }

            @Override
            public void onError(int statusCode) {
                view.showError();
            }
        });
        data.remove(position);
    }

    @Override
    public void startDetail(int position) {
        Diary diary = data.get(position);
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("data", diary);
        context.startActivity(intent);
    }

    @Override
    public void onDataChange(Object diary) {
        data.clear();
        List<Diary> realmResult = diaryModel.getAllDiariesFromRealm();

        if (realmResult.size() == 0) {
            data.add(empty);
        } else {
            if (realmResult.get(realmResult.size() - 1).getEventTime() < timeCursor && hasMoreItems) {
                realmResult.remove(realmResult.size() - 1);
            }
            data.addAll(realmResult);
        }

        if (!hasMoreItems) {
            data.add(noMore);
        }
        view.showData(data);
    }

    private void refreshUserProfile() {
        User user = userModel.getUserProfileFromRealm();
        if (user != null) {
            L.d("diaryPresenterProfile", user.getUid() + " " + user.getAvatar() + " " + user.getNickName());
            EventBus.getDefault().postSticky(new RefreshNavEvent(user.getAvatar(), user.getNickName()));
        }
        userModel.getUserProfile(new BaseResponseHandler<UserResponse>() {

            @Override
            public void onSuccess(UserResponse body) {
                if (body != null) {
                    User refreshUser = body.getData();
                    if (refreshUser != null) {
                        userModel.updateProfileInRealm(refreshUser);
                        sharedPrefUtil.put(Constant.SP_KEY_IS_BLACK, refreshUser.isBlack());
                        sharedPrefUtil.put(Constant.SP_KEY_LOVER_ID, refreshUser.getLoverId());
                        sharedPrefUtil.put(Constant.SP_KEY_IS_BLACK, refreshUser.isBlack());
                        sharedPrefUtil.put(Constant.SP_KEY_LOVER_ID, refreshUser.getLoverId());
                        EventBus.getDefault().
                                postSticky(new RefreshNavEvent(refreshUser.getAvatar(), refreshUser.getNickName()));
                    }
                }
            }

            @Override
            public void onError(int statusCode) {

            }
        });
    }
}
