package com.vonderland.diarydemo.homepage;

import android.view.View;

import com.vonderland.diarydemo.adapter.DiaryAdapter;
import com.vonderland.diarydemo.adapter.MomentAdapter;
import com.vonderland.diarydemo.utils.SnackbarUtil;

import java.util.List;

/**
 * Created by Vonderland on 2017/2/3.
 */

public class MomentFragment extends BaseHomePageFragment {
    
    private MomentAdapter momentAdapter;
    @Override
    public void showData(List data) {
        if (momentAdapter == null) {
            momentAdapter = new MomentAdapter(getActivity(), data);
            momentAdapter.setOnClickListener(new DiaryAdapter.OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    SnackbarUtil.make(getActivity(), "click moment position = " + position)
                            .show();
                }
            });
            momentAdapter.setOnLongClickListener(new DiaryAdapter.OnRecyclerViewOnLongClickListener() {
                @Override
                public void OnItemLongClick(View v, int position) {
                    SnackbarUtil.make(getActivity(), "long click moment position = " + position)
                            .show();
                }
            });
            recyclerView.setAdapter(momentAdapter);
        } else {
            momentAdapter.notifyDataSetChanged();
        }
    }
}
