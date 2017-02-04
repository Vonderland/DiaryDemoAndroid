package com.vonderland.diarydemo.editpage;

import com.vonderland.diarydemo.BasePresenter;
import com.vonderland.diarydemo.BaseView;
import com.vonderland.diarydemo.detailpage.DetailPageContract;

/**
 * Created by Vonderland on 2017/2/4.
 */

public interface EditPageContract {
    interface View extends BaseView<DetailPageContract.Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
