package com.vonderland.diarydemo.couplepages;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.BooleanResponse;
import com.vonderland.diarydemo.bean.CoupleModel;
import com.vonderland.diarydemo.bean.Request;
import com.vonderland.diarydemo.bean.RequestResponse;
import com.vonderland.diarydemo.bean.User;
import com.vonderland.diarydemo.bean.UserModel;
import com.vonderland.diarydemo.homepage.MainActivity;
import com.vonderland.diarydemo.network.BaseResponseHandler;

import java.lang.ref.WeakReference;

public class SingleActivity extends AppCompatActivity {

    private long lastBackPressed;
    private boolean isRequestShowing = false;
    private EditText loverEmail;
    private TextView sendBtn;
    private CoupleModel coupleModel;
    private UserModel userModel;
    private String myEmail;

    private static final int CHECK_LOVER = 0;
    private static final int CHECK_REQUEST = 1;
    private final Handler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_single);

        coupleModel = new CoupleModel();
        userModel = new UserModel();
        myEmail = "";
        User user = userModel.getUserProfileFromRealm();
        if (user != null) {
            myEmail = user.getEmail();
        }

        loverEmail = (EditText) findViewById(R.id.lover_email);
        sendBtn = (TextView) findViewById(R.id.btn_send);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loverEmail.getText().toString();
                if (TextUtils.equals(email, myEmail)) {
                    showError(118);
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SingleActivity.this, "请填写邮箱", Toast.LENGTH_SHORT).show();
                }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(SingleActivity.this, "邮箱格式填写不正确", Toast.LENGTH_SHORT).show();
                } else {
                    coupleModel.sendRequest(email, new BaseResponseHandler() {
                        @Override
                        public void onSuccess(Object body) {
                            Toast.makeText(SingleActivity.this, "请求发送成功！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(int statusCode) {
                            showError(statusCode);
                        }
                    });
                }
            }
        });

        mHandler.sendEmptyMessage(CHECK_LOVER);
        mHandler.sendEmptyMessage(CHECK_REQUEST);
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

    private void showError(int code) {
        int msgId;
        switch (code) {
            case 105:
            case 108:
                msgId = R.string.invalid_token;
                break;
            case 112:
                msgId = R.string.lover_not_reg;
                break;
            case 113:
                msgId = R.string.lover_has_lover;
                break;
            case 114:
                msgId = R.string.wait_lover;
                break;
            case 115:
                msgId = R.string.has_lover;
                break;
            case 118:
                msgId = R.string.send_to_self;
                break;
            default:
                msgId = R.string.network_error;
                break;
        }
        Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show();
    }

    private synchronized void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void checkLover() {
        coupleModel.hasLover(new BaseResponseHandler<BooleanResponse>() {
            @Override
            public void onSuccess(BooleanResponse body) {
                if (body.getData()) {
                    startMain();
                }
            }

            @Override
            public void onError(int statusCode) {

            }
        });
    }

    private void showRequest(Request request) {
        if (!isRequestShowing) {
            String fromEmail = request.getFromEmail();
            String fromNickName = request.getFromNickName();
            String message = fromNickName + "(" + fromEmail + ")" + "邀请您成为ta的另一半";
            final long requestId = request.getId();
            Dialog dialog = new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setPositiveButton("接受", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            coupleModel.acceptRequest(requestId, new BaseResponseHandler() {
                                @Override
                                public void onSuccess(Object body) {
                                    startMain();
                                }

                                @Override
                                public void onError(int statusCode) {
                                    showError(statusCode);
                                }
                            });
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isRequestShowing = false;
                            coupleModel.rejectRequest(requestId, new BaseResponseHandler() {
                                @Override
                                public void onSuccess(Object body) {

                                }

                                @Override
                                public void onError(int statusCode) {
                                    showError(statusCode);
                                }
                            });
                        }
                    })
                    .create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            isRequestShowing = true;
        }
    }

    private void checkRequest() {
        coupleModel.checkRequest(new BaseResponseHandler<RequestResponse>() {
            @Override
            public void onSuccess(RequestResponse body) {
                if (body.getSize() == 1) {
                    Request request = body.getData();
                    showRequest(request);
                }
            }

            @Override
            public void onError(int statusCode) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private static class MyHandler extends Handler {
        private final WeakReference<SingleActivity> mActivity;
        public MyHandler(SingleActivity activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            } else {
                switch (msg.what) {
                    case CHECK_LOVER:
                        mActivity.get().checkLover();
                        this.sendEmptyMessageDelayed(CHECK_LOVER, 7000);
                        break;
                    case CHECK_REQUEST:
                        mActivity.get().checkRequest();
                        this.sendEmptyMessageDelayed(CHECK_REQUEST, 5000);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
