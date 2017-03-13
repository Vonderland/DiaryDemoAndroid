package com.vonderland.diarydemo.profilepage;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.constant.Constant;

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
    }
}
