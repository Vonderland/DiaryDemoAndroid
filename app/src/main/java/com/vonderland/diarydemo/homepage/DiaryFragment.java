package com.vonderland.diarydemo.homepage;

import android.app.Fragment;
import android.view.View;

import com.vonderland.diarydemo.adapter.DiaryAdapter;
import com.vonderland.diarydemo.utils.L;
import com.vonderland.diarydemo.utils.SnackbarUtil;

import java.util.List;

/**
 * Created by Vonderland on 2017/2/2.
 */

public class DiaryFragment extends BaseHomePageFragment {

    private DiaryAdapter diaryAdapter;

    @Override
    public void showData(List data) {
        if (diaryAdapter == null) {
            diaryAdapter = new DiaryAdapter(getActivity(), data);
            diaryAdapter.setOnClickListener(new DiaryAdapter.OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
//                    SnackbarUtil.make(getActivity(), "click diary position = " + position)
//                    .show();
                    L.d("vonderlandDebug", "click");
                }
            });
            diaryAdapter.setOnLongClickListener(new DiaryAdapter.OnRecyclerViewOnLongClickListener() {
                @Override
                public void OnItemLongClick(View v, int position) {
//                    SnackbarUtil.make(getActivity(), "long click diary position = " + position)
//                            .show();
                    L.d("vonderlandDebug", "long click");
                }
            });
            recyclerView.setAdapter(diaryAdapter);
        } else {
            diaryAdapter.notifyDataSetChanged();
        }
    }
}
