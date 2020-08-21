package com.officego.ui.collect;

import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.officego.R;
import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.adapter.CollectOfficeBuildingAdapter;
import com.officego.ui.adapter.CollectWorkOfficeAdapter;
import com.officego.ui.collect.contract.CollectedContract;
import com.officego.ui.collect.model.CollectBuildingBean;
import com.officego.ui.collect.model.CollectHouseBean;
import com.officego.ui.collect.presenter.CollectedPresenter;
import com.officego.ui.home.OnLoadMoreListener;
import com.officego.ui.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@EFragment(R.layout.collect_fragment)
public class CollectFragment extends BaseMvpFragment<CollectedPresenter>
        implements CollectedContract.View, SwipeRefreshLayout.OnRefreshListener {
    private static final int REQUEST_CODE = 1002;
    @ViewById(R.id.ctl_root)
    ConstraintLayout ctlRoot;
    @ViewById(R.id.ctl_no_login)
    ConstraintLayout ctlNoLogin;
    @ViewById(R.id.bga_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.rl_collect)
    RecyclerView rlCollect;
    @ViewById(R.id.tv_office_building)
    TextView tvOfficeBuilding;
    @ViewById(R.id.tv_work_office)
    TextView tvWorkOffice;
    //暂无数据，网络异常
    @ViewById(R.id.fl_no_data)
    FrameLayout tvNoData;
    @ViewById(R.id.rl_exception)
    RelativeLayout rlException;
    //当前页码
    private int pageNum = 1;
    //list 是否有更多
    private boolean hasMore;
    //adapter
    private CollectOfficeBuildingAdapter collectOfficeBuildingAdapter;
    private List<CollectBuildingBean.ListBean> collectBuildingList = new ArrayList<>();
    private CollectWorkOfficeAdapter workOfficeAdapter;
    private List<CollectHouseBean.ListBean> workOfficeList = new ArrayList<>();
    private boolean isOffice = true;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        mPresenter = new CollectedPresenter();
        mPresenter.attachView(this);
        ctlRoot.setPadding(0, CommonHelper.statusHeight(mActivity), 0, 0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rlCollect.setLayoutManager(layoutManager);
        initRefresh();
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            netException();
            return;
        }
        //当前未登录状态
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            noLoginView();
        } else {
            setListInit();
        }
    }

    private void setListInit() {
        pageNum = 1;
        collectBuildingList.clear();
        workOfficeList.clear();
        collectOfficeBuildingAdapter = null;
        workOfficeAdapter = null;
        getList(isOffice);
    }

    //初始化
    private void getList(boolean isOffice) {
        if (isOffice) {
            mPresenter.favoriteBuildingList(pageNum, "", "");
        } else {
            mPresenter.favoriteHouseList(pageNum, "", "");
        }
    }

    //刷新
    private void initRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(true, -20, 100);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.common_blue_main_80a, R.color.common_blue_main);
        //加载更多
        rlCollect.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                loadingMoreList();
            }
        });
    }

    @Click(R.id.btn_login)
    void loginClick() {
        if (isFastClick(1500)) {
            return;
        }
        //神策
        SensorsTrack.login();
        LoginActivity_.intent(mActivity).isGotoLogin(true).startForResult(REQUEST_CODE);
    }

    @OnActivityResult(REQUEST_CODE)
    void onLoginResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setListInit();
        }
    }

    //写字楼/网点
    @Click(R.id.tv_office_building)
    void officeBuildingClick() {
        if (TextUtils.isEmpty(SpUtils.getSignToken())) return;
        pageNum = 1;
        collectOfficeBuildingAdapter = null;
        collectBuildingList.clear();
        workOfficeList.clear();
        isOffice = true;
        setCheckedTextColor(isOffice, tvOfficeBuilding, tvWorkOffice);
        getList(isOffice);
    }

    //办公室
    @Click(R.id.tv_work_office)
    void workOfficeClick() {
        if (TextUtils.isEmpty(SpUtils.getSignToken())) return;
        pageNum = 1;
        workOfficeAdapter = null;
        collectBuildingList.clear();
        workOfficeList.clear();
        isOffice = false;
        setCheckedTextColor(isOffice, tvOfficeBuilding, tvWorkOffice);
        getList(isOffice);
    }

    private void setCheckedTextColor(boolean isBuilding, TextView tvOfficeBuilding, TextView tvWorkOffice) {
        if (isBuilding) {
            tvOfficeBuilding.setTextSize(TypedValue.COMPLEX_UNIT_PX, CommonHelper.sp2px(mActivity, 16));
            tvWorkOffice.setTextSize(TypedValue.COMPLEX_UNIT_PX, CommonHelper.sp2px(mActivity, 12));
            tvOfficeBuilding.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvWorkOffice.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        } else {
            tvOfficeBuilding.setTextSize(TypedValue.COMPLEX_UNIT_PX, CommonHelper.sp2px(mActivity, 12));
            tvWorkOffice.setTextSize(TypedValue.COMPLEX_UNIT_PX, CommonHelper.sp2px(mActivity, 16));
            tvOfficeBuilding.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tvWorkOffice.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
    }

    //下拉刷新
    private void pullDownRefreshList() {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            netException();
            return;
        }
        pageNum = 1;
        collectBuildingList.clear();
        workOfficeList.clear();
        getList(isOffice);
    }

    //加载更多
    private void loadingMoreList() {
        if (NetworkUtils.isNetworkAvailable(mActivity) && hasMore) {
            pageNum++;
            getList(isOffice);
        }
    }

    //开始下拉刷新
    @Override
    public void onRefresh() {
        pullDownRefreshList();
    }

    //刷新完成
    @Override
    public void endRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //网络异常重试
    @Click(R.id.btn_again)
    void netExceptionAgainClick() {
        getList(isOffice);
    }

    @Override
    public void favoriteBuildingListSuccess(CollectBuildingBean data) {
        hasMore = data.getList() != null && data.getList().size() >= 10;
        if (pageNum == 1 && data.getTotal() == 0) {
            noData();
        } else {
            hasData();
        }
        initOfficeBuildingAdapter();
        collectBuildingList.addAll(data.getList());
        collectOfficeBuildingAdapter.notifyDataSetChanged();
    }

    //初始化adapter
    private void initOfficeBuildingAdapter() {
        if (collectOfficeBuildingAdapter == null) {
            collectOfficeBuildingAdapter = new CollectOfficeBuildingAdapter(mActivity, collectBuildingList);
            rlCollect.setAdapter(collectOfficeBuildingAdapter);
        }
    }

    @Override
    public void favoriteBuildingListFail(int code, String msg) {
        netException();
    }

    @Override
    public void favoriteHouseListSuccess(CollectHouseBean data) {
        hasMore = data.getList() != null && data.getList().size() >= 10;
        if (pageNum == 1 && data.getTotal() == 0) {
            noData();
        } else {
            hasData();
        }
        initWorkOfficeAdapter();
        workOfficeList.addAll(data.getList());
        workOfficeAdapter.notifyDataSetChanged();
    }

    //初始化adapter
    private void initWorkOfficeAdapter() {
        if (workOfficeAdapter == null) {
            workOfficeAdapter = new CollectWorkOfficeAdapter(mActivity, workOfficeList);
            rlCollect.setAdapter(workOfficeAdapter);
        }
    }

    @Override
    public void favoriteHouseListFail(int code, String msg) {

    }

    private void noData() {
        tvNoData.setVisibility(View.VISIBLE);
        rlException.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        ctlNoLogin.setVisibility(View.GONE);
    }

    private void hasData() {
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        ctlNoLogin.setVisibility(View.GONE);
    }

    private void netException() {
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        ctlNoLogin.setVisibility(View.GONE);
    }

    private void noLoginView() {
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        ctlNoLogin.setVisibility(View.VISIBLE);
    }

}
