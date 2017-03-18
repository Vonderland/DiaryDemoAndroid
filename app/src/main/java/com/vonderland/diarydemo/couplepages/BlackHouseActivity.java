package com.vonderland.diarydemo.couplepages;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.BooleanResponse;
import com.vonderland.diarydemo.bean.CoupleModel;
import com.vonderland.diarydemo.bean.User;
import com.vonderland.diarydemo.bean.UserModel;
import com.vonderland.diarydemo.bean.UserResponse;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.event.BreakUpEvent;
import com.vonderland.diarydemo.homepage.MainActivity;
import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.utils.SharedPrefUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

public class BlackHouseActivity extends AppCompatActivity {

    private UserModel userModel;
    private CoupleModel coupleModel;
    private SharedPrefUtil sharedPrefUtil;
    private long lastBackPressed;
    private static final int CHECK_BLACK = 0;
    private final Handler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_house);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        sharedPrefUtil = SharedPrefUtil.getInstance();
        userModel = new UserModel();
        coupleModel = new CoupleModel();
        refreshUserProfile();
        mHandler.sendEmptyMessage(CHECK_BLACK);
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

    private void checkBlack() {
        coupleModel.isBlack(new BaseResponseHandler<BooleanResponse>() {
            @Override
            public void onSuccess(BooleanResponse body) {
                if (!body.getData()) {
                    sharedPrefUtil.put(Constant.SP_KEY_IS_BLACK, false);
                    startMain();
                }
            }

            @Override
            public void onError(int statusCode) {

            }
        });
    }

    private void refreshUserProfile() {

        userModel.getUserProfile(new BaseResponseHandler<UserResponse>() {

            @Override
            public void onSuccess(UserResponse body) {
                if (body != null) {
                    User refreshUser = body.getData();
                    if (refreshUser != null) {
                        userModel.updateProfileInRealm(refreshUser);
                        sharedPrefUtil.put(Constant.SP_KEY_IS_BLACK, refreshUser.isBlack());
                        sharedPrefUtil.put(Constant.SP_KEY_LOVER_ID, refreshUser.getLoverId());
                        if (refreshUser.getLoverId() == -1) {
                            sharedPrefUtil.remove(Constant.SP_KEY_IS_BLACK);
                            startSingle();
                            EventBus.getDefault().post(new BreakUpEvent());
                        }
                        if (!refreshUser.isBlack()) {
                            startMain();
                        }
                    }
                }
            }

            @Override
            public void onError(int statusCode) {
                Toast.makeText(BlackHouseActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();;
    }

    private void startSingle() {
        startActivity(new Intent(this, SingleActivity.class));
        finish();
    }

    private static class MyHandler extends Handler {
        private final WeakReference<BlackHouseActivity> mActivity;
        public MyHandler(BlackHouseActivity activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            } else {
                switch (msg.what) {
                    case CHECK_BLACK:
                        mActivity.get().checkBlack();
                        this.sendEmptyMessageDelayed(CHECK_BLACK, 3000);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
