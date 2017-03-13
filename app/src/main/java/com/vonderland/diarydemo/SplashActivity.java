package com.vonderland.diarydemo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.homepage.MainActivity;
import com.vonderland.diarydemo.loginpage.LoginActivity;
import com.vonderland.diarydemo.utils.CipherUtil;
import com.vonderland.diarydemo.utils.L;
import com.vonderland.diarydemo.utils.SharedPrefUtil;

/**
 * 闪屏页逻辑比较简单，因此没有使用 MVP 来写
 */
public class SplashActivity extends AppCompatActivity {
    private static Handler mHandler;
    public static final int GOTO_MAIN = 0;
    private static final int GOTO_LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GOTO_MAIN:
                        mHandler.removeMessages(GOTO_MAIN);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                        break;
                    case GOTO_LOGIN:
                        mHandler.removeMessages(GOTO_LOGIN);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };

        String token = (String)SharedPrefUtil.getInstance().get(Constant.SP_KEY_TOKEN, "");
        boolean remember = (boolean)SharedPrefUtil.getInstance().get(Constant.SP_KEY_REMEMBER_STATUS, false);
        //if (TextUtils.isEmpty(token) || !remember) {
        if (TextUtils.isEmpty(token) || !remember) {
            mHandler.sendEmptyMessageDelayed(GOTO_LOGIN, 0);
        } else {
            mHandler.sendEmptyMessageDelayed(GOTO_MAIN, 0);
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(GOTO_MAIN);
        mHandler.removeMessages(GOTO_LOGIN);
        mHandler = null;
        super.onDestroy();
    }
}
