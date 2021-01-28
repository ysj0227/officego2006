package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.analytics.GoogleTrack;
import com.officego.commonlib.common.analytics.SensorsTrack;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.dialog.MapDialog;
import com.officego.commonlib.common.dialog.WeChatShareDialog;
import com.officego.commonlib.common.model.BuildingIdBundleBean;
import com.officego.commonlib.common.model.ShareBean;
import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.IVideoPlayer;
import com.officego.commonlib.view.LabelsView;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.config.ConditionConfig;
import com.officego.h5.WebViewVRActivity_;
import com.officego.ui.adapter.BuildingInfoAdapter;
import com.officego.ui.adapter.HouseItemAllAdapter;
import com.officego.ui.adapter.IndependentAllChildAdapter;
import com.officego.ui.dialog.PreImageDialog;
import com.officego.ui.home.contract.BuildingDetailsContract;
import com.officego.ui.home.model.BuildingConditionItem;
import com.officego.ui.home.model.BuildingDetailsBean;
import com.officego.ui.home.model.BuildingDetailsChildBean;
import com.officego.ui.home.model.BuildingInfoBean;
import com.officego.ui.home.model.BuildingJointWorkBean;
import com.officego.ui.home.model.ChatsBean;
import com.officego.ui.home.model.ConditionBean;
import com.officego.ui.home.presenter.BuildingDetailsPresenter;
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
 * Descriptions: 楼盘，办公室详情
 **/
@SuppressLint({"Registered", "NonConstantResourceId"})
@EActivity(R.layout.home_activity_house_details)
public class BuildingDetailsActivity extends BaseMvpActivity<BuildingDetailsPresenter> implements
        OnBannerListener, BuildingDetailsContract.View, NestedScrollView.OnScrollChangeListener,
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
    //   名称
    @ViewById(R.id.tv_building_name)
    TextView tvBuildingName;
    //  独立办公室
    @ViewById(R.id.ctl_independent)
    ConstraintLayout ctlIndependent;
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
    //开放工位
    @ViewById(R.id.ctl_open_work)
    ConstraintLayout ctlOpenWork;
    @ViewById(R.id.tv_open_office_num)
    TextView tvOpenOfficeNum;
    @ViewById(R.id.tv_open_office_price)
    TextView tvOpenOfficePrice;
    @ViewById(R.id.tv_open_office_price_text)
    TextView tvOpenOfficePriceText;
    //线路，特色
    @ViewById(R.id.rl_route_map)
    RelativeLayout rlRouteMap;
    @ViewById(R.id.label_house_characteristic)
    LabelsView labelHouseTags;
    @ViewById(R.id.tv_location)
    TextView tvLocation;
    @ViewById(R.id.tv_bus_line)
    TextView tvBusLine;
    @ViewById(R.id.tv_query_trains)
    TextView tvQueryTrains;
    @ViewById(R.id.rl_characteristic)
    RelativeLayout rlCharacteristic;
    @ViewById(R.id.ctl_bus_line)
    ConstraintLayout ctlBusLine;
    //  开放工位 item
    @ViewById(R.id.rl_open_work_model)
    RelativeLayout rlOpenWorkModel;
    @ViewById(R.id.tv_open_work_model_num)
    TextView tvOpenWorkModelNum;
    @ViewById(R.id.tv_open_work_model_text)
    TextView tvOpenWorkModelText;
    @ViewById(R.id.tv_open_work_model_price)
    TextView tvOpenWorkModelPrice;
    @ViewById(R.id.tv_min_month)
    TextView tvMinMonth;
    //独立办公室，写字楼
    @ViewById(R.id.rl_independent_office_model)
    RelativeLayout rlIndependentOfficeModel;
    @ViewById(R.id.rl_independent_office_set_area)
    RelativeLayout rlIndependentOfficeSetArea;
    @ViewById(R.id.tv_item_list_top)
    TextView tvItemListTop;
    @ViewById(R.id.tv_item_list_bottom)
    TextView tvItemListBottom;
    @ViewById(R.id.iv_clear_set_area)
    ImageView ivClearSetArea;
    @ViewById(R.id.tv_independent_office_text)
    TextView tvIndependentOfficeText;
    @ViewById(R.id.rv_independent_office_all)
    RecyclerView rvHorizontalAll;
    @ViewById(R.id.rv_independent_office_child)
    RecyclerView rvIndependentOfficeChild;
    @ViewById(R.id.btn_query_more)
    Button btnQueryMore;
    //共享服务 图片
    @ViewById(R.id.rv_create_service)
    RecyclerView rvCreateService;
    @ViewById(R.id.rv_base_service)
    RecyclerView rvBaseService;
    //楼盘信息
    @ViewById(R.id.ctl_building_details)
    ConstraintLayout ctlBuildingDetails;
    @ViewById(R.id.tv_building_text)
    TextView tvBuildingText;
    @ViewById(R.id.rv_building_message_info)
    RecyclerView rvBuildingMessageInfo;
    @ViewById(R.id.rl_joint_company)
    RelativeLayout rlJointCompany;
    @ViewById(R.id.tv_company_info)
    TextView tvCompanyInfo;
    //收藏取消
    @ViewById(R.id.tv_favorite)
    TextView tvFavorite;
    @ViewById(R.id.rl_bottom_view)
    RelativeLayout rlBottomView;

    //是否在拖动进度条中，默认为停止拖动，true为在拖动中，false为停止拖动
    private boolean isDragging;
    //是否暂停，是否静音，是否初始化了截屏
    private boolean isPaused;
    //音量
    private int bufferingUpdate;
    private String videoUrl;
    // String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    //视频源是否旋转了
    private boolean isSetVideoRate;
    //跳转防止黑屏重新播放视频
    private boolean isRePlayVideo;
    //Video 消息处理
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
    BuildingIdBundleBean mBuildingBean;
    @Extra
    ConditionBean mConditionBean;
    //是否已经收藏
    private boolean isFavorite;
    //显示child子项列表
    private final List<BuildingDetailsChildBean.ListBean> childList = new ArrayList<>();
    private IndependentAllChildAdapter childAdapter;
    //当前页码
    private int pageNum = 1;
    private boolean hasMore;
    private BuildingDetailsBean mData;
    private String currentAreaValue = "";
    //初始化是否展开
    private boolean isExpand;
    //是否播放过视频
    private boolean isPlayedVideo;
    //是否关闭了筛选条件
    private boolean isClosedCondition;
    //神策是否已读
    private boolean isRead;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new BuildingDetailsPresenter(context);
        mPresenter.attachView(this);
        rlRootHouseTitle.setPadding(0, CommonHelper.statusHeight(this), 0, 0);
        nsvView.setOnScrollChangeListener(this);
        mConditionBean = ConditionConfig.getConditionBean;
        if (BundleUtils.buildingBean(this) != null) {//聊天插入楼盘点击
            mBuildingBean = BundleUtils.buildingBean(this);
        }
        if (BundleUtils.ownerBuildingBean(this) != null) {//业主首页进入详情
            mBuildingBean = BundleUtils.ownerBuildingBean(this);
            rlBottomView.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) nsvView.getLayoutParams();
            params.bottomMargin = 10;
            nsvView.setLayoutParams(params);
            if (BundleUtils.ownerIsOff(this)) {
                btnShare.setVisibility(View.GONE);
            }
        }
        initIndependentBuildingRecView();
        buildingIntroduceInfo();
        centerPlayIsShow(true);
        initVideo();
        getBuildingDetails();
    }

    private void initIndependentBuildingRecView() {
        tvIndependentOfficeText.setText("在租写字楼");
        LinearLayoutManager lmHorizontal = new LinearLayoutManager(this);
        lmHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHorizontalAll.setLayoutManager(lmHorizontal);
        rvIndependentOfficeChild.setLayoutManager(new LinearLayoutManager(this));
    }

    private void buildingIntroduceInfo() {
        tvBuildingText.setText(getString(R.string.str_building_details_info));
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        layoutManager.setSmoothScrollbarEnabled(true);
        rvBuildingMessageInfo.setLayoutManager(layoutManager);
    }

    private void getBuildingDetails() {
        if (BundleUtils.ownerBuildingBean(this) != null) {//业主首页进入详情
            mPresenter.getBuildingDetailsOwner(String.valueOf(mBuildingBean.getBtype()),
                    String.valueOf(mBuildingBean.getBuildingId()), BundleUtils.ownerIsTemp((Activity) context));
        } else {
            mPresenter.getBuildingDetails(String.valueOf(mBuildingBean.getBtype()), String.valueOf(mBuildingBean.getBuildingId()),
                    mConditionBean == null || TextUtils.isEmpty(mConditionBean.getAreaValue()) ? "" : mConditionBean.getAreaValue(),
                    mConditionBean == null || TextUtils.isEmpty(mConditionBean.getDayPrice()) ? "" : mConditionBean.getDayPrice(),
                    mConditionBean == null || TextUtils.isEmpty(mConditionBean.getDecoration()) ? "" : mConditionBean.getDecoration(),
                    mConditionBean == null || TextUtils.isEmpty(mConditionBean.getHouseTags()) ? "" : mConditionBean.getHouseTags(),
                    mConditionBean == null || TextUtils.isEmpty(mConditionBean.getSeatsValue()) ? "" : mConditionBean.getSeatsValue());
        }
    }

    //初始化中间播放按钮显示
    private void centerPlayIsShow(boolean isShow) {
        rlDefaultHousePic.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    //是否显示VR ,视频 ,图片按钮
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

    @Click(R.id.btn_back)
    void backClick() {
        //神策
        if (mBuildingBean != null) {
            SensorsTrack.visitBuildingDataPageComplete(mBuildingBean.getBuildingId(), isRead);
        }
        finish();
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
            if (mData.getVrUrl() != null && mData.getVrUrl().size() > 0) {
                String name;
                if (mData.getBuilding() != null && mData.getBuilding().getName() != null) {
                    name = mData.getBuilding().getName();
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

    //图片按钮
    @Click(R.id.rb_picture)
    void pictureClick() {
        playButtonIsShow(false);
        pauseVideo();
    }

    //暂停
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

    //导航
    @Click({R.id.tv_location, R.id.tv_bus_line})
    void mapClick() {
        if (mData != null) {
            new MapDialog(context, mData.getBuilding().getLatitude(),
                    mData.getBuilding().getLongitude(), mData.getBuilding().getAddress());
        }
    }

    //微信分享
    @Click(R.id.btn_share)
    void shareClick() {
        if (isFastClick(1200)) {
            return;
        }
        if (mData != null) {
            String dec = mData.getBuilding().getAddress();
            ShareBean bean = new ShareBean();
            bean.setbType(mBuildingBean.getBtype());
            bean.setId("buildingId=" + mData.getBuilding().getBuildingId());
            bean.setHouseChild(false);
            bean.setTitle(mData.getBuilding().getName());
            bean.setDes(dec);
            bean.setImgUrl(mData.getBuilding().getMainPic());
            bean.setDetailsUrl(mData.getBuilding().getMainPic());
            new WeChatShareDialog(context, bean);
        }
    }

    //收藏 0收藏 1取消
    @Click(R.id.tv_favorite)
    void favoriteClick() {
        if (isFastClick(1200)) {
            return;
        }
        if (mData == null) {
            return;
        }
        //未登录
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            new LoginTenantUtils(context);
            return;
        }
        if (mBuildingBean != null) {
            GoogleTrack.buildingCollect(context);
            //神策
            SensorsTrack.clickFavoritesButton(mBuildingBean.getBuildingId(), !isFavorite);
            //收藏
            mPresenter.favorite(mBuildingBean.getBuildingId() + "", isFavorite ? 1 : 0);
        }
    }

    // 0:单房东,1:多房东  判断是否单房东
    @Override
    public void chatSuccess(ChatsBean data) {
        if (data.getMultiOwner() == 0) {
            ConversationActivity_.intent(context).buildingId(mData.getBuilding().getBuildingId())
                    .targetId(data.getTargetId()).start();
        } else {
            CommonDialog dialog = new CommonDialog.Builder(context)
                    .setTitle(R.string.str_selected_owner_to_chat)
                    .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                        dialog12.dismiss();
                        scrollViewY();
                    }).create();
            dialog.showWithOutTouchable(false);
        }
    }

    //聊天
    @Click(R.id.btn_chat)
    void chatClick() {
        if (isFastClick(1200)) {
            return;
        }
        GoogleTrack.buildingDetailChat(context);
        //未登录
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            new LoginTenantUtils(context);
            return;
        }
        //判断是否单房东
        if (mData != null && mData.getBuilding() != null) {
            mPresenter.gotoChat(mData.getBuilding().getBuildingId() + "");
        }
    }

    //滚动指定view
    private void scrollViewY() {
        int[] position = new int[2];
        rlIndependentOfficeModel.getLocationOnScreen(position);
        nsvView.fling(position[1]);
        nsvView.smoothScrollTo(0, position[1]);
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

    //*******************************************

    //初始视频设置
    private void initVideo() {
        initScreenWidthHeight();
        if (!NetworkUtils.isNetworkAvailable(context)) {
            errorView();
        }
    }

    // 初始视频宽高
    private void initScreenWidthHeight() {
        int screenWidth = CommonHelper.getScreenWidth(context);
        ViewGroup.LayoutParams params = iVideoPlayer.getLayoutParams();
        params.width = screenWidth;
        params.height = screenWidth;
        iVideoPlayer.setLayoutParams(params);
    }

    // 初始化播放
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
        }, 300);
    }

    // 初始化video
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

    //播放异常重试
    @Click(R.id.tv_retry)
    void retryClick() {
        if (isFastClick(1500)) {
            return;
        }
        if (!NetworkUtils.isNetworkAvailable(context)) {
            shortTip(R.string.toast_network_error);
            return;
        }
        playVideo();
    }

    @Click(R.id.ib_play)
    void onPlayClick() {
        if (mData == null || iVideoPlayer == null) {
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

    //更新进度
    private void syncProgress(Object obj) {
        if (obj != null) {
            String strProgress = String.valueOf(obj);
            int progress = Integer.valueOf(strProgress);
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

    // 视频尺寸
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

    // 缓存状态
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

    // 播放完毕
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

    // 播放异常
    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        errorView();
        return false;
    }

    // 开始播放
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

    // Seek拖动完毕
    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
    }

    // 进度条滑动监听
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            String time = iVideoPlayer.generateTime(progress);
            tvCurrentPlayTime.setText(time);
        }
    }

    // 开始拖动
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isDragging = true;
        mHandler.removeMessages(IjkVideoConfig.VIDEO_MSG_PROGRESS);
    }

    // 停止拖动
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
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (iVideoPlayer != null) {
            iVideoPlayer.release();
        }
    }

    //**************************************
    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.independentAll};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (args == null) {
            return;
        }
        if (id == CommonNotifications.independentAll) {
            //传递的面积区间值
            currentAreaValue = (String) args[0];
            //请求当前楼盘下的列表 初始化list 和pageNum
            childList.clear();
            pageNum = 1;
            getChildBuildingList();
            //神策
            SensorsTrack.clickBuildingDataPageScreenButton(mBuildingBean.getBuildingId(), currentAreaValue, "");
        }
    }

    //获取详情下的独立办公室列表
    private void getChildBuildingList() {
        if (mConditionBean == null || TextUtils.isEmpty(mConditionBean.getArea()) || isClosedCondition) {
            showAllAreaView();//显示全部的面积view
        } else {
            showSetAreaView();//显示筛选面积的view
            currentAreaValue = mConditionBean.getArea();
            ivClearSetArea.setOnClickListener(v -> {
                isClosedCondition = true;
                showAllAreaView();
                currentAreaValue = "";
                childList.clear();
                mPresenter.getBuildingSelectList(pageNum, String.valueOf(mBuildingBean.getBtype()),
                        String.valueOf(mBuildingBean.getBuildingId()), currentAreaValue,
                        mConditionBean == null || TextUtils.isEmpty(mConditionBean.getDayPrice()) ? "" : mConditionBean.getDayPrice(),
                        mConditionBean == null || TextUtils.isEmpty(mConditionBean.getDecoration()) ? "" : mConditionBean.getDecoration(),
                        mConditionBean == null || TextUtils.isEmpty(mConditionBean.getHouseTags()) ? "" : mConditionBean.getHouseTags(),
                        mConditionBean == null || TextUtils.isEmpty(mConditionBean.getSeats()) ? "" : mConditionBean.getSeats());
            });
        }
        mPresenter.getBuildingSelectList(pageNum, String.valueOf(mBuildingBean.getBtype()),
                String.valueOf(mBuildingBean.getBuildingId()), currentAreaValue,
                mConditionBean == null || TextUtils.isEmpty(mConditionBean.getDayPrice()) ? "" : mConditionBean.getDayPrice(),
                mConditionBean == null || TextUtils.isEmpty(mConditionBean.getDecoration()) ? "" : mConditionBean.getDecoration(),
                mConditionBean == null || TextUtils.isEmpty(mConditionBean.getHouseTags()) ? "" : mConditionBean.getHouseTags(),
                mConditionBean == null || TextUtils.isEmpty(mConditionBean.getSeats()) ? "" : mConditionBean.getSeats());
    }

    private void showSetAreaView() {
        rvHorizontalAll.setVisibility(View.GONE);
        rlIndependentOfficeSetArea.setVisibility(View.VISIBLE);
        tvItemListTop.setText(TextUtils.isEmpty(mConditionBean.getAreaValue()) ? "" : mConditionBean.getAreaValue());
        tvItemListBottom.setText("套");
    }

    @UiThread
    void showAllAreaView() {
        rvHorizontalAll.setVisibility(View.VISIBLE);
        rlIndependentOfficeSetArea.setVisibility(View.GONE);
    }

    //详情
    @SuppressLint("SetTextI18n")
    @Override
    public void BuildingDetailsSuccess(BuildingDetailsBean data) {
        if (data == null) {
            return;
        }
        mData = data;
        showVrVideoImg(data);
        isFavorite = data.isIsFavorite();
        isFavoriteView(data.isIsFavorite());
        //轮播图
        playBanner(data.getImgUrl());
        //楼盘信息
        buildingInfo(data);
        //楼盘基础信息
        buildingBaseInfo(data);
        //公交
        showBusLine(data);
        //特色
        showTags(data);
        //独立办公室
        rvHorizontalAll.setAdapter(new HouseItemAllAdapter(context, DetailsUtils.buildingList(data)));
        //独立办公室子列表
        getChildBuildingList();
    }

    @Override
    public void BuildingTakeOff(String msg) {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(msg)
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    dialog12.dismiss();
                    finish();
                }).create();
        dialog.showWithOutTouchable(false);
        dialog.setCancelable(false);
    }

    //楼盘信息
    private void buildingInfo(BuildingDetailsBean data) {
        if (data.getBuilding() != null) {
            tvTitle.setText(data.getBuilding().getName());
            tvBuildingName.setText(data.getBuilding().getName());
            tvIndependentOfficeAreaText.setText("面积");
            tvIndependentOfficePriceText.setText("租金");
            tvIndependentOfficeNumText.setText("在租房源");
            String mArea = "0㎡";
            if (data.getBuilding().getMinArea() != null && data.getBuilding().getMaxArea() != null) {
                if (TextUtils.equals(data.getBuilding().getMinArea().toString(), data.getBuilding().getMaxArea().toString())) {
                    mArea = CommonHelper.bigDecimal(data.getBuilding().getMaxArea(), true) + "㎡";
                } else {
                    mArea = CommonHelper.bigDecimal(data.getBuilding().getMinArea(), true) + "~" +
                            CommonHelper.bigDecimal(data.getBuilding().getMaxArea(), true) + "㎡";
                }
            }
            CommonHelper.reSizeTextView(context, tvIndependentOfficeArea, mArea);
            String mPrice;
            if (data.getBuilding().getMinDayPrice() != null) {
                mPrice = "¥" + CommonHelper.bigDecimal(data.getBuilding().getMinDayPrice(), false) + "/㎡/天起";
            } else {
                mPrice = "¥0.0/㎡/天起";
            }
            CommonHelper.reSizeTextView(context, tvIndependentOfficePrice, mPrice);
            tvIndependentOfficeNum.setText(Html.fromHtml("<font color='#46C3C2'>" + data.getBuilding().getHouseCount() + "套" + "</font>"));
            //线路
            if (TextUtils.isEmpty(data.getBuilding().getAddress())) {
                tvLocation.setVisibility(View.GONE);
            } else {
                tvLocation.setVisibility(View.VISIBLE);
                tvLocation.setText(data.getBuilding().getAddress());
            }
            //神策
            SensorsTrack.buildingDataPageScreen(data.getBuilding().getBuildingId(), data.getBuilding().getHouseCount() + "");
        }
    }

    //楼盘基础信息
    private void buildingBaseInfo(BuildingDetailsBean data) {
        if (data != null && data.getIntroduction() != null) {
            //信息
            if (data.getIntroduction().getBuildingMsg() != null) {
                List<BuildingDetailsBean.IntroductionBean.BuildingMsgBean> list = data.getIntroduction().getBuildingMsg();
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
            //入住企业
            if (TextUtils.isEmpty(data.getIntroduction().getSettlementLicence())) {
                rlJointCompany.setVisibility(View.GONE);
            } else {
                rlJointCompany.setVisibility(View.VISIBLE);
                tvCompanyInfo.setText(data.getIntroduction().getSettlementLicence());
            }
        }
    }

    //顶部视频，VR，图片
    private void showVrVideoImg(BuildingDetailsBean data) {
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

    //公交
    private void showBusLine(BuildingDetailsBean data) {
        if (data != null && data.getBuilding() != null && data.getBuilding().getStationline() != null &&
                data.getBuilding().getStationline().size() > 0) {
            List<String> stationLine = data.getBuilding().getStationline();
            List<String> stationName = data.getBuilding().getStationNames();
            List<String> workTime = data.getBuilding().getNearbySubwayTime();
            ctlBusLine.setVisibility(View.VISIBLE);
            tvQueryTrains.setVisibility(data.getBuilding().getStationline().size() > 1 ? View.VISIBLE : View.GONE);
            tvBusLine.setText(DetailsUtils.routeLine(isExpand, stationLine, workTime, stationName));
        } else {
            ctlBusLine.setVisibility(View.GONE);
            tvQueryTrains.setVisibility(View.GONE);
        }
    }

    //tags
    private void showTags(BuildingDetailsBean data) {
        if (data != null && data.getTags() != null && data.getTags().size() > 0) {
            rlCharacteristic.setVisibility(View.VISIBLE);
            labelHouseTags.setLabels(data.getTags(), (label, position, data1) -> data1.getDictCname());
        } else {
            rlCharacteristic.setVisibility(View.GONE);
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
        showBusLine(mData);
    }

    @Override
    public void BuildingJointWorkDetailsSuccess(BuildingJointWorkBean data) {

    }

    @Override
    public void favoriteSuccess() {
        isFavorite = !isFavorite;
        isFavoriteView(isFavorite);
    }

    @Override
    public void favoriteFail() {
        shortTip(R.string.network_error);
    }

    @Click(R.id.btn_query_more)
    void queryMoreClick() {
        if (mData == null) {
            return;
        }
        if (hasMore) {
            pageNum++;
            getChildBuildingList();
        } else {
            shortTip(R.string.tip_no_more_data);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void buildingSelectListSuccess(int totals, List<BuildingDetailsChildBean.ListBean> list) {
        tvItemListBottom.setText(totals + "套");//自选面积多少套
        hasMore = list == null || list.size() > 9;
        btnQueryMore.setVisibility(hasMore ? View.VISIBLE : View.GONE);
        assert list != null;
        childList.addAll(list);
        if (childAdapter == null) {
            childAdapter = new IndependentAllChildAdapter(context, childList);
            rvIndependentOfficeChild.setAdapter(childAdapter);
        }
        childAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY > oldScrollY) {//向下滚动
            if (scrollY > getResources().getDimensionPixelSize(R.dimen.dp_230)) {
                llTitle.setVisibility(View.VISIBLE);
                rlRootHouseTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.common_blue_main));
            }
            //神策
            isRead = scrollY > CommonHelper.getScreenHeight(context) - getResources().getDimensionPixelSize(R.dimen.dp_100);
        } else if (scrollY < oldScrollY || scrollY == 0) {//向上滚动 scrollY == 0 滚动到顶
            if (scrollY < getResources().getDimensionPixelSize(R.dimen.dp_230)) {
                llTitle.setVisibility(View.GONE);
                rlRootHouseTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
            }
        }
    }

    /**
     * 轮播图
     */
    private final List<String> mBannerList = new ArrayList<>();

    private void playBanner(List<BuildingDetailsBean.ImgUrlBean> list) {
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
            DetailsUtils.bannerSet(context, bannerImage, mBannerList);
        }
    }

    @Override
    public void OnBannerClick(int position) {
        if (mBannerList == null || mBannerList.size() == 0) {
            return;
        }
        new PreImageDialog(context, (ArrayList<String>) mBannerList, position);
    }
}
