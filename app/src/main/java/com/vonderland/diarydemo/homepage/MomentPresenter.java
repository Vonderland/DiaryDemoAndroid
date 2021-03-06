package com.vonderland.diarydemo.homepage;

import android.content.Context;
import android.content.Intent;

import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.bean.ListResponse;
import com.vonderland.diarydemo.bean.Moment;
import com.vonderland.diarydemo.bean.MomentModel;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.editpage.EditActivity;
import com.vonderland.diarydemo.network.BaseResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vonderland on 2017/2/3.
 */

public class MomentPresenter implements HomePageContract.Presenter {

    private Context context;
    private boolean isLoadingMore = false;
    private boolean hasMoreItems = true;
    private HomePageContract.View view;
    private MomentModel model;
    private List<Moment> data;
    private Moment footer;
    private Moment empty;
    private Moment noMore;
    private long timeCursor = 0;

    public MomentPresenter(Context context, HomePageContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.context = context;

        model = new MomentModel();
        data = new ArrayList<>();

        footer = new Moment();
        footer.setFooter(true);

        empty = new Moment();
        empty.setEmptyView(true);

        noMore = new Moment();
        noMore.setNoMore(true);
    }

    @Override
    public void refresh() {
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
    }

    @Override
    public void loadData(final boolean isRefreshing) {
        Map<String, String> options = new HashMap<>();
        if (!isRefreshing) {
            options.put(Constant.KEY_TIME_CURSOR, timeCursor + "");
        }
        if (isRefreshing && data.size() == 0) {
            data.addAll(model.getAllMomentFromRealm());
            if (data.size() == 0) {
                data.add(empty);
            }
            view.showData(data);
        }
        model.loadMoment(options, new BaseResponseHandler<ListResponse<Moment>>() {
            @Override
            public void onSuccess(ListResponse<Moment> body) {
                if (isRefreshing) {
                    data.clear();
                    data.addAll(body.getData());
                    model.deleteMomentInRealm();
                    model.insertMomentToRealm(body.getData());
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
                    model.insertMomentToRealm(body.getData());
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
    public void deleteData(int position) {
        model.deleteMoment(data.get(position).getId(), new BaseResponseHandler<ListResponse<Diary>>() {

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
        Moment moment = data.get(position);
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(Constant.KEY_EDIT_FROM, Constant.MOMENT_FROM_EDIT);
        intent.putExtra("data", moment);
        context.startActivity(intent);
    }

    @Override
    public void onDataChange(Object moment) {
        data.clear();
        List<Moment> realmResult = model.getAllMomentFromRealm();

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
}
