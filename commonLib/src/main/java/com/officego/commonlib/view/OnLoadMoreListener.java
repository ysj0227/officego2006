package com.officego.commonlib.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by YangShiJie
 * Data 2020/7/7.
 * Descriptions:
 **/
public abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener {
    private int countItem;
    private int lastItem;
    private boolean isScrolled = false;
    private static boolean isAllScreen = false;
    private RecyclerView.LayoutManager mLayoutManager;

    protected abstract void onLoading(int countItem, int lastItem);

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
            isScrolled = true;
            isAllScreen = true;
        } else {
            isScrolled = false;
        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            mLayoutManager = recyclerView.getLayoutManager();
            countItem = mLayoutManager.getItemCount();
            lastItem = ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition();
        }
        if (isScrolled && countItem != lastItem && lastItem == countItem - 1) {
            onLoading(countItem, lastItem);
        }
    }

    public static boolean isAllScreen() {
        return isAllScreen;
    }
}
