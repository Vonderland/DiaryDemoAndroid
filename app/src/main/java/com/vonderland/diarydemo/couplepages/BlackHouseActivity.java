package com.vonderland.diarydemo.couplepages;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.vonderland.diarydemo.R;

public class BlackHouseActivity extends AppCompatActivity {

    private long lastBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_house);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
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


}
