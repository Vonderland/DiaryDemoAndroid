package com.vonderland.diarydemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.utils.DateTimeUtil;

import java.util.List;

/**
 * Created by Vonderland on 2017/2/2.
 */

public class DiaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    public int getItemViewType(int position) {
        Diary diary = data.get(position);
        int viewType;
        if (diary.isFooter()) {
            viewType = VIEW_TYPE_LOADING;
        } else if (diary.isEmptyView()) {
            viewType = VIEW_TYPE_EMPTY;
        } else if (diary.isNoMore()) {
            viewType = VIEW_TYPE_NO_MORE;
        } else {
            viewType = VIEW_TYPE_DIARY;
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_DIARY:
                return new DiaryViewHolder(inflater.inflate(R.layout.item_diary, parent, false));
            case VIEW_TYPE_EMPTY:
                return new DiaryEmptyViewHolder(inflater.inflate(R.layout.item_diary_empty, parent, false));
            case VIEW_TYPE_LOADING:
                return new LoadingFooterHolder(inflater.inflate(R.layout.item_footer, parent, false));
            case VIEW_TYPE_NO_MORE:
                return new NoMoreDiaryViewHolder(inflater.inflate(R.layout.item_no_more, parent, false));
            default:
                return new DiaryViewHolder(inflater.inflate(R.layout.item_diary, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DiaryViewHolder) {
            Diary diary = data.get(position);

            if (TextUtils.isEmpty(diary.getUrl())) {
                ((DiaryViewHolder) holder).picture.setVisibility(View.GONE);
            } else {
                Glide.with(context)
                        .load(diary.getUrl())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(((DiaryViewHolder) holder).picture);
            }
            ((DiaryViewHolder) holder).title.setText(diary.getTitle());
            ((DiaryViewHolder) holder).description.setText(diary.getDescription());
            ((DiaryViewHolder) holder).date.setText(DateTimeUtil.formatDate(diary.getEventTime()));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class LoadingFooterHolder extends RecyclerView.ViewHolder {
        public LoadingFooterHolder(View itemView){
            super(itemView);
        }
    }

    public static class DiaryEmptyViewHolder extends RecyclerView.ViewHolder {
        public  DiaryEmptyViewHolder(View itemView){
            super(itemView);
        }
    }

    public static class NoMoreDiaryViewHolder extends RecyclerView.ViewHolder {
        public NoMoreDiaryViewHolder(View itemView){
            super(itemView);
        }
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        TextView title;
        TextView description;
        TextView date;

        public DiaryViewHolder(View itemView) {
            super(itemView);
            picture = (ImageView)itemView.findViewById(R.id.picture);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
