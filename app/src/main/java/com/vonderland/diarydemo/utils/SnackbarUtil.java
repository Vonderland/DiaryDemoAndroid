package com.vonderland.diarydemo.utils;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vonderland.diarydemo.R;


public class SnackbarUtil {
    public static Snackbar make(Activity activity){
        return Snackbar.make(getActivityView(activity), "", Snackbar.LENGTH_SHORT);
    }

    public static Snackbar make(Activity activity, CharSequence text){
        Snackbar snackbar = Snackbar.make(getActivityView(activity), text, Snackbar.LENGTH_SHORT);
        changeMsgColor(snackbar, R.color.white);
        return snackbar;
    }

    public static Snackbar make(Activity activity, int resId){
        return Snackbar.make(getActivityView(activity), resId, Snackbar.LENGTH_SHORT);
    }

    public static Snackbar make(Activity activity, CharSequence text, int duration){
        return Snackbar.make(getActivityView(activity), text, duration);
    }

    public static Snackbar make(Activity activity, int resId, int duration){
        return Snackbar.make(getActivityView(activity), resId, duration);
    }

    /*
     * 创建一个带两个按钮的 Snackbar
     * 其中,一个按钮为取消,点击后该 Snackbar 消失
     * 另一个按钮显示的文字用 actionResId 指定,使用 listener 来指定点击后的动作
     */
    public static Snackbar makeCancelableSnackbarWithAction(Activity activity, int msgResId, int actionResId, final View.OnClickListener listener){
        final Snackbar snackbar = Snackbar.make(getActivityView(activity), msgResId, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        changeMsgColor(snackbar, R.color.white);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarView;
        View actionsView = LayoutInflater.from(snackbarView.getContext()).inflate(R.layout.view_cancelable_snackbar_with_action, null);
        actionsView.findViewById(R.id.cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        TextView actionTV = (TextView) actionsView.findViewById(R.id.action_tv);
        actionTV.setText(actionResId);
        actionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                if(listener != null){
                    listener.onClick(v);
                }
            }
        });
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.gravity = Gravity.CENTER_VERTICAL;
        snackbarLayout.addView(actionsView, p);
        return snackbar;
    }

    private static void changeMsgColor(Snackbar snackbar, int colorId){
        View snackbarView = snackbar.getView();
        ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(ContextCompat.getColor(snackbarView.getContext(), colorId));
    }

    private static View getActivityView(Activity activity){
        return ((ViewGroup)activity.getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);
    }
}
