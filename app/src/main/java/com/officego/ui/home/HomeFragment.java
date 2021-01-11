package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.officego.R;
import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.analytics.SensorsTrack;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.update.VersionDialog;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.h5.WebViewMeetingActivity_;
import com.officego.ui.adapter.BrandAdapter;
import com.officego.ui.adapter.HomeAdapter;
import com.officego.ui.adapter.NewsAdapter;
import com.officego.ui.home.animation.CustomRotateAnim;
import com.officego.ui.home.contract.HomeContract;
import com.officego.ui.home.model.BannerBean;
import com.officego.ui.home.model.BrandRecommendBean;
import com.officego.ui.home.model.HomeHotBean;
import com.officego.ui.home.model.HomeMeetingBean;
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
    @ViewById(R.id.ctl_search)
    ConstraintLayout ctlSearch;
    @ViewById(R.id.iv_location)
    ImageView ivLocation;
    @ViewById(R.id.tv_location)
    TextView tvLocation;
    @ViewById(R.id.tv_search)
    TextView tvSearch;
    @ViewById(R.id.v_line_bottom)
    View vLineBottom;
    @ViewById(R.id.banner)
    Banner banner;
    @ViewById(R.id.rl_news)
    RelativeLayout rlNews;
    @ViewById(R.id.rl_brand)
    RelativeLayout rlBrand;
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
    private final List<BannerBean.DataBean> mBannerClickList = new ArrayList<>();

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        mPresenter = new HomePresenter(mSwipeRefreshLayout);
        mPresenter.attachView(this);
        nsvView.setOnScrollChangeListener(this);
        initViews();
        initRefresh();
        new VersionDialog(mActivity);
        getData();
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
        mSwipeRefreshLayout.setProgressViewOffset(true, 0, 220);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.common_blue_main_80a, R.color.common_blue_main);
        //解决下拉刷新快速滑动crash
        nsvView.setOnTouchListener((view, motionEvent) -> mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing());
    }

    private void getData() {
        mPresenter.getBannerList();
        mPresenter.getTodayRead();
        mPresenter.getBrandManagement();
        mPresenter.getHotList();
    }

    @Click(R.id.ctl_search)
    void searchClick() {
        if (isFastClick(1200)) {
            return;
        }
        SensorsTrack.searchButtonIndex();
        SearchRecommendActivity_.intent(mActivity).start();
    }

    @Click(R.id.rl_joint_work)
    void jointWorkClick() {
        goSearchActivity(Constants.SEARCH_JOINT_WORK);
    }

    @Click(R.id.rl_open_independent)
    void openSeatsClick() {
        goSearchActivity(Constants.SEARCH_OPEN_SEATS);
    }

    @Click(R.id.rl_office_type)
    void officeClick() {
        goSearchActivity(Constants.SEARCH_OFFICE);
    }

    @Click(R.id.rl_garden)
    void gardenClick() {
        goSearchActivity(Constants.SEARCH_GARDEN);
    }

    @Click(R.id.rl_meeting_room)
    void meetingClick() {
        WebViewMeetingActivity_.intent(mActivity).start();
    }

    //搜索列表
    private void goSearchActivity(int filterType) {
        SearchHouseListActivity_.intent(mActivity).filterType(filterType).start();
    }

    @Click({R.id.tv_all_house, R.id.btn_query_more})
    void moreHouseClick() {
        if (isFastClick(1200)) {
            return;
        }
        SearchHouseListActivity_.intent(mActivity).start();
    }

    @Click(R.id.iv_scroll_top)
    void scrollTopClick() {
        nsvView.fullScroll(NestedScrollView.FOCUS_UP);
    }

    @Click(R.id.iv_identity)
    void identityClick() {
        shortTip("H5认证");
    }

    @Click(R.id.iv_identity_close)
    void identityCloseClick() {
        isCloseIdentity = true;
        rlIdentity.clearAnimation();
        rlIdentity.setVisibility(View.GONE);
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
        } else if (scrollY >= getResources().getDimensionPixelSize(R.dimen.dp_720)) {
            ivScrollTop.setVisibility(View.VISIBLE);
        }
        //搜索底色
        if (scrollY > oldScrollY) {//向下滚动
            if (scrollY > getResources().getDimensionPixelSize(R.dimen.dp_200)) {
                StatusBarUtils.setStatusBarColor(mActivity);
                ctlSearch.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
                tvLocation.setTextColor(ContextCompat.getColor(mActivity, R.color.text_33));
                tvSearch.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.button_corners16_solid_gray));
                ivLocation.setBackgroundResource(R.mipmap.ic_location_black);
                vLineBottom.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.text_f7));
            }
        } else if (scrollY < oldScrollY || scrollY == 0) {//向上滚动 scrollY == 0 滚动到顶
            if (scrollY < getResources().getDimensionPixelSize(R.dimen.dp_200)) {
                StatusBarUtils.setStatusBarFullTransparent(mActivity);
                ctlSearch.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.transparent));
                tvLocation.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                tvSearch.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.button_corners16_solid_white));
                ivLocation.setBackgroundResource(R.mipmap.ic_location);
                vLineBottom.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.transparent));
            }
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
    public void todayReadFail() {
        rlNews.setVisibility(View.GONE);
    }

    @Override
    public void brandSuccess(boolean isShowView, List<BrandRecommendBean.DataBean> dataList) {
        rlBrand.setVisibility(isShowView ? View.VISIBLE : View.GONE);
        if (isShowView) {
            rvBrand.setAdapter(new BrandAdapter(mActivity, dataList));
        }
    }

    @Override
    public void brandFail() {
        rlBrand.setVisibility(View.GONE);
    }

    //1:楼盘重点版,2:楼盘文案版,3:网点版（三张图片）,4:房源特价
    @Override
    public void hotListSuccess(HomeMeetingBean.DataBean meetData, HomeHotBean.DataBean data) {
        List<HomeHotBean.DataBean.ListBean> list = data.getList();
        if (meetData != null && !TextUtils.isEmpty(meetData.getMeetingRoomLocation())) {
            list.add(Integer.parseInt(meetData.getMeetingRoomLocation()), null);
        }
        rvHots.setAdapter(new HomeAdapter(mActivity, meetData, list));
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
        if (mBannerClickList.size() > 0) {
            int type = mBannerClickList.get(position).getType();
            int pageType = mBannerClickList.get(position).getPageType() == null ? 0 :
                    Integer.parseInt(CommonHelper.bigDecimal(mBannerClickList.get(position).getPageType(), true));
            int pageId = mBannerClickList.get(position).getPageId() == null ? 0 :
                    Integer.parseInt(CommonHelper.bigDecimal(mBannerClickList.get(position).getPageId(), true));
            String wUrl = mBannerClickList.get(position).getWurl();
            //跳转
            BannerToActivity.toActivity(mActivity, type, pageType, pageId, wUrl);
        }
    }

    //轮播图
    private void playBanner(List<String> bannerList) {
        if (bannerList == null || bannerList.size() == 0) {
            return;
        }
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
//        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
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
