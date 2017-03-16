package com.vonderland.diarydemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.Diary;
import com.vonderland.diarydemo.constant.Constant;
import com.vonderland.diarydemo.utils.DateTimeUtil;
import com.vonderland.diarydemo.utils.SharedPrefUtil;

import java.util.List;

/**
 * Created by Vonderland on 2017/2/2.
 */

public class DiaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<Diary> data;
    private OnRecyclerViewOnClickListener onClickListener;
    private OnRecyclerViewOnLongClickListener onLongClickListener;

    private static final int VIEW_TYPE_DIARY = 1;
    private static final int VIEW_TYPE_LOADING = 2;
    private static final int VIEW_TYPE_NO_MORE = 3;
    private static final int VIEW_TYPE_EMPTY = 4;
    private static final int VIEW_TYPE_DIARY_NO_PIC = 5;

    public DiaryAdapter(Context context, List<Diary> data) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        Diary diary = data.get(position);
        String url = diary.getUrl();
        int viewType;
        if (diary.isFooter()) {
            viewType = VIEW_TYPE_LOADING;
        } else if (diary.isEmptyView()) {
            viewType = VIEW_TYPE_EMPTY;
        } else if (diary.isNoMore()) {
            viewType = VIEW_TYPE_NO_MORE;
        } else if (TextUtils.isEmpty(url)){
            viewType = VIEW_TYPE_DIARY_NO_PIC;
        } else {
            viewType = VIEW_TYPE_DIARY;
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_DIARY:
                return new DiaryViewHolder(inflater.inflate(R.layout.item_diary, parent, false), onClickListener, onLongClickListener);
            case VIEW_TYPE_EMPTY:
                return new DiaryEmptyViewHolder(inflater.inflate(R.layout.item_diary_empty, parent, false));
            case VIEW_TYPE_LOADING:
                return new LoadingFooterHolder(inflater.inflate(R.layout.item_footer, parent, false));
            case VIEW_TYPE_NO_MORE:
                return new NoMoreDiaryViewHolder(inflater.inflate(R.layout.item_no_more, parent, false));
            default:
                return new NoPictureDiaryViewHolder(inflater.inflate(R.layout.item_diary_no_picture, parent, false), onClickListener, onLongClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DiaryViewHolder) {
            Diary diary = data.get(position);

            if (TextUtils.isEmpty(diary.getUrl())) {
                ((DiaryViewHolder) holder).picture.setVisibility(View.GONE);
            } else {
                String url = diary.getUrl();
                if (url.startsWith("files/image/diaryImage")) {
                    url = SharedPrefUtil.getInstance().get(Constant.SP_KEY_HOST, Constant.HOST) + url;
                }
                Glide.with(context)
                        .load(url)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(((DiaryViewHolder) holder).picture);
            }
            ((DiaryViewHolder) holder).title.setText(diary.getTitle());
            ((DiaryViewHolder) holder).description.setText(diary.getDescription());
            ((DiaryViewHolder) holder).date.setText(DateTimeUtil.formatDate(diary.getEventTime()));
        } else if (holder instanceof NoPictureDiaryViewHolder) {
            Diary diary = data.get(position);
            ((NoPictureDiaryViewHolder) holder).title.setText(diary.getTitle());
            ((NoPictureDiaryViewHolder) holder).description.setText(diary.getDescription());
            ((NoPictureDiaryViewHolder) holder).date.setText(DateTimeUtil.formatDate(diary.getEventTime()));
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

    public static class DiaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        LinearLayout root;
        ImageView picture;
        TextView title;
        TextView description;
        TextView date;
        OnRecyclerViewOnLongClickListener longClickListener;
        OnRecyclerViewOnClickListener clickListener;

        public DiaryViewHolder(View itemView, OnRecyclerViewOnClickListener clickListener,
                               OnRecyclerViewOnLongClickListener longClickListener) {
            super(itemView);
            root = (LinearLayout) itemView.findViewById(R.id.diary_root);
            picture = (ImageView)itemView.findViewById(R.id.picture);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            date = (TextView) itemView.findViewById(R.id.date);
            this.clickListener = clickListener;
            this.longClickListener = longClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.OnItemClick(v,getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (longClickListener != null) {
                longClickListener.OnItemLongClick(v,getLayoutPosition());
            }
            return true;
        }
    }

    public static class NoPictureDiaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        LinearLayout root;
        TextView title;
        TextView description;
        TextView date;
        OnRecyclerViewOnLongClickListener longClickListener;
        OnRecyclerViewOnClickListener clickListener;

        public NoPictureDiaryViewHolder(View itemView, OnRecyclerViewOnClickListener clickListener,
                               OnRecyclerViewOnLongClickListener longClickListener) {
            super(itemView);
            root = (LinearLayout) itemView.findViewById(R.id.diary_root);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            date = (TextView) itemView.findViewById(R.id.date);
            this.clickListener = clickListener;
            this.longClickListener = longClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.OnItemClick(v,getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (longClickListener != null) {
                longClickListener.OnItemLongClick(v,getLayoutPosition());
            }
            return true;
        }
    }

    public void setOnLongClickListener(OnRecyclerViewOnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }


    public void setOnClickListener(OnRecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnRecyclerViewOnClickListener {

        void OnItemClick(View v,int position);

    }

    public interface OnRecyclerViewOnLongClickListener {

        void OnItemLongClick(View v,int position);

    }
}
