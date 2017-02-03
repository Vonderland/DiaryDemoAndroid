package com.vonderland.diarydemo.homepage;

import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.bean.DiaryCallModel;
import com.vonderland.diarydemo.bean.ListResponse;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.utils.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vonderland on 2017/2/3.
 */

public class DiaryPresenter implements HomePageContract.Presenter {

    private boolean isLoadingMore = false;
    private boolean hasMoreItems = true;
    private HomePageContract.View view;
    private DiaryCallModel model;
    private List<Diary> data;
    private Diary footer;
    private Diary empty;
    private Diary noMore;
    private long timeCursor;

    public DiaryPresenter (HomePageContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        model = new DiaryCallModel();
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
        model.loadDiaries(options, new BaseResponseHandler<ListResponse<Diary>>() {
            @Override
            public void onSuccess(ListResponse<Diary> body) {
                if (isRefreshing) {
                    data.clear();
                    data.addAll(body.getData());
                    int bodySize = body.getData().size();
                    if (data.size() == 0) {
                        data.add(empty);
                    } else if (bodySize < Constant.PAGE_SIZE) {
                        hasMoreItems = false;
                        data.add(noMore);
                    } else {
                        timeCursor = body.getData().get(bodySize - 1).getEventTime();
                    }
                    view.showData(data);
                    view.stopRefreshing();
                } else {
                    if (data.get(data.size() - 1).isFooter()) {
                        data.remove(data.size() - 1);
                    }
                    data.addAll(body.getData());
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
    }
}
