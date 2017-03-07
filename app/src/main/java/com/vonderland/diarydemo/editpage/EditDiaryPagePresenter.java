package com.vonderland.diarydemo.editpage;

import android.content.Context;
import android.text.TextUtils;

import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.bean.DiaryModel;
import com.vonderland.diarydemo.bean.ListResponse;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.utils.PictureUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Vonderland on 2017/2/4.
 */

public class EditDiaryPagePresenter implements EditDiaryPageContract.Presenter {

    private Context context;
    private EditDiaryPageContract.View view;
    private DiaryModel model;
    private Diary data;

    public EditDiaryPagePresenter(Context context, EditDiaryPageContract.View view) {

        this.context = context;
        this.view = view;
        this.view.setPresenter(this);

        model = new DiaryModel();
    }

    @Override
    public void postData(String title, long date, String description, String filePath, int change) {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart(Constant.KEY_TITLE, title)
                .addFormDataPart(Constant.KEY_EVENT_TIME, date + "")
                .addFormDataPart(Constant.KEY_DESCRIPTION, description);
        if (data == null) {
            if (!TextUtils.isEmpty(filePath)) {
                File picture = new File(filePath);
                builder.addFormDataPart(Constant.KEY_PICTURE, picture.getName(),
                        RequestBody.create(MediaType.parse(PictureUtil.getMimeType(picture)), picture));
            }
            RequestBody requestBody = builder.build();
            model.addDiary(requestBody, new BaseResponseHandler<ListResponse<Diary>> () {

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
            builder.addFormDataPart(Constant.KEY_ID, data.getId() + "")
                    .addFormDataPart(Constant.KEY_PICTURE_CHANGED, change + "");
            if (!TextUtils.isEmpty(filePath)) {
                File picture = new File(filePath);
                builder.addFormDataPart(Constant.KEY_PICTURE, picture.getName(),
                        RequestBody.create(MediaType.parse(PictureUtil.getMimeType(picture)), picture));
            }
            RequestBody requestBody = builder.build();
            model.updateDiary(requestBody, new BaseResponseHandler<ListResponse<Diary>> () {

                @Override
                public void onSuccess(ListResponse<Diary> body) {
                    view.showSuccessfully(true);
                    ((EditActivity)context).finish();
                    //TODO:日记阅读页面对应数据修改
                }

                @Override
                public void onError(int statusCode) {
                    view.showError(statusCode);
                }
            });
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