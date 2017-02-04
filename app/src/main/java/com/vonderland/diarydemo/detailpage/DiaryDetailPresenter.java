package com.vonderland.diarydemo.detailpage;

import android.content.Context;

import com.vonderland.diarydemo.bean.Diary;

/**
 * Created by Vonderland on 2017/2/4.
 */

public class DiaryDetailPresenter implements DetailPageContract.Presenter{

    private Context context;
    private DetailPageContract.View view;
    private Diary diary;

    public DiaryDetailPresenter(Context context, DetailPageContract.View fragment) {
        this.context = context;
        this.view = fragment;
        this.view.setPresenter(this);
    }

    @Override
    public void startEdit(Object data) {

    }

    @Override
    public void setData(Object data) {
        diary = (Diary)data;
    }

    @Override
    public void start() {
        view.showData(diary);
    }
}
