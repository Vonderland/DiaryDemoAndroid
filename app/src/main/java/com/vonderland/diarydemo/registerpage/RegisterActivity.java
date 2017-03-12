package com.vonderland.diarydemo.registerpage;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vonderland.diarydemo.R;

public class RegisterActivity extends AppCompatActivity {

    RegisterPageContract.Presenter presenter;
    RegisterPageContract.View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);
        view = new RegisterPageFragment();
        presenter = new RegisterPagePresenter(view);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, (Fragment) view)
                .commit();
    }
}
