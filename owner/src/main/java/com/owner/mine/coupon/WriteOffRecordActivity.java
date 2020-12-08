package com.owner.mine.coupon;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.OnLoadMoreListener;
import com.owner.R;
import com.owner.adapter.WriteOffAdapter;
import com.owner.mine.contract.WriteOffContract;
import com.owner.mine.presenter.WriteOffPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/8
 * 核销记录
 **/
@EActivity(resName = "coupon_activity_writeoff")
public class WriteOffRecordActivity extends BaseMvpActivity<WriteOffPresenter>
        implements WriteOffContract.View,
        SwipeRefreshLayout.OnRefreshListener {
    @ViewById(resName = "bga_refresh")
    SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(resName = "rv_write_off")
    RecyclerView rvWriteOff;
    @ViewById(resName = "tv_coupon_no_data")
    TextView tvNoData;
    @ViewById(resName = "rl_exception")
    RelativeLayout rlException;

    //当前页码
    private int pageNum = 1;
    //list 是否有更多
    private boolean hasMore;
    private WriteOffAdapter adapter;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        mPresenter = new WriteOffPresenter();
        mPresenter.attachView(this);
        initViews();
        initRefresh();
        getList();
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvWriteOff.setLayoutManager(layoutManager);
    }

    private void testList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add("");
        }
        adapter = new WriteOffAdapter(context, list);
        rvWriteOff.setAdapter(adapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(true, -20, 100);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.common_blue_main_80a, R.color.common_blue_main);
        rvWriteOff.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                loadingMoreList();
            }
        });
        //解决下拉刷新快速滑动crash
        rvWriteOff.setOnTouchListener((view, motionEvent) ->
                mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing());
    }

    private void getList() {
//        mPresenter.getWriteOffList();
        testList();
    }

    //加载更多
    private void loadingMoreList() {
        if (NetworkUtils.isNetworkAvailable(context) && hasMore) {
            pageNum++;
            getList();
        }
    }

    //开始下拉刷新
    @Override
    public void onRefresh() {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            netException();
            return;
        }
        getList();
    }

    private void netException() {
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.VISIBLE);
        rvWriteOff.setVisibility(View.GONE);
    }

    @Override
    public void writeOffListSuccess() {
        //todo
    }
}
