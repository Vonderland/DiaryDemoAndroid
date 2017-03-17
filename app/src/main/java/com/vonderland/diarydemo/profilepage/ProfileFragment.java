package com.vonderland.diarydemo.profilepage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.User;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.ui.ProfileEditDialogFragment;
import com.vonderland.diarydemo.utils.L;
import com.vonderland.diarydemo.utils.PictureUtil;
import com.vonderland.diarydemo.utils.SharedPrefUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Vonderland on 2017/3/13.
 */

public class ProfileFragment extends Fragment implements ProfilePageContract.View {

    private final String TAG = "profile_fragment";
    private Toolbar toolbar;
    private ImageView avatar;
    private TextView email;
    private TextView nickName;
    private TextView gender;

    private TextView breakupBtn;
    private LinearLayout nickNameLL;
    private LinearLayout myLoverLL;
    private LinearLayout blackHouseLL;
    private SwitchCompat blackSwitch;

    private ProfilePageContract.Presenter presenter;

    private final int REQUEST_PICK_FROM_GALLERY = 1;
    private final int REQUEST_CROP = 2;

    public enum SuccessType {
        UPDATE_AVATAR, UPDATE_NICK_NAME, PUT_IN_BLACK, RELEASE_FROM_BLACK
    }

    private String path;
    private String host;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);

        host = (String)SharedPrefUtil.getInstance().get(Constant.SP_KEY_HOST, Constant.HOST);
        ProfileActivity activity = (ProfileActivity) getActivity();
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        avatar = (ImageView) view.findViewById(R.id.profile_avatar_iv);
        email = (TextView) view.findViewById(R.id.profile_email);
        nickName = (TextView) view.findViewById(R.id.profile_nick);
        gender = (TextView) view.findViewById(R.id.profile_gender);
        breakupBtn = (TextView) view.findViewById(R.id.btn_break_up);
        nickNameLL = (LinearLayout) view.findViewById(R.id.profile_nick_ll);
        myLoverLL = (LinearLayout) view.findViewById(R.id.profile_lover_ll);
        blackHouseLL = (LinearLayout) view.findViewById(R.id.profile_black_house_ll);
        blackSwitch = (SwitchCompat) view.findViewById(R.id.black_house_switch);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_PICK_FROM_GALLERY);
            }
        });
        myLoverLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startLoverPage(getActivity());
            }
        });

        breakupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBreakUpDialog();
            }
        });

        nickNameLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileEditDialogFragment fragment
                        = ProfileEditDialogFragment.newInstance(R.string.nick_name, nickName.getText().toString());
                fragment.setConfirmClickedListener(new ProfileEditDialogFragment.OnConfirmClickedListener() {
                    @Override
                    public void onConfirmClicked(String string) {
                        nickName.setText(string);
                        presenter.updateNickName(string);
                    }
                });
                fragment.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });

        presenter.start();

        return view;
    }

    private void showBreakUpDialog() {
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.break_up_remind)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.startBreakUp(getActivity());
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    @Override
    public void showData(User user, boolean isLover) {
        if (isLover) {
            toolbar.setTitle("我的另一半");
            myLoverLL.setVisibility(View.GONE);
            blackHouseLL.setVisibility(View.VISIBLE);
            breakupBtn.setVisibility(View.VISIBLE);
            nickNameLL.setClickable(false);
            avatar.setClickable(false);
        } else {
            toolbar.setTitle("我的资料");
            myLoverLL.setVisibility(View.VISIBLE);
            blackHouseLL.setVisibility(View.GONE);
            breakupBtn.setVisibility(View.GONE);
            nickNameLL.setClickable(true);
            avatar.setClickable(true);
        }

        if (user != null) {
            String url = user.getAvatar();
            if (url.startsWith("files/image")) {
                url = host + url;
            }
            Glide.with(this)
                    .load(url)
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.mipmap.defalt_avatar)
                    .into(avatar);
            email.setText(user.getEmail());
            nickName.setText(user.getNickName());
            gender.setText(user.isGender() ? "女" : "男");
        }
    }

    @Override
    public void showSuccess(SuccessType type) {
        int msgId;
        switch (type) {
            case UPDATE_AVATAR:
                msgId = R.string.update_avatar_successfully;
                break;
            case UPDATE_NICK_NAME:
                msgId = R.string.update_nick_successfully;
                break;
            case PUT_IN_BLACK:
                msgId = R.string.put_in_black_successfully;
                break;
            case RELEASE_FROM_BLACK:
                msgId = R.string.release_from_black_successfully;
                break;
            default:
                msgId = R.string.update_successfully;
                break;
        }
        Toast.makeText(getActivity(), msgId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(int code) {
        int msgId;
        switch (code) {
            case 105:
                msgId = R.string.invalid_token;
                break;
            case 108:
                msgId = R.string.not_login;
                break;
            default:
                msgId = R.string.network_error;
                break;
        }
        Toast.makeText(getActivity(), msgId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(ProfilePageContract.Presenter presenter) {
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
                    updateAvatar(data.getData());
                    presenter.updateAvatar(path);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return true;
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
        intent.putExtra("return-data", "true");
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
                .getSmallBitmap(path, 300, 300));
    }
}
