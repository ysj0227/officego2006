package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.analytics.SensorsTrack;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.OnLoadMoreListener;
import com.officego.config.ConditionConfig;
import com.officego.ui.adapter.HouseAdapter;
import com.officego.ui.home.contract.SearchListContract;
import com.officego.ui.home.model.BuildingBean;
import com.officego.ui.home.model.ConditionSearchBean;
import com.officego.ui.home.presenter.SearchListPresenter;
import com.officego.utils.CommonList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions: 搜索列表
 **/
@SuppressLint("Registered,NonConstantResourceId")
@EActivity(R.layout.home_activity_search_house_list)
public class SearchHouseListActivity extends BaseMvpActivity<SearchListPresenter> implements
        SearchListContract.View, SearchPopupWindow.onSureClickListener,
        SwipeRefreshLayout.OnRefreshListener, HouseAdapter.ClickItemListener {
    private final int SEARCH_TYPE0 = 0;
    private final int SEARCH_TYPE1 = 1;
    private final int SEARCH_TYPE2 = 2;
    private final int SEARCH_TYPE3 = 3;
    @ViewById(R.id.btn_back)
    LinearLayout btnBack;
    @ViewById(R.id.et_search)
    ClearableEditText etSearch;
    @ViewById(R.id.btn_cancel)
    Button btnCancel;
    @ViewById(R.id.bga_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.rv_house)
    RecyclerView rvHouse;
    @ViewById(R.id.tv_no_data)
    TextView tvNoData;
    @ViewById(R.id.rl_exception)
    RelativeLayout rlException;
    //搜索-- 区域，写字楼，排序，筛选
    @ViewById(R.id.ctl_search)
    ConstraintLayout ctlSearch;
    @ViewById(R.id.tv_search_area)
    TextView tvSearchArea;
    @ViewById(R.id.tv_search_office)
    TextView tvSearchOffice;
    @ViewById(R.id.tv_search_order)
    TextView tvSearchOrder;
    @ViewById(R.id.tv_search_condition)
    TextView tvSearchCondition;

    @Extra
    String searchKeywords;
    @Extra
    int filterType;
    @Extra
    String brandId;

    private SearchPopupWindow popupWindow;
    //adapter
    private HouseAdapter houseAdapter;
    //首页列表数据
    private final List<BuildingBean.ListBean> buildingList = new ArrayList<>();
    //当前页码
    private int pageNum = 1;
    //list 是否有更多
    private boolean hasMore;
    //所有的筛选条件
    private int btype;
    private HashSet<Integer> hashSet;//地铁商圈的传值
    private SparseBooleanArray checkStates; //记录选中的位置
    private String district = "", business = "", line = "", nearbySubway = "",
            area = "", dayPrice = "", seats = "", decoration = "", houseTags = "", sort = "0";
    private boolean isReviewVR;//是否只预览带VR

    private ConditionSearchBean mSearchData;
    private List<DirectoryBean.DataBean> decorationList;
    private List<DirectoryBean.DataBean> buildingUniqueList;
    private List<DirectoryBean.DataBean> jointWorkUniqueList;
    private List<DirectoryBean.DataBean> brandList;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new SearchListPresenter(context);
        mPresenter.attachView(this);
        initRefresh();
        ConditionConfig.showText(tvSearchOffice, filterType);
        btnBack.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.GONE);
        etSearch.setText(searchKeywords);
        etSearch.setFocusable(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvHouse.setLayoutManager(layoutManager);
        if (!NetworkUtils.isNetworkAvailable(context)) {
            netException();
            return;
        }
        getBuildingList();
    }

    //获取数据列表
    private void getBuildingList() {
        String mArea = TextUtils.isEmpty(area) ? ("0," + CommonList.SEARCH_MAX) : area;
        String mDayPrice = TextUtils.isEmpty(dayPrice) ? ("0," + CommonList.SEARCH_MAX) : dayPrice;
        String mSeats = TextUtils.isEmpty(seats) ? ("0," + CommonList.SEARCH_MAX) : seats;
        String mDecoration = TextUtils.equals("0", decoration) ? "" : decoration;
        mPresenter.getBuildingList(pageNum, filterType, district, business,
                line, nearbySubway, mArea, mDayPrice, mSeats,
                mDecoration, houseTags, sort, brandId, isReviewVR, searchKeywords);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(true, -20, 100);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.common_blue_main_80a, R.color.common_blue_main);
        //加载更多
        rvHouse.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                loadingMoreList();
            }
        });
        //解决当下拉刷新快速滑动crash问题
        rvHouse.setOnTouchListener((view, motionEvent) -> mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing());
    }

    @Click(R.id.btn_back)
    void backClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ConditionConfig.getConditionBean = null;
    }

    @Click(R.id.et_search)
    void searchClick() {
        SearchRecommendActivity_.intent(context).start();
    }

    @Click(R.id.rl_search_area)
    void searchAreaClick() {
        popupWindowSearch(tvSearchArea, SEARCH_TYPE0);
    }

    @Click(R.id.rl_search_office)
    void searchOfficeClick() {
        popupWindowSearch(tvSearchOffice, SEARCH_TYPE1);
    }

    @Click(R.id.rl_search_order)
    void searchOrderClick() {
        popupWindowSearch(tvSearchOrder, SEARCH_TYPE2);
    }

    @Click(R.id.rl_search_condition)
    void searchConditionClick() {
        if (decorationList == null || buildingUniqueList == null ||
                jointWorkUniqueList == null || brandList == null) {
            mPresenter.getConditionList();
        } else {
            popupWindowSearch(tvSearchCondition, SEARCH_TYPE3);
        }
    }

    @Override
    public void conditionListSuccess(List<DirectoryBean.DataBean> decorationList,
                                     List<DirectoryBean.DataBean> buildingUniqueList,
                                     List<DirectoryBean.DataBean> jointWorkUniqueList,
                                     List<DirectoryBean.DataBean> brandList) {
        this.decorationList = decorationList;
        this.buildingUniqueList = buildingUniqueList;
        this.jointWorkUniqueList = jointWorkUniqueList;
        this.brandList = brandList;
        popupWindowSearch(tvSearchCondition, SEARCH_TYPE3);
    }

    private void popupWindowSearch(TextView textView, int searchType) {
        if (popupWindow != null && popupWindow.isShowing()) {
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ContextCompat.getDrawable(context, R.mipmap.ic_arrow_down), null);
            popupWindow.dismiss();
            return;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                ContextCompat.getDrawable(context, R.mipmap.ic_arrow_up_blue), null);
        popupWindow = new SearchPopupWindow(this, ctlSearch, textView, searchType,
                filterType, hashSet, checkStates, district, business, line, nearbySubway, sort,
                decorationList, buildingUniqueList, jointWorkUniqueList, brandList, mSearchData);
        popupWindow.setOnSureClickListener(this);
    }

    //网络异常重试
    @Click(R.id.btn_again)
    void netExceptionAgainClick() {
        buildingList.clear();
        houseAdapter = null;
        getBuildingList();
    }

    @Override
    public void BuildingListSuccess(List<BuildingBean.ListBean> list, boolean hasMore) {
        this.hasMore = hasMore;
        if (list == null || pageNum == 1 && list.size() == 0) {
            noData();
            return;
        }
        hasData();
        initAdapter();
        buildingList.addAll(list);
        houseAdapter.notifyDataSetChanged();
    }

    //初始化adapter
    private void initAdapter() {
        if (houseAdapter == null) {
            houseAdapter = new HouseAdapter(context, buildingList, ConditionConfig.getConditionBean);
            rvHouse.setAdapter(houseAdapter);
        }
        houseAdapter.setItemListener(this);
    }

    /**
     * popupWindow 搜索的类型
     */
    @Override
    public void onSurePopUpWindow(boolean isLine, HashSet<Integer> hashSet,
                                  SparseBooleanArray checkStates, String data1, String data2) {
        if (TextUtils.isEmpty(data1) && TextUtils.isEmpty(data2)) {
            tvSearchArea.setText("区域");
            tvSearchArea.setTextColor(ContextCompat.getColor(context, R.color.text_66));
        } else {
            tvSearchArea.setText(isLine ? "地铁" : "商圈");
            tvSearchArea.setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
        }
        this.hashSet = hashSet;
        this.checkStates = checkStates;
        if (isLine) {
            line = data1;
            nearbySubway = data2;
            district = "";
            business = "";
        } else {
            district = data1;
            business = data2;
            line = "";
            nearbySubway = "";
        }
        //查询列表
        getList();
    }

    //全部，写字楼，共享办公
    @Override
    public void onOfficeTypePopUpWindow(int filterType, int text) {
        this.filterType = filterType;
        tvSearchOffice.setText(text);
        //初始化选择的写字楼或共享办公
        area = "";
        seats = "";
        dayPrice = "";
        decoration = "";
        houseTags = "";
        bType(filterType);
        //查询列表
        getList();
    }

    //排序
    @Override
    public void onOfficeOrderPopUpWindow(String order) {
        sort = order;
        //查询列表
        getList();
    }

    //筛选
    @Override
    public void onConditionPopUpWindow(int filterType, ConditionSearchBean bean) {
        mSearchData = bean;
        this.filterType = filterType;
        this.area = bean.getArea();
        this.dayPrice = bean.getRent();
        this.seats = bean.getSeats();
        this.decoration = bean.getDecoration();
        this.houseTags = bean.getUnique();
        this.isReviewVR = bean.isVr();
        this.brandId = bean.getBrand();
        bType(filterType);
        ConditionConfig.showText(tvSearchOffice, filterType);
        ConditionConfig.getConditionBean = ConditionConfig.setConditionBean(filterType,
                btype, area, dayPrice, seats, decoration, houseTags);
        //查询列表
        getList();
    }

    //TODO
    private void bType(int filterType) {
        if (filterType == Constants.SEARCH_ALL) {
            btype = 0;
        } else if (filterType == Constants.SEARCH_JOINT_WORK || filterType == Constants.SEARCH_OPEN_SEATS) {
            btype = Constants.TYPE_JOINTWORK;
        } else if (filterType == Constants.SEARCH_OFFICE || filterType == Constants.SEARCH_GARDEN) {
            btype = Constants.TYPE_BUILDING;
        }
    }

    private void getList() {
        pageNum = 1;
        buildingList.clear();
        houseAdapter = null;
        getBuildingList();
    }

    //初始化下拉刷新
    private void pullDownRefreshList() {
        pageNum = 1;
        buildingList.clear();
        getBuildingList();
    }

    //加载更多
    private void loadingMoreList() {
        if (NetworkUtils.isNetworkAvailable(context) && hasMore) {
            pageNum++;
            getBuildingList();
        }
    }

    //刷新完成
    @Override
    public void endRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        pullDownRefreshList();
    }

    private void noData() {
        tvNoData.setVisibility(View.VISIBLE);
        rlException.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
    }

    private void hasData() {
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    private void netException() {
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void listItemClick(int position, int buildingId, int btype) {
        //神策
        SensorsTrack.clickSearchResultsPage(searchKeywords, btype, position, buildingId);
        SensorsTrack.visitBuildingDataPage(position, buildingId);
    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.sendKeyWords};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (id == CommonNotifications.sendKeyWords) {
            searchKeywords = (String) args[0];
            etSearch.setText(searchKeywords);
            getList();
        }
    }
}
