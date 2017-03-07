package com.vonderland.diarydemo.editpage;

import com.vonderland.diarydemo.BasePresenter;
import com.vonderland.diarydemo.BaseView;
import com.vonderland.diarydemo.bean.Moment;

/**
 * Created by Vonderland on 2017/2/7.
 */

public interface EditMomentPageContract {
    interface View extends BaseView<Presenter> {
        void showData(Moment data);
        void showError(int code);
        void showSuccessfully(boolean isEdit);
    }

    interface Presenter extends BasePresenter {
        void postData(String title, long date, String location);
        void setData(Moment data);
    }
}
