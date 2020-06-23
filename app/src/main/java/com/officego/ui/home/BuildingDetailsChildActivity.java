package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.LabelsView;
import com.officego.model.ShareBean;
import com.officego.ui.home.contract.BuildingDetailsChildContract;
import com.officego.ui.home.model.ChatsBean;
import com.officego.ui.home.model.HouseIdBundleBean;
import com.officego.ui.home.model.HouseOfficeDetailsBean;
import com.officego.ui.home.presenter.BuildingDetailsChildPresenter;
import com.officego.ui.message.ConversationActivity_;
import com.officego.utils.ImageLoaderUtils;
import com.officego.utils.WeChatUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/14.
 * Descriptions:
 **/
@SuppressLint("Registered")
@EActivity(R.layout.home_activity_house_details_child)
public class BuildingDetailsChildActivity extends BaseMvpActivity<BuildingDetailsChildPresenter>
        implements BuildingDetailsChildContract.View, NestedScrollView.OnScrollChangeListener {
    //title
    @ViewById(R.id.nsv_view)
    NestedScrollView nsvView;
    @ViewById(R.id.rl_root_house_title)
    RelativeLayout rlRootHouseTitle;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.ll_title)
    LinearLayout llTitle;
    //banner
    @ViewById(R.id.banner_image)
    Banner bannerImage;
    //名称
    @ViewById(R.id.tv_building_name)
    TextView tvBuildingName;
    //详情
    @ViewById(R.id.tv_independent_office)
    TextView tvIndependentOffice;
    @ViewById(R.id.tv_independent_office_area)
    TextView tvIndependentOfficeArea;
    @ViewById(R.id.tv_independent_office_area_text)
    TextView tvIndependentOfficeAreaText;
    @ViewById(R.id.tv_independent_office_price)
    TextView tvIndependentOfficePrice;
    @ViewById(R.id.tv_independent_office_price_text)
    TextView tvIndependentOfficePriceText;
    @ViewById(R.id.tv_independent_office_num)
    TextView tvIndependentOfficeNum;
    @ViewById(R.id.tv_independent_office_num_text)
    TextView tvIndependentOfficeNumText;
    //房源介绍
    @ViewById(R.id.tv_office_pattern)
    TextView tvOfficePattern;
    @ViewById(R.id.tv_orientation)
    TextView tvOrientation;
    @ViewById(R.id.tv_total_floor)
    TextView tvTotalFloor;
    @ViewById(R.id.tv_earliest_delivery)
    TextView tvEarliestDelivery;
    @ViewById(R.id.tv_rent_free_period)
    TextView tvRentFreePeriod;
    @ViewById(R.id.tv_minimum_lease)
    TextView tvMinimumLease;
    //介绍图
    @ViewById(R.id.iv_pattern)
    ImageView ivPattern;
    @ViewById(R.id.tv_pattern_description)
    TextView tvPatternDescription;
    //线路，特色
    @ViewById(R.id.rl_characteristic)
    RelativeLayout rlCharacteristic;
    @ViewById(R.id.label_house_characteristic)
    LabelsView labelHouseTags;
    @ViewById(R.id.tv_location)
    TextView tvLocation;
    @ViewById(R.id.tv_bus_line)
    TextView tvBusLine;
    //收藏取消
    @ViewById(R.id.tv_favorite)
    TextView tvFavorite;
    private String houseId = "";
    //是否已经收藏
    private boolean isFavorite;
    @Extra
    HouseIdBundleBean mChildHouseBean;
    private HouseOfficeDetailsBean mData;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new BuildingDetailsChildPresenter(context);
        mPresenter.attachView(this);
        nsvView.setOnScrollChangeListener(this);
        rlRootHouseTitle.setPadding(0, CommonHelper.statusHeight(this), 0, 0);
        tvIndependentOffice.setVisibility(View.GONE);
        mPresenter.getDetails(String.valueOf(mChildHouseBean.getBtype()), String.valueOf(mChildHouseBean.getHouseId()));
    }

    @Click(R.id.btn_back)
    void backClick() {
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void detailsSuccess(HouseOfficeDetailsBean data) {
        LogCat.e(TAG, "11111111111111= " + data.getHouse());
        mData = data;
        houseId = data.getHouse().getId() + "";
        //是否收藏
        isFavorite = data.isIsFavorite();
        isFavoriteView(isFavorite);
        //轮播图
        playBanner(data.getImgUrl());
        //楼盘信息
        tvTitle.setText(data.getHouse().getBuildingName());
        tvBuildingName.setText(data.getHouse().getBuildingName());
        if (data.getHouse() != null) {
            String seats = "0";
            if (data.getHouse().getSimple() != null && data.getHouse().getSimple().contains(",")) {
                String str1 = data.getHouse().getSimple().substring(0, data.getHouse().getSimple().indexOf(","));
                seats = data.getHouse().getSimple().substring(str1.length() + 1);
                tvIndependentOfficeAreaText.setText("最多" + seats + "个工位");
            } else {
                tvIndependentOfficeAreaText.setText(R.string.str_text_line);
            }
            //详情
            if (data.getHouse().getArea() != null) {
                tvIndependentOfficeArea.setText(Html.fromHtml("<font color='#46C3C2'>" + data.getHouse().getArea() + "</font>㎡"));
            } else {
                tvIndependentOfficeArea.setText(R.string.str_text_line);
            }
            if (data.getHouse().getDayPrice() != null) {
                tvIndependentOfficePrice.setText(Html.fromHtml("<font color='#46C3C2'>¥" + data.getHouse().getDayPrice() + "</font>/㎡/天起"));
            } else {
                tvIndependentOfficePrice.setText(R.string.str_text_line);
            }
            if (data.getHouse().getMonthPrice() != null) {
                tvIndependentOfficePriceText.setText("¥" + data.getHouse().getMonthPrice() + "/月");
            } else {
                tvIndependentOfficePriceText.setText(R.string.str_text_line);
            }
            if (data.getHouse().getDecoration() != null) {
                tvIndependentOfficeNum.setText(Html.fromHtml("<font color='#46C3C2'>" + data.getHouse().getDecoration() + "</font>"));
            } else {
                tvIndependentOfficeNum.setText(R.string.str_text_line);
            }
            tvIndependentOfficeNumText.setText("装修风格");
            //楼盘信息
            if (data.getHouse().getBasicInformation() != null) {
                tvOfficePattern.setText(TextUtils.isEmpty(data.getHouse().getBasicInformation().getOfficePattern()) ?
                        getResources().getString(R.string.str_text_line) : data.getHouse().getBasicInformation().getOfficePattern());
                tvTotalFloor.setText(TextUtils.isEmpty(data.getHouse().getBasicInformation().getFloor()) ?
                        getResources().getString(R.string.str_text_line) : data.getHouse().getBasicInformation().getFloor() + "层");
                tvEarliestDelivery.setText(TextUtils.isEmpty(data.getHouse().getBasicInformation().getEarliestDelivery()) ?
                        getResources().getString(R.string.str_text_line) : data.getHouse().getBasicInformation().getEarliestDelivery());
                tvRentFreePeriod.setText(TextUtils.isEmpty(data.getHouse().getBasicInformation().getRentFreePeriod()) ?
                        getResources().getString(R.string.str_text_line) : data.getHouse().getBasicInformation().getRentFreePeriod());
                tvMinimumLease.setText(TextUtils.isEmpty(data.getHouse().getBasicInformation().getMinimumLease()) ?
                        getResources().getString(R.string.str_text_line) : data.getHouse().getBasicInformation().getMinimumLease() + "年起");
                Glide.with(context).load(data.getHouse().getBasicInformation().getUnitPatternImg()).into(ivPattern);
                if (TextUtils.isEmpty(data.getHouse().getBasicInformation().getUnitPatternRemark())) {
                    tvPatternDescription.setVisibility(View.GONE);
                } else {
                    tvPatternDescription.setVisibility(View.VISIBLE);
                    tvPatternDescription.setText(data.getHouse().getBasicInformation().getUnitPatternRemark());
                }
            }
        }
        //交通
        trafficView(data);
        //特色
        labelHouseTags(data);
    }

    @Override
    public void favoriteChildSuccess() {
        isFavorite = !isFavorite;
        isFavoriteView(isFavorite);
    }

    //线路
    private void trafficView(HouseOfficeDetailsBean data) {
        if (data.getHouse() != null) {
            if (!TextUtils.isEmpty(data.getHouse().getAddress())) {
                tvLocation.setText(data.getHouse().getAddress());
            } else {
                tvLocation.setText(data.getHouse().getBusinessDistrict());
            }
            if (data.getHouse().getStationline() != null) {
                List<String> stationLine = data.getHouse().getStationline();
                List<String> stationName = data.getHouse().getStationNames();
                List<String> workTime = data.getHouse().getNearbySubwayTime();
                StringBuffer linePlan = new StringBuffer();
                for (int i = 0; i < stationLine.size(); i++) {
                    if (stationLine.size() == 1 || i == stationLine.size() - 1) {
                        linePlan.append("步行").append(workTime.get(i)).append("分钟到 | ")
                                .append(stationLine.get(i)).append("号线 ·").append(stationName.get(i));
                    } else {
                        linePlan.append("步行").append(workTime.get(i)).append("分钟到 | ")
                                .append(stationLine.get(i)).append("号线 ·").append(stationName.get(i)).append("\n");
                    }
                }
                tvBusLine.setText(linePlan);
            }
        }
    }

    //特色
    private void labelHouseTags(HouseOfficeDetailsBean data) {
        if (data.getHouse() != null && data.getHouse().getTags() != null && data.getHouse().getTags().size() > 0) {
            rlCharacteristic.setVisibility(View.VISIBLE);
            labelHouseTags.setLabels(data.getHouse().getTags(), (label, position, data1) -> data1.getDictCname());
        } else {
            rlCharacteristic.setVisibility(View.GONE);
        }
    }

    private List<String> mBannerList = new ArrayList<>();

    private void playBanner(List<HouseOfficeDetailsBean.ImgUrlBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (!TextUtils.isEmpty(list.get(i).getImgUrl())) {
                mBannerList.add(list.get(i).getImgUrl());
            }
        }
        bannerImage.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器，图片加载器在下方
        bannerImage.setImageLoader(new ImageLoaderUtils(context));
        //设置图片网址或地址的集合
        bannerImage.setImages(mBannerList);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        bannerImage.setBannerAnimation(Transformer.Default);
        //设置是否为自动轮播，默认是“是”。
        bannerImage.isAutoPlay(false);
        bannerImage.start();
    }

    //微信分享
    @Click(R.id.btn_share)
    void shareClick() {
        if (isFastClick(1200)) {
            return;
        }
        if (mData != null) {
            String dec = mData.getHouse().getDecoration() + "\n" + mData.getHouse().getAddress();
            ShareBean bean = new ShareBean();
            bean.setbType(mData.getHouse().getBtype());
            bean.setId("buildingId=" + mData.getHouse().getBuildingId() + "&houseId=" + mData.getHouse().getId());
            bean.setHouseChild(true);
            bean.setTitle(mData.getHouse().getBuildingName());
            bean.setDes(dec);
            bean.setImgUrl(mData.getHouse().getMainPic());
            bean.setDetailsUrl(mData.getHouse().getMainPic());
            new WeChatUtils(context, bean);
        }
    }

    //聊天
    @Click(R.id.btn_chat)
    void chatClick() {
        if (isFastClick(1500)) {
            return;
        }
        if (!NetworkUtils.isNetworkAvailable(context)) {
            shortTip(getString(R.string.str_check_net));
            return;
        }
        if (TextUtils.isEmpty(houseId)) {
            shortTip(R.string.tip_get_house_message_fail);
        } else {
            //获取对方的targetId
            mPresenter.gotoChat(houseId);
        }
    }

    //进入会话页面
    @Override
    public void chatSuccess(ChatsBean data) {
        if (data != null) {
            LogCat.e("TAG", "11111 details targetId: " + data.getTargetId());
            ConversationActivity_.intent(context).targetId(data.getTargetId() + "").start();
        }
    }

    @Override
    public void chatFail() {

    }

    @Click(R.id.tv_favorite)
    void tv_favoriteClick() {
        if (isFastClick(1200)) {
            return;
        }
        mPresenter.favoriteChild(houseId, isFavorite ? 1 : 0);
    }

    private void isFavoriteView(boolean isFavorite) {
        if (isFavorite) {
            tvFavorite.setText(R.string.str_collected);
            tvFavorite.setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
            tvFavorite.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context,
                    R.mipmap.ic_collect_selected), null, null);
        } else {
            tvFavorite.setText(R.string.str_collect);
            tvFavorite.setTextColor(ContextCompat.getColor(context, R.color.text_66));
            tvFavorite.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context,
                    R.mipmap.ic_collect_unselected), null, null);
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY > oldScrollY) {//向下滚动
            if (scrollY > getResources().getDimensionPixelSize(R.dimen.dp_230)) {
                llTitle.setVisibility(View.VISIBLE);
                rlRootHouseTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.common_blue_main));
            }
        } else if (scrollY < oldScrollY || scrollY == 0) {//向上滚动 scrollY == 0 滚动到顶
            if (scrollY < getResources().getDimensionPixelSize(R.dimen.dp_230)) {
                llTitle.setVisibility(View.GONE);
                rlRootHouseTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
            }
        }
    }
}
