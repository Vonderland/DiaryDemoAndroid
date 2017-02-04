package com.vonderland.diarydemo.detailpage;

import com.vonderland.diarydemo.BasePresenter;
import com.vonderland.diarydemo.BaseView;

/**
 * Created by Vonderland on 2017/2/4.
 */

public interface DetailPageContract {
    interface View extends BaseView<Presenter> {

        void showData(Object data);
    }

    interface Presenter extends BasePresenter {

        void startEdit(Object data);

        void setData(Object data);
    }
}
