package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.officego.R;
import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.update.VersionDialog;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.OnLoadMoreListener;
import com.officego.config.ConditionConfig;
import com.officego.h5.WebViewBannerActivity_;
import com.officego.h5.WebViewCouponActivity_;
import com.officego.ui.adapter.HouseAdapter;
import com.officego.ui.home.contract.HomeContract;
import com.officego.ui.home.model.BannerBean;
import com.officego.ui.home.model.BuildingBean;
import com.officego.ui.home.model.ConditionBean;
import com.officego.ui.home.presenter.HomePresenter;
import com.officego.utils.AppBarStateChangeListener;
import com.officego.utils.ImageLoaderUtils;
import com.officego.utils.SuperSwipeRefreshLayout;
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
import java.util.Map;

import static com.officego.config.ConditionConfig.mConditionBean;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@SuppressLint("NewApi")
@EFragment(R.layout.home_fragment)
public class HomeFragment extends BaseMvpFragment<HomePresenter> implements
        HomeContract.View, OnBannerListener,
        SearchPopupWindow.onSureClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        HouseAdapter.ClickItemListener {
    @ViewById(R.id.cdl_root)
    CoordinatorLayout cdlRoot;
    @ViewById(R.id.bga_refresh)
    SuperSwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.rv_house)
    RecyclerView rvHouse;
    @ViewById(R.id.banner)
    Banner banner;
    @ViewById(R.id.app_bar)
    AppBarLayout appBarLayout;
    //暂无数据，网络异常
    @ViewById(R.id.tv_no_data)
    TextView tvNoData;
    @ViewById(R.id.rl_exception)
    RelativeLayout rlException;
    //推荐,附近列表
    @ViewById(R.id.ctl_search)
    ConstraintLayout ctlSearch;
    @ViewById(R.id.rl_ibtn_search)
    RelativeLayout rlIbtnSearch;
    @ViewById(R.id.rl_home_title)
    RelativeLayout rlHomeTitle;
    //搜索 区域，写字楼，排序，筛选
    @ViewById(R.id.rl_search_area)
    RelativeLayout rlSearchArea;
    @ViewById(R.id.rl_search_office)
    RelativeLayout rlSearchOffice;
    @ViewById(R.id.rl_search_order)
    RelativeLayout rlSearchOrder;
    @ViewById(R.id.rl_search_condition)
    RelativeLayout rlSearchCondition;
    @ViewById(R.id.tv_search_area)
    TextView tvSearchArea;
    @ViewById(R.id.tv_search_office)
    TextView tvSearchOffice;
    @ViewById(R.id.tv_search_order)
    TextView tvSearchOrder;
    @ViewById(R.id.tv_search_condition)
    TextView tvSearchCondition;
    //选项标签的条件
    @ViewById(R.id.rl_label_construction)
    RelativeLayout rlLabelConstruction;
    @ViewById(R.id.rl_construction)
    RelativeLayout rlConstruction;
    @ViewById(R.id.tv_construction)
    TextView tvConstruction;
    @ViewById(R.id.rl_office_type)
    RelativeLayout rlOfficeType;
    @ViewById(R.id.tv_office_type)
    TextView tvOfficeType;
    @ViewById(R.id.rl_sel_condition)
    RelativeLayout rlSelCondition;
    @ViewById(R.id.tv_sel_condition)
    TextView tvSelCondition;
    @ViewById(R.id.rl_label_construction1)
    RelativeLayout rlLabelConstruction1;
    @ViewById(R.id.rl_construction1)
    RelativeLayout rlConstruction1;
    @ViewById(R.id.tv_construction1)
    TextView tvConstruction1;
    @ViewById(R.id.rl_office_type1)
    RelativeLayout rlOfficeType1;
    @ViewById(R.id.tv_office_type1)
    TextView tvOfficeType1;
    @ViewById(R.id.rl_sel_condition1)
    RelativeLayout rlSelCondition1;
    @ViewById(R.id.tv_sel_condition1)
    TextView tvSelCondition1;
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
    private Map<Integer, String> mapDecoration;//装修类型
    private HashSet<Integer> hashSet;//地铁商圈的传值
    private SparseBooleanArray checkStates; //记录选中的位置
    private String district = "", business = "", line = "", nearbySubway = "",
            area = "", dayPrice = "", seats = "", decoration = "", houseTags = "", sort = "0";

    private float alphaPercent;//渐变色百分比
    private List<BannerBean.DataBean> mBannerClickList = new ArrayList<>();

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarMainColor(mActivity, ContextCompat.getColor(mActivity, R.color.common_blue_main));
        mPresenter = new HomePresenter(mActivity);
        mPresenter.attachView(this);
        CommonHelper.setRelativeLayoutParams(mActivity, rlHomeTitle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvHouse.setLayoutManager(layoutManager);
        appBarLayout.addOnOffsetChangedListener(appBarStateChangeListener);
        alphaPercent = (float) 1 / CommonHelper.dp2px(mActivity, 190);
        ConditionConfig.mConditionBean = null;
        initBarLayoutBg();
        initRefresh();
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            netException();
            return;
        }
        //版本更新
        new VersionDialog(mActivity);
        //轮播图
        mPresenter.getBannerList();
        //列表
        getList();
        //筛选条件
        labelsConditionView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBarUtils.setStatusBarMainColor(mActivity, ContextCompat.getColor(mActivity, R.color.common_blue_main));
            initBarLayoutBg();
        }
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
        String mArea = "", mDayPrice = "", mSeats = "";
        if (btype == Constants.TYPE_BUILDING) {
            if (TextUtils.equals("", area) || TextUtils.equals("0,2000", area)) {
                mArea = "0,999999999";
            } else {
                mArea = area;
            }
            if (TextUtils.equals("", dayPrice) || TextUtils.equals("0,50", dayPrice)) {
                mDayPrice = "0,999999999";
            } else {
                mDayPrice = dayPrice;
            }
        } else if (btype == Constants.TYPE_JOINTWORK) {
            if (TextUtils.equals("", dayPrice) || TextUtils.equals("0,50000", dayPrice)) {
                mDayPrice = "0,999999999";
            } else {
                mDayPrice = dayPrice;
            }
            if (TextUtils.equals("", seats) || TextUtils.equals("0,30", seats)) {
                mSeats = "0,999999999";
            } else {
                mSeats = seats;
            }
        }
        mPresenter.getBuildingList(mActivity,pageNum, String.valueOf(btype), district, business,
                line, nearbySubway, mArea, mDayPrice, mSeats,
                decoration, houseTags, sort, "");
        //神策
        String areaType = tvSearchArea.getText().toString();
        boolean isSelect;
        isSelect = !TextUtils.equals("区域", areaType) || btype != 0 ||
                !TextUtils.equals("0", sort) ||
                !TextUtils.isEmpty(decoration) ||
                !TextUtils.isEmpty(mArea) ||
                !TextUtils.isEmpty(mDayPrice) ||
                !TextUtils.isEmpty(mSeats);

        SensorsTrack.visitBuildingNetworkList("上海市", areaType, Constants.SENSORS_AREA_CONTENT, btype,
                Integer.valueOf(sort), mArea, mDayPrice, mSeats, Constants.SENSORS_DECORATION, false, isSelect);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(true, -20, 160);
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

    private void initBarLayoutBg() {
        hasData();
    }

    //下拉刷新
    private void pullDownRefreshList() {
        pageNum = 1;
        buildingList.clear();
        houseAdapter = null;
        getBuildingList();
    }

    //加载更多
    private void loadingMoreList() {
        if (NetworkUtils.isNetworkAvailable(mActivity) && hasMore) {
            pageNum++;
            getBuildingList();
        }
    }

    //开始下拉刷新
    @Override
    public void onRefresh() {
        //轮播图
        mPresenter.getBannerList();
        pullDownRefreshList();
    }

    //刷新完成
    @Override
    public void endRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //搜索
    @Click(R.id.tv_search)
    void searchClick() {
        SensorsTrack.searchButtonIndex();
        gotoSearchActivity();
    }

    //搜索
    @Click({R.id.rl_ibtn_search, R.id.ctl_inside_bar})
    void btnSearchClick() {
        SensorsTrack.searchButtonIndex();
        gotoSearchActivity();
    }

    private void gotoSearchActivity() {
        if (isFastClick(1200)) {
            return;
        }
        SearchRecommendActivity_.intent(mActivity).start();
    }

    @Click(R.id.rl_search_area)
    void searchAreaClick() {
        popupWindowSearch(tvSearchArea, 0);
    }

    @Click(R.id.rl_search_office)
    void searchOfficeClick() {
        popupWindowSearch(tvSearchOffice, 1);
    }

    @Click(R.id.rl_search_order)
    void searchOrderClick() {
        popupWindowSearch(tvSearchOrder, 2);
    }

    @Click(R.id.rl_search_condition)
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
                decoration, houseTags, sort, mapDecoration);
        popupWindow.setOnSureClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //标签筛选条件
    private void labelConstruction(int abs) {
        if (abs * alphaPercent <= 1) {
            rlLabelConstruction.setAlpha(0);
            rlLabelConstruction1.setAlpha(1);
        } else {
            rlLabelConstruction.setAlpha(1);
            rlLabelConstruction1.setAlpha(0);
        }
    }

    /**
     * 滑动位置
     */
    private AppBarStateChangeListener appBarStateChangeListener = new AppBarStateChangeListener() {
        @Override
        public void onStateChanged(AppBarLayout appBarLayout, State state, int offset) {
        }

        @Override
        public void onStateAbs(int abs) {
            //title滑动透明度
            rlHomeTitle.setAlpha(abs * alphaPercent);
            //筛选点击
            //筛选是否可点击
            boolean isSearchClick = abs * alphaPercent >= 0.9;
            rlSearchArea.setEnabled(isSearchClick);
            rlSearchOffice.setEnabled(isSearchClick);
            rlSearchOrder.setEnabled(isSearchClick);
            rlSearchCondition.setEnabled(isSearchClick);
            //标签筛选
            labelConstruction(abs);
            //是否可以下拉刷新
            mSwipeRefreshLayout.setEnabled(abs == 0);
        }
    };

    //轮播图
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
    public void bannerListSuccess(List<String> bannerList, List<BannerBean.DataBean> data) {
        mBannerClickList.clear();
        mBannerClickList.addAll(data);
        playBanner(bannerList);
    }

    /**
     * "type": 0,//类型:0不可跳转,1内链 2:富文本 3外链
     * "pageType": 1,//内链类型，1：楼盘详情，2:网点详情 3:楼盘房源详情,4:网点房源详情 0
     * "pageId": null,//内链类型的id
     * "wurl": "",//外链跳转url
     */
    @Override
    public void OnBannerClick(int position) {
        if (mBannerClickList != null) {
            int type = mBannerClickList.get(position).getType();
            int pageType = mBannerClickList.get(position).getPageType() == null ? 0 :
                    Integer.valueOf(CommonHelper.bigDecimal(mBannerClickList.get(position).getPageType(), true));
            int pageId = mBannerClickList.get(position).getPageId() == null ? 0 :
                    Integer.valueOf(CommonHelper.bigDecimal(mBannerClickList.get(position).getPageId(), true));
            if (type == 1) {
                //pageType内链类型1：楼盘详情，2:网点详情 3:楼盘房源详情,4:网点房源详情 5会议室
                if (pageType == 5) {//会议室
                    WebViewCouponActivity_.intent(mActivity).amountRange("").start();
                }
                if (pageId != 0) {
                    if (pageType == 1) {
                        BuildingDetailsActivity_.intent(mActivity)
                                .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_BUILDING, pageId)).start();
                    } else if (pageType == 2) {
                        BuildingDetailsJointWorkActivity_.intent(mActivity)
                                .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_JOINTWORK, pageId)).start();
                    } else if (pageType == 3) {
                        BuildingDetailsChildActivity_.intent(mActivity)
                                .mChildHouseBean(BundleUtils.houseMessage(Constants.TYPE_BUILDING, pageId)).start();
                    } else if (pageType == 4) {
                        BuildingDetailsJointWorkChildActivity_.intent(mActivity)
                                .mChildHouseBean(BundleUtils.houseMessage(Constants.TYPE_JOINTWORK, pageId)).start();
                    }
                }
            } else if (type == 3) {
                //外链跳转
                String wUrl = mBannerClickList.get(position).getWurl();
                WebViewBannerActivity_.intent(getContext()).url(wUrl).start();
            }
        }
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
        //筛选标签
        labelsConditionView();
        //查询列表
        getList();
    }

    //全部，写字楼，共享办公
    @Override
    public void onOfficeTypePopUpWindow(int searchType, int officeType, int text) {
        btype = officeType;
        tvSearchOffice.setText(text);
        //初始化选择的写字楼或共享办公
        area = "";
        seats = "";
        dayPrice = "";
        decoration = "";
        houseTags = "";
        //筛选标签
        labelsConditionView();
        //查询列表
        getList();
    }

    //排序
    @Override
    public void onOfficeOrderPopUpWindow(int searchType, String order) {
        sort = order;
        getList();
    }

    @Override
    public void onConditionPopUpWindow(int searchType, int btype, String constructionArea,
                                       String rentPrice, String simple, String decoration, String tags, Map<Integer, String> mapDecoration) {
        this.btype = btype;
        this.area = constructionArea;
        this.dayPrice = rentPrice;
        this.seats = simple;
        this.decoration = decoration;
        this.houseTags = tags;
        this.mapDecoration = mapDecoration;
        mConditionBean = setConditionBean();
        if (btype == 0) {
            tvSearchOffice.setText(R.string.str_house_all);
        } else if (btype == 1) {
            tvSearchOffice.setText(R.string.str_house_office);
        } else if (btype == 2) {
            tvSearchOffice.setText(R.string.str_house_tenant);
        }
        //筛选标签
        labelsConditionView();
        getList();
    }

    private void getList() {
        pageNum = 1;
        buildingList.clear();
        houseAdapter = null;
        getBuildingList();
    }

    private ConditionBean setConditionBean() {
        ConditionBean bean = new ConditionBean();
        //面积
        if (TextUtils.equals("", this.area) || TextUtils.equals("0,2000", this.area)) {
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
            if (TextUtils.equals("", this.seats) || TextUtils.equals("0,30", this.seats)) {
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
        //刷新列表
        buildingList.clear();
        if (houseAdapter == null) {
            houseAdapter = new HouseAdapter(mActivity, buildingList, setConditionBean());
            rvHouse.setAdapter(houseAdapter);
        }
        houseAdapter.notifyDataSetChanged();
    }

    private void hasData() {
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.GONE);
        rvHouse.setVisibility(View.VISIBLE);
    }

    private void netException() {
        tvNoData.setVisibility(View.GONE);
        rlException.setVisibility(View.VISIBLE);
        rvHouse.setVisibility(View.GONE);
        labelsConditionView();
    }

    /**
     * 筛选条件***************************************************************
     */
    @SuppressLint("SetTextI18n")
    private void labelsConditionView() {
        //设置标签栏不可点击
        rlLabelConstruction.setOnClickListener(null);
        rlLabelConstruction1.setOnClickListener(null);
        //交通
        if (TextUtils.isEmpty(line) && TextUtils.isEmpty(district)) {
            rlConstruction.setVisibility(View.GONE);
            rlConstruction1.setVisibility(View.GONE);
        } else {
            rlConstruction.setVisibility(View.VISIBLE);
            rlConstruction1.setVisibility(View.VISIBLE);
            if (checkStates == null || checkStates.size() == 0) {
                tvConstruction.setText(!TextUtils.isEmpty(line) ? "地铁" : "商圈 ");
                tvConstruction1.setText(!TextUtils.isEmpty(line) ? "地铁" : "商圈");
            } else {
                tvConstruction.setText((!TextUtils.isEmpty(line) ? "地铁" : "商圈") + "(" + checkStates.size() + ")");
                tvConstruction1.setText((!TextUtils.isEmpty(line) ? "地铁" : "商圈") + "(" + checkStates.size() + ")");
            }
        }
        //楼盘类型
        if (btype == 1 || btype == 2) {
            rlOfficeType.setVisibility(View.VISIBLE);
            rlOfficeType1.setVisibility(View.VISIBLE);
            tvOfficeType.setText(btype == 1 ? R.string.str_house_office : R.string.str_house_tenant);
            tvOfficeType1.setText(btype == 1 ? R.string.str_house_office : R.string.str_house_tenant);
        } else {
            rlOfficeType.setVisibility(View.GONE);
            rlOfficeType1.setVisibility(View.GONE);
        }
        //面积-工位
        if (mConditionBean != null && (!TextUtils.isEmpty(area) || !TextUtils.isEmpty(seats))) {
            rlSelCondition.setVisibility(View.VISIBLE);
            rlSelCondition1.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mConditionBean.getArea())) {
                tvSelCondition.setText(mConditionBean.getAreaValue());
                tvSelCondition1.setText(mConditionBean.getAreaValue());
            }
            if (!TextUtils.isEmpty(mConditionBean.getSeats())) {
                tvSelCondition.setText(mConditionBean.getSeatsValue());
                tvSelCondition1.setText(mConditionBean.getSeatsValue());
            }
        } else {
            rlSelCondition.setVisibility(View.GONE);
            rlSelCondition1.setVisibility(View.GONE);
        }
        //是否隐藏筛选条件
        if (rlConstruction.getVisibility() == View.GONE &&
                rlOfficeType.getVisibility() == View.GONE &&
                rlSelCondition.getVisibility() == View.GONE) {
            rlLabelConstruction.setVisibility(View.GONE);
            rlLabelConstruction1.setVisibility(View.GONE);
        } else {
            rlLabelConstruction.setVisibility(View.VISIBLE);
            rlLabelConstruction1.setVisibility(View.VISIBLE);
        }
    }

    //删除交通
    @Click({R.id.ibt_delete_construction, R.id.ibt_delete_construction1})
    void deleteConstructionClick() {
        if (rlOfficeType.getVisibility() == View.GONE && rlSelCondition.getVisibility() == View.GONE) {
            rlLabelConstruction.setVisibility(View.GONE);
            rlLabelConstruction1.setVisibility(View.GONE);
        }
        rlConstruction.setVisibility(View.GONE);
        rlConstruction1.setVisibility(View.GONE);
        //清除条件
        if (hashSet != null) {
            hashSet.clear();
        }
        if (checkStates != null) {
            checkStates.clear();
        }
        line = "";
        nearbySubway = "";
        district = "";
        business = "";
        tvSearchArea.setText("区域");
        tvSearchArea.setTextColor(ContextCompat.getColor(mActivity, R.color.text_66));
        //清除神策选择文本
        Constants.SENSORS_AREA_CONTENT = "";
        Constants.SENSORS_DECORATION = "";
        //搜索
        getList();
    }

    //删除类型
    @Click({R.id.ibt_delete_office_type, R.id.ibt_delete_office_type1})
    void deleteTypeClick() {
        if (rlConstruction.getVisibility() == View.GONE && rlSelCondition.getVisibility() == View.GONE) {
            rlLabelConstruction.setVisibility(View.GONE);
            rlLabelConstruction1.setVisibility(View.GONE);
        }
        rlOfficeType.setVisibility(View.GONE);
        rlOfficeType1.setVisibility(View.GONE);
        //清除 初始化选择的写字楼或共享办公
        area = "";
        seats = "";
        dayPrice = "";
        decoration = "";
        houseTags = "";
        btype = 0;
        tvSearchOffice.setText(R.string.str_house_all);
        //清除筛选的面积或工位
        ConditionConfig.mConditionBean = null;
        if (rlConstruction.getVisibility() == View.GONE && rlOfficeType.getVisibility() == View.GONE) {
            rlLabelConstruction.setVisibility(View.GONE);
            rlLabelConstruction1.setVisibility(View.GONE);
        }
        rlSelCondition.setVisibility(View.GONE);
        rlSelCondition1.setVisibility(View.GONE);
        //搜索
        getList();
    }

    //删除工位或面积
    @Click({R.id.ibt_delete_sel_condition, R.id.ibt_delete_sel_condition1})
    void deleteSelConditionClick() {
        //清除筛选的面积或工位
        ConditionConfig.mConditionBean = null;
        if (rlConstruction.getVisibility() == View.GONE && rlOfficeType.getVisibility() == View.GONE) {
            rlLabelConstruction.setVisibility(View.GONE);
            rlLabelConstruction1.setVisibility(View.GONE);
        }
        rlSelCondition.setVisibility(View.GONE);
        rlSelCondition1.setVisibility(View.GONE);
        //清除 初始化选择的写字楼或共享办公
        area = "";
        seats = "";
        dayPrice = "";
        decoration = "";
        houseTags = "";
        //搜索
        getList();
    }

    @Override
    public void listItemClick(int position, int buildingId, int btype) {
        //神策
        SensorsTrack.visitBuildingDataPage(position, buildingId);
        //点击卡片
        SensorsTrack.clickCardShow(buildingId, String.valueOf(position), false);
    }
}
