package com.vonderland.diarydemo.homepage;

import com.vonderland.diarydemo.BasePresenter;
import com.vonderland.diarydemo.BaseView;

import java.util.List;

/**
 * Created by Vonderland on 2017/2/2.
 */

public interface HomePageContract {

    interface View extends BaseView<Presenter> {

        void showRefreshing();

        void stopRefreshing();

        void showData(List data);

        void showError();

        void showDeleteSuccessfully();
    }

    interface Presenter extends BasePresenter {

        void refresh();

        void loadMore();

        boolean isLoadingMore();

        boolean hasMoreItems();

        void loadData(boolean isRefreshing);

        void deleteData(int position);

        void startDetail(int position);
    }
}
