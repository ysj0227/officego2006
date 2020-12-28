package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.base.BaseFragment;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.update.VersionDialog;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.adapter.BrandAdapter;
import com.officego.ui.adapter.HomeAdapter;
import com.officego.ui.adapter.NewsAdapter;
import com.officego.ui.home.animation.CustomRotateAnim;

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
@SuppressLint({"NewApi", "NonConstantResourceId"})
@EFragment(R.layout.home_fragment)
public class HomeFragment extends BaseFragment implements
        NestedScrollView.OnScrollChangeListener {
    @ViewById(R.id.nsv_view)
    NestedScrollView nsvView;
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

    //暂无数据，网络异常 TODO
    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        nsvView.setOnScrollChangeListener(this);
        initViews();
        new VersionDialog(mActivity);
        testNews();
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

    private void testNews() {
        List<String> listBrand = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            listBrand.add("https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
        }
        rvNews.setAdapter(new NewsAdapter(mActivity, listBrand));
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
}
