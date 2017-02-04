package com.vonderland.diarydemo.homepage;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.vonderland.diarydemo.R;
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
    public void showDeleteSuccessfully() {
        Toast.makeText(getActivity(), R.string.delete_diary_successfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showData(List data) {
        if (diaryAdapter == null) {
            diaryAdapter = new DiaryAdapter(getActivity(), data);
            diaryAdapter.setOnClickListener(new DiaryAdapter.OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    presenter.startDetail(position);
                }
            });
            diaryAdapter.setOnLongClickListener(new DiaryAdapter.OnRecyclerViewOnLongClickListener() {
                @Override
                public void OnItemLongClick(View v, int position) {
                    showRemoveDialog(position);
                }
            });
            recyclerView.setAdapter(diaryAdapter);
        } else {
            diaryAdapter.notifyDataSetChanged();
        }
    }

    private void showRemoveDialog(final int pos) {
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage("确定删除这篇日记吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteData(pos);
                        diaryAdapter.notifyItemRemoved(pos);
                        diaryAdapter.notifyItemRangeChanged(pos, diaryAdapter.getItemCount());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        dialog.show();
    }
}
