package com.owner.mine.coupon;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.model.CouponWriteOffListBean;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.OnLoadMoreListener;
import com.owner.R;
import com.owner.adapter.WriteOffAdapter;
import com.owner.mine.contract.WriteOffContract;
import com.owner.mine.presenter.WriteOffPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
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

    private int pageNum = 1; //当前页码
    private boolean hasMore;
    private WriteOffAdapter adapter;
    private List<CouponWriteOffListBean.ListBean> list = new ArrayList<>();

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

    //网络异常重试
    @Click(resName = "btn_again")
    void exceptionAgainClick() {
        mPresenter.getWriteOffList(pageNum);
    }


    private void getList() {
        mPresenter.getWriteOffList(pageNum);
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
        //初始化
        list.clear();
        pageNum = 1;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        getList();
    }

    @Override
    public void endRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void writeOffListSuccess(CouponWriteOffListBean data) {
        if (data == null || (pageNum == 1 && data.getList().size() == 0)) {
            hasMore = false;
            noData();
            return;
        }
        hasMore = data.getList().size() >= 10;
        hasData();
        list.addAll(data.getList());
        if (adapter == null) {
            adapter = new WriteOffAdapter(context, list);
            rvWriteOff.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    private void hasData() {
        rvWriteOff.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.GONE);
    }

    private void noData() {
        rvWriteOff.setVisibility(View.GONE);
        tvNoData.setVisibility(View.VISIBLE);
    }

    private void netException() {
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.VISIBLE);
        rvWriteOff.setVisibility(View.GONE);
    }
}
