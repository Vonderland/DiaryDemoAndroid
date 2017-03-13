package com.vonderland.diarydemo.settingpage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.AuthModel;
import com.vonderland.diarydemo.bean.AuthResponse;
import com.vonderland.diarydemo.bean.Authorization;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.network.BaseResponseHandler;
import com.vonderland.diarydemo.utils.CipherUtil;
import com.vonderland.diarydemo.utils.SharedPrefUtil;

/**
 * 该页逻辑比较简单，因此没有使用 MVP 来写
 */
public class ResetPasswordActivity extends AppCompatActivity {

    private EditText old;
    private EditText password;
    private EditText confirm;
    private TextView resetBtn;

    private AuthModel authModel;
    private SharedPrefUtil sharedPrefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("修改密码");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        authModel = new AuthModel();

        old = (EditText) findViewById(R.id.reset_old);
        password = (EditText) findViewById(R.id.retset_passwd);
        confirm = (EditText) findViewById(R.id.reset_passwd_confirm);
        resetBtn = (TextView) findViewById(R.id.btn_reset);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doResetPassword();
            }
        });

        sharedPrefUtil = SharedPrefUtil.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    public void doResetPassword() {
        String oldPassWord = old.getText().toString();
        String newPassWord = password.getText().toString();
        String newConfirm = confirm.getText().toString();

        if (TextUtils.isEmpty(oldPassWord) || TextUtils.isEmpty(newPassWord) || TextUtils.isEmpty(newConfirm)) {
            Toast.makeText(this, "信息还没填写完整", Toast.LENGTH_SHORT).show();
        } else if (newPassWord.length() < 8 || newPassWord.length() > 16) {
            Toast.makeText(this, "密码长度不符合要求", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.equals(newPassWord, newConfirm)) {
            Toast.makeText(this, "两次新密码输入不一致", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.equals(newPassWord, oldPassWord)) {
            Toast.makeText(this, "新旧密码不能相同", Toast.LENGTH_SHORT).show();
        } else {
            authModel.resetPassword(CipherUtil.encodeData(oldPassWord), CipherUtil.encodeData(newPassWord),
                    new BaseResponseHandler<AuthResponse>() {
                @Override
                public void onSuccess(AuthResponse body) {
                    if (body != null) {
                        Authorization authorization = body.getData();
                        if (authorization != null) {
                            sharedPrefUtil.put(Constant.SP_KEY_TOKEN, authorization.getToken());
                            sharedPrefUtil.put(Constant.SP_KEY_UID, authorization.getUid());
                            Toast.makeText(ResetPasswordActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                            ResetPasswordActivity.this.finish();
                        }
                    }
                }

                @Override
                public void onError(int statusCode) {
                    int msgId;
                    switch (statusCode) {
                        case 105:
                        case 108:
                            msgId = R.string.invalid_token;
                            break;
                        case 110:
                            msgId = R.string.wrong_password;
                            break;
                        default:
                            msgId = R.string.network_error;
                            break;
                    }
                    Toast.makeText(ResetPasswordActivity.this, msgId, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
