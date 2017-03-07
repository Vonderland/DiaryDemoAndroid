package com.vonderland.diarydemo.homepage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.adapter.DiaryAdapter;
import com.vonderland.diarydemo.adapter.MomentAdapter;

import java.util.List;

/**
 * Created by Vonderland on 2017/2/3.
 */

public class MomentFragment extends BaseHomePageFragment {
    
    private MomentAdapter momentAdapter;

    @Override
    public void showDeleteSuccessfully() {
        Toast.makeText(getActivity(), R.string.delete_moment_successfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showData(List data) {
        if (momentAdapter == null) {
            momentAdapter = new MomentAdapter(getActivity(), data);
            momentAdapter.setOnClickListener(new DiaryAdapter.OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View v, final int position) {
                    new MaterialDialog.Builder(getActivity())
                            .items(R.array.moment_click_items)
//                            .contentColor(getResources().getColor(R.color.colorPrimary))
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    switch (which) {
                                        case 0:
                                            presenter.startDetail(position);
                                            break;
                                        case 1:
                                            dialog.dismiss();
                                            showRemoveDialog(position);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            })
                            .show();
                }
            });
            momentAdapter.setOnLongClickListener(new DiaryAdapter.OnRecyclerViewOnLongClickListener() {
                @Override
                public void OnItemLongClick(View v, int position) {
                    showRemoveDialog(position);
                }
            });
            recyclerView.setAdapter(momentAdapter);
        } else {
            momentAdapter.notifyDataSetChanged();
        }
    }

    private void showRemoveDialog(final int pos) {
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage("确定删除这个纪念日吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteData(pos);
                        momentAdapter.notifyItemRemoved(pos);
                        momentAdapter.notifyItemRangeChanged(pos, momentAdapter.getItemCount());
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
