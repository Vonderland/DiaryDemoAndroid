package com.vonderland.diarydemo.detailpage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.utils.DateTimeUtil;

/**
 * Created by Vonderland on 2017/2/4.
 */

public class DiaryDetailFragment extends Fragment implements DetailPageContract.View {

    private DetailPageContract.Presenter presenter;
    private CollapsingToolbarLayout toolbarLayout;
    private Context context;
    private ImageView picture;
    private TextView title;
    private TextView date;
    private TextView description;
    private TextView editInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_diary, container, false);

        setHasOptionsMenu(true);
        toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);

        DetailActivity activity = (DetailActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        picture = (ImageView) view.findViewById(R.id.diary_pic);
        title = (TextView) view.findViewById(R.id.diary_title);
        date = (TextView) view.findViewById(R.id.diary_date);
        description = (TextView) view.findViewById(R.id.diary_description);
        editInfo = (TextView) view.findViewById(R.id.edit_info);

        presenter.start();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public void showData(Object data) {
        Diary diary = (Diary) data;
        String url = diary.getUrl();
        if (TextUtils.isEmpty(url)) {
            picture.setImageResource((R.drawable.giraffe));
        } else {
            if (url.startsWith("files/image/diaryImage")) {
                url = Constant.HOST + url;
            }
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(picture);
        }

        setCollapsingToolbarLayoutTitle(diary.getTitle());
        title.setText(diary.getTitle());
        date.setText(DateTimeUtil.formatDate(diary.getEventTime()));
        description.setText(((Diary) data).getDescription());

        if (diary.getUpdateTime() > diary.getCreateTime()) {
            editInfo.setText("编辑于 " + DateTimeUtil.formatDate(diary.getUpdateTime()));
        } else {
            editInfo.setText("创建于 " + DateTimeUtil.formatDate(diary.getCreateTime()));
        }
    }

    @Override
    public void setPresenter(DetailPageContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    private void setCollapsingToolbarLayoutTitle(String title) {
        toolbarLayout.setTitle(title);
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
        } else if (id == R.id.action_edit) {
            Toast.makeText(context, "编辑日记", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu, menu);
    }
}
