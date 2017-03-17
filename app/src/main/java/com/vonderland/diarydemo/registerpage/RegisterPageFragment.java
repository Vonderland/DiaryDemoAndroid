package com.vonderland.diarydemo.registerpage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.application.DiaryDemoApplication;
import com.vonderland.diarydemo.utils.L;
import com.vonderland.diarydemo.utils.PictureUtil;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Vonderland on 2017/3/12.
 */

public class RegisterPageFragment extends Fragment implements RegisterPageContract.View {

    private RegisterPageContract.Presenter presenter;
    private ImageView avatar;
    private EditText email;
    private EditText password;
    private EditText passwordConfirm;
    private EditText nick;
    private RadioButton male;
    private RadioButton female;
    private TextView registerBtn;
    private MaterialDialog progressDialog;

    private final int REQUEST_PICK_FROM_GALLERY = 1;
    private final int REQUEST_CROP = 2;

    private String path;
    private File fileForCroppedPic;
    private Uri uriForCroppedPic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        avatar = (ImageView) view.findViewById(R.id.reg_avatar_iv);
        email = (EditText) view.findViewById(R.id.reg_email);
        password = (EditText) view.findViewById(R.id.reg_passwd);
        passwordConfirm = (EditText) view.findViewById(R.id.reg_passwd_confirm);
        nick = (EditText) view.findViewById(R.id.reg_nick);
        male = (RadioButton) view.findViewById(R.id.male);
        female = (RadioButton) view.findViewById(R.id.female);
        registerBtn = (TextView) view.findViewById(R.id.btn_register);
        progressDialog = new MaterialDialog.Builder(getActivity())
                .content("注册中…")
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .build();

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_PICK_FROM_GALLERY);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                String passwd = password.getText().toString();
                String confirm = passwordConfirm.getText().toString();
                String nickName = nick.getText().toString();
                if (TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passwd)
                || TextUtils.isEmpty(confirm) || TextUtils.isEmpty(nickName)) {
                    Toast.makeText(getActivity(), "注册信息还没填完整哦", Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                    Toast.makeText(getActivity(), "邮箱格式填写不正确", Toast.LENGTH_SHORT).show();
                } else if (passwd.length() < 8 || passwd.length() > 16) {
                    Toast.makeText(getActivity(), "密码长度不符合要求", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.equals(passwd, confirm)) {
                    Toast.makeText(getActivity(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(path)) {
                    Toast.makeText(getActivity(), "还没上传头像哦", Toast.LENGTH_SHORT).show();
                } else {
                    presenter.register(getActivity(), emailStr, passwd, nickName, female.isChecked(), path);
                }
            }
        });

        fileForCroppedPic = new File(DiaryDemoApplication.getGlobalContext().getExternalCacheDir(), "avatar_cropped.png");
        uriForCroppedPic = Uri.fromFile(fileForCroppedPic);
        presenter.start();
        return view;
    }

    @Override
    public void showError(int code) {
        int msgId;
        switch (code) {
            case 106:
                msgId = R.string.email_used;
                break;
            default:
                msgId = R.string.network_error;
                break;
        }
        Toast.makeText(getActivity(), msgId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(RegisterPageContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_FROM_GALLERY:
                    doCrop(data.getData());
                    break;
                case REQUEST_CROP:
                    updateAvatar(uriForCroppedPic);
                    break;
                default:
                    break;
            }
        }
    }

    private void doCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", "false");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForCroppedPic);
        try {
            startActivityForResult(intent, REQUEST_CROP);
        } catch (Exception e) {
            L.d("RegisterPageDoCrop", e.getMessage());
        }
    }

    private void updateAvatar(Uri uri) {
        L.d("registerPageCrop", "uri = " + uri);
        path = PictureUtil.getRealPathFromURI(getActivity(), uri);
        L.d("registerPageCrop", "path = " + path);
        avatar.setImageBitmap(PictureUtil
                .getSmallBitmap(path, 150, 150));
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
}
