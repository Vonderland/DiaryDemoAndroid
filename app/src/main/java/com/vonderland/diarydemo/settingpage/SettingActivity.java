package com.vonderland.diarydemo.settingpage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.UserModel;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.event.LogoutEvent;
import com.vonderland.diarydemo.loginpage.LoginActivity;
import com.vonderland.diarydemo.ui.ProfileEditDialogFragment;
import com.vonderland.diarydemo.utils.SharedPrefUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 该页逻辑比较简单，因此没有使用 MVP 来写
 */
public class SettingActivity extends AppCompatActivity {

    private LinearLayout resetLL;
    private LinearLayout clearLL;
    private LinearLayout hostLL;

    private TextView currentEmail;
    private TextView cacheInfo;
    private TextView hostAddress;
    private TextView logoutBtn;

    private SharedPrefUtil sharedPrefUtil;

    private UserModel userModel;

    private String host;
    private static final String TAG = "SettingActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resetLL = (LinearLayout) findViewById(R.id.reset_ll);
        clearLL = (LinearLayout) findViewById(R.id.clear_cache_ll);
        hostLL = (LinearLayout) findViewById(R.id.host_ll);

        currentEmail = (TextView) findViewById(R.id.current_email);
        cacheInfo = (TextView) findViewById(R.id.cache_info);
        hostAddress = (TextView) findViewById(R.id.host);

        logoutBtn = (TextView) findViewById(R.id.btn_logout);

        sharedPrefUtil = SharedPrefUtil.getInstance();
        userModel = new UserModel();
        String address = userModel.getUserProfileFromRealm().getEmail();
        currentEmail.setText(String.format(getResources().getString(R.string.current_email), address));

        long size = Glide.getPhotoCacheDir(this).length();
        long cacheSizeMb = size / (1024 * 1024);
        cacheInfo.setText(String.format(getResources().getString(R.string.current_cache_size), cacheSizeMb));

        host = (String)SharedPrefUtil.getInstance().get(Constant.SP_KEY_HOST, "");
        if (TextUtils.isEmpty(host)) {
            host = Constant.HOST;
        }
        hostAddress.setText(host);

        resetLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "重置密码", Toast.LENGTH_SHORT).show();
            }
        });

        clearLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearGlideCache();
            }
        });

        hostLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHostAddress();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void setHostAddress() {
        ProfileEditDialogFragment fragment
                = ProfileEditDialogFragment.newInstance(R.string.host, host);
        fragment.setConfirmClickedListener(new ProfileEditDialogFragment.OnConfirmClickedListener() {
            @Override
            public void onConfirmClicked(String string) {
                hostAddress.setText(string);
                sharedPrefUtil.put(Constant.SP_KEY_HOST, string);
            }
        });
        fragment.show(getSupportFragmentManager(), TAG);
    }

    private void clearGlideCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(SettingActivity.this).clearDiskCache();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cacheInfo.setText(String.format(getResources().getString(R.string.current_cache_size), 0));
                        Toast.makeText(SettingActivity.this, "图片缓存清理完毕", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void logout() {
        Dialog dialog = new AlertDialog.Builder(SettingActivity.this)
                .setMessage("确定退出该账号吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPrefUtil.logOutClear();
                        EventBus.getDefault().post(new LogoutEvent());
                        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                        SettingActivity.this.finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        dialog.show();
    }
}
