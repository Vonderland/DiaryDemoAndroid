package com.vonderland.diarydemo.homepage;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.utils.L;

/**
 * Created by Vonderland on 2017/2/3.
 */

abstract public class BaseHomePageFragment extends Fragment implements HomePageContract.View {

    protected SwipeRefreshLayout refreshLayout;
    protected RecyclerView recyclerView;
    protected FloatingActionButton fab;
    protected HomePageContract.Presenter presenter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        initViews(view);

        presenter.start();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                presenter.refresh();
            }

        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingToLast = dy > 0;
                // 隐藏或者显示fab
                if(dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
                if (dy > 0 && !refreshLayout.isRefreshing()) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int total = recyclerView.getLayoutManager().getItemCount();
                    int lastVisibleIndex = layoutManager.findLastVisibleItemPosition();
                    if (!presenter.isLoadingMore() && presenter.hasMoreItems() &&
                            /*total >= Constant.PAGE_SIZE && */total <= lastVisibleIndex + 1) {
                        presenter.loadMore();
                    }
                }
            }
        });
        return view;
    }

    public void initViews (View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
    }

    @Override
    public void showRefreshing() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void stopRefreshing() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void showError() {
        Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void setPresenter(HomePageContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        L.d("onResumeTest", this.getClass().toString());
    }
}
