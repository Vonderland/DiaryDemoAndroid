package com.vonderland.diarydemo.detailpage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.Diary;

public class DetailActivity extends AppCompatActivity {

    DetailPageContract.Presenter presenter;
    DetailPageContract.View fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);
        Object data = getIntent().getSerializableExtra("data");
        if (data instanceof Diary) {
            fragment = new DiaryDetailFragment();
            presenter = new DiaryDetailPresenter(this, fragment);
            presenter.setData(data);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, (DiaryDetailFragment)fragment)
                    .commit();
        }
    }

}
