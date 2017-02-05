package com.vonderland.diarydemo.detailpage;

import android.content.Context;
import android.content.Intent;

import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.editpage.EditActivity;

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
    public void startEdit() {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(Constant.KEY_EDIT_FROM, Constant.DIARY_FROM_EDIT);
        intent.putExtra("data", diary);
        context.startActivity(intent);
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
