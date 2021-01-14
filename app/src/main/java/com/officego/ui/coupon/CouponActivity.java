package com.officego.ui.coupon;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;
import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.model.CouponListBean;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.adapter.CouponAdapter;
import com.officego.ui.coupon.contract.CouponListContract;
import com.officego.ui.coupon.presenter.CouponListPresenter;
import com.officego.view.WrapContentLinearLayoutManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/4
 **/
@SuppressLint("Registered")
@EActivity(R.layout.coupon_activity_list)
public class CouponActivity extends BaseMvpActivity<CouponListPresenter> implements
        CouponListContract.View,
        TabLayout.OnTabSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    @ViewById(R.id.bga_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.rv_view)
    RecyclerView rvView;
    @ViewById(R.id.tv_no_data)
    TextView tvNoData;
    @ViewById(R.id.tl_view)
    TabLayout tlView;

    //卡券类型
    private int mStatus = 1;
    private final List<CouponListBean.ListBean> list = new ArrayList<>();
    private CouponAdapter adapter;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        mPresenter = new CouponListPresenter();
        mPresenter.attachView(this);
        initViews();
        initRefresh();
        getList(1);
    }

    private void getList(int status) {
        mStatus = status;
        mPresenter.getCouponList(mStatus);
    }

    private void initViews() {
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(context);
        rvView.setLayoutManager(layoutManager);
        tlView.addOnTabSelectedListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(true, -20, 100);
        mSwipeRefreshLayout.setColorSchemeResources(com.owner.R.color.common_blue_main_80, com.owner.R.color.common_blue_main);
        //解决下拉刷新快速滑动crash
        rvView.setOnTouchListener((view, motionEvent) -> mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing());
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            shortTip(R.string.toast_network_Exception);
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            return;
        }
        switch (tab.getPosition()) {
            case 0:
                getList(1);
                break;
            case 1:
                getList(2);
                break;
        }
    }

    @Override
    public void couponListSuccess(List<CouponListBean.ListBean> data) {
        if (data == null || data.size() == 0) {
            noData();
            return;
        }
        hasData();
        list.clear();
        list.addAll(data);
        adapter = new CouponAdapter(context, mStatus != 2, list);
        rvView.setAdapter(adapter);
    }

    @Override
    public void endRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //开始下拉刷新
    @Override
    public void onRefresh() {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            shortTip(R.string.toast_network_Exception);
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            return;
        }
        getList(mStatus);
    }

    private void hasData() {
        rvView.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.GONE);
    }

    private void noData() {
        rvView.setVisibility(View.GONE);
        tvNoData.setVisibility(View.VISIBLE);
    }

}
