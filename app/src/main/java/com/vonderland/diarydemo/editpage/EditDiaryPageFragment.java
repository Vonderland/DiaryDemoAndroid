package com.vonderland.diarydemo.editpage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.utils.DateTimeUtil;
import com.vonderland.diarydemo.utils.L;
import com.vonderland.diarydemo.utils.PictureUtil;
import com.vonderland.diarydemo.utils.SharedPrefUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Vonderland on 2017/2/4.
 */

public class EditDiaryPageFragment extends Fragment implements EditDiaryPageContract.View {

    private EditDiaryPageContract.Presenter presenter;
    private Context context;
    private ImageView picture;
    private EditText title;
    private LinearLayout calendarBtn;
    private TextView date;
    private LinearLayout modeLL;
    private TextView mode;
    private SwitchCompat privateSwitch;
    private EditText description;
    private ImageView deletePicBtn;

    private String filePath;
    private int change = 0;

    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    public final static int REQUEST_IMAGE = 1;
    private Uri outputFileUri;
    private String host;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        host = (String)SharedPrefUtil.getInstance().get(Constant.SP_KEY_HOST, Constant.HOST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_diary, container, false);

        setHasOptionsMenu(true);
        EditActivity activity = (EditActivity) getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("编辑日记");
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        picture = (ImageView) view.findViewById(R.id.edit_diary_picture);
        deletePicBtn = (ImageView) view.findViewById(R.id.delete_pic);
        calendarBtn = (LinearLayout) view.findViewById(R.id.calendar);
        title = (EditText) view.findViewById(R.id.edit_diary_title);
        date = (TextView) view.findViewById(R.id.date);
        description = (EditText) view.findViewById(R.id.edit_diary_content);
        modeLL = (LinearLayout) view.findViewById(R.id.mode_ll);
        mode = (TextView) view.findViewById(R.id.private_mode);
        privateSwitch = (SwitchCompat) view.findViewById(R.id.private_switch);

        presenter.start();

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarDialog();
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        deletePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture.setImageResource(R.drawable.add_picture);
                change = 1;
                filePath = null;
                deletePicBtn.setVisibility(View.GONE);
            }
        });

        privateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mode.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
                } else {
                    mode.setTextColor(ContextCompat.getColor(getActivity(), R.color.hintText));
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE) {
            outputFileUri = data.getData();
            filePath = PictureUtil.getRealPathFromURI(getActivity(), outputFileUri);

            picture.setImageBitmap(PictureUtil
                    .getSmallBitmap(filePath, 150, 150));
            change = 1;
            L.d("vonderlanddebug", filePath);
            deletePicBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showData(Diary data) {
        title.setText(data.getTitle());
        description.setText(data.getDescription());
        date.setText(DateTimeUtil.formatDate(data.getEventTime()));

        mYear = DateTimeUtil.getYear(data.getEventTime());
        mMonth = DateTimeUtil.getMonth(data.getEventTime());
        mDay = DateTimeUtil.getDay(data.getEventTime());

        if (data.getUid() != (long)SharedPrefUtil.getInstance().get(Constant.SP_KEY_UID, 0l)) {
            modeLL.setVisibility(View.GONE);
        }

        privateSwitch.setChecked(data.isPrivate());
        if (data.isPrivate()) {
            mode.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        } else {
            mode.setTextColor(ContextCompat.getColor(getActivity(), R.color.hintText));
        }

        if (!TextUtils.isEmpty(data.getUrl())) {
            String url = data.getUrl();
            if (url.startsWith("files/image/diaryImage")) {
                url = host + url;
            }
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(picture);
            deletePicBtn.setVisibility(View.VISIBLE);
        }
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
            Toast.makeText(context, "成功提交了一篇日记！", Toast.LENGTH_SHORT).show();
        }
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
    public void setPresenter(EditDiaryPageContract.Presenter presenter) {
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
            String diaryTitle = title.getText().toString();
            String diaryDescription = description.getText().toString();
            Calendar now = Calendar.getInstance();
            now.set(mYear, mMonth, mDay, 0, 0, 0);
            Long eventTime = now.getTimeInMillis() / 1000 * 1000;
            if (TextUtils.isEmpty(diaryTitle)) {
                Toast.makeText(context, "还没填写标题哦", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(diaryDescription)) {
                Toast.makeText(context, "还没填写日记内容哦", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.equals(date.getText(), context.getResources().getString(R.string.please_choose_time))) {
                Toast.makeText(context, "还没选择时间哦", Toast.LENGTH_SHORT).show();
            } else {
                presenter.postData(diaryTitle, eventTime, diaryDescription, filePath, change, privateSwitch.isChecked());
            }

        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
    }
}
