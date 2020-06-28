package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.officego.R;
import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.ui.adapter.HouseAdapter;
import com.officego.ui.home.contract.HomeContract;
import com.officego.ui.home.model.BuildingBean;
import com.officego.ui.home.model.ConditionBean;
import com.officego.ui.home.presenter.HomePresenter;
import com.officego.utils.AppBarStateChangeListener;
import com.officego.utils.ImageLoaderUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@EFragment(R.layout.home_fragment)
public class HomeFragment1 extends BaseMvpFragment<HomePresenter> implements
        HomeContract.View, OnBannerListener,
        SearchPopupWindow.onSureClickListener,
        BGARefreshLayout.BGARefreshLayoutDelegate {
    @ViewById(R.id.cdl_root)
    CoordinatorLayout cdlRoot;
    @ViewById(R.id.bga_refresh)
    BGARefreshLayout refreshLayout;
    @ViewById(R.id.rv_house)
    RecyclerView rvHouse;
    @ViewById(R.id.banner)
    Banner banner;
    @ViewById(R.id.v_background_blue)
    View vBackgroundBlue;
    @ViewById(R.id.app_bar)
    AppBarLayout appBarLayout;
    @ViewById(R.id.ctl_inside_bar)
    ConstraintLayout ctlInsideBar;
    //暂无数据，网络异常
    @ViewById(R.id.tv_no_data)
    TextView tvNoData;
    @ViewById(R.id.rl_exception)
    RelativeLayout rlException;
    //推荐,附近列表
    @ViewById(R.id.ctl_search)
    ConstraintLayout ctlSearch;
    @ViewById(R.id.btn_recommend)
    TextView btnRecommend;
    @ViewById(R.id.btn_nearby)
    TextView btnNearby;
    @ViewById(R.id.rl_ibtn_search)
    RelativeLayout rlIbtnSearch;
    //搜索-- 区域，写字楼，排序，筛选
    @ViewById(R.id.tv_search_area)
    TextView tvSearchArea;
    @ViewById(R.id.tv_search_office)
    TextView tvSearchOffice;
    @ViewById(R.id.tv_search_order)
    TextView tvSearchOrder;
    @ViewById(R.id.tv_search_condition)
    TextView tvSearchCondition;
    //搜索筛选
    private SearchPopupWindow popupWindow;
    //adapter
    private HouseAdapter houseAdapter;
    //首页列表数据
    private List<BuildingBean.ListBean> buildingList = new ArrayList<>();
    //当前页码
    private int pageNum = 1;
    //list 是否有更多
    private boolean hasMore;
    //所有的筛选条件
    private int btype = 0;
    private HashSet<Integer> hashSet;//地铁商圈的传值
    private SparseBooleanArray checkStates; //记录选中的位置
    private String district = "", business = "", line = "", nearbySubway = "",
            area = "", dayPrice = "", seats = "", decoration = "", houseTags = "", sort = "0";
    private int currentScrollPosition;

    private float alphaPercent;//渐变色百分比

    @SuppressLint("NewApi")
    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        mPresenter = new HomePresenter(mActivity);
        mPresenter.attachView(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvHouse.setLayoutManager(layoutManager);
        appBarLayout.addOnOffsetChangedListener(appBarStateChangeListener);
        alphaPercent = (float) 1 / CommonHelper.dp2px(mActivity, 300);
        initBarLayoutBg();
        initRefresh();
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            netException();
            return;
        }
        //轮播图
        mPresenter.getBannerList();
        //列表
        houseAdapter = null;
        getBuildingList();
    }

    /**
     * 列表
     * district 	否 	string 	大区
     * business 	否 	string 	商圈 不限：0 多个英文逗号分隔Id
     * nearbySubway 	否 	string 	地铁站名 ，不限：0 多个英文逗号分隔Id
     * line 	否 	string 	地铁线
     * area 	否 	string 	平方米区间英文逗号分隔
     * dayPrice 	否 	string 	楼盘的时候是 每平方米单价区间英文逗号分隔 网点的时候是 每工位每月单价区间英文逗号分隔
     * decoration 	否 	string 	装修类型id英文逗号分隔
     * btype 	否 	string 	类型1:楼盘,2:网点, 0全部
     * houseTags 	否 	string 	房源特色id英文逗号分隔
     * vrFlag 	否 	int 	是否只看VR房源 0:不限1:只看VR房源
     * sort 	否 	int 	排序0默认1价格从高到低2价格从低到高3面积从大到小4面积从小到大
     * seats 	否 	string 	联合工位区间英文逗号分隔
     * longitude 	否 	string 	经度
     * latitude 	否 	string 	纬度
     * keyWord 	否 	string 	关键字搜索
     * pageNo 	否 	int 	当前页
     * pageSize 	否 	int 	每页条数
     */
    private void getBuildingList() {
        mPresenter.getBuildingList(pageNum, String.valueOf(btype), district, business,
                line, nearbySubway, area, dayPrice, seats,
                decoration, houseTags, sort, "");
    }

    private void initRefresh() {
        refreshLayout.setDelegate(this);
        BGARefreshViewHolder viewHolder = new BGANormalRefreshViewHolder(mActivity, true);
        viewHolder.setLoadingMoreText(getString(R.string.str_loding_more));
        viewHolder.setLoadMoreBackgroundColorRes(R.color.common_bg);
        refreshLayout.setRefreshViewHolder(viewHolder);
    }

    private void initBarLayoutBg() {
        hasData();
        if (currentScrollPosition == 0) {
            appBarLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.common_blue_main));
            btnRecommend.setTextColor(ContextCompat.getColor(mActivity, R.color.text_main));
            btnNearby.setTextColor(ContextCompat.getColor(mActivity, R.color.text_main));
            ctlInsideBar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
            rlIbtnSearch.setVisibility(View.GONE);
            //结合状态栏布局滑动顶部背景变化
            vBackgroundBlue.setVisibility(View.GONE);
            refreshLayout.setPullDownRefreshEnable(true);
        } else if (currentScrollPosition == 1) {
            appBarLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
            btnRecommend.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
            btnNearby.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
            ctlInsideBar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.common_blue_main));
            ctlSearch.setVisibility(View.VISIBLE);
            rlIbtnSearch.setVisibility(View.VISIBLE);
            //结合状态栏布局滑动顶部背景变化
            vBackgroundBlue.setVisibility(View.VISIBLE);
            refreshLayout.setPullDownRefreshEnable(false);
        } else {
            appBarLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.common_blue_main));
            btnRecommend.setTextColor(ContextCompat.getColor(mActivity, R.color.text_main));
            btnNearby.setTextColor(ContextCompat.getColor(mActivity, R.color.text_main));
            ctlInsideBar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
            rlIbtnSearch.setVisibility(View.GONE);
            //结合状态栏布局滑动顶部背景变化
            vBackgroundBlue.setVisibility(View.GONE);
            refreshLayout.setPullDownRefreshEnable(false);
        }
    }

    @Override
    public void OnBannerClick(int position) {

    }

    //推荐房源
    @Click(R.id.btn_recommend)
    void recommendClick() {
        btnRecommend.setTextSize(TypedValue.COMPLEX_UNIT_PX, CommonHelper.sp2px(mActivity, 16));
        btnNearby.setTextSize(TypedValue.COMPLEX_UNIT_PX, CommonHelper.sp2px(mActivity, 12));
        btnRecommend.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        btnNearby.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    //附近房源
    @Click(R.id.btn_nearby)
    void nearbyClick() {
        btnRecommend.setTextSize(TypedValue.COMPLEX_UNIT_PX, CommonHelper.sp2px(mActivity, 12));
        btnNearby.setTextSize(TypedValue.COMPLEX_UNIT_PX, CommonHelper.sp2px(mActivity, 16));
        btnRecommend.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        btnNearby.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    //搜索
    @Click(R.id.tv_search)
    void searchClick() {
        gotoSearchActivity();
    }

    //搜索
    @Click(R.id.rl_ibtn_search)
    void btnSearchClick() {
        gotoSearchActivity();
    }

    private void gotoSearchActivity() {
        if (isFastClick(1200)) {
            return;
        }
        SearchRecommendActivity_.intent(mActivity).start();
    }

    @Click(R.id.tv_search_area)
    void searchAreaClick() {
        popupWindowSearch(tvSearchArea, 0);
    }

    @Click(R.id.tv_search_office)
    void searchOfficeClick() {
        popupWindowSearch(tvSearchOffice, 1);
    }

    @Click(R.id.tv_search_order)
    void searchOrderClick() {
        popupWindowSearch(tvSearchOrder, 2);
    }

    @Click(R.id.tv_search_condition)
    void searchConditionClick() {
        popupWindowSearch(tvSearchCondition, 3);
    }

    private void popupWindowSearch(TextView textView, int searchType) {
        if (popupWindow != null && popupWindow.isShowing()) {
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ContextCompat.getDrawable(mActivity, R.mipmap.ic_arrow_down), null);
            popupWindow.dismiss();
            return;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                ContextCompat.getDrawable(mActivity, R.mipmap.ic_arrow_up_blue), null);
        popupWindow = new SearchPopupWindow(mActivity, ctlSearch, textView, searchType,
                btype, hashSet, checkStates, district, business,
                line, nearbySubway, area, dayPrice, seats,
                decoration, houseTags, sort);
        popupWindow.setOnSureClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private AppBarStateChangeListener appBarStateChangeListener = new AppBarStateChangeListener() {
        @Override
        public void onStateChanged(AppBarLayout appBarLayout, State state, int offset) {
            if (state == State.EXPANDED) {
                //展开状态
                currentScrollPosition = 0;
//                StatusBarUtils.setStatusBarColor(mActivity);
                appBarLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.common_blue_main));
                btnRecommend.setTextColor(ContextCompat.getColor(mActivity, R.color.text_main));
                btnNearby.setTextColor(ContextCompat.getColor(mActivity, R.color.text_main));
                ctlInsideBar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
                ctlSearch.setVisibility(appBarLayout.getTotalScrollRange() - offset < 150 ? View.VISIBLE : View.GONE);
                rlIbtnSearch.setVisibility(View.GONE);
                //结合状态栏布局滑动顶部背景变化
                vBackgroundBlue.setVisibility(View.GONE);
                refreshLayout.setPullDownRefreshEnable(true);
            } else if (state == State.COLLAPSED) {
                //折叠状态
                currentScrollPosition = 1;
                StatusBarUtils.setStatusBarFullTransparent(mActivity);
                appBarLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
                btnRecommend.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                btnNearby.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                ctlInsideBar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.common_blue_main));
                ctlSearch.setVisibility(View.VISIBLE);
                rlIbtnSearch.setVisibility(View.VISIBLE);
                //结合状态栏布局滑动顶部背景变化
                vBackgroundBlue.setVisibility(View.VISIBLE);
                refreshLayout.setPullDownRefreshEnable(false);
            } else {
                //中间状态
//                StatusBarUtils.setStatusBarColor(mActivity);
                currentScrollPosition = 2;
                appBarLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.common_blue_main));
                btnRecommend.setTextColor(ContextCompat.getColor(mActivity, R.color.text_main));
                btnNearby.setTextColor(ContextCompat.getColor(mActivity, R.color.text_main));
                ctlInsideBar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
                ctlSearch.setVisibility(appBarLayout.getTotalScrollRange() - offset < 150 ? View.VISIBLE : View.GONE);
                rlIbtnSearch.setVisibility(View.GONE);
                //结合状态栏布局滑动顶部背景变化
                vBackgroundBlue.setVisibility(View.GONE);
                refreshLayout.setPullDownRefreshEnable(false);
            }
        }

        @Override
        public void onStateAbs(int abs) {
            LogCat.e("TAG", "1111 " + alphaPercent + " abs=" + abs);
            banner.setAlpha(1 - abs * alphaPercent);
        }
    };

    private void playBanner(List<String> bannerList) {
        if (bannerList == null || bannerList.size() == 0) {
            return;
        }
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        //  banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new ImageLoaderUtils(mActivity));
        //设置图片网址或地址的集合
        banner.setImages(bannerList);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                .setOnBannerListener(this)
                .start();
    }

    @Override
    public void bannerListSuccess(List<String> bannerList) {
        playBanner(bannerList);
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
        //pageNum == 1首次请求且size==0
        if (list == null || pageNum == 1 && list.size() == 0) {
            noData();
            return;
        }
        hasData();
        this.hasMore = hasMore;
        initAdapter();
        buildingList.addAll(list);
        houseAdapter.notifyDataSetChanged();
    }

    @Override
    public void BuildingListFail(int code, String msg) {
        netException();
    }

    //初始化adapter
    private void initAdapter() {
        if (houseAdapter == null) {
            houseAdapter = new HouseAdapter(mActivity, buildingList, setConditionBean());
            rvHouse.setAdapter(houseAdapter);
        }
    }

    /**
     * popupWindow 搜索的类型
     */
    @Override
    public void onSurePopUpWindow(boolean isLine, HashSet<Integer> hashSet,
                                  SparseBooleanArray checkStates, String data1, String data2) {
        LogCat.e("TAG", "onSurePopUpWindow  isLine=" + isLine + " data1=" + data1 + " data2=" + data2);
        if (TextUtils.isEmpty(data1) && TextUtils.isEmpty(data2)) {
            tvSearchArea.setText("区域");
            tvSearchArea.setTextColor(ContextCompat.getColor(mActivity, R.color.text_66));
        } else {
            tvSearchArea.setText(isLine ? "地铁" : "商圈");
            tvSearchArea.setTextColor(ContextCompat.getColor(mActivity, R.color.common_blue_main));
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
        buildingList.clear();
        houseAdapter = null;
        getBuildingList();
    }

    //全部，写字楼，联合办公
    @Override
    public void onOfficeTypePopUpWindow(int searchType, int officeType, int text) {
        btype = officeType;
        tvSearchOffice.setText(text);
        //查询列表
        buildingList.clear();
        houseAdapter = null;
        getBuildingList();
    }

    //排序
    @Override
    public void onOfficeOrderPopUpWindow(int searchType, String order) {
        sort = order;
        //查询列表
        buildingList.clear();
        houseAdapter = null;
        getBuildingList();
    }

    @Override
    public void onConditionPopUpWindow(int searchType, int btype, String constructionArea,
                                       String rentPrice, String simple, String decoration, String tags) {
        LogCat.e("TAG", "onConditionPopUpWindow btype= " + btype + " constructionArea=" + constructionArea +
                " rentPrice=" + rentPrice + " simple=" + simple + " decoration=" + decoration + " tags=" + tags);
        this.btype = btype;
        this.area = constructionArea;
        this.dayPrice = rentPrice;
        this.seats = simple;
        this.decoration = decoration;
        this.houseTags = tags;
        if (btype == 0) {
            tvSearchOffice.setText(R.string.str_house_all);
        } else if (btype == 1) {
            tvSearchOffice.setText(R.string.str_house_office);
        } else if (btype == 2) {
            tvSearchOffice.setText(R.string.str_house_tenant);
        }
        setConditionBean();
        //查询列表
        buildingList.clear();
        getBuildingList();
    }

    //初始化刷新
    private void pullDownRefreshList() {
        pageNum = 1;
        buildingList.clear();
        houseAdapter = null;
        getBuildingList();
    }

    @Override
    public void endRefresh() {
        refreshLayout.endLoadingMore();
        refreshLayout.endRefreshing();
    }

    //刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        pullDownRefreshList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (NetworkUtils.isNetworkAvailable(mActivity) && hasMore) {
            pageNum++;
            getBuildingList();
            return true;
        }
        return false;
    }

    private ConditionBean setConditionBean() {
        ConditionBean bean = new ConditionBean();
        //面积
        if (TextUtils.equals("", this.area) || TextUtils.equals("0,1000", this.area)) {
            this.area = "";
        } else {
            String start, end;
            if (this.area.contains(",")) {
                String str1 = this.area.substring(0, this.area.indexOf(","));
                start = this.area.substring(0, str1.length());
                end = this.area.substring(str1.length() + 1);
                bean.setAreaValue(start + "-" + end + "㎡");
            }
        }
        if (btype == 1) {//楼盘，办公室
            if (TextUtils.equals("", this.seats) || TextUtils.equals("0,500", this.seats)) {
                this.seats = "";
            } else {
                bean.setSeatsValue(setSeatsValue());
            }
        } else if (btype == 2) {//网点 没有面积条件
            if (TextUtils.equals("", this.seats) || TextUtils.equals("0,20", this.seats)) {
                this.seats = "";
                this.area = "";
            } else {
                bean.setSeatsValue(setSeatsValue());
            }
        }
        bean.setArea(this.area);
        bean.setSeats(this.seats);
        bean.setDayPrice(this.dayPrice);
        bean.setDecoration(this.decoration);
        bean.setHouseTags(this.houseTags);
        return bean;
    }

    private String setSeatsValue() {
        String start, end;
        if (this.seats.contains(",")) {//工位
            String str1 = this.seats.substring(0, this.seats.indexOf(","));
            start = this.seats.substring(0, str1.length());
            end = this.seats.substring(str1.length() + 1);
            return start + "-" + end + "人";
        }
        return "";
    }

    private void noData() {
        tvNoData.setVisibility(View.VISIBLE);
        rlException.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
    }

    private void hasData() {
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.VISIBLE);
    }

    private void netException() {
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.GONE);
    }
}
