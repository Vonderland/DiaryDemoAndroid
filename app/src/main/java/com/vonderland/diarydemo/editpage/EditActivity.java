package com.vonderland.diarydemo.editpage;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.vonderland.diarydemo.BasePresenter;
import com.vonderland.diarydemo.BaseView;
import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.bean.Moment;
import com.vonderland.diarydemo.constant.Constant;

public class EditActivity extends AppCompatActivity {

    BaseView view;
    BasePresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);

        String from = getIntent().getStringExtra(Constant.KEY_EDIT_FROM);
        if (TextUtils.equals(from, Constant.DIARY_FROM_CREATE)) {
            view = new EditDiaryPageFragment();
            presenter = new EditDiaryPagePresenter(this, (EditDiaryPageFragment)view);

        } else if (TextUtils.equals(from, Constant.DIARY_FROM_EDIT)) {
            view = new EditDiaryPageFragment();
            presenter = new EditDiaryPagePresenter(this, (EditDiaryPageFragment)view);
            Diary data = (Diary)getIntent().getSerializableExtra("data");
            ((EditDiaryPagePresenter)presenter).setData(data);
        } else if (TextUtils.equals(from, Constant.MOMENT_FROM_EDIT)) {
            view = new EditMomentPageFragment();
            presenter = new EditMomentPagePresenter(this, (EditMomentPageFragment)view);
            Moment data = (Moment)getIntent().getSerializableExtra("data");
            ((EditMomentPagePresenter)presenter).setData(data);
        } else {
            view = new EditMomentPageFragment();
            presenter = new EditMomentPagePresenter(this, (EditMomentPageFragment)view);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, (Fragment) view)
                .commit();
    }

}
