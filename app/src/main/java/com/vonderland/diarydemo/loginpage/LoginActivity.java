package com.vonderland.diarydemo.loginpage;

import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.event.RegisterFinishEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends AppCompatActivity {

    private long lastBackPressed;
    private LoginPageContract.View view;
    private LoginPageContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.frame);

        view = new LoginFragment();
        presenter = new LoginPresenter(view);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, (Fragment) view)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (java.util.Calendar.getInstance().getTimeInMillis() - lastBackPressed > 1000) {
            lastBackPressed = java.util.Calendar.getInstance().getTimeInMillis();
            Toast.makeText(this, R.string.click_again_to_exit, Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RegisterFinishEvent event) {
        finish();
    }
}

