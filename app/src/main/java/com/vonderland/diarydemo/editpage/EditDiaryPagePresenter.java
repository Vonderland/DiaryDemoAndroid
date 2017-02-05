package com.vonderland.diarydemo.editpage;

import android.content.Context;
import android.text.TextUtils;

import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.bean.DiaryCallModel;
import com.vonderland.diarydemo.bean.ListResponse;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.network.BaseResponseHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vonderland on 2017/2/4.
 */

public class EditDiaryPagePresenter implements EditDiaryPageContract.Presenter {

    private Context context;
    private EditDiaryPageContract.View view;
    private DiaryCallModel model;
    private Diary data;

    public EditDiaryPagePresenter(Context context, EditDiaryPageContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);

        model = new DiaryCallModel();
    }

    @Override
    public void postData(String title, long date, String description, String filePath, int change) {
        Map<String, String> options = new HashMap<>();
        options.put(Constant.KEY_TITLE, title);
        options.put(Constant.KEY_EVENT_TIME, date + "");
        options.put(Constant.KEY_DESCRIPTION, description);
        if (data == null) {
            if(TextUtils.isEmpty(filePath)) {
                model.addDiary(options, null, new BaseResponseHandler<ListResponse<Diary>> () {

                    @Override
                    public void onSuccess(ListResponse<Diary> body) {
                        view.showSuccessfully(false);
                        ((EditActivity)context).finish();
                        //TODO:主页对应数据修改
                    }

                    @Override
                    public void onError(int statusCode) {
                        view.showError(statusCode);
                    }
                });
            } else {
                //TODO:带图片的提交
                //TODO:主页对应数据修改
            }
        } else {
            options.put(Constant.KEY_ID, data.getId() + "");
            options.put(Constant.KEY_PICTURE_CHANGED, change + "");
            if(TextUtils.isEmpty(filePath)) {
                model.updateDiary(options, null, new BaseResponseHandler<ListResponse<Diary>> () {

                    @Override
                    public void onSuccess(ListResponse<Diary> body) {
                        view.showSuccessfully(true);
                        ((EditActivity)context).finish();
                        //TODO:主页对应数据修改和编辑页面数据修改
                    }

                    @Override
                    public void onError(int statusCode) {
                        view.showError(statusCode);
                    }
                });
            } else {
                //TODO:带图片的提交
            }
        }

    }

    @Override
    public void setData(Diary data) {
        this.data = data;
    }


    @Override
    public void start() {
        if (data != null) {
            view.showData(data);
        }
    }
}
