package com.my.vkclient.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.my.vkclient.R;
import com.my.vkclient.ui.adapters.BaseRecyclerViewAdapter;

import java.util.List;

import static com.my.vkclient.Constants.RecyclerView.IS_LOAD_COMPLETE_STATE_KEY;
import static com.my.vkclient.Constants.RecyclerView.LINEAR_LAYOUT_MANAGER_STATE_KEY;

public abstract class BaseFragment<T extends BaseRecyclerViewAdapter<E>, E> extends Fragment {
    public final Handler mainLooperHandler;
    public SwipeRefreshLayout swipeRefresh;
    public RecyclerView recyclerView;
    T recyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;

    public BaseFragment() {
        mainLooperHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setupSwipeRefresh(view);
        setupRecyclerView(view);

        if (savedInstanceState != null) {
            recyclerViewAdapter.addItems(getItemsFromRepository());
            linearLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LINEAR_LAYOUT_MANAGER_STATE_KEY));

            if (savedInstanceState.getBoolean(IS_LOAD_COMPLETE_STATE_KEY)) {
                recyclerViewAdapter.setLoadComplete();
            }
        } else {
            recyclerViewAdapter.initialLoadItems();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(LINEAR_LAYOUT_MANAGER_STATE_KEY, linearLayoutManager.onSaveInstanceState());
        outState.putBoolean(IS_LOAD_COMPLETE_STATE_KEY, recyclerViewAdapter.isLoadComplete());
    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    private void setupSwipeRefresh(@NonNull View view) {
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeResources(R.color.colorSwipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!recyclerViewAdapter.isLoading()) {
                    refreshRepository();
                    recyclerViewAdapter.refreshItems();
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = getItemDecoration();

        if (itemDecoration != null) {
            recyclerView.addItemDecoration(itemDecoration);
        }

        createRecyclerViewAdapter();

        recyclerView.setAdapter(recyclerViewAdapter);
    }

    abstract List<E> getItemsFromRepository();

    abstract void refreshRepository();

    abstract void createRecyclerViewAdapter();
}
