package com.vonderland.diarydemo.homepage;

import com.vonderland.diarydemo.bean.ListResponse;
import com.vonderland.diarydemo.bean.Moment;
import com.vonderland.diarydemo.bean.MomentCallModel;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.network.BaseResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vonderland on 2017/2/3.
 */

public class MomentPresenter implements HomePageContract.Presenter {
    private boolean isLoadingMore = false;
    private boolean hasMoreItems = true;
    private HomePageContract.View view;
    private MomentCallModel model;
    private List<Moment> data;
    private Moment footer;
    private Moment empty;
    private Moment noMore;
    private long timeCursor;

    public MomentPresenter(HomePageContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        model = new MomentCallModel();
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
        model.loadMoment(options, new BaseResponseHandler<ListResponse<Moment>>() {
            @Override
            public void onSuccess(ListResponse<Moment> body) {
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
}
