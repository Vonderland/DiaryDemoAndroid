package com.vonderland.diarydemo.detailpage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.constant.Constant;

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

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_DIARY_CHANGE);
        registerReceiver(receiver, intentFilter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), Constant.ACTION_DIARY_CHANGE)) {
                Object data = intent.getSerializableExtra(Constant.DIARY_FROM_BROADCAST);
                if (data != null && data instanceof Diary) {
                    presenter.onDataChange(data);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
