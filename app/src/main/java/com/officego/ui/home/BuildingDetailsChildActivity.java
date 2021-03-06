package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.analytics.GoogleTrack;
import com.officego.commonlib.common.analytics.SensorsTrack;
import com.officego.commonlib.common.dialog.MapDialog;
import com.officego.commonlib.common.dialog.WeChatShareDialog;
import com.officego.commonlib.common.model.HouseIdBundleBean;
import com.officego.commonlib.common.model.ShareBean;
import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.IVideoPlayer;
import com.officego.commonlib.view.LabelsView;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.h5.WebViewVRActivity_;
import com.officego.ui.adapter.BuildingInfoAdapter;
import com.officego.ui.dialog.PreImageDialog;
import com.officego.ui.home.contract.BuildingDetailsChildContract;
import com.officego.ui.home.model.BuildingInfoBean;
import com.officego.ui.home.model.ChatsBean;
import com.officego.ui.home.model.HouseOfficeDetailsBean;
import com.officego.ui.home.presenter.BuildingDetailsChildPresenter;
import com.officego.ui.login.CommonLoginTenant;
import com.officego.ui.message.ConversationActivity_;
import com.officego.utils.video.IjkVideoConfig;
import com.officego.utils.video.IjkVideoUtils;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by YangShiJie
 * Data 2020/5/14.
 * Descriptions:
 **/
@SuppressLint({"Registered", "NonConstantResourceId"})
@EActivity(R.layout.home_activity_house_details_child)
public class BuildingDetailsChildActivity extends BaseMvpActivity<BuildingDetailsChildPresenter>
        implements OnBannerListener, BuildingDetailsChildContract.View,
        NestedScrollView.OnScrollChangeListener,
        SeekBar.OnSeekBarChangeListener,
        IMediaPlayer.OnBufferingUpdateListener,
        IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnSeekCompleteListener,
        IMediaPlayer.OnVideoSizeChangedListener,
        IMediaPlayer.OnInfoListener {
    //title
    @ViewById(R.id.nsv_view)
    NestedScrollView nsvView;
    @ViewById(R.id.rl_root_house_title)
    RelativeLayout rlRootHouseTitle;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.ll_title)
    LinearLayout llTitle;
    @ViewById(R.id.btn_share)
    LinearLayout btnShare;
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
    //房源介绍;
    @ViewById(R.id.rv_building_message_info)
    RecyclerView rvBuildingMessageInfo;
    //介绍图
    @ViewById(R.id.iv_pattern)
    ImageView ivPattern;
    @ViewById(R.id.tv_pattern_description)
    TextView tvPatternDescription;
    @ViewById(R.id.ctl_pattern_details)
    ConstraintLayout ctlPatternDetails;
    //线路，特色
    @ViewById(R.id.rl_characteristic)
    RelativeLayout rlCharacteristic;
    @ViewById(R.id.label_house_characteristic)
    LabelsView labelHouseTags;
    @ViewById(R.id.tv_location)
    TextView tvLocation;
    @ViewById(R.id.tv_bus_line)
    TextView tvBusLine;
    @ViewById(R.id.tv_query_trains)
    TextView tvQueryTrains;
    @ViewById(R.id.ctl_bus_line)
    ConstraintLayout ctlBusLine;
    //收藏取消
    @ViewById(R.id.tv_favorite)
    TextView tvFavorite;
    @ViewById(R.id.rl_bottom_view)
    RelativeLayout rlBottomView;
    //视频图片切换
    @ViewById(R.id.rb_vr)
    RadioButton rbVr;
    @ViewById(R.id.rb_video)
    RadioButton rbVideo;
    @ViewById(R.id.rb_picture)
    RadioButton rbPicture;
    @ViewById(R.id.rg_video_picture)
    RadioGroup rgVideoPicture;
    //视频
    @ViewById(R.id.ctl_video_play)
    ConstraintLayout ctlVideoPlay;
    @ViewById(R.id.rl_default_house_picture)
    RelativeLayout rlDefaultHousePic;
    @ViewById(R.id.iv_video_bg)
    ImageView ivVideoBg;
    @ViewById(R.id.ib_init_start)
    ImageButton ibInitStart;
    @ViewById(R.id.ivp_player)
    IVideoPlayer iVideoPlayer;
    @ViewById(R.id.ib_play)
    ImageButton ibPlay;
    @ViewById(R.id.tv_current_play_time)
    TextView tvCurrentPlayTime;
    @ViewById(R.id.tv_count_play_time)
    TextView tvCountPlayTime;
    @ViewById(R.id.tv_fail_tip)
    TextView tvFailTip;
    @ViewById(R.id.sb_bar)
    SeekBar sbBar;
    @ViewById(R.id.ll_play_fail)
    LinearLayout llPlayFail;
    @ViewById(R.id.rl_bottom_panel)
    RelativeLayout rlBottomPanel;
    @ViewById(R.id.ll_play_loading)
    LinearLayout llPlayLoading;

    //是否在拖动进度条中，默认为停止拖动，true为在拖动中，false为停止拖动
    private boolean isDragging;
    //是否暂停，是否静音，是否初始化了截屏
    private boolean isPaused;
    //音量
    private int bufferingUpdate;
    //视频源是否旋转了
    private boolean isSetVideoRate;
    //跳转防止黑屏重新播放视频
    private boolean isRePlayVideo;
    private String videoUrl;
    //String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    //消息处理
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == IjkVideoConfig.VIDEO_MSG_PROGRESS) {
                if (!isDragging) {
                    msg = obtainMessage(IjkVideoConfig.VIDEO_MSG_PROGRESS, iVideoPlayer.getCurrentPosition());
                    sendMessageDelayed(msg, IjkVideoConfig.VIDEO_DELAY_MILLIS);
                    syncProgress(msg.obj);
                }
            }
        }
    };
    //******************************
    @Extra
    HouseIdBundleBean mChildHouseBean;

    private String houseId = "";
    //是否已经收藏
    private boolean isFavorite;
    private HouseOfficeDetailsBean mData;
    //初始化是否展开
    private boolean isExpand;
    //是否播放过视频
    private boolean isPlayedVideo;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new BuildingDetailsChildPresenter(context);
        mPresenter.attachView(this);
        rlRootHouseTitle.setPadding(0, CommonHelper.statusHeight(this), 0, 0);
        setImageViewLayoutParams(context, ivPattern);
        if (BundleUtils.houseBean(this) != null) {//聊天插入楼盘点击
            mChildHouseBean = BundleUtils.houseBean(this);
        }
        if (BundleUtils.ownerHouseBean(this) != null) {//业主首页进入详情
            mChildHouseBean = BundleUtils.ownerHouseBean(this);
            rlBottomView.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) nsvView.getLayoutParams();
            params.bottomMargin = 10;
            nsvView.setLayoutParams(params);
            if (BundleUtils.ownerIsOff(this)) {
                btnShare.setVisibility(View.GONE);
            }
        }
        buildingIntroduceInfo();
        centerPlayIsShow(true);
        initVideo();
        nsvView.setOnScrollChangeListener(this);
        tvIndependentOffice.setVisibility(View.GONE);
        getDetails();
    }

    private void getDetails() {
        if (mChildHouseBean != null) {
            if (BundleUtils.ownerHouseBean(this) != null) {//业主首页进入详情
                mPresenter.getDetailsOwner(String.valueOf(mChildHouseBean.getBtype()),
                        String.valueOf(mChildHouseBean.getHouseId()), BundleUtils.ownerIsTemp((Activity) context));
            } else {
                mPresenter.getDetails(String.valueOf(mChildHouseBean.getBtype()), String.valueOf(mChildHouseBean.getHouseId()));
            }
            SensorsTrack.visitHouseDataPage(String.valueOf(mChildHouseBean.getHouseId())); //神策
        }
    }

    private void setImageViewLayoutParams(Context context, View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.height = CommonHelper.getScreenWidth(context) * 3 / 4;
        view.setLayoutParams(params);
    }

    private void buildingIntroduceInfo() {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        layoutManager.setSmoothScrollbarEnabled(true);
        rvBuildingMessageInfo.setLayoutManager(layoutManager);
    }

    //back
    @Click(R.id.btn_back)
    void backClick() {
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void detailsSuccess(HouseOfficeDetailsBean data) {
        mData = data;
        //视频
        showVrVideoImg(data);
        houseId = data.getHouse().getId() + "";
        //是否收藏
        isFavorite = data.isIsFavorite();
        isFavoriteView(isFavorite);
        //轮播图
        playBanner(data.getImgUrl());
        //楼盘信息
        houseInfo(data);
        //基础信息
        houseBaseInfo(data);
        //交通
        trafficView(data);
        //特色
        labelHouseTags(data);
    }

    @Override
    public void detailsFail(String msg) {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(msg)
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    dialog12.dismiss();
                    finish();
                }).create();
        dialog.showWithOutTouchable(false);
    }

    @Override
    public void favoriteChildSuccess() {
        isFavorite = !isFavorite;
        isFavoriteView(isFavorite);
    }

    //楼盘房源信息
    @SuppressLint("SetTextI18n")
    private void houseInfo(HouseOfficeDetailsBean data) {
        if (data.getHouse() != null) {
            String name = TextUtils.isEmpty(data.getHouse().getTitle())
                    ? data.getHouse().getBuildingName() : data.getHouse().getTitle();
            tvTitle.setText(name);
            tvBuildingName.setText(name);
            String seats;
            if (data.getHouse().getSimple() != null && data.getHouse().getSimple().contains(",")) {
                String str1 = data.getHouse().getSimple().substring(0, data.getHouse().getSimple().indexOf(","));
                seats = data.getHouse().getSimple().substring(str1.length() + 1);
                tvIndependentOfficeAreaText.setText("最多" + seats + "个工位");
            } else {
                tvIndependentOfficeAreaText.setText(R.string.str_text_line);
            }
            //详情
            if (data.getHouse().getArea() != null) {
                tvIndependentOfficeArea.setText(Html.fromHtml("<font color='#46C3C2'>" + CommonHelper.bigDecimal(data.getHouse().getArea(), true) + "</font>㎡"));
            } else {
                tvIndependentOfficeArea.setText(R.string.str_text_line);
            }
            if (data.getHouse().getDayPrice() != null) {
                CommonHelper.reSizeTextView(context, tvIndependentOfficePrice, "¥" + CommonHelper.bigDecimal(data.getHouse().getDayPrice(), false) + "/㎡/天起");
            } else {
                tvIndependentOfficePrice.setText(R.string.str_text_line);
            }
            if (data.getHouse().getMonthPrice() != null) {
                tvIndependentOfficePriceText.setText("¥" + CommonHelper.bigDecimal(data.getHouse().getMonthPrice(), false) + "/月");
            } else {
                tvIndependentOfficePriceText.setText(R.string.str_text_line);
            }
            if (data.getHouse().getDecoration() != null) {
                tvIndependentOfficeNum.setText(Html.fromHtml("<font color='#46C3C2'>" + data.getHouse().getDecoration() + "</font>"));
            } else {
                tvIndependentOfficeNum.setText(R.string.str_text_line);
            }
            tvIndependentOfficeNumText.setText("装修风格");
        }
    }

    //楼盘房源基础信息
    private void houseBaseInfo(HouseOfficeDetailsBean data) {
        if (data.getHouse() != null && data.getHouse().getBasicInformation() != null) {
            //户型介绍
            if (TextUtils.isEmpty(data.getHouse().getBasicInformation().getUnitPatternImg()) &&
                    TextUtils.isEmpty(data.getHouse().getBasicInformation().getUnitPatternRemark())) {
                ctlPatternDetails.setVisibility(View.GONE);
            } else {
                ctlPatternDetails.setVisibility(View.VISIBLE);
                //布局图
                if (TextUtils.isEmpty(data.getHouse().getBasicInformation().getUnitPatternImg())) {
                    ivPattern.setVisibility(View.GONE);
                } else {
                    ivPattern.setVisibility(View.VISIBLE);
                    Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(data.getHouse().getBasicInformation().getUnitPatternImg()).into(ivPattern);
                }
                //描述
                if (TextUtils.isEmpty(data.getHouse().getBasicInformation().getUnitPatternRemark())) {
                    tvPatternDescription.setVisibility(View.GONE);
                } else {
                    tvPatternDescription.setVisibility(View.VISIBLE);
                    tvPatternDescription.setText(data.getHouse().getBasicInformation().getUnitPatternRemark());
                }
            }
            //基础字段介绍信息
            if (data.getHouse().getBasicInformation().getHouseMsg() != null) {
                List<HouseOfficeDetailsBean.HouseBean.BasicInformationBean.HouseMsgBean> list =
                        data.getHouse().getBasicInformation().getHouseMsg();
                List<BuildingInfoBean> infoBeanList = new ArrayList<>();
                BuildingInfoBean bean;
                for (int i = 0; i < list.size(); i++) {
                    bean = new BuildingInfoBean();
                    bean.setName(list.get(i).getName());
                    bean.setValue(list.get(i).getValue());
                    infoBeanList.add(bean);
                }
                BuildingInfoAdapter infoAdapter = new BuildingInfoAdapter(context, infoBeanList);
                rvBuildingMessageInfo.setAdapter(infoAdapter);
            }
        }
    }

    //线路
    private void trafficView(HouseOfficeDetailsBean data) {
        if (data.getHouse() != null) {
            if (!TextUtils.isEmpty(data.getHouse().getAddress())) {
                tvLocation.setText(data.getHouse().getAddress());
            } else {
                tvLocation.setText(data.getHouse().getBusinessDistrict());
            }
            showBusLine();
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

    //公交
    private void showBusLine() {
        if (mData != null && mData.getHouse().getStationline() != null && mData.getHouse().getStationline().size() > 0) {
            List<String> stationLine = mData.getHouse().getStationline();
            List<String> stationName = mData.getHouse().getStationNames();
            List<String> workTime = mData.getHouse().getNearbySubwayTime();
            ctlBusLine.setVisibility(View.VISIBLE);
            tvQueryTrains.setVisibility(mData.getHouse().getStationline().size() > 1 ? View.VISIBLE : View.GONE);
            tvBusLine.setText(CommonDetails.routeLine(isExpand, stationLine, workTime, stationName));
        } else {
            ctlBusLine.setVisibility(View.GONE);
            tvQueryTrains.setVisibility(View.GONE);
        }
    }

    /**
     * 全部站点是否展开
     */
    @Click(R.id.tv_query_trains)
    void queryTrainsClick() {
        if (mData == null) {
            return;
        }
        Drawable down = ContextCompat.getDrawable(context, R.mipmap.ic_down_arrow_gray);
        Drawable up = ContextCompat.getDrawable(context, R.mipmap.ic_up_arrow_gray);
        isExpand = !isExpand;
        tvQueryTrains.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, isExpand ? up : down, null);
        showBusLine();
    }

    //导航
    @Click({R.id.tv_location, R.id.tv_bus_line})
    void mapClick() {
        if (mData != null) {
            new MapDialog(context, mData.getHouse().getLatitude(),
                    mData.getHouse().getLongitude(), mData.getHouse().getAddress());
        }
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
            new WeChatShareDialog(context, bean);
        }
    }

    //聊天
    @Click(R.id.btn_chat)
    void chatClick() {
        if (isFastClick(1500)) {
            return;
        }
        //google
        GoogleTrack.buildingHouseDetailChat(context);
        //未登录
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            new CommonLoginTenant(context);
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
            ConversationActivity_.intent(context).houseId(mData.getHouse().getId()).targetId(data.getTargetId()).start();
        }
    }

    @Override
    public void chatFail() {

    }

    //收藏
    @Click(R.id.tv_favorite)
    void tv_favoriteClick() {
        if (isFastClick(1200)) {
            return;
        }
        //未登录
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            new CommonLoginTenant(context);
            return;
        }
        GoogleTrack.buildingHouseCollect(context);
        //神策
        SensorsTrack.clickFavoritesButtonChild(houseId, !isFavorite);
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

    /**
     * video****************************
     */
    private void showVrVideoImg(HouseOfficeDetailsBean data) {
        if (data.getVideoUrl() != null && data.getVideoUrl().size() > 0) {
            videoUrl = data.getVideoUrl().get(0).getImgUrl();//video
        }
        if (data.getVrUrl() != null && data.getVrUrl().size() > 0 &&
                data.getVideoUrl() != null && data.getVideoUrl().size() > 0) {
            ibInitStart.setBackgroundResource(R.mipmap.ic_vr_play);
            rbVr.setChecked(true);
            rbVr.setVisibility(View.VISIBLE);
            rbVideo.setVisibility(View.VISIBLE);
            rbPicture.setVisibility(View.VISIBLE);
            radioGroupIsShow(true);
        } else if (data.getVrUrl() != null && data.getVrUrl().size() > 0) {
            ibInitStart.setBackgroundResource(R.mipmap.ic_vr_play);
            rbVr.setChecked(true);
            rbVr.setVisibility(View.VISIBLE);
            rbVideo.setVisibility(View.GONE);
            rbPicture.setVisibility(View.VISIBLE);
            radioGroupIsShow(true);
        } else if (data.getVideoUrl() != null && data.getVideoUrl().size() > 0) {
            ibInitStart.setBackgroundResource(R.mipmap.ic_video_start);
            rbVideo.setChecked(true);
            rbVr.setVisibility(View.GONE);
            rbVideo.setVisibility(View.VISIBLE);
            rbPicture.setVisibility(View.VISIBLE);
            radioGroupIsShow(true);
        } else {
            //没有视频只显示轮播图
            playButtonIsShow(false);
            centerPlayIsShow(false);
            radioGroupIsShow(false);
        }
    }

    //初始化中间播放按钮显示
    private void centerPlayIsShow(boolean isShow) {
        rlDefaultHousePic.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    //是否显示视频，图片按钮
    private void radioGroupIsShow(boolean isShow) {
        rgVideoPicture.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    //是否显示播放vr video按钮
    private void playButtonIsShow(boolean isShow) {
        if (isShow) {
            if (rbVr.isChecked()) {
                ibInitStart.setBackgroundResource(R.mipmap.ic_vr_play);
            } else if (rbVideo.isChecked()) {
                ibInitStart.setBackgroundResource(R.mipmap.ic_video_start);
            }
            ctlVideoPlay.setVisibility(View.VISIBLE);
            bannerImage.setVisibility(View.GONE);
        } else {
            ctlVideoPlay.setVisibility(View.GONE);
            bannerImage.setVisibility(View.VISIBLE);
        }
    }

    //vr显示
    @Click(R.id.rb_vr)
    void vrClick() {
        rlBottomPanel.setVisibility(View.GONE);
        centerPlayIsShow(true);
        playButtonIsShow(true);
        pauseVideo();
    }

    //视频按钮
    @Click(R.id.rb_video)
    void videoClick() {
        playButtonIsShow(true);
    }

    //开始播放中间按钮
    @Click(R.id.ib_init_start)
    void ibStartClick() {
        if (mData == null) {
            return;
        }
        if (rbVr.isChecked()) {
            if (mData != null && mData.getVrUrl() != null && mData.getVrUrl().size() > 0) {
                String name;
                if (mData.getHouse() != null && mData.getHouse().getBuildingName() != null) {
                    name = mData.getHouse().getBuildingName();
                } else {
                    name = "";
                }
                WebViewVRActivity_.intent(context).title(name).vrUrl(mData.getVrUrl().get(0).getImgUrl()).start();
            } else {
                shortTip(R.string.str_no_vr);
            }
        } else if (rbVideo.isChecked()) {
            playVideo();
        }
    }

    //图片按钮
    @Click(R.id.rb_picture)
    void pictureClick() {
        playButtonIsShow(false);
        pauseVideo();
    }

    //onPause
    @Override
    protected void onPause() {
        super.onPause();
        //pauseVideo();
        //释放-防止预加载退出后台时继续播放
        if (iVideoPlayer != null) {
            iVideoPlayer.release();
        }
    }

    //视频暂停
    private void pauseVideo() {
        if (iVideoPlayer != null && iVideoPlayer.isPlaying()) {
            iVideoPlayer.pauseVideo();
            isPaused = true;
            ibPlay.setBackgroundResource(R.mipmap.play_normal);
            radioGroupIsShow(true);
        }
    }

    /**
     * 初始视频设置
     */
    private void initVideo() {
        initScreenWidthHeight();
        if (!NetworkUtils.isNetworkAvailable(context)) {
            errorView();
        }
    }

    /**
     * 初始视频宽高
     */
    private void initScreenWidthHeight() {
        int screenWidth = CommonHelper.getScreenWidth(context);
        ViewGroup.LayoutParams params = iVideoPlayer.getLayoutParams();
        params.width = screenWidth;
        params.height = screenWidth;
        iVideoPlayer.setLayoutParams(params);
    }

    /**
     * 初始化播放
     */
    private void initVideoPlay() {
        if (TextUtils.isEmpty(videoUrl)) {
            errorView();
            return;
        }
        new Handler().postDelayed(() -> {
            isPlayedVideo = true;
            iVideoPlayer.load(videoUrl);
            setVideoListener();
            isPaused = false;
            ibPlay.setBackgroundResource(R.mipmap.pause_normal);
            sbBar.setProgress(0);
            tvCurrentPlayTime.setText(iVideoPlayer.generateTime(0));
        }, 200);
    }

    /**
     * 初始化video listener
     */
    private void setVideoListener() {
        sbBar.setOnSeekBarChangeListener(this);
        iVideoPlayer.setOnPreparedListener(this);
        iVideoPlayer.setOnBufferingUpdateListener(this);
        iVideoPlayer.setOnCompletionListener(this);
        iVideoPlayer.setOnErrorListener(this);
        iVideoPlayer.setOnSeekCompleteListener(this);
        iVideoPlayer.setOnVideoSizeChangedListener(this);
        iVideoPlayer.setOnInfoListener(this);
    }

    @Click(R.id.tv_retry)
    void retryClick() {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            shortTip(R.string.toast_network_error);
            return;
        }
        if (isFastClick(1500)) {
            return;
        }
        playVideo();
    }

    @Click(R.id.ib_play)
    void onPlayClick() {
        if (iVideoPlayer == null) {
            return;
        }
        if (isRePlayVideo) {
            isRePlayVideo = !isPlayedVideo;
            iVideoPlayer.seekTo(0);
            playVideo();
            return;
        }
        ibPlay.setBackgroundResource(isPaused ? R.mipmap.pause_normal : R.mipmap.play_normal);
        isPaused = !isPaused;
        if (isPaused) {
            iVideoPlayer.pauseVideo();
            radioGroupIsShow(true);
        } else {
            if (sbBar.getProgress() == sbBar.getMax()) {
                //循环播放
                iVideoPlayer.seekTo(0);
                if (!iVideoPlayer.isPlaying()) {
                    iVideoPlayer.startVideo();
                    radioGroupIsShow(false);
                }
                isDragging = false;
                mHandler.sendEmptyMessageDelayed(IjkVideoConfig.VIDEO_MSG_PROGRESS, IjkVideoConfig.VIDEO_DELAY_MILLIS);
            } else {
                iVideoPlayer.startVideo();
                radioGroupIsShow(false);
            }
        }
    }

    /**
     * 更新进度
     */
    private void syncProgress(Object obj) {
        if (obj != null) {
            String strProgress = String.valueOf(obj);
            int progress = Integer.parseInt(strProgress);
            if ((progress == 0)) {
                return;
            }
            long generateTime;
            if (progress + IjkVideoConfig.VIDEO_DELAY_MILLIS >= iVideoPlayer.getDuration()) {
                progress = sbBar.getMax();
                generateTime = sbBar.getMax();//毫秒
            } else {
                generateTime = (Long) obj;
            }
            sbBar.setProgress(progress);
            tvCurrentPlayTime.setText(iVideoPlayer.generateTime(generateTime));
        }
    }

    @UiThread
    void errorView() {
        rlBottomPanel.setVisibility(View.GONE);
        iVideoPlayer.setVisibility(View.GONE);
        llPlayFail.setVisibility(View.VISIBLE);
        llPlayLoading.setVisibility(View.GONE);
        tvFailTip.setText(getString(R.string.tip_video_play_exception));
        radioGroupIsShow(false);
    }

    private void isShowBottomView() {
        iVideoPlayer.setVisibility(View.VISIBLE);
        llPlayFail.setVisibility(View.GONE);
        rlBottomPanel.setVisibility(View.VISIBLE);
        llPlayLoading.setVisibility(View.GONE);
    }

    private void loadingView() {
        rlBottomPanel.setVisibility(View.GONE);
        iVideoPlayer.setVisibility(View.GONE);
        llPlayFail.setVisibility(View.GONE);
        llPlayLoading.setVisibility(View.VISIBLE);
        rlDefaultHousePic.setVisibility(View.GONE);
    }

    //视频尺寸
    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height, int i2, int i3) {
        if (isSetVideoRate) {//视频旋转后设置宽高
            IjkVideoUtils.setVideoLayout(context, iVideoPlayer, width, height);
        }
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        if (what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
            // 视频旋转了extra度，需要恢复
            isSetVideoRate = true;
            if (iVideoPlayer != null) {
                iVideoPlayer.setRotation(extra);
            }
        }
        return true;
    }

    /**
     * 缓存状态
     **/
    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
        if (iVideoPlayer != null) {
            bufferingUpdate = i;
            int onBufferingProgress;
            if (i >= IjkVideoConfig.VIDEO_BUFFERING_PROGRESS) {
                onBufferingProgress = (int) iVideoPlayer.getDuration();
            } else {
                onBufferingProgress = (int) (iVideoPlayer.getDuration() / 100 * i);
            }
            sbBar.setSecondaryProgress(onBufferingProgress);
        }
    }

    /**
     * 播放完毕
     **/
    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        if (iVideoPlayer != null) {
            isPaused = true;
            ibPlay.setBackgroundResource(R.mipmap.play_normal);
        }
        if (mHandler != null) {
            mHandler.removeMessages(IjkVideoConfig.VIDEO_MSG_PROGRESS);
        }
        if (bufferingUpdate == 0) {
            errorView();
        } else {
            sbBar.setProgress(sbBar.getMax());
            tvCurrentPlayTime.setText(iVideoPlayer.generateTime(sbBar.getMax()));
        }
        radioGroupIsShow(true);
    }

    /**
     * 播放异常
     **/
    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        errorView();
        return false;
    }


    /**
     * 开始播放
     **/
    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        if (iVideoPlayer != null) {
            isShowBottomView();
            iVideoPlayer.startVideo();
            //设置seekBar的最大限度值，当前视频的总时长（毫秒）
            long duration = iVideoPlayer.getDuration();
            //不足一秒补一秒
            if (duration % 1000 > 0) {
                duration = duration + (1000 - duration % 1000);
            }
            sbBar.setMax((int) duration);
            //视频总时长
            tvCountPlayTime.setText(Objects.requireNonNull(iVideoPlayer).generateTime(duration));
            //发送当前播放时间点通知
            mHandler.sendEmptyMessageDelayed(IjkVideoConfig.VIDEO_MSG_PROGRESS, IjkVideoConfig.VIDEO_DELAY_MILLIS);
        }
    }

    /**
     * Seek拖动完毕
     **/
    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
    }

    /**
     * 进度条滑动监听
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            String time = iVideoPlayer.generateTime(progress);
            tvCurrentPlayTime.setText(time);
        }
    }

    /**
     * 开始拖动
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isDragging = true;
        mHandler.removeMessages(IjkVideoConfig.VIDEO_MSG_PROGRESS);
    }

    /**
     * 停止拖动
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (iVideoPlayer != null) {
            iVideoPlayer.seekTo(seekBar.getProgress());
            if (!iVideoPlayer.isPlaying()) {
                iVideoPlayer.startVideo();
                isPaused = false;
                ibPlay.setBackgroundResource(R.mipmap.pause_normal);
            }
            isDragging = false;
            mHandler.sendEmptyMessageDelayed(IjkVideoConfig.VIDEO_MSG_PROGRESS, IjkVideoConfig.VIDEO_DELAY_MILLIS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPlayedVideo && mData != null) {
            if (rbVideo.isChecked()) {
                playVideo();
            } else {
                isRePlayVideo = true;
            }
        }
    }

    private void playVideo() {
        centerPlayIsShow(false);
        radioGroupIsShow(false);
        playButtonIsShow(true);
        loadingView();
        //重新初始化
        initVideoPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (iVideoPlayer != null) {
            iVideoPlayer.release();
        }
    }

    /**
     * 轮播图
     */
    private final List<String> mBannerList = new ArrayList<>();

    private void playBanner(List<HouseOfficeDetailsBean.ImgUrlBean> list) {
        mBannerList.clear();
        if (context != null && list != null && list.size() > 0) {
            //视频设置第一张图为默认背景
            Glide.with(context).applyDefaultRequestOptions(GlideUtils.bannerOptions())
                    .load(list.get(0).getImgUrl())
                    .into(ivVideoBg);
            for (int i = 0; i < list.size(); i++) {
                if (!TextUtils.isEmpty(list.get(i).getImgUrl())) {
                    mBannerList.add(list.get(i).getImgUrl());
                }
            }
            //banner set
            bannerImage.setOnBannerListener(this);
            CommonDetails.bannerSet(context, bannerImage, mBannerList);
        }
    }

    //查看大图
    @Override
    public void OnBannerClick(int position) {
        if (mBannerList == null || mBannerList.size() == 0) {
            return;
        }
        new PreImageDialog(context, (ArrayList<String>) mBannerList, position);
    }

    //户型介绍图
    @Click(R.id.iv_pattern)
    void patternImgClick() {
        if (mData != null && mData.getHouse() != null &&
                mData.getHouse().getBasicInformation() != null) {
            String url = mData.getHouse().getBasicInformation().getUnitPatternImg();
            ArrayList<String> mList = new ArrayList<>();
            mList.add(url);
            new PreImageDialog(context, mList, 0);
        }
    }
}
