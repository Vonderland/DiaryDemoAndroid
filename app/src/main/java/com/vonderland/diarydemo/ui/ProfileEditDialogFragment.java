package com.vonderland.diarydemo.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vonderland.diarydemo.R;

/**
 * Created by Vonderland on 2017/3/13.
 */

public class ProfileEditDialogFragment extends DialogFragment {
    private static final String KEY_TITLE = "key_title";
    private static final String KEY_ORIGINAL_STRING = "key_original";

    private TextView titleTV;
    private EditText textET;

    private final int LENGTH_LIMIT = 20;
    private OnConfirmClickedListener confirmClickedListener;
    public static ProfileEditDialogFragment newInstance(int titleResId, String originalStr) {
        ProfileEditDialogFragment fragment = new ProfileEditDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TITLE, titleResId);
        bundle.putString(KEY_ORIGINAL_STRING, originalStr);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.ShareDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_dialog_profile_edit);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimate);

        titleTV = (TextView) dialog.findViewById(R.id.title_tv);
        textET = (EditText) dialog.findViewById(R.id.edit_text);

        titleTV.setText(getArguments().getInt(KEY_TITLE));
        textET.setText(getArguments().getString(KEY_ORIGINAL_STRING));
        textET.setSelection(getArguments().getString(KEY_ORIGINAL_STRING).length());

        dialog.findViewById(R.id.confirm_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(textET.getText().toString())) {
                    Toast.makeText(getActivity(), "你还没有输入哦", Toast.LENGTH_SHORT).show();
                } else if (textET.getText().toString().length() > LENGTH_LIMIT) {
                    Toast.makeText(getActivity(), "输入太长啦", Toast.LENGTH_SHORT).show();
                } else {
                    if (confirmClickedListener != null) {
                        confirmClickedListener.onConfirmClicked(textET.getText().toString());
                    }
                    dialog.dismiss();
                }
            }
        });
        /*
         *设置 dialog 宽度为屏宽、对齐方式为靠近屏幕底部。
         */
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public OnConfirmClickedListener getConfirmClickedListener() {
        return confirmClickedListener;
    }

    public void setConfirmClickedListener(OnConfirmClickedListener confirmClickedListener) {
        this.confirmClickedListener = confirmClickedListener;
    }

    public interface OnConfirmClickedListener {
        void onConfirmClicked(String string);
    }
}
