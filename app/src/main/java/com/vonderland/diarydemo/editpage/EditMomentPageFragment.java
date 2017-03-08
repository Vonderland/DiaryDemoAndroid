package com.vonderland.diarydemo.editpage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.Moment;
import com.vonderland.diarydemo.utils.DateTimeUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Created by Vonderland on 2017/2/7.
 */

public class EditMomentPageFragment  extends Fragment implements EditMomentPageContract.View {

    private EditMomentPageContract.Presenter presenter;
    private Context context;
    private EditText title;
    private LinearLayout calendarBtn;
    private TextView date;
    private EditText location;

    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_moment, container, false);

        setHasOptionsMenu(true);
        EditActivity activity = (EditActivity) getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("编辑纪念日");
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendarBtn = (LinearLayout) view.findViewById(R.id.calendar_ll);
        title = (EditText) view.findViewById(R.id.edit_moment_title);
        date = (TextView) view.findViewById(R.id.date);
        location = (EditText) view.findViewById(R.id.location);
        presenter.start();

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarDialog();
            }
        });
        return view;
    }

    private void showCalendarDialog() {
        Calendar now = Calendar.getInstance();
        now.set(mYear, mMonth, mDay);
        DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                Calendar temp = Calendar.getInstance();
                temp.clear();
                temp.set(year, monthOfYear, dayOfMonth);
                date.setText(DateTimeUtil.formatDate(temp.getTimeInMillis()));
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        dialog.setMaxDate(Calendar.getInstance());
        Calendar minDate = Calendar.getInstance();
        minDate.set(1970, 1, 1);
        dialog.setMinDate(minDate);
        dialog.vibrate(false);

        dialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
    }

    @Override
    public void showData(Moment data) {
        title.setText(data.getTitle());
        location.setText(data.getLocation());
        date.setText(DateTimeUtil.formatDate(data.getEventTime()));

        mYear = DateTimeUtil.getYear(data.getEventTime());
        mMonth = DateTimeUtil.getMonth(data.getEventTime());
        mDay = DateTimeUtil.getDay(data.getEventTime());
    }

    @Override
    public void showError(int code) {
        if (code == -1) {
            Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "啊噢，提交失败，再试一次吧~", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSuccessfully(boolean isEdit) {
        if(isEdit) {
            Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "成功记录了一个纪念日！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setPresenter(EditMomentPageContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
        } else if (id == R.id.action_edit) {
            String momentTitle = title.getText().toString();
            String momentLocation = location.getText().toString();
            Calendar now = Calendar.getInstance();
            now.set(mYear, mMonth, mDay, 0, 0, 0);
            Long eventTime = now.getTimeInMillis() / 1000 * 1000;
            if (TextUtils.isEmpty(momentTitle)) {
                Toast.makeText(context, "还没填写标题哦", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.equals(date.getText(), context.getResources().getString(R.string.please_choose_moment_time))) {
                Toast.makeText(context, "还没选择时间哦", Toast.LENGTH_SHORT).show();
            } else {
                presenter.postData(momentTitle, eventTime, momentLocation);
            }
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
    }
}
