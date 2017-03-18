package com.vonderland.diarydemo.profilepage;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.event.BlackHouseEvent;
import com.vonderland.diarydemo.event.BreakUpEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ProfileActivity extends AppCompatActivity {

    ProfilePageContract.View view;
    ProfilePageContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isLover = getIntent().getBooleanExtra(Constant.KEY_IS_LOVER, false);
        setContentView(R.layout.frame);

        view = new ProfileFragment();
        presenter = new ProfilePresenter(view, isLover);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, (Fragment) view)
                .commit();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BreakUpEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BlackHouseEvent event) {
        finish();
    }
}
