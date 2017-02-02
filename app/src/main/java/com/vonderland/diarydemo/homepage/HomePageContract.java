package com.vonderland.diarydemo.homepage;

import com.vonderland.diarydemo.BasePresenter;
import com.vonderland.diarydemo.BaseView;

/**
 * Created by Vonderland on 2017/2/2.
 */

public interface HomePageContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void StopLoading();

        void showData();
    }

    interface Presenter extends BasePresenter {

        void refresh();

        void loadMore();
    }
}
