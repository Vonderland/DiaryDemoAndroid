package com.vonderland.diarydemo.homepage;

import android.app.Fragment;

/**
 * Created by Vonderland on 2017/2/2.
 */

public class DiaryFragment extends Fragment implements HomePageContract.View {

    private HomePageContract.Presenter presenter;
    @Override
    public void setPresenter(HomePageContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showData() {

    }

    @Override
    public void StopLoading() {

    }

    @Override
    public void showLoading() {

    }
}
