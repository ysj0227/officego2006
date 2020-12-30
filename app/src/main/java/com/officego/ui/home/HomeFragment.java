package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.officego.R;
import com.officego.commonlib.base.BaseFragment;
import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.update.VersionDialog;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.OnLoadMoreListener;
import com.officego.h5.WebViewBannerActivity_;
import com.officego.h5.WebViewCouponActivity_;
import com.officego.ui.adapter.BrandAdapter;
import com.officego.ui.adapter.HomeAdapter;
import com.officego.ui.adapter.NewsAdapter;
import com.officego.ui.home.animation.CustomRotateAnim;
import com.officego.ui.home.contract.HomeContract;
import com.officego.ui.home.model.BannerBean;
import com.officego.ui.home.model.TodayReadBean;
import com.officego.ui.home.presenter.HomePresenter;
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
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@SuppressLint({"NewApi", "NonConstantResourceId", "ClickableViewAccessibility"})
@EFragment(R.layout.home_fragment)
public class HomeFragment extends BaseMvpFragment<HomePresenter> implements
        HomeContract.View, OnBannerListener, SwipeRefreshLayout.OnRefreshListener,
        NestedScrollView.OnScrollChangeListener {
    @ViewById(R.id.bga_refresh)
    SuperSwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.nsv_view)
    NestedScrollView nsvView;
    @ViewById(R.id.banner)
    Banner banner;
    @ViewById(R.id.rl_news)
    RelativeLayout rlNews;
    @ViewById(R.id.rv_news)
    RecyclerView rvNews;
    @ViewById(R.id.rv_brand)
    RecyclerView rvBrand;
    @ViewById(R.id.rv_hots)
    RecyclerView rvHots;
    @ViewById(R.id.iv_joint_work_flag)
    ImageView ivJointWorkFlag;
    @ViewById(R.id.iv_meeting_flag)
    ImageView ivMeetingFlag;
    @ViewById(R.id.rl_identity)
    RelativeLayout rlIdentity;
    @ViewById(R.id.iv_scroll_top)
    ImageView ivScrollTop;

    //设置左右移动
    private TranslateAnimation animationMove;
    private boolean isCloseIdentity;
    //轮播图
    private List<BannerBean.DataBean> mBannerClickList = new ArrayList<>();

    //暂无数据，网络异常 TODO
    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        mPresenter = new HomePresenter(mActivity);
        mPresenter.attachView(this);
        nsvView.setOnScrollChangeListener(this);
        initViews();
        initRefresh();
        new VersionDialog(mActivity);
        getData();
        testBrand();
        testHotsList();
        showAnimation();
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvNews.setLayoutManager(layoutManager);
        GridLayoutManager layoutManager1 = new GridLayoutManager(mActivity, 2);
        rvBrand.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(mActivity);
        rvHots.setLayoutManager(layoutManager2);
    }

    private void initRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(true, -20, 160);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.common_blue_main_80a, R.color.common_blue_main);
        //解决下拉刷新快速滑动crash
        rvHots.setOnTouchListener((view, motionEvent) -> mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing());
    }

    private void getData() {
        mPresenter.getTodayRead();
        mPresenter.getBannerList();
    }

    @Click({R.id.tv_all_house, R.id.btn_query_more})
    void moreHouseClick() {
        if (isFastClick(1200)) {
            return;
        }
        SearchHouseListActivity_.intent(mActivity)
                .searchKeywords("").start();
    }

    @Click(R.id.ctl_search)
    void searchClick() {
        if (isFastClick(1200)) {
            return;
        }
        SensorsTrack.searchButtonIndex();
        SearchRecommendActivity_.intent(mActivity).start();
    }

    @Click(R.id.iv_scroll_top)
    void scrollTopClick() {
        nsvView.fullScroll(NestedScrollView.FOCUS_UP);
    }

    @Click(R.id.iv_identity)
    void identityClick() {
        shortTip("aa");
    }

    @Click(R.id.iv_identity_close)
    void identityCloseClick() {
        isCloseIdentity = true;
        rlIdentity.clearAnimation();
        rlIdentity.setVisibility(View.GONE);
    }

    private void testBrand() {
        List<String> listBrand = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            listBrand.add("https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
        }
        rvBrand.setAdapter(new BrandAdapter(mActivity, listBrand));
    }

    private void testHotsList() {
        List<String> listBrand = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listBrand.add("https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
        }
        listBrand.add(2, "https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
        listBrand.add(3, "https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
        listBrand.add(5, "https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
        listBrand.add(7, "https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
        listBrand.add(9, "https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");

        rvHots.setAdapter(new HomeAdapter(mActivity, listBrand));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeAnimation();
    }

    //设置左右摇摆动画
    private void showAnimation() {
        CustomRotateAnim rotateAnim = new CustomRotateAnim();
        rotateAnim.setDuration(1500);
        rotateAnim.setRepeatCount(-1);
        ivJointWorkFlag.startAnimation(rotateAnim);
        ivMeetingFlag.startAnimation(rotateAnim);
    }

    //向右移动
    private void showRightAnimation() {
        animationMove = new TranslateAnimation(Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 200f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f);
        animationMove.setDuration(3000);
        animationMove.setRepeatCount(0);
        animationMove.setInterpolator(new AccelerateInterpolator());
        animationMove.setRepeatMode(Animation.RESTART);
        rlIdentity.startAnimation(animationMove);
    }

    //向左移动
    private void showLeftAnimation() {
        animationMove = new TranslateAnimation(Animation.ABSOLUTE, 200f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f);
        animationMove.setDuration(800);
        animationMove.setRepeatCount(0);
        animationMove.setInterpolator(new AccelerateInterpolator());
        animationMove.setRepeatMode(Animation.RESTART);
        animationMove.setFillAfter(true);
        rlIdentity.startAnimation(animationMove);
    }

    //关闭动画
    private void closeAnimation() {
        ivJointWorkFlag.clearAnimation();
        ivMeetingFlag.clearAnimation();
        rlIdentity.clearAnimation();
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //是否关闭了认证
        if (!isCloseIdentity) {
            if (scrollY == oldScrollY) {
                showRightAnimation();
            } else {
                showLeftAnimation();
            }
        }
        //滚动到顶
        if (scrollY == 0) {
            ivScrollTop.setVisibility(View.GONE);
        } else if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
            // 滚动到底
            ivScrollTop.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void endRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void todayReadSuccess(boolean isShowView, List<TodayReadBean.DataBean> dataList) {
        rlNews.setVisibility(isShowView ? View.VISIBLE : View.GONE);
        if (isShowView) {
            rvNews.setAdapter(new NewsAdapter(mActivity, dataList));
        }
    }

    @Override
    public void todayReadFail(boolean isShowView) {
        rlNews.setVisibility(View.GONE);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void bannerListSuccess(List<String> bannerList, List<BannerBean.DataBean> data) {
        mBannerClickList.clear();
        mBannerClickList.addAll(data);
        playBanner(bannerList);
    }

    @Override
    public void OnBannerClick(int position) {
        if (mBannerClickList != null) {
            int type = mBannerClickList.get(position).getType();
            int pageType = mBannerClickList.get(position).getPageType() == null ? 0 :
                    Integer.parseInt(CommonHelper.bigDecimal(mBannerClickList.get(position).getPageType(), true));
            int pageId = mBannerClickList.get(position).getPageId() == null ? 0 :
                    Integer.parseInt(CommonHelper.bigDecimal(mBannerClickList.get(position).getPageId(), true));
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

    //轮播图
    private void playBanner(List<String> bannerList) {
        if (bannerList == null || bannerList.size() == 0) {
            return;
        }
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
          banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new ImageLoaderUtils(mActivity));
        banner.setImages(bannerList);
        banner.setBannerAnimation(Transformer.Default);
        banner.setDelayTime(3000);
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                .setOnBannerListener(this)
                .start();
    }
}
