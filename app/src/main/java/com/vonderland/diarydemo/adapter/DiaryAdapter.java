package com.vonderland.diarydemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vonderland.diarydemo.bean.Diary;

import java.util.List;

/**
 * Created by Vonderland on 2017/2/2.
 */

public class DiaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private List<Diary> data;

    private static final int VIEW_TYPE_DIARY = 1;
    private static final int VIEW_TYPE_LOADING = 2;
    private static final int VIEW_TYPE_NO_MORE = 3;
    private static final int VIEW_TYPE_EMPTY = 4;

    public DiaryAdapter(Context context, List<Diary> data) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
