package com.vonderland.diarydemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.couplepages.BlackHouseActivity;
import com.vonderland.diarydemo.couplepages.SingleActivity;
import com.vonderland.diarydemo.homepage.MainActivity;
import com.vonderland.diarydemo.loginpage.LoginActivity;
import com.vonderland.diarydemo.utils.CipherUtil;
import com.vonderland.diarydemo.utils.L;
import com.vonderland.diarydemo.utils.SharedPrefUtil;

import java.lang.ref.WeakReference;

/**
 * 闪屏页逻辑比较简单，因此没有使用 MVP 来写
 */
public class SplashActivity extends AppCompatActivity {
    public static final int GOTO_MAIN = 0;
    private static final int GOTO_LOGIN = 1;
    private static final int GOTO_SINGLE = 2;
    private static final int GOTO_BLACK_HOUSE = 3;
    private final MyHandler mHandler = new MyHandler(this);
    private SharedPrefUtil sharedPrefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPrefUtil = SharedPrefUtil.getInstance();
        String token = (String)sharedPrefUtil.get(Constant.SP_KEY_TOKEN, "");
        boolean remember = (boolean)sharedPrefUtil.get(Constant.SP_KEY_REMEMBER_STATUS, false);
        long loverId = (long)sharedPrefUtil.get(Constant.SP_KEY_LOVER_ID, -1L);
        boolean isBlack = (boolean)sharedPrefUtil.get(Constant.SP_KEY_IS_BLACK, false);

        if (TextUtils.isEmpty(token) || !remember) {
            mHandler.sendEmptyMessageDelayed(GOTO_LOGIN, 0);
        } else if (loverId == -1) {
            mHandler.sendEmptyMessageDelayed(GOTO_SINGLE, 0);
        } else if (isBlack) {
            mHandler.sendEmptyMessageDelayed(GOTO_BLACK_HOUSE, 0);
        } else {
            mHandler.sendEmptyMessageDelayed(GOTO_MAIN, 0);
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        public MyHandler(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            } else {
                Intent intent;
                switch (msg.what) {
                    case GOTO_MAIN:
                        intent = new Intent(mActivity.get(), MainActivity.class);
                        break;
                    case GOTO_LOGIN:
                        intent = new Intent(mActivity.get(), LoginActivity.class);
                        break;
                    case GOTO_SINGLE:
                        intent = new Intent(mActivity.get(), SingleActivity.class);
                        break;
                    case GOTO_BLACK_HOUSE:
                        intent = new Intent(mActivity.get(), BlackHouseActivity.class);
                        break;
                    default:
                        intent = new Intent(mActivity.get(), LoginActivity.class);
                        break;
                }
                mActivity.get().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                mActivity.get().startActivity(intent);
                mActivity.get().finish();
            }
        }
    }
}
