package com.vonderland.diarydemo.loginpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.ui.ProfileEditDialogFragment;

/**
 * Created by Vonderland on 2017/3/12.
 */

public class LoginFragment extends Fragment implements LoginPageContract.View{

    private LoginPageContract.Presenter presenter;
    private EditText email;
    private EditText password;
    private AppCompatCheckBox checkBox;
    private TextView loginBtn;
    private TextView forgetBtn;
    private TextView registerBtn;
    private ImageView logo;
    private MaterialDialog progressDialog;

    private int logoClick = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        email = (EditText)view.findViewById(R.id.login_email);
        password = (EditText)view.findViewById(R.id.login_passwd);
        checkBox = (AppCompatCheckBox)view.findViewById(R.id.checkbox);
        loginBtn = (TextView)view.findViewById(R.id.btn_login);
        forgetBtn = (TextView)view.findViewById(R.id.btn_forget);
        registerBtn = (TextView)view.findViewById(R.id.btn_start_register);
        logo = (ImageView) view.findViewById(R.id.logo);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoClick++;
                if (logoClick > 10) {
                    ProfileEditDialogFragment fragment
                            = ProfileEditDialogFragment.newInstance(R.string.host, presenter.getHostAddress());
                    fragment.setConfirmClickedListener(new ProfileEditDialogFragment.OnConfirmClickedListener() {
                        @Override
                        public void onConfirmClicked(String string) {
                            presenter.setHostAddress(string);
                        }
                    });
                    fragment.show(getActivity().getSupportFragmentManager(), "loginFragment");
                }
            }
        });

        progressDialog = new MaterialDialog.Builder(getActivity())
                .content("登录中…")
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .build();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.setStatus(isChecked);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                String passwd = password.getText().toString();
                if (TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passwd)) {
                    Toast.makeText(getActivity(), "登录信息还没填写完整哦", Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()){
                    Toast.makeText(getActivity(), "邮箱格式填写不正确", Toast.LENGTH_SHORT).show();
                } else {
                    presenter.login(getActivity(), email.getText().toString(), password.getText().toString());
                }
            }
        });
        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                if (TextUtils.isEmpty(emailStr)) {
                    Toast.makeText(getActivity(), "请填写邮箱", Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()){
                    Toast.makeText(getActivity(), "邮箱格式填写不正确", Toast.LENGTH_SHORT).show();
                } else {
                    presenter.forgetPassword(emailStr);
                }
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startRegister(getActivity());
            }
        });

        presenter.start();
        return view;
    }

    @Override
    public void showStatus(boolean remember) {
        checkBox.setChecked(remember);
    }

    @Override
    public void showProgressBar() {
        progressDialog.show();
    }

    @Override
    public void dismissProgressBar() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void setPresenter(LoginPageContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showError(int code) {
        int msgId;
        switch (code) {
            case 107:
                msgId = R.string.not_register;
                break;
            case 110:
                msgId = R.string.wrong_password;
                break;
            case 111:
                msgId = R.string.forget_fail;
                break;
            default:
                msgId = R.string.network_error;
                break;
        }
        Toast.makeText(getActivity(), msgId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmailSuccess() {
        Toast.makeText(getActivity(), "新密码邮件已发送到您的邮箱", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showWaitMessage() {
        Toast.makeText(getActivity(), "已向服务器提交请求，请稍候", Toast.LENGTH_LONG).show();
    }
}
