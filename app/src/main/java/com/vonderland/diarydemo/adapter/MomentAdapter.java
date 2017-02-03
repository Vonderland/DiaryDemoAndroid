package com.vonderland.diarydemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.bean.Moment;
import com.vonderland.diarydemo.utils.DateTimeUtil;

import java.util.List;

/**
 * Created by Vonderland on 2017/2/2.
 */

public class MomentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<Moment> data;

    private DiaryAdapter.OnRecyclerViewOnClickListener onClickListener;
    private DiaryAdapter.OnRecyclerViewOnLongClickListener onLongClickListener;

    private static final int VIEW_TYPE_MOMENT = 1;
    private static final int VIEW_TYPE_LOADING = 2;
    private static final int VIEW_TYPE_NO_MORE = 3;
    private static final int VIEW_TYPE_EMPTY = 4;

    public MomentAdapter(Context context, List<Moment> data) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        Moment moment = data.get(position);
        int viewType;
        if (moment.isFooter()) {
            viewType = VIEW_TYPE_LOADING;
        } else if (moment.isEmptyView()) {
            viewType = VIEW_TYPE_EMPTY;
        } else if (moment.isNoMore()) {
            viewType = VIEW_TYPE_NO_MORE;
        } else {
            viewType = VIEW_TYPE_MOMENT;
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_MOMENT:
                return new MomentViewHolder(inflater.inflate(R.layout.item_moment, parent, false), onClickListener, onLongClickListener);
            case VIEW_TYPE_EMPTY:
                return new MomentEmptyViewHolder(inflater.inflate(R.layout.item_moment_empty, parent, false));
            case VIEW_TYPE_LOADING:
                return new LoadingFooterHolder(inflater.inflate(R.layout.item_footer, parent, false));
            case VIEW_TYPE_NO_MORE:
                return new NoMoreMomentViewHolder(inflater.inflate(R.layout.item_no_more, parent, false));
            default:
                return new MomentViewHolder(inflater.inflate(R.layout.item_moment, parent, false), onClickListener, onLongClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MomentViewHolder) {
            Moment moment = data.get(position);
            ((MomentViewHolder) holder).title.setText(moment.getTitle());
            ((MomentViewHolder) holder).location.setText(moment.getLocation());
            ((MomentViewHolder) holder).date.setText(DateTimeUtil.formatDate(moment.getEventTime()));
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

    public static class MomentEmptyViewHolder extends RecyclerView.ViewHolder {
        public  MomentEmptyViewHolder(View itemView){
            super(itemView);
        }
    }

    public static class NoMoreMomentViewHolder extends RecyclerView.ViewHolder {
        public NoMoreMomentViewHolder(View itemView){
            super(itemView);
        }
    }

    public static class MomentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        LinearLayout root;
        TextView title;
        TextView location;
        TextView date;
        DiaryAdapter.OnRecyclerViewOnLongClickListener longClickListener;
        DiaryAdapter.OnRecyclerViewOnClickListener clickListener;

        public MomentViewHolder(View itemView, DiaryAdapter.OnRecyclerViewOnClickListener clickListener,
                                DiaryAdapter.OnRecyclerViewOnLongClickListener longClickListener) {
            super(itemView);
            root = (LinearLayout) itemView.findViewById(R.id.bubble_ll);
            title = (TextView) itemView.findViewById(R.id.title);
            location = (TextView) itemView.findViewById(R.id.location);
            date = (TextView) itemView.findViewById(R.id.date);
            this.clickListener = clickListener;
            this.longClickListener = longClickListener;
            root.setOnClickListener(this);
            root.setOnLongClickListener(this);
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

    public void setOnLongClickListener(DiaryAdapter.OnRecyclerViewOnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }


    public void setOnClickListener(DiaryAdapter.OnRecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
