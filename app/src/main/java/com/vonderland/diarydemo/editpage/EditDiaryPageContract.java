package com.vonderland.diarydemo.editpage;

import com.vonderland.diarydemo.BasePresenter;
import com.vonderland.diarydemo.BaseView;
import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.detailpage.DetailPageContract;

/**
 * Created by Vonderland on 2017/2/4.
 */

public interface EditDiaryPageContract {
    interface View extends BaseView<Presenter> {
        void showData(Diary data);
        void showError(int code);
        void showSuccessfully(boolean isEdit);
    }

    interface Presenter extends BasePresenter {
        void postData(String title, long date, String description, String filePath, int change, boolean isPrivate);
        void setData(Diary data);
    }
}
