package com.vonderland.diarydemo.editpage;

import android.content.Context;
import android.content.Intent;

import com.vonderland.diarydemo.bean.ListResponse;
import com.vonderland.diarydemo.bean.Moment;
import com.vonderland.diarydemo.bean.MomentModel;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.network.BaseResponseHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vonderland on 2017/2/21.
 */

public class EditMomentPagePresenter implements EditMomentPageContract.Presenter {

    private Context context;
    private EditMomentPageContract.View view;
    private MomentModel model;
    private Moment data;

    public EditMomentPagePresenter(Context context, EditMomentPageContract.View view) {

        this.context = context;
        this.view = view;
        this.view.setPresenter(this);

        model = new MomentModel();
    }
    @Override
    public void postData(String title, long date, String location) {
        Map<String, String> options = new HashMap<>();
        options.put(Constant.KEY_TITLE, title);
        options.put(Constant.KEY_EVENT_TIME, date + "");
        options.put(Constant.KEY_LOCATION, location);

        if (data == null) {
            model.addMoment(options, new BaseResponseHandler<ListResponse<Moment>>() {

                @Override
                public void onSuccess(ListResponse<Moment> body) {
                    List<Moment> moment = body.getData();
                    if (moment.size() > 0) {
                        model.insertMomentToRealm(moment.get(0));
                        Intent intent = new Intent();
                        intent.setAction(Constant.ACTION_MOMENT_CHANGE);
                        intent.putExtra(Constant.MOMENT_FROM_BROADCAST, moment.get(0));
                        context.sendBroadcast(intent);
                    }
                    view.showSuccessfully(false);
                    ((EditActivity)context).finish();
                }

                @Override
                public void onError(int statusCode) {
                    view.showError(statusCode);
                }
            });
        } else {
            options.put(Constant.KEY_ID, data.getId() + "");

            model.updateMoment(options, new BaseResponseHandler<ListResponse<Moment>>() {

                @Override
                public void onSuccess(ListResponse<Moment> body) {
                    List<Moment> moment = body.getData();
                    if (moment.size() > 0) {
                        model.updateMomentInRealm(moment.get(0));
                        Intent intent = new Intent();
                        intent.setAction(Constant.ACTION_MOMENT_CHANGE);
                        intent.putExtra(Constant.MOMENT_FROM_BROADCAST, moment.get(0));
                        context.sendBroadcast(intent);
                    }
                    view.showSuccessfully(true);
                    ((EditActivity)context).finish();
                }

                @Override
                public void onError(int statusCode) {
                    view.showError(statusCode);
                }
            });
        }
    }

    @Override
    public void setData(Moment data) {
        this.data = data;
    }

    @Override
    public void start() {
        if (data != null) {
            view.showData(data);
        }
    }
}
